package com.anr.appwithroomdatabase.fragments;

import static com.anr.appwithroomdatabase.utilites.EditTextHandler.getDataFromTextInput;
import static com.anr.appwithroomdatabase.utilites.MyValidator.checkFieldData;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.anr.appwithroomdatabase.databinding.FragmentSignupLayoutBinding;
import com.anr.appwithroomdatabase.roomdatabase.DatabaseHelper;
import com.anr.appwithroomdatabase.roomdatabase.datamodel.UserModel;
import com.anr.appwithroomdatabase.utilites.ImageParser;
import com.anr.appwithroomdatabase.utilites.MyDialog;
import com.anr.appwithroomdatabase.utilites.MyValidator;

import java.io.IOException;

public class SignUpFragment extends Fragment {
    private FragmentSignupLayoutBinding binding;
    private ActivityResultLauncher<String> mTakePhoto;
    public String base64String = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSignupLayoutBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.signinInSignup.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());
        binding.editImageButton.setOnClickListener(v -> mTakePhoto.launch("image/*"));
        mTakePhoto = imageContract();
        binding.signUpButton.setOnClickListener(v -> signUpProcess(DatabaseHelper.getDB(getContext())));
    }

    private ActivityResultLauncher<String> imageContract() {
        return registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), result);
                base64String = ImageParser.encodeImage(bitmap);
                binding.signUpProfilePhoto.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void signUpProcess(DatabaseHelper databaseHelper) {
        String firstNameInput = getDataFromTextInput(binding.firstNameInput);
        String lastNameInput = getDataFromTextInput(binding.lastNameInput);
        String emailInput = getDataFromTextInput(binding.emailInput);
        String mobileNoInput = getDataFromTextInput(binding.mobileNoInput);
        String passInput = getDataFromTextInput(binding.passwordInput);
        String errorMsg = checkAllFields(firstNameInput, lastNameInput, emailInput, mobileNoInput, passInput);
        if (errorMsg == null) {
            if (databaseHelper.entityDao().checkEmailInDatabase(emailInput) == null) {
                databaseHelper.entityDao().addUser(new UserModel(firstNameInput, lastNameInput, emailInput, mobileNoInput, passInput, base64String));
                //               Toast.makeText(getContext(), "Account created.", Toast.LENGTH_SHORT).show();
                MyDialog.showDialog(getContext(), "New account Created", "Now you can sign in to app.");
                MyBroadcastSender.sendBroadcast(requireActivity().getApplicationContext(), "Clicked on SignUp  button, New user created.");
                requireActivity().getSupportFragmentManager().popBackStack();
            } else {
                MyDialog.showDialog(getContext(), "This email is already associated with some account", "Please Sign in.");
               // Toast.makeText(getContext(), "This email is already associated with some account.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), errorMsg, Toast.LENGTH_SHORT).show();
        }
    }

    @Nullable
    private String checkAllFields(String firstNameInput, String lastNameInput, String emailInput, String mobileNoInput, String passInput) {
        if (!checkFieldData(MyValidator.InputField.NAME, firstNameInput)) {
            return "Please fill proper first name";
        }
        if (!checkFieldData(MyValidator.InputField.NAME, lastNameInput)) {
            return "Please fill proper last name";
        }
        if (!checkFieldData(MyValidator.InputField.EMAIL, emailInput)) {
            return "Please fill proper email";
        }
        if (!checkFieldData(MyValidator.InputField.MOBILE_NO, mobileNoInput)) {
            return "Please fill proper mobile number";
        }
        if (!checkFieldData(MyValidator.InputField.PASSWORD, passInput)) {
            return "Please fill proper password.";
        }
        return null;
    }
}