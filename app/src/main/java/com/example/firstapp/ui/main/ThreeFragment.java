package com.example.firstapp.ui.main;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
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

import static androidx.constraintlayout.widget.Constraints.TAG;

import static android.graphics.Bitmap.Config.ARGB_8888;
import static android.graphics.Bitmap.createBitmap;

/**
 * A placeholder fragment containing a simple view.
 */
public class ThreeFragment extends Fragment {

    private static final int GALLERY_REQUEST_CODE = 10;
    ImageView imageView1, imageView2 , imageView3;
    Button button1;
    Button button2, button3, button4;
    ImageEncryption imageEncryption;
    Bitmap backgroundImage, secretImage, encryptedImage;
    boolean bgInitial = false;
    boolean scInitial = false;
    boolean bgJustNow = false;
    ProgressBar simpleProgressBar;
    Bitmap encrypted;

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
        simpleProgressBar = (ProgressBar) view.findViewById(R.id.simpleProgressBar);
        //imageView4 = view.findViewById(R.id.imageView4);
        simpleProgressBar.setVisibility(View.INVISIBLE);

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

               if (bgInitial && scInitial) {


                   //give "Encrypting Message"
//                   Toast.makeText(getContext(), "Encryption In Progress", Toast.LENGTH_LONG).show();
                   //Snackbar.make(view, "Encryption In Progress...", Snackbar.LENGTH_LONG).setAction("Action", null).show();


                   //Bitmap encrypted = imageEncryption.Encrypt(backgroundImage, secretImage);
                   if (backgroundImage.getHeight() != secretImage.getHeight() || backgroundImage.getWidth() != secretImage.getWidth()){
                       Toast.makeText(getContext(), "Cannot encrypt! Images have different sizes!", Toast.LENGTH_SHORT).show();
                   }
                   else {

                       Toast.makeText(getContext(), "Encryption In Progress", Toast.LENGTH_SHORT).show();
                       new EncryptTask().execute(backgroundImage, secretImage);
                   }
                   //new EncryptTask().execute(backgroundImage, secretImage);
//                   setProgressValue(progress + 10);
                  // imageView3.setImageBitmap(encryptedImage);

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
                final File myDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "encrypted");

                if (!myDir.exists()) {
                    Log.d(TAG, "Folder doesn't exist, creating it...");
                    boolean rv = myDir.mkdir();
                    Log.d(TAG, "Folder creation " + ( rv ? "success" : "failed"));
                } else {
                    Log.d(TAG, "Folder already exists.");
                }
//                String root = Environment.getExternalStorageDirectory().toString();
//                File myDir = new File(root + "/Encryption");
//                myDir.mkdirs();
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

    private void scanFile(String path) {

        MediaScannerConnection.scanFile(getContext(), new String[] { path }, null, new MediaScannerConnection.OnScanCompletedListener() {

                    public void onScanCompleted(String path, Uri uri) {
                        Log.d("Tag", "Scan finished. You can view the image in the gallery now.");
                    }
                });
    }

    private void setProgressValue (final int progress){
        simpleProgressBar.setProgress(progress);
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


    private class EncryptTask extends AsyncTask<Bitmap, Integer, Bitmap> {
        // Do the long-running work in here

        protected void onPreExecute(){
            simpleProgressBar.setVisibility(View.VISIBLE);
            setProgressValue(0);
            imageView3.setImageResource(R.drawable.ic_launcher_background);

//            if (backgroundImage.getHeight() != secretImage.getHeight() || backgroundImage.getWidth() != secretImage.getWidth()){
//                Toast.makeText(getContext(), "Encryption In Progress", Toast.LENGTH_SHORT).show();
//            }
//            else {
//                Toast.makeText(getContext(), "Cannot encrypt! Images have different sizes!", Toast.LENGTH_SHORT).show();
//            }

        }
        protected Bitmap doInBackground(Bitmap... imgs) {
            //encryptedImage = imageEncryption.Encrypt(backgroundImage, secretImage);

            Bitmap encryptImage = createBitmap(backgroundImage.getWidth(), backgroundImage.getHeight(), ARGB_8888);//backGroundImage ;//Bitmap.createBitmap(backGroundImage); //BitmapFactory.decodeFile(backGround);
            int p, a, b, c, d, e, f, g, h;

            if (backgroundImage.getHeight() != secretImage.getHeight() || backgroundImage.getWidth() != secretImage.getWidth())
            {
                return backgroundImage;
            }

            for (int i = 0; i < backgroundImage.getHeight(); i++) {
                for (int j = 0; j < backgroundImage.getWidth(); j++) {
                    p = backgroundImage.getPixel(j, i);
                    a = (p >> 24) & 0xff;
                    b = (p >> 16) & 0xff;
                    c = (p  >> 8) & 0xff;
                    d = (p & 0x0ff);
                    p = secretImage.getPixel(j, i);
                    e = (p >> 24) & 0xff;
                    f = (p >> 16) & 0xff;
                    g = (p  >> 8) & 0xff;
                    h = (p & 0x0ff);
                    a = (16 * (a / 16) + e / 16) << 24;
                    b = (16 * (b / 16) + f / 16) << 16;
                    c = (16 * (c / 16) + g / 16) <<8;
                    d = (16 * (d / 16) + h / 16);

                    try {
                        encryptImage.setPixel(j, i, a+b+c+d);//0x008577);
                    } catch (IllegalStateException z){
                        z.printStackTrace();
                    }
                }

                if (i % 100 == 0) {
                    publishProgress((int)((i*100.0 / backgroundImage.getHeight())));
                }

            }

            encryptedImage = encryptImage;
//            int i = 0;
//                while (i <= 100) {
//                    try {
//                        Thread.sleep(50);
//                        publishProgress(i);
//                        i++;
//                    }
//                    catch (Exception e) {
//                        Log.i("makemachine", e.getMessage());
//                    }
//            }
//            for (int i = 0; i < count; i++) {
//                totalSize += Downloader.downloadFile(urls[i]);
//                publishProgress((int) ((i / (float) count) * 100));
//                // Escape early if cancel() is called
//                if (isCancelled()) break;
//            }
            return encryptedImage;
        }

        // This is called each time you call publishProgress()
        protected void onProgressUpdate(Integer... progress) {
            //setProgressPercent(progress[0]);
            setProgressValue(progress[0]);
        }

        // This is called when doInBackground() is finished
        protected void onPostExecute(Bitmap result) {
            Toast.makeText(getContext(), "Encryption complete", Toast.LENGTH_SHORT).show();
            //showNotification("Processing Complete");
            simpleProgressBar.setVisibility(View.INVISIBLE);
            imageView3.setImageBitmap(result);
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
