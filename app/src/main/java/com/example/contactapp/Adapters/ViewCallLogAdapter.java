package com.example.contactapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactapp.Data.CallDetails;
import com.example.contactapp.R;

import java.util.List;

public class ViewCallLogAdapter extends RecyclerView.Adapter<ViewCallLogAdapter.ViewHolder> {

    public Context context;
    public List<CallDetails> callDetailsList;

    public ViewCallLogAdapter(List<CallDetails> callDetailsList, Context context){
        this.context = context;
        this.callDetailsList = callDetailsList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView callTypeIcon;
        public TextView callTimeTextView;
        public TextView callTypeTextView;
        public TextView callSimTextView;
        public TextView callDurationTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            callTypeIcon = itemView.findViewById(R.id.call_type_icon);
            callTimeTextView = itemView.findViewById(R.id.call_time);
            callTypeTextView = itemView.findViewById(R.id.call_type);
            callSimTextView = itemView.findViewById(R.id.call_sim);
            callDurationTextView = itemView.findViewById(R.id.call_duration);
        }
    }
    public void setCallDetailsList(List<CallDetails> callDetailsList){
        this.callDetailsList = callDetailsList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewCallLogAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View card = LayoutInflater.from(context).inflate(R.layout.log_details_view,parent, false);
        return new ViewHolder(card);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewCallLogAdapter.ViewHolder holder, int position) {
        CallDetails callLog = callDetailsList.get(position);
        holder.callTimeTextView.setText(callLog.getTime());
        holder.callTypeTextView.setText(callLog.getType());
        holder.callDurationTextView.setText(callLog.getTime());
        holder.callSimTextView.setText(callLog.getSim());
        switch (callLog.getType()){
            case "Incoming":
                holder.callTypeIcon.setImageResource(R.drawable.incomming_call);
                break;
            case "Outgoing":
                holder.callTypeIcon.setImageResource(R.drawable.outgoing_call);
                break;
            case "Missed":
                holder.callTypeIcon.setImageResource(R.drawable.missed_call);
                break;
            default:
                holder.callTypeIcon.setImageResource(R.drawable.all_call);

        }
    }

    @Override
    public int getItemCount() {
        return callDetailsList.size();
    }
}
