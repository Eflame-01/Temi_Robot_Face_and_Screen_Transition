package com.example.temidemoforfebruary_19;

import java.util.ArrayList;

class Path {
    /**Path is a class that holds a list of
     * Nodes together in order of the start
     * Node to the end Node. It also gives
     * the total weight of its path.
     *                              -Ese
     */
    //Data Member
    int weightOfPath;
    ArrayList<Integer> drawableFrames;

    //Constructor
    Path(){
        weightOfPath = 0;
        drawableFrames = new ArrayList<>();
    }

    /*addToPath() adds the destinationDrawableID
    * to the list of frames and the weight from the
    * edge to the total weight of the path.
    * Runtime: O(1).
    */
    void addToPath(int weight, int id){
        this.weightOfPath += weight;
        this.drawableFrames.add(id);
    }
}//End of class Path{}...
