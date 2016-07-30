package com.birulia.newsstand20.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.birulia.newsstand20.R;


public class FilterDialogFragment extends DialogFragment {

    private EditText etDate;
    private Button btnSave;

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
        // Fetch arguments from bundle and set title
        String title = getArguments().getString("title", "Filter Settings");
        getDialog().setTitle(title);
        // Show soft keyboard automatically and request focus to field
        etDate.requestFocus();

        etDate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                FragmentManager fm = getFragmentManager();
                DatePickerFragment datePickerFragment = new DatePickerFragment(){
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                        etDate.setText(year + "/" + month + "/" + day);

//                        SharedPreferences mSettings = getActivity().getSharedPreferences(, 0);
//                        SharedPreferences.Editor editor = mSettings.edit();
//                        editor.putString("begin_date", getBeginDate());
//                        editor.putLong("sort_id", getSortOrderId());
//                        editor.putString("sort", getSortOrderName());
//                        editor.putString("fq", getNewsDesk());
//                        editor.apply();
                    }
                };
                datePickerFragment.show(fm, "select date");
            }
        });

//        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

}

