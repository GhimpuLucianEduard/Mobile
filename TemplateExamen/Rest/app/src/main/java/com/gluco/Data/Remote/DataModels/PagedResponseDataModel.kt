package com.gluco.Data.Remote.DataModels

data class PagedResponseDataModel(
        val page: Int,
        val items: List<TodoEntityDataModel>,
        val more: Boolean
)