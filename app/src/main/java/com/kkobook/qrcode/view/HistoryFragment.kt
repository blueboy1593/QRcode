package com.kkobook.qrcode.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.kkobook.qrcode.App
import com.kkobook.qrcode.HistoryAdapter
import com.kkobook.qrcode.QRClickListener
import com.kkobook.qrcode.R
import kotlinx.android.synthetic.main.fragment_history.*

class HistoryFragment: Fragment(), QRClickListener {

    private val adapter = HistoryAdapter()

    companion object {
        fun newInstance(): HistoryFragment {
            return HistoryFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        historyRecyclerView.layoutManager = LinearLayoutManager(activity)
        historyRecyclerView.adapter = adapter
        adapter.setQRClickListener(this)

        deleteContainer.setOnClickListener {
            App.prefs.deleteQRCodes()
            adapter.setData(emptyList())
        }
    }

    override fun onResume() {
        super.onResume()
        val currentQRCodes = App.prefs.getQRCodes()
        adapter.setData(currentQRCodes)
    }

    override fun onQRClick(url: String?) {
        val intent = Intent(Intent.ACTION_VIEW)
        val parsedUrl = Uri.parse(url)
        intent.data = parsedUrl
        startActivity(intent)
    }
}