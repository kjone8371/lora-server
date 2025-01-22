package com.dipvision.lora.core.facility.entity

import com.dipvision.lora.business.facility.consts.FacilityStatus
import com.dipvision.lora.business.facility.consts.FacilityType
import com.dipvision.lora.core.common.id.WrappedLong
import com.dipvision.lora.core.image.entity.Image
import jakarta.persistence.*

@Entity
@Table(name = "tb_facility")
class Facility(
    @Column(nullable = false)
    var name: String, // Name: "Downtown Plaza Light"

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    var type: FacilityType, // FacilityType for categorization

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    var status: FacilityStatus, // Status of the facility

    @Column(nullable = false)
    var address: String, // StreetAddress: "101 Plaza Blvd, Cityville"

    @Column(nullable = false)
    var latitude: Double, // Latitude: 35.8841013

    @Column(nullable = false)
    var longitude: Double, // Longitude: 128.6354975

    @Column(nullable = false)
    var department: String, // Department: "Plaza Lighting"

    @Column(nullable = false)
    var fixture: String, // Fixture: "Lamp-post"

    @Column(name = "pole_format", nullable = false)
    var poleFormat: String, // PoleFormat: "Steel"

    @Column(nullable = false)
    var dimmer: String, // Dimmer: "Yes"

//    @Column(name = "filter_one", nullable = true)
//    val filter1: String? = null, // Additional filter
//
//    @Column(name = "filter_two", nullable = true)
//    val filter2: String? = null, // Additional filter
//
//    @Column(name = "qr", nullable = true)
//    val qr: String? = null, // QR code for identification


    @JoinColumn(name = "image_id", nullable = true)
    @ManyToOne(cascade = [CascadeType.DETACH])
    var image: Image?,

    @Column(nullable = false, length = 2000)
    var memo: String, // 메모

    // 전화번호 필드 - 010-1234-1234-00 형태
    @Column(name = "phone_number",nullable = false)
    var phoneNumber: String,  // 예: "010-1234-1234-00"

    // 에스코 상태
    @Column(nullable = false)
    var escoStatus: String,  // 예: "ACTIVE", "INACTIVE" 등

    // 전력 소비
    @Column(nullable = false)
    var powerConsumption: String,  // 예: "250kWh"

    // 청구 유형
    var billingType: String,  // 예: "MONTHLY", "PAY-AS-YOU-GO"


    @OneToOne(orphanRemoval = true, cascade = [CascadeType.ALL], mappedBy = "facility")
    var remoteInfo: FacilityRemoteInfo? = null,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: WrappedLong = WrappedLong.NULL, // Primary Key
) {
    companion object;
}
