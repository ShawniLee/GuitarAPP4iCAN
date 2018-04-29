package ex.guitartest.BlueTooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

public class RxBle {
    public static final UUID CCCD = java.util.UUID
            .fromString("00002902-0000-1000-8000-00805f9b34fb");
    public static final String RX_CHAR_UUID =
            "6e400002-b5a3-f393-e0a9-e50e24dcca9e";
    public static final String TX_CHAR_UUID = "6e400003-b5a3-f393-e0a9-e50e24dcca9e";
    private static final long SCAN_PERIOD = 5000;
    private static final String TAG = RxBle.class.getSimpleName();
    private static final String UUID = "0000ffe1-0000-1000-8000-00805f9b34fb";
    private Context mContext;

    private String CONNECTION_STATE;
    private BluetoothAdapter mBleAdapter;
    private boolean mIsScanning;
    private static String sTargetDeviceName;
    private BluetoothGatt mBleGatt;
    private BluetoothGattCharacteristic mBleGattChar;

    private boolean msendDataState;
    private Subject<byte[], byte[]> mBus;
    private BluetoothGattCharacteristic mwriteChar;
    public interface BleScanListener {
        /**
         * Callback in BLE scanning,then you should use the method {@link #connectDevice} to connect target device
         * @param bleDevice Identifies the remote device
         * @param rssi The RSSI value for the remote device as reported by the Bluetooth hardware. 0 if no RSSI value is available
         * @param scanRecord The content of the advertisement record offered by the remote device.
         */
        void onBleScan(BluetoothDevice bleDevice, int rssi, byte[] scanRecord);
    }

    private BleScanListener mScanListener;

    /**
     * Set listener on device scanning
     * @param scanListener Listener of scaning
     */
    public void setScanListener(BleScanListener scanListener) {
        mScanListener = scanListener;
    }

    private RxBle() {
        mBus = new SerializedSubject<>(PublishSubject.<byte[]>create());
    }

    public static RxBle getInstance() {
        return Singleton.INSTANCE;
    }

    public RxBle setTargetDevice(String deviceName) {
        sTargetDeviceName = deviceName;
        return Singleton.INSTANCE;
    }

    private static class Singleton {
        private static final RxBle INSTANCE = new RxBle();
    }

    public void openBle(Context context) {
        mContext = context.getApplicationContext();
        BluetoothManager bluetoothManager =
                (BluetoothManager) this.mContext.getSystemService(Context.BLUETOOTH_SERVICE);
        mBleAdapter = bluetoothManager.getAdapter();
        if (mBleAdapter != null) {
            mBleAdapter.enable();
        }
    }

    public void scanBleDevices(boolean enable) {
        if (enable) {
            Log.d(TAG, "scanBleDevices");
            mIsScanning = true;
            mBleAdapter.startLeScan(mBleScanCallback);
            Observable.timer(SCAN_PERIOD, TimeUnit.MILLISECONDS).subscribe(new Action1<Long>() {
                @Override
                public void call(Long aLong) {
                    mIsScanning = false;
                    mBleAdapter.stopLeScan(mBleScanCallback);
                    if(mBleAdapter.isDiscovering()){
                        mBleAdapter.cancelDiscovery();
                    }
                }
            });
        } else {
            mIsScanning = false;
            mBleAdapter.stopLeScan(mBleScanCallback);
        }
    }

