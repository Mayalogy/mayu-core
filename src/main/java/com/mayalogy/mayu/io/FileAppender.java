package com.mayalogy.mayu.io;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;

/**
 * This class is open source subject to the Apache 2.0 license available @ http://www.apache.org/licenses/LICENSE-2.0.html
 */
public final class FileAppender {

    public static void appendToFile(final InputStream in, final File f) throws IOException {
        OutputStream stream = null;
        try {
            stream = outStream(f);
            IOUtils.copy(in, stream);
        } finally {
            IOUtils.closeQuietly(stream);
        }
    }

    public static void appendToFile(final String in, final File f) throws IOException {
        InputStream stream = null;
        try {
            stream = IOUtils.toInputStream(in);
            appendToFile(stream, f);
        } finally {
            IOUtils.closeQuietly(stream);
        }
    }

    private static OutputStream outStream(final File f) throws IOException {
        return new BufferedOutputStream(new FileOutputStream(f, true));
    }

    private FileAppender() {}

}