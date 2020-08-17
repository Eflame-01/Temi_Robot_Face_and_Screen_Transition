package com.example.temidemoforfebruary_19;

public enum GazeType {
    /**GazeType is an enum that holds the values for the
     * probability of the robot facing a direction, duration
     * of the gaze and other values depending on what type of
     * gaze aversion it's performing.
     *                                              -Ese
     */

    //GazeTypes
    COGNITIVE("COGNITIVE", 1, 393, 550, 844, 1000, 1100, 3500),
    INTIMACY_REGULATION("INTIMACY REGULATION", 2, 137, 425, 713, 1000, 1390,4750),
    FLOOR_MANAGEMENT("FLOOR MANAGEMENT", 3, 213, 459, 754, 1000, 1100, 2300),
    STRAIGHT("STRAIGHT", 4, -1, -1, -1, -1, 1000, 1000),
    CANCEL("CANCEL", -1, -1, -1, -1, -1, 1000, 1000);

    //Data Members
    String name;
    int typeVal;
    int up;
    int down;
    int left;
    int right;
    int duration_mean;
    int duration_sd;

    //Constructors
    GazeType(String name, int typeVal, int up, int left, int down, int right, int duration_mean, int duration_sd){
        this.name = name;
        this.typeVal = typeVal;
        this.up = up;
        this.down = down;
        this.left = left;
        this.right = right;
        this.duration_mean = duration_mean;
        this.duration_sd = duration_sd;
    }
}
