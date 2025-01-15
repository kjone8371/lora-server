package com.dipvision.lora.core.facility.entity

import com.dipvision.lora.business.facility.consts.FacilityStatus
import com.dipvision.lora.business.facility.consts.FacilityType
import com.dipvision.lora.core.common.id.WrappedLong
import com.dipvision.lora.core.facility.image.Image
import jakarta.persistence.*
import org.springframework.web.multipart.MultipartFile
import org.w3c.dom.Text

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

    @Column(name = "meter_number", nullable = false)
    var meterNumber: String, // MeterNumber: "M45678"

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

    // 이미지 엔티티와의 관계 설정
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id", nullable = false)
    var image: Image,  // Image 엔티티를 참조

    @Column(nullable = false)
    var memo: String, // 메모

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: WrappedLong = WrappedLong.NULL, // Primary Key
) {
    companion object;
}