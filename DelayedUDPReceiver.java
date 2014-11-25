import java.lang.Thread;
import java.io.IOException;
import java.util.concurrent.DelayQueue;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.SocketException;

public class DelayedUDPReceiver extends Thread {
	private DatagramSocket socket;
	private byte[] buffer = new byte[1000];
	private DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
	private DelayQueue<DelayedDatagram> queue;
	private long delay; 

	public DelayedUDPReceiver(int port, long delay, DelayQueue<DelayedDatagram> queue) {
		this.delay 	= delay;
		this.queue 	= queue;
		try {
			this.socket = new DatagramSocket(port);
		} catch(SocketException e) {
      		System.err.println("Can't open socket");
      		System.exit(1);
    	}
	}

	public void run() {
		long timeToSend;
		while(true) {
			try {
				socket.receive(this.packet);
				timeToSend = System.currentTimeMillis() + this.delay;
				queue.put(new DelayedDatagram(this.packet, timeToSend));
			} catch(IOException e) {
				System.err.println("Communication error");
				e.printStackTrace();
    		}
		}
	}
}