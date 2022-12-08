package com.anr.appwithroomdatabase.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.anr.appwithroomdatabase.R;
import com.anr.appwithroomdatabase.databinding.FragmentHomeLayoutBinding;
import com.anr.appwithroomdatabase.roomdatabase.DatabaseHelper;
import com.anr.appwithroomdatabase.roomdatabase.datamodel.UserModel;
import com.anr.appwithroomdatabase.utilites.ImageParser;

public class HomeFragment extends Fragment {

    public static final String ARG_EMAIL = "emailId";

    private FragmentHomeLayoutBinding binding;
    private String currentEmail;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeLayoutBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
        if (getArguments() != null) {
            currentEmail = getArguments().getString(ARG_EMAIL);
        }
        DrawerLayout drLayout = binding.drawer;
        View header = binding.navView.getHeaderView(0);
        ImageView profilePic = header.findViewById(R.id.profile_photo);
        LiveData<UserModel> userModel = DatabaseHelper.getDB(getContext()).entityDao().getSingleRecord(currentEmail);
        userModel.observe(getViewLifecycleOwner(), new Observer<UserModel>() {
            @Override
            public void onChanged(UserModel userModel) {
                profilePic.setImageBitmap(ImageParser.decodedImage(getContext(), currentEmail));
                TextView first_name = header.findViewById(R.id.profile_name);
                TextView last_name = header.findViewById(R.id.profile_email);
                first_name.setText(userModel.getFirstName());
                last_name.setText(userModel.getEmailID());
                displayUserInfo(userModel);
            }
        });
        binding.logoutButton.setOnClickListener(v -> onLogoutClick());
        binding.logoutButton2.setOnClickListener(v-> onLogoutClick());
        binding.logoutBtnInDrawer.setOnClickListener(v -> onLogoutClick());
        binding.menuButton.setOnClickListener(v -> drLayout.open());
        binding.homePageImageView.setImageBitmap(ImageParser.decodedImage(requireContext(), currentEmail));
        profilePic.setOnClickListener(v -> {
            MyBroadcastSender.sendBroadcast(requireActivity().getApplicationContext(), "Clicked on profile picture from homePage drawer header icon.");
            if (fragmentTransaction.isEmpty()) {
                Fragment fragment = new EditProfileFragment();
                fragment.setArguments(getArguments());
                fragmentTransaction.replace(R.id.base_layout_after_signup, fragment).addToBackStack("base").commit();
                drLayout.close();
            }
        });
    }

    private void displayUserInfo(UserModel userModel) {
        binding.nameDisplay.setText(getString(R.string.user_name, userModel.getFirstName(), userModel.getLastName()));
        binding.emailDisplay.setText(getString(R.string.user_email, currentEmail));
        binding.phoneNumberDisplay.setText(getString(R.string.user_contact, userModel.getContactNumber()));
    }

    private void onLogoutClick() {
        SharedPreferences.Editor editor = requireActivity().getSharedPreferences("Session", Context.MODE_PRIVATE).edit();
        editor.remove("current_email").apply();
        MyBroadcastSender.sendBroadcast(requireActivity().getApplicationContext(), "Clicked on logout button from HomePage");
        requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_activity_layout, new SigInFragment()).commit();
    }
}
