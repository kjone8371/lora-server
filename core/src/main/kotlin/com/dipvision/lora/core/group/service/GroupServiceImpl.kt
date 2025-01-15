package com.dipvision.lora.core.group.service

import com.dipvision.lora.business.group.dto.GroupDto
import com.dipvision.lora.business.group.dto.GroupPreviewDto
import com.dipvision.lora.business.group.service.GroupService
import com.dipvision.lora.common.permission.Permission
import com.dipvision.lora.common.permission.Permissions
import com.dipvision.lora.common.uuid.UUIDSafe
import com.dipvision.lora.core.common.permission.validate
import com.dipvision.lora.core.group.entity.Group
import com.dipvision.lora.core.group.findSafe
import com.dipvision.lora.core.group.repository.GroupRepository
import com.dipvision.lora.core.group.toDto
import com.dipvision.lora.core.member.MemberHolder
import com.dipvision.lora.core.member.findSafe
import com.dipvision.lora.core.member.repository.MemberJpaRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true, rollbackFor = [Exception::class])
class GroupServiceImpl(
    private val groupRepository: GroupRepository,
    private val memberRepository: MemberJpaRepository,
    
    private val memberHolder: MemberHolder
) : GroupService {
    override fun getAvailableGroups(): List<GroupPreviewDto> {
        val groups = groupRepository.findAll()
        return groups.map {
            GroupPreviewDto(it.id.get, it.name, it.permission.toList())
        }
    }

    override fun editGroupPermissions(groupId: String, permissions: List<Permissions>): GroupDto {
        memberHolder.getUserPermission().validate(Permissions.UPDATE_GROUP)
        
        val group = groupRepository.findSafe(UUIDSafe(groupId))

        val finalPermission = permissions
            .map { it.permission }
            .reduce(Permission::plus)

        val editedGroup = groupRepository.save(
            group.apply {
                permission = finalPermission
            }
        )
        return editedGroup.toDto()
    }

    @Transactional(rollbackFor = [Exception::class])
    override fun createGroup(name: String, initialPermission: List<Permissions>): GroupDto {
        memberHolder.getUserPermission().validate(Permissions.CREATE_GROUP)

        val finalPermission = initialPermission
            .map { it.permission }
            .reduce(Permission::plus)

        val group = groupRepository.save(
            Group(
                name = name,
                permission = finalPermission
            )
        )

        return group.toDto()
    }

    @Transactional(rollbackFor = [Exception::class])
    override fun addUserToGroup(groupId: String, userId: Long): GroupDto {
        memberHolder.getUserPermission().validate(Permissions.UPDATE_GROUP)

        val id = UUIDSafe(groupId)

        val member = memberRepository.findSafe(userId)
        val group = groupRepository.findSafe(id)

        val modified = groupRepository.save(
            group.apply {
                members.add(member)
            }
        )

        return modified.toDto()
    }
    
    override fun deleteUserFromGroup(groupId: String, userId: Long): GroupDto {
        memberHolder.getUserPermission().validate(Permissions.UPDATE_GROUP)

        val id = UUIDSafe(groupId)
        
        val member = memberRepository.findSafe(userId)
        val group = groupRepository.findSafe(id)

        val modified = groupRepository.save(
            group.apply {
                members.remove(member)
            }
        )
        
        return modified.toDto()
    }

    @Transactional(readOnly = true, rollbackFor = [Exception::class])
    override fun getGroupByUUID(uuid: String): GroupDto {
        val id = UUIDSafe(uuid)
        
        val group = groupRepository.findSafe(id)
        return group.toDto()
    }
}