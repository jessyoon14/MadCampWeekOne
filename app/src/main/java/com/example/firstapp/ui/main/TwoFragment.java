package com.example.firstapp.ui.main;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.firstapp.R;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A placeholder fragment containing a simple view.
 */
public class TwoFragment extends Fragment {
    private ArrayList<String> images;
   // private View view;
    public TwoFragment() {
// Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onAttach(getContext());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate (R.layout.fragment_two, container, false);


// Inflate the layout for this fragment
        GridView gallery = (GridView) view.findViewById(R.id.galleryGridView);
        gallery.setAdapter(new ImageAdapter((Activity) requireActivity()));
//        getAvailableActivity(new IActivityEnabledListener() {
//            @Override
//            public void onActivityEnabled(FragmentActivity activity) {
//                gallery.setAdapter(new ImageAdapter((Activity) getContext()));
//            }
//        });
//        gallery.setAdapter(new ImageAdapter(getAvailableActivity( new ABaseFragment.IActivityEnabledListener() {
//            @Override
//            public void onActivityEnabled(FragmentActivity activity) {
//                // Do manipulations with your activity
//            }
//        })));

        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                if (null != images && !images.isEmpty())
                    Toast.makeText(
                            getContext(),
                            "position " + position + " " + images.get(position),
                            Toast.LENGTH_SHORT).show();


            }
        });


        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity){
            Activity mActivity =(Activity) context;
        }

//        GridView gallery = (GridView) view.findViewById(R.id.galleryGridView);
//        gallery.setAdapter(new ImageAdapter((Activity) requireActivity()));
////        getAvailableActivity(new IActivityEnabledListener() {
////            @Override
////            public void onActivityEnabled(FragmentActivity activity) {
////                gallery.setAdapter(new ImageAdapter((Activity) getContext()));
////            }
////        });
////        gallery.setAdapter(new ImageAdapter(getAvailableActivity( new ABaseFragment.IActivityEnabledListener() {
////            @Override
////            public void onActivityEnabled(FragmentActivity activity) {
////                // Do manipulations with your activity
////            }
////        })));
//
//        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> arg0, View arg1,
//                                    int position, long arg3) {
//                if (null != images && !images.isEmpty())
//                    Toast.makeText(
//                            getContext(),
//                            "position " + position + " " + images.get(position),
//                            Toast.LENGTH_SHORT).show();
//
//
//            }
//        });
    }

//    @Override
//    public void onViewCreated(View view, Bundle savedInstanceState) {
////        final GridView gallery = (GridView) view.findViewById(R.id.galleryGridView);
////        gallery.setAdapter(new ImageAdapter((Activity) getContext()));
//////        getAvailableActivity(new IActivityEnabledListener() {
//////            @Override
//////            public void onActivityEnabled(FragmentActivity activity) {
//////                gallery.setAdapter(new ImageAdapter((Activity) getContext()));
//////            }
//////        });
//////        gallery.setAdapter(new ImageAdapter(getAvailableActivity( new ABaseFragment.IActivityEnabledListener() {
//////            @Override
//////            public void onActivityEnabled(FragmentActivity activity) {
//////                // Do manipulations with your activity
//////            }
//////        })));
////
////        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
////
////            @Override
////            public void onItemClick(AdapterView<?> arg0, View arg1,
////                                    int position, long arg3) {
////                if (null != images && !images.isEmpty())
////                    Toast.makeText(
////                            getContext(),
////                            "position " + position + " " + images.get(position),
////                            Toast.LENGTH_SHORT).show();
////                ;
////
////            }
////        });
//    }

//    @Override
//    public void onAttach ( Context context){
//        final GridView gallery = (GridView) view.findViewById(R.id.galleryGridView);
//        gallery.setAdapter(new ImageAdapter((Activity) getContext()));
////        getAvailableActivity(new IActivityEnabledListener() {
////            @Override
////            public void onActivityEnabled(FragmentActivity activity) {
////                gallery.setAdapter(new ImageAdapter((Activity) getContext()));
////            }
////        });
////        gallery.setAdapter(new ImageAdapter(getAvailableActivity( new ABaseFragment.IActivityEnabledListener() {
////            @Override
////            public void onActivityEnabled(FragmentActivity activity) {
////                // Do manipulations with your activity
////            }
////        })));
//
//        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> arg0, View arg1,
//                                    int position, long arg3) {
//                if (null != images && !images.isEmpty())
//                    Toast.makeText(
//                            getContext(),
//                            "position " + position + " " + images.get(position),
//                            Toast.LENGTH_SHORT).show();
//                ;
//
//            }
//        });
//    }

    private class ImageAdapter extends BaseAdapter {

        /** The context. */
        private Activity context;

        /**
         * Instantiates a new image adapter.
         *
         * @param localContext
         *            the local context
         */
        public ImageAdapter(Activity localContext) {
            context = localContext;
            images = getAllShownImagesPath(context);
        }

        public int getCount() {
            return images.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            ImageView picturesView;
            if (convertView == null) {
                picturesView = new ImageView(context);
                picturesView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                picturesView
                        .setLayoutParams(new GridView.LayoutParams(270, 270));

            } else {
                picturesView = (ImageView) convertView;
            }

            Glide.with(context).load(images.get(position))
                    .placeholder(R.drawable.ic_launcher_background).centerCrop()
                    .into(picturesView);

            return picturesView;
        }

        /**
         * Getting All Images Path.
         *
         * @param activity
         *            the activity
         * @return ArrayList with images Path
         */
        private ArrayList<String> getAllShownImagesPath(Activity activity) {
            Uri uri;
            Cursor cursor;
            int column_index_data, column_index_folder_name;
            ArrayList<String> listOfAllImages = new ArrayList<String>();
            String absolutePathOfImage = null;
            uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

            String[] projection = { MediaStore.MediaColumns.DATA,
                    MediaStore.Images.Media.BUCKET_DISPLAY_NAME };

            cursor = activity.getContentResolver().query(uri, projection, null,
                    null, null);

            column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            column_index_folder_name = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
            while (cursor.moveToNext()) {
                absolutePathOfImage = cursor.getString(column_index_data);

                listOfAllImages.add(absolutePathOfImage);
            }

            Collections.reverse(listOfAllImages);
            return listOfAllImages;
        }
    }

}


