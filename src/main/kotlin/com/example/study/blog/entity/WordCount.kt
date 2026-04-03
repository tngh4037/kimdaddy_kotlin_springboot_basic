package com.example.study.blog.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "WORDCOUNT")
class WordCount (
    @Id
    val word: String,
    val cnt: Int = 0
)