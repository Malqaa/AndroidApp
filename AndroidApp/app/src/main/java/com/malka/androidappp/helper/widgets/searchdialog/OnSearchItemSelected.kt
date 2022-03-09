package com.malka.androidappp.helper.widgets.searchdialog

import com.malka.androidappp.helper.widgets.searchdialog.SearchListItem

/**
 * Created by ajithvgiri on 06/11/17.
 */
internal interface OnSearchItemSelected {
    fun onClick(position: Int, searchListItem: SearchListItem?)
}