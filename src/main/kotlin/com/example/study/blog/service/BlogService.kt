package com.example.study.blog.service

import com.example.study.blog.dto.BlogDto
import com.example.study.blog.repository.WordRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono

@Service
class BlogService (
    private val wordRepository: WordRepository,
    @Value("\${KAKAO_RESTAPI_KEY}")
    private val restApiKey: String
) {

    /**
     * WebClient 를 사용한 kakao api 연동
     */
    fun searchKakao(blogDto: BlogDto): String? {

        val webClient = WebClient
            .builder()
            .baseUrl("https://dapi.kakao.com")
            .build()

        val response = webClient
            .get()
            .uri { it.path("/v2/search/blog")
                .queryParam("query", blogDto.query)
                .queryParam("sort", blogDto.sort)
                .queryParam("page", blogDto.page)
                .queryParam("size", blogDto.size)
                .build()
            }
            .header("Authorization","KakaoAK $restApiKey")
            .retrieve()
            .bodyToMono<String>()

        return response.block()
    }
}
