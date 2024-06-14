package com.cwb.freshmeter.ui.main.articleItem

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cwb.freshmeter.R
import com.cwb.freshmeter.ui.main.WebViewActivity

class ArticlesAdapter(private val articlesList : List<Articles>, private val context:Context) :
    RecyclerView.Adapter<ArticlesAdapter.ArticlesViewHolder>() {

        class ArticlesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val articlesImageView: ImageView = itemView.findViewById(R.id.article_image)
            val articlesTitleTv: TextView = itemView.findViewById(R.id.article_title)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticlesViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.articles_item, parent,false)
            return ArticlesViewHolder(view)
        }

        override fun getItemCount(): Int {
            return articlesList.size
        }

        override fun onBindViewHolder(holder: ArticlesViewHolder, position: Int) {
            val articles = articlesList[position]
            holder.articlesImageView.setImageResource(articles.articlesImage)
            holder.articlesTitleTv.text = articles.articlesTitle

            holder.itemView.setOnClickListener {
                val intent = Intent(context, WebViewActivity::class.java)
                intent.putExtra("articleUrl", articles.link) // Assuming you want to open link1 by default
                context.startActivity(intent)

                if (context is Activity) {
                    context.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                }
            }
        }
}
