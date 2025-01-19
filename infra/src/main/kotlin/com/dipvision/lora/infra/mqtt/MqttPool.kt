package com.dipvision.lora.infra.mqtt

import com.dipvision.lora.common.protocol.DPacket
import com.dipvision.lora.common.protocol.SPacket
import com.dipvision.lora.core.common.id.WrappedLong
import com.dipvision.lora.core.facility.entity.FacilityRemoteInfo
import com.dipvision.lora.core.remote.delegate.MqttConnection
import com.dipvision.lora.core.remote.entity.Remote
import com.dipvision.lora.core.remote.repository.RemoteJpaRepository
import io.github.davidepianca98.MQTTClient
import io.github.davidepianca98.mqtt.MQTTVersion
import io.github.davidepianca98.mqtt.packets.Qos
import jakarta.annotation.PostConstruct
import jakarta.annotation.PreDestroy
import kotlinx.coroutines.asCoroutineDispatcher
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.*
import java.util.concurrent.Executors

private val logger = LoggerFactory.getLogger(MqttPool::class.java)

@Component
@OptIn(ExperimentalUnsignedTypes::class)
class MqttPool(
    private val remoteJpaRepository: RemoteJpaRepository
) : MqttConnection {
    private final val mqttWorkerPool = Executors.newVirtualThreadPerTaskExecutor()
        .asCoroutineDispatcher()
    
    private final val queue: MutableMap<WrappedLong, MQTTClient> = Collections.synchronizedMap(mutableMapOf())
    
    @PreDestroy
    fun destroy() {
        mqttWorkerPool.close()
    }
    
    @PostConstruct
    fun queueAllClients() {
        for (remote in remoteJpaRepository.findAll()) {
            createClient(remote)
        }
    }
    
    fun createClient(remote: Remote) {
        val client = MQTTClient(
            MQTTVersion.MQTT5,
            tls = null,
            clientId = UUID.randomUUID().toString(),

            address = remote.address,
            port = remote.port,
            userName = remote.username,
            password = remote.password?.encodeToByteArray()?.toUByteArray(),
        ) {
            if (!it.topicName.endsWith("Data"))
                return@MQTTClient

            it.payload?.let { pl ->
                try {
                    val bytes = pl.toRawByteArray()
                    val packet = DPacket.deserialize(bytes)

                    logger.info("Packet Received: {}", packet)
                    // TODO: Track status change
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        queueMqttClient(client)
        queue[remote.id] = client
    }
    
    private fun UByteArray.toRawByteArray(): ByteArray {
        val arr = toByteArray().toMutableList()
        val list = arrayListOf<Byte>()

        while (arr.isNotEmpty()) {
            val m = "${arr.removeFirst().toInt().toChar()}${arr.removeFirst().toInt().toChar()}"
            list.add(m.toInt(16).toByte())
        }
        return list.toByteArray()
    }
    
    fun queueMqttClient(mqtt: MQTTClient) {
        mqtt.runSuspend(mqttWorkerPool)
    }

    override fun send(info: FacilityRemoteInfo, packet: SPacket) {
        val mqtt = queue[info.remote.id]!!
        
        mqtt.publish(
            false,
            Qos.AT_LEAST_ONCE,
            "TheOne/Server/${info.phone}/Control",
            payload = packet.serialize()
        )
    }
}