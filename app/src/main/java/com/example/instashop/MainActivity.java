package com.example.instashop;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity
{

    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                openScanApp();
            }
        });
        hideNavigationBar();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        hideNavigationBar();
    }

    @SuppressLint("InlinedApi")
    private void hideNavigationBar()
    {
        this.getWindow().getDecorView()
                .setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_FULLSCREEN |
                                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                );
    }


    public void openScanApp()
    {
        Intent intent = new Intent(this, ScanActivity.class);
        startActivity(intent);
    }
}

