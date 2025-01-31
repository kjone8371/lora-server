package com.dipvision.lora.core.facility.entity

import com.dipvision.lora.business.facility.consts.FacilityStatus
import com.dipvision.lora.business.facility.consts.FacilityType
import com.dipvision.lora.core.common.id.WrappedLong
import com.dipvision.lora.core.image.entity.Image
import jakarta.persistence.*

@Entity
@Table(name = "tb_facility")
data class Facility(
    @Column(name = "name", nullable = false)
    var name: String, // Name: "Downtown Plaza Light"

    @Column(name = "type", nullable = false)
    @Enumerated(value = EnumType.STRING)
    var type: FacilityType, // FacilityType for categorization

    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    var status: FacilityStatus?, // Status of the facility

    @Column(name = "address", nullable = false)
    var address: String, // StreetAddress: "101 Plaza Blvd, Cityville"

    @Column(name = "latitude", nullable = false)
    var latitude: Double, // Latitude: 35.8841013

    @Column(name = "longitude", nullable = false)
    var longitude: Double, // Longitude: 128.6354975

    @Column(name = "department", nullable = false)
    var department: String, // Department: "Plaza Lighting"

    @Column(name = "fixture", nullable = false)
    var fixture: String, // Fixture: "Lamp-post"

    @Column(name = "pole_format", nullable = false)
    var poleFormat: String, // PoleFormat: "Steel"

    @Column(name = "dimmer", nullable = false)
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

    @Column(name = "memo", nullable = false, length = 2000)
    var memo: String, // 메모

    // 전화번호 필드 - 010-1234-1234-00 형태
    @Column(name = "phone_number",nullable = false)
    var phoneNumber: String,  // 예: "010-1234-1234-00"

    // 에스코 상태
    @Column(name = "esco_status", nullable = false)
    var escoStatus: String,  // 예: "ACTIVE", "INACTIVE" 등

    // 전력 소비
    @Column(name = "power_consumption", nullable = false)
    var powerConsumption: String,  // 예: "250kWh"

    // 청구 유형
    @Column(name = "billing_type")
    var billingType: String,  // 예: "MONTHLY", "PAY-AS-YOU-GO"

    // 전주 번호
    @Column(name = "pole_number")
    var poleNumber: String,

    @OneToOne(orphanRemoval = true, cascade = [CascadeType.ALL], mappedBy = "facility")
    var remoteInfo: FacilityRemoteInfo? = null,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: WrappedLong = WrappedLong.NULL, // Primary Key
) {
    companion object;
}