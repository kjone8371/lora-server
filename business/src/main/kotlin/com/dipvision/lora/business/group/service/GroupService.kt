package com.dipvision.lora.business.group.service

import com.dipvision.lora.business.group.dto.GroupDto
import com.dipvision.lora.business.group.dto.GroupPreviewDto
import com.dipvision.lora.common.permission.Permissions

interface GroupService {
    fun getAvailableGroups(): List<GroupPreviewDto>
    fun editGroupPermissions(groupId: String, permissions: List<Permissions>): GroupDto
    fun createGroup(name: String, initialPermission: List<Permissions>): GroupDto
    fun getGroupByUUID(uuid: String): GroupDto
    
    fun addUserToGroup(groupId: String, userId: Long): GroupDto
    fun deleteUserFromGroup(groupId: String, userId: Long): GroupDto
}