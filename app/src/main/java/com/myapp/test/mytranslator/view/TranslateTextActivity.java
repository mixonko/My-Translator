package com.myapp.test.mytranslator.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.myapp.test.mytranslator.R;
import com.myapp.test.mytranslator.contracts.TranslateTextContract;
import com.myapp.test.mytranslator.myAppContext.MyApplication;
import com.myapp.test.mytranslator.presenter.TranslateTextPresenter;

import java.util.ArrayList;

public class TranslateTextActivity extends Activity implements TranslateTextContract.View {
    private static final int RECOGNIZER_REQUEST_CODE = 1;
    private EditText userText;
    private TextView resultText;
    private Button swapLang;
    private Button recorder;
    private TranslateTextContract.Presenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate_text);

        presenter = new TranslateTextPresenter(this);

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
        recorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onButtonRecorderWasClicked();
            }
        });
        swapLang = findViewById(R.id.swapLang);
        swapLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onSwapLangButtonWasClicked();
            }
        });
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
    public void recordVoice() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "en-US");
        intent.putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE, "en-US");
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");
        startActivityForResult(intent, RECOGNIZER_REQUEST_CODE); // вызываем акт
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
}
