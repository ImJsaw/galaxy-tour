package com.game.bing.starwar.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;

public class player extends character {
    private Bitmap shiedPic;
    private boolean shied = false;
    private static int maxBlood;
    private RectF shiedRender = new RectF();

    private static int playerPower;

    player(Bitmap bitmap,Bitmap pic, int setBlood,int power){
        super(bitmap,setBlood,2);
        playerPower = power;
        this.shiedPic = pic;
        maxBlood = setBlood;
    }

//    void setX(float x1){
//        centerX = x1;
//        render.left = centerX - width/2;
//        render.right = centerX + width/2;
//    }
//    void setY(float y1){
//        centerY = y1;
//        render.top = centerY - height/2;
//        render.bottom = centerY + height/2;
//    }


    @Override
    void setX(float x1) {
        super.setX(x1);
        shiedRender.left = getRender().left-20;
        shiedRender.right = getRender().right+20;
    }

    @Override
    void setY(float y1) {
        super.setY(y1);
        shiedRender.top = getRender().top - 90;
        shiedRender.bottom = getRender().top - 90 + shiedPic.getHeight();
    }

    void setShied(boolean status){
        shied = status;
    }

    boolean shiedStatus(){
        return shied;
    }

    public static int getMaxBlood(){
        return maxBlood;
    }


    public static int getPlayerPower() {
        return playerPower;
    }
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (shiedStatus()) canvas.drawBitmap(shiedPic,null,shiedRender,null);

    }
}
