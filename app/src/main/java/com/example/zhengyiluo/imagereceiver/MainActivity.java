package com.example.zhengyiluo.imagereceiver;

import android.graphics.Bitmap;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "RECEIVER";
    public PreviewReceiver receiver;
    public ImageView view;
    public WifiManager mWifiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mWifiManager = (WifiManager) getSystemService(this.WIFI_SERVICE);

        TextView ipview = (TextView) findViewById(R.id.textView);


        DhcpInfo dhcp = mWifiManager.getDhcpInfo();
        int dhc = dhcp.serverAddress;

        String dhcS = (dhc & 0xFF) + "." + ((dhc >> 8) & 0xFF) + "." + ((dhc >> 16) & 0xFF) + "." + ((dhc >> 24) & 0xFF);
        Log.d(TAG, "IP: " + dhcS);
        view = (ImageView) findViewById(R.id.view);

        ipview.setText(dhcS);

        receiver = new PreviewReceiver(this, dhcS);
        Thread imageDisply = new Thread(receiver);
        imageDisply.start();
    }

    public void setBitmap(final Bitmap bmp) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                view.setImageBitmap(bmp);
            }
        });

    }


}
