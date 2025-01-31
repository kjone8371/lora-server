package com.dipvision.lora.business.facility.dto

import com.dipvision.lora.business.facility.consts.FacilityType

data class FacilityExcelDto(
    val name: String,               // 시설물 명치
    val address: String,            // 도로명 주소
    val phoneNumber: String,        // 전화번호
    val type: FacilityType,         // 조명 구분 (예: 가로등)
    val poleFormat: String,         // 등기구 형태 (예: 기타)
    val fixture: String,            // 램프 종류 (예: 나트륨)
    val dimmer: String,             // 점멸기 (예: 양방향(CDMA))
    val escoStatus: String,         // ESCO (예: 해당없음)
    val powerConsumption: String,   // 소비 전력 (예: 15W)
    val billingType: String,        // 요금 형태 (예: 정액등)
    val department: String,         // 관리부서 (예: 더원)
    val poleNumber: String,         // 전주 번호
    val memo: String,               // 메모 (예: 없음)
)