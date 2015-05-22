package edu.gatech.grits.pancakes.lang;

import edu.gatech.grits.pancakes.service.ControlOption;

/**
 * This class provides the necessary data to send control signals to the Services
 * or Tasks running on the deployed system.
 * 
 * @author patrickmartin
 *
 */
public class ControlPacket extends Packet {
	
	private static final long serialVersionUID = 1761628700970106348L;
	private final ControlOption ctrlOpt;
	private final String componentToControl;
	private final String reqComponent;
	private final long delay;
	
	public ControlPacket(ControlOption ctrl, String targetComponent, String reqComponent) {
		super("control");
		this.ctrlOpt = ctrl;
		this.componentToControl = targetComponent;
		this.reqComponent = reqComponent;
		this.delay = 0l;
	}
	
	public ControlPacket(ControlOption ctrl, String targetComponent, String reqComponent, long newDelay) {
		super("control");
		this.ctrlOpt = ctrl;
		this.componentToControl = targetComponent;
		this.reqComponent = reqComponent;
		this.delay = newDelay;
	}
	
	public final ControlOption getControl() {
		return ctrlOpt;
	}
	
	public final long getDelay() {
		return delay;
	}

	public final String getComponentToControl() {
		return componentToControl;
	}

	public String getReqComponent() {
		return reqComponent;
	}

}
