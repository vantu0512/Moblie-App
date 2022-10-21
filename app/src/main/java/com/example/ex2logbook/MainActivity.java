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
    private int index,start,last;
    private int currentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        UrlList = new ArrayList<String>();
        UrlList.add("https://images5.alphacoders.com/104/1046568.jpg");
        UrlList.add("https://images2.alphacoders.com/516/516664.jpg");
        UrlList.add("https://cdn.sforum.vn/sforum/wp-content/uploads/2021/12/android-studio.png");
        UrlList.add("https://photo2.tinhte.vn/data/attachment-files/2021/09/5660152_city_road_buildings_188197_3840x2160.jpg");
        if(!UrlList.isEmpty()){
            start = 0 ;
            last = UrlList.size() -1;
            String url = UrlList.get(index);
            new fetchImage(url).start();

        }
        binding.nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index+=1;
                if (index >last){
                    index=0;
                }
                String url = UrlList.get(index);
                new fetchImage(url).start();

            }
        });
        binding.previewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index -= 1;
                if (index < 0){
                    index = last;
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
                        if (!UrlList.isEmpty()){
                            start = 0;
                            last = UrlList.size();
                            currentPosition = 0;
                        }
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
                    progressDialog.setCancelable(false);
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