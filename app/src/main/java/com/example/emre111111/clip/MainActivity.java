package com.example.emre111111.clip;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
//import android.support.v4.widget.SearchViewCompatIcs;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.app.NotificationManager;
import android.app.PendingIntent;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {
    private final String TAG=this.getClass().getSimpleName();
    Button bEmre,btnjsongetir;
    ClipboardManager clpEmre;
    TextView tvEmre,tvjsonsonuc;

    NotificationCompat.Builder notification;
    private static final int uniqueID = 45612;
    String SCvrAnaCumle = null, SCvrCevirelen = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bEmre = (Button) findViewById(R.id.btnEmre);
        tvEmre = (TextView) findViewById(R.id.tvYapistir);
        clpEmre = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        btnjsongetir=(Button)findViewById(R.id.btnjson);
        tvjsonsonuc=(TextView)findViewById(R.id.tvpost);
//notifi
        //super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        notification = new NotificationCompat.Builder(this);
        notification.setAutoCancel(true);
//notifi end


        /**Kopyalama Tetikleme Kısmı
         *       Eklenecekler:
         *        çeviri apisi bu kısma eklenecek
         * */
        clpEmre.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {
            @Override
            public void onPrimaryClipChanged() {

                ClipData data = clpEmre.getPrimaryClip();
                ClipData.Item item = data.getItemAt(0);
                String ClipBoardAlinan = item.getText().toString();
                tvEmre.setText(ClipBoardAlinan);
                SCvrAnaCumle = ClipBoardAlinan;
                //SCvrCevirelen = "lorem ipsum";

               CEVIR();



            //   NotifiGoster();

                Toast.makeText(MainActivity.this, "Çeviri Yapıldı Bildirimi Kontrol Et", Toast.LENGTH_LONG).show();

                Log.d(TAG,"clip change bitti");
            }

        });/**Kopyalama Tetikleme Kısmı ____SON*/


        bEmre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotifiGoster();
                /*
                 ClipData data=clpEmre.getPrimaryClip();
                ClipData.Item item=data.getItemAt(0);
                String Text=item.getText().toString();
                tvEmre.setText(Text);
                Toast.makeText(MainActivity.this,"yapistitttt",Toast.LENGTH_LONG).show();
                */
            }
        });









    }//on create sonu


    public int CEVIR(){

        Log.i(TAG,"Ceviri Basladi");
        String jsonurl="https://translate.yandex.net/api/v1.5/tr.json/translate?key=trnsl.1.1.20170118T003338Z.f214da86872ae232.1dee3d16f8c722034eb5b5b54dfe7b5a9ca061ab&text="
                +SCvrAnaCumle+
                "&lang=en-tr&[format=html]&[options=1]&[callback=emreemre]";

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, jsonurl,(String) null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG,"ONRESPONSE");

                        try {
                            tvjsonsonuc.setText(response.getString("text"));
                            SCvrCevirelen=response.getString("text");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        NotifiGoster();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG,"ceviri on error");
                error.printStackTrace();
            }
        });

        MySingleton.getInstence(MainActivity.this).addToRequestque(jsonObjectRequest);

        Log.i(TAG,"Ceviri bitti");



return 1;
    }


    public void NotifiGoster() {
        Log.d(TAG,"Notifi Basladı");
        notification.setSmallIcon(R.drawable.ikilik);
        notification.setTicker("This is the ticker");
        notification.setWhen(System.currentTimeMillis());
        notification.setContentTitle(SCvrAnaCumle);
        notification.setContentText(SCvrCevirelen);

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setContentIntent(pendingIntent);

        //Builds notification and issues it
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.notify(uniqueID, notification.build());


       // Toast.makeText(MainActivity.this, "emre noti calisti", Toast.LENGTH_LONG).show();
        Log.d(TAG,"Notifi Bitti");

    }


    /**
     * post
     */


}
