package com.example.testalarm2;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.AlphabeticIndex;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.core.InFilter;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import java.util.zip.Inflater;

public class SettingAlarm extends MainActivity {
    private static final String TAG="SettingAlarm";
    private FirebaseFirestore firebaseFirestore;
    private TimePicker timePicker;
    private EditText drugEditText;
    private AlarmInfo alarmInfo2; //database에 올린 결과들을 가져오는 변수
    private AlarmManager alarmManager;
    private String time, ampm;
    private String notificationText;
    private String firedrugtext;
    private String hourtime, minutetime;
    private String ampmtext;
    private String notificationId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_alarm);


        timePicker = findViewById(R.id.timepicker);
        drugEditText = findViewById(R.id.editText);
        alarmManager=(AlarmManager)getSystemService(Context.ALARM_SERVICE);

        alarmInfo2 = (AlarmInfo)getIntent().getSerializableExtra("alarmInfo");

        findViewById(R.id.btnset).setOnClickListener(onClickListener);
        findViewById(R.id.btncancel).setOnClickListener(oncancelClickListener);


    }
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v.getId() ==R.id.btnset){
                uploader();
                setAlarm();
                myStartActivity(MainActivity.class);
            }
        }
    };

    View.OnClickListener oncancelClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId()==R.id.btncancel){
                myStartActivity(MainActivity.class);
            }
        }
    };


    private void setAlarm(){

        hourtime = timePicker.getCurrentHour().toString();
        minutetime = timePicker.getCurrentMinute().toString();
        notificationText = drugEditText.getText().toString();

        final String timetimes = hourtime+minutetime;

        int hourtext = Integer.parseInt(hourtime);

        if(hourtext>11 && hourtext<24){
            ampmtext = "오후";
        }
        else
        {
            ampmtext="오전";
        }


     /*   firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("AlarmDemo").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){
                                //데이터를 다 가져와 alarmList배열에 넣어줌.

                                firedrugtext = document.getData().get("drugtext").toString();
                                time = document.getData().get("times").toString();
                                ampm = document.getData().get("ampm").toString();
                                //document.getId();

                                Log.e("data : ",firedrugtext);
                                Log.e("time : ",time);
                                Log.e("ampm: ", ampm);

                            }
                        }
                    }
                });*/
        if(Build.VERSION.SDK_INT< Build.VERSION_CODES.O){
            Toast.makeText(this,"버전을 확인해주세요.",Toast.LENGTH_SHORT).show();
            return;
        }


        final Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hourtime));
        calendar.set(Calendar.MINUTE, Integer.parseInt(minutetime));
        calendar.set(Calendar.SECOND,0);

        long intervalTime = 1000*24*60*60;
        long currentTime = System.currentTimeMillis();

        if(currentTime>calendar.getTimeInMillis()){
            //알림설정한 시간이 이미 지나간 시간이라면 하루뒤로 알림설정하도록함.
            calendar.setTimeInMillis(calendar.getTimeInMillis()+intervalTime);
        }



        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("AlarmDemo").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                                if(timetimes.equals(documentSnapshot.getData().get("times"))){
                                    firedrugtext = documentSnapshot.getData().get("drugtext").toString();

                                    Log.e("확인확인",firedrugtext);
                                    notificationId = documentSnapshot.getData().get("times").toString();
                                    Log.e("확인확인", notificationId);

                                    Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);


                                    intent.putExtra("drug",firedrugtext);
                                    intent.putExtra("id", notificationId);

                                    Log.e("intent확인확인",notificationText);
                                    Log.e("intent확인확인", notificationId);

                                    PendingIntent pIntent = PendingIntent.getBroadcast(getApplicationContext(), Integer.parseInt(notificationId),intent,0);
                                    alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pIntent);
                                    Toast.makeText(getApplicationContext(),"알림이 설정되었습니다.",Toast.LENGTH_SHORT).show();
                                }

                            }

                        }

                    }

                });
        //intent.putExtra("drug",notificationText);

    }

    //private void contentsUpdate(){
      /*  TimePicker timePicker = (TimePicker)findViewById(R.id.timepicker);
        TextView textView = (TextView)findViewById(R.id.ampmText);
        final String hour = timePicker.getCurrentHour().toString();
        final String minute = timePicker.getCurrentMinute().toString();
        final String drugText = ((EditText)findViewById(R.id.editText)).getText().toString();

        int hourtest = Integer.parseInt(hour);
        int minutetest = Integer.parseInt(minute);

        String hourtext="";
        String minutetext="";

        String realTime = "";

        //int count=0;

        //시,분이 입력되었을때
        if(hour.length()>0 && minute.length()>0){


            if(hourtest>11 && hourtest<24){
                ampm="오후";
                realTime= String.valueOf(hourtest-12);
            }else{
                ampm="오전";
                realTime= String.valueOf(hourtest);
            }

            if(hourtest<10){
                hourtext = " "+realTime+":";
            }else{
                hourtext=realTime+":";
            }
            if(minutetest<10){
                minutetext = "0"+minute;
            }else {
                minutetext=minute;
            }

            final String ampmText = ampm;


            AlarmInfo alarmInfo = new AlarmInfo(hourtext,minutetext,drugText,ampmText);


            uploader(alarmInfo);//시,분,약이름이 uploader로 들어감.
        }else{
            Log.e("알림시간설정~!~!~!~!~!",TAG);
            //Toast.makeText(this,"알림시간을 설정해주세요.", Toast.LENGTH_SHORT).show();
        }*/
    //}
    //저장 버튼을 누르면 hour,minute,drugtext를 파이어베이스에 넘어감
    private void uploader() {

        TimePicker timePicker = (TimePicker) findViewById(R.id.timepicker);
        TextView textView = (TextView) findViewById(R.id.ampmText);
        final String hour = timePicker.getCurrentHour().toString();
        final String minute = timePicker.getCurrentMinute().toString();
        final String drugText = ((EditText) findViewById(R.id.editText)).getText().toString();

        String times = hour+minute;


        int hourtest = Integer.parseInt(hour);
        int minutetest = Integer.parseInt(minute);

        String hourtext = "";
        String minutetext = "";

        String realTime = "";

        //int count=0;

        //시,분이 입력되었을때
        if (hour.length() > 0 && minute.length() > 0) {


            if (hourtest > 11 && hourtest < 24) {
                ampm = "오후";
                realTime= String.valueOf(hourtest-12);
            } else {
                ampm = "오전";
                realTime= String.valueOf(hourtest);
            }

            if(hourtest<10){
                hourtext = " "+realTime+":";
            }else{
                hourtext=realTime+":";
            }
            if(minutetest<10){
                minutetext = "0"+minute;
            }else {
                minutetext=minute;
            }

          /*  if(hourtest<10){
                hourtext = " "+realTime+":";
            }else{
                hourtext=realTime+":";
            }
            if(minutetest<10){
                minutetext = "0"+minute;
            }else {
                minutetext=minute;
            } */

            final String ampmText = ampm;


            firebaseFirestore = FirebaseFirestore.getInstance();

      /*  final DocumentReference documentReference = alarmInfo2 == null ? firebaseFirestore.collection("AlarmDemo").document(times)
                : firebaseFirestore.collection("AlarmDemo").document(alarmInfo2.getId());*/
          final Map<String, Object> mData = new HashMap<>();
          mData.put("hour", hourtext);
          mData.put("minute", minutetext);
          mData.put("drugtext", drugText);
          mData.put("ampm", ampmText);
          mData.put("times",times);

          firebaseFirestore.collection("AlarmDemo").document(times)
                  .set(mData)
                  .addOnCompleteListener(new OnCompleteListener<Void>() {
                      @Override
                      public void onComplete(@NonNull Task<Void> task) {
                          Log.e(TAG, "id");
                      }
                  }).addOnFailureListener(new OnFailureListener() {
              @Override
              public void onFailure(@NonNull Exception e) {
                  Log.e(TAG, "Error", e);
              }
          });



    /*    documentReference.set(alarmInfo.getAlarmInfo())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d(TAG,"id");
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG,"ERROR",e);
                    }
                });*/
        }
    }

    private void myStartActivity(Class c){
        Intent intent = new Intent(this,c);
        startActivityForResult(intent,1);
    }
}
