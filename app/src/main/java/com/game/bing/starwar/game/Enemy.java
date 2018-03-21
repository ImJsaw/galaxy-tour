package com.game.bing.starwar.game;

import android.graphics.Bitmap;
import android.graphics.RectF;

class Enemy extends character {
    private int curStage;
    private int atkPower;

    Enemy(Bitmap bitmap, float x, float y,int setBlood,int stage,int power){
        super(bitmap,x,y,setBlood,1);
        atkPower = power;
        super.setdx((float)Math.random()*50);
        super.setdy((float)Math.random()*50);
        curStage = stage;
    }

    boolean dropReward(){
        switch (curStage){
            case 1:
                return Math.random() < 0.1;
            case 2:
                return Math.random() < 0.03;
        }
        return true;
    }

    int getAtkPower(){
        return atkPower;
    }

    //
    @Override
    void move(int x,int y) {
        switch (curStage){
            case 1:
                break;
            case 2:
                super.bounce(x,y);
                break;
            default:
                break;
        }
    }

    boolean shoot() {
        switch (curStage){
            case 1:
                if(Math.random() < 0.003) return true;
                break;
            case 2:
                if(Math.random() < 0.01) return true;
                break;
            default:
                break;
        }
        return false;
    }
}
