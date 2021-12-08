package com.memoria

import android.widget.ImageView

class Carta (indice : Int, img : ImageView){
	val index : Int = indice
	val imageView : ImageView = img
	var nImg : Int = 0
	var volteada = false
	var acertada = false
}