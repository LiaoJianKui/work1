package com.example.administrator.astest1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private EditText etPath;
    private TextView tvContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews() {
       etPath= (EditText) findViewById(R.id.etPath);
       tvContent= (TextView) findViewById(R.id.tvContent);

    }
    public void click(View view){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String path=etPath.getText().toString().trim();
                try {
                    URL url=new URL(path);
                    HttpURLConnection conn= (HttpURLConnection) url.openConnection();
                    //设置发送get请求
                    conn.setRequestMethod("GET");
                    //设置请求超时时间
                    conn.setConnectTimeout(5000);
                    //获取服务器返回的状态码
                    int code=conn.getResponseCode();
                    //如果code==200说明请求成功
                    if(code==200){
                        //获取服务器返回的数据，以流的形式
                        InputStream in=conn.getInputStream();
                        InputStreamReader isr= new InputStreamReader(in);
                        BufferedReader br=new BufferedReader(isr);
                        StringBuffer sb=new StringBuffer();
                        while((br.readLine())!=null){
                            sb.append(br.readLine());
                        }
                        isr.close();
                        br.close();
                        final String content=sb.toString();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tvContent.setText(content);
                            }
                        });
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
