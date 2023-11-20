package com.marcos.longhini.agrosocial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

public class SobreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sobre);

        TextView txtVersion = findViewById(R.id.txtVersion);
        TextView txtNumber = findViewById(R.id.txtNumber);

        PackageManager packageManager = getPackageManager();
        try {
            // Get the package information
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);

            // Retrieve the version information
            String versionName = packageInfo.versionName;
            int versionCode = packageInfo.versionCode;

            // Use the version information
            txtVersion.setText(versionName);

            txtNumber.setText(String.valueOf(versionCode));

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }
}