package com.example.contactapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.contactapp.CallLogView;
import com.example.contactapp.Data.CallDetails;
import com.example.contactapp.R;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CallLogsAdapter extends RecyclerView.Adapter<CallLogsAdapter.ViewHolder> {

    ArrayList<CallDetails> callDetails;
    Context context;
    public CallLogsAdapter(ArrayList<CallDetails> callDetails, Context context){
        this.callDetails = callDetails;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public CallLogsAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View card = LayoutInflater.from(context).inflate(R.layout.call_log_item,parent,false);
        return new ViewHolder(card);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CallLogsAdapter.ViewHolder holder, int position) {
        CallDetails callLog = callDetails.get(position);
        holder.callTime.setText(callLog.getTime());
        holder.callTypeText.setText(callLog.getType());
        holder.contactName.setText(callLog.getKey());
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

        holder.callButton.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:"+callLog.getNumber()));
            context.startActivity(intent);
        });
        holder.viewCall.setOnClickListener(view -> {
            Intent intent = new Intent(context, CallLogView.class);
            intent.putExtra("phoneNumber", callLog.getNumber());
            intent.putExtra("phoneName", callLog.getName());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return callDetails.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView contactIcon;
        public TextView contactName;
        public ImageView callTypeIcon;
        public TextView callTypeText;
        public TextView callTime;
        public ImageButton callButton;
        public ImageButton viewCall;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            contactIcon = itemView.findViewById(R.id.contact_name);
            contactName = itemView.findViewById(R.id.call_log_name);
            callTypeIcon = itemView.findViewById(R.id.icon_type);
            callTypeText = itemView.findViewById(R.id.call_type_text);
            callTime = itemView.findViewById(R.id.call_time);
            callButton = itemView.findViewById(R.id.call_button);
            viewCall = itemView.findViewById(R.id.view_call);
        }
    }
}
