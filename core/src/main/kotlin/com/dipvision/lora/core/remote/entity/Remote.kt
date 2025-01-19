package com.dipvision.lora.core.remote.entity

import com.dipvision.lora.business.facility.consts.RemoteProvider
import com.dipvision.lora.core.common.id.WrappedLong
import jakarta.persistence.*

@Entity
@Table(name = "tb_remote")
class Remote(
    val provider: RemoteProvider,

    val address: String, // almost IoT Device uses mqtt protocol.
    val port: Int,
    val username: String?,
    val password: String?,
    val phone: String?,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: WrappedLong = WrappedLong.NULL,
)
