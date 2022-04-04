package com.rajendra.nestedrecyclerview.model

class AllCategory(var categoryTitle: String, categoryItemList: List<CategoryItem>) {

    var categoryItemList: List<CategoryItem>

    init {
        this.categoryItemList = categoryItemList
    }
}