package com.conagra.hardware.mcSdk;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Handler;

public class EhongBLEScan {

	
	//private BluetoothAdapter mBluetoothAdapter;

	private Handler mHandler;
	private int mMC10State;
	// 10���ֹͣ��������.
	
	private static final String EH_MC10_DEFAULT_UUID128 = "00FFEEDDCCBBAA998877665544332211";
//	private static final String EH_MC10_DEFAULT_UUID128 = "112233445566778899AABBCCDDEEFF00";
    private static final long SCAN_PERIOD = 10000;
	
	
	static class DeviceScan{
    	
    	/** check if BLE Supported device */
        public static boolean isBLESupported(Context context) {
            return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
        }

        /** get BluetoothManager */
        public static BluetoothManager getManager(Context context) {
            return (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        }
    }
	
	private static final String[] hexArr = {"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"};
	public static String byte2HexStr(byte[] byt){
		StringBuffer strRet = new StringBuffer();
		for(int i=0;i<byt.length;i++){ 
			strRet.append(hexArr[(byt[i] & 0xf0)/16]); 
			strRet.append(hexArr[byt[i] & 0x0f]); 
		} 
		return strRet.toString(); 
	}
	
////used for fileter the deviceQQ群 by uuid /* 2015-03-29*/
    public static  boolean EH_FilterUUID_128(String srcAdvData){
    	
    	if(srcAdvData.substring(10,42).equalsIgnoreCase(EH_MC10_DEFAULT_UUID128)){
    		return true;
    	}
    	return false;
    }
    
    public static boolean EH_BLE_Support(Context context){
        if (!DeviceScan.isBLESupported(context)) {
            //Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
        
    }
    
    
    public static BluetoothAdapter EH_GetBLE_Manager(Context context){
        final BluetoothManager bluetoothManager = DeviceScan.getManager(context);
        final BluetoothAdapter mBluetoothAdapter = bluetoothManager.getAdapter();
        return mBluetoothAdapter;
    }
	/*
	private void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                	mMC10State = 1;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                }
            }, SCAN_PERIOD);

            mMC10State = 1;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
        	mMC10State = 0;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
    }
	*/
	
}
