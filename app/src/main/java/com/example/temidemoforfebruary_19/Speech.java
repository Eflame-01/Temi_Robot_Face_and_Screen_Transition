package com.example.temidemoforfebruary_19;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.util.Log;
import android.widget.Toast;

import java.util.Locale;

public class Speech {
    /**Speech is a class that is supposed to utilize
     * speech into the robot. However, it currently
     * uses the TTS feature on android (which is GARBAGE)
     * and not the built in temi one. This, along with
     * syncing the mouth to the speech, is something you
     * would have to do, which is why I haven't used this
     * code.
     */

    private TextToSpeech textToSpeech;
    private Context context;

    public Speech(Context context){
        this.context = context;

        textToSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                Voice voice = new Voice(
                        "it-it-x-kda#male_2-local",
                        Locale.getDefault(),
                        Voice.QUALITY_HIGH,
                        Voice.LATENCY_HIGH,
                        false,
                        null
                );

                if (status == TextToSpeech.SUCCESS) {
                    int ttsLang = textToSpeech.setLanguage(Locale.US);

                    if (ttsLang == TextToSpeech.LANG_MISSING_DATA
                            || ttsLang == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "The Language is not supported!");
                    } else {
                        Log.i("TTS", "Language Supported.");
                    }
                    Log.i("TTS", "Initialization success.");
                } else {
                    Toast.makeText(context, "TTS Initialization failed!", Toast.LENGTH_SHORT).show();
                }

                textToSpeech.setVoice(voice);
            }
        });
    }

    public void speak(String data){
        int speechStatus = textToSpeech.speak(data, TextToSpeech.QUEUE_FLUSH, null, null);

        if (speechStatus == TextToSpeech.ERROR) {
            Log.e("TTS", "Error in converting Text to Speech!");
        }
    }
}
