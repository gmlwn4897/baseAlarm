package com.example.testalarm2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;

import listener.OnAlarmListener;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private AlarmInfo alarmInfo2;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private MyAdapter myAdapter;
    private FirebaseFirestore firebaseFirestore;
    private ArrayList<AlarmInfo> alarmList;
    private EditText editText;
    private TimePicker timePicker;
    private int hour,minute;
    private AlarmManager alarmManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText)findViewById(R.id.editText);
        timePicker = (TimePicker)findViewById(R.id.timepicker);

        alarmInfo2= (AlarmInfo)getIntent().getSerializableExtra("alarmInfo");
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(layoutManager);

        firebaseFirestore = FirebaseFirestore.getInstance();
        alarmUpdate();
        alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        findViewById(R.id.floatingActionButton).setOnClickListener(onClickListener);

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            myStartActivity(SettingAlarm.class);
        }
    };

    private void alarmUpdate(){
        firebaseFirestore.collection("AlarmDemo").orderBy("times",Query.Direction.ASCENDING).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            alarmList = new ArrayList<>();
                            alarmList.clear();
                            for(QueryDocumentSnapshot document : task.getResult()){
                                alarmList.add(new AlarmInfo(
                                        document.getData().get("hour").toString(),
                                        document.getData().get("minute").toString(),
                                        document.getData().get("drugtext").toString(),
                                        document.getData().get("ampm").toString(),
                                        document.getId()
                                ));
                            }
                            myAdapter = new MyAdapter(MainActivity.this, alarmList);
                            //myAdapter.setOnAlarmListener(onAlarmListener);
                            recyclerView.setAdapter(myAdapter);
                            myAdapter.notifyDataSetChanged();

                        }else {
                            Log.d(TAG, "Error : ",task.getException());
                        }
                    }
                });
    }

  /*  OnAlarmListener onAlarmListener = new OnAlarmListener() {//인터페이스인 OnPostListener를 가져와서 구현해줌
        @Override
        public void onDelete(int position) {//MainAdapter에 넘겨주기 위한 메서드 작성

            String id = alarmList.get(position).getId();//document의 id에 맞게 지워주기 위해 id값을 얻어옴
            firebaseFirestore.collection("AlarmDemo").document(id).delete()//그 id에 맞는 값들을 지워줌
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {//성공시
                            Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
                            String requestcode = intent.getStringExtra("id");
                            PendingIntent pIntent = PendingIntent.getBroadcast(getApplicationContext(), Integer.parseInt(requestcode),intent,0);
                            alarmManager.cancel(pIntent);
                            Toast.makeText(getApplicationContext(),"알림이 취소되었습니다.",Toast.LENGTH_SHORT).show();
                            startToast("게시글을 삭제하였습니다.");
                            alarmUpdate();//새로고침을 위해 이 이벤트를 mainActivity에서 알아야함.->listener를 만들어줘야함
                        }
                    }).addOnFailureListener(new OnFailureListener(){
                @Override
                public void onFailure(@NonNull Exception e) {//실패시
                    startToast("게시글 삭제에 실패하였습니다.");
                }
            });
        }
        @Override
        public void onModify(int position) {//여기서 수정하면 writepostActivity를 켜서 수정해주는코드
            myStartActivity(SettingAlarm.class,alarmList.get(position));
    }
    };*/
    private void myStartActivity(Class c) {//게시물을 추가하는 경우 WritePostActivity 화면으로 넘겨주는 코드
        Intent intent = new Intent(this, c);
        startActivityForResult(intent, 1);
}

  /* private void myStartActivity(Class c, AlarmInfo alarmInfo){
        Intent intent = new Intent(this,c);
        intent.putExtra("alarmInfo", alarmInfo);
        startActivity(intent);
    }*/

 /* public void deletebtn(View view){
        firebaseFirestore.collection("AlarmDemo").document("notificationId").delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        alarmUpdate();
                        Toast.makeText(getApplicationContext(),"알림을 삭제하였습니다.",Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"알림삭제를 실패했습니다.", Toast.LENGTH_SHORT).show();

            }
        });
    }*/

  //취소
    public void cancel(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    private void startToast(String msg){//toast를 띄워주는 메서드를 함수로 정의함
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

}