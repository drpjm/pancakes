package edu.gatech.grits.pancakes.util;

import java.io.File;
import java.io.FilenameFilter;

import javax.naming.Context;

import javolution.util.FastList;

public class ClassFinder {

	private final String path;
	private final String userDir;
	private FastList<String> qualPathList;
	
	public ClassFinder(String path){
		this.path = path;
		//TODO: how do we get this to be more flexible? i.e. "classes" may not be the root!
		userDir = System.getProperty("user.dir") + "/classes";
		qualPathList = new FastList<String>();
	}


	public String findQualifiedClassname(String unqualName){
		
		String name = path;
		qualPathList.clear();
		String clientFilePath;
		if(path.contains(".")){
			clientFilePath = path.replace('.', '/');
		}
		else{
			clientFilePath = path;
		}
		
		File rootPkg = new File(userDir + "/" + clientFilePath);
		visitAllDirsAndFiles(rootPkg, unqualName);
		
		for(String s : qualPathList){
			name += "." + s; 
		}
		
		return name + "." + unqualName;
	}
	
	// from exampledepot.com
	private void visitAllDirsAndFiles(File dir, String unqualName) {

		// look for class files in current level

	    if (dir.isDirectory()) {
	        String[] children = dir.list();
	        for (int i=0; i<children.length; i++) {
	        	if(children[i].contains(unqualName)){
	        		qualPathList.add(dir.getName());
	        		return;
	        	}
	            visitAllDirsAndFiles(new File(dir, children[i]), unqualName);
	        }
	    }
	    
	}

	
	
	public FastList<String> getQualPathList() {
		return qualPathList;
	}


	public static void main(String[] args){
//		ClassFinder cf = new ClassFinder("com.rawksolid");
//		String name = cf.findQualifiedClassname("Dummy");
//		System.out.println(name);
//		String name2 = cf.findQualifiedClassname("AnotherDummy");
//		System.out.println(name2);

	}
}
