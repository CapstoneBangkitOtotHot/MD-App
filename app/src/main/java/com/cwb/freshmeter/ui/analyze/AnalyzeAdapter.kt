package com.cwb.freshmeter.ui.analyze

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.cwb.freshmeter.R
import com.cwb.freshmeter.api.InferenceData
import com.squareup.picasso.Picasso

class AnalyzeAdapter(private val context: Context, private val list: List<InferenceData>) : RecyclerView.Adapter<AnalyzeAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(
            R.layout.recycle_inference_result,
            parent, false
        )
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val inferenceData = list[position]

        // Load the cropped image using Picasso
        if (inferenceData.cropped_img.isNotEmpty()) {
            Picasso.get().load(inferenceData.cropped_img).into(holder.ivDetailInference)
        }

        // Set the values for inference details
        holder.tvFruitClass.text = inferenceData.fruit_class_string
        holder.tvFreshnessPercentage.text = "Freshness: ${inferenceData.freshness_percentage}"

        // Set the transition name for the image
        holder.ivDetailInference.transitionName = "inference_image_transition_$position"

        // Set the click listener to open DetailActivity
        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetailAnalyzeActivity::class.java)
            intent.putExtra("inference_data", inferenceData)

            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                context as Activity,
                holder.ivDetailInference,
                holder.ivDetailInference.transitionName
            )

            context.startActivity(intent, options.toBundle())
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivDetailInference: ImageView = itemView.findViewById(R.id.Detail_inference)
        val tvFruitClass: TextView = itemView.findViewById(R.id.tvFruitClass)
        val tvFreshnessPercentage: TextView = itemView.findViewById(R.id.tvFreshnessPercentage)
    }
}






