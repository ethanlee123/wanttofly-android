package com.example.wanttofly.advancedsearch;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.example.wanttofly.R;
import com.example.wanttofly.search.FlightStatusCheck;
import com.example.wanttofly.search.SearchViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class FilterBottomSheetFragment extends BottomSheetDialogFragment {
    public static final String TAG = "FilterBottomSheetFragment";

    CheckBox onTimeCheckbox;
    CheckBox delayedCheckbox;
    CheckBox cancelledCheckbox;
    Button saveButton;

    BottomSheetDialog dialog;
    SearchViewModel viewModel;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_filter_bottom_sheet, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(SearchViewModel.class);

        onTimeCheckbox = v.findViewById(R.id.cb_on_time);
        delayedCheckbox = v.findViewById(R.id.cb_delayed);
        cancelledCheckbox = v.findViewById(R.id.cb_cancelled);
        saveButton = v.findViewById(R.id.b_save);

        setupCheckboxes();
        setupSaveButton();
        return v;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public int getTheme() {
        return R.style.AppBottomSheetDialogTheme;
    }

    private void setupCheckboxes() {
        FlightStatusCheck sorter = viewModel.getSortOptions();
        onTimeCheckbox.setChecked(sorter.isCheckedOnTime());
        delayedCheckbox.setChecked(sorter.isCheckedDelayed());
        cancelledCheckbox.setChecked(sorter.isCheckedCancelled());
    }

    private void setupSaveButton() {
        saveButton.setOnClickListener(view -> {
            viewModel.setSortOptions(onTimeCheckbox.isChecked(),
                    delayedCheckbox.isChecked(),
                    cancelledCheckbox.isChecked());

            viewModel.updateShownFlightsBasedOnSort();

            this.dismiss();
        });
    }
}
