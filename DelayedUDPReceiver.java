
import java.lang.Thread;
import java.io.IOException;
import java.util.concurrent.DelayQueue;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.SocketException;
import java.util.Random;


public class DelayedUDPReceiver extends Thread {

	private DatagramSocket socket;
	private byte[] buffer = new byte[2048];
	private DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
	private DelayQueue<DelayedDatagram> queue;
	private MySocketAddress host;

	
	private float delay_avg, delay_var, drop_rate;

	public DelayedUDPReceiver(DatagramSocket socket, float delay_avg, float delay_var, float drop_rate, DelayQueue<DelayedDatagram> queue, MySocketAddress host) {
		
		this.delay_avg 	= delay_avg;
		this.delay_var 	= delay_var;
		this.drop_rate 	= drop_rate;
		this.queue 		= queue;
		this.socket 	= socket;
		this.host 		= host;
		
	}

	public void run() {
		long timeToSend;
		Random rand = new Random();
		float r = 0;
		while(true) {
			
			try {

				socket.receive(this.packet);
				//System.out.println(new String (this.packet.getData(),"UTF-8"));
				if( (r = rand.nextFloat()) > drop_rate) {
					
					timeToSend = (long)(System.currentTimeMillis() + this.delay_avg + (rand.nextFloat()*2-1)*this.delay_var);
					
					this.host.setFullAddress(this.packet.getAddress(), this.packet.getPort());

					queue.put(new DelayedDatagram(this.packet.getData(), timeToSend, this.packet.getLength()));
				}
				else {
					System.out.println(r);
				}



			} catch(IOException e) {	

				System.err.println("Communication error");
				e.printStackTrace();
			}
		}
	}
}