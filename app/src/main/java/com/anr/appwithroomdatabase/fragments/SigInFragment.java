package com.anr.appwithroomdatabase.fragments;

import static com.anr.appwithroomdatabase.utilites.EditTextHandler.getDataFromTextInput;
import static com.anr.appwithroomdatabase.utilites.MyValidator.checkFieldData;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.anr.appwithroomdatabase.R;
import com.anr.appwithroomdatabase.databinding.FragmentSigninLayoutBinding;
import com.anr.appwithroomdatabase.roomdatabase.DatabaseHelper;
import com.anr.appwithroomdatabase.roomdatabase.datamodel.UserModel;
import com.anr.appwithroomdatabase.utilites.MyValidator;


public class SigInFragment extends Fragment {
    FragmentSigninLayoutBinding binding;
    private String emailInput, passwordInput;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSigninLayoutBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DatabaseHelper databaseHelper = DatabaseHelper.getDB(getContext());
        binding.signInButton.setOnClickListener(v -> {
            MyBroadcastSender.sendBroadcast(requireActivity().getApplicationContext(),"Clicked on SignIn button from signIn page.");
            emailInput = getDataFromTextInput(binding.emailEdittext);
            passwordInput = getDataFromTextInput(binding.passwordEdittext);
            if (checkFieldData(MyValidator.InputField.EMAIL, emailInput) && checkFieldData(MyValidator.InputField.PASSWORD, passwordInput)) {
                LiveData<UserModel> userModel = databaseHelper.entityDao().getSingleRecord(emailInput);
                userModel.observe(getViewLifecycleOwner(), new Observer<UserModel>() {
                    @Override
                    public void onChanged(UserModel userModel) {
                        if (userModel != null) {
                            if (userModel.getPassword().equals(passwordInput)) {
                                addLoginStatus();
                            } else {
                                showToast("Wrong email or password.");
                            }
                        } else {
                            showToast("This user does not exists.");
                        }
                    }
                });
            } else {
                showToast("Please fill all fields properly.");
            }
        });
        binding.forgetPasswordText.setOnClickListener(v -> {
            MyBroadcastSender.sendBroadcast(requireActivity().getApplicationContext(), "Clicked on forget password button from SignIn Page.");
            requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_activity_layout, new ForgetPasswordFragment(), "forgetFrag").addToBackStack("signUpFrag").commit();
        });
        binding.signUpInSignIn.setOnClickListener(v -> {
            binding.emailEdittext.setText(null);
            binding.passwordEdittext.setText(null);
            MyBroadcastSender.sendBroadcast(requireActivity().getApplicationContext(), "Clicked on signUp button from signIn page");
            requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_activity_layout, new SignUpFragment(), "signUpFrag").addToBackStack("signUpFrag").commit();
        });
    }

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void addLoginStatus() {
        SharedPreferences.Editor editor = requireActivity().getSharedPreferences("Session", Context.MODE_PRIVATE).edit();
        editor.putString("current_email", emailInput).apply();
        HomeFragment homeFragment = new HomeFragment();
        Bundle bundle = new Bundle();
        bundle.putString(HomeFragment.ARG_EMAIL, emailInput);
        homeFragment.setArguments(bundle);
        getParentFragmentManager().beginTransaction().replace(R.id.main_activity_layout, homeFragment).commit();
    }
}