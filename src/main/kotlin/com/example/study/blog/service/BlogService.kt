package com.example.study.blog.service

import com.example.study.blog.dto.BlogDto
import com.example.study.blog.repository.WordRepository
import org.springframework.stereotype.Service

@Service
class BlogService (
    private val wordRepository: WordRepository
) {
    fun searchKakao(blogDto: BlogDto): String? {
        println(blogDto.toString())
        return "SearchKakao"
    }
}