package com.example.szopen.elsreader;

import android.util.Log;

import static android.content.ContentValues.TAG;

/**
 * Created by szopen on 23.01.2018.
 */

public class StudentInfo {
    public String names;
    public String surname;
    public String pesel;
    public String university;
    public String index;

    void print (){
        Log.d("StudentInfo", "Student Info: ");
        Log.d("StudentInfo", "name: "+ this.names);
        Log.d("StudentInfo", "surname: "+this.surname);
        Log.d("StudentInfo", "pesel: "+this.pesel);
        Log.d("StudentInfo", "index: "+this.index);
        Log.d("StudentInfo", "university: "+this.university);
    }
}
