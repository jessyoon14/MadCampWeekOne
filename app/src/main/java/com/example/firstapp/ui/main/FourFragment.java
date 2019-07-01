package com.example.firstapp.ui.main;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.example.firstapp.R;

import java.io.IOException;

/**
 * A placeholder fragment containing a simple view.
 */
public class FourFragment extends Fragment {

    private static final int GALLERY_REQUEST_CODE = 10;
    ImageView imageView1, imageView2;
    Button button1, button2, button3;
    ImageEncryption imageEncryption;
    Bitmap encryptedImage, decryptedImage;
    int count = 0;
    boolean hasImage = false;
    //HashMap<String, Bitmap> bitmapDict = new HashMap<String, Bitmap>();


    public FourFragment() {
// Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
// Inflate the layout for this fragment

        View view = inflater.inflate (R.layout.fragment_three, container, false);

        button1 = view.findViewById(R.id.button);
        button2 = view.findViewById(R.id.button2);
        button3 = view.findViewById(R.id.button3);
        imageView1 = view.findViewById(R.id.imageView1);
        imageView2 = view.findViewById(R.id.imageView2);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickFromGallery();
                hasImage = true;
            }
        });

        imageEncryption = new ImageEncryption();
        button2.setOnClickListener (new View.OnClickListener() {
           @Override
           public void onClick (View view) {
               if (hasImage) {
                   decryptedImage = imageEncryption.Decrypt(encryptedImage);
                   imageView2.setImageBitmap(decryptedImage);
               }
               //else: give error message
           }
        });


        return view;
    }


    private void pickFromGallery(){
        //Create an Intent with action as ACTION_PICK
        Intent intent=new Intent(Intent.ACTION_PICK);
        // Sets the type as image/*. This ensures only components of type image are selected
        intent.setType("image/*");
        //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
        // Launching the Intent
        startActivityForResult(intent,GALLERY_REQUEST_CODE);

    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        // Result code is RESULT_OK only if the user selects an Image
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode){
                case GALLERY_REQUEST_CODE:
                    //data.getData returns the content URI for the selected Image
                    //count ++;

                    Uri selectedImage = data.getData();

                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(), selectedImage);
                        imageView1.setImageURI(selectedImage);
                        encryptedImage = bitmap;

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    break;
            }

}

}


//    private static final String ARG_SECTION_NUMBER = "section_number";
//
//    private PageViewModel pageViewModel;
//
//    public static PlaceholderFragment newInstance(int index) {
//        PlaceholderFragment fragment = new PlaceholderFragment();
//        Bundle bundle = new Bundle();
//        bundle.putInt(ARG_SECTION_NUMBER, index);
//        fragment.setArguments(bundle);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
//        int index = 1;
//        if (getArguments() != null) {
//            index = getArguments().getInt(ARG_SECTION_NUMBER);
//        }
//        pageViewModel.setIndex(index);
//    }
//
//    @Override
//    public View onCreateView(
//            @NonNull LayoutInflater inflater, ViewGroup container,
//            Bundle savedInstanceState) {
//        View root = inflater.inflate(R.layout.fragment_main, container, false);
//        final TextView textView = root.findViewById(R.id.section_label);
//        pageViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
//        return root;
//    }