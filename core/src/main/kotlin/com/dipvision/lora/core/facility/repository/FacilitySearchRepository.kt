package com.dipvision.lora.core.facility.repository

import com.dipvision.lora.core.facility.repository.elastic.FacilityElasticQueryRepository
import com.dipvision.lora.core.facility.repository.elastic.FacilityElasticRepository
import org.springframework.stereotype.Repository

@Repository
interface FacilitySearchRepository : FacilityElasticRepository, FacilityElasticQueryRepository