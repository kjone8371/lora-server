package com.dipvision.lora.api.group.controller

import com.dipvision.lora.api.group.dto.request.GroupAddUserRequest
import com.dipvision.lora.api.group.dto.request.GroupCreateRequest
import com.dipvision.lora.api.group.dto.request.GroupPermissionEditRequest
import com.dipvision.lora.business.group.dto.GroupDto
import com.dipvision.lora.business.group.dto.GroupPreviewDto
import com.dipvision.lora.business.group.service.GroupService
import com.dipvision.lora.common.response.ResponseData
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/groups")
@SecurityRequirement(name = "Authorization")
@CrossOrigin(origins = ["https://1e5a-218-233-244-111.ngrok-free.app"])
class GroupController(
    private val groupService: GroupService,
) {
    @GetMapping
    @CrossOrigin(origins = ["https://1e5a-218-233-244-111.ngrok-free.app/groups"])
    fun getAllGroups(): ResponseEntity<ResponseData<List<GroupPreviewDto>>> {
        val groups = groupService.getAvailableGroups()
        return ResponseData.ok(data = groups)
    }
    
    @GetMapping("/{uuid}")
    @CrossOrigin(origins = ["https://1e5a-218-233-244-111.ngrok-free.app/groups/{uuid}"])
    fun getGroup(@PathVariable uuid: String): ResponseEntity<ResponseData<GroupDto>> {
        val dto = groupService.getGroupByUUID(uuid)
        return ResponseData.ok(data = dto)
    }

    @PatchMapping("/{groupId}/permissions")
    @CrossOrigin(origins = ["https://1e5a-218-233-244-111.ngrok-free.app/groups/{groupId}/permissions"])
    fun editGroupPermissions(
        @PathVariable groupId: String,
        @RequestBody request: GroupPermissionEditRequest,
    ): ResponseEntity<ResponseData<GroupDto>> {
        val group = groupService.editGroupPermissions(groupId, request.permissions)
        return ResponseData.ok(data = group)
    }

    @PostMapping
    @CrossOrigin(origins = ["https://1e5a-218-233-244-111.ngrok-free.app/groups"])
    fun addGroup(@Valid @RequestBody groups: GroupCreateRequest): ResponseEntity<ResponseData<GroupDto>> {
        val group = groupService.createGroup(groups.name, groups.permissions)
        return ResponseData.ok(data = group)
    }

    @PostMapping("/{groupId}/users")
    @CrossOrigin(origins = ["https://1e5a-218-233-244-111.ngrok-free.app/groups/{groupId}/users"])
    fun addUserToGroup(
        @PathVariable groupId: String,
        @RequestBody dto: GroupAddUserRequest,
    ): ResponseEntity<ResponseData<GroupDto>> {
        val group = groupService.addUserToGroup(groupId, dto.userId)
        return ResponseData.ok(data = group)
    }
    
    @DeleteMapping("/{groupId}/users/{userId}")
    @CrossOrigin(origins = ["https://1e5a-218-233-244-111.ngrok-free.app/groups/{groupId}/users/{userId}"])
    fun addUserToGroup(
        @PathVariable groupId: String,
        @PathVariable userId: Long
    ): ResponseEntity<ResponseData<GroupDto>> {
        val group = groupService.deleteUserFromGroup(groupId, userId)
        return ResponseData.ok(data = group)
    }
}
