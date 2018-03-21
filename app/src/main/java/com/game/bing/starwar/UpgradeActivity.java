package com.game.bing.starwar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.game.bing.starwar.game.player;

import java.util.Timer;
import java.util.TimerTask;

public class UpgradeActivity extends AppCompatActivity {

    //Button atkP,atkM,bloodP,bloodM,spdP,spdM;
    TextView atk,blood,spd,restPoint;
    private SharedPreferences data;

    private static Boolean exitConfirm = false;
    private static Boolean exitBuffer = false; //避免重複timer導致退出
    private int pow,bld,tpow,tbld;

    private int point,tempPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrade);

        atk = findViewById(R.id.atkValue);
        blood = findViewById(R.id.bloodValue);
        spd = findViewById(R.id.spdValue);
        restPoint = findViewById(R.id.restPoint);





        data = getSharedPreferences("data",MODE_PRIVATE);
        point = data.getInt("restPoint",0);
        pow = data.getInt("playerATK",5);
        bld = data.getInt("playerBLOOD",10);

        tempPoint = point;
        tpow = pow;
        tbld = bld;


        atk.setText(String.valueOf(tpow));
        blood.setText(String.valueOf(tbld));
        restPoint.setText("剩餘點數 : "+ String.valueOf(tempPoint));
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.atkPlus:
                if (tempPoint > 0){
                    tempPoint--;
                    tpow++;
                    atk.setText(String.valueOf(tpow));
                    restPoint.setText("剩餘點數 : "+ String.valueOf(tempPoint));
                }

                break;
            case R.id.atkMinus:
                if (tempPoint < point && tpow > pow){
                    tempPoint++;
                    tpow--;
                    atk.setText(String.valueOf(tpow));
                    restPoint.setText("剩餘點數 : "+ String.valueOf(tempPoint));
                }
                break;
            case R.id.bloodPlus:
                if (tempPoint > 0){
                    tempPoint--;
                    tbld++;
                    blood.setText(String.valueOf(tbld));
                    restPoint.setText("剩餘點數 : "+ String.valueOf(tempPoint));
                }
                break;
            case R.id.bloodMinus:
                if (tempPoint < point && tbld > bld){
                    tempPoint++;
                    tbld--;
                    blood.setText(String.valueOf(tbld));
                    restPoint.setText("剩餘點數 : "+ String.valueOf(tempPoint));
                }
                break;
            case R.id.spdPlus:
                break;
            case R.id.spdMinus:
                break;
            case R.id.confirm:
                SharedPreferences.Editor editor = data.edit();
                editor.putInt("restPoint",tempPoint);
                editor.putInt("playerATK",tpow);
                editor.putInt("playerBLOOD",tbld);
                editor.apply();

                point = tempPoint;
                bld = tbld;
                pow = tpow;
                //clear history

                break;
            case R.id.cancel:
                tempPoint = point;
                tpow = pow;
                tbld = bld;
                atk.setText(String.valueOf(tpow));
                blood.setText(String.valueOf(tbld));
                restPoint.setText("剩餘點數 : "+ String.valueOf(tempPoint));
                //back to history

                break;
            case R.id.start_game:
                GameStart();
                break;
            case R.id.leftArrow:
                Toast.makeText(this,"不要猴急.哥還在開發",Toast.LENGTH_SHORT).show();
                break;
            case R.id.rightArrow:
                Toast.makeText(this,"不要猴急.哥還在開發",Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void GameStart() {
        Intent game = new Intent(this,GameActivity.class);
        startActivity(game);
        finish();
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
}
