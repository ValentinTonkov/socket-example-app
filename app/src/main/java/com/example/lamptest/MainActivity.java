package com.example.lamptest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;

import com.example.lamptest.databinding.ActivityMainBinding;

import java.io.BufferedOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private boolean lampStatus = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        SocketViewModel model = new ViewModelProvider(this).get(SocketViewModel.class);
        model.init("192.168.180.175", 7777);

        binding.lampImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lampStatus == false){
                    lampStatus = true;
                    binding.lampImage.setImageResource(R.drawable.baseline_lightbulb_24_yellow);
                    model.sendMessage("lampOn");
                } else {
                    lampStatus = false;
                    binding.lampImage.setImageResource(R.drawable.baseline_lightbulb_24_gray);
                    model.sendMessage("lampOff");
                }
            }
        });

        binding.tempImage.setOnClickListener(v -> {
            model.sendMessage("getTemp");
            model.receiveMessage();
        });

        model.getResponse().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                binding.tempText.setText(s);
            }
        });


    }
}