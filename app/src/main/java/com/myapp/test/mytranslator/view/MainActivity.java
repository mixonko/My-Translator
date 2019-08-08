package com.myapp.test.mytranslator.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.myapp.test.mytranslator.contracts.MainContract;
import com.myapp.test.mytranslator.myAppContext.MyApplication;
import com.myapp.test.mytranslator.R;
import com.myapp.test.mytranslator.presenter.MainPresenter;

public class MainActivity extends AppCompatActivity implements MainContract.View {
    private MainContract.MainPresenter mainPresenter;
    private Button translateText;
    private Button translateCameraText;
    private Button translateVoiceText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainPresenter = new MainPresenter(this);

        translateText = findViewById(R.id.translateText);
        translateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainPresenter.onButtonTranslateTextWasClicked();
            }
        });

        translateCameraText = findViewById(R.id.translateCameraText);
        translateCameraText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainPresenter.onButtonTranslateCameraTextWasClicked();
            }
        });

        translateVoiceText = findViewById(R.id.translateVoiceText);
        translateVoiceText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainPresenter.onButtonTranslateVoiceTextWasClicked();
            }
        });

    }

    @Override
    public void goToTranslateTextActivity() {
        startActivity(new Intent(MyApplication.getAppContext(), TranslateTextActivity.class));
    }

    @Override
    public void goToTranslateCameraTextActivity() {
        startActivity(new Intent(MyApplication.getAppContext(), TranslateCameraTextActivity.class));
    }

    @Override
    public void goToTranslateVoiceTextActivity() {
        startActivity(new Intent(MyApplication.getAppContext(), TranslateVoiceTextActivity.class));
    }

}
