package com.example.e0046644.adproteam6;

/**
 * Created by e0046485 on 1/24/2017.
 */

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by sumonmon on 21/12/16.
 */

public class JSONParser
{

    static String readStream(InputStream is)
    {
        StringBuilder sb = new StringBuilder();
        try
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            String line = null;
            while ((line = reader.readLine()) != null)
            {
                sb.append(line);
                sb.append('\n');
            }
            is.close();
        } catch (Exception e)
        {
            Log.e("readStream Exception", StackTrace.trace(e));
        }
        return (sb.toString());
    }

    public static String getStream(String url,String token)
    {
        InputStream is = null;
        try
        {
            URL u = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Token", token);
            conn.connect();
            is = conn.getInputStream();
        } catch (UnsupportedEncodingException e)
        {
            Log.e("getStream Exception", StackTrace.trace(e));
        } catch (Exception e)
        {
            Log.e("getStream Exception", StackTrace.trace(e));
        }
        return readStream(is);
    }

    public static String postStream(String url, String data,String token)
    {
        InputStream is = null;
        try
        {
            URL u = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-type", "application/json");
            conn.setRequestProperty("Token",token);
            conn.setFixedLengthStreamingMode(data.getBytes().length);
            conn.connect();
            OutputStream os = new BufferedOutputStream(conn.getOutputStream());
            os.write(data.getBytes());
            os.flush();
            is = conn.getInputStream();
        } catch (UnsupportedEncodingException e)
        {
            Log.e("postStream Exception", StackTrace.trace(e));
        } catch (Exception e)
        {
            Log.e("postStream Exception", StackTrace.trace(e));
        }
        return readStream(is);
    }

    public static JSONObject getJSONFromUrl(String url,String token)
    {
        JSONObject jObj = null;
        try
        {
            jObj = new JSONObject(getStream(url,token));
        } catch (JSONException e)
        {
            Log.e("Exception", StackTrace.trace(e));
        }
        return jObj;
    }

    public static JSONArray getJSONArrayFromUrl(String url,String token)
    {
        JSONArray jArray = null;
        try
        {
            jArray = new JSONArray(getStream(url,token));
        } catch (JSONException e)
        {
            Log.e("Exception", StackTrace.trace(e));
        }
        return jArray;
    }
}