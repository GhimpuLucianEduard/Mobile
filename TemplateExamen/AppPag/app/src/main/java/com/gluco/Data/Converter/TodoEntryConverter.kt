package com.gluco.Data.Converter

import com.gluco.Data.Remote.DataModels.NoteDataModel
import com.gluco.Data.Local.NoteDomainModel
import com.gluco.Data.Remote.DataModels.NotesResponse

class TodoEntryConverter {
    companion object {

        fun convertPageResponse(response: NotesResponse) : List<NoteDomainModel> {
            var rez = ArrayList<NoteDomainModel>()
            response.notes.forEach {
                rez.add(convert(it))
            }
            return rez
        }

        fun convert(dataModel: NoteDataModel) : NoteDomainModel {
            return NoteDomainModel(
                    id = dataModel.id,
                    text = dataModel.text,
                    date = dataModel.date
            )
        }

        fun convert(domainModel: NoteDomainModel) : NoteDataModel {
            return NoteDataModel(
                    id = domainModel.id,
                    text = domainModel.text,
                    date = domainModel.date
            )
        }
    }
}