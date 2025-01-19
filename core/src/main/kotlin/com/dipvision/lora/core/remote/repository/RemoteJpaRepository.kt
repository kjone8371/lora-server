package com.dipvision.lora.core.remote.repository

import com.dipvision.lora.core.remote.entity.Remote
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Service

@Service
interface RemoteJpaRepository : JpaRepository<Remote, Long>