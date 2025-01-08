package com.dipvision.lora.core.facility.repository.elastic

import co.elastic.clients.elasticsearch._types.GeoLocation
import co.elastic.clients.elasticsearch._types.LatLonGeoLocation
import co.elastic.clients.elasticsearch._types.SortOrder
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders
import com.dipvision.lora.common.page.SlicedResponse
import com.dipvision.lora.core.common.pageable.ElasticPageRequest
import com.dipvision.lora.core.facility.entity.FacilityDocument
import org.springframework.data.elasticsearch.client.elc.NativeQuery
import org.springframework.data.elasticsearch.core.ElasticsearchOperations
import org.springframework.stereotype.Repository


@Repository
class FacilityElasticQueryRepositoryImpl(
    private val elasticsearchOperations: ElasticsearchOperations,
) : FacilityElasticQueryRepository {
    override fun findByPoint(latitude: Double, longitude: Double, distance: Double): List<FacilityDocument> {
        val geoQuery = QueryBuilders.geoDistance {
            it
                .distance("${distance}km")
                .location(
                    GeoLocation.Builder()
                        .latlon(
                            LatLonGeoLocation.Builder()
                                .lat(latitude)
                                .lon(longitude)
                                .build()
                        )
                        .build()
                )
                .field(FacilityDocument::point.name)
        }

        val boolQuery = QueryBuilders.bool {
            it.mustNot(geoQuery)
        }
        val query = NativeQuery.builder()
            .withQuery(boolQuery)
            .build()

        return elasticsearchOperations.search(query, FacilityDocument::class.java)
            .map { it.content }.toList()
    }

    override fun findByAddress(address: String, pageable: ElasticPageRequest): SlicedResponse<FacilityDocument> {
        val queryString = QueryBuilders.queryString {
            it.defaultField(FacilityDocument::address.name)
                .query(address)
        }

        val query = NativeQuery.builder()
            .withQuery(queryString)
            .withSort {
                it.score { sort ->
                    sort.order(SortOrder.Desc)
                }
            }
            .withPageable(ElasticPageRequest(pageable.page, pageable.size + 1))
            .build()

        val result = elasticsearchOperations.search(query, FacilityDocument::class.java)
        val data = result.map { it.content }.toList()
        
        val hasNext = pageable.size < data.size
        
        return SlicedResponse(hasNext, if (hasNext) pageable.size else data.size, if (hasNext) data.dropLast(1) else data)
    }
}