package com.example.madlabminiproject.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.madlabminiproject.Order
import com.example.madlabminiproject.R
import com.example.madlabminiproject.RetrofitInterface
import com.google.gson.GsonBuilder
import com.razorpay.Checkout
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import kotlinx.android.synthetic.main.activity_payment.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PaymentActivity : AppCompatActivity(),PaymentResultWithDataListener {
    lateinit var retorfit:Retrofit
    lateinit var retorfitInterface:RetrofitInterface
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        Checkout.preload(applicationContext)

        val gson = GsonBuilder().setLenient()

        retorfit=Retrofit.Builder()
            .baseUrl("http://165.232.190.91:8000")
            .addConverterFactory(GsonConverterFactory.create(gson.create()))
            .build()

        retorfitInterface=retorfit.create(RetrofitInterface::class.java)

        pay_Btn.setOnClickListener {
            val amount=amountEdit.text.toString()
            if(amount.isEmpty()){
                return@setOnClickListener
            }
            getOrderId(amount)
        }
    }

    private fun getOrderId(amount: String) {
        val map=HashMap<String,String>()
        map["amount"]=amount
        retorfitInterface
            .getOrderId(map).enqueue(object :Callback<Order> {
            override fun onResponse(call: Call<Order>, response: Response<Order>) {
                if(response.body()!=null)
                    initiatePayment(amount, response.body()!!)
            }

            override fun onFailure(call: Call<Order>, t: Throwable) {
                Toast.makeText(this@PaymentActivity, t.message, Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun initiatePayment(amount: String, body: Order) {
        val checkout=Checkout()
        checkout.setKeyID(body.getKeyId())
        checkout.setImage(R.drawable.sjbit_logo)
        val paymentOption=JSONObject()
        paymentOption.put("name","SJBIT Fee payment")
        paymentOption.put("amount",amount+"00")
        paymentOption.put("orderId",body.getOrderId())
        paymentOption.put("currency","INR")
        paymentOption.put("description","Reference no:#1234")

        checkout.open(this,paymentOption)
    }

    override fun onPaymentSuccess(p0: String?, p1: PaymentData?) {
        val map=HashMap<String,String>()
        map["order_id"]=p1!!.orderId
        map["pay_id"]=p1.paymentId
        map["signature"]=p1.signature

        retorfitInterface.updateTransaction(map).enqueue(object :Callback<String>{
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if(response.body().equals("success"))
                    Toast.makeText(this@PaymentActivity,"Payment Successful",Toast.LENGTH_LONG).show()
                    finish()
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(this@PaymentActivity, t.message, Toast.LENGTH_LONG).show()
            }

        })
    }

    override fun onPaymentError(p0: Int, p1: String?, p2: PaymentData?) {
        Toast.makeText(this, "Payment failed", Toast.LENGTH_LONG).show()
    }


}