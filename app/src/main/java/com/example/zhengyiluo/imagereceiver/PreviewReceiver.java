package com.example.zhengyiluo.imagereceiver;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

/**
 * Created by ZhengyiLuo on 1/2/16.
 */
public class PreviewReceiver implements Runnable {

    private Context context;
    private MainActivity mActivity;
    private TextView statusText;
    public final int port = 8888;
    byte[] buffer = new byte[99999];
    Bitmap b;
    boolean receiveing = true;
    OutputStream outputStream;
    DatagramSocket s;

    public PreviewReceiver(MainActivity mActivity) {
        this.mActivity = mActivity;
    }


    @Override
    public void run() {

        try {

            Log.d(mActivity.TAG, "Start");
            // Log.d(mActivity.TAG, "IP: " + getIpAddress());


            /**
             * Create a server socket and wait for client connections. This
             * call blocks until a connection is accepted from a client
             */
            InetAddress serveraddr = InetAddress.getByName("192.168.49.226");
            s = new DatagramSocket(port, serveraddr);

            try {
                /**
                 * If this code is reached, a client has connected and transferred data
                 */
//                Log.d(mActivity.TAG, "We are receiving");

                copyFile();
                b.recycle();

            } catch (Exception e) {
                Log.d(mActivity.TAG, "Exception!!!!" + e.toString());
                e.printStackTrace();
            }
//                try {
//                    wait(100);
//                } catch (Exception e) {
//
//                }


        } catch (IOException e) {
            Log.e(null, e.getMessage());
        }
    }

    public boolean copyFile() {
        Log.d(mActivity.TAG, "Getting File");

        int len, count = 0;
        try {
            // inputStream.read(buffer);
            byte[] byteArr = new byte[0];
            //inputStream.read(buffer);

            while (receiveing) {
                //     Log.d(mActivity.TAG, "Getting File Continusouly");
                DatagramPacket p = new DatagramPacket(buffer, buffer.length);
                s.receive(p);
                byteArr = p.getData();
                b = BitmapFactory.decodeByteArray(byteArr, 0, byteArr.length);
                //    Log.d(mActivity.TAG, "ByteLength" + Integer.toString(byteArr.length) + "Byte Data" + Byte.toString(byteArr[99]));

                mActivity.setBitmap(b);
                // Thread.sleep(100);
            }


            // Log.d(mActivity.TAG, "We are reading and this is what we read");


//                for (int i = 0; i < buffer.length; i++) {
//                    Log.d(mActivity.TAG, Byte.toString(buffer[i]));
//                }

        } catch (Exception e) {
            Log.d(mActivity.TAG, e.toString());
        }


//        BitmapFactory.Options options = new BitmapFactory.Options();// Create object of bitmapfactory's option method for further option use
//        options.inPurgeable = true; // inPurgeable is used to free up memory while required
//        Bitmap songImage1 = BitmapFactory.decodeByteArray(thumbnail, 0, thumbnail.length, options);//Decode image, "thumbnail" is the object of image file
//        Bitmap songImage = Bitmap.createScaledBitmap(songImage1, 50, 50, true);// convert decoded bitmap into well scalled Bitmap format.

        //   b = BitmapFactory.decodeStream(inputStream);
//        b = BitmapFactory.decodeByteArray(buffer, 0, buffer.length);
//        Log.d(mActivity.TAG, Byte.toString(buffer[99]));
//        try {
//            inputstream.reset();
//        } catch (Exception e) {
//
//
//        }
//
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        if (b == null) {
//            Log.d(mActivity.TAG, "Fail dcode image!!!!");
//            return false;
//        }
//        b.compress(Bitmap.CompressFormat.WEBP, 100, stream);
//        byte[] byteArray = stream.toByteArray();
//        Log.d(mActivity.TAG, "This is what we received:");
//        Log.d(mActivity.TAG, Byte.toString(byteArray[99]));
//        System.gc();
        return true;
    }

    public String getMACAddress(String interfaceName) {
        try {
            List<NetworkInterface> interfaces = Collections
                    .list(NetworkInterface.getNetworkInterfaces());

            for (NetworkInterface intf : interfaces) {
                if (interfaceName != null) {
                    if (!intf.getName().equalsIgnoreCase(interfaceName))
                        continue;
                }
                byte[] mac = intf.getHardwareAddress();
                if (mac == null)
                    return "";
                StringBuilder buf = new StringBuilder();
                for (int idx = 0; idx < mac.length; idx++)
                    buf.append(String.format("%02X:", mac[idx]));
                if (buf.length() > 0)
                    buf.deleteCharAt(buf.length() - 1);
                return buf.toString();
            }
        } catch (Exception ex) {
        } // for now eat exceptions
        return "";
        /*
         * try { // this is so Linux hack return
         * loadFileAsString("/sys/class/net/" +interfaceName +
         * "/address").toUpperCase().trim(); } catch (IOException ex) { return
         * null; }
         */
    }

    public String getIpAddress() {
        try {
            List<NetworkInterface> interfaces = Collections
                    .list(NetworkInterface.getNetworkInterfaces());
        /*
         * for (NetworkInterface networkInterface : interfaces) { Log.v(TAG,
         * "interface name " + networkInterface.getName() + "mac = " +
         * getMACAddress(networkInterface.getName())); }
         */

            for (NetworkInterface intf : interfaces) {
                if (!getMACAddress(intf.getName()).equalsIgnoreCase(
                        null)) {
                    // Log.v(TAG, "ignore the interface " + intf.getName());
                    // continue;
                }
                if (!intf.getName().contains("p2p"))
                    continue;

                Log.v(mActivity.TAG,
                        intf.getName() + "   " + getMACAddress(intf.getName()));

                List<InetAddress> addrs = Collections.list(intf
                        .getInetAddresses());

                for (InetAddress addr : addrs) {
                    // Log.v(TAG, "inside");

                    if (!addr.isLoopbackAddress()) {
                        // Log.v(TAG, "isnt loopback");
                        String sAddr = addr.getHostAddress().toUpperCase();
                        Log.v(mActivity.TAG, "ip=" + sAddr);


                        if (true) {
                            if (sAddr.contains("192.168.49.")) {
                                Log.v(mActivity.TAG, "ip = " + sAddr);
                                return sAddr;
                            }
                        }

                    }

                }
            }

        } catch (Exception ex) {
            Log.v(mActivity.TAG, "error in parsing");
        } // for now eat exceptions
        Log.v(mActivity.TAG, "returning empty ip address");
        return "";
    }

}



