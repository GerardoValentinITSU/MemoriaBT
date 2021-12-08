package com.memoria

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothServerSocket
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import java.util.*
import kotlin.collections.ArrayList

class HostPartidaActivity : AppCompatActivity() {
	private lateinit var servidor : SocketServidorBT

	private lateinit var tvStatus : TextView

	private lateinit var imagenes : ArrayList<Carta>
	private lateinit var cartas : Cartas

	private lateinit var btAdapter : BluetoothAdapter
	private lateinit var socketAux : BluetoothServerSocket

	private val APP_NAME = "Memoria BT"
	private val myUUID : UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
	private val REQUEST_ENABLE_BLUETOOTH = 1

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_host_partida)

		btAdapter = BluetoothAdapter.getDefaultAdapter()

		if(!btAdapter.isEnabled){
			val enableBluetoothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
			startActivityForResult(enableBluetoothIntent, REQUEST_ENABLE_BLUETOOTH)
			Toast.makeText(this, "Bluetooth Habilitado", Toast.LENGTH_SHORT).show()
		}else{
			socketAux = btAdapter!!.listenUsingRfcommWithServiceRecord(APP_NAME, myUUID)
			relacionarObjetos()
		}

		servidor = SocketServidorBT(this,socketAux,tvStatus,cartas)
		servidor.execute()

	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)
		relacionarObjetos()
		if(resultCode == -1){
			socketAux = btAdapter!!.listenUsingRfcommWithServiceRecord(APP_NAME, myUUID)
		}
	}

	private fun relacionarObjetos(){
		this.tvStatus = findViewById(R.id.tvSalidaHost)

		imagenes = ArrayList()

		for (i in 0..15){
			imagenes.add(Carta(i, findViewById(getImgView(i))))
		}

		cartas = Cartas(this,imagenes,tvStatus)
		cartas.generarCartas()
	}

	private fun getImgView(n: Int) : Int{
		when(n){
			0 -> return R.id.ivImg1Host
			1 -> return R.id.ivImg2Host
			2 -> return R.id.ivImg3Host
			3 -> return R.id.ivImg4Host
			4 -> return R.id.ivImg5Host
			5 -> return R.id.ivImg6Host
			6 -> return R.id.ivImg7Host
			7 -> return R.id.ivImg8Host
			8 -> return R.id.ivImg9Host
			9 -> return R.id.ivImg10Host
			10 -> return R.id.ivImg11Host
			11 -> return R.id.ivImg12Host
			12 -> return R.id.ivImg13Host
			13 -> return R.id.ivImg14Host
			14 -> return R.id.ivImg15Host
			15 -> return R.id.ivImg16Host
		}
		return 0
	}
}