package com.example.temidemoforfebruary_19;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import org.jetbrains.annotations.NotNull;

import static com.example.temidemoforfebruary_19.TransitionDemo.face;
import static com.example.temidemoforfebruary_19.TransitionDemo.robot;

public class GazeThread extends Thread{
    /**The GazeThread is a class that extends the Thread
     * class. it is designed to perform almost autonomous-like
     * gaze aversions when having a conversation.
     *                                              -Ese
     */

    //Data member
    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler(){
        /*handleMessage() will check the current message given and
         * see what type of gaze to perform based on the message.
         */
        @Override
        public void handleMessage(Message msg){
            GazeTask task = new GazeTask();
            switch (msg.what){
                case Gaze.COGNITIVE:
                    task.execute(GazeType.COGNITIVE, GazeType.CANCEL);
                    break;
                case Gaze.INTIMACY_REGULATION:
                    task.execute(GazeType.INTIMACY_REGULATION, GazeType.CANCEL);
                    break;
                case Gaze.FLOOR_MANAGEMENT:
                    task.execute(GazeType.FLOOR_MANAGEMENT, GazeType.CANCEL);
                    break;
                case Gaze.STRAIGHT:
                    task.execute(GazeType.STRAIGHT, GazeType.CANCEL);
                    break;
                case Gaze.PERFORM_ALL:
                    task.execute(
                            GazeType.COGNITIVE, GazeType.STRAIGHT,
                            GazeType.INTIMACY_REGULATION, GazeType.STRAIGHT,
                            GazeType.FLOOR_MANAGEMENT,GazeType.STRAIGHT,
                            GazeType.CANCEL
                    );
                    break;
                case Gaze.CANCEL:
                    task.execute(GazeType.CANCEL);
                    break;
            }
        }
    };

    /*run() will loop through the handlerQueue to check for any gaze
     * messages the user wants the robot ro perform
     */
    @Override
    public void run(){
        if(Looper.myLooper() == null){
            Looper.prepare();
        }
        Looper.loop();
    }

    private static class GazeTask extends AsyncTask<GazeType, String, Void> {
        /**The GazeTask is a class that extends the AsyncTask
         * class. It is designed to perform computations in the background
         * Thread, while also performing the necessary graphical changes in the
         * UI Thread. In this case, it will be responsible for moving the pupils
         * of the robot's face depending on the type of gaze
         */

        //Data member
        EyeGaze eyeGaze = new EyeGaze();
        String direction;
        String result;
        long duration;

        @Override
        protected void onPreExecute() {
            face.setEyesToNoPupils();
            Face.inGazeMode = true;
        }

        /*doInBackground will take the gazeType and give us the direction the
         * robot should face. Then it will sleep for the necessary duration. It
         * does this for each type of gaze in the array.
         */
        @Override
        protected Void doInBackground(@NotNull GazeType... gazeTypes) {
            for(GazeType gazeType : gazeTypes){
                if(gazeType.name.equals("CANCEL")){
                    cancel(true);
                    break;
                }

                direction = Gaze.getDirection(gazeType);
                duration = Gaze.getDuration(gazeType);
                result = direction + " : " + gazeType.name;

                publishProgress(result);

                try{
                    Thread.sleep(duration);
                }catch (Exception e){
                    Log.e("GazeThread", "doInBackground: " + e.toString() );
                }
            }

            return null;
        }

        /*onProgressUpdate will take the results from the doInBackground method
         * and turn the robot in the direction that was stated. It will also change
         * it's facial expression based on the type of gaze the robot is performing.
         */
        @Override
        protected void onProgressUpdate(@NotNull String... values) {
            String result = values[0];
            //just in case the face isn't initialized, we put it inside of a try/catch block
            try{
                face.setEyesToNoPupils();
                eyeGaze.revealPupils();

                Log.i("GazeThread ", "onProgressUpdate: " + result);

                //perform actions based on the gaze type and the direction
                if(result.contains("LEFT")){
                    eyeGaze.sendPupilsLeft();
                }
                else if(result.contains("RIGHT")){
                    eyeGaze.sendPupilsRight();
                }
                else if(result.contains("UP")){
                    eyeGaze.sendPupilsUp();
                    //tilt robot's head upwards
                    robot.tiltAngle(35);
                }
                else if(result.contains("DOWN")){
                    eyeGaze.sendPupilsDown();
                    //tilt robot's head downwards
                    robot.tiltAngle(15);
                }
                else if(result.contains("STRAIGHT")){
                    eyeGaze.sendPupilsCenter();
                    //tilt robot's head to be straight
                    robot.turnBy(0);
                    robot.tiltAngle(25);
                }
                else {
                    onCancelled();
                }
            }catch (Exception e){
                Log.e("GazeThread", "onProgressUpdate: " + e.toString() );
            }

        }

        /*onCancelled will reset the face's pupils to be still and not
         * to be gazing.
         */
        @Override
        protected void onCancelled() {
            handleCancelled();
        }
        @Override
        protected void onCancelled(Void aVoid) {
            handleCancelled();
        }

        private void handleCancelled(){
            Log.i("GazeThread", "handleCancelled: resetting face...");
            eyeGaze.sendPupilsCenter();
            robot.tiltAngle(25);
            eyeGaze.hidePupils();
            face.setEyesToPupils();
            Face.inGazeMode = false;
        }
    }//End of GazeTask{}...

}//End of GazeThread{}...

