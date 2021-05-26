package  com.xzy.forestSystem.gogisapi.GPS;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import com.xzy.forestSystem.PubVar;
import com.xzy.forestSystem.gogisapi.Common.Common;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class BluetoothGPSDevice extends GPSDevice {
    private Handler LinkDetectedHandler = new Handler() { // from class:  com.xzy.forestSystem.gogisapi.GPS.BluetoothGPSDevice.2
        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            try {
                if (msg.what == 0) {
                    Common.ShowToast(msg.obj.toString());
                } else if (msg.what == 10) {
                    BluetoothGPSDevice.this.connectedTime++;
                    if (BluetoothGPSDevice.this.connectedTime == BluetoothGPSDevice.this.maxAutoLinkTime) {
                        Common.ShowToast(String.valueOf(msg.obj.toString()) + "\n自动连接" + BluetoothGPSDevice.this.connectedTime + "此失败,请设置后再进行此操作.");
                        BluetoothGPSDevice.this.StopGPSDevice();
                        return;
                    }
                    Common.ShowToast(String.valueOf(msg.obj.toString()) + "\n10秒后程序将重新自动进行连接.");
                    new Thread(BluetoothGPSDevice.this.startConectRunnable).start();
                } else if (msg.what == 1) {
                    String tempMsg = msg.obj.toString();
                    if (!tempMsg.equals("")) {
                        NMEA0183Stand.ProcessData(BluetoothGPSDevice.this.gpsLocation, BluetoothGPSDevice.this, tempMsg);
                    }
                }
            } catch (Exception e) {
            }
        }
    };
    private String bluetoothAddress = "";
    public String bluetoothName = "";
    private clientThread clientConnectThread = null;
    private BluetoothDevice connectDevice = null;
    private int connectedTime = 0;
    private Context context = null;
    private boolean isStartConnect = false;
    private BluetoothAdapter mAdapter = null;
    private int maxAutoLinkTime = 5;
    private readThread mreadThread = null;
    private BluetoothSocket socket = null;
    private Runnable startConectRunnable = new Runnable() { // from class:  com.xzy.forestSystem.gogisapi.GPS.BluetoothGPSDevice.1
        @Override // java.lang.Runnable
        public void run() {
            try {
                Thread.sleep(10000);
                BluetoothGPSDevice.this.connectDevice();
            } catch (InterruptedException e) {
            }
        }
    };

    public BluetoothGPSDevice(Context context2, GPSLocationClass gpsLocation) {
        super(gpsLocation);
        this.deviceName = "蓝牙GPS设备";
        this.context = context2;
    }

    @Override //  com.xzy.forestSystem.gogisapi.GPS.GPSDevice
    public void startConnect() {
        String[] tempStrs;
        super.startConnect();
        this.connectedTime = 0;
        try {
            this.mAdapter = BluetoothAdapter.getDefaultAdapter();
            if (!this.mAdapter.isEnabled()) {
                this.mAdapter.enable();
            }
            String tempStr = PubVar._PubCommand.m_UserConfigDB.GetUserParam().GetUserPara("Tag_System_GPS").get("F4");
            if (tempStr != null && !tempStr.equals("") && (tempStrs = tempStr.split(";")) != null && tempStrs.length > 1) {
                this.bluetoothName = tempStrs[0];
                this.deviceName = String.valueOf(this.deviceName) + " [" + tempStr + "]";
                this.bluetoothAddress = tempStrs[1];
            }
            if (this.bluetoothName.equals("") || this.bluetoothAddress.equals("")) {
                Common.ShowDialog("设置的蓝牙GPS设备不存在,请重新设置.");
            } else {
                connectDevice();
            }
        } catch (Exception e) {
        }
    }

    @Override //  com.xzy.forestSystem.gogisapi.GPS.GPSDevice
    @SuppressLint({"NewApi"})
    public void StopGPSDevice() {
        if (!this.isDisposed) {
            super.StopGPSDevice();
            try {
                if (this.clientConnectThread != null && this.clientConnectThread.isAlive()) {
                    this.clientConnectThread.interrupt();
                }
            } catch (Exception e) {
            }
            try {
                if (this.mreadThread != null && this.mreadThread.isAlive()) {
                    this.mreadThread.interrupt();
                }
            } catch (Exception e2) {
            }
            try {
                if (this.socket != null) {
                    this.socket.close();
                }
            } catch (Exception e3) {
            }
            this.isDisposed = true;
        }
    }

    public void connectDevice() {
        disableConnect();
        try {
            if (!this.bluetoothAddress.equals("")) {
                this.isStartConnect = true;
                this.connectDevice = this.mAdapter.getRemoteDevice(this.bluetoothAddress);
                this.clientConnectThread = new clientThread(this, null);
                this.clientConnectThread.start();
            }
        } catch (Exception e) {
        }
    }

    @SuppressLint({"NewApi"})
    public void disableConnect() {
        this.isStartConnect = false;
        try {
            if (this.clientConnectThread != null && this.clientConnectThread.isAlive()) {
                this.clientConnectThread.interrupt();
            }
            if (this.mreadThread != null && this.mreadThread.isAlive()) {
                this.mreadThread.interrupt();
            }
            if (this.socket != null && this.socket.isConnected()) {
                this.socket.close();
            }
        } catch (IOException e) {
        }
    }

    /* access modifiers changed from: private */
    public class clientThread extends Thread {
        private clientThread() {
        }

        /* synthetic */ clientThread(BluetoothGPSDevice bluetoothGPSDevice, clientThread clientthread) {
            this();
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            BluetoothSocket bluetoothSocket = null;
            if (!BluetoothGPSDevice.this.isDisposed) {
                try {
                    BluetoothGPSDevice.this.socket = BluetoothGPSDevice.this.connectDevice.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
                    BluetoothGPSDevice.this.socket.connect();
                    BluetoothGPSDevice.this.mreadThread = new readThread(BluetoothGPSDevice.this, null);
                    BluetoothGPSDevice.this.mreadThread.start();
                    Message msg = new Message();
                    msg.obj = "连接设备[" + BluetoothGPSDevice.this.bluetoothName + "]成功!开始接收数据...";
                    msg.what = 0;
                    BluetoothGPSDevice.this.LinkDetectedHandler.sendMessage(msg);
                } catch (IOException e) {
                    try {
                        if (BluetoothGPSDevice.this.socket != null) {
                            BluetoothGPSDevice.this.socket.close();
                        }
                    } catch (Exception e2) {
                    } finally {
                        BluetoothGPSDevice.this.socket = bluetoothSocket;
                    }
                    Message msg2 = new Message();
                    msg2.obj = "连接蓝牙设备[" + BluetoothGPSDevice.this.bluetoothName + "]异常!\n请确保蓝牙设备已经开启并在附近可搜索范围内,或断开连接重新试一试.";
                    msg2.what = 10;
                    BluetoothGPSDevice.this.LinkDetectedHandler.sendMessage(msg2);
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public class readThread extends Thread {
        private readThread() {
        }

        /* synthetic */ readThread(BluetoothGPSDevice bluetoothGPSDevice, readThread readthread) {
            this();
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            if (!BluetoothGPSDevice.this.isDisposed) {
                byte[] buffer = new byte[1024];
                InputStream mmInStream = null;
                try {
                    mmInStream = BluetoothGPSDevice.this.socket.getInputStream();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                while (BluetoothGPSDevice.this.isStartConnect) {
                    try {
                        int bytes = mmInStream.read(buffer);
                        if (bytes > 0) {
                            byte[] buf_data = new byte[bytes];
                            for (int i = 0; i < bytes; i++) {
                                buf_data[i] = buffer[i];
                            }
                            String s = new String(buf_data);
                            Message msg = new Message();
                            msg.obj = s;
                            msg.what = 1;
                            BluetoothGPSDevice.this.LinkDetectedHandler.sendMessage(msg);
                        }
                    } catch (IOException e) {
                        try {
                            mmInStream.close();
                            return;
                        } catch (IOException e12) {
                            Message msg2 = new Message();
                            msg2.obj = "读取数据错误:" + e12.getMessage();
                            msg2.what = 0;
                            BluetoothGPSDevice.this.LinkDetectedHandler.sendMessage(msg2);
                            return;
                        }
                    }
                }
            }
        }
    }
}
