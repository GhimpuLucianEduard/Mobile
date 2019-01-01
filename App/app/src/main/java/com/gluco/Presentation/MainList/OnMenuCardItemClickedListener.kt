package com.gluco.Presentation.MainList

import com.gluco.Data.Local.GlucoseEntry

interface OnMenuCardItemClickedListener {
    fun onEditClicked(glucoseEntry: GlucoseEntry)
}