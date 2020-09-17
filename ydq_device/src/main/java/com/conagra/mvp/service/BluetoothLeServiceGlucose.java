/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.conagra.mvp.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.List;
import java.util.UUID;

/**
 * Service for managing connection and data communication with a GATT server
 * hosted on a given Bluetooth LE device.
 */
@SuppressLint("NewApi")
public class BluetoothLeServiceGlucose extends Service {
	private final static String TAG = BluetoothLeServiceGlucose.class.getSimpleName();

	public BluetoothGattCharacteristic h2s;
	public BluetoothGattCharacteristic s2h;
	public BluetoothGattService mBluetoothService;
	private BluetoothGatt mBluetoothGatt;

	private BluetoothManager mBluetoothManager;
	private BluetoothAdapter mBluetoothAdapter;
	private String mBluetoothDeviceAddress;
	@SuppressWarnings("unused")
	private int mConnectionState = STATE_DISCONNECTED;

	private static final int STATE_DISCONNECTED = 0;
	private static final int STATE_CONNECTING = 1;
	private static final int STATE_CONNECTED = 2;

	public final static String ACTION_GATT_CONNECTED = "com.example.bluetooth.le.ACTION_GATT_CONNECTED";
	public final static String ACTION_GATT_DISCONNECTED = "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED";
	public final static String ACTION_GATT_SERVICES_DISCOVERED = "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
	public final static String ACTION_DATA_AVAILABLE = "com.example.bluetooth.le.ACTION_DATA_AVAILABLE";
	public final static String EXTRA_DATA = "com.example.bluetooth.le.EXTRA_DATA";

	// 蓝牙血糖仪
	public static final UUID UUID_HOST2SLAVE = UUID.fromString(SampleGattAttributes.HOST2SLAVE);
	public static final UUID UUID_SLAVE2HOST = UUID.fromString(SampleGattAttributes.SLAVE2HOST);
	public static final UUID UUID_WANKANG_SEVICES = UUID.fromString(SampleGattAttributes.WANKANG_SEVICES);

	public final static UUID UUID_HEART_RATE_MEASUREMENT = UUID.fromString(SampleGattAttributes.HEART_RATE_MEASUREMENT);

	private int count = 0;

