package com.example.firstapp.ui.main;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firstapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;
import static java.sql.DriverManager.println;

/**
 * A placeholder fragment containing a simple view.
 */
public class OneFragment extends Fragment {
        //implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener{
    String JSON_STRING = "{\"person\":[{\"name\":\"Jeesoo Yoon\",\"number\":010-1111-1111},{\"name\":\"Kim Dongha\",\"number\":010-2222-2222}]}";
    //String name, number;
    //TextView personName, personNumber;
    RecyclerView recyclerView;
    List<String> nameList = new ArrayList<String>();
    List<String> numberList = new ArrayList<String>();



    private TextView mTextView;
    private FragmentActivity mFrgAct;
    private Intent mIntent;
    private LinearLayout mLinearLayout;
    private RecyclerAdapter adapter;
    private List<String> Phone_number = new ArrayList<>();
    private List<String> Phone_name = new ArrayList<>();


    /*
     * Defines an array that contains column names to move from
     * the Cursor to the ListView.
     */
//    @SuppressLint("InlinedApi")
//    private final static String[] FROM_COLUMNS = {
//            Build.VERSION.SDK_INT
//                    >= Build.VERSION_CODES.HONEYCOMB ?
//                    ContactsContract.Contacts.DISPLAY_NAME_PRIMARY :
//                    ContactsContract.Contacts.DISPLAY_NAME
//    };
//    /*
//     * Defines an array that contains resource ids for the layout views
//     * that get the Cursor column contents. The id is pre-defined in
//     * the Android framework, so it is prefaced with "android.R.id"
//     */
//    private final static int[] TO_IDS = {
//            android.R.id.text1
//    };
//    // Define global mutable variables
//    // Define a ListView object
//    ListView contactsList;
//    // Define variables for the contact the user selects
//    // The contact's _ID value
//    long contactId;
//    // The contact's LOOKUP_KEY
//    String contactKey;
//    // A content URI for the selected contact
//    Uri contactUri;
//    // An adapter that binds the result Cursor to the ListView
//    private SimpleCursorAdapter cursorAdapter;


