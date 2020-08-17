package com.example.temidemoforfebruary_19;

import android.util.Log;

import org.jetbrains.annotations.NotNull;


public class FaceFeatures {
    /*
    *
    *
    * These arrays contain the hardcoded names of the relevant files in the drawables folder.
    * Pros: the different facial features don't need to be initialized, so less heap memory is used
    * Cons: cannot add new features unless you have the source code; relies on .png file format
    *
    */
    static int[] leftEyes = { R.drawable.simple_eyes4, R.drawable.simple_eyes3,R.drawable.simple_eyes2,R.drawable.realistic_lefteye0};
    static int[] rightEyes = { R.drawable.simple_eyes4, R.drawable.simple_eyes3,R.drawable.simple_eyes2,R.drawable.realistic_righteye0};
    static int[] mouths = { R.drawable.mouth_placeholder};
    static int[] noses = { R.drawable.nose_placeholder};

    //enum for naming face features
    public enum F{left, right, mouth, nose}

    //USAGE: requires that the arrays above contain at least one element
    static int getFeature(@NotNull F f, int i){
        switch(f){
            case left:
                return leftEyes[i%leftEyes.length];
            case nose:
                return noses[i%noses.length];
            case mouth:
                return mouths[i%mouths.length];
            case right:
                return rightEyes[i%rightEyes.length];
        }

        return rightEyes[0]; //garbage value
    }


}
