package com.game.bing.starwar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.game.bing.starwar.game.gameView;

import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends Activity {
    private gameView gameView;
    private static Boolean exitConfirm = false;
    private static Boolean exitBuffer = false; //避免重複timer導致退出
    Timer t = new Timer();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_activity);
        gameView = findViewById(R.id.gameView);

        int[] bitmapIDs = {
                R.drawable.plane,//0
                R.drawable.bullet,//1
                R.drawable.demon,//2
                R.drawable.demon_small,//3
                R.drawable.fire_ball,//4
                R.drawable.pause,//5
                R.drawable.medicine,//6
                R.drawable.shied,//7
                R.drawable.plane_shied,//8
                R.drawable.ammo,//9
                R.drawable.fork,//10

        };

        setGoHomeListener();
        gameView.start(bitmapIDs);
    }

    private void setGoHomeListener() {
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                if (gameView.goHome()) backToHome();
            }
        },2000,1000);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (exitConfirm) {
                System.exit(0);
            } else {
                exitConfirm = true;
                Toast.makeText(this, "再按一次返回鍵退出遊戲", Toast.LENGTH_SHORT).show();
                if(!exitBuffer) {
                    endAPP();
                }
            }
        }
        return false;
    }

    private void endAPP(){
        exitBuffer = true;
        Timer exitTimer = new Timer();
        TimerTask exitTask = new TimerTask() {
            @Override
            public void run() {
                exitConfirm = false;
                exitBuffer = false;
            }
        };
        exitTimer.schedule(exitTask,2000);
    }

    private void backToHome(){
        Intent intent = new Intent(this,StartActivity.class);
        startActivity(intent);
        t.cancel();
        finish();
    }
}
