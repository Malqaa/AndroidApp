package com.malka.androidappp.newPhase.data.helper.widgets.rcv
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filterable
import androidx.recyclerview.widget.ListAdapter
import com.malka.androidappp.newPhase.data.helper.BaseItemCallback
import com.malka.androidappp.newPhase.data.helper.BaseViewHolder

abstract class GenericListAdapter<T : Any>(
     val layoutId: Int,
    inline val bind: (item: T, holder: BaseViewHolder, itemCount: Int,position:Int) -> Unit
) : ListAdapter<T, BaseViewHolder>(
    BaseItemCallback<T>()
),Filterable {
    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        bind(getItem(position), holder, itemCount,position)
    }

    override fun getItemViewType(position: Int) = layoutId
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val root = LayoutInflater.from(parent.context).inflate(
            viewType, parent, false
        )
        return BaseViewHolder(root)
    }

    override fun getItemCount() = currentList.size
}