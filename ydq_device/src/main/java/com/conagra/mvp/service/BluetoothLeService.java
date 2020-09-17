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
public class BluetoothLeService extends Service {
	private final static String TAG = BluetoothLeService.class.getSimpleName();

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

	public final static String ACTION_DATA_NOTIFY  = "com.example.bluetooth.le.ACTION_DATA_NOTIFY";
	public final static String ACTION_DATA_READBACK = "com.example.bluetooth.le.ACTION_DATA_READBACK";


	// 蓝牙血压计
	public static final UUID UUID_HOST2SLAVE = UUID
			.fromString(SampleGattAttributes.HOST2SLAVE);
	public static final UUID UUID_SLAVE2HOST = UUID
			.fromString(SampleGattAttributes.SLAVE2HOST);
	public static final UUID UUID_WANKANG_SEVICES = UUID
			.fromString(SampleGattAttributes.WANKANG_SEVICES);

	public final static UUID UUID_HEART_RATE_MEASUREMENT = UUID
			.fromString(SampleGattAttributes.HEART_RATE_MEASUREMENT);

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
		final Intent localIntent = new Intent(action);

		if (UUID_SLAVE2HOST.equals(characteristic.getUuid())) {
			byte[] arrayOfByte = characteristic.getValue();
			if (arrayOfByte != null && arrayOfByte.length > 3) {
				StringBuilder localStringBuilder = new StringBuilder(arrayOfByte.length);
				int i = arrayOfByte[0];
				int j = arrayOfByte[1];
				int command = arrayOfByte[2];

				if (i == 4) {
					if ((byte) (0xFF & command + (i + j)) == arrayOfByte[3]) {
						localStringBuilder.append("OK ");
						Object[] arrayOfObject10 = new Object[1];
						arrayOfObject10[0] = Byte.valueOf(command + "");
						Log.d(TAG, String.format("%02X ", arrayOfObject10));
						switch (command + 80) {
							case 0: // 血压计查询蓝牙是否连接
								localStringBuilder.append("waiting...");
								test_send();
								break;
							case 1: // 血压计通知开始测量
								localStringBuilder.append("start");
								break;
							case 2: // 血压计通知停止测量
								localStringBuilder.append("stop");
								//发送数据
								localIntent.putExtra("State", "Stop");
								sendBroadcast(localIntent);
								break;
							case 3: // 血压计通知正在归零
								localStringBuilder.append("return zero");
								break;
							case 4: // 血压计通知归零结束
								localStringBuilder.append("return zero end");
								break;
							case 10: // 血压计发送关机信号
								localStringBuilder.append("poweroff");
								break;
							case 11: // 血压计发送低电信号
								localStringBuilder.append("battery low");
								//发送数据
								localIntent.putExtra("State",
										"BatteryLow");
								sendBroadcast(localIntent);
								break;
							case 5: // 血压计回复电量
							case 6: // 血压计回复设备ID
							case 7: // 血压计发送测量过程数据
							case 8: // 血压计发送测量结果
							case 9: // 血压计发送测量错误信息
							default:
								break;
						}
					}
				} else if (i == 5) {
					int i9 = arrayOfByte[3];
					int i10 = 0xFF & i9 + (command + (i + j));
					Object[] arrayOfObject7 = new Object[1];
					arrayOfObject7[0] = Byte.valueOf(command + "");
					Log.d(TAG, String.format("%02X ", arrayOfObject7));
					int i11 = command + 80;
					if ((byte) i10 == arrayOfByte[4])
						switch (i11) {
							case 5:	//血压计回复电量
								Object[] arrayOfObject9 = new Object[1];
								arrayOfObject9[0] = Integer.valueOf(i9 & 0xFF);
								localStringBuilder.append("OK ");
								localStringBuilder.append(String.format(
										"battery level = %d", arrayOfObject9));
								break;
							case 9:	//血压计发送测量错误信息
								Object[] arrayOfObject8 = new Object[1];
								arrayOfObject8[0] = Byte.valueOf(i9 + "");
								localStringBuilder.append(String.format(
										"error 0x%02x", arrayOfObject8));

								//发送数据
								localIntent.putExtra("State",
										"Error");
								sendBroadcast(localIntent);
								break;
							default:
								break;
						}
				} else if (i == 6) {
					int i7 = arrayOfByte[3];
					int i8 = arrayOfByte[4];
					if ((byte) (0xFF & i8 + (i7 + (command + (i + j)))) == arrayOfByte[5]) {
						localStringBuilder.append("OK ");
						Object[] arrayOfObject5 = new Object[1];
						arrayOfObject5[0] = Byte.valueOf(command + "");
						Log.d(TAG, String.format("%02X ", arrayOfObject5));
						if (command == -73) {	//血压计发送测量过程数据
							Object[] arrayOfObject6 = new Object[2];
							arrayOfObject6[0] = Byte.valueOf(i7 + "");
							arrayOfObject6[1] = Integer.valueOf(i8 & 0xFF);
							localStringBuilder.append(String.format(
									"run 0x%02x %d", arrayOfObject6));
							//发送数据
							localIntent.putExtra("State", "Measuring");
							sendBroadcast(localIntent);
						}
					}
				} else if (i == 7) {
					int i4 = arrayOfByte[3];
					int i5 = arrayOfByte[4];
					int i6 = arrayOfByte[5];
					if ((byte) (0xFF & i6 + (i5 + (i4 + (command + (i + j))))) == arrayOfByte[6]) {
						localStringBuilder.append("OK ");
						Object[] arrayOfObject3 = new Object[1];
						arrayOfObject3[0] = Byte.valueOf(command + "");
						Log.d(TAG, String.format("%02X ", arrayOfObject3));
						if (command == -74) {	//血压计回复设备ID
							Object[] arrayOfObject4 = new Object[3];
							arrayOfObject4[0] = Byte.valueOf(i4 + "");
							arrayOfObject4[1] = Byte.valueOf(i5 + "");
							arrayOfObject4[2] = Byte.valueOf(i6 + "");
							localStringBuilder.append(String.format(
									"id %x%x%x", arrayOfObject4));
						}
					}
				} else if (i == 8) {
					int m = arrayOfByte[3];
					int n = arrayOfByte[4];
					int i1 = arrayOfByte[5];
					int i2 = arrayOfByte[6];
					if ((byte) (0xFF & i2 + (i1 + (n + (m + (command + (i + j)))))) == arrayOfByte[7]) {
						String str1 = TAG;
						localStringBuilder.append("OK ");
						Object[] arrayOfObject1 = new Object[1];
						arrayOfObject1[0] = Byte.valueOf(command + "");
						Log.d(str1, String.format("%02X ", arrayOfObject1));
						if (command == -72) {	//血压计发送测量结果
							int i3 = 256 * (m & 0x7F) + (n & 0xFF);
							Object[] arrayOfObject2 = new Object[3];
							arrayOfObject2[0] = Integer.valueOf(i3);	//收缩压
							arrayOfObject2[1] = Byte.valueOf(i1 + "");	//舒张压
							arrayOfObject2[2] = Byte.valueOf(i2 + "");	//心率值
							localStringBuilder.append(String.format(
									"Systolic=%d  Diastolic=%d Heart=%d",
									arrayOfObject2));
							//发送数据
							localIntent.putExtra("State",
									"OK");
							localIntent.putExtra("Systolic",
									arrayOfObject2[0].toString());
							localIntent.putExtra("Diastolic",
									arrayOfObject2[1].toString());
							localIntent.putExtra("Heart",
									arrayOfObject2[2].toString());
							sendBroadcast(localIntent);
						}
					}
				}

				Log.i(TAG, localStringBuilder.toString());
				localStringBuilder = null;
//				localIntent.putExtra(EXTRA_DATA,
//						localStringBuilder.toString());
//				sendBroadcast(localIntent);
			}
		}
	}

	public class LocalBinder extends Binder {
		public BluetoothLeService getService() {
			return BluetoothLeService.this;
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
		mBluetoothGatt = device.connectGatt(this, false, mGattCallback);
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
					.getDescriptor(UUID
							.fromString(SampleGattAttributes.CLIENT_CHARACTERISTIC_CONFIG));
			descriptor
					.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
			mBluetoothGatt.writeDescriptor(descriptor);
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
		byte[] arrayOfByte = { 4, 0, -96, 0, 4, 0, -91, 0 };
		arrayOfByte[5] = -64;
		arrayOfByte[1] = -64;
		arrayOfByte[3] = ((byte) (0xFF & arrayOfByte[0] + arrayOfByte[1]
				+ arrayOfByte[2]));
		arrayOfByte[7] = ((byte) (0xFF & arrayOfByte[4] + arrayOfByte[5]
				+ arrayOfByte[6]));
		if(h2s != null){
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
		if(this.h2s != null){
			this.h2s.setValue(arrayOfByte);
			writeCharacteristic(this.h2s);
		}

	}

	@SuppressLint("NewApi")
	public void send_poweroff() {
		send_cmd((byte) -90);
	}

	/**
	 * 开始
	 */
	public void send_start() {
		send_cmd((byte) -95);
	}

	/**
	 * 停止
	 */
	public void send_stop() {
		send_cmd((byte) -94);
	}
}
