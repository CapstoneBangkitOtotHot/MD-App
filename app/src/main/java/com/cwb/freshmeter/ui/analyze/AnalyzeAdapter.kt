package com.example.burjoholic7.ui.transactions

import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.cwb.freshmeter.R
import com.cwb.freshmeter.api.InferenceData
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import com.squareup.picasso.Picasso


class AnalyzeAdapter(context: Context, list: List<InferenceData>?) : RecyclerView.Adapter<AnalyzeAdapter.ListViewHolder>() {
    private var listTransaction: ArrayList<InferenceData>?
    private var _context: Context

    init {
        listTransaction = list as ArrayList<InferenceData>?
        _context = context
    }

    //
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(
            R.layout.recycle_inference_result,
            parent, false
        )

        return ListViewHolder(_context, view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val inferenceData = listTransaction!![position]
        val persentase_kesegaran = inferenceData.freshness
        val confidence = inferenceData.confidence
        val buah = inferenceData.fruit_class
        val image = inferenceData.cropped_img
        //val persentase_kesegaran = inferenceData.
        val hari = 7

        Log.wtf("WTF!", image)
        if (image != "")
            Picasso.get().load(image).into(holder.ivDetailInference)

        if (persentase_kesegaran != null) {
            if (persentase_kesegaran < 0) {
                holder.tvLabelHari.text = "Hari sebelum busuk"
            } else {
                holder.tvLabelHari.text = "Hari Setelah busuk"
            }
            holder.tvValHari.text         = "NOT IMPLEMENTED"
        } else {
            holder.tvValHari.text         = "NOT SUPPORTED"
        }

        holder.tvValPersentaseKesegaran.text         = persentase_kesegaran.toString()




//        holder.itemView.setOnClickListener {
//            val detailIntent = Intent(
//                holder.itemView.context,
//                DetailActivity::class.java
//            )
//            detailIntent.putExtra(DetailActivity.KEY_ID, transaction.id)
//
//            detailIntent.putExtra("hideButton", frag is HistoriesFragment)
//
//
//            holder.itemView.context.startActivity(detailIntent)
//        }

    }

    class ListViewHolder(context: Context, itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ivDetailInference:           ImageView = itemView.findViewById(R.id.Detail_inference)
        var tvLabelPersentaseKesegaran:  TextView = itemView.findViewById(R.id.labelPersentaseKesegaran)
        var tvValPersentaseKesegaran:    TextView = itemView.findViewById(R.id.valPersentaseKesegaran)
        var tvLabelHari:                 TextView = itemView.findViewById(R.id.labelHari)
        var tvValHari:                   TextView = itemView.findViewById(R.id.valHari)


        init {
            tvLabelPersentaseKesegaran.text = "Persentase Kesegaran"
            tvLabelHari.text = "Hari"
        }
    }

    override fun getItemCount(): Int {
        return listTransaction!!.size
    }
}