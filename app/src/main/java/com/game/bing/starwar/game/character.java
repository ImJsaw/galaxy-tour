package com.game.bing.starwar.game;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

class character extends sprite{
    private int maxBlood,blood,atkSpeed;
    private RectF bloodRender = new RectF();

    character(Bitmap bitmap,int setBlood,int type){
        super(bitmap,type);
        maxBlood = setBlood;
        blood = setBlood;
    }

    character(Bitmap bitmap, float x, float y,int setBlood,int type){
        super(bitmap,x,y,type);
        maxBlood = setBlood;
        blood = setBlood;
    }

    void damage(int damage){
        blood = blood - damage;
    }

    void heal(int plusBlood){
        if (plusBlood == 0) plusBlood++;
        blood = blood + plusBlood;
        Log.d("heal", String.valueOf(plusBlood));
        if (blood > maxBlood) blood = maxBlood;
    }

    int getBlood(){
        return blood;
    }

    void setBlood(int value){
        blood = value;
    }

    //setting about blood
    private void updateBloodPos() {

        RectF temp = super.getRender();
        bloodRender.left = temp.left;
        bloodRender.right = temp.right;
        bloodRender.bottom = temp.top -10;
        bloodRender.top = temp.top -20;
    }


    public int getAtkSpeed() {
        return atkSpeed;
    }

    public void setAtkSpeed(int atkSpeed) {
        this.atkSpeed = atkSpeed;
    }

    @Override
    public void draw(Canvas canvas){
        Paint mpaint = new Paint();
        RectF temp = bloodRender;
       super.draw(canvas);
       updateBloodPos();
       //full
       mpaint.setColor(Color.RED);
       mpaint.setStyle(Paint.Style.FILL);
       canvas.drawRect(temp,mpaint);
       //blood
        mpaint.setColor(Color.GREEN);
        mpaint.setStyle(Paint.Style.FILL);
        temp.right = temp.left + temp.width()*blood/maxBlood;
        canvas.drawRect(temp,mpaint);
        //border
        mpaint.setColor(Color.BLACK);
       mpaint.setStyle(Paint.Style.STROKE);
       canvas.drawRect(bloodRender,mpaint);
    }
}
