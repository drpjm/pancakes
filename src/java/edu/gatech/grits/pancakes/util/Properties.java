package edu.gatech.grits.pancakes.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

public class Properties {
	
	private final String CFG_LOC = "/cfg/";

	private HashMap<String, String> propertyTable = null;
	
	public Properties(String fileName) {
		if(fileName.endsWith(".props"))
			this.loadFromPlainText(fileName);
		else
			System.err.println("error: invalid configuration file!");
	}
	
	public void loadFromPlainText(String fileName) {
		String fullFileName = this.getPathToFile(fileName);
		propertyTable = new HashMap<String, String>();	
		
		try {
			
			BufferedReader br = new BufferedReader(new FileReader(fullFileName));
			
			while(br.ready()) {
				String line = br.readLine();
				StringTokenizer st = new StringTokenizer(line, "=");
				ArrayList<String> tokens = new ArrayList<String>(2);
				while(st.hasMoreTokens()) {
					tokens.add(st.nextToken());
				}
				propertyTable.put(tokens.get(0).trim(), tokens.get(1).trim());
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void addProperty(String key, String value) {
		propertyTable.put(key, value);
	}

	public void debug() {
		for(String key : propertyTable.keySet())
			System.out.println(key + " = " + propertyTable.get(key));
	}
	
	
	public ArrayList<String> getDevices() {
		ArrayList<String> list = new ArrayList<String>();
		for(String key : propertyTable.keySet()) {
			if(key.startsWith("devices.device"))
				list.add(propertyTable.get(key));
		}
		return list;
	}
	
	private final String getPathToFile(String fileName){
		String rootName = System.getProperty("user.dir");
		return rootName + this.CFG_LOC + fileName;
	}
	
	// accessors
	
	public String getID() {
		return propertyTable.get("kernel.id");
	}
	
	public int getFrequency() {
		return new Integer(propertyTable.get("kernel.frequency"));
	}
	
	public String getBackend() {
		return propertyTable.get("backend.type");
	}
	
	public int getBackendPort() {
		System.out.println(propertyTable.get("backend.port"));
		return new Integer(propertyTable.get("backend.port"));
	}
	
	public String getNetworkAddress() {
		return propertyTable.get("network.address");
	}
	
	public int getNetworkPort() {
		return new Integer(propertyTable.get("network.port"));
	}
	
	// Twitter
	
	public boolean isTwitterEnabled() {
		if(propertyTable.get("twitter.enabled").equals("true"))
			return true;
		else
			return false;
	}
	
	public String getTwitterID() {
		return propertyTable.get("twitter.username");
	}
	
	public String getTwitterPasswd() {
		return propertyTable.get("twitter.password");
	}
}
