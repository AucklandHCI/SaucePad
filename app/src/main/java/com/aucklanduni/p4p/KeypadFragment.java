package com.aucklanduni.p4p;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.text.Spannable;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aucklanduni.p4p.scalang.Keypad;
import com.aucklanduni.p4p.scalang.KeypadItem;
import com.aucklanduni.p4p.scalang.ScalaElement;
import com.aucklanduni.p4p.scalang.member.sMethod;
import com.aucklanduni.p4p.scalang.printer.ClickableText;
import com.aucklanduni.p4p.scalang.printer.NonClickableText;
import com.aucklanduni.p4p.scalang.printer.ScalaPrinter;
import com.aucklanduni.p4p.scalang.sClass;
import com.aucklanduni.p4p.symtab.MethodSymbol;
import com.aucklanduni.p4p.symtab.Scope;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
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
    private sClass mainClass;
//    private final ScalaPrinter printer = new ScalaPrinter();

    private GridView gv_keyPad;
    private EditText editor;
    private ArrayAdapter<KeypadItem> adapter;
    private int count = 0;
    private static Context ctx;
    private Keypad keypad;
    private String enteredText;
    private Stack<Object> stk_bckSpc = new Stack<>(); //Stack holding all the items that are printed onto the screen
    private Stack<List<KeypadItem>> stk_prevKeyPadItems = new Stack<>(); //Keeps track of all the lists that are displayed on the keypad.

    static final List<KeypadItem> initalList = new ArrayList<KeypadItem>();
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
        initalList.clear();
        initalList.add(new KeypadItem("New Class", true));

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
        editor.setMovementMethod(LinkMovementMethod.getInstance());
        keypad = new Keypad(this);
        setItemAdapter(initalList);


        gv_keyPad.setOnItemClickListener(this);

        return view;
    }




    public void printText(){
        editor.setText("");
        ScalaPrinter printer = new ScalaPrinter(keypad);

        String source = printer.getSource(mainClass);
        editor.setText(source, TextView.BufferType.SPANNABLE);
        Spannable spans = (Spannable) editor.getText();

        BreakIterator brIterator = BreakIterator.getWordInstance(Locale.US);
        brIterator.setText(source);

        List<ClickableSpan> cTexts = printer.getClickables();

        BreakIterator iterator = BreakIterator.getWordInstance(Locale.US);
        iterator.setText(source);

        int start = brIterator.first();
        int cIndex = 0;


        try {
            for (int end = brIterator.next(); end != BreakIterator.DONE; start = end, end = brIterator
                    .next()) {
                String possibleWord = source.substring(start, end);
                possibleWord = possibleWord.trim();
                if (!possibleWord.isEmpty() && !possibleWord.equals("≤")) {
                    ClickableSpan cs = cTexts.get(cIndex);
                    if (cs instanceof ClickableText) {
                        ClickableText ct = (ClickableText) cs;
                        ct.setOnClickListener(this);
                        spans.setSpan(ct, start, end,
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                    cIndex++;
                }
            }
        }catch (IndexOutOfBoundsException e){
            e.printStackTrace();
        }
        //TODO need to hide the keyboard
    }

    public void addToStack(String text){
        stk_bckSpc.push(text);
    }

    public void setItemAdapter(List<KeypadItem> items){

        if(items != null){
            if(items.size() != 0){
                stk_prevKeyPadItems.push(items);
            }
        }

        if (items == null){ // get input form user
            getLiteralInput(String.class);
            return;
        }else if (items.size() == 0) { // application has printed a mandatory character.
            items = keypad.getNextItems(""); //get next item
            if(items == null){
                getLiteralInput(String.class);
                return;
            }

            try {
                while (items.size() == 0) {
                    items = keypad.getNextItems("");

                }
            }catch(NullPointerException e){
                setItemAdapter(null);
                return;
            }

//            setItemAdapter(keypad.getNextItems());
        }

        if(items.size() == 1) {
            KeypadItem first = items.get(0);
            if (first == null){
                showAvailableMethods();
                return;
            }
        }

        if(items.size() == 2){
            KeypadItem first = items.get(0);
            KeypadItem second = items.get(1);

            if (first == null && second == null){
                getLiteralInput(Integer.class);
                return;
            }
        }

        adapter = new ArrayAdapter<KeypadItem>(ctx, R.layout.keypad_items /*android.R.layout.simple_list_item_1*/, items);
        KeypadItem bckSpace = new KeypadItem("Back", true);
        adapter.add(bckSpace);
        gv_keyPad.setAdapter(adapter);
        gv_keyPad.invalidateViews();
        printText();


    }

    private void showAvailableMethods(){


        final Dialog dialog = new Dialog(ctx);
        dialog.setContentView(R.layout.dialog_methods);
        dialog.setTitle("Method Lookup");

        // set the custom dialog components - text, image and button
        final EditText text = (EditText) dialog.findViewById(R.id.etMSearch);
        Button cancel = (Button) dialog.findViewById(R.id.btnCancel);
        final ListView lv_methods = (ListView) dialog.findViewById(R.id.lvMethods);

        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Scope scope = keypad.getCurrentScope();
        List<String> methodNames = scope.getByInstanceOf(MethodSymbol.class);
        Collections.sort(methodNames);

        MethodSymbol mSym = null;
        sMethod sm = null;
        final List<String> methods = new ArrayList<>();

        for(String m : methodNames){
            mSym = (MethodSymbol)scope.resolve(m);
            sm = mSym.getMethod();
            methods.add(ScalaPrinter.printMethodSignature(sm));
        }


        final ArrayAdapter<String> a = new ArrayAdapter<String>(ctx,android.R.layout.simple_list_item_1,methods);
        lv_methods.setAdapter(a);


        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                List<String> m = new ArrayList<String>();
                for (String mn : methods) {
                    if (mn.contains(s)) {
                        m.add(mn);
                    }
                }

                lv_methods.setAdapter(new ArrayAdapter<String>(ctx, android.R.layout.simple_list_item_1, m));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        lv_methods.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                printText();

//                setItemAdapter(keypad.getNextItems(input.getValue()));
            }
        });

        dialog.show();


    }

    private void getLiteralInput(Class type){



        final AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setTitle("Title");

        // Set up the input
        final EditText input = new EditText(this.getActivity());

        // Specify the type of input expected;
        if(type == Integer.class){
            input.setInputType(InputType.TYPE_CLASS_PHONE);
        }else{
            input.setInputType(InputType.TYPE_CLASS_TEXT);
        }

        input.requestFocus();
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                enteredText = input.getText().toString() + " ";
//                printText(enteredText);
                addToStack(enteredText);
                keypad.setField(enteredText);
                printText();
                InputMethodManager imm = (InputMethodManager) ctx.getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                //txtName is a reference of an EditText Field
                imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
                setItemAdapter(keypad.getNextItems(""));

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setItemAdapter(keypad.getNextItems(""));
                dialog.cancel();
            }
        });

        final AlertDialog dlg = builder.show();

        input.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
               
                enteredText = input.getText().toString() + " ";
