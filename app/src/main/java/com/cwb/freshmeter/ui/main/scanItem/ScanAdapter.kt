package com.cwb.freshmeter.ui.main.scanItem

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cwb.freshmeter.R

class ScanAdapter(private val scanList: List<ScanItem>, private val context: Context) : RecyclerView.Adapter<ScanAdapter.ScanViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScanViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.scan_item, parent, false)
        return ScanViewHolder(view)
    }

    override fun onBindViewHolder(holder: ScanViewHolder, position: Int) {
        val scanItem = scanList[position]
        holder.bind(scanItem)
    }

    override fun getItemCount(): Int {
        return scanList.size
    }

    inner class ScanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.scan_image)
        private val titleView: TextView = itemView.findViewById(R.id.scan_title)

        fun bind(scanItem: ScanItem) {
            imageView.setImageResource(scanItem.image)
            titleView.text = scanItem.title
        }
    }
}