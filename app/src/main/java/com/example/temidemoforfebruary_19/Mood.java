package com.example.temidemoforfebruary_19;

public enum Mood {
    /**Mood is an Enum that will hold the values of
     * the facial feature images depending on the emotion.
     *                                              -Ese
     */
    //Instances of the Mood Enum
    NEUTRAL(R.drawable.base_eyebrow, R.drawable.base_eyebrow,
            R.drawable.normal_eye0000, R.drawable.normal_eye0000,
            R.drawable.normal_mouth_0000),

    HAPPY(R.drawable.raise_eyebrow_0003, R.drawable.raise_eyebrow_0003,
            R.drawable.normal_eye0000, R.drawable.normal_eye0000,
            R.drawable.smile_mouth_0003),

    SURPRISE(R.drawable.rise_eyebrow_l_0003, R.drawable.rise_eyebrow_r_0003,
            R.drawable.surprise_eye0000, R.drawable.surprise_eye0000,
            R.drawable.surprise_mouth_0003),

    ANGER(R.drawable.set_eyebrow_l_0003, R.drawable.set_eyebrow_r_0003,
            R.drawable.angry_tired_eye0000, R.drawable.angry_tired_eye0000,
            R.drawable.angry_tired_mouth_0003),

    SADNESS(R.drawable.lower_eyebrow_0003, R.drawable.lower_eyebrow_0003,
            R.drawable.sad_eye0000, R.drawable.sad_eye0000,
            R.drawable.sad_mouth_0003),

    AFRAID(R.drawable.rise_eyebrow_l_0005, R.drawable.rise_eyebrow_r_0005,
            R.drawable.surprise_eye0000, R.drawable.surprise_eye0000,
            R.drawable.afraid_mouth_0003),

    TIRED(R.drawable.lower_eyebrow_0003, R.drawable.lower_eyebrow_0003,
            R.drawable.angry_tired_eye0000 , R.drawable.angry_tired_eye0000,
            R.drawable.angry_tired_mouth_0003);

    //Data Members
    int leftEyebrowImageViewID;
    int rightEyebrowImageViewID;
    int leftEyeImageViewID;
    int rightEyeImageViewID;
    int mouthImageViewID;

    //Constructor
    Mood(int leftEyebrowImageViewID, int rightEyebrowImageViewID,
         int leftEyeImageViewID, int rightEyeImageViewID,
         int mouthImageViewID){

        this.leftEyebrowImageViewID = leftEyebrowImageViewID;
        this.rightEyebrowImageViewID = rightEyebrowImageViewID;
        this.leftEyeImageViewID = leftEyeImageViewID;
        this.rightEyeImageViewID = rightEyeImageViewID;
        this.mouthImageViewID = mouthImageViewID;
    }
}//End of Mood{}...