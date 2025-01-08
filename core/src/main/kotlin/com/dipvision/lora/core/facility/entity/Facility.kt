package com.dipvision.lora.core.facility.entity

import com.dipvision.lora.business.facility.consts.FacilityStatus
import com.dipvision.lora.business.facility.consts.FacilityType
import com.dipvision.lora.core.common.id.WrappedLong
import jakarta.persistence.*

@Entity
@Table(name = "tb_facility")
class Facility(
    @Column(nullable = false)
    var name: String, // 관리 번호

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    var type: FacilityType, // 시설물 명칭

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    var status: FacilityStatus, // 시설 상태

    @Column(nullable = false)
    var address: String, // 시설물 주소
    @Column(nullable = false)
    var latitude: Double, // 위도
    @Column(nullable = false)
    var longitude: Double, // 경도

    @Column(name = "filter_one", nullable = true)
    val filter1: String? = null, // 필터1
    @Column(name = "filter_two", nullable = true)
    val filter2: String? = null, // 필터2

    @Column(name = "qr", nullable = true)
    val qr: String? = null, //QR 코드

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: WrappedLong = WrappedLong.NULL, //No
) {
    companion object;
}