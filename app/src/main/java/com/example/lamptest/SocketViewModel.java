package com.example.lamptest;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class SocketViewModel extends ViewModel {
    private AppSocketClient client;

    public void init(String ip, int port){
        if (client == null) {
            client = new AppSocketClient(ip, port);
        }
    }

    public void sendMessage(String message){
        client.sendMessage(message);
    }

    public LiveData<String> getResponse(){
        return client.getResponse();
    }

    public void receiveMessage(){
        client.receiveMessage();
    }

}
