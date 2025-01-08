package com.dipvision.lora.infra.elasticsearch

import org.apache.http.ssl.SSLContexts
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.DefaultResourceLoader
import org.springframework.data.elasticsearch.client.ClientConfiguration
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration
import java.security.KeyStore
import java.security.cert.CertificateFactory
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchProperties as SpringElasticsearchProperties


@Configuration
class  ElasticsearchConfig(
    private val elasticsearchProperties: ElasticsearchProperties,
    private val springElasticsearchProperties: SpringElasticsearchProperties,
) : ElasticsearchConfiguration() {
    override fun clientConfiguration(): ClientConfiguration {
        val builder = ClientConfiguration.builder()
            .connectedTo(*springElasticsearchProperties.uris.toTypedArray())

        if (elasticsearchProperties.isSsl) {
            val certificate = DefaultResourceLoader()
                .getResource(elasticsearchProperties.elasticCertPath)
                .inputStream.use {
                    CertificateFactory.getInstance("X.509").generateCertificate(it)
                }

            val keyStore = KeyStore.getInstance("pkcs12")
            keyStore.load(null, null)
            keyStore.setCertificateEntry("ca", certificate)

            val sslContext = SSLContexts.custom()
                .loadTrustMaterial(keyStore, null)
                .build()

            builder.usingSsl(sslContext)
        }

        return builder
            .withBasicAuth(springElasticsearchProperties.username, springElasticsearchProperties.password)
            .build()
    }
}