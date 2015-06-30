package com.fisheradelakin.traveltracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Fisher on 6/29/15.
 */
public class MemoryDialogFragment extends DialogFragment {

    private static final String TAG = "MemoryDialogFragment";
    private static final String MEMORY_KEY = "MEMORY";

    private Memory mMemory;
    private Listener mListener;
    private View mView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if(args != null) {
            mMemory = (Memory) args.getSerializable(MEMORY_KEY);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        mView = getActivity().getLayoutInflater().inflate(R.layout.memory_dialog_fragment, null);
        TextView cityView = (TextView) mView.findViewById(R.id.city);
        cityView.setText(mMemory.getCity());

        TextView countryView = (TextView) mView.findViewById(R.id.country);
        countryView.setText(mMemory.getCountry());

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(mView)
                .setTitle(R.string.memory_dialog_title)
                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText notesView = (EditText) mView.findViewById(R.id.notes);
                        mMemory.setNotes(notesView.getText().toString());
                        mListener.onSaveClicked(mMemory);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.onCancelClicked(mMemory);
                    }
                });

        return builder.create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (Listener) getActivity();
        } catch (ClassCastException e) {
            throw new IllegalStateException("Activity does not implement listener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    // factory method
    public static MemoryDialogFragment newInstance(Memory memory) {
        MemoryDialogFragment fragment = new MemoryDialogFragment();

        Bundle args = new Bundle();
        args.putSerializable(MEMORY_KEY, memory);
        fragment.setArguments(args);

        return fragment;
    }

    public interface Listener {
        void onSaveClicked(Memory memory);
        void onCancelClicked(Memory memory);

    }

}
