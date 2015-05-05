package com.aucklanduni.p4p;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v7.internal.widget.ActionBarOverlayLayout;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.Stack;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EditorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditorFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Stack<String> previous = new Stack<String>();
    private Button clsButton;
    private Button bckSpcButton;
    private EditText editTxt;
    private String m_Text = "";
    private String toAdd = "";


    ActionBarOverlayLayout x = null;


    private String TAG = "testing";
    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditorFragment newInstance(String param1, String param2) {
        EditorFragment fragment = new EditorFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);

        return fragment;
    }

    public EditorFragment() {
        // Required empty public constructor
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
        View rowView =  inflater.inflate(R.layout.fragment_editor, container, false);
        clsButton = (Button) rowView.findViewById(R.id.btn_Class);
        bckSpcButton = (Button) rowView.findViewById(R.id.btn_bckspc);
        editTxt = (EditText) rowView.findViewById(R.id.et_editor);

        clsButton.setOnClickListener(this);
        bckSpcButton.setOnClickListener(this);
        return rowView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "name: " + v.getId());

        if(v.getId() == R.id.btn_Class){
            toAdd = "class ";
            previous.push(toAdd);
            typeClassName();

        }
        else if(v.getId() == R.id.btn_bckspc ){
            if(!previous.empty()) {
//                int len = ((String) previous.pop()).length();
//                String editor = editTxt.getText().toString();
//                editTxt.setText(editor.substring(0, editor.length() - len));
                editTxt.getText().clear();
                clsButton.setEnabled(true);
            }
            return;
        }
//        editTxt.setText(editTxt.getText() + toAdd);
        printToScreen(editTxt.getText().toString());
    }

    private void typeClassName(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setTitle("Title");

        // Set up the input
        final EditText input = new EditText(this.getActivity());
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_Text = input.getText().toString() + " ";
                clsButton.setEnabled(false);
                previous.push(m_Text);
                printToScreen(m_Text);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                editTxt.getText().clear();
            }
        });

        builder.show();
    }

    private void printToScreen(String text){
        editTxt.setText(toAdd + text);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
