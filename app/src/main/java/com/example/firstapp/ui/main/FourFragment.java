package com.example.firstapp.ui.main;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.firstapp.R;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import static android.graphics.Bitmap.Config.ARGB_8888;
import static android.graphics.Bitmap.createBitmap;
import static androidx.constraintlayout.widget.Constraints.TAG;

/**
 * A placeholder fragment containing a simple view.
 */
public class FourFragment extends Fragment {

    private static final int GALLERY_REQUEST_CODE = 10;
    ImageView imageView1, imageView2;
    Button button1, button2, button3;
    ImageEncryption imageEncryption;
    Bitmap encryptedImage, decryptedImage;
    ProgressBar simpleProgressBar;
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

        View view = inflater.inflate (R.layout.fragment_four, container, false);

        button1 = view.findViewById(R.id.button);
        button2 = view.findViewById(R.id.button2);
        button3 = view.findViewById(R.id.button3);
        imageView1 = view.findViewById(R.id.imageView1);
        imageView2 = view.findViewById(R.id.imageView2);
        simpleProgressBar = (ProgressBar) view.findViewById(R.id.simpleProgressBar);
        simpleProgressBar.setVisibility(View.INVISIBLE);
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
                   Toast.makeText(getContext(), "Decryption In Progress", Toast.LENGTH_SHORT).show();
                   new FourFragment.DecryptTask().execute(encryptedImage);
//                   decryptedImage = imageEncryption.Decrypt(encryptedImage);
//                   imageView2.setImageBitmap(decryptedImage);
               }
               else {
                   //else: give error message\
                   Snackbar.make(view, "Please Choose Image to Decrypt", Snackbar.LENGTH_LONG).setAction("Action", null).show();
               }
               //else: give error message
           }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                String root = Environment.getExternalStorageDirectory().toString();
                File myDir = new File(root + "/DCIM/Decryption");
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
                    decryptedImage.compress(Bitmap.CompressFormat.JPEG, 90, out);
                    out.flush();
                    out.close();
                    scanFile(myDir + "Image-" + n + ".jpg");
                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    intent.setData(Uri.fromFile(file));

                    getActivity().sendBroadcast(intent);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                Toast.makeText(getContext(), "Saved!", Toast.LENGTH_SHORT).show();
            }
        });


        return view;
    }
    private void setProgressValue (final int progress){
        simpleProgressBar.setProgress(progress);
    }
    private void scanFile(String path) {

        MediaScannerConnection.scanFile(getContext(), new String[] { path }, null, new MediaScannerConnection.OnScanCompletedListener() {

            public void onScanCompleted(String path, Uri uri) {
                Log.d("Tag", "Scan finished. You can view the image in the gallery now.");
            }
        });
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

    private class DecryptTask extends AsyncTask<Bitmap, Integer, Bitmap> {
        // Do the long-running work in here

        protected void onPreExecute(){
            simpleProgressBar.setVisibility(View.VISIBLE);
            setProgressValue(0);
            imageView2.setImageResource(R.drawable.ic_launcher_background);

//            if (backgroundImage.getHeight() != secretImage.getHeight() || backgroundImage.getWidth() != secretImage.getWidth()){
//                Toast.makeText(getContext(), "Encryption In Progress", Toast.LENGTH_SHORT).show();
//            }
//            else {
//                Toast.makeText(getContext(), "Cannot encrypt! Images have different sizes!", Toast.LENGTH_SHORT).show();
//            }

        }
        protected Bitmap doInBackground(Bitmap... imgs) {
            //encryptedImage = imageEncryption.Encrypt(backgroundImage, secretImage);

            Bitmap decryptImage = createBitmap(encryptedImage.getWidth(), encryptedImage.getHeight(), ARGB_8888);
            int p, a, b, c, d;

            for (int i = 0; i < encryptedImage.getHeight(); i++)
            {
                for (int j = 0; j < encryptedImage.getWidth(); j++)
                {
                    p = encryptedImage.getPixel(j, i);
                    a = (p >> 24) & 0xff;
                    b = (p >> 16) & 0xff;
                    c = (p  >> 8) & 0xff;
                    d = (p & 0x0ff);
                    a = (16 * (a % 16)) << 24;
                    b = (16 * (b % 16)) << 16;
                    c = (16 * (c % 16)) << 8;
                    d = 16 * (d % 16);
                    decryptImage.setPixel(j,i, a + b + c + d);
                }
                if (i % 100 == 0) {
                    publishProgress((int)((i*100.0 / encryptedImage.getHeight())));
                }
            }

            decryptedImage = decryptImage;
            return decryptedImage;
        }

        // This is called each time you call publishProgress()
        protected void onProgressUpdate(Integer... progress) {
            //setProgressPercent(progress[0]);
            setProgressValue(progress[0]);
        }

        // This is called when doInBackground() is finished
        protected void onPostExecute(Bitmap result) {
            Toast.makeText(getContext(), "Decryption complete", Toast.LENGTH_SHORT).show();
            //showNotification("Processing Complete");
            simpleProgressBar.setVisibility(View.INVISIBLE);
            imageView2.setImageBitmap(result);
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
