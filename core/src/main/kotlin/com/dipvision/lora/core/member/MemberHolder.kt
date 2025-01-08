package com.dipvision.lora.core.member

import com.dipvision.lora.common.permission.Permissions
import com.dipvision.lora.core.member.details.MemberDetails
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
class MemberHolder {
    fun get() = (SecurityContextHolder.getContext().authentication.principal as MemberDetails).member
    
    fun getUserPermission() = get().group?.permission ?: Permissions.NOTHING.permission
}
