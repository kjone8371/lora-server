package com.dipvision.lora.core.facility.repository.elastic

import com.dipvision.lora.core.facility.entity.FacilityDocument
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository
import org.springframework.stereotype.Repository

@Repository
interface FacilityElasticRepository : ElasticsearchRepository<FacilityDocument, Long> {
    fun findByName(name: String, pageable: Pageable): Slice<FacilityDocument>
}