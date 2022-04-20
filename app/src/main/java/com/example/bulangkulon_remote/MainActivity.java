package com.example.bulangkulon_remote;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.bulangkulon_remote.menu.AturMesin;

public class MainActivity extends AppCompatActivity {

    Animation topAnim;
    android.view.animation.Animation botAnim;
    ImageView img1, logo1;
    ImageButton btn1;
    TextView txt1, txt2;
    LinearLayout ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        botAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        img1 = (ImageView) findViewById(R.id.img1);
        logo1 = (ImageView) findViewById(R.id.logo1);
        btn1 = (ImageButton) findViewById(R.id.btn1);
        txt1 = (TextView) findViewById(R.id.txt1);
        txt2 = (TextView) findViewById(R.id.txt2);
        ll = (LinearLayout) findViewById(R.id.linier1) ;

        img1.setAnimation(topAnim);
        logo1.setAnimation(topAnim);
        btn1.setAnimation(botAnim);
        txt1.setAnimation(botAnim);
        txt2.setAnimation(botAnim);
        ll.setAnimation(botAnim);

        Thread thread = new Thread() {
            public void run() {
                try {
                    sleep(3000);

                } catch (InterruptedException e) {
                    e.printStackTrace();

                } finally {
                    PackageManager pm = getPackageManager();

                    pm.setComponentEnabledSetting(new ComponentName(MainActivity.this, Dashboard.class),
                            PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
                    Intent intent = new Intent(MainActivity.this, Dashboard.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
        thread.start();
    }
}