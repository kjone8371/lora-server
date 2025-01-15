package com.dipvision.lora.business.facility.dto

data class FacilityInfoCreateDto (
    val lightingType: String,
    val memo: String,
    val image: String,
    val fixture: String,
    val poleFormat: String,
    val poleNumber: String,
    val department: String,
    val streetAddress: String,
    val meterNumber: String,
    val dimmer: String,
    val facilityId: Long
)