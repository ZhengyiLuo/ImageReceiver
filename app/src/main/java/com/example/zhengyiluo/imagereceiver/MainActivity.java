package com.example.zhengyiluo.imagereceiver;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "RECEIVER";
    public PreviewReceiver receiver;
    public ImageView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        view = (ImageView) findViewById(R.id.view);


        receiver = new PreviewReceiver(this);
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
