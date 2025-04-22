package com.gmail_bssushant2003.journeycraft.Login

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.gmail_bssushant2003.journeycraft.R
import android.Manifest
import android.content.Intent
import android.telephony.SmsManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import kotlin.random.Random
import com.gmail_bssushant2003.journeycraft.databinding.ActivitySendOtpactivityBinding

class SendOTPActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySendOtpactivityBinding
    private val SMS_PERMISSION_CODE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySendOtpactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = resources.getColor(R.color.white, theme)

        binding.buttonOTP.setOnClickListener {
            val mobileNumber = binding.inputMobile.text.toString()
            if(mobileNumber.length != 10) Toast.makeText(this, "Enter valid phone number", Toast.LENGTH_LONG).show()
            else solve(mobileNumber)
        }
    }

    private fun solve(mobileNumber: String) {
        val otp = generateOTP()

        if (checkSmsPermission()) {
            sendSms(mobileNumber, otp)
        } else {
            requestSmsPermission()
        }
    }

    private fun generateOTP(): String {
        return Random.nextInt(100000, 999999).toString()
    }

    private fun sendSms(phoneNumber: String, otp: String) {
        try {
            val smsManager = SmsManager.getDefault()
            val message = "Your OTP is: $otp"
            smsManager.sendTextMessage(phoneNumber, null, message, null, null)
            Toast.makeText(this, "OTP sent successfully", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, VerificationOTPActivity::class.java)
            intent.putExtra("otp", otp)
            intent.putExtra("phone_number", phoneNumber)
            startActivity(intent)
            finish()
        } catch (e: Exception) {
            Toast.makeText(this, "Failed to send OTP: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkSmsPermission(): Boolean {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestSmsPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.SEND_SMS), SMS_PERMISSION_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == SMS_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}