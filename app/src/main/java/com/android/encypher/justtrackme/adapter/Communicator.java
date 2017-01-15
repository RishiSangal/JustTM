package com.android.encypher.justtrackme.adapter;

/**
 * Created by gipsy_danger on 14/6/16.
 */
public interface Communicator {
    void response(String i);
    void emial(String i);
    void emial(String i,String j);
    void email(String email, String phone, String s, String s1, String s2, String s3,String country);

    void floatToConatct(String home,String circle_id);

}
