package com.gluco.Data.Converter

import com.gluco.Data.Remote.DataModels.TodoEntityDataModel
import com.gluco.Data.Local.TodoEntity

class TodoEntryConverter {
    companion object {
        fun convert(dataModel: TodoEntityDataModel) : TodoEntity {
            return TodoEntity(
                    id = dataModel.id,
                    text = dataModel.text,
                    updated = dataModel.updated,
                    status = dataModel.status
            )
        }

        fun convert(domainModel: TodoEntity) : TodoEntityDataModel {
            return TodoEntityDataModel(
                    id = domainModel.id,
                    text = domainModel.text,
                    updated = domainModel.updated,
                    status = domainModel.status
            )
        }

        fun convertListToDomain(data: List<TodoEntityDataModel>) : List<TodoEntity> {
            var rez = ArrayList<TodoEntity>()
            data.forEach {
                rez.add(convert(it))
            }
            return rez
        }

        fun convertListToData(data: List<TodoEntity>) : List<TodoEntityDataModel> {
            var rez = ArrayList<TodoEntityDataModel>()
            data.forEach {
                rez.add(convert(it))
            }
            return rez
        }
    }
}