	// Implements callback methods for GATT events that the app cares about. For
	// example,
	// connection change and services discovered.
	@SuppressLint("NewApi")
	private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
		@Override
		public void onConnectionStateChange(BluetoothGatt gatt, int status,
											int newState) {
			if (newState == BluetoothProfile.STATE_CONNECTED) {
				mConnectionState = STATE_CONNECTED;
				broadcastUpdate(ACTION_GATT_CONNECTED);
				Log.i(TAG, "Connected to GATT server.");
				// Attempts to discover services after successful connection.
				Log.i(TAG, "Attempting to start service discovery:"
						+ mBluetoothGatt.discoverServices());

			} else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
				mConnectionState = STATE_DISCONNECTED;
				Log.i(TAG, "Disconnected from GATT server.");
				broadcastUpdate(ACTION_GATT_DISCONNECTED);
			}
		}

		@Override
		public void onServicesDiscovered(BluetoothGatt gatt, int status) {
			if (status == BluetoothGatt.GATT_SUCCESS) {
				broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);
			} else {
				Log.w(TAG, "onServicesDiscovered received: " + status);
			}
		}

		@Override
		public void onCharacteristicRead(BluetoothGatt gatt,
										 BluetoothGattCharacteristic characteristic, int status) {
			if (status == BluetoothGatt.GATT_SUCCESS) {
				broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
			}
		}

		public void onCharacteristicWrite(BluetoothGatt gatt,
										  BluetoothGattCharacteristic characteristic, int status) {
			Log.d(TAG, "e");
		}

		public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {

		}

		@Override
		public void onCharacteristicChanged(BluetoothGatt gatt,
											BluetoothGattCharacteristic characteristic) {
			broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
		}
	};

	private void broadcastUpdate(final String action) {
		sendBroadcast(new Intent(action));
	}

	@SuppressLint("NewApi")
	private void broadcastUpdate(final String action,
								 final BluetoothGattCharacteristic characteristic) {

		final byte[] data = characteristic.getValue();
		if (data != null && data.length > 0) {
			StringBuilder stringBuilder = new StringBuilder(data.length);
			for(byte byteChar : data)
				stringBuilder.append(String.format("%02X ", byteChar));

			Log.e(TAG, stringBuilder.toString());
			stringBuilder = null;
//	          final Intent localIntent1 = new Intent(action);
//	          localIntent1.putExtra(EXTRA_DATA, new String(data) + "\n" + stringBuilder.toString());
//	          sendBroadcast(localIntent1);
		}

		if (UUID_SLAVE2HOST.equals(characteristic.getUuid())) {

			final byte[] arrayOfByte = characteristic.getValue();
			final int dataLen= 8;	//数据个数为8
			if (arrayOfByte != null && arrayOfByte.length == dataLen) {
				final int commandIndex = 3;	//命令数据索引
				final int data1Index = 6;	//命令数据索引
				final int sendResultCommand = 0x5A;
				final int data1 = 0x88 - 0xFF - 1;
				final int command = arrayOfByte[commandIndex];

				//处理错乱的情况(通常发生在app重新连接的时候)
				//例如: 01 FE 6A 75 5A 00 B0 88, 其中B0是测量结果
				if (arrayOfByte[0] == 0x01 && arrayOfByte[1] == -2/*0xFE*/ &&
						arrayOfByte[2] == 0x6A && arrayOfByte[3] == 0x75 &&
						arrayOfByte[4] == 0x5A && arrayOfByte[5] == 0x00 &&
						arrayOfByte[7] == data1) {
					//获取正数血糖值
					int glucose = (int) arrayOfByte[6];
					while (glucose < 0) {
						glucose += 256;
					}

					//发送数据
					final Intent localIntent = new Intent(action);
					localIntent.putExtra("State", "OK");
					localIntent.putExtra("Glucose", String.format("%d", glucose));
					sendBroadcast(localIntent);
				} else if (command == sendResultCommand && (int) arrayOfByte[data1Index] == data1) {	//判断是否发送结果命令
					//计算校验和
					int dataSum = 0xFF & arrayOfByte[commandIndex];
					for (int i = 1; i < arrayOfByte.length - 1; i++) {
						if (i != commandIndex) {
							dataSum += arrayOfByte[i];
						}
					}

					//判断校验和
					if ((byte) dataSum == arrayOfByte[arrayOfByte.length - 1]) {
						final int resultIndex = 5;	//结果数据索引
						final Intent localIntent = new Intent(action);

						//获取正数血糖值
						int glucose = (int) arrayOfByte[resultIndex];
						while (glucose < 0) {
							glucose += 256;
						}

						//发送数据
						localIntent.putExtra("State", "OK");
						localIntent.putExtra("Glucose", String.format("%d", glucose));
						sendBroadcast(localIntent);
					}
				}
			}
		}
	}

	public class LocalBinder extends Binder {
		public BluetoothLeServiceGlucose getService() {
			return BluetoothLeServiceGlucose.this;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		// After using a given device, you should make sure that
		// BluetoothGatt.close() is called
		// such that resources are cleaned up properly. In this particular
		// example, close() is
		// invoked when the UI is disconnected from the Service.
		close();
		return super.onUnbind(intent);
	}

	private final IBinder mBinder = new LocalBinder();

	/**
	 * Initializes a reference to the local Bluetooth adapter.
	 *
	 * @return Return true if the initialization is successful.
	 */
	@SuppressLint("NewApi")
	public boolean initialize() {
		// For API level 18 and above, get a reference to BluetoothAdapter
		// through
		// BluetoothManager.
		if (mBluetoothManager == null) {
			mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
			if (mBluetoothManager == null) {
				Log.e(TAG, "Unable to initialize BluetoothManager.");
				return false;
			}
		}

		mBluetoothAdapter = mBluetoothManager.getAdapter();
		if (mBluetoothAdapter == null) {
			Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
			return false;
		}

		return true;
	}

	/**
	 * Connects to the GATT server hosted on the Bluetooth LE device.
	 *
	 * @param address
	 *            The device address of the destination device.
	 *
	 * @return Return true if the connection is initiated successfully. The
	 *         connection result is reported asynchronously through the
	 *         {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
	 *         callback.
	 */
	@SuppressLint("NewApi")
	public boolean connect(final String address) {
		if (mBluetoothAdapter == null || address == null) {
			Log.w(TAG,
					"BluetoothAdapter not initialized or unspecified address.");
			return false;
		}

		// Previously connected device. Try to reconnect.
		if (mBluetoothDeviceAddress != null
				&& address.equals(mBluetoothDeviceAddress)
				&& mBluetoothGatt != null) {
			Log.d(TAG,
					"Trying to use an existing mBluetoothGatt for connection.");
			if (mBluetoothGatt.connect()) {
				mConnectionState = STATE_CONNECTING;
				return true;
			} else {
				return false;
			}
		}

		final BluetoothDevice device = mBluetoothAdapter
				.getRemoteDevice(address);
		if (device == null) {
			Log.w(TAG, "Device not found.  Unable to connect.");
			return false;
		}
		// We want to directly connect to the device, so we are setting the
		// autoConnect
		// parameter to false.
		mBluetoothGatt = device.connectGatt(this, true, mGattCallback);
		Log.d(TAG, "Trying to create a new connection.");
		mBluetoothDeviceAddress = address;
		mConnectionState = STATE_CONNECTING;
		return true;
	}

	/**
	 * Disconnects an existing connection or cancel a pending connection. The
	 * disconnection result is reported asynchronously through the
	 * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
	 * callback.
	 */
	public void disconnect() {
		if (mBluetoothAdapter == null || mBluetoothGatt == null) {
			Log.w(TAG, "BluetoothAdapter not initialized");
			return;
		}
		mBluetoothGatt.disconnect();
	}

	/**
	 * After using a given BLE device, the app must call this method to ensure
	 * resources are released properly.
	 */
	public void close() {
		if (mBluetoothGatt == null) {
			return;
		}
		mBluetoothGatt.close();
		mBluetoothGatt = null;
	}

	/**
	 * Request a read on a given {@code BluetoothGattCharacteristic}. The read
	 * result is reported asynchronously through the
	 * {@code BluetoothGattCallback#onCharacteristicRead(android.bluetooth.BluetoothGatt, android.bluetooth.BluetoothGattCharacteristic, int)}
	 * callback.
	 *
	 * @param characteristic
	 *            The characteristic to read from.
	 */
	public void readCharacteristic(BluetoothGattCharacteristic characteristic) {
		if (mBluetoothAdapter == null || mBluetoothGatt == null) {
			Log.w(TAG, "BluetoothAdapter not initialized");
			return;
		}
		mBluetoothGatt.readCharacteristic(characteristic);
	}

	@SuppressLint("NewApi")
	public void writeCharacteristic(BluetoothGattCharacteristic characteristic) {
		if (mBluetoothAdapter == null || mBluetoothGatt == null) {
			Log.w(TAG, "BluetoothAdapter not initialized");
			return;
		}
		mBluetoothGatt.writeCharacteristic(characteristic);
	}

	/**
	 * Enables or disables notification on a give characteristic.
	 *
	 * @param characteristic
	 *            Characteristic to act on.
	 * @param enabled
	 *            If true, enable notification. False otherwise.
	 */
	@SuppressLint("NewApi")
	public void setCharacteristicNotification(
			BluetoothGattCharacteristic characteristic, boolean enabled) {
		if (mBluetoothAdapter == null || mBluetoothGatt == null) {
			Log.w(TAG, "BluetoothAdapter not initialized");
			return;
		}
		mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);

		// This is specific to Heart Rate Measurement.
		if (UUID_SLAVE2HOST.equals(characteristic.getUuid())) {
			BluetoothGattDescriptor descriptor = characteristic
					.getDescriptor(UUID.fromString(SampleGattAttributes.CLIENT_CHARACTERISTIC_CONFIG));
			if(descriptor != null){
				descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
				mBluetoothGatt.writeDescriptor(descriptor);
			}
		}
	}

	/**
	 * Retrieves a list of supported GATT services on the connected device. This
	 * should be invoked only after {@code BluetoothGatt#discoverServices()}
	 * completes successfully.
	 *
	 * @return A {@code List} of supported services.
	 */
	public List<BluetoothGattService> getSupportedGattServices() {
		if (mBluetoothGatt == null)
			return null;

		return mBluetoothGatt.getServices();
	}

	@SuppressLint("NewApi")
	public BluetoothGattService getService(UUID paramUUID) {
		if (this.mBluetoothGatt == null)
			return null;
		return this.mBluetoothGatt.getService(paramUUID);
	}

	@SuppressLint("NewApi")
	public void test_send() {
		byte[] arrayOfByte = { -2, 106, 117, 90, 85, -86, -69, 0 };
		arrayOfByte[7] = ((byte) (0xFF & arrayOfByte[0] + arrayOfByte[1]
				+ arrayOfByte[2] + arrayOfByte[3] + arrayOfByte[4]
				+ arrayOfByte[5] + arrayOfByte[6]));
		if(null != h2s){
			this.h2s.setValue(arrayOfByte);
			writeCharacteristic(this.h2s);
			this.count = (1 + this.count);
			Log.d(TAG, "count=" + this.count);
		}
	}

	private void send_cmd(byte paramByte) {
		byte[] arrayOfByte = { 4, 0, 0, 0 };
		arrayOfByte[1] = -64;
		arrayOfByte[2] = paramByte;
		arrayOfByte[3] = ((byte) (0xFF & arrayOfByte[0] + arrayOfByte[1]
				+ arrayOfByte[2]));
		if(null != h2s) {
			this.h2s.setValue(arrayOfByte);
			writeCharacteristic(this.h2s);
		}
	}

	@SuppressLint("NewApi")
	public void send_poweroff() {
		send_cmd((byte) -90);
	}

	public void send_start() {
		send_cmd((byte) -95);
	}

	public void send_stop() {
		send_cmd((byte) -94);
	}
}
