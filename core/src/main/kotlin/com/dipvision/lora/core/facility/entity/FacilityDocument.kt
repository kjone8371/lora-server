package com.dipvision.lora.core.facility.entity

import org.springframework.data.elasticsearch.annotations.Document
import org.springframework.data.elasticsearch.annotations.Field
import org.springframework.data.elasticsearch.annotations.FieldType
import org.springframework.data.elasticsearch.annotations.Setting
import org.springframework.data.elasticsearch.core.geo.GeoPoint

@Document(indexName = "facility")
@Setting(settingPath = "elastic/nori.json")
class FacilityDocument(
    val id: Long,
    
    val name: String,
//    val type: FacilityType, // FIXME: add or remove if required
//    val status: FacilityStatus,

    @Field(type = FieldType.Search_As_You_Type, analyzer = "nori", searchAnalyzer = "nori")
    val address: String,
    val point: GeoPoint,
) {
    companion object {
        fun fromEntity(facility: Facility) = FacilityDocument(
            facility.id.get,

            facility.name,
//            facility.type,
//            facility.status,

            facility.address,
            GeoPoint(facility.longitude, facility.latitude),
        )
    }
}