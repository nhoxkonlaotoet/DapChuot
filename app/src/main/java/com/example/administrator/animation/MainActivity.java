package com.example.administrator.animation;

import android.content.res.Resources;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import static android.provider.SyncStateContract.Helpers.update;

public class MainActivity extends AppCompatActivity {

    ImageView imgvBaby,imgvPirate,btnPlay;
    TextView txtScore;
    Integer score;
    CountDownTimer cdt,cdt_pirate;
    Boolean clicked=false;
    Integer life=3;
    Integer ratio=2;
    Boolean gameOver=false;
    long interval=3000;

    ImageView imgvFirstLife, imgvSecondLife, imgvLastLife;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        score=0;
        imgvFirstLife = findViewById(R.id.imgvFirstLife);
        imgvSecondLife = findViewById(R.id.imgvSecondLife);
        imgvLastLife = findViewById(R.id.imgvLastLife);
        imgvBaby = findViewById(R.id.imgvMole);
        imgvPirate = findViewById(R.id.imgvNoClick);
        btnPlay = findViewById(R.id.btnPlay);
        txtScore = findViewById(R.id.txtScore);



      //  imgvBaby.setAnimation(animationAlpha);



        //xuất hiện trừ điểm
        cdt_pirate = new CountDownTimer(9000000, 3000) {
            @Override
            public void onTick(long l) {
                if (score > 5) {
                    imgvPirate.setVisibility(View.INVISIBLE);
                    Random rand = new Random();
                    int i = rand.nextInt(ratio);
                    if (i != 1) {

                        final int pirateX = rand.nextInt(getScreenWidth()-50)+50 -imgvPirate.getWidth();
                        final int pirateY = rand.nextInt(getScreenHeight()-50)+50 -imgvPirate.getHeight();
                        imgvPirate.setX(pirateX);
                        imgvPirate.setY(pirateY);
                        imgvPirate.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFinish() {
                cdt_pirate.start();
            }
        };


        imgvPirate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangeLifes(-1);
                imgvPirate.setVisibility(View.INVISIBLE);
            }
        });
        imgvBaby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(gameOver)
                    return;
                imgvBaby.setVisibility(View.INVISIBLE);
                score++;
                txtScore.setText(score.toString());
                clicked=true;
                if(score%10==0) {

                    lvUp();
                }
                try {
                    cdt.cancel();
                }catch (Exception e){}
                cdt= new CountDownTimer(900000000, interval) {

                    public void onTick(long millisUntilFinished) {
                        Random rand = new Random();

                        final int x = rand.nextInt(getScreenWidth()-50)+50 -imgvBaby.getWidth();
                        final int y = rand.nextInt(getScreenHeight()-50)+50 -imgvBaby.getHeight();
                        imgvBaby.setX(x);
                        imgvBaby.setY(y);
                        imgvBaby.setVisibility(View.VISIBLE);


                        if(!clicked)
                        {
                            ChangeLifes(-1);
                            // thua
                            if(life<=0) {
                                Toast.makeText(MainActivity.this, "Gameover mất rồi :(", Toast.LENGTH_SHORT).show();
                                gameOver=true;
                                cancel();
                                cdt_pirate.cancel();
                                btnPlay.setVisibility(View.VISIBLE);
                            }
                        }
                        else {
                            clicked=false;
                        }
                    }
                    public void onFinish() {


                    }
                };
                cdt.start();
            }
        });

   
    }

    void lvUp()
    {
        ratio++;
        setSize(imgvBaby,5,5);
        setSize(imgvPirate,3,3);
        ChangeLifes(1);
        if(interval>=1000 && interval<2000)
            interval-=200;
        if(interval>=2000)
            interval-=500;
        Toast.makeText(MainActivity.this, "Level up!", Toast.LENGTH_SHORT).show();
    }
    void setSize(View view, int witdh, int height)
    {

        ViewGroup.LayoutParams params = view.getLayoutParams();
        if(witdh==0 && height==0) {
            params.height = 70;
            params.width = 70;
        }
        else
        {
            params.height -=height;
            params.width -=witdh;
        }
        view.setLayoutParams(params);

    }
    public void btnPlay_Click(View view)
    {
        ratio=3;
        score=0;
        txtScore.setText(score.toString());
        gameOver=false;
        ChangeLifes(3);
        view.setVisibility(View.INVISIBLE);
        cdt_pirate.start();
        setSize(imgvBaby,0,0);
        setSize(imgvPirate,0,0);
        //vị trí đầu tiên của baby :v
        imgvPirate.setVisibility(View.INVISIBLE);
        Random rand = new Random();
        final int x = rand.nextInt(getScreenWidth()-50)+50 -imgvBaby.getWidth();
        final int y = rand.nextInt(getScreenHeight()-50)+50 -imgvBaby.getHeight();
        imgvBaby.setX(x);
        imgvBaby.setY(y);
        imgvBaby.setVisibility(View.VISIBLE);
    }
    void ChangeLifes(Integer number)
    {
        Animation animationAlpha= AnimationUtils.loadAnimation(this,R.anim.animation_alpha);

        life+=number;
        if(life>3)
            life=3;
        if(life==0)
        {
            imgvFirstLife.setVisibility(View.INVISIBLE);
            imgvSecondLife.setVisibility(View.INVISIBLE);
            imgvLastLife.setVisibility(View.INVISIBLE);
        }
        if(life==1)
        {
            imgvFirstLife.setVisibility(View.VISIBLE);
            imgvSecondLife.setVisibility(View.INVISIBLE);
            imgvLastLife.setVisibility(View.INVISIBLE);
        }
        if(life==2)
        {
            imgvFirstLife.setVisibility(View.VISIBLE);
            imgvSecondLife.setVisibility(View.VISIBLE);
            imgvLastLife.setVisibility(View.INVISIBLE);
        }
        if(life==3)
        {
            imgvFirstLife.setVisibility(View.VISIBLE);
            imgvSecondLife.setVisibility(View.VISIBLE);
            imgvLastLife.setVisibility(View.VISIBLE);
        }
    }
    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }
}
