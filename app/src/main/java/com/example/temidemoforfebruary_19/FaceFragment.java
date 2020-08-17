package com.example.temidemoforfebruary_19;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.constraintlayout.widget.Guideline;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FaceFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FaceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FaceFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    //Views for facial features - the "containers" for each picture of an eye, nose, etc. go here.
    View leftEye;
    View rightEye;
    View nose;
    View mouth;

    //guidelines - move these to adjust the location of each facial feature
    Guideline eyeGuide; //dictates vertical position of eyes
    Guideline noseGuide;
    Guideline mouthGuide;
    Guideline leftGuide; // vertical guide for eye -- stores distance of eye from left border
    Guideline rightGuide;

    //VALUES
    //image scales
    float eyeScale;
    float noseScale;
    float mouthScale;
    float faceScale; //size of whole face

    //locations
    float faceX; //x value of face
    float faceY;
    float eyeHeight; //vertical location of eye guideline
    float noseHeight;
    float mouthHeight;
    float eyeCloseness; //distance of each vertical guide from border

    /*I added the public static View ffv to the code. The reason for this
    *   was because the fragment is going to have the layout that will have
    *   the imageView's needed for the facial features. In order for me to
    *   have access to them and manipulate them, I would need to have a way
    *   to gain access to the View. If you get rid of ffv, You WILL get an error
    *   in the method setAllImageViews() in Face.java.
    *                                                               -Ese
    */
    public static View ffv;

    private ViewModel viewModel;
    public void setViewModel(ViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public FaceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FaceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FaceFragment newInstance(String param1, String param2) {
        FaceFragment fragment = new FaceFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        /*I set ffv to the fragment that has the imageViews I want to manipulate.
        * by inflating the layout that I created for the temi-robot
        *                                                               -Ese
        */
        ffv =  inflater.inflate(R.layout.temi_face, container, false);
        return ffv;

        //return inflater.inflate(R.layout.fragment_face, container, false);
    }


    //custom methods:
    //assigns relevant values to face components
    void getViews(){
        leftEye = getView().findViewById(R.id.leftEye);
        rightEye = getView().findViewById(R.id.rightEye);
        nose = getView().findViewById(R.id.nose);
        mouth = getView().findViewById(R.id.mouth);
    }

    //assigns relevant guidelines
    void getGuides(){
        Log.i("Guide","in guides");
        eyeGuide = getView().findViewById(R.id.eyeGuide);
        Log.i("Guide","found view");

        noseGuide = getView().findViewById(R.id.noseGuide);
        mouthGuide = getView().findViewById(R.id.mouthGuide);
        rightGuide = getView().findViewById(R.id.rightGuide);
        leftGuide = getView().findViewById(R.id.leftGuide);
    }


    //helper function for correct
    float correctRatio(float r){
        if (r>1){return 1;}
        if (r<0){return 0;}
        return r;
    }

    //sets values for face params
    void setEyeScale(float s){
        leftEye.setScaleX(s);
        leftEye.setScaleY(s);
        rightEye.setScaleX(s);
        rightEye.setScaleY(s);
    }
    void setEyeHeight(float r){
        eyeGuide.setGuidelinePercent(correctRatio(r));
    }
    void setEyeCloseness(float r){

        rightGuide.setGuidelinePercent(correctRatio(r));
        leftGuide.setGuidelinePercent(1-correctRatio(r));
    }
    void setNoseScale(float s){
        nose.setScaleX(s);
        nose.setScaleY(s);
    }
    void setNoseHeight(float r){
        noseGuide.setGuidelinePercent(correctRatio(r));
    }
    void setMouthScale(float sX,float sY){
        mouth.setScaleX(sX);
        mouth.setScaleY(sY);
    }
    void setMouthHeight(float r){
        mouthGuide.setGuidelinePercent(correctRatio(r));
    }



    //function for setting up a simple debugger
    int debugEyeId;
    void debugEyeSwitcher(int i){
        debugEyeId =0;
        leftEye.setClickable(true);
        View.OnClickListener o = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ImageView)leftEye).setImageResource(FaceFeatures.getFeature(FaceFeatures.F.left,debugEyeId));
                ((ImageView)rightEye).setImageResource(FaceFeatures.getFeature(FaceFeatures.F.right,debugEyeId));
                debugEyeId++;

            }
        };
        leftEye.setOnClickListener(o);
        rightEye.setOnClickListener(o);
    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
