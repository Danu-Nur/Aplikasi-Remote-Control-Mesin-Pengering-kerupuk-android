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
import android.widget.EditText;
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

public class AturSuhu extends AppCompatActivity {
    public static final String url = Server.URL + "hp_baca.php";
    public static final String url_update = Server.URL + "hp_kirim_kontrolsuhu.php";
    private static final String TAG = AturSuhu.class.getSimpleName();
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    String tag_json_obj = "json_obj_req";
    ConnectivityManager conMgr;
    int success;
    Handler handler = new Handler();
    Runnable refresh;
    TextView ssuhu, suhuatass, suhubawahh;
    EditText nn, tt;
    ImageButton simpan;
    Integer naik, turun;
    String edit1, edit2;
    Integer txtnaik, txtturun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atur_suhu);

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
        toolbar.setTitle("KONTROL SUHU");
        toolbar.setBackgroundColor(getColor(R.color.tranparant));
        toolbar.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        toolbar.setTitleTextColor(getColor(R.color.btn_hijau));
        setSupportActionBar(toolbar);

        ssuhu = (TextView) findViewById(R.id.suhuu);
        suhuatass = (TextView) findViewById(R.id.suhuatas);
        suhubawahh = (TextView) findViewById(R.id.suhubawah);
        simpan = (ImageButton) findViewById(R.id.btn_simpan);
        nn = (EditText) findViewById(R.id.editnaik);
        tt = (EditText) findViewById(R.id.editturun);
        cleartxt();

        refresh = new Runnable() {
            public void run() {
                // Do something
                handler.postDelayed(refresh, 3000);
                loadStatusSuhu();
            }
        };
        handler.post(refresh);

        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit1 = nn.getText().toString();
                edit2 = tt.getText().toString();
                txtnaik = Integer.valueOf(edit1);
                txtturun = Integer.valueOf(edit2);
                if (txtnaik < txtturun) {
                    Toast.makeText(getApplicationContext(), "Suhu NAIK tidak boleh lebih kecil dari suhu TURUN",
                            Toast.LENGTH_LONG).show();
                } else if (txtnaik == txtturun) {
                    Toast.makeText(getApplicationContext(), "Suhu NAIK tidak boleh sama dengan suhu TURUN",
                            Toast.LENGTH_LONG).show();
                } else {
                    edit1 = nn.getText().toString();
                    edit2 = tt.getText().toString();
                    update_data();
                    clearedit();
                }

            }
        });
    }

    public void clearedit(){
        nn.setText("");
        tt.setText("");
    }

    public void cleartxt() {
        ssuhu.setText("");
        suhuatass.setText("");
        suhubawahh.setText("");
    }

    private void loadStatusSuhu() {
        StringRequest strReq = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Response Suhu : " + response.toString());

                        try {
                            JSONObject jObj = new JSONObject(response);
                            success = jObj.getInt(TAG_SUCCESS);

                            // Cek error node pada json
                            if (success == 1) {
                                String id = jObj.getString("iddata");
                                String suhu = jObj.getString("suhu");
                                String atas = jObj.getString("suhuatas");
                                String bawah = jObj.getString("suhubawah");

                                ssuhu.setText(suhu + " °C");
                                suhuatass.setText(atas + " °C");
                                suhubawahh.setText(bawah + " °C");

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

                        loadStatusSuhu();

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

                params.put("atas", edit1);
                params.put("bawah", edit2);
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
//        pm.setComponentEnabledSetting(new ComponentName(AturSuhu.this, Dashboard.class),
//                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
//
//        pm.setComponentEnabledSetting(new ComponentName(AturSuhu.this, AturSuhu.class),
//                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);

        handler.removeCallbacks(refresh);
        Intent intent = new Intent(AturSuhu.this, Dashboard.class);
        startActivity(intent);
        this.finish();

    }
}