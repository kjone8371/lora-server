package com.dipvision.lora.common.protocol

import java.time.LocalDate
import java.time.LocalTime

data class DPacket(
    val id: Byte,
    val channel: Byte,
    val battery: Byte,
    val event: List<DeviceEvent>,

    val status: Byte,
    val type: Byte,

    val controlTime: Int, // 4byte

    val sunrise: LocalTime,
    val sunset: LocalTime,

    val sunriseCorrection: Short,
    val sunsetCorrection: Short,

    val splitLightType: Byte,

    val splitLightStart: LocalDate,
    val splitLightEnd: LocalDate,
    val splitLightOff: LocalTime,
    val splitLightOn: LocalTime,

    val latitude: Float,
    val longitude: Float,

    val p2pCheck: Byte,
    val unsigned: Byte,

    val checksum: Short
) {
    companion object {
        private const val PROTOCOL_VERSION: Byte = 1
        
        fun deserialize(data: ByteArray): DPacket {
            val que = data.toMutableList()
            val protocol = que.popByte()
            if (protocol != PROTOCOL_VERSION) {
                throw IllegalArgumentException("Protocol version is not match. (received: $protocol, required: $PROTOCOL_VERSION)")
            }
            return DPacket(
                que.popByte(),
                que.popByte(),
                que.popByte(),
                que.popStatus(),
                
                que.popByte(),
                que.popByte(),
                que.popInt(),
                que.popTime(),
                que.popTime(),
                que.popShort(),
                que.popShort(),
                
                que.popByte(),

                que.popDate(),
                que.popDate(),
                que.popTime(),
                que.popTime(),

                que.popLocation(),
                que.popLocation(),

                que.popByte(),
                que.popByte(),

                que.popShort()
            )
        }

        private fun MutableList<Byte>.popStatus(): List<DeviceEvent> {
            val m = popByte().toInt()
            
            return DeviceEvent.entries.filter { m and it.offset shr it.ordinal == 1 }
        }
        
        private fun MutableList<Byte>.popByte() = removeFirst()
        private fun MutableList<Byte>.popShort() = ((removeFirst().toInt() shl 8) or removeFirst().toInt()).toShort()
        private fun MutableList<Byte>.popTime(): LocalTime = LocalTime.of(popByte().toInt(), popByte().toInt())
        private fun MutableList<Byte>.popDate(): LocalDate = LocalDate.of(0, popByte().toInt() + 1, popByte().toInt() + 1)
        private fun MutableList<Byte>.popInt(): Int {
            val first = removeFirst().toInt() shl 24
            val second = removeFirst().toInt() shl 16
            val third = removeFirst().toInt() shl 8
            val fourth = removeFirst().toInt() shl 0

            return first or second or third or fourth
        }

        private fun MutableList<Byte>.popLocation(): Float {
            val first = removeFirst().toInt() shl 24
            val second = removeFirst().toInt() shl 16
            val third = removeFirst().toInt() shl 8
            val fourth = removeFirst().toInt() shl 0

            return (first or second or third or fourth).toFloat() / 1_000_000f
        }
    }
}