import java.lang.Thread;
import java.io.IOException;
import java.util.concurrent.DelayQueue;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.InetSocketAddress;


public class DelayedUDPSender extends Thread {

	private DatagramSocket socket;
	private byte[] buffer = new byte[2048];
	private DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
	private DelayQueue<DelayedDatagram> queue;
	private InetAddress targetAddress;
	private int port;


	private MySocketAddress host;
	
	public DelayedUDPSender(DatagramSocket socket, DelayQueue<DelayedDatagram> queue, MySocketAddress host) {
	
		this.queue = queue;
		this.socket = socket;
		this.host = host;
		
	}

	public void run() {
	
		DelayedDatagram datagram;		

		while(true) {
			try {

				datagram = this.queue.take();
				this.packet.setData(datagram.getData());
				this.packet.setLength(datagram.getLength());
				this.packet.setSocketAddress(new InetSocketAddress(host.getAddr(),host.getPort()));
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