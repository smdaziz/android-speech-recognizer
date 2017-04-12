package com.example.speechrecognizer;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.cloud.translate.Detection;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.common.collect.ImmutableList;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView speechResult;
    private TextView languageResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onButtonClick(View view) {
        if(view.getId() == R.id.micButton) {
            speechResult = (TextView) findViewById(R.id.speechText);
            languageResult = (TextView) findViewById(R.id.languageText);
            promptSpeechInput();
        }
    }

    public void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
//        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say Something!");

        try {
            startActivityForResult(intent, 100);
        } catch (ActivityNotFoundException e) {
            Toast toast = Toast.makeText(MainActivity.this, "Sorry, you device doesn't support speech language", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        switch (requestCode) {
            case 100:
                if(resultCode == RESULT_OK && intent != null) {
                    List<String> speechResult = intent.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    this.speechResult.setText(speechResult.get(0));
//                    LanguageIdentifier languageIdentifier = new LanguageIdentifier(speechResult.get(0));
//                    this.languageResult.setText(languageIdentifier.getLanguage());
                    Translate translate = TranslateOptions.newBuilder().build().getService();
                    String detectedLanguages = "";
                    List<Detection> detections = translate.detect(ImmutableList.of(speechResult.get(0)));
                    for(Detection detection : detections) {
                        detectedLanguages += detection.getLanguage() + " - ";
                    }
                    this.languageResult.setText(detectedLanguages);
                }
//              break;
        }
    }

}
