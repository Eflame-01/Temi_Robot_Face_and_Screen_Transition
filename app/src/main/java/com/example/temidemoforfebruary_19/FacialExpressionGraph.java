package com.example.temidemoforfebruary_19;

import android.graphics.drawable.AnimationDrawable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

class FacialExpressionGraph {
    /**FacialExpressionGraph is a class that
     * creates a weighted graph to map out
     * facial expression transitions.
     *
     * The way this will work is by creating a
     * Node with the ImageViewIDs, adding those
     * Nodes to the HashMap, and creating the
     * appropriate Edges to those Nodes.
     *
     * That way, when you need a transition from one
     * image to another image, the class can get the
     * starting Node from the HashMap, grab all the other
     * Nodes (and subsequently their images) in the path
     * to the ending Node, simulating a animation.
     *                                            -Ese
     */
    //Data Members
    private HashMap<Integer, Node> nodeLookup = new HashMap<>();
    private HashSet<Path> pathLookup = new HashSet<>();

    /*createEdgeInGraph() takes two drawableIDs
    * and connects them through a edge. If the drawableID
    * has not been in the nodeLookup, then it will create
    * the node and connect them.
    * Runtime: O(1).
    */
    private void createEdgeInGraph(int sourceID, int destinationID, int weight){
        Node source;
        Node destination;

        if(nodeLookup.containsKey(sourceID)){
           nodeLookup.get(sourceID).addEdge(new Edge(weight, destinationID));
        }
        else{
            source = new Node(sourceID);
            source.addEdge(new Edge(weight, destinationID));
            nodeLookup.put(sourceID, source);
        }
        if(nodeLookup.containsKey(destinationID)){
            nodeLookup.get(destinationID).addEdge(new Edge(weight, sourceID));
        }
        else{
            destination = new Node(destinationID);
            destination.addEdge(new Edge(weight, sourceID));
            nodeLookup.put(destinationID, destination);
        }
    }

    /*findPathInGraph() tries to find the path between
    * one drawableID (sourceID) and another drawableID
    * (destinationID). if the sourceID or destinationID
    * do not exist, then there are no paths between
    * the two paths.
    * Runtime: O(n).
    */
    private void findPathsInGraph(int sourceID, int destinationID){
        if(nodeLookup.get(sourceID) == null || nodeLookup.get(destinationID) == null){
            return;
        }

        Path visited = new Path();

        if(sourceID == destinationID){
            return;
        }

        for(Edge child : nodeLookup.get(sourceID).adjacencyList){
            hasPathDFS(child.weightedLine, nodeLookup.get(child.destinationID), destinationID, visited);
        }
    }
    /*hasPathDFS() is a helper function to findPathInGraph()
    * that uses Depth First Search to find the edges that lead
    * to the destinationID.
    * Runtime: O(V).
    */
    /*TODO: this method of finding a path only gives us ONE path. Find out a way to get
    *  MULTIPLE paths to be stored in the listOfPaths to return the BEST optimal animation
    */
    private boolean hasPathDFS(int weight, Node source, int destinationID, Path visited){
        Integer id = source.drawableId;

        if(visited.drawableFrames.contains(source.drawableId)){
            //This means you have already visited this node without reaching destination
            return false;
        }
        visited.addToPath(weight, source.drawableId);
        if(source.drawableId == destinationID){
            //This means that we have reached the destination
            pathLookup.add(visited);
            return true;
        }
        //If we have not reached the destination, go to the next edge from this Node until you reach the destination or the end of path
        for(Edge child : source.adjacencyList){
            if(hasPathDFS(child.weightedLine, nodeLookup.get(child.destinationID), destinationID, visited)){
                //This means that the destination was found from this edge
                return true;
            }
        }

        //This means that each of your edges lead to a dead-end instead of the destination.
        visited.drawableFrames.remove(id);
        return false;
    }

    /*findBestPathInGraph() takes all the possible paths
    * in the graphs from the source to the destination
    * and picks the path with the least amount of weight.
    * Runtime: O(n).
    */
    private ArrayList<Integer> findBestPathInGraph(){
        int weight = Integer.MAX_VALUE;
        ArrayList<Integer> bestPath = null;
        for (Path path : pathLookup) {
            if(path.weightOfPath < weight){
                bestPath = path.drawableFrames;
                weight = path.weightOfPath;
            }
        }
        pathLookup.clear();
        return bestPath;
    }

