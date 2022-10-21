package com.example.ex2logbook;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.example.ex2logbook.databinding.ActivityMainBinding;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    Handler mainHandler = new Handler();
    ProgressDialog progressDialog;
    ArrayList<String> UrlList;
    private int index, last;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        UrlList = new ArrayList<>();
        UrlList.add("https://d1hjkbq40fs2x4.cloudfront.net/2016-01-31/files/1045.jpg");
        UrlList.add("https://danviet.mediacdn.vn/2020/10/27/2-16037897686341859390320.jpg");
        UrlList.add("https://toanthaydinh.com/wp-content/uploads/2020/04/chum-tour-du-lich-dong-tay-bac-mixtourist.jpg");
        String url = UrlList.get(index);
        new fetchImage(url).start();
        binding.nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index++;
                if (index > UrlList.size()-1){
                    index = 0;
                }
                String url = UrlList.get(index);
                new fetchImage(url).start();
            }
        });

        binding.previewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index--;
                if (index < 0){
                    index = UrlList.size()-1;
                }
                String url = UrlList.get(index);
                new fetchImage(url).start();
            }
        });

        binding.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String WebUrl = "(https?:\\/\\/.*\\.(?:png|jpg))";

                if (binding.inputURL.getText() != null && binding.inputURL.getText().toString().matches(WebUrl)){
                    String url = binding.inputURL.getText().toString();
                    if(!url.isEmpty()){
                        UrlList.add(url);
                        last = UrlList.size() - 1;
                        new fetchImage(url).start();
                        Toast.makeText(MainActivity.this, "Add successfully!", Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(MainActivity.this, "Add fail!", Toast.LENGTH_LONG).show();
                    }
                    new fetchImage(url).start();
                }
                else {
                    Toast.makeText(MainActivity.this, "Invalid input, please check again!", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
    class fetchImage extends Thread{
        String Url;
        Bitmap bitMap;
        fetchImage(String Url){
            this.Url=Url;
        }

        @Override
        public void run() {
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    progressDialog = new ProgressDialog(MainActivity.this);
                    progressDialog.setMessage("Get your picture...");
                    progressDialog.setCancelable(true);
                    progressDialog.show();
                }
            });

            InputStream inputStream = null;
            try {
                inputStream = new java.net.URL(Url).openStream();
                bitMap = BitmapFactory.decodeStream(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }

            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                    binding.imageView.setImageBitmap(bitMap);
                }
            });
        }
    }
}