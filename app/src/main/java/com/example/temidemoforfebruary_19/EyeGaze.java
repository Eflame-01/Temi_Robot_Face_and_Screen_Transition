package com.example.temidemoforfebruary_19;

import android.widget.ImageView;


public class EyeGaze {
    /**EyeGaze is a class that handles the movements of the pupils when the
     * robot is performing the gaze feature. The EyeGaze class has predefined
     * variables that determine where the pupils will be located at, and moves
     * them to that location using a little animation
     *                                                          -Ese
     */
    private ImageView leftPupil = FaceFragment.ffv.findViewById(R.id.left_pupil);
    private ImageView rightPupil = FaceFragment.ffv.findViewById(R.id.right_pupil);

    private static final float LEFT_EYE_CENTER_X = FaceFragment.ffv.findViewById(R.id.left_pupil).getX();
    private static final float LEFT_EYE_CENTER_Y = FaceFragment.ffv.findViewById(R.id.left_pupil).getY();
    private static final float RIGHT_EYE_CENTER_X = FaceFragment.ffv.findViewById(R.id.right_pupil).getX();
    private static final float RIGHT_EYE_CENTER_Y = FaceFragment.ffv.findViewById(R.id.right_pupil).getY();

    private final float LEFT_EYE_LEFT = FaceFragment.ffv.findViewById(R.id.left_eye_start_width_guide).getX();
    private final float LEFT_EYE_RIGHT = FaceFragment.ffv.findViewById(R.id.left_eye_end_width_guide).getX() - FaceFragment.ffv.findViewById(R.id.left_pupil).getWidth();
    private final float RIGHT_EYE_LEFT = FaceFragment.ffv.findViewById(R.id.right_eye_start_width_guide).getX();
    private final float RIGHT_EYE_RIGHT = FaceFragment.ffv.findViewById(R.id.right_eye_end_width_guide).getX() - FaceFragment.ffv.findViewById(R.id.right_pupil).getWidth();

    private final float LEFT_EYE_TOP = FaceFragment.ffv.findViewById(R.id.eye_start_height_guide).getY();
    private final float LEFT_EYE_BOTTOM = FaceFragment.ffv.findViewById(R.id.eye_end_height_guide).getY()  - FaceFragment.ffv.findViewById(R.id.left_pupil).getHeight();
    private final float RIGHT_EYE_TOP = FaceFragment.ffv.findViewById(R.id.eye_start_height_guide).getY();
    private final float RIGHT_EYE_BOTTOM = FaceFragment.ffv.findViewById(R.id.eye_end_height_guide).getY()  - FaceFragment.ffv.findViewById(R.id.right_pupil).getHeight();


    /*revealPupils() will display the pupils in the UI Thread.
    * This will only happen when the Face is set to Gaze Mode.
    */
    protected void revealPupils(){
        leftPupil.setImageResource(R.drawable.pupil);
        rightPupil.setImageResource(R.drawable.pupil);
    }

    /*revealPupils() will remove the pupils in the UI Thread.
     * This will only happen when the Face is no longer set to
     * GazeMode.
     */
    public  void hidePupils(){
        leftPupil.setImageDrawable(null);
        rightPupil.setImageDrawable(null);
    }

    /*These methods are responsible for sending
    * the pupils in a certain direction based off of
    * the name.
    */
    public void sendPupilsUp(){
        revealPupils();
        leftPupil.animate()
                .y(LEFT_EYE_TOP)
                .setDuration(300)
                .start();
        rightPupil.animate()
                .y(RIGHT_EYE_TOP)
                .setDuration(300)
                .start();
    }
    public void sendPupilsDown(){
        revealPupils();
        leftPupil.animate()
                .y(LEFT_EYE_BOTTOM)
                .setDuration(300)
                .start();
        rightPupil.animate()
                .y(RIGHT_EYE_BOTTOM)
                .setDuration(300)
                .start();
    }
    public void sendPupilsLeft(){
        revealPupils();
        leftPupil.animate()
                .x(LEFT_EYE_LEFT)
                .setDuration(300)
                .start();
        rightPupil.animate()
                .x(RIGHT_EYE_LEFT)
                .setDuration(300)
                .start();
    }
    public void sendPupilsRight(){
        revealPupils();
        leftPupil.animate()
                .x(LEFT_EYE_RIGHT)
                .setDuration(300)
                .start();
        rightPupil.animate()
                .x(RIGHT_EYE_RIGHT)
                .setDuration(300)
                .start();
    }
    public void sendPupilsCenter(){
        revealPupils();
        leftPupil.animate()
                .x(LEFT_EYE_CENTER_X)
                .y(LEFT_EYE_CENTER_Y)
                .setDuration(300)
                .start();
        rightPupil.animate()
                .x(RIGHT_EYE_CENTER_X)
                .y(RIGHT_EYE_CENTER_Y)
                .setDuration(300)
                .start();
    }

}//End of EyeGaze{}...
