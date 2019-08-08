package com.myapp.test.mytranslator.contracts;

public interface MainContract {
    interface View{
        void goToTranslateTextActivity();
        void goToTranslateCameraTextActivity();
        void goToTranslateVoiceTextActivity();
    }

    interface MainPresenter{
        void onButtonTranslateTextWasClicked();
        void onButtonTranslateCameraTextWasClicked();
        void onButtonTranslateVoiceTextWasClicked();
    }

}
