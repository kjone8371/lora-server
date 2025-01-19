package com.dipvision.lora.core.facility.repository.elastic

import co.elastic.clients.elasticsearch._types.GeoLocation
import co.elastic.clients.elasticsearch._types.LatLonGeoLocation
import co.elastic.clients.elasticsearch._types.SortOrder
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders
import com.dipvision.lora.common.page.SlicedResponse
import com.dipvision.lora.core.common.pageable.ElasticPageRequest
import com.dipvision.lora.core.facility.entity.FacilityDocument
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.domain.SliceImpl
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

    // 새로 추가함
    override fun findByName(name: String, pageable: Pageable): Slice<FacilityDocument> {
        val queryString = QueryBuilders.queryString {
            it.defaultField(FacilityDocument::name.name)
                .query(name)
        }

        val query = NativeQuery.builder()
            .withQuery(queryString)
            .withSort {
                it.score { sort ->
                    sort.order(SortOrder.Desc) // 이름에 대한 일치도 기준으로 정렬
                }
            }
            .withPageable(pageable) // Pageable로 페이징 처리
            .build()

        val result = elasticsearchOperations.search(query, FacilityDocument::class.java)

        // 검색 결과를 리스트로 변환
        val data = result.map { it.content }.toList()

        // 총 결과 개수를 기준으로 hasNext를 계산
        val totalHits = result.totalHits
        val hasNext = totalHits > (pageable.pageNumber + 1) * pageable.pageSize

        return if (hasNext) {
            // 마지막 페이지가 아닌 경우에는 한 페이지 더 많은 항목을 반환
            SliceImpl(data.dropLast(1), pageable, hasNext)
        } else {
            // 마지막 페이지라면 그 자체로 반환
            SliceImpl(data, pageable, hasNext)
        }
    }


}