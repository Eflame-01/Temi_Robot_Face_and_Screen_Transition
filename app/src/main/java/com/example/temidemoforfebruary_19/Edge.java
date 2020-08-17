package com.example.temidemoforfebruary_19;

class Edge {
    /**Edge is a class that connects a Node
     * to the destination Nodes that
     * are 'connected' to each other, along with
     * their 'weight'.
     *                                   -Ese
     */
    //Data Members
    int weightedLine;
    int destinationID;

    //Constructor
    Edge(int weight, int destinationID){
        this.weightedLine = weight;
        this.destinationID = destinationID;
    }

    /*sameDestination() checks if the current
    * edge and the edge it's being compared to
    * has the same destination.
    * Runtime: O(1).
    */
    boolean sameDestination(Edge edge){
        return(this.destinationID == edge.destinationID);
    }
}//End of class Edge{}...