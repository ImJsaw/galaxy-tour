package com.game.bing.starwar.game;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;

class sprite {
    private float centerX,centerY;
    private int width,height;
    private float dx = 0,dy = 0;
    private RectF render = new RectF();
    private Bitmap pic;
    private boolean alive = true;

    private int type = 0;
    //  0 sprite
    //  1 enemy
    //  2 player
    //  3 bullet
    //  4 enemyBullet
    //  5 reward

    sprite(Bitmap bitmap,int type){
        pic = bitmap;
        width = pic.getWidth();
        height = pic.getHeight();
        this.type = type;
    }
    sprite(Bitmap bitmap,float x,float y){
        pic = bitmap;
        width = pic.getWidth();
        height = pic.getHeight();
        setX(x);
        setY(y);
    }

    sprite(Bitmap bitmap,float x,float y,int type){
        pic = bitmap;
        width = pic.getWidth();
        height = pic.getHeight();
        setX(x);
        setY(y);
        this.type = type;
    }

    void setX(float x1){
        centerX = x1;
        render.left = centerX - width/2;
        render.right = centerX + width/2;
    }
    void setY(float y1){
        centerY = y1;
        render.top = centerY - height/2;
        render.bottom = centerY + height/2;
    }
    void setPosition(float x,float y){
        setX(x);
        setY(y);
    }

    RectF getRender(){
        return render;
    }
    float getX(){
        return centerX;
    }
    float getY(){
        return centerY;
    }

    float getdx(){
        return dx;
    }
    float getdy(){
        return dy;
    }
    void setdx(float dx1){
        dx = dx1;
    }
    void setdy(float dy1){
        dy = dy1;
    }
    boolean getAlive() {
        return alive;
    }
    void setAlive(boolean alive) {
        this.alive = alive;
    }
    void setType(int i){
        type = i;
    }
    int getType(){
        return  type;
    }

    void move(int x,int y){
        setX(getX() + dx);
        setY(getY() + dy);
    }

    void bounce(int x,int y) {
        setX(getX() + dx);
        setY(getY() + dy);
        if (render.right > x) {
            setX(x-render.width()/2);
            dx = (float)Math.random()*-30;
            dy = (float)Math.random()*60-30;
        }
        if (render.bottom > y) {
            setY(y-render.height()/2);
            dx = (float)Math.random()*60-30;
            dy = (float)Math.random()*-30;
        }
        if (render.left < 0) {
            setX(render.width()/2);
            dx = (float)Math.random()*30;
            dy = (float)Math.random()*60-30;
        }
        if (render.top < 0) {
            setY(render.height()/2);
            dx = (float)Math.random()*60-30;
            dy = (float)Math.random()*30;
        }

    }

    void setpic(Bitmap bitmap){
        pic = bitmap;
    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(pic,null,render,null);
    }


    void destroy(){
        pic = null;
        alive = false;
    }


}
