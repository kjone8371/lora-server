package com.dipvision.lora.core.facility

import com.dipvision.lora.business.facility.dto.FacilityRemoteStatusDto
import com.dipvision.lora.core.facility.entity.FacilityRemoteStatus

fun FacilityRemoteStatus.toDto() = FacilityRemoteStatusDto (

        id.get,

        powerOutageStatus,
        smpsOrStabilizer,

        lampFailureStatus,
        circuitBreakerStatus,
        acRelayStatus,
        magnetStatus,
        elbAStatus,
        elbBStatus,

        lightingTime,
        turningOffTime,
        lightingDeviation,
        turningOffDeviation,

        sunsetTime,
        sunriseTime,
        nightModeEnabled,

        nightLightLevelSetting,
        nightStartDate,
        nightEndDate,
        nightStartTime,
        nightEndTime,

        manager1Name,
        manager1Contact,
        manager2Name,
        manager2Contact
)
