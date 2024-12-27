package com.dipvision.lora.infra.elasticsearch

import org.springframework.context.annotation.Configuration
import org.springframework.core.io.DefaultResourceLoader
import org.springframework.data.elasticsearch.client.ClientConfiguration
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration
import java.security.KeyStore
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchProperties as SpringElasticsearchProperties


@Configuration
class ElasticsearchConfig(
    private val elasticsearchProperties: ElasticsearchProperties,
    private val springElasticsearchProperties: SpringElasticsearchProperties,
) : ElasticsearchConfiguration() {
    override fun clientConfiguration(): ClientConfiguration {
        val builder = ClientConfiguration.builder()
            .connectedTo(*springElasticsearchProperties.uris.toTypedArray())
        
        if (elasticsearchProperties.isSsl) {
            val keyStore = KeyStore.getInstance("pkcs12")
            DefaultResourceLoader().getResource(elasticsearchProperties.elasticCertPath).inputStream.use {
                keyStore.load(it, elasticsearchProperties.elasticCertPassword.toCharArray())
                keyStore
            }

            val trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
            trustManagerFactory.init(keyStore)

            val sslContext = SSLContext.getInstance("TLS")
            sslContext.init(null, trustManagerFactory.trustManagers, null)
            
            builder.usingSsl(sslContext)
        }
        
        return builder
            .withBasicAuth(springElasticsearchProperties.username, springElasticsearchProperties.password)
            .build()
    }
}