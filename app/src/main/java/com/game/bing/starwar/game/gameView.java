package com.game.bing.starwar.game;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.support.v7.app.AlertDialog;
import android.text.method.KeyListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.game.bing.starwar.R;
import com.game.bing.starwar.StartActivity;
import com.game.bing.starwar.UpgradeActivity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.MODE_PRIVATE;


public class gameView extends View {
    private List<Bitmap> bitmaps = new ArrayList<>();
    private  List<bullet> bullets = new ArrayList<>();
    private  List<Enemy> enemies = new ArrayList<>();
    private List<dropItem> rewards = new ArrayList<>();

    private int playerShotID,enemyShotID;

    private boolean hasEnemy = false;

    private int status;
    private final int statusStart = 1;
    private final int statusDestroy = 0;
    private final int statusPause = -1;
    private final int statusGameOver = 2;
    private final int statusWin = 3;



    public player player;

    private boolean goHome = false;
    private boolean gameOverShow = false;
    private boolean touchToHome = false;

    private Paint mPaint = new Paint();
    private boolean move = false;
    private long frame = 0;
    private long score,highScore;
    private int curStage = 0;
    private final int totalStage = 2;
    private boolean shiedCD,forkCD,forkAmmo;
    private int shootCD;

    private SharedPreferences data;

    private SoundPool playerShotPool,enemyShotPool;

    private Context c;
    private int canvasWidth, canvasHeight;


    public gameView(Context context) {
        super(context);
        c = context;
    }

