package com.example.myapplicationdebug.ui.notifications;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import android.Manifest;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DeviceInfoViewModel extends AndroidViewModel {
    private BluetoothAdapter bluetoothAdapter;
    private MutableLiveData<List<BluetoothDevice>> devicesLiveData;

    public DeviceInfoViewModel(Application application) {
        super(application);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        devicesLiveData = new MutableLiveData<>();
    }

    public LiveData<List<BluetoothDevice>> getDevices() {
        return devicesLiveData;
    }

    public void loadDevices(Context context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_ADMIN) == PackageManager.PERMISSION_GRANTED) {
            if (bluetoothAdapter != null && bluetoothAdapter.isEnabled()) {
                Set<BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();
                List<BluetoothDevice> devicesList = new ArrayList<>(bondedDevices);
                devicesLiveData.setValue(devicesList);  // 更新 LiveData
            } else {
                // 处理蓝牙不可用的情况
                devicesLiveData.setValue(new ArrayList<>());  // 发送空列表表示没有设备或蓝牙未开启
            }
        } else {
            // 处理无权限的情况
            devicesLiveData.setValue(new ArrayList<>());  // 发送空列表表示没有设备或无权限
        }
    }}