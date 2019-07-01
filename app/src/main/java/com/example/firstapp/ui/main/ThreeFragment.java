package com.example.firstapp.ui.main;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.firstapp.R;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import static androidx.constraintlayout.widget.Constraints.TAG;

/**
 * A placeholder fragment containing a simple view.
 */
public class ThreeFragment extends Fragment {

    private static final int GALLERY_REQUEST_CODE = 10;
    ImageView imageView1, imageView2 , imageView3;
    Button button1;
    Button button2, button3, button4;
    ImageEncryption imageEncryption;
    Bitmap backgroundImage, secretImage;
    boolean bgInitial = false;
    boolean scInitial = false;
    boolean bgJustNow = false;
    Bitmap encrypted;
    //HashMap<String, Bitmap> bitmapDict = new HashMap<String, Bitmap>();


    public ThreeFragment() {
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
        button4 = view.findViewById(R.id.button4);
        imageView1 = view.findViewById(R.id.imageView1);
        imageView2 = view.findViewById(R.id.imageView2);
        imageView3 = view.findViewById(R.id.imageView3);
        //imageView4 = view.findViewById(R.id.imageView4);


        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //b1 = false;
                pickFromGallery();
                bgInitial = true;
                bgJustNow = true;
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //b2 = false;
                pickFromGallery();
                scInitial = true;
                bgJustNow = false;
            }
        });

        imageEncryption = new ImageEncryption();
        button3.setOnClickListener (new View.OnClickListener() {
           @Override
           public void onClick (View view) {
               Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Encryption In Progress", Toast.LENGTH_LONG);
               toast.show();
               if (bgInitial && scInitial) {
                   //give "Encrypting Message"
                   //Toast.makeText(getContext(), "Encryption In Progress", Toast.LENGTH_LONG).show();
                   //Snackbar.make(view, "Encryption In Progress...", Snackbar.LENGTH_LONG).setAction("Action", null).show();


                   encrypted = imageEncryption.Encrypt(backgroundImage, secretImage);
                   imageView3.setImageBitmap(encrypted);
                   //give "Encrypting Complete"
                   //Snackbar.make(view, "Encryption Successfully Completed", Snackbar.LENGTH_LONG).setAction("Action", null).show();
               }
               else {
                   //else: give error message\
                   Snackbar.make(view, "Please Choose Both Background and Secret Images", Snackbar.LENGTH_LONG).setAction("Action", null).show();
               }
           }
        });

        button4.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                String root = Environment.getExternalStorageDirectory().toString();
                File myDir = new File(root + "/req_images");
                myDir.mkdirs();
                Random generator = new Random();
                int n = 10000;
                n = generator.nextInt(n);
                String fname = "Image-" + n + ".jpg";
                File file = new File(myDir, fname);
                Log.i(TAG, "" + file);
                if (file.exists())
                    file.delete();
                try {
                    FileOutputStream out = new FileOutputStream(file);
                    encrypted.compress(Bitmap.CompressFormat.JPEG, 90, out);
                    out.flush();
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
                        if (bgJustNow) {
                            imageView1.setImageURI(selectedImage);
                            backgroundImage = bitmap;

                        }
                        else {
                            secretImage = bitmap;
                            imageView2.setImageURI(selectedImage);


                            //count = 0;
                        }

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
