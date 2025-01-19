package com.dipvision.lora.api.member.presentation

import com.dipvision.lora.api.member.dto.response.MemberInfoResponse
import com.dipvision.lora.business.member.service.MemberService
import com.dipvision.lora.common.response.ResponseData
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/member")
//@CrossOrigin(origins = ["https://4b78-218-233-244-111.ngrok-free.app", "https://the-one-led.vercel.app"])
@SecurityRequirement(name = "Authorization")
class MemberController(
    private val memberService: MemberService
) {
//    @CrossOrigin(origins = ["https://4b78-218-233-244-111.ngrok-free.app/member/me", "https://the-one-led.vercel.app/member/me"])
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
