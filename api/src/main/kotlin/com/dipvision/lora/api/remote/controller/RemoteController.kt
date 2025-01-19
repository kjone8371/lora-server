package com.dipvision.lora.api.remote.controller

import com.dipvision.lora.business.remote.dto.RemoteCreateDto
import com.dipvision.lora.business.remote.dto.RemoteDto
import com.dipvision.lora.business.remote.service.RemoteService
import com.dipvision.lora.common.response.ResponseData
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/remotes")
class RemoteController(
    private val remoteService: RemoteService

) {
    // Lora 통신하는 master 생성
    @PostMapping
    fun addRemote(request: RemoteCreateDto): ResponseEntity<ResponseData<RemoteDto>> {
        val data = remoteService.createRemote(request)
        return ResponseData.ok(data = data)
    }

    // 리스트에서 시설 통신 아이디 구해오기
    @GetMapping
    fun getRemotes(): ResponseEntity<ResponseData<List<RemoteDto>>> {
        val data = remoteService.getRemotes()
        return ResponseData.ok(data = data)
    }
}