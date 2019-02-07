package com.gluco.Data.Converter

import com.gluco.Data.Remote.DataModels.TaskDataModel
import com.gluco.Data.Local.TaskDomainModel
import com.gluco.Data.Remote.DataModels.NotesResponse

class TodoEntryConverter {
    companion object {

        fun convertPageResponse(response: NotesResponse) : List<TaskDomainModel> {
            var rez = ArrayList<TaskDomainModel>()
            response.tasks.forEach {
                rez.add(convert(it))
            }
            return rez
        }

        fun convert(dataModel: TaskDataModel) : TaskDomainModel {
            return TaskDomainModel(
                    id = dataModel.id,
                    text = dataModel.text,
                    updated = dataModel.updated,
                version = dataModel.version
            )
        }

        fun convert(domainModel: TaskDomainModel) : TaskDataModel {
            return TaskDataModel(
                    id = domainModel.id,
                    text = domainModel.text,
                    updated = domainModel.updated,
                version = domainModel.version
            )
        }
    }
}