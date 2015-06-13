package com.aucklanduni.p4p;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;

import com.aucklanduni.p4p.scalang.Keypad;
import com.aucklanduni.p4p.scalang.KeypadItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Stack;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link KeypadFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link KeypadFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class KeypadFragment extends Fragment implements AdapterView.OnItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String TAG = "testing";


    private GridView gv_keyPad;
    private EditText editor;
    private ArrayAdapter<KeypadItem> adapter;
    private int count = 0;
    private static Context ctx;
    private Keypad keypad;
    private String enteredText;
    private Stack<Object> stk_bckSpc = new Stack<>(); //Stack holding all the items that are printed onto the screen
    private Stack<List<KeypadItem>> stk_prevKeyPadItems = new Stack<>(); //Keeps track of all the lists that are displayed on the keypad.


    static final List<KeypadItem> letters = new ArrayList<KeypadItem>();
    static final List<String> numbers = new ArrayList<String>();


//    static final List<String>

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment KeypadFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static KeypadFragment newInstance(Context context) {
        KeypadFragment fragment = new KeypadFragment();
        ctx = context;
        KeypadItem [] l = new KeypadItem[]{
                new KeypadItem("def")

        };

//                {
//                "A", "B", "C", "D", "E",
//                "F", "G", "H", "I", "J",
//                "K", "L", "M", "N", "O",
//                "P", "Q", "R", "S", "T",
//                "U", "V", "W", "X", "Y", "Z"};

//        String [] n = new String[]{
//                "0", "1", "2", "3", "4",
//                "5", "6", "7", "8", "9",};

        for(KeypadItem s : l){
            letters.add(s);
        }
//
//        for(String s : n){
//            numbers.add(s);
//        }


//        lettersayList<String>(Arrays.asList(l));

        return fragment;
    }

    public KeypadFragment() {
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
        View view = inflater.inflate(R.layout.fragment_keypad, container, false);

        gv_keyPad = (GridView) view.findViewById(R.id.gv_KeyPad);
        editor = (EditText) view.findViewById(R.id.et_edit);

        setItemAdapter(letters);
        keypad = new Keypad(this);

        gv_keyPad.setOnItemClickListener(this);

        return view;
    }

    public void printText(String text){
        editor.setText(editor.getText() + " " + text);
    }

    public void addToStack(String text){
        stk_bckSpc.push(text);
    }

    private void setItemAdapter(List<KeypadItem> items){


//        if(items != null){
//            for (KeypadItem s : items){
//                Log.d(TAG, "item: " + s);
//            }
//        }
        if(items != null){
            if(items.size() != 0){
                stk_prevKeyPadItems.push(items);
            }
        }

        if (items == null){ // get input form user
            getStringLiteralInput();
            return;
        }else if (items.size() == 0) { // application has printed a mandatory character.
            items = keypad.getNextItems(); //get next item
            if(items == null){
                getStringLiteralInput();
                return;
            }
//            setItemAdapter(keypad.getNextItems());
        }

        if(items.size() == 1 && items.get(0).toString() == "def"){
            addToStack("def");
        }

        adapter = new ArrayAdapter<KeypadItem>(ctx, android.R.layout.simple_list_item_1, items);
        KeypadItem bckSpace = new KeypadItem("Back", true);
        adapter.add(bckSpace);
        gv_keyPad.setAdapter(adapter);
        gv_keyPad.invalidateViews();


    }

    private void getStringLiteralInput(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setTitle("Title");

        // Set up the input
        final EditText input = new EditText(this.getActivity());
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.requestFocus();
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                enteredText = input.getText().toString() + " ";
                printText(enteredText);
                addToStack(enteredText);
                keypad.setField(enteredText);
                InputMethodManager imm = (InputMethodManager) ctx.getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                //txtName is a reference of an EditText Field
                imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
                setItemAdapter(keypad.getNextItems());

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();

        InputMethodManager inputMethodManager = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
        // only will trigger it if no physical keyboard is open
        inputMethodManager.showSoftInput(input, InputMethodManager.SHOW_IMPLICIT);
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        KeypadItem input = (KeypadItem) parent.getAdapter().getItem(position);

        if (!input.isDummy()){ // if not dummy print it
            editor.setText(editor.getText() + " " + input.getValue() );
            keypad.setField(input.getValue());
        }

        if(input.getValue() == "Back"){
            Object poped = stk_bckSpc.pop(); //gets the poped "object"
            String popedStr = poped.toString();
            if(popedStr == "("){
                for(int i = 0 ; i <= 1 ; i++){
                    popedStr = stk_bckSpc.pop().toString() + " " + popedStr;
                }
            }
//            if(popedStr.contains("mand")){
//                popedStr = popedStr.replace("mand" , "");
//                popedStr = stk_bckSpc.pop().toString() + " " + popedStr ;
//            }
            Log.d(TAG, "BACKSPACE ITEM: " + poped.toString());
            String editStr = editor.getText().toString();
            String x = editStr.replace(popedStr,""); // Removes unwanted string
            editor.getText().clear();
            printText(x);
//            keypad.getPrevItems();
            setItemAdapter(stk_prevKeyPadItems.pop());
            return;
        }

        if (keypad.getType() == null) {
            keypad.setType(input.getValue()); //set it to whatever is clicked from keypad
        }

        setItemAdapter(keypad.getNextItems());


//
//        if (count > 8){
//            count = 0;
//        }else{
//            count++;
//        }
//
//        if((count % 2) == 0){
//            setAdapter(letters);
//        }else{
//            setAdapter(numbers);
//        }

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
