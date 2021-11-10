package com;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class PortFile {
	
	private static String fileName =  System.getProperty("user.dir")+"\\port";
	
	public static int getPort(){
		File file = new File(fileName);
	    BufferedReader reader = null;
	    StringBuffer sbf = new StringBuffer();
	    try {
	        reader = new BufferedReader(new FileReader(file));
	        String tempStr;
	        while ((tempStr = reader.readLine()) != null) {
	            sbf.append(tempStr);
	        }
	        reader.close();
	        return Integer.parseInt(sbf.toString());
	    } catch (IOException e) {
	        e.printStackTrace();
	        sbf = new StringBuffer("5688");
	    } finally {
	        if (reader != null) {
	            try {
	                reader.close();
	            } catch (IOException e1) {
	                e1.printStackTrace();
	            }
	        }
	    }
	    return Integer.parseInt(sbf.toString());
		
	}

}
