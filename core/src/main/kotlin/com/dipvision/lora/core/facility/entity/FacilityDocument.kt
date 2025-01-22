package com.dipvision.lora.core.facility.entity

import org.springframework.data.elasticsearch.annotations.Document
import org.springframework.data.elasticsearch.annotations.Field
import org.springframework.data.elasticsearch.annotations.FieldType
import org.springframework.data.elasticsearch.annotations.Setting
import org.springframework.data.elasticsearch.core.geo.GeoPoint
import org.springframework.data.geo.Point



@Document(indexName = "facility")
@Setting(settingPath = "elastic/nori.json")
class FacilityDocument(
//    val type: FacilityType, // FIXME: add or remove if required
//    val status: FacilityStatus,

    val id: Long,

    val name: String,

    @Field(type = FieldType.Search_As_You_Type, analyzer = "nori", searchAnalyzer = "nori")
    val address: String,
    val point: GeoPoint?

) {
    companion object {
        fun fromEntity(facility: Facility) = FacilityDocument(
            facility.id.get,

            facility.name,
//            facility.type,
//            facility.status,

            facility.address,
            if (facility.latitude != null && facility.longitude != null) {
                GeoPoint(facility.latitude, facility.longitude)
            } else {
                null
            }
//            GeoPoint(facility.latitude, facility.longitude),
        )
    }
}