package com.anr.appwithroomdatabase.fragments;

import static com.anr.appwithroomdatabase.utilites.EditTextHandler.getDataFromTextInput;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.anr.appwithroomdatabase.databinding.FragmentEditProfileLayoutBinding;
import com.anr.appwithroomdatabase.roomdatabase.DatabaseHelper;
import com.anr.appwithroomdatabase.utilites.ImageParser;
import com.anr.appwithroomdatabase.utilites.MyDialog;

import java.io.IOException;

public class EditProfileFragment extends Fragment {
    private static final String ARG_EMAIL = "emailId";
    private FragmentEditProfileLayoutBinding binding;
    private ActivityResultLauncher<String> mTakePhoto;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentEditProfileLayoutBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle arguments = this.getArguments();
        if (arguments == null) {
            return;
        }
        final String current_email = arguments.getString(ARG_EMAIL);
        if (TextUtils.isEmpty(current_email)) {
            return;
        }
        binding.profilePicPlaceholder.setImageBitmap(ImageParser.decodedImage(getContext(), current_email));
        binding.editImageButton.setOnClickListener(v -> mTakePhoto.launch("image/*"));
        mTakePhoto = imageContract(current_email);
        binding.updatePassBtn.setOnClickListener(v -> {
            String new_pass = getDataFromTextInput(binding.newPasswordEdit);
            String currentPassword = DatabaseHelper.getDB(getContext()).entityDao().getPasswordFromDatabase(current_email);
            if (currentPassword.equals(getDataFromTextInput(binding.oldPasswordEdit)) && new_pass.length() >= 8) {
                DatabaseHelper.getDB(getContext()).entityDao().changePassword(current_email, new_pass);
                MyDialog.showDialog(getContext(), "Password updated successfully.", "");
                MyBroadcastSender.sendBroadcast(requireActivity().getApplicationContext(), "Clicked on profile update button from edit profile page.");
                binding.newPasswordEdit.setText(null);
                binding.oldPasswordEdit.setText(null);
            } else {
                MyDialog.showDialog(getContext(), "Please enter valid old and new password.", "Try again.");
            }
        });
    }

    private ActivityResultLauncher<String> imageContract(String current_email) {
        return registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), result);
                String base64String = ImageParser.encodeImage(bitmap);
                binding.profilePicPlaceholder.setImageBitmap(bitmap);
                DatabaseHelper.getDB(getContext()).entityDao().changeProfilePicture(current_email, base64String);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
