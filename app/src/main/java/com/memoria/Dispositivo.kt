package com.memoria

import android.bluetooth.BluetoothDevice

data class Dispositivo(val device : BluetoothDevice) {
	val btDevice : BluetoothDevice = device
	val nombre = btDevice.name
	val address = btDevice.address

	override fun toString(): String {
		return nombre
	}
}