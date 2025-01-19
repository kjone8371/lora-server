package com.dipvision.lora.core.image.repository

import com.dipvision.lora.core.image.entity.Image
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ImageJpaRepository : JpaRepository<Image, UUID>