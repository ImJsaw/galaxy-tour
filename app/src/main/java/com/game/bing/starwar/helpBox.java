package com.game.bing.starwar;


import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v7.app.AlertDialog;
import android.text.SpannableString;
import android.text.util.Linkify;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

class helpBox {


    static void show(Activity callingActivity){
        View help;
        TextView helpText;

        SpannableString text = new SpannableString(callingActivity.getString(R.string.help));


        try{
            LayoutInflater inflater = callingActivity.getLayoutInflater();
            help = inflater.inflate(R.layout.help_box,(ViewGroup) callingActivity.findViewById(R.id.helpView));
            helpText = (TextView) help.findViewById(R.id.helpText);

            helpText.setText(text);
            Linkify.addLinks(helpText,Linkify.ALL);
            new AlertDialog.Builder(callingActivity)
                    .setTitle("Help")
                    .setCancelable(true)
                    .setPositiveButton("OK",null)
                    .setView(help)
                    .show();
        }
        catch (InflateException e){
            //出錯時使用預設
           // help = helpText = new TextView(callingActivity);
        }






    }
}
