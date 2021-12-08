package com.memoria

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
	private lateinit var buscarPartidaBtn : Button
	private lateinit var crearPartidaBtn : Button

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		implementarListeners()
	}

	override fun onResume() {
		super.onResume()
	}

	fun implementarListeners(){
		relacionarObjetos()

		this.buscarPartidaBtn.setOnClickListener {
			val intent = Intent(this,BuscarPartidaActivity::class.java)
			startActivity(intent)
			finish()
		}

		this.crearPartidaBtn.setOnClickListener {
			val intent = Intent(this,HostPartidaActivity::class.java)
			startActivity(intent)
			finish()
		}
	}

	private fun relacionarObjetos(){
		this.buscarPartidaBtn = findViewById(R.id.btnBuscarP)
		this.crearPartidaBtn = findViewById(R.id.btnCrearP)
	}
}