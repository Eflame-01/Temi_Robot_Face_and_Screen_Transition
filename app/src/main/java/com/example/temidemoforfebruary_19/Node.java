package com.example.temidemoforfebruary_19;

import java.util.ArrayList;

class Node {
    /**Node is the class that contains the imageViewID
     * along with the edges that the Node is connected
     * to.
     *                                            -Ese
     */
    //Data Members:
    int drawableId;
    ArrayList<Edge> adjacencyList;

    //Constructor
    Node(int drawableId){
        this.drawableId = drawableId;
        this.adjacencyList = new ArrayList<>();
    }

    /*addEdge() adds a new edge connected to
    * a new node. If there is already an edge
    * connected to that destination, than
    * this method replaces that edge.
    * Runtime: O(n).
    */
    void addEdge(Edge newEdge){
        Edge edgeInQuestion = null;
        for(Edge edge : adjacencyList){
            if(edge.sameDestination(newEdge)){
                edgeInQuestion = edge;
            }
        }
        if(edgeInQuestion != null){
            this.adjacencyList.remove(edgeInQuestion);
        }
        this.adjacencyList.add(newEdge);
    }
}//End of class Node{}...