//                printText(enteredText);
                addToStack(enteredText);
                keypad.setField(enteredText);
                printText();
                InputMethodManager imm = (InputMethodManager) ctx.getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                //txtName is a reference of an EditText Field
                imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
                setItemAdapter(keypad.getNextItems(""));
                
                dlg.dismiss();
                return false;
            }
        });

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
        String value = input.getValue();



        if (!input.dontPrint()){ // if not dummy print it
//            editor.setText(editor.getText() + " " + value);

            keypad.setField(value);
        }



        Stack<ScalaElement> stack = keypad.getTypeStack();
        if (value.contains("Another")){
            stack.peek().incrementCount();
            keypad.setIsList(true);
        }else if (value.contains("Done")){
            stack.peek().incrementCount();
            stack.peek().setDoneWithStatements(true);
            keypad.setIsList(false);
//            stack.peek().incrementCount();
//            ScalaElement prev = stack.get(stack.size() - 2);
//            prev.incrementCount();
        }


//        if(input.getValue() == "Back"){
//            Object poped = stk_bckSpc.pop(); //gets the poped "object"
//            String popedStr = poped.toString();
//            if(popedStr == "("){
//                for(int i = 0 ; i <= 1 ; i++){
//                    popedStr = stk_bckSpc.pop().toString() + " " + popedStr;
//                }
//            }
////            if(popedStr.contains("mand")){
////                popedStr = popedStr.replace("mand" , "");
////                popedStr = stk_bckSpc.pop().toString() + " " + popedStr ;
////            }
//            Log.d(TAG, "BACKSPACE ITEM: " + poped.toString());
//            String editStr = editor.getText().toString();
//            String x = editStr.replace(popedStr,""); // Removes unwanted string
//            editor.getText().clear();
////            printText(x);
////            keypad.getPrevItems();
//            setItemAdapter(stk_prevKeyPadItems.pop());
//            return;
//        }

        if (keypad.getType() == null) {
            List<KeypadItem> list = keypad.setType(input.getValue()); //set it to whatever is clicked from keypad
            if (list != null){
                setItemAdapter(list);
                return;
            }
        }

        printText();

        setItemAdapter(keypad.getNextItems(input.getValue()));


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

    public void setMainClass(sClass mainClass) {
        this.mainClass = mainClass;
    }

    public List<KeypadItem> getInitialList(){
        return initalList;
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
