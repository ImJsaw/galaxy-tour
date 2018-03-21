package com.game.bing.starwar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class StartActivity extends AppCompatActivity {

    TextView title;
    Button[] button = new Button[4];



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        title = (TextView)findViewById(R.id.title_starwar);
        //title.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/WCL-09.ttf"));

        Typeface font2 = Typeface.createFromAsset(getResources().getAssets(), "fonts/WCL-09.ttf");
        button[0] = (Button)findViewById(R.id.StartButton);
        button[0].setTypeface(font2);//24

        button[1] = (Button)findViewById(R.id.OptionButton);
        button[1].setTypeface(font2);//28

        button[2] = (Button)findViewById(R.id.HelpButton);
        button[2].setTypeface(font2);//34

        button[3] = (Button)findViewById(R.id.AboutButton);
        button[3].setTypeface(font2);//40

    }

    public void onClick(View view) {
        if(view.getId() == R.id.StartButton){
            GameStart();
        }
        if (view.getId() == R.id.AboutButton){
            aboutBox.show(this);
        }
        if (view.getId() == R.id.HelpButton){
            for(int i = 0;i < 4;i++){
                button[i].setBackgroundColor(Color.BLACK);
                button[i].setTextColor(Color.GREEN);
            }
        }


    }



    private void GameStart() {
        Intent intent = new Intent(this,UpgradeActivity.class);
        startActivity(intent);
        finish();
    }
}
