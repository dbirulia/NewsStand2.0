package com.birulia.newsstand20.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.birulia.newsstand20.Constants;
import com.birulia.newsstand20.R;

import java.util.Calendar;


public class FilterDialogFragment extends DialogFragment {

    private EditText etDate;
    private Button btnSave, btnCancel;
    private Spinner sOrder, sNewsDesk;

    public FilterDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static FilterDialogFragment newInstance(String title) {
        FilterDialogFragment frag = new FilterDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        // request a window without the title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_filter, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get fields from view
        etDate = (EditText) view.findViewById(R.id.etDate);
        btnSave = (Button) view.findViewById(R.id.btnSave);
        btnCancel = (Button) view.findViewById(R.id.btnCancel);
        sOrder = (Spinner) view.findViewById(R.id.sOrder);
        sNewsDesk = (Spinner) view.findViewById(R.id.sNewsDesk);

//        // Create an ArrayAdapter using the string array and a default spinner layout
//        ArrayAdapter<CharSequence> orderAdapter = ArrayAdapter.createFromResource(getContext(),
//                R.array.order_array, android.R.layout.simple_spinner_item);
//        // Specify the layout to use when the list of choices appears
//        orderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        // Apply the adapter to the spinner
//        sOrder.setAdapter(orderAdapter);
//
//        // Create an ArrayAdapter using the string array and a default spinner layout
//        ArrayAdapter<CharSequence> newsDeskAdapter = ArrayAdapter.createFromResource(getContext(),
//                R.array.news_desk_array, android.R.layout.simple_spinner_item);
//        // Specify the layout to use when the list of choices appears
//        newsDeskAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        // Apply the adapter to the spinner
//        sOrder.setAdapter(newsDeskAdapter);


        String title = getArguments().getString("title", "Filter Settings");
        getDialog().setTitle(title);
        // Show soft keyboard automatically and request focus to field
        // etDate.requestFocus();

        final Calendar c = Calendar.getInstance();
        etDate.setText(dateToStr(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)));
        etDate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                FragmentManager fm = getFragmentManager();
                DatePickerFragment datePickerFragment = new DatePickerFragment(){
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        String dateStr = dateToStr(year, month, day);
                        etDate.setText(dateStr);

                    }
                };
                datePickerFragment.show(fm, "select date");
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {
                                            SharedPreferences mSettings = getActivity().getSharedPreferences(Constants.SHARED_SEARCH_SETTINGS, 0);
                                            SharedPreferences.Editor editor = mSettings.edit();
                                            editor.putString("begin_date", getBeginDate());
                                            editor.putString("sort", getSortOrder());
                                            editor.putString("news_desk", getNewsDesk());
                                            editor.apply();
                                            dismiss();

                                       }
                                   });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

//        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    @Override
    public void onDismiss(final DialogInterface dialog) {
        super.onDismiss(dialog);
        final Activity activity = getActivity();
        if (activity instanceof DialogInterface.OnDismissListener) {
            ((DialogInterface.OnDismissListener) activity).onDismiss(dialog);
        }
    }

    private String getBeginDate(){
        return String.valueOf(etDate.getText()).replaceAll("/","");
    }

    private String getNewsDesk(){
        if (sNewsDesk.getSelectedItem().toString().toLowerCase() != "all") {
            return sNewsDesk.getSelectedItem().toString().toLowerCase();
        }
        else{
            return "";
        }
    }

    private String getSortOrder(){
        return sOrder.getSelectedItem().toString().toLowerCase();
    }

    private String dateToStr(int year, int month, int day){

        String yearStr = Integer.valueOf(year).toString();
        String monthStr = Integer.valueOf(month).toString();
        String dayStr = Integer.valueOf(day).toString();
        if (month < 10){monthStr = "0"+ month;}
        if (day < 10){dayStr = "0"+ day;}
        return yearStr + "/" + monthStr + "/" + dayStr;
    }
}

