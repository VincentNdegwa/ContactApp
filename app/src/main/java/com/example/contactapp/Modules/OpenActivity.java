package com.example.contactapp.Modules;

import android.content.Context;
import android.content.Intent;

public class OpenActivity {
    public static void Open(Context context, Class<?> csl, String data){
        Intent openIntent = new Intent(context,csl);
        openIntent.putExtra("data", data);
        context.startActivity(openIntent);
    }
}
