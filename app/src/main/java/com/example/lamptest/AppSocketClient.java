package com.example.lamptest;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AppSocketClient {
    private Socket socket;
    private ExecutorService service;
    private PrintWriter out;
    private BufferedReader in;

    private MutableLiveData<String> response = new MutableLiveData<>("");

    public AppSocketClient(String ip, int port){
        service = Executors.newFixedThreadPool(3);

        service.execute(() -> {
            try {
                socket = new Socket(ip, port);
                out = new PrintWriter(new BufferedOutputStream(socket.getOutputStream()));
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void sendMessage(String message){

        service.execute(() -> {
            out.println(message);
            out.flush();
        });
    }

    public void receiveMessage(){

        service.execute(new Runnable() {
            String received = "";

            @Override
            public void run() {

                try {
                    received = in.readLine();

                    while (received.equals("")) {
                        Thread.sleep(300);
                        received = in.readLine();
                    }

                    response.postValue(received);


                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }
        });

    }

    public LiveData<String> getResponse() {
        return response;
    }
}
