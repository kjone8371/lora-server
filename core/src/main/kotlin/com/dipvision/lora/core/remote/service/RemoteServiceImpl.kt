package com.dipvision.lora.core.remote.service

import com.dipvision.lora.business.auth.service.AuthService
import com.dipvision.lora.business.facility.consts.RemoteProvider
import com.dipvision.lora.business.remote.dto.RemoteCreateDto
import com.dipvision.lora.business.remote.dto.RemoteDto
import com.dipvision.lora.business.remote.service.RemoteService
import com.dipvision.lora.core.remote.delegate.MqttConnection
import com.dipvision.lora.core.remote.entity.Remote
import com.dipvision.lora.core.remote.repository.RemoteJpaRepository
import com.dipvision.lora.core.remote.toDto
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true, rollbackFor = [Exception::class])
class RemoteServiceImpl(
    private val remoteJpaRepository: RemoteJpaRepository,
    private val mqttConnection: MqttConnection
) : RemoteService {
    @Transactional(rollbackFor = [Exception::class])
    override fun createRemote(dto: RemoteCreateDto): RemoteDto {
        val (
            type: RemoteProvider,
            address: String,
            port: Int,
            username: String?,
            password: String?,
        ) = dto

        val remote = remoteJpaRepository.save(
            Remote(
                type,
                address,
                port,
                username,
                password
            )
        )
        mqttConnection.createClient(remote)

        return remote.toDto()
    }

    override fun getRemotes(): List<RemoteDto> {
        val remotes = remoteJpaRepository.findAll()
        return remotes.map { it.toDto() }
    }
}