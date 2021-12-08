package com.memoria

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothServerSocket
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import java.util.*

class BuscarPartidaActivity : AppCompatActivity() {

	private var mBluetoothAdapter : BluetoothAdapter? = null
	lateinit var n_pairedDevice : Set<BluetoothDevice>
	var lvLista : ListView? = null
	var socketAux : BluetoothServerSocket? = null

	val APP_NAME = "Memoria BT"
	val myUUID : UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
	val REQUEST_ENABLE_BLUETOOTH = 1

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_buscar_partida)

		relacionarObjetos()

		Log.i("console","llego")

		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

		if(!mBluetoothAdapter!!.isEnabled){
			val enableBluetoothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
			startActivityForResult(enableBluetoothIntent, REQUEST_ENABLE_BLUETOOTH)
			Toast.makeText(this, "Bluetooth Habilitado", Toast.LENGTH_SHORT).show()
		}else{
			socketAux = mBluetoothAdapter!!.listenUsingRfcommWithServiceRecord(APP_NAME, myUUID)
			implementarListeners()
			Log.i("console","ya estaba prendido")
		}
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)
		implementarListeners()
		if(resultCode == -1){
			socketAux = mBluetoothAdapter!!.listenUsingRfcommWithServiceRecord(APP_NAME, myUUID)
		}
	}

	private fun implementarListeners(){
		n_pairedDevice = mBluetoothAdapter!!.bondedDevices
		//val list : ArrayList<BluetoothDevice> = ArrayList()
		val list : ArrayList<Dispositivo> = ArrayList()
		if(n_pairedDevice.isNotEmpty()){
			for(device : BluetoothDevice in n_pairedDevice){
				list.add(Dispositivo(device))
				Log.i("console", "device: ${device.name}")
			}
		}else{
			Toast.makeText(this,"No hay dispositivos vinculados", Toast.LENGTH_SHORT).show()
		}

		val adapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,list)

		lvLista!!.adapter = adapter
		lvLista!!.onItemClickListener = AdapterView.OnItemClickListener{ _, _, position, _ ->
			val device : BluetoothDevice = list[position].btDevice
			val intent : Intent = Intent(this,ClienteActivity::class.java)
			Log.i("console","address from buscar partida activity: ${device.address}")
			intent.putExtra("btAddress",device.address)
			startActivity(intent)
		}
	}

	fun relacionarObjetos(){
		lvLista = findViewById(R.id.lvVinculados)
	}
}