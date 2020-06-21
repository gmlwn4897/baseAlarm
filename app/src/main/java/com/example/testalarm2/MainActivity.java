package com.example.testalarm2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

        findViewById(R.id.floatingActionButton).setOnClickListener(onClickListener);

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            myStartActivity(SettingAlarm.class);
        }
    };

    private void alarmUpdate(){
        firebaseFirestore.collection("AlarmDemo").orderBy("hour",Query.Direction.ASCENDING).get()
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
                            recyclerView.setAdapter(myAdapter);
                            myAdapter.notifyDataSetChanged();

                        }else {
                            Log.d(TAG, "Error : ",task.getException());
                        }
                    }
                });
    }


    private void myStartActivity(Class c) {//게시물을 추가하는 경우 WritePostActivity 화면으로 넘겨주는 코드
        Intent intent = new Intent(this, c);
        startActivityForResult(intent, 1);
    }

  /*  private void myStartActivity(Class c, AlarmInfo alarmInfo){
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

}