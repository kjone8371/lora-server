package com.dipvision.lora.core.member.entity

import com.dipvision.lora.core.common.entity.BaseTimeEntity
import com.dipvision.lora.core.common.id.WrappedLong
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.time.Instant

@Entity(name = "tb_member")
data class Member(
    @Column(unique = true, nullable = false)
    val credential: String,

    @Column(nullable = false)
    val password: String, // bcrypt

    @Column(nullable = false)
    val name: String,
    
    @Column(nullable = false)
    val lastPasswordModified: Instant = Instant.now(),

    @Id
    val id: WrappedLong = WrappedLong.NULL
) : BaseTimeEntity()
