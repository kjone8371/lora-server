package com.dipvision.lora.core.facility.image

import jakarta.persistence.*


@Entity
@Table(name = "tb_image")
class Image(

    @Column(nullable = false)
    var filename: String,  // 이미지 파일명

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0, // 이미지의 ID
)