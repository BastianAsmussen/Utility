package tech.asmussen.util;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * A class that provides a simple way to print values to the console fast, in
 * bulk.
 *
 * @author Bastian Asmussen
 * @version 1.0.0
 * @see #println(Object...)
 */
public class FastPrinter {

    /**
     * Print values to the console.
     *
     * <p>
     * It is recommended that you use compile the values to print to an Object[]
     * array and pass it to this method.
     * If compiled to an Object[] array the method will be orders of magnitude
     * faster than System.out.println().
     * </p>
     *
     * @param objects For each object a new line will be printed.
     * @since 1.0.0
     */
    public static void println(Object... objects) {

        // Create a new BufferedWriter to write to the console, using the US ASCII
        // charset and a buffer size of 512.
        BufferedWriter out = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(FileDescriptor.out), StandardCharsets.US_ASCII), 512);

        try {

            for (Object o : objects) {

                out.write(o.toString()); // Write the string value of the object to the buffer.
                out.newLine(); // Write a new line to the buffer.
            }

            out.flush(); // Flush the buffer to the console.

        } catch (IOException e) {

            e.printStackTrace();
        }
    }
}
