package com.simpletempco.simpletemp.util

import android.os.Parcel
import android.os.Parcelable
import android.view.View
import com.simpletempco.simpletemp.ui.custom.views.calendar.DateTag

class SavedState : View.BaseSavedState {
    var date: String? = null
    var tagList: List<DateTag>? = null

    internal constructor(superState: Parcelable?) : super(superState) {}
    constructor(source: Parcel) : super(source) {
        date = source.readString()
        tagList?.let { source.readList(it, DateTag::class.java.classLoader) }
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        super.writeToParcel(dest, flags)
        dest.writeString(date)
        dest.writeList(tagList)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<SavedState?> = object : Parcelable.Creator<SavedState?> {
            override fun createFromParcel(`in`: Parcel): SavedState {
                return SavedState(`in`)
            }

            override fun newArray(size: Int): Array<SavedState?> {
                return arrayOfNulls(size)
            }
        }
    }
}