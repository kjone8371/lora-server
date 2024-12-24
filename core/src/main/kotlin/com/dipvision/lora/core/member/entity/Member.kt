package com.dipvision.lora.core.member.entity

import com.dipvision.lora.core.common.entity.BaseTimeEntity
import com.dipvision.lora.core.common.id.WrappedLong
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "tb_member")
class Member(
    @Column(unique = true, nullable = false)
    val credential: String,

    @Column(nullable = false)
    var password: String, // bcrypt

    @Column(nullable = false)
    val name: String,
    
    @Column(nullable = false)
    val lastPasswordModified: Instant = Instant.now(),

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: WrappedLong = WrappedLong.NULL,
) : BaseTimeEntity() {
    override fun toString(): String {
        return "Member(credential=$credential, password=TRUNCATED, name=$name, phone=$phone, group=${group?.name}, lastPasswordModified=$lastPasswordModified, id=${id.get})"
    }
}