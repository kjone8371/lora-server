package com.dipvision.lora.core.remote.delegate

import com.dipvision.lora.common.protocol.SPacket
import com.dipvision.lora.core.facility.entity.FacilityRemoteInfo

interface MqttConnection {
    fun send(info: FacilityRemoteInfo, packet: SPacket)
}