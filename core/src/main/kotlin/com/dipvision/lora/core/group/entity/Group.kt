package com.dipvision.lora.core.group.entity

import com.dipvision.lora.common.permission.Permission
import com.dipvision.lora.common.permission.Permissions
import com.dipvision.lora.core.common.id.WrappedUUID
import com.dipvision.lora.core.member.entity.Member
import jakarta.persistence.*

@Entity
@Table(name = "tb_group")
class Group(
    @Column(nullable = false)
    val name: String,

    @Column(nullable = false)
    var permission: Permission = Permissions.NOTHING.permission,

    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY, orphanRemoval = true)
    var members: MutableSet<Member> = mutableSetOf(),

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: WrappedUUID = WrappedUUID.NULL,
)