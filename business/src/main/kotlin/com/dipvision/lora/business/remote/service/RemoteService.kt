package com.dipvision.lora.business.remote.service

import com.dipvision.lora.business.remote.dto.RemoteCreateDto
import com.dipvision.lora.business.remote.dto.RemoteDto

interface RemoteService {
    fun createRemote(dto: RemoteCreateDto): RemoteDto
    fun getRemotes(): List<RemoteDto>
}