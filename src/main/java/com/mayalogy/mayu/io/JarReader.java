package com.mayalogy.mayu.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.mayalogy.mayu.misc.CollectionUtil;

public class JarReader extends PathMatchingResourcePatternResolver {
    
    public static BufferedReader getResourceAsReader(final String resourceName) throws IOException {
        return new BufferedReader(new InputStreamReader(getResourceAsInputStream(resourceName)));
    }
    
    public static ObjectInputStream getResourceAsObjectStream(final String resourceName) throws IOException {
        return new ObjectInputStream(getResourceAsInputStream(resourceName));
    }
    
    public static InputStream getResourceAsInputStream(final String resourceName) throws IOException {
        ClassPathResource cpr = new ClassPathResource(resourceName);
        final InputStream in = cpr.getInputStream();

        if (in == null) {
            throw new IOException("Resource not in jar: " + resourceName);
        }
        return in;
    }
    
	public static String[] loadFileNamesWithExtensionInJarResourcePath(String path, String extension) throws IOException {
		List<String> names = new ArrayList<String>();
		PathMatchingResourcePatternResolver rr = new PathMatchingResourcePatternResolver();
		for(Resource r:rr.getResources(path+"/*."+extension)) {
			names.add(r.getFilename());
		}
		return CollectionUtil.convert(names);
	}
	
	public static File addResourceAsFileToTempDir(final String resourceName, File tempDir) throws IOException {
		InputStream is = JarReader.getResourceAsInputStream(resourceName);
		File f = new File(tempDir.getPath()+"/"+resourceName);
		FileUtils.copyInputStreamToFile(is, f);
		return f;
	}
}
