package com.dipvision.lora.infra.elasticsearch

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class ElasticsearchProperties(
    @Value("\${app.elasticsearch.is-ssl}")
    val isSsl: Boolean,
    @Value("\${app.elasticsearch.certificate}")
    val elasticCertPath: String,
    @Value("\${app.elasticsearch.cert-password}")
    val elasticCertPassword: String
)