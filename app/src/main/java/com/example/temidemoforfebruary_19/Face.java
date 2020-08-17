package com.example.temidemoforfebruary_19;

import android.app.Activity;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.AnimationDrawable;
import android.util.Log;
import android.widget.ImageView;

import static com.example.temidemoforfebruary_19.TransitionDemo.handler;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Face {
     /**Face is a class that holds the information
     *   needed to display the facial features, colors,
     *   expressions, and other features that should/will
     *   be implemented later such as gaze aversion and
     *   speech.
     *                                        -Ese
     */

     //Data Members

    //keep track of imageViews, facialExpressionGraphs, AnimationDrawables, and resources
    private final int LEFT_EYEBROW = 0;
    private final int RIGHT_EYEBROW = 1;
    private final int LEFT_EYE = 2;
    private final int RIGHT_EYE = 3;
    private final int MOUTH = 4;

    //data structures for imageViews, facialExpressionGraphs, AnimationDrawables, and resources
    private FacialExpressionGraph[] facialExpressionGraph = {
            new FacialExpressionGraph().createLeftEyebrowExpressionGraph(),
            new FacialExpressionGraph().createRightEyebrowExpressionGraph(),
            new FacialExpressionGraph().createEyesExpressionGraph(),
            new FacialExpressionGraph().createEyesExpressionGraph(),
            new FacialExpressionGraph().createMouthExpressionGraph()
    };
    private AnimationDrawable[] facialAnimation = {
            new AnimationDrawable(),
            new AnimationDrawable(),
            new AnimationDrawable(),
            new AnimationDrawable(),
            new AnimationDrawable()
    };
    private int[] resource = {-1, -1, -1, -1, -1};
    private ImageView[] imageView = {null, null, null, null, null};

    //the activity the face class is instantiated from
    private Activity activity;

    //the mood the face will be in
    private Mood mood;

    //the timer that is responsible for blinking
    private Timer t;

    //the color of the facial features and the variables to set the color
    private int baseColor;
    private PorterDuffColorFilter porterDuffColorFilter;

    //The thread responsible for gazing and speaking
    private GazeThread gazeThread; //Thread that handles gazing.
    protected static boolean inGazeMode;

    //Constructors
    public Face(Activity activity){
        setActivity(activity);
        setMood(Mood.NEUTRAL);

        setAllResources();
        setAllImageViews();
        setAllFeatures();

        setBaseColor(this.activity.getResources().getColor(R.color.white));

        resetFace();

        //This is to make sure the imageViews are at the correct values (May not be needed)
        changeFaceTo(this.mood);
        changeColorTo(this.baseColor);

        //After setting up everything, simulate the "randomized" blinking
        startWaitingToBlink();
    }

    //Getters and setters for private data members that you'll most likely want to use outside the face class
    public Activity getActivity() {
        return activity;
    }
    public void setActivity(Activity activity){
        this.activity = activity;
    }
    public Mood getMood(){
        return mood;
    }
    public void setMood(Mood mood){
        this.mood = mood;
    }
    public int getBaseColor(){
        return baseColor;
    }
    private void setBaseColor(int color){
        this.baseColor = color;
        this.porterDuffColorFilter = new PorterDuffColorFilter(color, PorterDuff.Mode.MULTIPLY);
        resetColor();
    }
    public Timer getTimer(){
        return t;
    }
    public void setTimer(Timer timer){
        this.t = timer;
    }
    public GazeThread getGazeThread() {
        return gazeThread;
    }
    public void setGazeThread(GazeThread gazeThread) {
        this.gazeThread = gazeThread;
    }

    //Helper methods for the constructor
    private void setAllResources(){
        resource[LEFT_EYEBROW] = mood.leftEyebrowImageViewID;
        resource[RIGHT_EYEBROW] = mood.rightEyebrowImageViewID;
        resource[LEFT_EYE] = mood.leftEyeImageViewID;
        resource[RIGHT_EYE] = mood.rightEyeImageViewID;
        resource[MOUTH] = mood.mouthImageViewID;
    }
    private void setAllImageViews(){
        //I use the FaceFragment class from Michael's code to get the ImageViews from the Face Fragment View 'ffv'.
        try{
            imageView[LEFT_EYEBROW] = FaceFragment.ffv.findViewById(R.id.left_eyebrow);
            imageView[RIGHT_EYEBROW] = FaceFragment.ffv.findViewById(R.id.right_eyebrow);
            imageView[LEFT_EYE] = FaceFragment.ffv.findViewById(R.id.left_eye);
            imageView[RIGHT_EYE] = FaceFragment.ffv.findViewById(R.id.right_eye);
            imageView[MOUTH] = FaceFragment.ffv.findViewById(R.id.mouth);

            //After i set them to that I make sure to set their background to the face's resource, in order to display images on those ImageViews.
            for(int i = LEFT_EYEBROW; i <= MOUTH; i++){
                imageView[i].setBackground(this.activity.getDrawable(resource[i]));
            }
        }catch (Exception e){
            Log.e("Face", "setAllImageViews: " + e.toString());
        }
    }
    private void setAllFeatures(){
        gazeThread = new GazeThread();
    }

    /*setFace(Mood mood) is a method that will
    * get the mood the user wishes to change their expression to
    * along with the intensity of that mood, and animate the face
    */
    private void setFace(Mood mood){
        //check if face is already in that mood before animating it
        if(this.mood.equals(mood)){
            return;
        }

        //set background resource to the current mood so that you can change it later
        this.resetFace();

        //create the animation frames needed to animate each part of the face
        facialAnimation[LEFT_EYEBROW] = facialExpressionGraph[LEFT_EYEBROW].animateFacialFeature(this, mood, LEFT_EYEBROW);
        facialAnimation[RIGHT_EYEBROW] = facialExpressionGraph[RIGHT_EYEBROW].animateFacialFeature(this, mood, RIGHT_EYEBROW);
        facialAnimation[LEFT_EYE] = facialExpressionGraph[LEFT_EYE].animateFacialFeature(this, mood, LEFT_EYE);
        facialAnimation[RIGHT_EYE] = facialExpressionGraph[RIGHT_EYE].animateFacialFeature(this, mood, RIGHT_EYE);
        facialAnimation[MOUTH] = facialExpressionGraph[MOUTH].animateFacialFeature(this, mood, MOUTH);

        //set the background now to the animations for each face
        for(int i = LEFT_EYEBROW; i <= MOUTH; i++){
            imageView[i].setBackground(facialAnimation[i]);
        }

        //animate each facial feature
        for(AnimationDrawable animation : facialAnimation){
            animation.start();
        }

        //set the mood to the new expression
        this.setMood(mood);

        //create the color of the faces
        this.resetColor();
    }//End of changeFaceTo()...

    /*blink() is a method that animates the eyes
    * to make them blink.
    */
    private void blink(){
        try {
            imageView[LEFT_EYE].setBackgroundResource(resource[LEFT_EYE]);
            imageView[RIGHT_EYE].setBackgroundResource(resource[RIGHT_EYE]);

            if (this.mood.equals(Mood.NEUTRAL) || this.mood.equals(Mood.HAPPY)) {
                imageView[LEFT_EYE].setBackgroundResource(R.drawable.blink_normal);
                imageView[RIGHT_EYE].setBackgroundResource(R.drawable.blink_normal);
            } else if (this.mood.equals(Mood.AFRAID) || this.mood.equals(Mood.SURPRISE)) {
                imageView[LEFT_EYE].setBackgroundResource(R.drawable.blink_shocked);
                imageView[RIGHT_EYE].setBackgroundResource(R.drawable.blink_shocked);
            } else if (this.mood.equals(Mood.ANGER) || this.mood.equals(Mood.TIRED)) {
                imageView[LEFT_EYE].setBackgroundResource(R.drawable.blink_annoyed);
                imageView[RIGHT_EYE].setBackgroundResource(R.drawable.blink_annoyed);
            } else if (this.mood.equals((Mood.SADNESS))) {
                imageView[LEFT_EYE].setBackgroundResource(R.drawable.blink_sad);
                imageView[RIGHT_EYE].setBackgroundResource(R.drawable.blink_sad);
            }
        }catch (Exception e){
            Log.e("Face", "resetColor: " + e.toString() );
        }

        try{
            facialAnimation[LEFT_EYE] = (AnimationDrawable) imageView[LEFT_EYE].getBackground();
            facialAnimation[RIGHT_EYE] = (AnimationDrawable) imageView[RIGHT_EYE].getBackground();

            imageView[LEFT_EYE].setBackground(facialAnimation[LEFT_EYE]);
            imageView[RIGHT_EYE].setBackground(facialAnimation[RIGHT_EYE]);

            facialAnimation[LEFT_EYE].setOneShot(true);
            facialAnimation[RIGHT_EYE].setOneShot(true);

            facialAnimation[LEFT_EYE].setVisible(true, true);
            facialAnimation[RIGHT_EYE].setVisible(true, true);

            facialAnimation[LEFT_EYE].start();
            facialAnimation[RIGHT_EYE].start();
        }catch(Exception e){
            Log.i("Face ->", "blink: Face did not blink");
        }

        resetColor();
    }//End of blink()...

    /*startWaitingToBlink() simply puts the face
    * on a timer to periodically blink. The blinking
    * time will later be changed so that the rate of
    * blinks will vary.
    */
    private void startWaitingToBlink(){
        t = new Timer();
        t.schedule(new BlinkTask(), rateOfBlink());
    }

    /*rateOfBlink() randomly changes the rate of which the eyes
    * will blink. This will either be based off a research paper
    * or I will randomly create times myself.
    */
    private long rateOfBlink(){
        //The blink rate is an average of 6 seconds with a standard deviation of 1 second.
        //Z-score = (X - mean) / sd
        //So, to find the rate, we convert the function to:
        //X = Z-score * sd + mean

        Random random = new Random();
        double z = random.nextGaussian();
        int sd = 1000;
        int mean = 6000;

        return (long) (z * sd + mean);
    }

    /*resetFace() is a method that sets the face to
    * the initialized face, or to the last facial expression
    * the face had.
    */
    private void resetFace(){
        try {
            imageView[LEFT_EYEBROW].setBackgroundResource(resource[LEFT_EYEBROW]);
            imageView[RIGHT_EYEBROW].setBackgroundResource(resource[RIGHT_EYEBROW]);
            imageView[LEFT_EYE].setBackgroundResource(resource[LEFT_EYE]);
            imageView[RIGHT_EYE].setBackgroundResource(resource[RIGHT_EYE]);
            imageView[MOUTH].setBackgroundResource(resource[MOUTH]);
        }catch (Exception e){
            Log.e("Face", "resetColor: " + e.toString() );
        }
    }//End of resetFace()...

    /*changeColor() is a method that sets the tint of
    * the facial features to the baseColor. Each time
    * an expression or gaze changes, you would need to
    * reset the color so that the color matches/stays the
    * same.
    */
    private void resetColor(){
        try {
            imageView[LEFT_EYEBROW].getBackground().setColorFilter(porterDuffColorFilter);
            imageView[RIGHT_EYEBROW].getBackground().setColorFilter(porterDuffColorFilter);
            imageView[LEFT_EYE].getBackground().setColorFilter(porterDuffColorFilter);
            imageView[RIGHT_EYE].getBackground().setColorFilter(porterDuffColorFilter);
            imageView[MOUTH].getBackground().setColorFilter(porterDuffColorFilter);
        }catch (Exception e){
            Log.e("Face", "resetColor: " + e.toString() );
        }
    }

    /*These are the functions to change the appearance of the robot.
    * These are the only other public/protected functions that the user should
    * be able to access besides the Getters, Setters, and the Constructor.
    */
    public void changeColorTo(int color){
        setBaseColor(color);
    }
    public void changeFaceTo(Mood mood){
        if(inGazeMode){
            Log.i("Face", "changeFaceTo: inGazeMode. Cannot change face.");
            return;
        }
        setFace(mood);
    }
    public void changeGazeTo(int gazeType){
        if(gazeThread == null) {
            gazeThread = new GazeThread();
            gazeThread.start();
        }

        gazeThread.mHandler.sendEmptyMessage(gazeType);
    }
    public void changeToBlink(){
        blink();
    }

    protected void setEyesToNoPupils(){
        //stop the blink timer
        t.cancel();

        //replace the resources to the ones without pupils
        switch (mood){
            case NEUTRAL:
            case HAPPY:
            case AFRAID:
            case SURPRISE:
                imageView[LEFT_EYE].setBackgroundResource(R.drawable.normal_eye_without_pupil);
                imageView[RIGHT_EYE].setBackgroundResource(R.drawable.normal_eye_without_pupil);
                break;
            case SADNESS:
                imageView[LEFT_EYE].setBackgroundResource(R.drawable.sad_eye_without_pupil);
                imageView[RIGHT_EYE].setBackgroundResource(R.drawable.sad_eye_without_pupil);
                break;
            case ANGER:
            case TIRED:
                imageView[LEFT_EYE].setBackgroundResource(R.drawable.angry_tired_eye_without_pupil);
                imageView[RIGHT_EYE].setBackgroundResource(R.drawable.angry_tired_eye_without_pupil);
                break;
        }
        resetColor();
    }
    protected void setEyesToPupils(){
        //replace the resources to the ones with pupils
        switch (mood) {
            case NEUTRAL:
            case HAPPY:
                imageView[LEFT_EYE].setBackgroundResource(R.drawable.normal_eye0000);
                imageView[RIGHT_EYE].setBackgroundResource(R.drawable.normal_eye0000);
                break;
            case SURPRISE:
            case AFRAID:
                imageView[LEFT_EYE].setBackgroundResource(R.drawable.surprise_eye0000);
                imageView[RIGHT_EYE].setBackgroundResource(R.drawable.surprise_eye0000);
            case SADNESS:
                imageView[LEFT_EYE].setBackgroundResource(R.drawable.sad_eye0000);
                imageView[RIGHT_EYE].setBackgroundResource(R.drawable.sad_eye0000);
                break;
            case ANGER:
            case TIRED:
                imageView[LEFT_EYE].setBackgroundResource(R.drawable.angry_tired_eye0000);
                imageView[RIGHT_EYE].setBackgroundResource(R.drawable.angry_tired_eye0000);
                break;

        }
        resetColor();

        //start the blink timer
        startWaitingToBlink();
    }

    private class BlinkTask extends TimerTask{
        /**BlinkTask is a class that uses TimerTask to
         * perform a method in the Face class while implementing
         * Runnable to have that same method run on a different Thread.
         * BlinkTask simply has the face perform the blink() method.
         *                                              -Ese
         */

        /*run() just performs the blink() method. After which it will
        * set up another timer to begin the process to blink again
        * using the method startWaitingToBlink().
        */
        @Override
        public void run() {
            handler.post(Face.this::blink);
            handler.post(Face.this::startWaitingToBlink);
        }
    }
}//End of Face{}...