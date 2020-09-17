package com.conagra.hardware.mcSdk;

import android.app.Activity;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.os.IBinder;
import android.util.Log;

import java.util.List;
import java.util.UUID;

/**
 * 血糖蓝牙连接处理
 */
public class EhongBLEGatt {
	private final static String TAG = "EhongBLEGatt";
	public static String DataServiceUUID = "11223344-5566-7788-99AA-BBCCDDEEFF00";
    public static String Data_UUID = "00004a5b-0000-1000-8000-00805f9b34fb";
    
    public static String InfoServiceUUID = "0000180a-0000-1000-8000-00805f9b34fb";
    public static String Version_UUID = "00002a26-0000-1000-8000-00805f9b34fb";
    
    public static BluetoothLeService mBluetoothLeService;
    
    private static BluetoothGattService MC10_DataService;
    private static BluetoothGattCharacteristic MC10_DataCharacteristic;
    
    private static BluetoothGattService MC10_InfoService;
    private static BluetoothGattCharacteristic MC10_InfoCharacteristic;
    
    public static BluetoothGattCharacteristic EH_GATT_GetCharacteristic(BluetoothGattService svc, UUID uuid){
		if(svc == null) return null;
    	List<BluetoothGattCharacteristic> characteristics = svc.getCharacteristics();
    	if(characteristics == null) return null;

    	for (BluetoothGattCharacteristic chr : characteristics){
			Log.i(TAG, "characteristic uuid ="+chr.getUuid().toString());
		}
    	
    	for(BluetoothGattCharacteristic gattCharacteristic : characteristics){
    		if(gattCharacteristic != null && uuid.equals(gattCharacteristic.getUuid())){
    			return gattCharacteristic;
    		}
    	}
    	
    	return null;
    }

    	public boolean EH_GATT_GetServiceAndCharacteristics(){
    	
    	MC10_DataService = EH_GATT_GetSerivce(UUID.fromString(DataServiceUUID));
    	MC10_InfoService = EH_GATT_GetSerivce(UUID.fromString(InfoServiceUUID));
    	if(MC10_DataService==null){
    		//Toast.makeText(this, "Can't get service", Toast.LENGTH_SHORT).show();
    		return false;
    	}

    	MC10_DataCharacteristic = EH_GATT_GetCharacteristic(MC10_DataService, UUID.fromString(Data_UUID));
    	MC10_InfoCharacteristic = EH_GATT_GetCharacteristic(MC10_InfoService, UUID.fromString(Version_UUID));
    	
    	
    	
    	if(MC10_DataCharacteristic == null){
    		//Toast.makeText(this, "Can't get characteristics", Toast.LENGTH_SHORT).show();
    		return false;
    	}
    	EH_GATT_StartNotify(true);
    	return true;
    }
    
    public void EH_GATT_StartNotify(boolean on){
    	mBluetoothLeService.setCharacteristicNotification(
    			MC10_DataCharacteristic, on);
    }

	public static BluetoothGattService EH_GATT_GetSerivce(UUID uuid){
	
		List<BluetoothGattService> services = mBluetoothLeService.getSupportedGattServices();
		
		for (BluetoothGattService svc : services){
			Log.i(TAG, "service uuid ="+svc.getUuid().toString());

		}
		
		for (BluetoothGattService gattService : services){
			if(uuid.equals(gattService.getUuid())){
				return gattService;
			}
		}
	
		return null;
	}

	
	public boolean EH_GATT_Init(Activity activity,IBinder service, String mDeviceAddress){
		
		mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();

        if (!mBluetoothLeService.initialize()) {
            //Log.e(TAG, "Unable to initialize Bluetooth");
            return false;
        }
        // Automatically connects to the device upon successful start-up initialization.
//		if(CurrentServerManagerUtils.isDesignCompany){
//			SharedPreferencesUtils.addBloodSugar(mDeviceAddress);
//		}else  if(!SharedPreferencesUtils.isExistAboutBloodSugar(mDeviceAddress)){
//			Toast.makeText(activity,"该设备未注册，请联系您的家庭医生",Toast.LENGTH_SHORT).show();
//			return false;
//		}
        mBluetoothLeService.connect(mDeviceAddress);
        
        return true;
	}
	
	public void EH_GATT_Reinit(){
		mBluetoothLeService = null;
	}

	public boolean EH_GATT_IsConnectAction(String action){
		
		return BluetoothLeService.ACTION_GATT_CONNECTED.equals(action);
	}
	
	public boolean EH_GATT_IsDisconnectAction(String action){
			
		return BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action);
	}
	
	public boolean EH_GATT_IsDiscoverService(String action){
		
		return BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action);
	}
	
	public boolean EH_GATT_IsDataComeIn(String action){
		
		return BluetoothLeService.ACTION_DATA_NOTIFY.equals(action);
	}
	
	public boolean EH_GATT_IsDataReadBack(String action){
		Log.i(TAG,"ACTION_DATA_READBACK----"+action);
		return BluetoothLeService.ACTION_DATA_READBACK.equals(action);
	}

	public boolean EH_GATT_Connect(String address){
		return mBluetoothLeService.connect(address);
	}
	
	public void EH_GATT_Disconnect(){
		mBluetoothLeService.disconnect();
	}
	
	public BluetoothLeService EH_GATT_ReturnLeService(){
		return mBluetoothLeService;
	}
	
	
	///////**mc10 api*///////////////////////
	public void EH_GATT_SendData(byte[] d){
		if(MC10_DataCharacteristic != null){
			MC10_DataCharacteristic.setValue(d);
			MC10_DataCharacteristic.setWriteType(MC10_DataCharacteristic.getWriteType());
		}
		if(null != mBluetoothLeService) {
			mBluetoothLeService.writeCharacteristic(MC10_DataCharacteristic);
		}
	}
	
	public void EH_GATT_ReadVersion(){
	
		mBluetoothLeService.readCharacteristic(MC10_InfoCharacteristic);
	}

}
