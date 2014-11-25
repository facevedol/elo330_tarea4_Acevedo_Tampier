import java.lang.Thread;
import java.io.IOException;
import java.util.concurrent.DelayQueue;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;

public class DelayedUDPSender extends Thread {
	private DatagramSocket socket;
	private byte[] buffer = new byte[1000];
	private DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
	private DelayQueue<DelayedDatagram> queue;
	private InetAddress targetAddress;

	public DelayedUDPSender(InetAddress addr, DelayQueue<DelayedDatagram> queue) {
		this.queue 	= queue;
		this.targetAddress = addr;
		this.packet.setAddress(this.targetAddress);
		try {
			this.socket = new DatagramSocket();
		} catch(SocketException e) {
      		System.err.println("Can't open socket");
      		System.exit(1);
    	}
	}

	public void run() {
		DelayedDatagram datagram;
		while(true) {
			try {
				datagram = queue.take();
				this.packet.setData(datagram.getData());
				socket.send(this.packet);
			} catch(IOException e) {
				System.err.println("Communication error");
				e.printStackTrace();
    		} catch(InterruptedException e) {
    			System.err.println("Queue error");
    			e.printStackTrace();
    		}
		}
	}
}