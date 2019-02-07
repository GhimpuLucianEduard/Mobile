package com.gluco.Presentation.MainList

import com.gluco.Data.Local.NoteDomainModel

interface CardClickListener {
    fun onDeletedClicked(note: NoteDomainModel)
}