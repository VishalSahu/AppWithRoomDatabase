package com.anr.appwithroomdatabase.fragments;

import static com.anr.appwithroomdatabase.utilites.EditTextHandler.getDataFromTextInput;
import static com.anr.appwithroomdatabase.utilites.MyValidator.checkFieldData;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.anr.appwithroomdatabase.databinding.FragmentForgetPasswordBinding;
import com.anr.appwithroomdatabase.roomdatabase.DatabaseHelper;
import com.anr.appwithroomdatabase.utilites.MyValidator;

public class ForgetPasswordFragment extends Fragment {
    private FragmentForgetPasswordBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentForgetPasswordBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.resetButton.setOnClickListener(v -> changePassFromDatabase());
    }

    private void changePassFromDatabase() {
        DatabaseHelper databaseHelper = DatabaseHelper.getDB(getContext());
        String email = getDataFromTextInput(binding.emailEdittext);
        String password = getDataFromTextInput(binding.passwordEdittext);
        if (!email.isEmpty() && checkFieldData(MyValidator.InputField.EMAIL, email) && checkFieldData(MyValidator.InputField.PASSWORD, password)) {
            if (databaseHelper.entityDao().getSingleRecord(email) != null) {
                databaseHelper.entityDao().changePassword(email, password);
                Toast.makeText(getContext(), "Password updated.", Toast.LENGTH_SHORT).show();
                requireActivity().getSupportFragmentManager().popBackStack();
            } else {
                Toast.makeText(getContext(), "No account for this email.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "Please fill all details properly.", Toast.LENGTH_SHORT).show();
        }
    }
}