    /*animateFacialFeature() will be used to animate the Nodes selected
     * from the Graph for the left/right eyebrows, eyes, and mouth.
     * Runtime: O(n).
     */
    AnimationDrawable animateFacialFeature(Face face, Mood newMood, int facialFeature){
        ArrayList<Integer> frames;
        AnimationDrawable animation = new AnimationDrawable();
        final int TOTAL_DURATION = 300; //millis
        int duration;
        int drawableID = -1;

        switch(facialFeature){
            case 0:
                findPathsInGraph(face.getMood().leftEyebrowImageViewID, newMood.leftEyebrowImageViewID);
                drawableID = newMood.leftEyebrowImageViewID;
                break;
            case 1:
                findPathsInGraph(face.getMood().rightEyebrowImageViewID, newMood.rightEyebrowImageViewID);
                drawableID = newMood.rightEyebrowImageViewID;
                break;
            case 2:
                findPathsInGraph(face.getMood().leftEyeImageViewID, newMood.leftEyeImageViewID);
                drawableID = newMood.leftEyeImageViewID;
                break;
            case 3:
                findPathsInGraph(face.getMood().rightEyeImageViewID, newMood.rightEyeImageViewID);
                drawableID = newMood.rightEyeImageViewID;
                break;
            case 4:
                findPathsInGraph(face.getMood().mouthImageViewID, newMood.mouthImageViewID);
                drawableID = newMood.mouthImageViewID;
                break;
        }

        frames = findBestPathInGraph();
        if(frames == null){
            animation.addFrame(face.getActivity().getDrawable(drawableID), TOTAL_DURATION);
        }
        else{
            frames.trimToSize();
            duration = TOTAL_DURATION/frames.size();

            for(Integer frame : frames){
                animation.addFrame(face.getActivity().getDrawable(frame), duration);
            }
        }
        animation.setOneShot(true);

        this.pathLookup.clear();

        return animation;
    }

