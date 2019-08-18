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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.myapp.test.mytranslator.R;
import com.myapp.test.mytranslator.contracts.CommunicationTextContract;
import com.myapp.test.mytranslator.myAppContext.MyApplication;
import com.myapp.test.mytranslator.presenter.CommunicationPresenter;

import java.util.ArrayList;
import java.util.Locale;

public class CommunicationActivity extends Activity implements CommunicationTextContract.View, View.OnClickListener {
    private static final int FIRST_RECOGNIZER_REQUEST_CODE = 1;
    private static final int SECOND_RECOGNIZER_REQUEST_CODE = 2;
    private CommunicationTextContract.Presenter presenter;
    private EditText firstEditText;
    private EditText secondEditText;
    private TextView firstTextView;
    private TextView secondTextView;
    private Button firstMic;
    private Button secondMic;
    private Button playFirstText;
    private Button playSecondText;
    private Button deleteFirstText;
    private Button deleteSecondText;
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
        firstLang = findViewById(R.id.firstLang);
        firstLang.setAdapter(getSpinnerAdapter());
        firstLang.setSelection(3);
        secondLang = findViewById(R.id.secondLang);
        secondLang.setAdapter(getSpinnerAdapter());
        secondLang.setSelection(64);
        firstEditText = findViewById(R.id.firstEditText);
        firstEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(firstEditText.getText().toString().length() != 0) presenter.onFirstEditTextWasChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(firstEditText.getText().toString().length() == 0) presenter.firstEditTextIsEmpty();
            }
        });
        firstTextView = findViewById(R.id.firstTextView);
        secondEditText = findViewById(R.id.secondEditText);
        secondTextView = findViewById(R.id.secondTextView);
        secondEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(firstEditText.getText().toString().length() != 0) presenter.onSecondEditTextWasChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(firstEditText.getText().toString().length() == 0) presenter.secondEditTextIsEmpty();
            }
        });
        playFirstText = findViewById(R.id.playFirstText);
        playFirstText.setOnClickListener(this);
        playSecondText = findViewById(R.id.playSecondText);
        playSecondText.setOnClickListener(this);
        deleteFirstText = findViewById(R.id.deleteFirstText);
        deleteFirstText.setOnClickListener(this);
        deleteSecondText = findViewById(R.id.deleteSecondText);
        deleteSecondText.setOnClickListener(this);
    }

    @Override
    public String getFirstText() {
        return firstEditText.getText().toString();
    }

    @Override
    public String getSecondText() {
        return secondEditText.getText().toString();
    }

    @Override
    public void setFirstTextView(String resultText) {
        firstTextView.setText(resultText);
    }

    @Override
    public void setSecondTextView(String resultText) {
        secondTextView.setText(resultText);
    }

    @Override
    public void playFirstTextView(final String lang) {
        textToSpeech = new TextToSpeech(MyApplication.getAppContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    locale = new Locale(lang);
                    int result = textToSpeech.setLanguage(locale);
                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Toast.makeText(MyApplication.getAppContext(), "Извините, этот язык не поддерживается", Toast.LENGTH_LONG).show();
                    } else {
                        textToSpeech.speak(firstTextView.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
                    }
                } else {
                    Toast.makeText(MyApplication.getAppContext(), "Ошибка воспроизведения", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void playSecondTextView(final String lang) {
        textToSpeech = new TextToSpeech(MyApplication.getAppContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    locale = new Locale(lang);
                    int result = textToSpeech.setLanguage(locale);
                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Toast.makeText(MyApplication.getAppContext(), "Извините, этот язык не поддерживается", Toast.LENGTH_LONG).show();
                    } else {
                        textToSpeech.speak(secondTextView.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
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

    @Override
    public void showNoConnection() {
        Toast.makeText(MyApplication.getAppContext(), "Отсутствует интернет соединение", Toast.LENGTH_LONG).show();
    }

    @Override
    public void showError(Throwable t) {
        Toast.makeText(MyApplication.getAppContext(), "Ошибка сервера " + t.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void voiceInputFirstText() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "en-US");
        intent.putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE, "en-US");
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");
        startActivityForResult(intent, FIRST_RECOGNIZER_REQUEST_CODE);
    }

    @Override
    public void voiceInputSecondText() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "ru");
        intent.putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE, "ru");
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ru");
        startActivityForResult(intent, SECOND_RECOGNIZER_REQUEST_CODE);
    }

    @Override
    public void showFirstTextGroup() {
        firstEditText.setVisibility(View.VISIBLE);
        secondTextView.setVisibility(View.VISIBLE);
        deleteFirstText.setVisibility(View.VISIBLE);
        playSecondText.setVisibility(View.VISIBLE);
    }

    @Override
    public void showSecondTextGroup() {
        secondEditText.setVisibility(View.VISIBLE);
        firstTextView.setVisibility(View.VISIBLE);
        deleteSecondText.setVisibility(View.VISIBLE);
        playFirstText.setVisibility(View.VISIBLE);
    }

    @Override
    public void deleteAllText() {
        firstEditText.setText(" ");
        firstTextView.setText(" ");
        secondEditText.setText(" ");
        secondTextView.setText(" ");
    }

    @Override
    public void hideAllText() {
        firstEditText.setVisibility(View.INVISIBLE);
        firstTextView.setVisibility(View.INVISIBLE);
        secondEditText.setVisibility(View.INVISIBLE);
        secondTextView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void hideButtons() {
        deleteFirstText.setVisibility(View.INVISIBLE);
        deleteSecondText.setVisibility(View.INVISIBLE);
        playFirstText.setVisibility(View.INVISIBLE);
        playSecondText.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            ArrayList<String> voiceResults = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            switch (requestCode) {
                case FIRST_RECOGNIZER_REQUEST_CODE:
                    firstEditText.setText(voiceResults.get(0));
                    presenter.firstTextWasInserted();
                    break;
                case SECOND_RECOGNIZER_REQUEST_CODE:
                    secondEditText.setText(voiceResults.get(0));
                    presenter.secondTextWasInserted();
                    break;
            }
        } else {
            Toast.makeText(getApplicationContext(), "Ошибка записи", Toast.LENGTH_LONG).show();
        }
    }

    private ArrayAdapter<String> getSpinnerAdapter() {
        String[] languages = getResources().getStringArray(R.array.languages);
        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, languages);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        return adapter;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.firstMic:
                presenter.onFirstMicButtonWasClicked();
                break;
            case R.id.secondMic:
                presenter.onSecondMicButtonWasClicked();
                break;
            case R.id.playFirstText:
                presenter.onPlayFirstButTextWasClicked();
                break;
            case R.id.playSecondText:
                presenter.onPlaySecondTextButWasClicked();
                break;
            case R.id.deleteFirstText:
                presenter.onDeleteFirstTextButWasClicked();
                break;
            case R.id.deleteSecondText:
                presenter.onDeleteSecondTextButWasClicked();
                break;
        }
    }

    @Override
    public void onStop() {
        stopTextToSpeech();
        super.onStop();
    }

    @Override
    public void stopTextToSpeech(){
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }
}
