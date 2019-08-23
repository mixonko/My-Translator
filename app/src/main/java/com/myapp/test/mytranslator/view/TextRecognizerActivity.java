package com.myapp.test.mytranslator.view;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.myapp.test.mytranslator.R;
import com.myapp.test.mytranslator.contracts.TextRecognizerContract;
import com.myapp.test.mytranslator.myAppContext.MyApplication;
import com.myapp.test.mytranslator.presenter.TextRecognizerPresenter;

import java.io.IOException;

public class TextRecognizerActivity extends AppCompatActivity implements TextRecognizerContract.View, AdapterView.OnItemSelectedListener, View.OnClickListener {
    private TextRecognizerContract.Presenter presenter;
    private SurfaceView cameraView;
    private TextView translatedText;
    private CameraSource cameraSource;
    private TextBlock item;
    private Spinner firstLang;
    private Spinner secondLang;
    private Button translation;
    private Button takePhoto;
    private Button openPhoto;
    private FrameLayout language;

    private static final String TAG = "MainActivity";
    private static final int requestPermissionID = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_recognizer);

        cameraView = findViewById(R.id.surfaceView);
        translatedText = findViewById(R.id.translatedText);
        translation = findViewById(R.id.translation);
        translation.setOnClickListener(this);
        takePhoto = findViewById(R.id.takePhoto);
        takePhoto.setOnClickListener(this);
        openPhoto = findViewById(R.id.openPhoto);
        openPhoto.setOnClickListener(this);
        language = findViewById(R.id.language);
        firstLang = findViewById(R.id.firstLang);
        firstLang.setAdapter(getSpinnerAdapter());
        firstLang.setSelection(3);
        firstLang.setClickable(false);
        secondLang = findViewById(R.id.secondLang);
        secondLang.setAdapter(getSpinnerAdapter());
        secondLang.setSelection(64);
        secondLang.setOnItemSelectedListener(this);

        presenter = new TextRecognizerPresenter(this);

        startCameraSource();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != requestPermissionID) {
            Log.d(TAG, "Got unexpected permission result: " + requestCode);
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            try {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                cameraSource.start(cameraView.getHolder());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void startCameraSource() {

        final TextRecognizer textRecognizer = new TextRecognizer.Builder(MyApplication.getAppContext()).build();

        if (!textRecognizer.isOperational()) {
            Log.w(TAG, "Detector dependencies not loaded yet");
        } else {

            //Initialize camerasource to use high resolution and set Autofocus on.
            cameraSource = new CameraSource.Builder(getApplicationContext(), textRecognizer)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setRequestedPreviewSize(1280, 1024)
                    .setAutoFocusEnabled(true)
                    .setRequestedFps(2.0f)
                    .build();

            cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                    try {

                        if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                            ActivityCompat.requestPermissions(TextRecognizerActivity.this,
                                    new String[]{Manifest.permission.CAMERA},
                                    requestPermissionID);
                            return;
                        }
                        cameraSource.start(cameraView.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                }

                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {
                    cameraSource.stop();
                }
            });

            //Set the TextRecognizer's Processor.
            textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {
                @Override
                public void release() {
                }

                @Override
                public void receiveDetections(Detector.Detections<TextBlock> detections) {
                    final SparseArray<TextBlock> items = detections.getDetectedItems();
                    if (items.size() != 0) {

                        translatedText.post(new Runnable() {
                            @Override
                            public void run() {
                                StringBuilder stringBuilder = new StringBuilder();
                                for (int i = 0; i < items.size(); i++) {
                                    item = items.valueAt(i);
                                    stringBuilder.append(item.getValue());
                                    stringBuilder.append("\n");
                                }
                                translatedText.setText(stringBuilder.toString());
                            }
                        });
                    }
                }
            });
        }
    }

    @Override
    public void showTranslate() {
        translatedText.setVisibility(View.VISIBLE);
        language.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideTranslate() {
        translatedText.setVisibility(View.INVISIBLE);
        language.setVisibility(View.INVISIBLE);
    }

    @Override
    public void takePhoto() {

    }

    @Override
    public void openPhoto() {

    }

    @Override
    public String getFirstLang() {
        String identifier = String.valueOf(item.getLanguage());
        int stringId = getResources().getIdentifier(identifier, "string", MyApplication.getAppContext().getPackageName());
        return getString(stringId);
    }

    @Override
    public void setFirstLang(int position) {
        firstLang.setSelection(position);
    }

    @Override
    public String getSecondLang() {
        String identifier = String.valueOf(secondLang.getSelectedItem());
        int stringId = getResources().getIdentifier(identifier, "string", MyApplication.getAppContext().getPackageName());
        return getString(stringId);
    }

    @Override
    public void showNoConnection() {
        Toast.makeText(MyApplication.getAppContext(), "Отсутствует интернет соединение", Toast.LENGTH_LONG).show();
    }

    @Override
    public void showError(Throwable t) {
        Toast.makeText(MyApplication.getAppContext(), "Ошибка сервера " + t.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        presenter.onLangWasSelected();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private ArrayAdapter<String> getSpinnerAdapter() {
        String[] languages = getResources().getStringArray(R.array.languages);
        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, languages);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        return adapter;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.translation:
                presenter.onTranslationButtonWasClicked();
                break;
            case R.id.takePhoto:
                presenter.onTakePhotoButtonWasClicked();
                break;
            case R.id.openPhoto:
                presenter.onOpenPhotoButtonWasClicked();
                break;
        }
    }
}