    public OneFragment() {
// Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.fragment_one);
//
//        personName = (TextView) findViewById(R.id.name);
        //personName.setText("hi");
//        personNumber = (TextView) findViewById(R.id.number);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

// Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_one, container, false);
        //View root = inflater.inflate(R.layout.fragment_main, null);
        //return root;
        View view = inflater.inflate (R.layout.fragment_one, container, false);
        //personName = (TextView) view.findViewById(R.id.name);
        //personNumber = (TextView) view.findViewById(R.id.number);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        //Phone_name.add("stop error");
        //Phone_number.add("stop error");
        //f (Phone_name.size() > 0) personName.setText("Name: "+ Phone_name.get(0));



        try {
            // get JSONObject from JSON file
            JSONObject obj = new JSONObject(JSON_STRING);
            JSONArray peopleArray = obj.getJSONArray("person");
            // fetch JSONObject named employee
            // get employee name and salary

            for (int i = 0; i<peopleArray.length(); i++){
                JSONObject p = peopleArray.getJSONObject(i);
                nameList.add(p.getString("name"));
                numberList.add(p.getString("number"));

            }

//            name = person.getString("name");
//            number = person.getString("number");
//            // set employee name and salary in TextView's
//            personName.setText("Name: "+name);
//            personNumber.setText("Number: "+number);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ////Read Contacts from phone


        JSONArray personarray = new JSONArray();


        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new RecyclerAdapter();
        recyclerView.setAdapter(adapter);

        gettingPhoneContacts();
        Log.e("NAME: ", Phone_name.toString());
        getData();


        return view;
    }

    public void gettingPhoneContacts() {


        ContentResolver cr = getActivity().getContentResolver();
        // Read Contacts
        Cursor c = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            new String[] { ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.RawContacts.ACCOUNT_TYPE },
                        ContactsContract.RawContacts.ACCOUNT_TYPE + " <> 'google' ",null,  ContactsContract.Contacts.DISPLAY_NAME+ " ASC");
        if (c.getCount() <= 0) {
            Toast.makeText(getActivity(), "No Phone Contact Found..!", Toast.LENGTH_SHORT).show();
            //Phone_name.add("No Contact Found");
            //Phone_number.add("No Contact Found");
        }
        else {
            while (c.moveToNext()) {
                Phone_number.add (c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))); //Phone number
                Phone_name.add(c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));  //Name of contact
                println("hi");
            }

        }
        return;
    }

    public JSONArray cur2Json(Cursor cursor) {

        JSONArray resultSet = new JSONArray();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int totalColumn = cursor.getColumnCount();
            JSONObject rowObject = new JSONObject();
            for (int i = 0; i < totalColumn; i++) {
                if (cursor.getColumnName(i) != null) {
                    try {
                        rowObject.put(cursor.getColumnName(i),
                                cursor.getString(i));
                    } catch (Exception e) {
                        Log.d(TAG, e.getMessage());
                    }
                }
            }
            resultSet.put(rowObject);
            cursor.moveToNext();
        }

        cursor.close();
        return resultSet;

    }

    private void getData() {
        // 임의의 데이터입니다.
//        List<String> listTitle = Arrays.asList("국화", "사막", "수국", "해파리", "코알라", "등대", "펭귄", "튤립",
//                "국화", "사막", "수국", "해파리", "코알라", "등대", "펭귄", "튤립");
//        List<String> listContent = Arrays.asList(
//                "이 꽃은 국화입니다.",
//                "여기는 사막입니다.",
//                "이 꽃은 수국입니다.",
//                "이 동물은 해파리입니다.",
//                "이 동물은 코알라입니다.",
//                "이것은 등대입니다.",
//                "이 동물은 펭귄입니다.",
//                "이 꽃은 튤립입니다.",
//                "이 꽃은 국화입니다.",
//                "여기는 사막입니다.",
//                "이 꽃은 수국입니다.",
//                "이 동물은 해파리입니다.",
//                "이 동물은 코알라입니다.",
//                "이것은 등대입니다.",
//                "이 동물은 펭귄입니다.",
//                "이 꽃은 튤립입니다."
//        );
//        List<Integer> listResId = Arrays.asList(
//                R.drawable.chrysanthemum,
//                R.drawable.desert,
//                R.drawable.hydrangeas,
//                R.drawable.jellyfish,
//                R.drawable.koala,
//                R.drawable.lighthouse,
//                R.drawable.penguins,
//                R.drawable.tulips,
//                R.drawable.chrysanthemum,
//                R.drawable.desert,
//                R.drawable.hydrangeas,
//                R.drawable.jellyfish,
//                R.drawable.koala,
//                R.drawable.lighthouse,
//                R.drawable.penguins,
//                R.drawable.tulips
//        );
        for (int i = 0; i < Phone_name.size(); i++) {
            // 각 List의 값들을 data 객체에 set 해줍니다.
            Data data = new Data();
            //data.setTitle(nameList.get(i));
            //data.setContent(numberList.get(i));
            data.setTitle(Phone_name.get(i));
            data.setContent(Phone_number.get(i));
            //data.setResId(listResId.get(i));

            // 각 값이 들어간 data를 adapter에 추가합니다.
            adapter.addItem(data);
        }

        // adapter의 값이 변경되었다는 것을 알려줍니다.
        adapter.notifyDataSetChanged();
    }
//
//    public void onViewCreated(View view, Bundle savedInstanceState) {
//        // you can add listener of elements here
//          /*Button mButton = (Button) view.findViewById(R.id.button);
//            mButton.setOnClickListener(this); */
//        super
//        mTextView = view.findViewById(R.id.name);
//        mLinearLayout = (LinearLayout)view;
//    }
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        mFrgAct = getActivity();
//        mIntent = mFrgAct.getIntent(); //  Intent intent = new Intent(getActivity().getIntent());
//    }



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
