package com.dipvision.lora.core.remote.repository

import com.dipvision.lora.core.remote.entity.Remote
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Service

@Service
interface RemoteJpaRepository : JpaRepository<Remote, Long>{
    // 리모트 엔티티 비관적 잠금
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT r FROM Remote r WHERE r.id = :remoteId")
    fun lockRemote(remoteId: Long): Remote
}