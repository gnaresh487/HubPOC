package com.naresh.hubpoc;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CallLogsAdapter extends RecyclerView.Adapter<CallLogsAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private ArrayList<CallLogsModel> callLogsList;

    // data is passed into the constructor
    CallLogsAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);

    }

    // inflates the row layout from xml when needed
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.call_logs_rv, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CallLogsModel logsModel = callLogsList.get(position);
        Log.d("TAG", "onBindViewHolder: "+logsModel.getUserNumber());
        if(logsModel.getCallerName() != null) {
            holder.callerName.setText(logsModel.getCallerName());
            holder.callerPhoneNumber.setText(logsModel.getCallerNumber());
        } else {
            holder.callerName.setText(logsModel.getCallerNumber());
            holder.callerPhoneNumber.setText(logsModel.getCountryCode());
        }
        holder.callTime.setText(String.valueOf(logsModel.getCallDateAndTime()));
    }

    // total number of rows
    @Override
    public int getItemCount() {
        //return mData.size();
        return callLogsList.size();
    }

    void setData(ArrayList<CallLogsModel> callLogsModels){
        this.callLogsList = callLogsModels;
    }

    // stores and recycles views as they are scrolled off screen
     class ViewHolder extends RecyclerView.ViewHolder {
        TextView callerName, callerPhoneNumber, callTime;
        ImageView callTypeImg, callDetails;

        ViewHolder(View itemView) {
            super(itemView);
            callTypeImg = itemView.findViewById(R.id.img_call_type);
            callerName = itemView.findViewById(R.id.caller_name);
            callerPhoneNumber = itemView.findViewById(R.id.caller_phone_number);
            callTime = itemView.findViewById(R.id.call_time);
            callDetails = itemView.findViewById(R.id.call_details);
        }
    }

}