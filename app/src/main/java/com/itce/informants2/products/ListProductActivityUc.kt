package com.itce.informants2.products

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.itce.informants2.R
import kotlinx.android.synthetic.main.activity_list_product_uc.*


class ListProductActivityUc : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_product_uc)

        // val binding = ActivityListProductBinding.inflate(layoutInflater)
        // setContentView(binding.com.itce.informants.root)


        product_toolbar?.title = title
        setSupportActionBar(product_toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}