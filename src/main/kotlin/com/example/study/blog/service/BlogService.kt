package com.example.study.blog.service

import com.example.study.blog.dto.BlogDto
import com.example.study.blog.entity.WordCount
import com.example.study.blog.repository.WordRepository
import com.example.study.core.exception.InvalidInputException
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

        // validation check ( spring validation 을 통해 애노테이션 기반으로 처리되도록 수정 )
        // val msgList = mutableListOf<ExceptionMsg>()
        // if (blogDto.query.trim().isEmpty()) {
        //     msgList.add(ExceptionMsg.EMPTY_QUERY)
        // }
        // if (blogDto.sort.trim() !in arrayOf("accuracy", "recency")) {
        //     msgList.add(ExceptionMsg.NOT_IN_SORT)
        // }
        // when {
        //     blogDto.page < 1 -> msgList.add(ExceptionMsg.LESS_THAN_MIN)
        //     blogDto.page > 50 -> msgList.add(ExceptionMsg.MORE_THAN_MAX)
        // }
        // if (msgList.isNotEmpty()) {
        //     val message = msgList.joinToString { it.msg }
        //     throw InvalidInputException(message)
        // }

        // request kakao api
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

        // 검색어 순위 추가
        val lowQuery: String = blogDto.query.lowercase()
        val word: WordCount = wordRepository.findById(lowQuery).orElse(WordCount(lowQuery))
        word.cnt++
        wordRepository.save(word) // 카카오 api 요청한 검색어를 소문자 처리해서 WordCount 에 저장 ( 기존에 있다면 cnt 컬럼에 +1, 없으면 새로운 WordCount 생성 )

        return response.block()
    }

    /**
     * 검색어 상위 10개의 정보 조회
     */
    fun searchWordRank(): List<WordCount> = wordRepository.findTop10ByOrderByCntDesc()
}

private enum class ExceptionMsg(val msg: String) {
    EMPTY_QUERY("query parameter required"),
    NOT_IN_SORT("sort parameter one of accuracy and recency"),
    LESS_THAN_MIN("page is less than min"),
    MORE_THAN_MAX("page is more than max")
}