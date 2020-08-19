package com.example.d_fir_login;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

import java.util.ArrayList;
import java.util.Locale;

public class SpeechActivity extends AppCompatActivity {

    private EditText editText;
    private ImageView speech, translate;
    private Button settings;
    private TextView textView;
    private ArrayList<String> matches;
    private static final int RECOGNIZER_RESULT = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech);

        editText = findViewById(R.id.editText);
        speech = findViewById(R.id.speech);
        settings = findViewById(R.id.settings);
        translate = findViewById(R.id.translate);

        translate.setVisibility(View.GONE);

        if (Locale.getDefault().toString().equals("hi_IN")) {
            View b = findViewById(R.id.settings);
            b.setVisibility(View.INVISIBLE);
        }
        else
            settings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(android.provider.Settings.ACTION_LOCALE_SETTINGS);
                    //intent.setComponent(new ComponentName("com.android.settings", "com.android.settings.Settings$InputMethodAndLanguageSettingsActivity"));
                    startActivity(intent);
                }
            });

        speech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                translate.setVisibility(View.VISIBLE);
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                startActivityForResult(intent, RECOGNIZER_RESULT);
            }
        });

        translate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TranslatorOptions options = new TranslatorOptions.Builder().setSourceLanguage(TranslateLanguage.HINDI).setTargetLanguage(TranslateLanguage.ENGLISH).build();
                final Translator hindiEnglishTranslator = Translation.getClient(options);

                DownloadConditions conditions = new DownloadConditions.Builder().build();
                hindiEnglishTranslator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SpeechActivity.this, "Couldn't download the model", Toast.LENGTH_SHORT).show();
                        return;
                    }
                });

                hindiEnglishTranslator.translate(matches.get(0)).addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        editText.setText(s);
                        Toast.makeText(SpeechActivity.this, "Translation successful!", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SpeechActivity.this, "Downloading Hindi Model...", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RECOGNIZER_RESULT && resultCode == RESULT_OK) {
            matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            editText.setText(matches.get(0));
        }
    }
}