package com.app.donteatalone.model;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;

/**
 * Created by ChomChom on 5/18/2017
 */

public class CustomSocket {
    private Socket mSocket;
    public CustomSocket() throws URISyntaxException {
        try {
            mSocket = IO.socket("https://firstapp0609.herokuapp.com/");//KTX's Nga

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public Socket getmSocket(){
        return mSocket;
    }
}
