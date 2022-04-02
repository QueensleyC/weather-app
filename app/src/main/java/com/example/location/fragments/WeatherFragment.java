package com.example.location.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.location.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Objects;

import static android.content.ContentValues.TAG;


public class WeatherFragment extends Fragment implements View.OnClickListener {

    TextView city, date, time, temperature, temperature2, wind, humidity, pressure;
    EditText searchCity;
    ImageView clearSearch;
    RelativeLayout relativeLayout;

    public static final String OPEN_WEATHER_MAP_URL = "https://api.openweathermap.org/data/2.5/weather?q=london&appid=9b01df4b5acd25c7492260ce6b2cbb9a&units=metrics";
    public static final String OPEN_WEATHER_MAP_API = "9b01df4b5acd25c7492260ce6b2cbb9a";


    public WeatherFragment() {
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
        return inflater.inflate(R.layout.fragment_weather, container, false);


    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initialiseViews();

        Toast.makeText(getContext(), "Ensure you're connected to the internet", Toast.LENGTH_LONG).show();

        searchCity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!searchCity.getText().toString().trim().equals("")) {
                    clearSearch.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        clearSearch.setOnClickListener(this);

        searchCity.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                if (i == EditorInfo.IME_ACTION_DONE) {
                    Log.i(TAG, "onEditorAction:  search");
                    if (!searchCity.getText().toString().trim().equals("")) {
                        downloadTask();
                        hideKeyboard(Objects.requireNonNull(getActivity()));
                    } else {
                        Toast.makeText(getContext(), "Enter a city name", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                } else {
                    return false;

                }
            }
        });


    }


    public void initialiseViews() {
        city = Objects.requireNonNull(getActivity()).findViewById(R.id.city);
        date = getActivity().findViewById(R.id.date);
        time = getActivity().findViewById(R.id.time);
        temperature = getActivity().findViewById(R.id.temperature);
        temperature2 = getActivity().findViewById(R.id.temperature2);
        wind = getActivity().findViewById(R.id.wind);
        humidity = getActivity().findViewById(R.id.humidity);
        pressure = getActivity().findViewById(R.id.pressure);
        searchCity = getActivity().findViewById(R.id.edtTextSearch);
        clearSearch = getActivity().findViewById(R.id.closeSearch);
        relativeLayout = getActivity().findViewById(R.id.relativeLayoutProgressBar);

    }

    public void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(getContext().INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.closeSearch) {
            searchCity.getText().clear();
            clearSearch.setVisibility(View.GONE);

        }
    }

    public void downloadTask() {

        class DownloadTask extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                relativeLayout.setVisibility(View.VISIBLE);
            }

            @Override
            protected String doInBackground(String... urls) {

                StringBuilder result = new StringBuilder(); //stores results from json
                URL url; //store url
                HttpURLConnection connection; //

                try {
                    url = new URL(urls[0]); //this will me the url passed to the method @line 300, in this case, openweatherapp.org...

                    connection = (HttpURLConnection) url.openConnection(); //set up url connection

                    InputStream inputStream = connection.getInputStream(); //holds the content of url

                    InputStreamReader reader = new InputStreamReader(inputStream); //reads content of url

                    int jsonData = reader.read(); //keeps track of what has been read

                    while (jsonData != -1) {
                        // the test works because it counts 1,2,3... as long as there is a character.
                        // Once it reaches the end of the json file, it returns -1

                        char current = (char) jsonData; //current character beind read from the file

                        result.append(current); //add it to result

                        jsonData = reader.read(); //moves on to next character
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
                return result.toString();
            }

            @SuppressLint("SetTextI18n")
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);

                relativeLayout.setVisibility(View.GONE);

                Log.i(TAG, "onPostExecute: " + result);

                try {
                    JSONObject jsonObject = new JSONObject(result); //contains elements of json

                    JSONObject main = jsonObject.getJSONObject("main");
                    JSONObject windInfo = jsonObject.getJSONObject("wind");
                    JSONObject sys = jsonObject.getJSONObject("sys");

                    // (char) 0x00B0 is thhe symbol for degree
                    String mtemperature = String.valueOf(main.getDouble("temp")) + (char) 0x00B0 + "C";

                    String mpressure = main.getString("pressure") + "hPa";

                    String mhumidity = main.getString("humidity") + "%";

                    String mcountry = sys.getString("country");

                    String mcity = jsonObject.getString("name");

                    String mwind = windInfo.getString("speed") + "m/s";

                    long cityTimezone = jsonObject.getLong("timezone");

                    getDate(cityTimezone);

                    getDateAndTime(getDate(cityTimezone));

                    //set values gotten to textviews
                    pressure.setText(mpressure);
                    humidity.setText(mhumidity);
                    temperature.setText(mtemperature);
                    temperature2.setText(mtemperature);
                    city.setText(mcity + ", " + mcountry);
                    wind.setText(mwind);


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }
        new DownloadTask().execute("https://api.openweathermap.org/data/2.5/weather?q=" + searchCity.getText().toString().trim() + "&appid=9b01df4b5acd25c7492260ce6b2cbb9a&units=metric");
    }

    public Date getDate(long cityTimezone){
        /*To get the readable time from json we follow the following steps
         * 1. Obtain current local time
         * 2. Find local time offset
         * 3. Obtain current UTC time
         * 4. Obtain destination city's offset in hours and convert to milliseconds
         * 5. Convert to readable format*/

        Date localDate = new Date();
        Log.i(TAG, "onPostExecute: " + localDate);

        /*1*/
        long localTime = localDate.getTime();

        /*2*/
        int localOffset = localDate.getTimezoneOffset() * 60000;

        /*3*/
        long utc = localOffset + localTime;

        /*4*/
        long destinationCity = utc + (1000 * +cityTimezone);

        /*5*/
        Date newDate = new Date(destinationCity);

        Log.i(TAG, "onPostExecutedestinationCityDateAndTime: " + newDate);

        return newDate;
    }

    public void getDateAndTime(Date dateGotten){
        String destinationCityDateAndTime = String.valueOf(dateGotten); //

        String[] splits = destinationCityDateAndTime.split("\\s"); //splits the string based on white spaces

        String mDate = ""; //will stores only date of the city

        /*Loops through the split string*/
        for (int i = 0; i < splits.length; i++) {
            Log.i(TAG, "onPostExecutewww: " + splits[i]);

            if (i == 0 || (i == 1) || (i == 2) || (i == 5)) {
                mDate = mDate.concat(splits[i]);
                if (!(i == 2)) {
                    mDate = mDate.concat(" ");
                } else {
                    mDate = mDate.concat(", ");
                }

            }

            if (i == 3) {
                Log.i(TAG, "onPostExecutettiimmee: " + splits[i]);
                time.setText(splits[i]);
            }
        }
        date.setText(mDate);
    }

}