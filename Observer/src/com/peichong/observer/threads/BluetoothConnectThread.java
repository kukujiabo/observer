package com.peichong.observer.threads;

import android.bluetooth.BluetoothAdapter;


/*
 * 蓝牙链接线程
 * @author meroc
 */
public class BluetoothConnectThread implements Runnable {

	private String _macAddress = "";
	
	private boolean _connecting = false;
	
	private boolean _connected = false;
	
	private BluetoothAdapter _bAdapter;
	
	//private DeviceReceiver _dReciver;
	
	public BluetoothConnectThread (String mac) {
		
		_macAddress = mac;
		
	}
	
	public BluetoothConnectThread (String mac, BluetoothAdapter bAdapter) {
		
		_macAddress = mac;
		
		_bAdapter = bAdapter == null ? BluetoothAdapter.getDefaultAdapter() : bAdapter;
		
	}
	
	public void connect () {
		
		_connecting = true;
		
		_connected = false;
		
		if (_bAdapter != null) {
			
			if (!_bAdapter.isEnabled()) {
				
				_bAdapter.enable();
				
			}
			
		} else {
		
		
		
		}
		
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

}
