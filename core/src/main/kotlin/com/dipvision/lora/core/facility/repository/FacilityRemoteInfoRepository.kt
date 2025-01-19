package com.dipvision.lora.core.facility.repository

import com.dipvision.lora.core.facility.entity.FacilityRemoteInfo
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FacilityRemoteInfoRepository : JpaRepository<FacilityRemoteInfo, Long>