package com.example.temidemoforfebruary_19;

import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class Gaze {
    /**The Gaze class is a class that is dedicated to implementing
     * 'thoughtfulness' or convey 'naturalness' when the robot is
     * in communication with someone else. The gaze will be based
     * on a research paper that analyzes human gazes and interactions
     * with humans.
     *
     * During the first part of communication, the robot should
     * use more Cognitive-level gaze aversion.
     * During the middle part of communication, the robot should
     * use more Intimacy-regulation gaze aversion.
     * During the last part of the communication, the robot should
     * use more Floor-management gaze aversion.
     *                                                      -Ese
     */

    //Data members
    static final int CANCEL = -1;
    static final int STRAIGHT = 0;
    static final int COGNITIVE = 1;
    static final int INTIMACY_REGULATION = 2;
    static final int FLOOR_MANAGEMENT = 3;
    static final int PERFORM_ALL = 4;

    /*getDirection() is a method that calculated
     * where the robot would be facing (or directing
     * its eyes) based on the type of gaze it is
     * performing.
     */
    @NotNull
    protected static String getDirection(@NotNull GazeType gazeType){
        //Generate random number and turn in the direction
        //based on where the number lands in the spot.
        Random random = new Random();
        int percent = random.nextInt(1000) + 1; //returns a number between 1-1000

        if(percent <= gazeType.up){
            return "UP";
        }
        else if(percent <= gazeType.left){
            return "LEFT";
        }
        else if(percent <= gazeType.down){
            return "DOWN";
        }
        else if(percent <= gazeType.right){
            return "RIGHT";
        }

        //if the percent was greater than all of these values, that means the gazeType was either CANCEL or STRAIGHT
        return "STRAIGHT";
    }

    /*getDuration() is a method that uses a helper function
    * to calculate the duration of gaze based on the gaze type
    */
    protected static long getDuration(@NotNull GazeType gazeType){
        return durationOfGaze(gazeType.duration_mean, gazeType.duration_sd);
    }

    /*durationOfGaze() is a method that calculates
    * how long the gaze should last based on the
    * average and standard deviation of said gaze
    */
    private static long durationOfGaze(int sd, int mean){
        //Z-score * standard deviation + mean = duration.
        Random random = new Random();
        double z = random.nextGaussian();

        return Math.abs((long) z*sd + mean);
    }

    /*performGaze() is a method that will implement one type of
    * gaze or a particular series of gazes based on the type
    **/
    public void performGaze(int type) {
        GazeThread gazeThread = new GazeThread();
        gazeThread.mHandler.sendEmptyMessage(type);
        gazeThread.run();
    }

}//End of Gaze{}...