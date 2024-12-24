package com.dipvision.lora.core.member.entity

import com.dipvision.lora.core.common.entity.BaseTimeEntity
import com.dipvision.lora.core.common.id.WrappedLong
import com.dipvision.lora.core.group.entity.Group
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
    
    @ManyToOne
    @JoinColumn(name = "group_id", nullable = true)
    var group: Group? = null,

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