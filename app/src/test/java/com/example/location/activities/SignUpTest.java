package com.example.location.activities;

import android.util.Patterns;

import org.junit.Test;

import java.util.regex.Pattern;

import static org.junit.Assert.*;

public class SignUpTest {

    SignUp signUp = new SignUp();

    public static final Pattern EMAILPATTERN = Patterns.EMAIL_ADDRESS;


    @Test
    public void validateEmail() {
        // assertThat().isTrue();
        String email = "eby@gmail.com";
        assertTrue(EMAILPATTERN.matcher("eby@gmail.com").matches()) ;
    }

    @Test
    public void validatePassword() {
        assertTrue(signUp.validatePassword("hhH1gt"));
    }

    @Test
    public void validateNames() {
        assertTrue("This works", signUp.validateNames("Hello", "World"));
    }

}