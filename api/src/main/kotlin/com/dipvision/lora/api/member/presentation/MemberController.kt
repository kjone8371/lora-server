package com.dipvision.lora.api.member.presentation

import com.dipvision.lora.api.member.dto.response.MemberInfoResponse
import com.dipvision.lora.business.member.service.MemberService
import com.dipvision.lora.common.response.ResponseData
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/member")
@SecurityRequirement(name = "Authorization")
class MemberController(
    private val memberService: MemberService
) {
    @GetMapping("/me")
    fun me(): ResponseEntity<ResponseData<MemberInfoResponse>> {
        val me = memberService.me()
        
        val data = MemberInfoResponse(
            me.name,
            me.phone,
            me.groupName,
            me.permissions,
            me.id
        )
        return ResponseData.ok(data = data)
    }
}
