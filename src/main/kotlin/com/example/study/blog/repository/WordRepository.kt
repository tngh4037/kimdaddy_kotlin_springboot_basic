package com.example.study.blog.repository

import com.example.study.blog.entity.WordCount
import org.springframework.data.repository.CrudRepository

interface WordRepository : CrudRepository<WordCount, String> {
    fun findTop10ByOrderByCntDesc(): List<WordCount> // 상위 10개의 WordCount 조회
}