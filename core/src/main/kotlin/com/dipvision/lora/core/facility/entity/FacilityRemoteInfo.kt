package com.dipvision.lora.core.facility.entity

import com.dipvision.lora.core.common.id.WrappedLong
import com.dipvision.lora.core.remote.entity.Remote
import jakarta.persistence.*

@Entity
@Table(name = "tb_facility_remote")
class FacilityRemoteInfo(
    @MapsId
    @OneToOne
    @JoinColumn(name = "id")
    val facility: Facility,

    @ManyToOne
    @JoinColumn(nullable = false)
    val remote: Remote,

    val phone: String,
    val number: Int,

    @Id
    val id: WrappedLong = WrappedLong.NULL,
)