    /*createFacialExpressionGraph() will be used to create
    * the Graph for the left/right eyebrows, eyes, and mouth
    * and return the Graph as a result.
    * Runtime: O(1).
    */
    FacialExpressionGraph createLeftEyebrowExpressionGraph(){
    /*
                                                    [afraid]
                                                   /
                                       [surprised]
                                    /
                [neutralToSurprised]
               /                    \\
      [neutral] --[neutralToHappy]-- [happy]
               \                    //
                  [neutralToSad]
                               \
                                [sad/Tired]
                                           \
                                            [angry]
     */
        nodeLookup.clear();
        pathLookup.clear();

        createEdgeInGraph(R.drawable.base_eyebrow, R.drawable.elevated_eyebrow, 1);
        createEdgeInGraph(R.drawable.base_eyebrow, R.drawable.low_eyebrow, 1);

        createEdgeInGraph(R.drawable.elevated_eyebrow, R.drawable.rise_eyebrow_l_0001, 1);
        createEdgeInGraph(R.drawable.elevated_eyebrow, R.drawable.raise_eyebrow_0001, 1);

        createEdgeInGraph(R.drawable.rise_eyebrow_l_0001, R.drawable.rise_eyebrow_l_0004, 1);
        createEdgeInGraph(R.drawable.rise_eyebrow_l_0001, R.drawable.rise_eyebrow_l_0002, 1);

        createEdgeInGraph(R.drawable.rise_eyebrow_l_0002, R.drawable.rise_eyebrow_l_0003, 1);
        createEdgeInGraph(R.drawable.rise_eyebrow_l_0004, R.drawable.rise_eyebrow_l_0005, 1);

        createEdgeInGraph(R.drawable.raise_eyebrow_0001, R.drawable.raise_eyebrow_0002, 1);
        createEdgeInGraph(R.drawable.raise_eyebrow_0002, R.drawable.raise_eyebrow_0003, 1);


        createEdgeInGraph(R.drawable.low_eyebrow, R.drawable.lower_eyebrow_0001, 1);
        createEdgeInGraph(R.drawable.low_eyebrow, R.drawable.set_eyebrow_l_0001, 1);

        createEdgeInGraph(R.drawable.set_eyebrow_l_0001, R.drawable.set_eyebrow_l_0002, 1);
        createEdgeInGraph(R.drawable.set_eyebrow_l_0002, R.drawable.set_eyebrow_l_0003, 1);

        createEdgeInGraph(R.drawable.lower_eyebrow_0001, R.drawable.lower_eyebrow_0002, 1);
        createEdgeInGraph(R.drawable.lower_eyebrow_0002, R.drawable.lower_eyebrow_0003, 1);

        return this;
    }
    FacialExpressionGraph createRightEyebrowExpressionGraph(){
    /*
     *                                               [afraid]
     *                                              /
     *                                  [surprised]
     *                               /
     *           [neutralToSurprised]
     *          /                    \\
     * [neutral] --[neutralToHappy]-- [happy]
     *          \                    //
     *             [neutralToSad]
     *                          \
     *                           [sad/Tired]
     *                                      \
     *                                       [angry]
     */
        nodeLookup.clear();
        pathLookup.clear();

        createEdgeInGraph(R.drawable.base_eyebrow, R.drawable.elevated_eyebrow, 1);
        createEdgeInGraph(R.drawable.base_eyebrow, R.drawable.low_eyebrow, 1);

        createEdgeInGraph(R.drawable.elevated_eyebrow, R.drawable.rise_eyebrow_r_0001, 1);
        createEdgeInGraph(R.drawable.elevated_eyebrow, R.drawable.raise_eyebrow_0001, 1);


        createEdgeInGraph(R.drawable.rise_eyebrow_r_0001, R.drawable.rise_eyebrow_r_0004, 1);
        createEdgeInGraph(R.drawable.rise_eyebrow_r_0001, R.drawable.rise_eyebrow_r_0002, 1);
        createEdgeInGraph(R.drawable.rise_eyebrow_r_0002, R.drawable.rise_eyebrow_r_0003, 1);
        createEdgeInGraph(R.drawable.rise_eyebrow_r_0004, R.drawable.rise_eyebrow_r_0005, 1);

        createEdgeInGraph(R.drawable.raise_eyebrow_0001, R.drawable.raise_eyebrow_0002, 1);
        createEdgeInGraph(R.drawable.raise_eyebrow_0002, R.drawable.raise_eyebrow_0003, 1);


        createEdgeInGraph(R.drawable.low_eyebrow, R.drawable.lower_eyebrow_0001, 1);
        createEdgeInGraph(R.drawable.low_eyebrow, R.drawable.set_eyebrow_r_0001, 1);

        createEdgeInGraph(R.drawable.set_eyebrow_r_0001, R.drawable.set_eyebrow_r_0002, 1);
        createEdgeInGraph(R.drawable.set_eyebrow_r_0002, R.drawable.set_eyebrow_r_0003, 1);

        createEdgeInGraph(R.drawable.lower_eyebrow_0001, R.drawable.lower_eyebrow_0002, 1);
        createEdgeInGraph(R.drawable.lower_eyebrow_0002, R.drawable.lower_eyebrow_0003, 1);

        return this;
    }
    FacialExpressionGraph createEyesExpressionGraph(){
    /*
     *  [neutral/happy] ----------- [neutral/happy to surprised/afraid] -------------- [surprised/afraid]
     *                 \                                                              /
     *                  [neutral/happy to close]          [surprised/afraid to close]
     *                                        |             |
     *                        [eyes almost closed_1]  [eyes almost closed_4]
     *                                          \       /
     *                                        [eyes closed]
     *                                          /       \
     *                        [eyes almost closed_3]   [eyes almost closed_2]
     *                                        |           |
     *                    [angry/tired to close]         [sad to close]
     *                  /                                              \
     *   [angry/tired]                                                  [sad]
     */
        nodeLookup.clear();
        pathLookup.clear();

        createEdgeInGraph(R.drawable.normal_eye0000, R.drawable.normal_eye0001, 1);
        createEdgeInGraph(R.drawable.normal_eye0001, R.drawable.eyes_almost_closed_01, 1);

        createEdgeInGraph(R.drawable.sad_eye0000, R.drawable.sad_eye0001, 1);
        createEdgeInGraph(R.drawable.sad_eye0001, R.drawable.eyes_almost_closed_02, 1);

        createEdgeInGraph(R.drawable.angry_tired_eye0000, R.drawable.angry_tired_eye0001, 1);
        createEdgeInGraph(R.drawable.angry_tired_eye0001, R.drawable.eyes_almost_closed_03, 1);

        createEdgeInGraph(R.drawable.surprise_eye0000, R.drawable.surprise_eye0001, 1);
        createEdgeInGraph(R.drawable.surprise_eye0001, R.drawable.eyes_almost_closed_04, 1);

        createEdgeInGraph(R.drawable.eyes_closed, R.drawable.eyes_almost_closed_01,1);
        createEdgeInGraph(R.drawable.eyes_closed, R.drawable.eyes_almost_closed_02,1);
        createEdgeInGraph(R.drawable.eyes_closed, R.drawable.eyes_almost_closed_03,1);
        createEdgeInGraph(R.drawable.eyes_closed, R.drawable.eyes_almost_closed_04,1);

        //TODO: You can try adding these sprites once the DFS is modified to take in more than one path
//        createEdgeInGraph(R.drawable.normal_eye0000, R.drawable.normal_to_surprise_eye0001, 1);
//        createEdgeInGraph(R.drawable.normal_to_surprise_eye0001, R.drawable.normal_to_surprise_eye0002, 1);
//        createEdgeInGraph(R.drawable.normal_to_surprise_eye0002, R.drawable.normal_to_surprise_eye0003, 1);
//        createEdgeInGraph(R.drawable.normal_to_surprise_eye0003, R.drawable.surprise_eye0000, 1);

        return this;
    }
    FacialExpressionGraph createMouthExpressionGraph(){
    /*
    *
    *                                                                     afraid_mouth_001 - afraid_mouth_002 - afraid_mouth_003
    *                                                                   /
    *                     sad_mouth_001 - sad_mouth_002 -  sad_mouth_003
    *                     |
    *                     ------------------------------------------              surprise_mouth_001 - surprise_mouth_002 - surprise_mouth_003
    *                                                                \            /
    *                                                                 normal_mouth
    *                                                                /            \
    *            angry_mouth_001 - angry_mouth_002 - angry_mouth_003               smile_mouth_001 - smile_mouth_002 - smile_mouth_003
    *
    * */
        nodeLookup.clear();
        pathLookup.clear();
        createEdgeInGraph(R.drawable.afraid_mouth_0003, R.drawable.afraid_mouth_0002, 1);
        createEdgeInGraph(R.drawable.afraid_mouth_0002, R.drawable.afraid_mouth_0001, 1);
        createEdgeInGraph(R.drawable.afraid_mouth_0001, R.drawable.sad_mouth_0003, 1);
        createEdgeInGraph(R.drawable.sad_mouth_0003, R.drawable.sad_mouth_0002, 1);
        createEdgeInGraph(R.drawable.sad_mouth_0002, R.drawable.sad_mouth_0001, 1);
        createEdgeInGraph(R.drawable.sad_mouth_0001, R.drawable.normal_mouth_0000, 1);

        createEdgeInGraph(R.drawable.angry_tired_mouth_0003, R.drawable.angry_tired_mouth_0002, 1);
        createEdgeInGraph(R.drawable.angry_tired_mouth_0002, R.drawable.angry_tired_mouth_0001, 1);
        createEdgeInGraph(R.drawable.angry_tired_mouth_0001, R.drawable.normal_mouth_0000, 1);

        createEdgeInGraph(R.drawable.smile_mouth_0003, R.drawable.smile_mouth_0002, 1);
        createEdgeInGraph(R.drawable.smile_mouth_0002, R.drawable.smile_mouth_0001, 1);
        createEdgeInGraph(R.drawable.smile_mouth_0001, R.drawable.normal_mouth_0000, 1);

        createEdgeInGraph(R.drawable.surprise_mouth_0003, R.drawable.surprise_mouth_0002, 1);
        createEdgeInGraph(R.drawable.surprise_mouth_0002, R.drawable.surprise_mouth_0001, 1);
        createEdgeInGraph(R.drawable.surprise_mouth_0001, R.drawable.normal_mouth_0000, 1);

        return this;
    }
}//End of FacialExpressionGraph{}...