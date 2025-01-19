package com.dipvision.lora.core.facility.entity

import com.dipvision.lora.core.common.id.WrappedLong
import com.dipvision.lora.core.remote.entity.Remote
import jakarta.persistence.*

@Entity
@Table(name = "tb_facility_remote")
class FacilityRemoteInfo(

//    @MapsId("id")

    @OneToOne
    @JoinColumn(name = "facility_id", nullable = false)
    val facility: Facility,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "remote_id", nullable = false)
    val remote: Remote,

    val phone: String,
    val number: Int,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: WrappedLong = WrappedLong.NULL,

)
