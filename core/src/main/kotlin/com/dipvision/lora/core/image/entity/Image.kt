package com.dipvision.lora.core.image.entity

import com.dipvision.lora.core.common.id.WrappedUUID
import jakarta.persistence.*


@Table(name = "image")
@Entity
class Image(
    @Column(name = "file_name", nullable = false)
    val fileName: String,

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: WrappedUUID = WrappedUUID.NULL
)