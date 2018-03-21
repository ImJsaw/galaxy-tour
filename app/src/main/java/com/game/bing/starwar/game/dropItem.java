package com.game.bing.starwar.game;

import android.graphics.Bitmap;

class dropItem extends sprite {
    private int rewardType;

    dropItem(Bitmap bitmap, float x, float y, int type,int rType) {
        super(bitmap, x, y, type);
        setdy(10);
        rewardType = rType;
    }

    int getRewardType() {
        return rewardType;
    }

    public int rewardType() {
        return rewardType;
        //heal 1
        //shied 2
    }
}
