package edu.gatech.grits.pancakes.lang;

public class ControlPacket extends Packet {
	
	private static final long serialVersionUID = 1761628700970106348L;
	private final String control;
	private final String taskName;
	private final long delay;
	
	public static final String RESCHEDULE = "reschedule";

	public ControlPacket(String serviceName, String ctrl, String taskToSchedule) {
		super(serviceName);
		control = ctrl;
		taskName = taskToSchedule;
		delay = 0l;
	}
	
	public ControlPacket(String serviceName, String ctrl, String taskToSchedule, long newDelay) {
		super(serviceName);
		control = ctrl;
		taskName = taskToSchedule;
		delay = newDelay;
	}
	
	public final String getControl() {
		return control;
	}
	
	public final long getDelay() {
		return delay;
	}

	public final String getTaskName() {
		return taskName;
	}

}
