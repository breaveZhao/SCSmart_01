package com.socool.utilslibrary;

import android.content.Context;
import android.util.Log;

import com.inuker.bluetooth.library.BluetoothClient;
import com.inuker.bluetooth.library.Constants;
import com.inuker.bluetooth.library.connect.listener.BleConnectStatusListener;
import com.inuker.bluetooth.library.connect.options.BleConnectOptions;
import com.inuker.bluetooth.library.connect.response.BleConnectResponse;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleWriteResponse;
import com.inuker.bluetooth.library.model.BleGattProfile;
import com.inuker.bluetooth.library.search.SearchRequest;
import com.inuker.bluetooth.library.search.response.SearchResponse;

import java.util.UUID;

import static com.inuker.bluetooth.library.Constants.REQUEST_SUCCESS;
import static com.inuker.bluetooth.library.Constants.STATUS_CONNECTED;
import static com.inuker.bluetooth.library.Constants.STATUS_DISCONNECTED;

/**
 * Created by 李良海 on 2017/10/19.
 * Function：
 */

public class BleUtils {
    private static String TAG = "Ble_TAG";
    private static final String UUID_SERVICE = "0000fff0-0000-1000-8000-00805f9b34fb";
    private static final String UUID_CHA_RW = "0000fff1-0000-1000-8000-00805f9b34fb";
    private static final String UUID_CHA_NOT = "0000fff2-0000-1000-8000-00805f9b34fb";
    public static String mac = "";
    private static BluetoothClient mClient;
    private static final BleConnectStatusListener mBleConnectStatusListener = new BleConnectStatusListener() {

        @Override
        public void onConnectStatusChanged(String mac, int status) {
            if (status == STATUS_CONNECTED) {
                Log.e(TAG, "连接成功");

            } else if (status == STATUS_DISCONNECTED) {
                Log.e(TAG, "断开链接");
            }
        }
    };

    /**
     * 初始化蓝牙客户端
     */
    public static void InitBleClient(Context context) {
        if (mClient == null) {
            mClient = new BluetoothClient(context);
        }
    }


    /**
     * 搜索ble设备
     */
    public static void BleScan(SearchResponse searchResponse) {
        SearchRequest request = new SearchRequest.Builder()
                .searchBluetoothLeDevice(10000)      // 扫BLE设备2s
                .build();
        mClient.search(request, searchResponse);
    }


    /**
     * 连接设备
     */

    public static void BleConnect(final String mac, final String value) {
        /**
         * 判断mac地址是否有效*/
        if (stringIsMac(mac)) {
            int status = mClient.getConnectStatus(mac);
            /**
             * 判断设备是否连接上了*/
            if (status == Constants.STATUS_DEVICE_DISCONNECTED) {
                Log.e(TAG, "开始链接设备");
                BleConnectOptions options = new BleConnectOptions.Builder()
                        .setConnectRetry(3)   // 连接如果失败重试3次
                        .setConnectTimeout(30000)   // 连接超时30s
                        .setServiceDiscoverRetry(3)  // 发现服务如果失败重试3次
                        .setServiceDiscoverTimeout(20000)  // 发现服务超时20s
                        .build();
                mClient.registerConnectStatusListener(mac, mBleConnectStatusListener);
                mClient.connect(mac, options, new BleConnectResponse() {
                    @Override
                    public void onResponse(int code, BleGattProfile data) {
                        Log.e(TAG, "连接成功并且发现服务");
//                        setNotify(mac);
                        for (int i = 0; i < data.getServices().size(); i++) {
                            Log.e(TAG, "Serice = " + data.getServices().get(i));
                            for (int j = 0; j < data.getServices().get(i).getCharacters().size(); j++) {
                                Log.e(TAG, "Character = " + data.getServices().get(i).getCharacters().get(j));
                            }
                        }
                        writeIndication(mac, HexUtils.hexStringToBytes(value));
                    }
                });
            } else {
                Log.e(TAG, "设备不是断开状态");
            }

        } else {
            Log.e(TAG, "MAC地址无效");
        }

    }

    /**
     * 写入认证信息
     */
    public static void writeIndication(String mac, byte[] mbyte) {
        mClient.write(mac, UUID.fromString(UUID_SERVICE), UUID.fromString(UUID_CHA_RW), mbyte, new BleWriteResponse() {
            @Override
            public void onResponse(int code) {
                Log.e(TAG, "写入数据结果" + code);
                if (code == REQUEST_SUCCESS) {

                }
            }
        });
    }



    /**
     * 设备notify
     */
    public static void setNotify(String mac) {
        Log.e(TAG, "打开设备notify");
        mClient.notify(mac, UUID.fromString(UUID_SERVICE), UUID.fromString(UUID_CHA_NOT), new BleNotifyResponse() {
            @Override
            public void onNotify(UUID service, UUID character, byte[] value) {
                Log.e(TAG, "---打开--");
                for (int i = 0; i < value.length; i++) {
                    Log.e("TAG_ZY", value[0] + "");
                }
            }

            @Override
            public void onResponse(int code) {

                if (code == REQUEST_SUCCESS) {
                    Log.e("TAG_ZY", "---打开--" + code);
                }
            }
        });
    }


    /**
     * 判断设备的mac地址函数
     */
    private static boolean stringIsMac(String val) {
        String trueMacAddress = "([A-Fa-f0-9]{2}-){5}[A-Fa-f0-9]{2}";
        // 这是真正的MAV地址；正则表达式；
        if (val.replace(":", "-").matches(trueMacAddress)) {
            return true;
        } else {
            return false;
        }
    }
}
