package  com.xzy.forestSystem.gogisapi.GPS;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import com.xzy.forestSystem.PubVar;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.MyControls.ImageTextListAdapter;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import com.stczh.gzforestSystem.R;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class BluetoothGPSSelectDialog {
    private Handler LinkDetectedHandler = new Handler() { // from class:  com.xzy.forestSystem.gogisapi.GPS.BluetoothGPSSelectDialog.2
        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            try {
                if (msg.what == 0) {
                    Common.ShowToast(msg.obj.toString());
                } else if (msg.what == 1) {
                    BluetoothGPSSelectDialog.this.txtMsg.append(String.valueOf(msg.obj.toString()) + "\n");
                    BluetoothGPSSelectDialog.this.scrollViewMsg.scrollTo(0, BluetoothGPSSelectDialog.this.txtMsg.getHeight());
                }
            } catch (Exception e) {
            }
        }
    };
    private XDialogTemplate _Dialog = null;
    private clientThread clientConnectThread = null;
    private BluetoothDevice connectDevice = null;
    private String connectDeviceName = "";
    private Context context = null;
    private List<HashMap<String, Object>> deviceDataList = null;
    private Hashtable<String, String> devices = null;
    private boolean isStartConnect = false;
    private BluetoothAdapter mAdapter = null;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() { // from class:  com.xzy.forestSystem.gogisapi.GPS.BluetoothGPSSelectDialog.3
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context2, Intent intent) {
            try {
                String action = intent.getAction();
                if ("android.bluetooth.device.action.FOUND".equals(action)) {
                    BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
                    if (!BluetoothGPSSelectDialog.this.devices.contains(device.getName())) {
                        BluetoothGPSSelectDialog.this.devices.put(device.getName(), device.getAddress());
                    }
                } else if ("android.bluetooth.adapter.action.DISCOVERY_FINISHED".equals(action)) {
                    Common.ShowToast("蓝牙设备搜索完成.");
                    BluetoothGPSSelectDialog.this.refreshDevices();
                }
            } catch (Exception e) {
            }
        }
    };
    private ICallback m_Callback = null;
    private readThread mreadThread = null;
    private ListView myListView;
    private ICallback pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.GPS.BluetoothGPSSelectDialog.1
        @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
        public void OnClick(String paramString, Object pObject) {
            HashMap tempHash;
            if (paramString.equals("确定")) {
                BluetoothGPSSelectDialog.this.disableConnect();
                boolean tempTag = true;
                if (!(BluetoothGPSSelectDialog.this.deviceDataList == null || BluetoothGPSSelectDialog.this.selecteDeviceIndex == -1 || (tempHash = (HashMap) BluetoothGPSSelectDialog.this.deviceDataList.get(BluetoothGPSSelectDialog.this.selecteDeviceIndex)) == null)) {
                    tempTag = false;
                    GPSLocationClass.SaveGPSDeviceInfo("1", tempHash.get("D4") + ";" + tempHash.get("D5"));
                    PubVar._PubCommand.ProcessCommand("GPS_打开GPS");
                    BluetoothGPSSelectDialog.this._Dialog.dismiss();
                }
                if (tempTag) {
                    Common.ShowDialog("没有选择任何蓝牙设备对象.");
                }
            } else if (paramString.equals("返回")) {
                BluetoothGPSSelectDialog.this.disableConnect();
            }
        }
    };
    private ScrollView scrollViewMsg = null;
    private int selecteDeviceIndex = -1;
    private BluetoothSocket socket = null;
    private TextView txtMsg;

    public BluetoothGPSSelectDialog(Context _context) {
        String tempStr;
        String[] tempStrs;
        this.context = _context;
        this._Dialog = new XDialogTemplate(this.context);
        this._Dialog.SetLayoutView(R.layout.gps_bluetoothselect_dialog);
        this._Dialog.SetCaption("蓝牙GPS设备");
        this._Dialog.SetCallback(this.pCallback);
        this._Dialog.SetHeadButtons("1,2130837858,确定,确定", this.pCallback);
        ((Button) this._Dialog.findViewById(R.id.scanBluetooth)).setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.GPS.BluetoothGPSSelectDialog.4
            @Override // android.view.View.OnClickListener
            public void onClick(View arg0) {
                BluetoothGPSSelectDialog.this.mAdapter.startDiscovery();
                BluetoothGPSSelectDialog.this.context.registerReceiver(BluetoothGPSSelectDialog.this.mReceiver, new IntentFilter("android.bluetooth.device.action.FOUND"));
                BluetoothGPSSelectDialog.this.context.registerReceiver(BluetoothGPSSelectDialog.this.mReceiver, new IntentFilter("android.bluetooth.adapter.action.DISCOVERY_FINISHED"));
            }
        });
        this.txtMsg = (TextView) this._Dialog.findViewById(R.id.textViewRecData);
        this.scrollViewMsg = (ScrollView) this._Dialog.findViewById(R.id.sv_show);
        this.mAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!this.mAdapter.isEnabled()) {
            this.mAdapter.enable();
        }
        this.devices = new Hashtable<>();
        if (this.mAdapter != null) {
            Set<BluetoothDevice> pairedDevices = this.mAdapter.getBondedDevices();
            if (pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {
                    this.devices.put(device.getName(), device.getAddress());
                }
            }
        }
        this.myListView = (ListView) this._Dialog.findViewById(R.id.listView1);
        this.myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class:  com.xzy.forestSystem.gogisapi.GPS.BluetoothGPSSelectDialog.5
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
                try {
                    HashMap<String, String> map = (HashMap) BluetoothGPSSelectDialog.this.myListView.getItemAtPosition(arg2);
                    if (map != null) {
                        String tempName = map.get("D4");
                        if (map.get("D2").equals("true")) {
                            BluetoothGPSSelectDialog.this.connectDeviceName = "";
                            BluetoothGPSSelectDialog.this.disableConnect();
                        } else {
                            BluetoothGPSSelectDialog.this.connectDeviceName = tempName;
                            BluetoothGPSSelectDialog.this.disableConnect();
                            if (!BluetoothGPSSelectDialog.this.connectDeviceName.equals("")) {
                                BluetoothGPSSelectDialog.this.isStartConnect = true;
                                BluetoothGPSSelectDialog.this.connectDevice = BluetoothGPSSelectDialog.this.mAdapter.getRemoteDevice((String) BluetoothGPSSelectDialog.this.devices.get(BluetoothGPSSelectDialog.this.connectDeviceName));
                                BluetoothGPSSelectDialog.this.clientConnectThread = new clientThread(BluetoothGPSSelectDialog.this, null);
                                BluetoothGPSSelectDialog.this.clientConnectThread.start();
                            }
                        }
                    }
                } catch (Exception ex) {
                    Common.Log("CreateReport", "错误:" + ex.toString() + "-->" + ex.getMessage());
                } finally {
                    BluetoothGPSSelectDialog.this.refreshDevices();
                }
            }
        });
        HashMap<String, String> tempHashMap = PubVar._PubCommand.m_UserConfigDB.GetUserParam().GetUserPara("Tag_System_GPS");
        if (!(tempHashMap == null || (tempStr = tempHashMap.get("F4")) == null || (tempStrs = tempStr.split(";")) == null || tempStrs.length <= 0)) {
            this.connectDeviceName = tempStrs[0];
        }
        refreshDevices();
    }

    public void SetCallback(ICallback tmpICallback) {
        this.m_Callback = tmpICallback;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void refreshDevices() {
        try {
            this.deviceDataList = new ArrayList();
            this.selecteDeviceIndex = -1;
            try {
                if (this.devices.size() > 0) {
                    Bitmap tempBitmap = BitmapFactory.decodeResource(PubVar._PubCommand.m_Context.getResources(), R.drawable.bluetooth0148);
                    int tempI = 0;
                    for (String tempStr : this.devices.keySet()) {
                        HashMap tempHash = new HashMap();
                        tempHash.put("D1", String.valueOf(tempI));
                        if (this.connectDeviceName.equals(tempStr)) {
                            tempHash.put("D2", "true");
                            this.selecteDeviceIndex = tempI;
                        } else {
                            tempHash.put("D2", "false");
                        }
                        tempHash.put("D3", tempBitmap);
                        tempHash.put("D4", tempStr);
                        tempHash.put("D5", this.devices.get(tempStr));
                        this.deviceDataList.add(tempHash);
                        tempI++;
                    }
                }
            } catch (Exception e) {
            }
            ImageTextListAdapter tempAdpater = new ImageTextListAdapter(this.myListView.getContext(), this.deviceDataList, false, R.layout.x_imagetextlistview, new String[]{"D3", "D4"}, new int[]{R.id.imgtxt_itemimage1, R.id.imgtxt_itemtext1});
            tempAdpater.SetSelectItemIndex(this.selecteDeviceIndex);
            this.myListView.setAdapter((ListAdapter) tempAdpater);
        } catch (Exception ex) {
            Common.Log("refreshDevices", "错误:" + ex.toString() + "-->" + ex.getMessage());
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void disableConnect() {
        this.isStartConnect = false;
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
            this.socket = null;
        } catch (Exception e3) {
        }
    }

    /* access modifiers changed from: private */
    public class clientThread extends Thread {
        private clientThread() {
        }

        /* synthetic */ clientThread(BluetoothGPSSelectDialog bluetoothGPSSelectDialog, clientThread clientthread) {
            this();
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            try {
                BluetoothGPSSelectDialog.this.socket = BluetoothGPSSelectDialog.this.connectDevice.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
                BluetoothGPSSelectDialog.this.socket.connect();
                BluetoothGPSSelectDialog.this.mreadThread = new readThread(BluetoothGPSSelectDialog.this, null);
                BluetoothGPSSelectDialog.this.mreadThread.start();
                Message msg = new Message();
                msg.obj = "连接设备[" + BluetoothGPSSelectDialog.this.connectDeviceName + "]成功!开始接收数据...";
                msg.what = 0;
                BluetoothGPSSelectDialog.this.LinkDetectedHandler.sendMessage(msg);
            } catch (IOException e) {
                Message msg2 = new Message();
                msg2.obj = "尝试连接设备[" + BluetoothGPSSelectDialog.this.connectDeviceName + "]异常!请断开连接重新试一试.";
                msg2.what = 0;
                BluetoothGPSSelectDialog.this.LinkDetectedHandler.sendMessage(msg2);
            }
        }
    }

    /* access modifiers changed from: private */
    public class readThread extends Thread {
        private readThread() {
        }

        /* synthetic */ readThread(BluetoothGPSSelectDialog bluetoothGPSSelectDialog, readThread readthread) {
            this();
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            byte[] buffer = new byte[1024];
            InputStream mmInStream = null;
            try {
                mmInStream = BluetoothGPSSelectDialog.this.socket.getInputStream();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            while (BluetoothGPSSelectDialog.this.isStartConnect) {
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
                        BluetoothGPSSelectDialog.this.LinkDetectedHandler.sendMessage(msg);
                    }
                } catch (IOException e) {
                    try {
                        mmInStream.close();
                        return;
                    } catch (IOException e12) {
                        Message msg2 = new Message();
                        msg2.obj = "读取数据错误:" + e12.getMessage();
                        msg2.what = 0;
                        BluetoothGPSSelectDialog.this.LinkDetectedHandler.sendMessage(msg2);
                        return;
                    }
                }
            }
        }
    }

    public void ShowDialog() {
        this._Dialog.show();
    }
}
