package kr.co.mikarusoft.seoul_happyplus_finder;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.SurfaceTexture;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

import static android.provider.Settings.Secure.isLocationProviderEnabled;

public class MainControlActivity extends AppCompatActivity implements TextureView.SurfaceTextureListener{

    private TextureView lineView;
    ArrayList<GPS> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_control);

        enableGPSSetting();

        LocationManager manager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        Double latitude = 0.;
        Double longitude = 0.;






        try{
            Location location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if(location != null)
            {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                Toast.makeText(this.getApplicationContext(),latitude + " : " + longitude,Toast.LENGTH_LONG).show();
            }else
            {
                location = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                if(location != null)
                {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();

                    Toast.makeText(this.getApplicationContext(),latitude + " : " + longitude,Toast.LENGTH_LONG).show();
                }else
                {

                    Toast.makeText(this.getApplicationContext(),"위치정보없음",Toast.LENGTH_LONG).show();


                }

            }

        }catch(Exception e)
        {
            Toast.makeText(this.getApplicationContext(),"못가져옴",Toast.LENGTH_LONG).show();
        }


        list= new ArrayList<GPS>();
        list.add(new GPS("내가있는위치",latitude,longitude));
        list.add(new GPS("목동",37.5374037,126.8823938));
        list.add(new GPS("시청역점",37.564746,126.9774173));
        list.add(new GPS("시청점",37.5665477,126.9782505));
        list.add(new GPS("상상플러스점",37.5495289,127.0772274));
        list.add(new GPS("대치동점",37.4972088,127.0558727));

        lineView = (TextureView)findViewById(R.id.tv);
        lineView.setSurfaceTextureListener(this);





        Button button = (Button)findViewById(R.id.go_mokdong);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                intent.putExtra("name", "mokdong");
                intent.putExtra("latitude", 37.5374037);
                intent.putExtra("longitude", 126.8823938);
                startActivity(intent);
            }
        });

        Button button2 = (Button)findViewById(R.id.go_city_hall_station);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                intent.putExtra("name", "cityhallstation");
                intent.putExtra("latitude", 37.564746);
                intent.putExtra("longitude", 126.9774173);
                startActivity(intent);
            }
        });

        Button button3 = (Button)findViewById(R.id.go_new_city_hall);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                intent.putExtra("name", "newcityhall");
                intent.putExtra("latitude", 37.5665477);
                intent.putExtra("longitude", 126.9782505);
                startActivity(intent);
            }
        });

        Button button4 = (Button)findViewById(R.id.go_seoul_sangsang);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                intent.putExtra("name", "sangsangnara");
                intent.putExtra("latitude", 37.5495289);
                intent.putExtra("longitude", 127.0772274);
                startActivity(intent);
            }
        });

        Button button5 = (Button)findViewById(R.id.go_daechi);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                intent.putExtra("name", "deachi");
                intent.putExtra("latitude", 37.4972088);
                intent.putExtra("longitude", 127.0558727);
                startActivity(intent);
            }
        });
    }
    private void enableGPSSetting(){
        ContentResolver res = getContentResolver();

        boolean gpsEnabled = isLocationProviderEnabled(res, LocationManager.GPS_PROVIDER);

        if (!gpsEnabled) {
            new AlertDialog.Builder(this).setTitle("GPS 설정")
                    .setMessage("GPS가 꺼져 있습니다.\nGPS를 켜시겠습니까?")
                    .setPositiveButton("GPS 켜기", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("닫기", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();
        }
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        int gap = 140;



        int w  = width-gap*2; //가로, 세로 50씩 공간 주기 위해
        int h  = height - gap*2;

        int longW = (w>h)? h:w;

        double minX=1000.;
        double maxX=0.;
        double minY=1000.;
        double maxY =0.;


        for(int i=0;i<list.size();i++)
        {
            if(minX>list.get(i).latitude)
                minX = list.get(i).latitude;
            if(maxX<list.get(i).latitude)
                maxX = list.get(i).latitude;


            if(minY>list.get(i).longitude)
                minY = list.get(i).longitude;
            if(maxY<list.get(i).longitude)
                maxY = list.get(i).longitude;
        }
        double mx = maxY-minY;
        double my = maxY-minY;
        double mxy = (mx > my)? mx:my;

        double be = longW/mxy;



        double per = mxy/longW;

        Log.d("per","가로 mx---->"+mx+"    세로- my --->"+my+"   ====>"+mxy+"   per:"+per);









        Canvas canvas = lineView.lockCanvas();
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.YELLOW);
        canvas.drawRect(gap,gap,gap+longW,gap+longW,paint);
        canvas.drawRect(0,0,width,height,paint);

        int colorlist[] ={Color.RED, Color.BLUE ,Color.BLACK, Color.GREEN, Color.GRAY,Color.DKGRAY};
        paint.setColor(Color.RED);

        int a=1; // 글자의 가로인지 세로인지 안겹치게 하기위해 서 서울시청역점과 시청점이 겹치는것을 방지

        for(int i=0;i<list.size();i++)
        {
            a=-a;

            paint.setColor(colorlist[i]);

            Log.d("구분"+i,"-------------------------------------");



            float x = (float)(             (list.get(i).latitude - minX)*be         )+gap;
            float y = (float)((list.get(i).longitude - minY)*be)+gap;
            Log.d("가로 세로길이","x---->"+x+"    y --->"+y);
            x=longW-x;
            paint.setTextSize(30);
            paint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(list.get(i).name,y,x-50*a,paint);
            canvas.drawCircle(y,x,20,paint);
        }


        lineView.unlockCanvasAndPost(canvas);
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }
}
