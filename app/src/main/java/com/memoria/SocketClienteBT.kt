package com.memoria

import android.bluetooth.BluetoothSocket
import android.content.Context
import android.os.AsyncTask
import android.os.Message
import android.util.Log
import android.widget.TextView
import java.io.IOException

class SocketClienteBT(
	val context : Context,
	val socketCliente: BluetoothSocket,
	val tvStatus : TextView,
	val cartas : Cartas
) : AsyncTask<String, Int, String>()  {

	val MENSAJE = 0
	val STATE_LISTENING = 1
	val STATE_CONNECTING = 2
	val STATE_CONNECTED = 3
	val STATE_CONNECTION_FAILED = 4
	val STATE_MESSAGE_RECEIVED = 5

	var btSocketCliente: BluetoothSocket? = null

	var buffer : String? = null

	override fun onPreExecute() {
		try{
			btSocketCliente = socketCliente
			tvStatus.text = ""
		}catch(ex : IOException){
			Log.e("console", "Error: ${ex.message}")
		}
		super.onPreExecute()
	}

	override fun doInBackground(vararg p0: String?): String {
		var mensaje: Message? = null
		mensaje = Message.obtain()
		mensaje!!.what = STATE_CONNECTING
		publishProgress(mensaje.what)

		try{
			btSocketCliente!!.connect()
			cartas.socket = btSocketCliente
			mensaje!!.what = STATE_CONNECTED
			publishProgress(mensaje.what)
			while(btSocketCliente != null){
				try{
					var a : Char? = null
					a = btSocketCliente!!.inputStream.read().toChar()
					if(a == ':'){
						buffer = ""
					}else{
						if(a == '.'){
							Log.i("console", "recibido $buffer")
							publishProgress(MENSAJE)
						}else{
							buffer = "$buffer$a"
						}
					}
				}catch (e : IOException){
					Log.e("console", "Error socket cliente doinbackground: $e")
				}
			}
		}catch(ex : IOException){
			Log.e("console", "Error socket cliente doinbackground: $ex")
			mensaje = Message.obtain()
			mensaje.what = STATE_CONNECTION_FAILED
			publishProgress(mensaje.what)
		}
		return ""
	}

	override fun onProgressUpdate(vararg values: Int?) {
		when(values[0]){
			MENSAJE -> procMensaje()
			STATE_LISTENING -> tvStatus.text = "Esperando conexión"
			STATE_CONNECTING -> tvStatus.text = "Conectando..."
			STATE_CONNECTED -> tvStatus.text = "Conectado"
			STATE_CONNECTION_FAILED -> tvStatus.text = "Conexión Fallida"
			STATE_MESSAGE_RECEIVED -> tvStatus.text = "Mensaje Recibido"
		}
		super.onProgressUpdate(*values)
	}

	fun procMensaje(){
		when(buffer!![0]){
			'#' -> voltearCarta(buffer)
			'-' -> ajugar()
			'!' -> setCartas()
		}
	}

	private fun voltearCarta(m : String?){
		var e : String = ""
		for(i in m!!){
			if(i != '#'){
				e = "$e$i"
			}
		}
		cartas.voltearCarta(e.toInt())
	}

	private fun ajugar(){
		cartas.setJugando()
		tvStatus.text = "Tu turno"
	}

	private fun setCartas(){
		cartas.setCartas(buffer!!)
	}
}