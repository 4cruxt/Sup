package com.fole_Studios.sup;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.fole_Studios.sup.authentication.AuthActivity;

public class SplashActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Intent _authIntent = new Intent(SplashActivity.this, AuthActivity.class);
        startActivity(_authIntent);
        finish();
    }
}