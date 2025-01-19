package com.dipvision.lora.common.protocol

import java.time.LocalDate
import java.time.LocalTime

@OptIn(ExperimentalUnsignedTypes::class, ExperimentalStdlibApi::class)
sealed interface SPacket {
    companion object {
        private const val PROTOCOL_VERSION: Byte = 1
    }

    val id: Int
    val event: Byte

    fun serialize(): UByteArray

    data class SQueryPacket(override val id: Int) : SPacket {
        override val event: Byte = 0x01

        override fun serialize(): UByteArray {
            val byte = mutableListOf(
                PROTOCOL_VERSION,
                id.toByte(),
                event,
                0,

                0, 0,
                0, 0,
                0, 0,

                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
            )

            byte.addAll(computeChecksum(byte))

            return byte.toByteArray().toHexString(HexFormat.UpperCase).toByteArray().toUByteArray()
        }
    }

    data class SChangeSettingPacket(
        override val id: Int,
        val sunriseCorrection: Short,
        val sunsetCorrection: Short,
        
        val splitLightType: Byte,

        val splitLightStart: LocalDate,
        val splitLightEnd: LocalDate,
        val splitLightOn: LocalTime,
        val splitLightOff: LocalTime,
    ) : SPacket {
        override val event: Byte = 0x02

        override fun serialize(): UByteArray {
            val byte = mutableListOf(
                PROTOCOL_VERSION,
                id.toByte(),
                event,
                0,

                0, 0,
                *splitByte(sunriseCorrection).toTypedArray(),
                *splitByte(sunsetCorrection).toTypedArray(),

                splitLightType,
                splitLightStart.month.value.toByte(),
                splitLightStart.dayOfMonth.toByte(),
                splitLightEnd.month.value.toByte(),
                splitLightEnd.dayOfMonth.toByte(),
                splitLightOn.hour.toByte(),
                splitLightOn.minute.toByte(),
                splitLightOff.hour.toByte(),
                splitLightOff.minute.toByte(),
            )

            byte.addAll(computeChecksum(byte))
            
            val res = byte.toByteArray().toHexString(HexFormat.UpperCase)
            return res.toByteArray().toUByteArray()
        }
    }
    
    data class SLightTogglePacket(
        override val id: Int,
        val lightOn: Boolean,
        val rollbackTimeMinute: Short
    ) : SPacket {
        override val event: Byte = 0x04

        override fun serialize(): UByteArray {
            val byte = mutableListOf(
                PROTOCOL_VERSION,
                id.toByte(),
                event,
                if (lightOn) 0 else 1,

                *splitByte(rollbackTimeMinute).toTypedArray(),
                0, 0,
                0, 0,

                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
            )
            
            byte.addAll(computeChecksum(byte))
            val res = byte.toByteArray().toHexString(HexFormat.UpperCase)
            return res.toByteArray().toUByteArray()
        }
    }
}
private fun computeChecksum(bytes: List<Byte>): List<Byte> {
    var checksum: Int = bytes.sumOf { it.toUByte().toInt() }
    checksum = checksum and 0xFFFF // Ensure it is a 2-byte value
    return splitByte(checksum.toShort())
}

private fun splitByte(short: Short): List<Byte> {
    val first = (short.toInt() and 0xFF00) shr 8
    val second = short.toInt() and 0x00FF
    return listOf(first.toByte(), second.toByte())
}