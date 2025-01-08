package com.dipvision.lora.api.auth.dto.request

import com.fasterxml.jackson.annotation.JsonCreator

data class NewTokenRequest @JsonCreator constructor(
    val refreshToken: String
)