    public gameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        c = context;
    }

    public gameView(Context context,  AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        c = context;
    }


    public void start(int[] ids){
        //clear
        destroy();

        highScore = 0;
        //get pic
        Log.d("bitmapFin","not yet");
        for(int id:ids) bitmaps.add(BitmapFactory.decodeResource(getResources(),id));
        postInvalidate();

        try {
            initSP();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d("bitmapFin","Y");
        //real start
        realStart();
    }


    private void initSP() throws Exception{
        //设置最多可容纳5个音频流，音频的品质为5
        SoundPool.Builder soundPoolBuilder = new SoundPool.Builder();
        soundPoolBuilder.setMaxStreams(3);
        soundPoolBuilder.setAudioAttributes(new AudioAttributes.Builder()
                                                .setLegacyStreamType(AudioManager.STREAM_MUSIC)
                                                .build());
        playerShotPool = soundPoolBuilder.build();
        enemyShotPool = soundPoolBuilder.build();

        enemyShotID = enemyShotPool.load(c,R.raw.shot_alien,1);
        playerShotID = playerShotPool.load(c,R.raw.shot,1);
        //soundID[2] = spl.load(c,R.raw.blast,1);

    }

    private void realStart(){
        data = c.getSharedPreferences("data",MODE_PRIVATE);
        int pow = data.getInt("playerATK",5);
        int bld = data.getInt("playerBLOOD",10);

        player = new player(bitmaps.get(0),bitmaps.get(8),bld,pow);
        //initial value
        status = statusStart;
        shiedCD = false;
        forkCD = false;
        forkAmmo = false;
        score = 0;
        shootCD = 30;

        //start
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvasHeight = canvas.getHeight();
        canvasWidth = canvas.getWidth();

        if(frame == 0) checkNextStage(canvas);
        if(frame % shootCD == 0 && status == statusStart) playerShoot();


        switch (status){
            case statusStart:
                drawGame(canvas);
                break;
            case statusPause:
                drawPause(canvas);
                break;
            case statusGameOver:
                drawGameOver(canvas);
                break;
            case statusWin:
                drawGameWin(canvas);
                touchToHome = true;
            default:
                break;
        }

        frame++;
    }

    private void drawGame(Canvas canvas) {
        mPaint.setTextSize(50);
        //player bullet
        Iterator<bullet> bulletIterator= bullets.iterator();
        while(bulletIterator.hasNext()){
            bullet bullet = bulletIterator.next();
            bullet.draw(canvas);
            bullet.move(0,0);
            if(bulletDestroy(bullet)) bulletIterator.remove();
        }
        //draw enemy
        Iterator<Enemy> enemyIterator= enemies.iterator();
        while(enemyIterator.hasNext()){
            Enemy e = enemyIterator.next();
            if(e.shoot()) enemyShoot(e);
            e.move(canvas.getWidth(), canvas.getHeight() /2);
            if(collidePlayer(e)) {
                if(e.getBlood() > 5) {
                    for (int i = 0;i < 5;i++) e.damage(5);
                }
                else enemyIterator.remove();
            }
            e.draw(canvas);
            hasEnemy = true;
        }

        //draw drops
        Iterator<dropItem> rewardIterator= rewards.iterator();
        while(rewardIterator.hasNext()){
            dropItem reward = rewardIterator.next();
            reward.move(0,0);
            if(collidePlayer(reward)) rewardIterator.remove();
            else if (reward.getY() > canvas.getHeight()){
                    if (reward.getRewardType() == 7) shiedCD = false;
                    if (reward.getRewardType() == 10) forkCD = false;

                    rewardIterator.remove();
                }
                else reward.draw(canvas);
        }

        //draw player
        if(player.getAlive()) player.draw(canvas);
        else status = statusGameOver;

        checkNextStage(canvas);

        //draw score
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setTextSize(50);
        mPaint.setColor(Color.WHITE);
        canvas.drawText("SCORE:"+String.valueOf(score),20,50,mPaint);

        //draw pause button
        canvas.drawBitmap(bitmaps.get(5),canvas.getWidth()-20-bitmaps.get(5).getWidth(),20,mPaint);
        postInvalidate();
    }

    private void drawPause(Canvas canvas){
        //player bullet
        for (com.game.bing.starwar.game.bullet bullet : bullets) {
            bullet.draw(canvas);
        }
        //draw enemy
        for (Enemy e:enemies){
            e.draw(canvas);
        }

        //draw player
        player.draw(canvas);

        //draw score
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setTextSize(50);
        mPaint.setColor(Color.WHITE);
        canvas.drawText("SCORE:"+String.valueOf(score),20,50,mPaint);

        //draw pause button
        canvas.drawBitmap(bitmaps.get(5),canvas.getWidth()-20-bitmaps.get(5).getWidth(),20,mPaint);

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setTextSize(80);
        mPaint.setColor(Color.WHITE);
        canvas.drawText("煞氣a時間暫停!",canvas.getWidth()/2-300,canvas.getHeight()/2,mPaint);

        postInvalidate();
    }

    private void drawGameOver(Canvas canvas) {
        countPoint();

        if(gameOverShow) return;// not repeat paint

        AlertDialog reviveDialog = new AlertDialog.Builder(c)
                                    .setTitle("Hint")
                                    .setMessage("你死惹Q_Q")
                                    .setNegativeButton("重新開始", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Log.d("act","restart");
                                            restart();
                                        }
                                    })
                                    .create();
        reviveDialog.setCancelable(false);
        reviveDialog.show();



        gameOverShow = true;
    }

    private void restart() {
        destroyRecycleBitmaps();
        realStart();
    }

    private void destroyRecycleBitmaps() {
        gameOverShow = false;
        frame = 0;
        curStage = 0;
        score = 0;
        if(player != null) player.destroy();
        player = null;
        for (bullet b:bullets)  b.destroy();
        bullets.clear();
        for (Enemy e:enemies) e.destroy();
        enemies.clear();
        for (dropItem reward:rewards) reward.destroy();
        rewards.clear();

    }

    private void destroy(){
        destroyRecycleBitmaps();
        //clear bitmap
        for(Bitmap bitmap:bitmaps) bitmap.recycle();
        bitmaps.clear();
    }

    private void checkNextStage(Canvas canvas) {
        if(hasEnemy) {
            hasEnemy = false;
            return;
        }
        curStage++;
        if(curStage <= totalStage) {
            showText("目前關卡:"+String.valueOf(curStage));
            loadStage(canvas);
            player.setX(canvas.getWidth()/2);
            player.setY(canvas.getHeight()-200);
        }
        else status = statusWin;
    }

    private void drawGameWin(Canvas canvas){
        countPoint();
        if (score > highScore) highScore = score;

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setTextSize(200);
        mPaint.setColor(Color.GREEN);
        mPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("VICTORY",canvasWidth/2,400,mPaint);

        mPaint.setTextSize(80);
        mPaint.setColor(Color.WHITE);
        canvas.drawText("High Score : "+String.valueOf(highScore),canvasWidth/2,canvas.getHeight()/2,mPaint);
        canvas.drawText("Score : "+String.valueOf(score),canvasWidth/2,canvas.getHeight()/2+200,mPaint);

        mPaint.setTextSize(60);
        mPaint.setColor(Color.GREEN);
        canvas.drawText("觸碰返回主畫面 ",canvas.getWidth()/2,canvas.getHeight()-300,mPaint);

    }

    private void loadStage(Canvas canvas) {
        Enemy enemy;
        switch (curStage){
            case 1:
                for (int x = 1; x < 8;x++){
                    for (int y = 1;y < 5;y++){
                        enemy = new Enemy(bitmaps.get(3),canvas.getWidth()*x/8,20+canvas.getHeight()/2*y/5,40,curStage,1);
                        enemies.add(enemy);
                    }
                }
                break;
            case 2:
                enemy = new Enemy(bitmaps.get(2), canvas.getWidth() / 2, 300, 1000, curStage,3);
                enemies.add(enemy);
                break;
            default:
                break;
        }
    }

    private void playerShoot() {
        bullet bullet = new bullet(bitmaps.get(1),player.getX(),player.getY()-100,3, com.game.bing.starwar.game.player.getPlayerPower());
        bullets.add(bullet);
        if (forkAmmo){
            bullet = new bullet(bitmaps.get(1),player.getX(),player.getY()-100,3,com.game.bing.starwar.game.player.getPlayerPower());
            bullet.setdx(1);
            bullets.add(bullet);

            bullet = new bullet(bitmaps.get(1),player.getX(),player.getY()-100,3,com.game.bing.starwar.game.player.getPlayerPower());
            bullet.setdx(-1);
            bullets.add(bullet);

        }

        playerShotPool.play(playerShotID,1,1,1,0,1);
    }

    private void enemyShoot(Enemy e){
        bullet bullet = new bullet(bitmaps.get(4),e.getX(),e.getY()+150,4,e.getAtkPower());
        bullet.setdy(10);
        bullets.add(bullet);
        enemyShotPool.play(enemyShotID,1,1,1,0,1);
    }

    private boolean bulletDestroy(bullet bullet) {
        return bullet.getY() < 0 || bullet.getY() > canvasHeight || collideEnemy(bullet) || collidePlayer(bullet);
    }

    private boolean collideEnemy(sprite sprite) {
        Iterator<Enemy> EnemyIterator = enemies.iterator();
        while (EnemyIterator.hasNext()){
            Enemy e = EnemyIterator.next();
            if(sprite.getRender().intersect(e.getRender()) && sprite.getType() == 3) {
                e.damage(com.game.bing.starwar.game.player.getPlayerPower());
                score += 1;
                if(e.dropReward()) { //掉落機制待平衡
                    dropReward(e);
                }
                if(e.getBlood() <= 0) {
                    EnemyIterator.remove();
                    score += 5;
                }
                return true;
            }
        }
        return  false;
    }

    private void dropReward(Enemy e) {
        int temp = 0;//6 heal 7 shied 9 ammo
        boolean drop = true;
        double r = Math.random();
        if(r < 0.3 && !shiedCD) {
            temp = 7;
            shiedCD = true;
        }
        else if(r < 0.5) temp = 6;
        else if(r < 0.8) temp = 9;
        else if (!forkCD) {
            temp = 10;
            forkCD = true;
        }
        else drop = false;

        if(drop){
            dropItem reward = new dropItem(bitmaps.get(temp),e.getX(),e.getY()+20,5,temp);
            rewards.add(reward);
        }

    }

    private boolean collidePlayer(sprite sprite){
        if(sprite.getRender().intersect(player.getRender())) {
            switch (sprite.getType()) {
                case 1:
                    //enemy
                    if (!player.shiedStatus()) playerDead();
                    else player.setShied(false);

                    break;
                case 3:
                    //bullet
                    return false;
                case 4:
                    //Enemy bullet
                    bullet b = (bullet) sprite;
                    if (!player.shiedStatus()){
                        player.damage(b.getAtkPower());
                        if(player.getBlood() < 0) playerDead();
                    }
                    break;
                case 5:
                    //reward
                    score += 2;
                    dropItem d = (dropItem) sprite;
                    switch (d.getRewardType()){
                        case 6:
                            //heal
                            int maxBlood = com.game.bing.starwar.game.player.getMaxBlood();
                            player.heal(maxBlood/10);
                            Log.d("maxBlood", String.valueOf(maxBlood));
                            if (player.getBlood() > maxBlood) player.setBlood(maxBlood);//void over heal
                            break;
                        case 7:
                            //shied
                            player.setShied(true);
                            setShiedTimer();
                            break;
                        case 9:
                            //ammo
                            shootCD = (int)(shootCD *0.75);
                            if (shootCD < 10) shootCD = 10;
                            break;
                        case 10:
                            //fork
                            forkAmmo = true;
                            setForkTimer();

                    }
                    break;
            }
            return true;
        }
        return false;
    }

    private void setForkTimer() {
        Timer shiedTimer = new Timer();
        TimerTask shiedTask = new TimerTask() {
            @Override
            public void run() {
                forkAmmo = false;
                forkCD = false;
            }
        };
        shiedTimer.schedule(shiedTask,3000);
    }

    private void setShiedTimer(){
        Timer shiedTimer = new Timer();
        TimerTask shiedTask = new TimerTask() {
            @Override
            public void run() {
                player.setShied(false);
                shiedCD = false;
            }
        };
        shiedTimer.schedule(shiedTask,3000);
    }

    private void playerDead() {
        player.setAlive(false);
    }

    private void showText(String s){
        Toast.makeText(c,s,Toast.LENGTH_SHORT).show();
    }

    private void countPoint(){
        int getPoint = (int) score/100;
        int oiPoint = data.getInt("restPoint",0);

        SharedPreferences.Editor editor = data.edit();
        editor.putInt("restPoint",oiPoint + getPoint);
        editor.apply();

        showText("獲得"+String.valueOf(getPoint)+"點升級點");
    }

    private RectF getPauseRender(){
        return new RectF(canvasWidth-20-bitmaps.get(3).getWidth(),20,canvasWidth-20,20+bitmaps.get(3).getHeight());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();

        if(event.getAction() == MotionEvent.ACTION_DOWN){
            if (touchToHome) goHome = true;
            if(player.getRender().contains(touchX,touchY) && status == statusStart) move = true;
            else if(getPauseRender().contains(touchX,touchY)) status = status*-1;
        }
        else if(event.getAction() == MotionEvent.ACTION_UP) move = false;
       if(move) {
           player.setPosition(touchX,touchY);
       }
        return true;
    }

    public boolean goHome(){
        return goHome;
    }


}
