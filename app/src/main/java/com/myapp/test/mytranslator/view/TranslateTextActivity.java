package com.myapp.test.mytranslator.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.myapp.test.mytranslator.R;
import com.myapp.test.mytranslator.contracts.TranslateTextContract;
import com.myapp.test.mytranslator.myAppContext.MyApplication;
import com.myapp.test.mytranslator.presenter.TranslateTextPresenter;

import java.util.ArrayList;
import java.util.Locale;

public class TranslateTextActivity extends Activity implements TranslateTextContract.View, View.OnClickListener, TextToSpeech.OnInitListener {
    private static final int RECOGNIZER_REQUEST_CODE = 1;
    private EditText userText;
    private TextView resultText;
    private Spinner firstLang;
    private Spinner secondLang;
    private Button swapLang;
    private Button playUserText;
    private Button deleteUserText;
    private Button playResultText;
    private Button recorder;
    private TextToSpeech textToSpeech;
    private TranslateTextContract.Presenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate_text);

        presenter = new TranslateTextPresenter(this);


        firstLang = findViewById(R.id.firstLang);
        firstLang.setAdapter(getSpinnerAdapter());
        firstLang.setSelection(4);
        secondLang = findViewById(R.id.secondLang);
        secondLang.setAdapter(getSpinnerAdapter());
        secondLang.setSelection(64);
        textToSpeech = new TextToSpeech(MyApplication.getAppContext(), this);
        resultText = findViewById(R.id.resultText);
        userText = findViewById(R.id.userText);
        userText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                presenter.onTextWasChanged();

            }
        });
        recorder = findViewById(R.id.recorder);
        recorder.setOnClickListener(this);
        swapLang = findViewById(R.id.swapLang);
        swapLang.setOnClickListener(this);
        playUserText = findViewById(R.id.playUserText);
        playUserText.setOnClickListener(this);
        playResultText = findViewById(R.id.playResultText);
        playResultText.setOnClickListener(this);
        deleteUserText = findViewById(R.id.deleteUserText);
        deleteUserText.setOnClickListener(this);
    }

    @Override
    public String getText() {
        return userText.getText().toString();
    }

    @Override
    public void deleteUserText() {
        userText.setText("");
        resultText.setText("");
    }

    @Override
    public void setText(String resultText) {
        this.resultText.setText(resultText);
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
    public void voiceInputText() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "en-US");
        intent.putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE, "en-US");
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");
        startActivityForResult(intent, RECOGNIZER_REQUEST_CODE); // вызываем акт
    }

    @Override
    public void playUserText() {
        textToSpeech.speak(userText.getText().toString(), TextToSpeech.QUEUE_FLUSH, null, "DEFAULT");
    }

    @Override
    public void playResultText() {
        textToSpeech.speak(resultText.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    public void showButtons() {
        playUserText.setVisibility(View.VISIBLE);
        deleteUserText.setVisibility(View.VISIBLE);
        playResultText.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideButtons() {
        playUserText.setVisibility(View.INVISIBLE);
        deleteUserText.setVisibility(View.INVISIBLE);
        playResultText.setVisibility(View.INVISIBLE);
    }

    @Override
    public void swapLanguages() {
        int swap = firstLang.getSelectedItemPosition();
        firstLang.setSelection(secondLang.getSelectedItemPosition());
        secondLang.setSelection(swap);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            ArrayList<String> voiceResults = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (!voiceResults.isEmpty()) {
                userText.setText(voiceResults.get(0));
            }
        } else {
            Toast.makeText(getApplicationContext(), "Ошибка записи", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.recorder:
                presenter.onButtonRecorderWasClicked();
                break;
            case R.id.swapLang:
                presenter.onSwapLangButtonWasClicked();
                break;
            case R.id.playUserText:
                presenter.onPlayUserTextwasClicked();
                break;
            case R.id.playResultText:
                presenter.onPlayResultTextWasClicked();
                break;
            case R.id.deleteUserText:
                presenter.onDeleteTextButtonWasClicked();
                break;
        }
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {

            Locale locale = new Locale("en");

            int result = textToSpeech.setLanguage(locale);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(MyApplication.getAppContext(), "Извините, этот язык не поддерживается", Toast.LENGTH_LONG).show();

            }
        } else {
            Toast.makeText(MyApplication.getAppContext(), "Ошибка воспроизведения", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }

    private ArrayAdapter<String> getSpinnerAdapter(){
        String[] languages = getResources().getStringArray(R.array.languages);
        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, languages);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        return adapter;
    }
}
