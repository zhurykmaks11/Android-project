package com.example.lab1.models

class Budget {
    var limit: Double
    var category: Category


    constructor(limit: Double, category: Category) {
        this.limit = limit
        this.category = category
    }


    constructor(limit: Double) {
        this.limit = limit
        this.category = Category.OTHER
    }
}