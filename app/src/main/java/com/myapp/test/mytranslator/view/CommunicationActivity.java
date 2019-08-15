package com.myapp.test.mytranslator.view;

import android.app.Activity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.myapp.test.mytranslator.R;
import com.myapp.test.mytranslator.contracts.CommunicationTextContract;
import com.myapp.test.mytranslator.myAppContext.MyApplication;
import com.myapp.test.mytranslator.presenter.CommunicationPresenter;

import java.util.Locale;

public class CommunicationActivity extends Activity implements CommunicationTextContract.View, View.OnClickListener {
    CommunicationTextContract.Presenter presenter;
    private EditText firstText;
    private EditText secondText;
    private Button firstMic;
    private Button secondMic;
    private Button speaker;
    private Spinner firstLang;
    private Spinner secondLang;
    private TextToSpeech textToSpeech;
    private Locale locale;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_communication);

        presenter = new CommunicationPresenter(this);

        firstMic = findViewById(R.id.firstMic);
        firstMic.setOnClickListener(this);
        secondMic = findViewById(R.id.secondMic);
        secondMic.setOnClickListener(this);
        speaker = findViewById(R.id.speaker);
        speaker.setOnClickListener(this);
        firstLang = findViewById(R.id.firstLang);
        firstLang.setAdapter(getSpinnerAdapter());
        firstLang.setSelection(4);
        secondLang = findViewById(R.id.secondLang);
        secondLang.setAdapter(getSpinnerAdapter());
        secondLang.setSelection(64);
        firstText = findViewById(R.id.firstText);
        secondText = findViewById(R.id.secondText);
    }
    @Override
    public String getFirstText() {
        return firstText.getText().toString();
    }

    @Override
    public String getSecondText() {
        return secondText.getText().toString();
    }

    @Override
    public void setFirstText(String resultText) {
        firstText.setText(resultText);
    }

    @Override
    public void setSecondText(String resultText) {
        secondText.setText(resultText);
    }

    @Override
    public void playFirstText() {
        textToSpeech = new TextToSpeech(MyApplication.getAppContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    locale = new Locale(getFirstLang());
                    int result = textToSpeech.setLanguage(locale);
                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Toast.makeText(MyApplication.getAppContext(), "Извините, этот язык не поддерживается", Toast.LENGTH_LONG).show();
                    }else {
                        textToSpeech.speak(firstText.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
                    }
                } else {
                    Toast.makeText(MyApplication.getAppContext(), "Ошибка воспроизведения", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void playSecondText() {
        textToSpeech = new TextToSpeech(MyApplication.getAppContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    locale = new Locale(getFirstLang());
                    int result = textToSpeech.setLanguage(locale);
                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Toast.makeText(MyApplication.getAppContext(), "Извините, этот язык не поддерживается", Toast.LENGTH_LONG).show();
                    }else {
                        textToSpeech.speak(secondText.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
                    }
                } else {
                    Toast.makeText(MyApplication.getAppContext(), "Ошибка воспроизведения", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public String getFirstLang() {
        String identifier = String.valueOf(firstLang.getSelectedItem());
        int stringId = getResources().getIdentifier(identifier, "string", MyApplication.getAppContext().getPackageName());
        return getString(stringId);
    }

    @Override
    public String getSecondLang() {
        String identifier = String.valueOf(secondLang.getSelectedItem());
        int stringId = getResources().getIdentifier(identifier, "string", MyApplication.getAppContext().getPackageName());
        return getString(stringId);
    }

    private ArrayAdapter<String> getSpinnerAdapter(){
        String[] languages = getResources().getStringArray(R.array.languages);
        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, languages);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        return adapter;
    }

    @Override
    public void onClick(View view) {

    }
}
