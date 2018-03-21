package com.game.bing.starwar.game;

import android.graphics.Bitmap;

class bullet extends sprite {

    private int atkPower;


    bullet(Bitmap bitmap,float x,float y,int type,int power){
        super(bitmap,x,y,type);
        setdy(-10);
        atkPower = power;
    }

    int getAtkPower(){
        return atkPower;
    }

}
