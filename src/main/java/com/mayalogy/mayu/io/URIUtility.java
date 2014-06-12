package com.mayalogy.mayu.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class URIUtility {
    
    public static String getUrlContents( URL url ) throws IOException {
        BufferedReader bReader = null;
        String contents = null;
        try {
            StringBuilder buf = new StringBuilder();
            bReader = new BufferedReader(new InputStreamReader(url.openStream()));

            String inputLine;
            while ((inputLine = bReader.readLine()) != null) {
                buf.append(inputLine);
            }
            contents = buf.toString();
        } finally {
            if ( bReader != null )
                bReader.close();
        }
        return contents;
    }
}
