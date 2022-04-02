package com.example.location.fragments;

import android.util.Log;

import org.junit.Test;

import java.util.Date;

import static android.content.ContentValues.TAG;
import static org.junit.Assert.*;

public class WeatherFragmentTest {

    @Test
    public void getDate() {

        Date localDate = new Date();
//        Log.i(TAG, "onPostExecute: " + localDate);

        /*1*/
        long localTime = localDate.getTime();

        /*2*/
        int localOffset = localDate.getTimezoneOffset() * 60000;

        /*3*/
        long utc = localOffset + localTime;

        /*4*/
        int cityTimezone = -18000;
        long destinationCity = utc + (1000 * +cityTimezone);

        /*5*/
        Date newDate = new Date(destinationCity);

        assertEquals(newDate, newDate);
    }

    @Test
    public void getDateAndTime() {
    }
}