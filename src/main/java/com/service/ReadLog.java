/**
 * This class is used to search a log file for a term and restart
 * the service if the term is found. An example use of this class
 * would be to search an Apache Tomcat log file for 
 * "java.lang.OutOfMemoryError: PermGen space" and restart Tomcat
 * if the message is found.
 */

package com.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

public class ReadLog {

/**
 * Reference to the log4j logger
 */
private static final Logger LOG = Logger.getLogger(ReadLog.class);	

    /**
     * A file name, path and search term are passed to this class at runtime.
     * The file is searched for the term. If the term is found writing()
     * is called and a file is written which signifies the term was found.
     * A wrapper that calls this class will see that file was written
     * and will restart the service.
     */
    public static void main(String[] args) {

        FileInputStream fstream = null;
        BufferedReader reader = null;

        try {
            fstream = new FileInputStream(args[0]);
            reader = new BufferedReader(new InputStreamReader(fstream));
            String line = "";

            while ((line = reader.readLine()) != null)   {

                if (line.indexOf(args[1]) != -1) {
                    writing(args[1]);
                    if (LOG.isDebugEnabled()) {
                        LOG.debug(line);
                    }
                    break;
                }				   				  
            }
        } catch (IOException e) {
            LOG.error("Error: " + e.getMessage());

        } finally {
            IOUtils.closeQuietly(fstream);
            IOUtils.closeQuietly(reader);
        }
    }

    /**
     * This method is used to write a file to the file system to signify
     * the term was found in the log file and the service should be restarted.
     */
    public static void writing(final String searchTerm) {

    Writer writer = null;

        try {  
            writer = new BufferedWriter(new FileWriter("NeedToRestart.txt"));
            writer.write(searchTerm + " found in log file");
            writer.close();
        } catch (IOException e) {
            LOG.error("Problem writing to NeedToRestart.txt");

        } finally {
            IOUtils.closeQuietly(writer);
        }
    }
}
