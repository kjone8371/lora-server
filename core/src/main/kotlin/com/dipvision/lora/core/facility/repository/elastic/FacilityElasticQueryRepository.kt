package com.dipvision.lora.core.facility.repository.elastic

import com.dipvision.lora.common.page.SlicedResponse
import com.dipvision.lora.core.common.pageable.ElasticPageRequest
import com.dipvision.lora.core.facility.entity.FacilityDocument
import org.springframework.data.domain.Slice

import org.springframework.data.domain.Pageable

interface FacilityElasticQueryRepository {
    fun findByPoint(latitude: Double, longitude: Double, distance: Double): List<FacilityDocument>
    fun findByAddress(address: String, pageable: ElasticPageRequest): SlicedResponse<FacilityDocument>

    // 새로 추가함.
    fun findByName(name: String, pageable: Pageable): Slice<FacilityDocument>
}