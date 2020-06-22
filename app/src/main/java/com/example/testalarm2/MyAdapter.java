package com.example.testalarm2;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import listener.OnAlarmListener;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private ArrayList<AlarmInfo> mDataset;
    private Activity activity;
    private OnAlarmListener onAlarmListener;
    private Button modifybtn;
    private Button deletebtn;

    private Context context;


    static class MyViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        MyViewHolder(Activity activity, CardView v, AlarmInfo alarmInfo) {
            super(v);
            cardView = v;
        }
    }
    public int getItemViewType(int position){
        return position;
    }

    public void setOnAlarmListener(OnAlarmListener onAlarmListener){
        this.onAlarmListener = onAlarmListener;
    }
    MyAdapter(Activity activity, ArrayList<AlarmInfo> mDataset){
        this.mDataset = mDataset;
        this.activity = activity;
    }

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, final int viewType) {

        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_alarm, parent, false);
        final MyViewHolder myViewHolder = new MyViewHolder(activity, cardView, mDataset.get(viewType));

        //


        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, final int position) {

        CardView cardView = holder.cardView;
        TextView hourText = cardView.findViewById(R.id.hourText);
        hourText.setText(mDataset.get(position).getHour());
        Log.e("확인확인", String.valueOf(mDataset.get(position).getHour()));


        TextView minuteText = cardView.findViewById(R.id.minuteText);
        minuteText.setText(mDataset.get(position).getMinute());
        Log.e("getMinute", String.valueOf(mDataset.get(position).getMinute()));


        TextView drugText = cardView.findViewById(R.id.drug_text);
        drugText.setText(mDataset.get(position).getDrugText());
        Log.e("getDrugText",mDataset.get(position).getDrugText());


        TextView ampmText = cardView.findViewById(R.id.ampmText);
        ampmText.setText(mDataset.get(position).getAmpm());
        Log.e("getAmpm", mDataset.get(position).getAmpm());

        modifybtn = cardView.findViewById(R.id.modifybtn);
        deletebtn = cardView.findViewById(R.id.deletebtn);



        modifybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAlarmListener.onModify(position);
            }
        });
        deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAlarmListener.onDelete(position);
            }
        });
        //modifybtn = cardView.findViewById(R.id.modifybtn);
        //deletebtn = cardView.findViewById(R.id.deletebtn);
    }

    @Override
    public int getItemCount() {
        return (mDataset !=null ? mDataset.size() :0);
    }
    private void myStartActivity (Class c, AlarmInfo alarmInfo){//intent를 이용하여 id 값을 전달해줄것임.
        Intent intent = new Intent(activity, c);
        intent.putExtra("alarmInfo", alarmInfo);//앞에는 key값, 뒤에는 실제 값
        //id값을 보내주면 WritePostActivity에서 받아서 쓸것임
        activity.startActivity(intent);
    }
}
