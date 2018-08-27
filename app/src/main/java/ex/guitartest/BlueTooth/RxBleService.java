package ex.guitartest.BlueTooth;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import rx.functions.Action1;

public class RxBleService extends Service {
    private RxBle mrxBle = RxBle.getInstance();
    private String maddress;
    private String TAG = this.getClass().getName();
    public boolean hereOver = false;
    //communication
    private OnMessageReceiveListener monMessageReceiveListener;

    private boolean msendDataState;
    private RxBleServiceReceiver mreceiver = new RxBleServiceReceiver();
    private RxBle.BleScanListener mscanListener = new RxBle.BleScanListener() {
        @Override
        public void onBleScan(BluetoothDevice bleDevice, int rssi, byte[] scanRecord) {
            //LeScan have set default scanType to Le
            if (bleDevice.getBondState() != BluetoothDevice.BOND_BONDED && bleDevice.getBondState() != BluetoothDevice.BOND_BONDING) {
                if ((maddress!=null&&maddress.equals(bleDevice.getAddress())) || bleDevice.getName().contains("Smart_Guitar"))
                {
                    Log.d(TAG,"find device!");
                    mrxBle.connectDevice(bleDevice);
                }

            }
        }
    };

    public RxBleService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        maddress = null;

        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        readyBond();
    }


    @Override
    public void onDestroy() {
        terminateBle();
        terminateReceiver();
        Handler handler=new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            public void run() {
                Toast.makeText(getApplication(), "Server is destoried", Toast.LENGTH_SHORT).show();
            }
        });
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void terminateBle() {
        if (mrxBle != null) {
            mrxBle.closeBle();
        }
    }

    private void terminateReceiver() {
        if (mreceiver != null) {
            unregisterReceiver(mreceiver);
        }
    }
    public void reScan(){
        if(mrxBle!=null){
            mrxBle.scanBleDevices(false);
            mrxBle.setScanListener(mscanListener);
            mrxBle.scanBleDevices(true);

        }
    }

    public void readyBond() {
        //TODO permission manage

        Intent requestBluetoothOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        requestBluetoothOn.setAction(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        requestBluetoothOn.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 250);
        startActivity(requestBluetoothOn);
        //TODO state manage and distribute
        if (mrxBle != null) {
            mrxBle.openBle(this);
            mrxBle.setScanListener(mscanListener);
            mrxBle.scanBleDevices(true);

            hereOver = true;
        }
    }

    /**
     * default delay set to 0
     * @param data command provided in Command
     * @return sendData state on return
     */
    public boolean sendCommand(byte[] data){
        return sendCommand(data,0);
    }

    /**
     * send command
     *
     * @param data constant in Command
     * @param delay  milliseconds
     * @return state success or not
     */
    public boolean sendCommand(byte[] data, final int delay) {
        mrxBle.sendData(data,0);
        return msendDataState;
    }
    /**
     * Deal with data start if OnReceivedOkListener is set
     */
    public void dealWithData() {
            mrxBle.receiveData().subscribe(new Action1<byte[]>() {
                @Override
                public void call(byte[] bytes) {
                    //TODO: receive
                }
            });

    }

    /**
     * set callback to bluetooth scan, or apply default scan listener
     *
     * @param scanListener
     */
    public void setScanListener(RxBle.BleScanListener scanListener) {
        mscanListener = scanListener;
    }



    /**
     * have RxBle deal with inner logic
     */
    public void setOnMessageReceiveListener(OnMessageReceiveListener listener) {
        monMessageReceiveListener = listener;
    }

    public interface OnMessageReceiveListener {
        void onMessageReceived(int action, byte data[]);
    }

    private class RxBleServiceReceiver extends BroadcastReceiver {
        //TODO design inner receiver to broadcast
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
        }
    }

    public static RxBleService getInstance() {
        return Singleton.INSTANCE;
    }

    private static class Singleton {
        private static final RxBleService INSTANCE = new RxBleService();
    }
}
