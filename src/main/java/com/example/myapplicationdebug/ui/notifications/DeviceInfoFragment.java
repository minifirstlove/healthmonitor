package com.example.myapplicationdebug.ui.notifications;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplicationdebug.R;

public class DeviceInfoFragment extends Fragment {

    private BluetoothAdapter mBluetoothAdapter;
    private DeviceInfoViewModel mViewModel;
    private static final int REQUEST_ENABLE_BT = 1;
    private static final int REQUEST_PERMISSION_BT_CONNECT = 101;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            showUnsupportedDeviceMessage();
            return;
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_device_info, container, false);
        mViewModel = new ViewModelProvider(this).get(DeviceInfoViewModel.class);
        rootView.findViewById(R.id.btnCheckPermissions).setOnClickListener(v -> checkBluetoothPermissionsAndLoadDevices());
        observeViewModel();  // 确保添加这一行
        return rootView;
    }

    private void observeViewModel() {
        mViewModel.getDevices().observe(getViewLifecycleOwner(), devices -> {
            if (devices.isEmpty()) {
                Toast.makeText(getContext(), "没有配对的设备。", Toast.LENGTH_SHORT).show();
            } else {
                for (BluetoothDevice device : devices) {
                    displayDeviceInfo(device);
                }
            }
        });
    }

    private void displayDeviceInfo(BluetoothDevice device) {
        View view = getView();
        if (view != null) {
            TextView tvDeviceName = view.findViewById(R.id.tvDeviceName);
            TextView tvDeviceAddress = view.findViewById(R.id.tvDeviceAddress);
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {
                tvDeviceName.setText(String.format("设备名称: %s", device.getName()));
                tvDeviceAddress.setText(String.format("设备地址: %s", device.getAddress()));
            } else {
                tvDeviceName.setText("设备名称: 权限不足");
                tvDeviceAddress.setText("设备地址: 权限不足");
                requestBluetoothPermission();
            }
        }
    }

    private void requestBluetoothPermission() {
        requestPermissions(new String[]{Manifest.permission.BLUETOOTH_CONNECT}, REQUEST_PERMISSION_BT_CONNECT);
    }

    @Override
    public void onResume() {
        super.onResume();
        checkBluetoothPermissionsAndLoadDevices();
    }

    private void checkBluetoothPermissionsAndLoadDevices() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            // For Android 11 and below
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN}, REQUEST_ENABLE_BT);
            } else {
                mViewModel.loadDevices(requireContext());
            }
        } else {
            // For Android 12 and above
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.BLUETOOTH_CONNECT}, REQUEST_PERMISSION_BT_CONNECT);
            } else {
                mViewModel.loadDevices(requireContext());
            }
        }
    }

    private void requestEnableBluetooth() {
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
    }

    private void showUnsupportedDeviceMessage() {
        Activity activity = getActivity();
        if (activity != null) {
            Toast.makeText(activity, "This device does not support Bluetooth", Toast.LENGTH_LONG).show();
            activity.finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_ENABLE_BT && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            mViewModel.loadDevices(requireContext());
        } else if (requestCode == REQUEST_PERMISSION_BT_CONNECT && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            mViewModel.loadDevices(requireContext());
        } else {
            Toast.makeText(getActivity(), "Bluetooth permission is required to access devices.", Toast.LENGTH_SHORT).show();
        }
    }
}