package com.dipvision.lora.core.remote.delegate

import com.dipvision.lora.common.protocol.SPacket
import com.dipvision.lora.core.facility.entity.FacilityRemoteInfo
import com.dipvision.lora.core.remote.entity.Remote

interface MqttConnection {
    fun send(info: FacilityRemoteInfo, packet: SPacket)
    fun createClient(remote: Remote)
    fun listen(info: FacilityRemoteInfo)
}