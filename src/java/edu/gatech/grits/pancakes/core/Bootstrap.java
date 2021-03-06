package edu.gatech.grits.pancakes.core;

/*
 * In computers, pressing a bootstrap button caused a hardwired program to read
 * a bootstrap program from an input unit and then execute the bootstrap program
 * which read more program instructions and became a self-sustaining process that
 * proceeded without external help from manually entered instructions.
 */

public class Bootstrap {

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		if(args.length == 1 && args[0].endsWith(".props")) {
			System.err.println("Handing off control to Kernel.");
			Kernel kernel = Kernel.newInstance(args[0]);
			kernel.startUp();
		} else {
			System.err.println("usage: java -jar pancakes.jar pancakes.props");
		}
	}

}
