package com.example.testalarm2;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private ArrayList<AlarmInfo> mDataset;
    private Activity activity;


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

    MyAdapter(Activity activity, ArrayList<AlarmInfo> mDataset){
        this.mDataset = mDataset;
        this.activity = activity;
    }

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_alarm, parent, false);
        final MyViewHolder myViewHolder = new MyViewHolder(activity, cardView, mDataset.get(viewType));

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, SettingAlarm.class);
                intent.putExtra("alarmInfo",mDataset.get(myViewHolder.getAdapterPosition()));
                //Toast.makeText(v.getContext(),mDataset.get(myViewHolder.getAdapterPosition())+"",Toast.LENGTH_LONG).show();
                activity.startActivity(intent);
            }
        });
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, int position) {

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

    }

    @Override
    public int getItemCount() {
        return (mDataset !=null ? mDataset.size() :0);
    }


}
