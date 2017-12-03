package com.example.koungho632.newsogongsaapplication.Page;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.koungho632.newsogongsaapplication.Adapter.Noti_Adapter;
import com.example.koungho632.newsogongsaapplication.Dialog.ChangeDialog;
import com.example.koungho632.newsogongsaapplication.Dialog.CustomDialog;
import com.example.koungho632.newsogongsaapplication.ListItem.Noti_listitem;
import com.example.koungho632.newsogongsaapplication.R;
import com.example.koungho632.newsogongsaapplication.Server_Connection.ServerConnect;

import java.util.ArrayList;
import java.util.HashMap;

public class Notification_page extends AppCompatActivity {

    ListView noti_list;
    public ArrayList<Noti_listitem> list;
    public Noti_Adapter adapter;

    public ArrayList<HashMap> userNoti;
    String flag;
    String uId;
    String ID;
    int pos;
    int size;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_page);

        userNoti=MainActivity.userNoti;

        list=new ArrayList<Noti_listitem>();

        noti_list=(ListView)findViewById(R.id.noti_list);
        adapter=new Noti_Adapter(getApplicationContext(), R.layout.notification_listview,list);
        noti_list.setAdapter(adapter);

        noti_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, View view, final int position, long id) {

                Noti_listitem noti_listitem = (Noti_listitem) adapter.getItem(position);
                flag = list.get(position).getSw();
                ID = list.get(position).getId();
                uId = list.get(position).getuId();

                //Log.d("id",ID);
                pos = position;

                if (pos >= size) {
                    final ChangeDialog dialog=new ChangeDialog(Notification_page.this,R.style.Theme_Dialog);
                    dialog.setCustomText("삭제 하시겠습니까?");
                    dialog.setOkBtn("ok");
                    View.OnClickListener okListener = new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ServerConnect.DataDownloadListener dataDownloadListener = new ServerConnect.DataDownloadListener() {
                                @Override
                                public void dataDownloadedSuccessfully(Object data) {
                                    String fl = (String) data;
                                    if (fl.equals(login_page.TAG)) {
                                        Log.d("111111","222222");
                                        MainActivity.userNoti.remove(position);
                                        list.remove(position);
                                        onResume();
                                    }
                                }

                                @Override
                                public void dataDownloadFailed() {

                                }
                            };

                            login_page._serverConnect.setDataDownloadListener(dataDownloadListener);
                            HashMap hashMap = new HashMap();
                            hashMap.put("cmd", "deleteWait");
                            hashMap.put("userId", MainActivity.UserId);
                            hashMap.put("sw", flag);
                            hashMap.put("id", ID);
                            Log.d((String) hashMap.get("id"), "33333333");
                            login_page._serverConnect.sendMsg(hashMap);

                            dialog.dismiss();
                        }
                    };
                    dialog.okListener(okListener);

                    dialog.setCancel_Btn("cancel");
                    View.OnClickListener cancelListener = new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    };
                    dialog.cancelListener(cancelListener);

                    dialog.show();
                } else {
                    final ChangeDialog dialog=new ChangeDialog(Notification_page.this,R.style.Theme_Dialog);
                    dialog.setCustomText("가입을 승인하시겠습니까?");
                    dialog.setOkBtn("승인");
                    View.OnClickListener okListener=new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (flag.equals("0")) {
                                approve("groupApprove", "0");
                            } else {
                                approve("meetingApprove", "0");
                            }

                            dialog.dismiss();
                        }
                    };
                    dialog.okListener(okListener);

                    dialog.setCancel_Btn("거절");
                    View.OnClickListener cancelListener = new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (flag.equals("0")) {
                                approve("groupApprove", "1");
                            } else {
                                approve("meetingApprove", "1");
                            }
                            dialog.dismiss();
                        }
                    };
                    dialog.cancelListener(cancelListener);
                    dialog.show();
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        list.clear();
        if(userNoti!=null){

            HashMap temp=userNoti.get(userNoti.size()-1);
            size=(int)temp.get("size");
            int i=0;
            for(HashMap item:userNoti){

                if(item.equals(temp))
                    break;

                byte[] bytes = (byte[]) item.get("img");
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                String text = (String) item.get("text");
                String sw = (String) item.get("switch");


                if(i<size) {
                    String id =(String)item.get("gId");
                    if(Integer.parseInt(id)==-1)
                        id=(String)item.get("mId");
                    String uId = (String) item.get("uId");
                    Noti_listitem noti_listitem = new Noti_listitem(bitmap, text, sw, uId, id);
                    list.add(noti_listitem);
                    i++;
                }else{
                    String id=(String)item.get("id");
                    Noti_listitem noti_listitem=new Noti_listitem(bitmap,text,sw,"-1",id);
                    list.add(noti_listitem);
                }
            }
        }

        adapter.notifyDataSetChanged();
    }

    public void approve(String cmd,String appr){

        ServerConnect.DataDownloadListener dataDownloadListener1=new ServerConnect.DataDownloadListener() {
            @Override
            public void dataDownloadedSuccessfully(Object data) {
                String flag=(String)data;
                if(flag.equals(login_page.TAG)){
                    Log.d("3333","22222");
                    MainActivity.userNoti.remove(pos);
                    list.remove(pos);
                    onResume();
                }
            }

            @Override
            public void dataDownloadFailed() {

            }
        };

        login_page._serverConnect.setDataDownloadListener(dataDownloadListener1);
        HashMap hashMap=new HashMap();
        hashMap.put("cmd", cmd);
        hashMap.put("flag",appr);
        hashMap.put("id",ID);
        hashMap.put("uId",uId);
        Log.d("3333333",hashMap.toString());
        login_page._serverConnect.sendMsg(hashMap);
    }


}
