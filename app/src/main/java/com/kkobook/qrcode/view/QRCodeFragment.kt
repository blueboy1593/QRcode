package com.kkobook.qrcode.view

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.zxing.integration.android.IntentIntegrator
import com.journeyapps.barcodescanner.CaptureManager

import com.kkobook.qrcode.R


class QRCodeFragment: Fragment() {
    private val TAG = "QRCodeActivity"
    private lateinit var capture: CaptureManager

    companion object {
        fun newInstance(): QRCodeFragment {
            return QRCodeFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_qrcode, container, false)
    }

//    // Register the launcher and result handler
//    private val barcodeLauncher = registerForActivityResult(
//        ScanContract()
//    ) { result: ScanIntentResult ->
//        if (result.contents == null) {
//            Toast.makeText(context, "Cancelled", Toast.LENGTH_LONG).show()
//        } else {
//            Toast.makeText(
//                context,
//                "Scanned: " + result.contents,
//                Toast.LENGTH_LONG
//            ).show()
//        }
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val options = ScanOptions()
//        options.setDesiredBarcodeFormats(ScanOptions.ONE_D_CODE_TYPES)
//        options.setPrompt("Scan a barcode")
//        options.setCameraId(0) // Use a specific camera of the device
//        options.setBeepEnabled(false)
//        options.setBarcodeImageEnabled(true)
//        barcodeLauncher.launch(options)
    }

    override fun onResume() {
        super.onResume()
        val integrator = IntentIntegrator(activity)
        integrator.setBarcodeImageEnabled(true) // 인식한 바코드 사진을 저장하고 경로를 반환
        integrator.setCameraId(0)
        integrator.setBeepEnabled(true)
        integrator.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // 액티비티 호출 결과를 ZXing을 통해 파싱한다. 결과가 null이면 ZXing 액티비티에서 전달된 결과가 아니다.
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            // result.contents에는 스캔한 결과가 포함된다. 만약 null이라면 사용자가 스캔을 완료하지 않고 QR 리더 액티비티를 종료한 것이다.
            if (result.contents != null) {
                Toast.makeText(context, "Scanned: ${result.contents} ${result.formatName}", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(context, "Cancelled", Toast.LENGTH_LONG).show()
            }
            // integrator.setBarcodeImageEnabled(true)를 하였다면 아래와 같이 스캔된 결과 이미지 파일을 저장하고 그 경로를 가져올 수 있다.
            if (result.barcodeImagePath != null) {
                Log.i(TAG, "onActivityResult: ${result.barcodeImagePath}")
                val bitmap = BitmapFactory.decodeFile(result.barcodeImagePath)
//                barcodeImage.setImageBitmap(bitmap)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}