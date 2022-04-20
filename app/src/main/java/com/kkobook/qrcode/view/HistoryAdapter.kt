package com.kkobook.qrcode

import android.content.Intent
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kkobook.qrcode.model.History
import com.kkobook.qrcode.utils.inflate
import kotlinx.android.synthetic.main.list_item_history.view.*
import java.text.SimpleDateFormat

interface QRClickListener {
    fun onQRClick(url: String?)
}

class HistoryAdapter: RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {
    private var qrcodeLists: List<History> = emptyList()
    private var qrClickListener: QRClickListener? = null

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private lateinit var qrCode: History

        fun bind(qrCode: History) {
            this.qrCode = qrCode
            val SDF = SimpleDateFormat("yyyy-MM-dd-hh-mm")
            itemView.QR_address.text = qrCode.content
            itemView.QR_date.text = SDF.format(qrCode.currentTime)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.list_item_history))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(qrcodeLists[position])
        holder.itemView.apply {
            this.setOnClickListener {
                qrClickListener?.onQRClick(qrcodeLists[position].content)
            }
        }
    }

    override fun getItemCount() = qrcodeLists.size

    fun setData(qrcodeLists: List<History>) {
        this.qrcodeLists = qrcodeLists
        notifyDataSetChanged()
    }

    fun setQRClickListener(qrClickListener: QRClickListener) {
        this.qrClickListener = qrClickListener
    }
}