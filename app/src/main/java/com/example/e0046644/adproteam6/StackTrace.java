package com.example.e0046644.adproteam6;

/**
 * Created by e0046485 on 1/24/2017.
 */

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by sumonmon on 21/12/16.
 */

public class StackTrace {
    public static String trace(Exception ex) {
        StringWriter outStream = new StringWriter();
        ex.printStackTrace(new PrintWriter(outStream));
        return outStream.toString();
    }
}