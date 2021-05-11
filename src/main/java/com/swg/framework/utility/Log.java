package com.swg.framework.utility;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Creating log files.
 */
public class Log {
    private static StringBuffer logbuffer = new StringBuffer();

    public static void put(String log) {
        if (logbuffer == null) {
            logbuffer = new StringBuffer();
        }
        logbuffer.append(log);
        logbuffer.append(System.getProperty("line.separator"));
    }

    public static void save(String Reportpath, String filename) {
        try {
            String logpath = Reportpath + "\\" + filename + "_Log.txt";
            File file = new File(logpath);

            // if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(logbuffer.toString());
            bw.close();
            logbuffer = null;
            System.out.println("Done");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
