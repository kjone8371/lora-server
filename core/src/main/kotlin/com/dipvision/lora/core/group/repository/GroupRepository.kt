package com.dipvision.lora.core.group.repository

import com.dipvision.lora.core.group.entity.Group
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface GroupRepository : JpaRepository<Group, UUID>