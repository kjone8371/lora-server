package com.dipvision.lora.core.common.pageable

import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort

class ElasticPageRequest(val page: Int, val size: Int) : Pageable {
    override fun getPageNumber(): Int = page

    override fun getPageSize(): Int = size

    override fun getOffset(): Long = this.pageNumber.toLong() * this.pageSize.toLong()

    override fun getSort(): Sort = Sort.unsorted()

    override fun next(): Pageable = ElasticPageRequest(page + 1, size)

    override fun previousOrFirst(): Pageable = if (hasPrevious()) previous() else first()


    private fun previous(): Pageable {
        return if (this.pageNumber == 0) this else ElasticPageRequest(page - 1, size)
    }    

    override fun first(): Pageable = ElasticPageRequest(0, size)

    override fun withPage(pageNumber: Int): Pageable = ElasticPageRequest(pageNumber, size)

    override fun hasPrevious(): Boolean {
        return this.pageNumber > 0
    }
}