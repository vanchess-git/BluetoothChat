package com.karoliinamultas.bluetoothchat

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.le.*
import android.os.ParcelUuid
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.karoliinamultas.bluetoothchat.data.MessagesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.nio.charset.Charset
import java.util.*

class MyViewModel(messagesRepository: MessagesRepository) : ViewModel() {

    private val mBluetoothAdapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

    lateinit var currentAdvertisingSet:AdvertisingSet
    var messages = MutableLiveData<List<String>>(listOf("message"))
    var uuids : List<String> = listOf("uuids")
    private val mResults = java.util.HashMap<String, ScanResult>()
    var fScanning = MutableLiveData<Boolean>(false)
    var scanResults = MutableLiveData<List<ScanResult>>(null)
    var dataToSend = MutableLiveData<ByteArray>("".toByteArray())


    // Create an AdvertiseData object to include data in the advertisement


    val parameters = AdvertisingSetParameters.Builder()
        .setLegacyMode(false)
        .setInterval(AdvertisingSetParameters.INTERVAL_LOW)
        .setTxPowerLevel(AdvertisingSetParameters.TX_POWER_MAX)
        .setPrimaryPhy(BluetoothDevice.PHY_LE_1M)
        .setSecondaryPhy(BluetoothDevice.PHY_LE_2M)

//    callBack is what triggers when scanner found needed service uuid
    private val leScanCallback: ScanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {super.onScanResult(callbackType, result)
            val serviceData = result.scanRecord?.getServiceData(ParcelUuid(UUID_APP_SERVICE))
            Log.d("hei", String(serviceData?: "t".toByteArray(Charsets.UTF_8), Charset.defaultCharset()))
            val splitMessage = String(serviceData?: "".toByteArray(Charsets.UTF_8), Charset.defaultCharset()).split("//")
            if(!uuids?.contains(splitMessage[0])!!){
            messages.postValue(messages.value?.plus(splitMessage[1]))
            uuids += splitMessage[0]
            Log.d("hei", uuids.toString())
                sendMessage(mBluetoothAdapter.bluetoothLeAdvertiser, mBluetoothAdapter.bluetoothLeScanner, splitMessage[1], splitMessage[0])
            }
        }
    }

    val callback: AdvertisingSetCallback = object : AdvertisingSetCallback() {
        override fun onAdvertisingSetStarted(
            advertisingSet: AdvertisingSet,
            txPower: Int,
            status: Int
        ) {
            Log.i(
                "LOG_TAG", "onAdvertisingSetStarted(): txPower:" + txPower + " , status: "
                        + status
            )
            currentAdvertisingSet = advertisingSet
        }

        override fun onAdvertisingSetStopped(advertisingSet: AdvertisingSet) {
            Log.i("LOG_TAG", "onAdvertisingSetStopped():")
        }
    }
@SuppressLint("MissingPermission")
fun sendMessage(bluetoothLeAdvertiser:BluetoothLeAdvertiser, bluetoothLeScanner: BluetoothLeScanner, message:String, uuid: String){
    val buildMessage: String
    if (uuid.isEmpty()){
     val uuidl = UUID.randomUUID().toString()
     buildMessage =  uuidl + "//" +message
    uuids += uuidl
    Log.d("uid",uuidl)
    } else {
        buildMessage = uuid + "//" + message
    uuids += uuid
    }
    messages.postValue(messages.value?.plus(message))
    val data = AdvertiseData.Builder()
        .setIncludeDeviceName( true )
        .addServiceData(ParcelUuid(UUID.fromString("cc17cc5a-b1d6-11ed-afa1-0242ac120002")), buildMessage.toByteArray(Charsets.UTF_8))
        .addServiceUuid(ParcelUuid(UUID.fromString("cc17cc5a-b1d6-11ed-afa1-0242ac120002")))
        .build()

    stopScan(bluetoothLeScanner)
    viewModelScope.launch(Dispatchers.IO) {
        bluetoothLeAdvertiser.startAdvertisingSet(
            parameters.build(),
            data,
            null,
            null,
            null,
            callback
        )
        delay(MESSAGE_PERIOD)
        bluetoothLeAdvertiser.stopAdvertisingSet(callback)
        scanDevices(bluetoothLeScanner)
    }
}

    //    Scanner with settings to follow specifis service uuid
    @SuppressLint("MissingPermission")
    fun scanDevices(bluetoothLeScanner:BluetoothLeScanner) {

        var filterList:List<ScanFilter> = listOf()
//        Scan filter and options to filter for
        fun buildScanFilters(): List<ScanFilter> {
        val builder = ScanFilter.Builder()
            builder.setServiceUuid(ParcelUuid(UUID_APP_SERVICE))
//            builder.setServiceData(ParcelUuid(UUID.fromString("cc17cc5a-b1d6-11ed-afa1-0242ac120002")))
//            builder.setDeviceName("PAVEL")
            val filter = builder.build()
            return listOf(filter)
        }
        if (filterList.isEmpty()){
            filterList = buildScanFilters()
        }
        viewModelScope.launch(Dispatchers.IO) {

            fScanning.postValue(true)

            val settings = ScanSettings.Builder()
                .setLegacy(false)
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES)
                .setReportDelay(0)
                .build()

            bluetoothLeScanner.startScan(filterList,settings, leScanCallback)

        }
    }
    @SuppressLint("MissingPermission")
    fun stopScan(bluetoothLeScanner: BluetoothLeScanner){
        fScanning.postValue(false)
        scanResults.postValue(mResults.values.toList())
        viewModelScope.launch(Dispatchers.IO) {
        bluetoothLeScanner.stopScan(leScanCallback)
        }
    }
    companion object GattAttributes {
        const val SCAN_PERIOD: Long = 10000
        const val MESSAGE_PERIOD: Long= 700
        const val STATE_CONNECTING = 1
        const val STATE_CONNECTED = 2
        val UUID_APP_SERVICE = UUID.fromString("cc17cc5a-b1d6-11ed-afa1-0242ac120002")
        val UUID_APP_DATA = UUID.fromString("cc17cc5a-b1d6-11ed-afa1-0242ac120002")

    }
}
