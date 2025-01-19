package com.dipvision.lora.common.protocol

enum class DeviceEvent(val offset: Int) {
    RESPONSE(0b00000001),
    CHANGE_SETTING(0b00000010),
    LIGHT_TOGGLED(0b00000100),
    NEED_TO_CHANGE_SETTING(0b00001000),
    BROKEN(0b00010000),
}