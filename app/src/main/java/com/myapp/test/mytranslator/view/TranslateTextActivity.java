package com.myapp.test.mytranslator.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.myapp.test.mytranslator.R;
import com.myapp.test.mytranslator.contracts.TranslateTextContract;
import com.myapp.test.mytranslator.myAppContext.MyApplication;
import com.myapp.test.mytranslator.presenter.TranslateTextPresenter;

public class TranslateTextActivity extends Activity implements TranslateTextContract.View {
    private EditText userText;
    private TextView resultText;
    private Button swapLang;
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
}
