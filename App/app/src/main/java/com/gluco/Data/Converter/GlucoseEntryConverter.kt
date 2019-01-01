package com.gluco.Data.Converter

import com.gluco.Data.Local.GlucoseEntry
import com.gluco.Data.Remote.DataModels.GlucoseEntryDataModel

class GlucoseEntryConverter {
    companion object {
        fun convert(dataModel: GlucoseEntryDataModel): GlucoseEntry {
            return GlucoseEntry(
                afterMeal = dataModel.afterMeal,
                timestamp = dataModel.timestamp,
                value = dataModel.value,
                apiId = dataModel.id,
                note = dataModel.note ?: "")
        }

        fun convert(model: GlucoseEntry): GlucoseEntryDataModel {
            return GlucoseEntryDataModel(
                afterMeal = model.afterMeal,
                timestamp = model.timestamp!!,
                value = model.value,
                note = model.note ?: "")
        }
    }
}