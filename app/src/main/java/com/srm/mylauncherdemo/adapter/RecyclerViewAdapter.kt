package com.srm.mylauncherdemo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.srm.launchersdk.model.AppDetails
import com.srm.mylauncherdemo.R
import com.srm.mylauncherdemo.interfaces.OnItemClickListener
import java.util.*
import kotlin.collections.ArrayList

class RecyclerViewAdapter(
    private val items: MutableList<AppDetails>,
    private val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>(), Filterable {

    private lateinit var mContext: Context
    private var appFilterList = mutableListOf<AppDetails>()

    init {
        appFilterList = items
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext = parent.context
        val view =
            LayoutInflater.from(mContext).inflate(R.layout.item_list_app_details, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return appFilterList.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = appFilterList[position]

        item.appIcon.let {
            holder.imageAppIcon.setImageDrawable(it)
        }
        item.appName.let {
            holder.textAppName.text = it
        }
        item.packageName.let {
            holder.textPkgName.text =
                String.format(mContext.resources.getString(R.string.text_pkg), it)
        }
        item.mainActivityName.let {
            var name = it
            if (name != "NA") {
                name = name.substring(startIndex = name.lastIndexOf(".", name.length, true) + 1)
            }
            holder.textActivityName.text =
                String.format(mContext.resources.getString(R.string.text_main_activity), name)
        }
        item.versionCode.let {
            holder.textVersionCode.text =
                String.format(mContext.resources.getString(R.string.text_version_code), it)
        }
        item.versionName.let {
            holder.textVersionName.text =
                String.format(mContext.resources.getString(R.string.text_version_name), it)
        }

        holder.itemView.setOnClickListener {
            onItemClickListener.onItemClicked(item)
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageAppIcon: ImageView = view.findViewById(R.id.imageViewAppIcon)
        val textAppName: TextView = view.findViewById(R.id.textAppName)
        val textPkgName: TextView = view.findViewById(R.id.textPkgName)
        val textActivityName: TextView = view.findViewById(R.id.textMainActivityName)
        val textVersionCode: TextView = view.findViewById(R.id.textVersionCode)
        val textVersionName: TextView = view.findViewById(R.id.textVersionName)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                appFilterList = if (charSearch.isEmpty()) {
                    items
                } else {
                    val resultList = ArrayList<AppDetails>()
                    for (item in items) {
                        if (item.appName.toLowerCase(Locale.ROOT)
                                .contains(charSearch.toLowerCase(Locale.ROOT))
                        ) {
                            resultList.add(item)
                        }
                    }
                    resultList
                }
                val filterResults = FilterResults()
                filterResults.values = appFilterList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                appFilterList = results?.values as MutableList<AppDetails>
                notifyDataSetChanged()
            }
        }
    }
}