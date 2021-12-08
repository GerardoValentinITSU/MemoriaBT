package com.memoria

import android.bluetooth.BluetoothSocket
import android.content.Context
import android.util.Log
import android.widget.TextView

class Cartas (val contexto : Context,lista : ArrayList<Carta>, val tvStatus : TextView){//, indices : ArrayList<Int>) {
	val cartas : ArrayList<Carta> = lista
	var last : Int = 0
	var unodos = true
	var jugando = false
	var paresAcumulados : Int = 0

	var socket : BluetoothSocket? = null

	fun generarCartas(){
		generarNumeros()
		for(elemento in cartas){
			elemento.imageView.setOnClickListener{
				Log.e("console","tocar")
				jugarCarta(elemento.index)
			}
		}
	}

	private fun getImagenId(n : Int) : Int{
		var out : Int = 0
		when(n){
			1 -> out = R.drawable.img1
			2 -> out = R.drawable.img2
			3 -> out = R.drawable.img3
			4 -> out = R.drawable.img4
			5 -> out = R.drawable.img5
			6 -> out = R.drawable.img6
			7 -> out = R.drawable.img7
			8 -> out = R.drawable.img8
		}
		return out
	}

	private fun generarNumeros(){
		var tmp : Int
		var repetidas : Int

		for(item in cartas){
			do{
				repetidas = 0
				tmp = (1..8).random()
				for(item in cartas){
					if(tmp == item.nImg){
						repetidas++
					}
				}
			}while(repetidas >= 2)
			item.nImg = tmp
		}
	}

	fun setJugando(){
		jugando = true
		unodos = true
		this.voltearCartas()
	}

	fun seAcabo() : Boolean{
		var out : Boolean = true
		for(item in cartas){
			if(!item.acertada){
				out = false
			}
		}
		return out
	}

	private fun enviar(m : String){
		var out = ":$m"
		socket!!.outputStream.write(out.toByteArray())
		Log.e("console","enviado $m")
	}

	fun voltearCarta(i : Int){
		cartas[i].imageView.setImageDrawable(contexto.getDrawable(getImagenId(cartas[i].nImg)))
		cartas[i].volteada = true
	}

	fun voltearCartas(){
		for(item in cartas){
			if(!item.acertada){
				item.imageView.setImageDrawable(contexto.getDrawable(R.drawable.blank))
				item.volteada = false
			}
		}
	}

	fun jugarCarta(i : Int){
		if(!seAcabo()){
			if(!cartas[i].acertada && jugando && !cartas[i].volteada) {
				voltearCarta(i)
				this.enviar("#${cartas[i].index}.")
				if (unodos) {
					last = cartas[i].index
				} else {
					comprobarPar(cartas[i].index)
				}
				unodos = !unodos
			}
		}else{
			tvStatus.text = "obtuviste $paresAcumulados aciertos"
		}
	}

	private fun comprobarPar(actual : Int){
		if(cartas[actual].nImg == 0 || cartas[last].nImg == 0 ){
			unodos = true
		}

		if(cartas[actual].nImg == cartas[last].nImg){
			for(m in cartas){
				if(m.volteada){
					m.acertada = true
				}
			}
			paresAcumulados++
		}

		if(cartas[actual].nImg != cartas[last].nImg){
			tvStatus.text = "Turno del oponente"
			jugando = false
			this.enviar("-.")
		}
	}

	fun enviarCartas(){
		var out : String = "!"
		for(item in cartas){
			out = "$out${item.nImg},"
		}
		out = "$out."
		this.enviar(out)
	}

	fun setCartas(m : String){
		var tmp : String = ""
		var index = 0
		for(l in m){
			if(l != '!'){
				if(l == ','){
					cartas[index].nImg = tmp.toInt()
					tmp = ""
					index++
				}else{
					tmp = "$tmp$l"
				}
			}
		}
	}
}