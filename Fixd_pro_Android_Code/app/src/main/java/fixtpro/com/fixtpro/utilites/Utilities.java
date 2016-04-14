package fixtpro.com.fixtpro.utilites;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sony on 04-02-2016.
 */
public class Utilities {
    public static boolean hideKeyBoad(Context context, View view) {
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
        return true;
    }
    public static SharedPreferences getSharedPreferences(Context _context){
        return _context.getSharedPreferences(Preferences.FIXIT_PRO_PREFERNCES,Context.MODE_PRIVATE);
    }
    public static long  getTimeInMilliseconds(String Time) throws ParseException {
        String input = Time;
        Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(input);
        long milliseconds = date.getTime();
        return milliseconds;

    }
    // validating email id
    public static boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    public static byte[] convertFileToBytes(File file){
        byte[] b = new byte[(int) file.length()];
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            fileInputStream.read(b);
            for (int i = 0; i < b.length; i++) {
                System.out.print((char)b[i]);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File Not Found.");
            e.printStackTrace();
        }
        catch (IOException e1) {
            System.out.println("Error Reading The File.");
            e1.printStackTrace();
        }
        return  b;
    }
    public static  String Am_PMFormat(String Time){
        if (Time ==  null || Time.equals("null")){
            return "";
        }

        final String time = Time;
        String time1 = null;
        try {
            final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            final Date dateObj = sdf.parse(time);
            System.out.println(dateObj);
            System.out.println(new SimpleDateFormat("k:mm a").format(dateObj));
            time1 = new SimpleDateFormat("k:mm a").format(dateObj);

        } catch (final ParseException e) {
            e.printStackTrace();
        }
        return time1;

    }
    public static String convertDate(String date){
        String date1 = null;
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            final Date dateObj = sdf.parse(date);
            date1  = new SimpleDateFormat("EEE, MMM d, yyyy").format(dateObj);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date1;
    }

    public static LatLng getLocationFromAddress(Context context,String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (Exception ex) {

            ex.printStackTrace();
        }

        return p1;
    }
    public static String timeMedthod(long timeInteval){

        long deltaSeconds = timeInteval ;

        long deltaMiniutes = deltaSeconds/60;


        int value = 0;
        if (deltaSeconds < 60){
            return (int)deltaSeconds+"s";
        }else if (deltaMiniutes < 60){
            return (int)deltaMiniutes+"m";
        }else if (deltaMiniutes < (24*60)){
            value = (int)(deltaMiniutes/60);
            return value +"h";
        }else if (deltaMiniutes < (24 * 60 * 7)){
            value = (int)deltaMiniutes/(60 * 24);
            return value+"d";
        }else if (deltaMiniutes < (24 * 60 * 31)){
            value = (int)(deltaMiniutes/(60 * 24 * 7));
            return value+"w";
        }else if (deltaMiniutes < (24 * 60 * 365.25)){
            value = (int)(deltaMiniutes/(60 * 24 * 30));
            return value+"mo";
        }else {
            value = (int)(deltaMiniutes/(60 * 24 * 365));
            return value+"yr";
        }

    }
}
