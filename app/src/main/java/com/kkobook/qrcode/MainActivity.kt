package com.kkobook.qrcode

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.zxing.integration.android.IntentIntegrator
import com.kkobook.qrcode.databinding.ActivityMainBinding
import com.kkobook.qrcode.view.HistoryFragment
import com.kkobook.qrcode.view.QRCodeFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupNavigation()
        setupViewPager()

//        binding.scanBarcode.setOnClickListener {
//            IntentIntegrator(this).initiateScan()
//        }
//
//        binding.scanBarcodeCustom.setOnClickListener {
//            val integrator = IntentIntegrator(this)
//            integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE) // 특정 규격의 바코드만 지원
//            integrator.setPrompt("QR 코드를 스캔하여 주세요") // 카메라 프리뷰 하단에 표시되는 문구
//            integrator.setCameraId(0) // 0 후면카메라, 1 전면카메라
//            integrator.setBeepEnabled(true) // 바코드 인식할 때 비프음 여부
//            integrator.setBarcodeImageEnabled(true) // 인식한 바코드 사진을 저장하고 경로를 반환
//            integrator.setOrientationLocked(false) // orientation이 fullSensor일 때 orientation 변경을 허용할지 여부
//            integrator.initiateScan()
//        }
//
//        binding.scanBarcodeCustomActivity.setOnClickListener {
//            val integrator = IntentIntegrator(this)
//            integrator.setBarcodeImageEnabled(true) // 인식한 바코드 사진을 저장하고 경로를 반환
//            integrator.captureActivity = QRCodeActivity::class.java
//            integrator.initiateScan()
//        }
    }

    private fun setupViewPager() {
        viewPager.adapter = object : FragmentPagerAdapter(supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            override fun getCount(): Int = 2

            override fun getItem(position: Int): Fragment {
                if (position == 1) {
                    return HistoryFragment.newInstance()
                }
                return QRCodeFragment.newInstance()
            }

        }
    }

    private fun setupNavigation() {
        navigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
    }

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_QR -> {
                title = "QR"
                viewPager.setCurrentItem(0, false)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_history -> {
                title = "history"
                viewPager.setCurrentItem(1, false)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // 액티비티 호출 결과를 ZXing을 통해 파싱한다. 결과가 null이면 ZXing 액티비티에서 전달된 결과가 아니다.
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            // result.contents에는 스캔한 결과가 포함된다. 만약 null이라면 사용자가 스캔을 완료하지 않고 QR 리더 액티비티를 종료한 것이다.
            if (result.contents != null) {
                Toast.makeText(this, "Scanned: ${result.contents} ${result.formatName}", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
            }
            // integrator.setBarcodeImageEnabled(true)를 하였다면 아래와 같이 스캔된 결과 이미지 파일을 저장하고 그 경로를 가져올 수 있다.
            if (result.barcodeImagePath != null) {
                Log.i(TAG, "onActivityResult: ${result.barcodeImagePath}")
                val bitmap = BitmapFactory.decodeFile(result.barcodeImagePath)
//                barcodeImage.setImageBitmap(bitmap)
            }
            App.prefs.saveQRCodes(result.contents, System.currentTimeMillis())
            viewPager.setCurrentItem(1, false)
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}