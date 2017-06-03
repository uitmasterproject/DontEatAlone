package com.app.donteatalone.model;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;

/**
 * Created by ChomChom on 5/18/2017.
 */

public class CustomSocket {
    private Socket mSocket;
    public CustomSocket() throws URISyntaxException {
        try {
            //mSocket = IO.socket("http://10.86.85.72:3000"); //FPT
            mSocket = IO.socket("http://10.0.128.134:3000");//KTX
            //mSocket = IO.socket("http://192.168.28.232:3000");//Feel
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public Socket getmSocket(){
        return mSocket;
    }
}
