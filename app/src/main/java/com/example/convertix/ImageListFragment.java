package com.example.convertix;


import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class ImageListFragment extends Fragment {

    private static final int STORAGE_REQUEST_CODE = 100;
    private static final int CAMERA_REQUEST_CODE = 101;

    private String[] cameraPermission;
    private String[] storagePermission;

    private Uri imageUri = null;

    private FloatingActionButton addImageFab;


    private Context mContext;

    public ImageListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {

        mContext = context;
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_image_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};


        addImageFab = view.findViewById(R.id.addImageFab);


        addImageFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImageDialog();
            }
        });

    }

    private void pickImaageGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        galleryActivityResultLauncher.launch(intent);

    }

    private void showImageDialog() {
        PopupMenu popupMenu = new PopupMenu(mContext, addImageFab);

        popupMenu.getMenu().add(Menu.NONE, 1, 1, "Camera");
        popupMenu.getMenu().add(Menu.NONE, 1, 1, "Gallery");

        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int itemId = menuItem.getItemId();
                if (itemId == 1) {
                    if (checkCameraPermission()) {
                        pickImageCamera();
                    } else {
                        Toast.makeText(mContext, "Please Check Camera Permission", Toast.LENGTH_SHORT).show();
                        requestCameraPermission();
                    }

                } else if (itemId == 2) {

                    if (checkStoragePermission()) {
                        pickImaageGallery();
                    } else {
                        pickImaageGallery();
                    }
                }

                return true;
            }
        });
    }

    private ActivityResultLauncher<Intent> galleryActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {

            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();

                imageUri = data.getData();
            } else {
                Toast.makeText(mContext, "Cancel", Toast.LENGTH_SHORT).show();
            }
        }
    });

    private void pickImageCamera() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, "TEMP_IMAGE_TITLE");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "TEMP_IMAGE_DESCRIPTION");

        imageUri = mContext.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        cameraActivityResultLauncher.launch(intent);

    }

    private ActivityResultLauncher<Intent> cameraActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {

            if (result.getResultCode() == Activity.RESULT_OK) {

            } else {
                Toast.makeText(mContext, "Cancelled..", Toast.LENGTH_SHORT).show();
            }

        }
    });


    private boolean checkStoragePermission() {
        boolean resutl = ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

        return resutl;
    }


    private void requestStoragePermission() {

        requestPermissions(storagePermission, STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermission() {
        boolean cameraResult = ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        boolean storageResult = ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

        return cameraResult && storageResult;

    }

    private void requestCameraPermission() {
        requestPermissions(cameraPermission, CAMERA_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case CAMERA_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (cameraAccepted && storageAccepted) {

                    } else {
                        Toast.makeText(mContext, "Camera & Storage Permission required", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mContext, "Cancelled...", Toast.LENGTH_SHORT).show();
                }
            }
            break;
            case STORAGE_REQUEST_CODE: {

                if (grantResults.length > 0) {
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if (storageAccepted) {


                    } else {
                        Toast.makeText(mContext, "Storage Permission is required...", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mContext, "Cancelled....", Toast.LENGTH_SHORT).show();
                }

            }
            break;

        }
    }
}