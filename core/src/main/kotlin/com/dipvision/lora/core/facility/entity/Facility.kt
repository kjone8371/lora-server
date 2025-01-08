package com.dipvision.lora.core.facility.entity

import com.dipvision.lora.business.facility.consts.FacilityStatus
import com.dipvision.lora.business.facility.consts.FacilityType
import com.dipvision.lora.core.common.id.WrappedLong
import jakarta.persistence.*

@Entity
@Table(name = "tb_facility")
class Facility(
    @Column(nullable = false)
    var name: String,

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    var type: FacilityType,

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    var status: FacilityStatus,

    @Column(nullable = false)
    var address: String,
    @Column(nullable = false)
    var latitude: Double,
    @Column(nullable = false)
    var longitude: Double,

    @Column(name = "filter_one", nullable = true)
    val filter1: String? = null,
    @Column(name = "filter_two", nullable = true)
    val filter2: String? = null,

    @Column(name = "qr", nullable = true)
    val qr: String? = null,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: WrappedLong = WrappedLong.NULL,
) {
    companion object;
}