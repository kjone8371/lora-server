package com.dipvision.lora.core.facility.repository.elastic

import co.elastic.clients.elasticsearch._types.GeoLocation
import co.elastic.clients.elasticsearch._types.LatLonGeoLocation
import co.elastic.clients.elasticsearch._types.SortOrder
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders
import com.dipvision.lora.common.page.SlicedResponse
import com.dipvision.lora.core.common.pageable.ElasticPageRequest
import com.dipvision.lora.core.facility.entity.Facility
import com.dipvision.lora.core.facility.entity.FacilityDocument
import com.dipvision.lora.core.facility.repository.FacilityJpaRepository
import org.springframework.data.domain.*
import org.springframework.data.elasticsearch.client.elc.NativeQuery
import org.springframework.data.elasticsearch.core.ElasticsearchOperations
import org.springframework.data.elasticsearch.core.SearchHits
import org.springframework.stereotype.Repository


@Repository
class FacilityElasticQueryRepositoryImpl(
    private val elasticsearchOperations: ElasticsearchOperations,
    private val facilityJpaRepository: FacilityJpaRepository
) : FacilityElasticQueryRepository {
    override fun findByPoint(latitude: Double, longitude: Double, distance: Double): List<FacilityDocument> {
        val geoQuery = QueryBuilders.geoDistance {
            it
                .distance("${distance}km") // 반경 거리 설정
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

        // 부정 쿼리가 아닌 'must' 조건으로 변경하여 실제 거리에 포함되는 시설들을 찾음
        val boolQuery = QueryBuilders.bool {
            it.must(geoQuery)  // 정확한 거리 내의 시설을 검색하기 위해 'must' 사용
        }

        // NativeQuery 설정
        val query = NativeQuery.builder()
            .withQuery(boolQuery)  // 'must' 조건을 포함한 쿼리
            .build()

        // 검색 결과 반환
        return elasticsearchOperations.search(query, FacilityDocument::class.java)
            .map { it.content }
            .toList()
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



    // 이름으로 검색하는 메서드
    override fun findByName(name: String, pageable: ElasticPageRequest): SlicedResponse<FacilityDocument> {
        // Match 쿼리를 사용하여 이름을 검색합니다.
        val matchQuery = QueryBuilders.match {
            it.field(FacilityDocument::name.name) // 'name' 필드를 검색
                .query(name) // 이름을 쿼리로 전달
        }

        // NativeQuery 설정
        val query = NativeQuery.builder()
            .withQuery(matchQuery) // match 쿼리 사용
            .withSort {
                it.score { sort ->
                    sort.order(SortOrder.Desc) // 점수 내림차순으로 정렬
                }
            }
            .withPageable(ElasticPageRequest(pageable.page, pageable.size + 1)) // 페이지 및 사이즈 설정
            .build()

        // 쿼리 실행
        val result = elasticsearchOperations.search(query, FacilityDocument::class.java)

        // 검색 결과
        val data = result.map { it.content }.toList()

        // hasNext 계산
        val hasNext = data.size > pageable.size
        return SlicedResponse(
            hasNext,
            if (hasNext) pageable.size else data.size,
            if (hasNext) data.dropLast(1) else data
        )
    }


}