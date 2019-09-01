package com.myapp.test.mytranslator.view;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.guna.ocrlibrary.OCRCapture;
import com.myapp.test.mytranslator.R;
import com.myapp.test.mytranslator.contracts.TranslateTextContract;
import com.myapp.test.mytranslator.myAppContext.MyApplication;
import com.myapp.test.mytranslator.presenter.TranslateTextPresenter;
import com.myapp.test.mytranslator.utils.Permissions;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import static com.guna.ocrlibrary.OcrCaptureActivity.TextBlockObject;

public class TranslateTextActivity extends AppCompatActivity implements TranslateTextContract.View, View.OnClickListener, AdapterView.OnItemSelectedListener {
    private final int VOICE_REQUEST_CODE = 1;
    private final int CAMERA_SCAN_REQUEST_CODE = 2;
    private final int LOAD_IMAGE_REQUEST_CODE = 3;
    private EditText userText;
    private TextView resultText;
    private TextView firstLangText;
    private TextView secondLangText;
    private Spinner firstLang;
    private Spinner secondLang;
    private Button swapLang;
    private Button playUserText;
    private Button deleteUserText;
    private Button playResultText;
    private Button copyResultText;
    private ImageView voiceInput;
    private ImageView communication;
    private TextToSpeech textToSpeech;
    private Locale locale;
    private TranslateTextContract.Presenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate_text);

        ActionBar actionBar = getSupportActionBar(); 
        actionBar.setTitle(R.string.translator);

        presenter = new TranslateTextPresenter(this);

        firstLangText = findViewById(R.id.firstLangText);
        secondLangText = findViewById(R.id.secondLangText);
        firstLang = findViewById(R.id.firstLang);
        firstLang.setAdapter(getSpinnerAdapter());
        firstLang.setSelection(3);
        firstLang.setOnItemSelectedListener(this);
        secondLang = findViewById(R.id.secondLang);
        secondLang.setAdapter(getSpinnerAdapter());
        secondLang.setSelection(64);
        secondLang.setOnItemSelectedListener(this);
        resultText = findViewById(R.id.resultText);
        userText = findViewById(R.id.userText);
        userText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                presenter.onTextWasChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (userText.getText().toString().length() == 0) presenter.userTextIsEmpty();
            }
        });
        voiceInput = findViewById(R.id.recorder);
        voiceInput.setOnClickListener(this);
        swapLang = findViewById(R.id.swapLang);
        swapLang.setOnClickListener(this);
        playUserText = findViewById(R.id.playUserText);
        playUserText.setOnClickListener(this);
        playResultText = findViewById(R.id.playResultText);
        playResultText.setOnClickListener(this);
        deleteUserText = findViewById(R.id.deleteUserText);
        deleteUserText.setOnClickListener(this);
        copyResultText = findViewById(R.id.copyResultText);
        copyResultText.setOnClickListener(this);
        communication = findViewById(R.id.communication);
        communication.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionCamera:
                presenter.onCameraButtonWasClicked();
                break;
            case R.id.actionPhoto:
                if (Permissions.checkStoragePermission(MyApplication.getAppContext())) {
                    presenter.onImageButtonWasClicked();
                } else {
                    getPermission();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public String getText() {
        return userText.getText().toString();
    }

    @Override
    public void deleteAllText() {
        userText.setText("");
        resultText.setText("");
    }

    @Override
    public void setText(String resultText) {
        if (userText.getText().toString().length() != 0) this.resultText.setText(resultText);
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
    public void voiceInputText(String lang) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, lang);
        intent.putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE, lang);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, lang);
        startActivityForResult(intent, VOICE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            switch (requestCode) {
                case VOICE_REQUEST_CODE:
                    if (resultCode == RESULT_OK) {
                        ArrayList<String> voiceResults = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        if (!voiceResults.isEmpty()) {
                            userText.setText(voiceResults.get(0));
                        } else {
                            Toast.makeText(getApplicationContext(), "Ошибка записи", Toast.LENGTH_LONG).show();
                        }
                    }
                    break;
                case CAMERA_SCAN_REQUEST_CODE:
                        userText.setText(data.getStringExtra(TextBlockObject));
                    break;
                case LOAD_IMAGE_REQUEST_CODE:
                    if (resultCode == RESULT_OK) {
                        Uri pickedImage = data.getData();
                        CropImage.activity(pickedImage)
                                .start(this);
                    }
                    break;
                case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    if (resultCode == RESULT_OK) {
                        Uri resultUri = result.getUri();
                        Bitmap bitmap = null;
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        String cropText = OCRCapture.Builder(this).getTextFromBitmap(bitmap);
                        userText.setText(String.valueOf(cropText));
                    } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                        Exception error = result.getError();
                        Toast.makeText(MyApplication.getAppContext(), "Ошибка; " + error, Toast.LENGTH_LONG).show();
                    }
                    break;

            }
        }
    }

    @Override
    public void playUserText() {
        textToSpeech = new TextToSpeech(MyApplication.getAppContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    locale = new Locale(getFirstLang());
                    int result = textToSpeech.setLanguage(locale);
                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Toast.makeText(MyApplication.getAppContext(), "Извините, этот язык не поддерживается", Toast.LENGTH_LONG).show();
                    } else {
                        textToSpeech.speak(userText.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
                    }
                } else {
                    Toast.makeText(MyApplication.getAppContext(), "Ошибка воспроизведения", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void playResultText() {
        textToSpeech = new TextToSpeech(MyApplication.getAppContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    locale = new Locale(getSecondLang());
                    int result = textToSpeech.setLanguage(locale);
                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Toast.makeText(MyApplication.getAppContext(), "Извините, этот язык не поддерживается", Toast.LENGTH_LONG).show();
                    } else {
                        textToSpeech.speak(resultText.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
                    }
                } else {
                    Toast.makeText(MyApplication.getAppContext(), "Ошибка воспроизведения", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void showButtons() {
        playUserText.setVisibility(View.VISIBLE);
        deleteUserText.setVisibility(View.VISIBLE);
        playResultText.setVisibility(View.VISIBLE);
        copyResultText.setVisibility(View.VISIBLE);
        firstLangText.setVisibility(View.VISIBLE);
        secondLangText.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideButtons() {
        playUserText.setVisibility(View.INVISIBLE);
        deleteUserText.setVisibility(View.INVISIBLE);
        playResultText.setVisibility(View.INVISIBLE);
        copyResultText.setVisibility(View.INVISIBLE);
        firstLangText.setVisibility(View.INVISIBLE);
        secondLangText.setVisibility(View.INVISIBLE);
    }

    @Override
    public void swapLanguages() {
        int swapLang = firstLang.getSelectedItemPosition();
        firstLang.setSelection(secondLang.getSelectedItemPosition());
        secondLang.setSelection(swapLang);
        userText.setText(resultText.getText().toString());
    }

    @Override
    public void copyResultText() {
        ClipboardManager clipboard = (ClipboardManager) MyApplication.getAppContext()
                .getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("", resultText.getText().toString());
        clipboard.setPrimaryClip(clip);
        Toast.makeText(MyApplication.getAppContext(), "Текст скопирован", Toast.LENGTH_LONG).show();
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
    public void startCommunicationActivity() {
        startActivity(new Intent(MyApplication.getAppContext(), CommunicationActivity.class));
    }

    @Override
    public void setTextViewLanguage() {
        firstLangText.setText(String.valueOf(firstLang.getSelectedItem()));
        secondLangText.setText(String.valueOf(secondLang.getSelectedItem()));
    }

    @Override
    public void deleteResultText() {
        resultText.setText("");
    }

    @Override
    public void stopTextToSpeech() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }

    public void getPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
    }

    @Override
    public void pickImage() {
        if (Permissions.checkStoragePermission(MyApplication.getAppContext())) {
            Intent intentGallery = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intentGallery, LOAD_IMAGE_REQUEST_CODE);
        } else getPermission();
    }

    @Override
    public void startScanCamera() {
        OCRCapture.Builder(this)
                .setUseFlash(false)
                .setAutoFocus(true)
                .buildWithRequestCode(CAMERA_SCAN_REQUEST_CODE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.recorder:
                presenter.onVoiceInputButtonWasClicked();
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
            case R.id.copyResultText:
                presenter.onCopyButtonWasClicked();
                break;
            case R.id.communication:
                presenter.onCommunicationButtonWasClicked();
                break;
        }
    }

    @Override
    public void onStop() {
        presenter.onStopActivity();
        super.onStop();
    }

    private ArrayAdapter<String> getSpinnerAdapter() {
        String[] languages = getResources().getStringArray(R.array.languages);
        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, languages);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        return adapter;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        presenter.onLangWasSelected();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

}
