package com.dipvision.lora.core.facility.entity

import com.dipvision.lora.core.common.id.WrappedLong
import java.time.LocalDate
import java.time.LocalTime
import jakarta.persistence.*


@Entity
@Table(name = "tb_facility_remote_status")
data class FacilityRemoteStatus(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: WrappedLong = WrappedLong.NULL,

    @Column(name = "power_outage_status")
    val powerOutageStatus: String?,  // 정전 여부

    @Column(name = "smps_or_stabilizer")
    val smpsOrStabilizer: String?,  // SMPS/안정기

    @Column(name = "lamp_failure_status")
    val lampFailureStatus: String?,  // 램프 불량

    @Column(name = "circuit_breaker_status")
    val circuitBreakerStatus: String?,  // 누전 차단기

    @Column(name = "ac_relay_status")
    val acRelayStatus: String?,  // AC 릴레이

    @Column(name = "magnet_status")
    val magnetStatus: String?,  // 마그네트

    @Column(name = "elb_a_status")
    val elbAStatus: String?,  // ELB_A

    @Column(name = "elb_b_status")
    val elbBStatus: String?,  // ELB_B

    @Column(name = "lighting_time")
    val lightingTime: LocalTime?,  // 점등 시간

    @Column(name = "turning_off_time")
    val turningOffTime: LocalTime?,  // 소등 시간

    @Column(name = "lighting_deviation")
    val lightingDeviation: Int?,  // 점등 편차

    @Column(name = "turning_off_deviation")
    val turningOffDeviation: Int?,  // 소등 편차

    @Column(name = "sunset_time")
    val sunsetTime: LocalTime?,  // 일몰 시간

    @Column(name = "sunrise_time")
    val sunriseTime: LocalTime?,  // 일출 시간

    @Column(name = "night_mode_enabled")
    val nightModeEnabled: Boolean?,  // 심야 설정

    @Column(name = "night_light_level_setting")
    val nightLightLevelSetting: Int?,  // 심야 조도 설정

    @Column(name = "night_start_date")
    val nightStartDate: LocalDate?,  // 심야 시작 일자

    @Column(name = "night_end_date")
    val nightEndDate: LocalDate?,  // 심야 종료 일자

    @Column(name = "night_start_time")
    val nightStartTime: LocalTime?,  // 심야 시작 시간

    @Column(name = "night_end_time")
    val nightEndTime: LocalTime?,  // 심야 종료 시간

    @Column(name = "manager1_name")
    val manager1Name: String?,  // 관리자1 이름

    @Column(name = "manager1_contact")
    val manager1Contact: String?,  // 관리자1 연락처

    @Column(name = "manager2_name")
    val manager2Name: String?,  // 관리자2 이름

    @Column(name = "manager2_contact")
    val manager2Contact: String?  // 관리자2 연락처

)