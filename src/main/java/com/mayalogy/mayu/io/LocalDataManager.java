package com.mayalogy.mayu.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import org.apache.commons.io.FileUtils;

/**
 * This class is open source subject to the Apache 2.0 license available @ http://www.apache.org/licenses/LICENSE-2.0.html
 */
public class LocalDataManager {

    public static void serializeAndWriteToFile(String outFile, Object o) throws IOException {
        FileOutputStream fout = new FileOutputStream(outFile+".ser");
        ObjectOutputStream oos = new ObjectOutputStream(fout);
        oos.writeObject(o);
        oos.close();
    }
    
    public static Object readFromJarAndDeserialize(String resourceName) throws ClassNotFoundException, IOException {
        InputStream is = Class.class.getResourceAsStream("/"+resourceName);
        if(is==null) { //Used for when jar is running in a servlet
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            is = classLoader.getResourceAsStream(resourceName);
        }
        ObjectInputStream ois = new ObjectInputStream(is);
        Object oRead = ois.readObject();
        ois.close();
        return oRead;
    }

    public static Object readFromFileAndDeserialize(String inFile) throws ClassNotFoundException, IOException {
        FileInputStream fin = new FileInputStream(inFile);
        ObjectInputStream ois = new ObjectInputStream(fin);
        Object oRead = ois.readObject();
        ois.close();
        return oRead;
    }
    
    public static void writeToFile(List<String> list, File file) throws IOException {
        StringBuilder sb = new StringBuilder();
        for(String w:list) {
            sb.append(w);
            sb.append("\n");
        }
        FileUtils.writeStringToFile(file, sb.toString());
    }
    
    public static BufferedReader openResourceAsReader(final String resourceName) throws IOException {
        return new BufferedReader(new InputStreamReader(openResourceAsInputStream(resourceName)));
    }
    
    public static ObjectInputStream openResourceAsObjectStream(final String resourceName) throws IOException {
        return new ObjectInputStream(openResourceAsInputStream(resourceName));
    }
    
    public static InputStream openResourceAsInputStream(final String resourceName) throws IOException {
        
        final InputStream in = LocalDataManager.class.getResourceAsStream(resourceName);
        if (in == null) {
            throw new FileNotFoundException("File not found: " + resourceName);
        }
        return in;

    }
}
