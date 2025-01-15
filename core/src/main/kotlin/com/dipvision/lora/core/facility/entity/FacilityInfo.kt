package com.dipvision.lora.core.facility.entity
import jakarta.persistence.*

@Entity
@Table(name = "tb_facility_info")
class FacilityInfo(

    @Column(name = "lighting_type", nullable = true)
    var lightingType: String? = null, // 조명 구분

    @Column(name = "memo", nullable = true)
    var memo: String? = null, // 메모

    @Column(name = "image", nullable = true)
    var image: String? = null, // 이미지

    @Column(name = "fixture", nullable = true)
    var fixture: String? = null, // 램프 종류

    @Column(name = "pole_format", nullable = true)
    var poleFormat: String? = null, // 등주 형식

    @Column(name = "pole_number", nullable = true)
    var poleNumber: String? = null, // 전주 번호

    @Column(name = "department", nullable = true)
    var department: String? = null, // 관리 부서

    @Column(name = "street_address", nullable = true)
    var streetAddress: String? = null, // 도로명 주소

    @Column(name = "meter_number", nullable = true)
    var meterNumber: String? = null, // 계기 번호

    @Column(name = "dimmer", nullable = true)
    var dimmer: String? = null, // 점멸기

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facility_id", nullable = false)
    var facility: Facility, // 시설

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null // 시설 상세 정보 고유 아이디
)


