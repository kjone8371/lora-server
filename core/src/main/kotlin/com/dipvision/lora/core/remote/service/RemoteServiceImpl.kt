package com.dipvision.lora.core.remote.service

import com.dipvision.lora.business.auth.service.AuthService
import com.dipvision.lora.business.facility.consts.RemoteProvider
import com.dipvision.lora.business.remote.dto.RemoteCreateDto
import com.dipvision.lora.business.remote.dto.RemoteDto
import com.dipvision.lora.business.remote.service.RemoteService
import com.dipvision.lora.core.remote.entity.Remote
import com.dipvision.lora.core.remote.repository.RemoteJpaRepository
import com.dipvision.lora.core.remote.toDto
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class RemoteServiceImpl(
    private val remoteJpaRepository: RemoteJpaRepository,
    private val authService: AuthService,
    private val passwordEncoder: PasswordEncoder,

    ) : RemoteService {
//    override fun createRemote(dto: RemoteCreateDto): RemoteDto {
//        val (
//            type: RemoteProvider,
//            address: String,
//            port: Int,
//            username: String?,
//            password: String?,
//        ) = dto
//
//        val remote = remoteJpaRepository.save(
//            Remote(
//                type,
//                address,
//                port,
//                username,
//                password
//            )
//        )
//
//        return remote.toDto()
//    }
//
//    override fun getRemotes(): List<RemoteDto> {
//        val remotes = remoteJpaRepository.findAll()
//        return remotes.map { it.toDto() }
//    }


    override fun createRemote(dto: RemoteCreateDto): RemoteDto {
        val (
            type: RemoteProvider,
            address: String,
            port: Int,
            username: String?,
            password: String?,
            phone: String?,
        ) = dto

        // Remote 저장
        val remote = remoteJpaRepository.save(
            Remote(
                type,
                address,
                port,
                username,
                passwordEncoder.encode(password),
                phone
            )
        )

        // tb_member에 회원 정보 저장
        if (username != null && password != null && phone != null) {
            authService.createNewMember(
                name = username,          // name에 username 전달
                credential = username,    // credential에 username 전달 (필요시 수정)
                password = password,
                phone = phone
            )
        }



        return remote.toDto()
    }

    override fun getRemotes(): List<RemoteDto> {
        val remotes = remoteJpaRepository.findAll()
        return remotes.map { it.toDto() }
    }
}