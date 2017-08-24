package com.kevin.www.sensordemo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    ShakeUtils shakeUtils;
    private ConfirmDialog confirmDialog;
    private View rootView;
    private TextView position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        confirmDialog = new ConfirmDialog(this);
        rootView = findViewById(R.id.rootView);

        TextView tvSensors = (TextView) findViewById(R.id.sensor);
        position = (TextView) findViewById(R.id.position);
        //获取传感器SensorManager对象
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_ALL);
        for (Sensor sensor : sensors) {
            tvSensors.append(sensor.getName() + "\n");
        }

        shakeUtils = new ShakeUtils(this);
        shakeUtils.setOnShakeListener(new ShakeUtils.OnShakeListener() {
            @Override
            public void onShake() {
                Toast.makeText(MainActivity.this, "摇了摇", Toast.LENGTH_SHORT).show();
                confirmDialog.setOnConfirmListener(new ConfirmDialog.OnConfirmClickListener() {
                    @Override
                    public void onFeedBack() {
                        gotoFeedBackActivity();
                    }

                    @Override
                    public void onScreenShot() {
                        saveScreenShoot(rootView);

                    }
                });
                confirmDialog.show();

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        shakeUtils.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        shakeUtils.onResume();
    }

    private void gotoFeedBackActivity() {
        Intent intent = new Intent(MainActivity.this, FeedBackActivity.class);
        startActivity(intent);
    }


    public void saveScreenShoot(View rootView) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.US);
        File tempfile = new File(FileUtils.getExternalCacheDir(this),
                sdf.format(new Date()));
        View view = rootView.getRootView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        if (bitmap != null) {
            System.out.println("bitmapgot!");
            try {
                FileOutputStream out = new FileOutputStream(tempfile);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                position.setText("图片已存储:" + tempfile.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("bitmap is NULL!");
        }
    }
}