    /**
     * Callback when scanning BLE devices
     */
    private BluetoothAdapter.LeScanCallback mBleScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice bleDevice, int rssi, byte[] scanRecord) {
            if (mIsScanning) {
                if (bleDevice.getName() != null) {
                    Log.d(TAG, "onLeScan: find device: " + bleDevice.getName());
                    if (sTargetDeviceName != null && sTargetDeviceName.equals(bleDevice.getName())) {
                        connectDevice(bleDevice);
                    } else if (mScanListener != null) {
                        mScanListener.onBleScan(bleDevice, rssi, scanRecord);
                    }
                }
            } else {
                Log.d(TAG, "onLeScan: stop scan");
            }
        }
    };


    /**
     * Connect BLE devices
     *
     * @param bleDevice Target device you want to connect
     * @return connectDevice State
     */
    public boolean connectDevice(BluetoothDevice bleDevice) {
        //TODO detect the state change
        scanBleDevices(false);
        mBleGatt = bleDevice.connectGatt(mContext,
                true,//true mean that can auto reconnect after disconnect
                new BleGattCallback());
        if(mBleGatt!=null){
            Log.d(TAG, "connectDevice: start to connect " + mBleGatt.getDevice().getName());
        mBleGatt.connect();
        return true;}
        else{
            return false;
        }


    }

    /**
     * Callback after device has been connected
     */
    private class BleGattCallback extends BluetoothGattCallback {

        @Override
        public void onConnectionStateChange(BluetoothGatt bleGatt, int status, int newState) {
            super.onConnectionStateChange(bleGatt, status, newState);
            Log.d(TAG, "onConnectionStateChange: " + newState);
            if (newState == BluetoothGatt.STATE_CONNECTED) {
                CONNECTION_STATE = "STATE_CONNECTED";
                Log.d(TAG, "onConnectionStateChange: device connected");
                //Discover services will call the next override method: onServicesDiscovered
                bleGatt.discoverServices();
            } else if (newState == BluetoothGatt.STATE_DISCONNECTED) {
                CONNECTION_STATE = "STATE_DISCONNECTED";
                Log.d(TAG, "onConnectionStateChange: device disconnected");
            }
        }

        @Override
        public void onServicesDiscovered(final BluetoothGatt bleGatt, int status) {
            Log.d(TAG, "onServicesDiscovered: services size: " + bleGatt.getServices().size());
            List<BluetoothGattService> serviceList = bleGatt.getServices();
            //TODO 发现服务
            Observable.from(serviceList)
                    .flatMap(new Func1<BluetoothGattService, Observable<BluetoothGattCharacteristic>>() {
                        @Override
                        public Observable<BluetoothGattCharacteristic> call(BluetoothGattService bleGattService) {
                            return Observable.from(bleGattService.getCharacteristics());
                        }
                    })
                    .filter(new Func1<BluetoothGattCharacteristic, Boolean>() {
                        @Override
                        public Boolean call(BluetoothGattCharacteristic bleGattChar) {
                            return bleGattChar.getUuid().toString().equals(TX_CHAR_UUID);
                        }
                    })
                    .subscribe(new Action1<BluetoothGattCharacteristic>() {
                        @Override
                        public void call(BluetoothGattCharacteristic bleGattChar) {
                            mBleGattChar = bleGattChar;


                            setTxNotification(0);
                        }
                    });
            Observable.from(serviceList)
                    .flatMap(new Func1<BluetoothGattService, Observable<BluetoothGattCharacteristic>>() {
                        @Override
                        public Observable<BluetoothGattCharacteristic> call(BluetoothGattService bleGattService) {
                            return Observable.from(bleGattService.getCharacteristics());
                        }
                    })
                    .filter(new Func1<BluetoothGattCharacteristic, Boolean>() {
                        @Override
                        public Boolean call(BluetoothGattCharacteristic bleGattChar) {
                            return bleGattChar.getUuid().toString().equals(RX_CHAR_UUID);
                        }
                    })
                    .subscribe(new Action1<BluetoothGattCharacteristic>() {
                        @Override
                        public void call(BluetoothGattCharacteristic bleGattChar) {
                            mwriteChar = bleGattChar;
                        }
                    });
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            Log.d(TAG, "onCharacteristicChanged");
            byte receiveData[] = characteristic.getValue();
            Log.d(TAG, "receive BLE 's data :" + receiveData);
            Observable.just(receiveData)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<byte[]>() {
                        @Override
                        public void call(byte[] receiveData) {
                            mBus.onNext(receiveData);
                        }
                    });
        }
    }
    /**
     * Set TxNotification as to establish communication
     * @param delay unit:Milli Second
     * @return res as result of success | failure
     */
    public boolean setTxNotification(final int delay){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                Thread.sleep(delay);}
                catch (InterruptedException e){
                    e.printStackTrace();
                }
                //TODO receive logic
                mBleGatt.setCharacteristicNotification(mBleGattChar, true);
                BluetoothGattDescriptor descriptor = mBleGattChar.getDescriptor(CCCD);
                descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                mBleGatt.writeDescriptor(descriptor);
            }
        }).start();
       return true;
    }
    /**
     * Send data to BLE device with delay
     *
     * @param data byte[] will be send to BLE device
     * @param time Delay time , milliseconds
     */
    public boolean sendData(final byte[] data,int time) {
        if (!mBleAdapter.isEnabled() || mBleAdapter == null||mBleGatt==null || !mBleGatt.connect()) {
            Log.d(TAG, "sendData: BLE is disconnected");
            Toast.makeText(mContext,"sendData: BLE is disconnected",Toast.LENGTH_SHORT).show();
            scanBleDevices(true);
            return false;
        }
        Observable.timer(time, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long l) {
                        //TODO write logic
                        if (mBleGatt != null && mwriteChar != null) {
                            mwriteChar.setValue(data);
                            boolean isSend = mBleGatt.writeCharacteristic(mwriteChar);
                            msendDataState=isSend;
                            Log.d(TAG, "send " + (isSend ? "success" : "fail"));
                        }
                    }
                });
        //TODO how to deal with async response?
        return msendDataState;
    }

    /**
     * Receive the data from BLE device
     *
     * @return Subject you should subscribed
     */
    public Observable<byte[] > receiveData() {
        return mBus;
    }

    public void closeBle() {
        Log.d(TAG, "close BLE");
        if (mBleGatt != null) {
            mBleGatt.close();
            mBleGatt.disconnect();
            mBleGatt=null;
        }
        if (mBleAdapter != null) {
            mBleAdapter.cancelDiscovery();
        }
        mBus.onCompleted();
    }

}