package edu.gatech.grits.pancakes.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import javolution.util.FastList;

public class Properties {
	
	private String cfgLoc;

	private HashMap<String, String> propertyTable = null;
	
	public Properties(String fileName) {
		String fileSep = System.getProperty("file.separator");
		cfgLoc = fileSep + "cfg" + fileSep;
		if(fileName.endsWith(".props"))
			this.loadFromPlainText(fileName);
		else
			System.err.println("error: invalid configuration file!");
	}
	
	// helpers
	
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
	
	public String getProperty(String key) {
		return propertyTable.get(key);
	}
	
	private final String getPathToFile(String fileName){
		String rootName = System.getProperty("user.dir");
		return rootName + this.cfgLoc + fileName;
	}

	// accessors
	
	public FastList<String> getDevices() {
		FastList<String> list = new FastList<String>();
		String result = getProperty("service.devices.device.list");
		if(result != null) {
			StringTokenizer st = new StringTokenizer(result, ",");
			while(st.hasMoreTokens()) {
				list.add(st.nextToken());
			}
		}
		return list;
	}
	
	public Long getDelayOf(String item){
		String propName = "service.devices.device." + item + ".delay";
		String result = getProperty(propName);
		if(result != null){
			return Long.valueOf(result);
		}
		else{
//			System.err.println("Error: no delay specified for " + item);
			return new Long(0);
		}
	}
	
	public FastList<String> getServices() {
		FastList<String> list = new FastList<String>();
		String result = getProperty("kernel.service.list");
		if(result != null) {
			StringTokenizer st = new StringTokenizer(result, ",");
			while(st.hasMoreTokens()) {
				list.add(st.nextToken());
			}
		}
		return list;
	}
	
	public FastList<String> getClientTasks(){
		FastList<String> tasks = new FastList<String>();
		String result = getProperty("service.client.list");
		if(result != null){
			result = result.trim();
			String[] resultSplit = result.split(",");
			for(String s : resultSplit){
				tasks.add(s);
			}
		}
		return tasks;
	}
	
	public String getID() {
		return propertyTable.get("kernel.id");
	}
	
	public String getBackend() {
		return propertyTable.get("service.devices.backend.type");
	}
	
	public long getDelay(String device) {
		String result = propertyTable.get("service.devices.device." + device + ".delay");
		if(result != null) {
			return new Long(result);
		} else {
			return 0l;
		}
	}
	
	public int getBackendPort() {
		//System.out.println(propertyTable.get("service.backend.port"));
		return new Integer(propertyTable.get("service.devices.backend.port"));
	}
	
	public String getNetworkAddress() {
		return propertyTable.get("service.network.address");
	}
	
	public int getNetworkPort() {
		return new Integer(propertyTable.get("service.network.port"));
	}

	
	// Twitter

	
	public String getTwitterID() {
		return propertyTable.get("service.twitter.username");
	}
	
	public String getTwitterPasswd() {
		return propertyTable.get("service.twitter.password");
	}
	
	// Home location
	public float[] getHomeLocation(){
		String homeLoc = propertyTable.get("kernel.homeloc");
		String[] stringLocs = homeLoc.split(",");
		
		float[] locs = { Float.parseFloat(stringLocs[0]), Float.parseFloat(stringLocs[1]) };
		return locs;
		
	}
}
