package com.example.koungho632.newsogongsaapplication.Server_Connection;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.koungho632.newsogongsaapplication.ListItem.Noti_listitem;
import com.example.koungho632.newsogongsaapplication.Page.MainActivity;
import com.example.koungho632.newsogongsaapplication.Page.Notification_page;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
/**
 * Created by koungho632 on 2016. 4. 30..
 */
public class ServerConnect extends AsyncTask<String,Object,Object> {

    DataDownloadListener dataDownloadListener=null;

    public static Socket socket=null;
    ProgressDialog asyncDialog;
    Context context=null;

    String macaddr;
    int port;

    ObjectOutputStream oos;

    public ServerConnect(Context context, String macaddr, int port){
        this.context=context;
        this.macaddr=macaddr;
        this.port=port;
    }

    @Override
    protected void onPreExecute() {
        // show dialog
        super.onPreExecute();
    }


    //서버에서 데이터를 받을 때까지 무한 대기
    @Override
    protected Object doInBackground(String... params) {
        try{
            if(socket==null)
                socket=new Socket(macaddr,port);

            oos = new ObjectOutputStream(socket.getOutputStream());

            Log.d("받기 시작 ","11111");

            InputStream is=socket.getInputStream();
            ObjectInputStream ois=new ObjectInputStream(is);

            while(socket.isConnected()) {
                Object obj = ois.readObject();

                Log.d("obj", obj.getClass().toString());
                publishProgress(obj);
                Log.d("server_receive", "서버에서 받기 성공");
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Object aVoid) {
        super.onPostExecute(aVoid);
    }

    public void sendMsg(Object msg){
        HashMap hashMap=(HashMap)msg;
        Log.d("메시지 전송","시작");
        Log.d("DATA : ",msg.toString());

        try {
            if(oos==null)
                oos=new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(hashMap);



        }catch (Exception e){
            e.printStackTrace();
        }
    }
    //이벤트 등록 메소드
    public void setDataDownloadListener(DataDownloadListener dataDownloadListener) {
        this.dataDownloadListener = dataDownloadListener;
    }

    @Override
    protected void onProgressUpdate(Object... values)
    {
        if (socket!=null && socket.isConnected())
        {
            if(values != null){
                dataDownloadListener.dataDownloadedSuccessfully(values[0]);
            }else
                dataDownloadListener.dataDownloadFailed();
        }
        super.onProgressUpdate(values);
    }

    public interface DataDownloadListener {
        void dataDownloadedSuccessfully(Object data);
        void dataDownloadFailed();
    }
}

