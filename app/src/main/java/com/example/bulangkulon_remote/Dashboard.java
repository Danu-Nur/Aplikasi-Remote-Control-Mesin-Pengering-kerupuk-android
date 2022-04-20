package com.example.bulangkulon_remote;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.bulangkulon_remote.menu.AturMesin;
import com.example.bulangkulon_remote.menu.AturSuhu;
import com.example.bulangkulon_remote.menu.AturTimer;

import org.json.JSONException;
import org.json.JSONObject;

public class Dashboard extends AppCompatActivity {

    CardView btn_mesin, btn_suhu, btn_timer;
    Intent intent;
    private long backPressedTime = 0;
    public static final String url = Server.URL + "hp_baca.php";
    private static final String TAG = Dashboard.class.getSimpleName();
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    String tag_json_obj = "json_obj_req";
    ConnectivityManager conMgr;
    int success;
    TextView statuss, mesinn, suhuu, batterai;

    Handler handler = new Handler();
    Runnable refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        {
            if (conMgr.getActiveNetworkInfo() != null
                    && conMgr.getActiveNetworkInfo().isAvailable()
                    && conMgr.getActiveNetworkInfo().isConnected()) {
            } else {
                Toast.makeText(getApplicationContext(), "Tidak Terhubung Ke Internet",
                        Toast.LENGTH_LONG).show();
            }
        }

        btn_mesin = (CardView) findViewById(R.id.btn_mesin);
        btn_suhu = (CardView) findViewById(R.id.btn_suhu);
        btn_timer = (CardView) findViewById(R.id.btn_timer);

        statuss = (TextView) findViewById(R.id.statuss);
        mesinn = (TextView) findViewById(R.id.mesinn);
        suhuu = (TextView) findViewById(R.id.suhuu);
        batterai = (TextView) findViewById(R.id.batterai);
        cleartxt();

        refresh = new Runnable() {
            public void run() {
                // Do something
                handler.postDelayed(refresh, 3000);
                loadDataMesin();
            }
        };
        handler.post(refresh);

        btn_mesin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                handler.removeCallbacks(refresh);
                intent = new Intent(Dashboard.this, AturMesin.class);
                startActivity(intent);
                finish();

            }
        });

        btn_suhu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                handler.removeCallbacks(refresh);
                intent = new Intent(Dashboard.this, AturSuhu.class);
                startActivity(intent);
                finish();

            }
        });

        btn_timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.removeCallbacks(refresh);
                intent = new Intent(Dashboard.this, AturTimer.class);
                startActivity(intent);
                finish();

            }
        });
    }

    public void cleartxt() {
        statuss.setText("");
        mesinn.setText("");
        suhuu.setText("");
        batterai.setText("");
    }

    private void loadDataMesin() {
        StringRequest strReq = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Response Dashboard : " + response.toString());

                        try {
                            JSONObject jObj = new JSONObject(response);
                            success = jObj.getInt(TAG_SUCCESS);

                            // Cek error node pada json
                            if (success == 1) {
                                String id = jObj.getString("iddata");
                                String status = jObj.getString("status");
                                String mesin = jObj.getString("mesin");
                                String suhu = jObj.getString("suhu");
                                String batt = jObj.getString("batt");

                                String m;
                                statuss.setText(status);
                                if (Integer.parseInt(mesin) == 2) {
                                    m = "HIDUP";
                                } else {
                                    m = "MATI";
                                }
                                mesinn.setText(m);
                                suhuu.setText(suhu + " °C");
                                batterai.setText(batt + " V");
                            } else {
                                Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            // JSON error
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    @Override
    public void onBackPressed() {
        long t = System.currentTimeMillis();
        if (t - backPressedTime > 2000) {    // 2 secs
            backPressedTime = t;
            Toast.makeText(this, "Tekan Tombol 'Kembali' lagi untuk keluar",
                    Toast.LENGTH_SHORT).show();
        } else {    // this guy is serious
            // clean up
            super.onBackPressed();
            handler.removeCallbacks(refresh);
        }
    }
}