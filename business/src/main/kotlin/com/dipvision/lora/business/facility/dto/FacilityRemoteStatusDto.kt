package com.dipvision.lora.business.facility.dto

import java.time.LocalDate
import java.time.LocalTime

data class FacilityRemoteStatusDto (
    val id: Long?,
    val powerOutageStatus: String?,
    val smpsOrStabilizer: String?,
    val lampFailureStatus: String?,
    val circuitBreakerStatus: String?,
    val acRelayStatus: String?,
    val magnetStatus: String?,
    val elbAStatus: String?,
    val elbBStatus: String?,
    val lightingTime: LocalTime?,
    val turningOffTime: LocalTime?,
    val lightingDeviation: Int?,
    val turningOffDeviation: Int?,
    val sunsetTime: LocalTime?,
    val sunriseTime: LocalTime?,
    val nightModeEnabled: Boolean?,
    val nightLightLevelSetting: Int?,
    val nightStartDate: LocalDate?,
    val nightEndDate: LocalDate?,
    val nightStartTime: LocalTime?,
    val nightEndTime: LocalTime?,
    val manager1Name: String?,
    val manager1Contact: String?,
    val manager2Name: String?,
    val manager2Contact: String?
)