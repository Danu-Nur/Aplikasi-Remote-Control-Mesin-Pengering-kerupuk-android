package com.example.bulangkulon_remote.menu;

import static android.view.View.TEXT_ALIGNMENT_CENTER;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.bulangkulon_remote.AppController;
import com.example.bulangkulon_remote.Dashboard;
import com.example.bulangkulon_remote.R;
import com.example.bulangkulon_remote.Server;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AturMesin extends AppCompatActivity {
    public static final String url = Server.URL + "hp_baca.php";
    public static final String url_update = Server.URL + "hp_kirim_mesin.php";
    private static final String TAG = AturMesin.class.getSimpleName();
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    String tag_json_obj = "json_obj_req";
    ConnectivityManager conMgr;
    int success;
    Handler handler = new Handler();
    Runnable refresh;
    TextView mesinn, komporr, blowerr;
    ImageButton on_of_btn;
    Integer data_mesin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atur_mesin);

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

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("KONTROL MESIN");
        toolbar.setBackgroundColor(getColor(R.color.tranparant));
        toolbar.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        toolbar.setTitleTextColor(getColor(R.color.btn_pink));
        setSupportActionBar(toolbar);

        mesinn = (TextView) findViewById(R.id.statuss_mesin);
        komporr = (TextView) findViewById(R.id.statuss_kompor);
        blowerr = (TextView) findViewById(R.id.statuss_blower);
        on_of_btn = (ImageButton) findViewById(R.id.btn_on_off);
        cleartxt();

        refresh = new Runnable() {
            public void run() {
                // Do something
                handler.postDelayed(refresh, 1500);
                loadStatusMesin();
                if (mesinn.getText().toString() == "HIDUP") {

                    on_of_btn.setBackground(getDrawable(R.drawable.group_132));
                } else {

                    on_of_btn.setBackground(getDrawable(R.drawable.group_131));
                }
            }
        };
        handler.post(refresh);

        on_of_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mesinn.getText().toString() == "HIDUP") {
                    data_mesin = 1;
                    update_data();
                } else {
                    data_mesin = 2;
                    update_data();
                }

            }
        });
    }

    public void cleartxt() {
        komporr.setText("");
        mesinn.setText("");
        blowerr.setText("");
    }

    private void loadStatusMesin() {
        StringRequest strReq = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Response Mesin : " + response.toString());

                        try {
                            JSONObject jObj = new JSONObject(response);
                            success = jObj.getInt(TAG_SUCCESS);

                            // Cek error node pada json
                            if (success == 1) {
                                String id = jObj.getString("iddata");
                                String mesin = jObj.getString("mesin");
                                String kompor = jObj.getString("kompor");
                                String blower = jObj.getString("blower");

                                String m, k, b;
                                if (Integer.parseInt(mesin) == 2) {
                                    m = "HIDUP";
                                } else {
                                    m = "MATI";
                                }
                                mesinn.setText(m);

                                if (Integer.parseInt(kompor) == 2) {
                                    k = "HIDUP";
                                } else {
                                    k = "MATI";
                                }
                                komporr.setText(k);

                                if (Integer.parseInt(blower) == 2) {
                                    b = "HIDUP";
                                } else {
                                    b = "MATI";
                                }
                                blowerr.setText(b);

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

    private void update_data() {
        StringRequest strReq = new StringRequest(Request.Method.POST, url_update, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Cek error node pada json
                    if (success == 1) {

                        loadStatusMesin();

                        Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

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
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap<String, String>();

                params.put("mesinn", String.valueOf(data_mesin));
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

//        PackageManager pm = getPackageManager();
//
//        pm.setComponentEnabledSetting(new ComponentName(AturMesin.this, Dashboard.class),
//                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
//
//        pm.setComponentEnabledSetting(new ComponentName(AturMesin.this, AturMesin.class),
//                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        handler.removeCallbacks(refresh);

        Intent intent = new Intent(AturMesin.this, Dashboard.class);
        startActivity(intent);
        finish();

    }
}