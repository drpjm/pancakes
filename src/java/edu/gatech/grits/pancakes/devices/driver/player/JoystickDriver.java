package edu.gatech.grits.pancakes.devices.driver.player;

import java.io.IOException;
import java.io.InputStream;
import java.util.StringTokenizer;

import edu.gatech.grits.pancakes.core.Kernel;
import edu.gatech.grits.pancakes.core.Stream.CommunicationException;
import edu.gatech.grits.pancakes.devices.backend.Backend;
import edu.gatech.grits.pancakes.devices.driver.HardwareDriver;
import edu.gatech.grits.pancakes.lang.CoreChannel;
import edu.gatech.grits.pancakes.lang.JoystickPacket;
import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

public class JoystickDriver implements HardwareDriver<JoystickPacket> {
	
	public JoystickDriver(Backend backend) {
		Kernel.getInstance().getSyslog().debug("Loading joystick driver...");
		try {
			this.connect("/dev/ttyUSB0");
			Kernel.getInstance().getSyslog().debug("done.");
		} catch (Exception e) {
			Kernel.getInstance().getSyslog().error("Unable to connect to serial joystick.");
		}
	}
	
    public void connect ( String portName ) throws Exception
    {
        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
        if ( portIdentifier.isCurrentlyOwned() )
        {
            System.out.println("Error: Port is currently in use");
        }
        else
        {
            CommPort commPort = portIdentifier.open(this.getClass().getName(),2000);
            
            if ( commPort instanceof SerialPort )
            {
                SerialPort serialPort = (SerialPort) commPort;
                serialPort.setSerialPortParams(9600,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
                
                InputStream in = serialPort.getInputStream();
                //OutputStream out = serialPort.getOutputStream();
                               
                //(new Thread(new SerialWriter(out))).start();
                
                serialPort.addEventListener(new SerialReader(in));
                serialPort.notifyOnDataAvailable(true);

            }
            else
            {
                System.out.println("Error: Only serial ports are handled by this example.");
            }
        }     
    }
    
    /**
     * Handles the input coming from the serial port. A new line character
     * is treated as the end of a block in this example. 
     */
    public class SerialReader implements SerialPortEventListener 
    {
        private InputStream in;
        private byte[] buffer = new byte[1024];
        
        public SerialReader ( InputStream in )
        {
            this.in = in;
        }
        
        public void serialEvent(SerialPortEvent arg0) {
            int data;
          
            try
            {
                int len = 0;
                while ( ( data = in.read()) > -1 )
                {
                    if ( data == '\n' ) {
                        break;
                    }
                    buffer[len++] = (byte) data;
                }
//                Kernel.getInstance().getSyslog().debug(new String(buffer,0,len-1));
                
                // parse & publish
                String output = new String(buffer,0,len-1);
                output.trim();
                StringTokenizer st = new StringTokenizer(output, ",");
                if(st.countTokens() == 4) {
                	JoystickPacket pkt = new JoystickPacket();
                	pkt.setPositionX(Integer.parseInt(st.nextToken()));
                	pkt.setPositionY(Integer.parseInt(st.nextToken()));
                	pkt.setPushButton1(Integer.parseInt(st.nextToken()));
                	pkt.setPushButton2(Integer.parseInt(st.nextToken()));
                	
                	try {
						Kernel.getInstance().getStream().publish(CoreChannel.SYSTEM, pkt);
					} catch (CommunicationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                } else {
                	System.err.println("unknown number (" + st.countTokens() + ") of tokens");
                }
            }
            catch ( IOException e )
            {
                e.printStackTrace();
                System.exit(-1);
            }             
        }

    }
	
	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public JoystickPacket query() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void request(JoystickPacket pkt) {
		// TODO Auto-generated method stub
		
	}

}
