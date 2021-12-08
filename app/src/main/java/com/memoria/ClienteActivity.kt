package com.memoria

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import java.util.*

class ClienteActivity : AppCompatActivity() {
	private lateinit var tvStatus : TextView
	private lateinit var btnEnviar : Button

	private lateinit var cliente : SocketClienteBT
	private lateinit var btDevice : BluetoothDevice
	private lateinit var btAdapter : BluetoothAdapter
	private lateinit var socketCliente : BluetoothSocket

	private lateinit var imagenes : ArrayList<Carta>
	private lateinit var cartas : Cartas

	val myUUID : UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_cliente)

		btAdapter = BluetoothAdapter.getDefaultAdapter()

		relacionarObjetos()
		var address: String? = intent.getStringExtra("btAddress")
		btDevice = btAdapter.getRemoteDevice(address)

		socketCliente = btDevice.createRfcommSocketToServiceRecord(myUUID)

		cliente = SocketClienteBT(this,socketCliente,tvStatus,cartas)
		cliente.execute()
	}

	private fun relacionarObjetos(){
		this.tvStatus = findViewById(R.id.tvSalidaCliente)

		imagenes = ArrayList()

		for (i in 0..15){
			imagenes.add(Carta(i, findViewById(getImgView(i))))
		}

		cartas = Cartas(this,imagenes,tvStatus)
		cartas.generarCartas()
	}

	fun getImgView(n: Int) : Int{
		when(n){
			0 -> return R.id.ivImg1Client
			1 -> return R.id.ivImg2Client
			2 -> return R.id.ivImg3Client
			3 -> return R.id.ivImg4Client
			4 -> return R.id.ivImg5Client
			5 -> return R.id.ivImg6Client
			6 -> return R.id.ivImg7Client
			7 -> return R.id.ivImg8Client
			8 -> return R.id.ivImg9Client
			9 -> return R.id.ivImg10Client
			10 -> return R.id.ivImg11Client
			11 -> return R.id.ivImg12Client
			12 -> return R.id.ivImg13Client
			13 -> return R.id.ivImg14Client
			14 -> return R.id.ivImg15Client
			15 -> return R.id.ivImg16Client
		}
		return 0
	}
}