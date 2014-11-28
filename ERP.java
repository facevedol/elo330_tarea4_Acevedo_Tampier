import java.lang.Thread;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.SocketException;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.DelayQueue;
import java.net.InetAddress;
import java.net.DatagramSocket;
import java.lang.InterruptedException;



public class ERP {

	private static DelayQueue<DelayedDatagram> queue;
	private static DelayQueue<DelayedDatagram> queue2;

	private static DelayedUDPReceiver receive_from_client;
	private static DelayedUDPSender send_to_client;
	private static DelayedUDPReceiver receive_from_server;
	private static DelayedUDPSender send_to_server;

	private static DatagramSocket client_socket;
	private static DatagramSocket server_socket;

	private static int port1 = 1234;
	private static int port2 = 12345;
	private static float delay_avg, delay_var, drop_rate;
	private static InetAddress addr;
	private static MySocketAddress serverHost, clientHost, emptyHost;	


	public static void main(String[] args){
		if(args.length == 6) {
			delay_avg 	= Float.valueOf(args[0]);
			delay_var 	= Float.valueOf(args[1]);
			drop_rate 	= Float.valueOf(args[2]);
			port1 		= Integer.valueOf(args[3]);
			port2		= Integer.valueOf(args[5]);
			try{
				addr = InetAddress.getByName(args[4]);
			}
			catch(IOException e){
			}

		} else if(args.length == 5){
			delay_avg 	= Float.valueOf(args[0]);
			delay_var 	= Float.valueOf(args[1]);
			drop_rate 	= Float.valueOf(args[2]);
			port1 		= Integer.valueOf(args[3]);
			port2		= Integer.valueOf(args[4]);
			try{
				addr = InetAddress.getByName("localhost");
			}
			catch(IOException e){
			}
		}
		else{
			System.out.println("Wrong parameters");
			return;
		}


		queue = new DelayQueue<DelayedDatagram>();
		queue2 = new DelayQueue<DelayedDatagram>();
		serverHost = new MySocketAddress();
		clientHost = new MySocketAddress();
		emptyHost = new MySocketAddress();

		serverHost.setFullAddress(addr,port2);
	
		try {
			client_socket = new DatagramSocket(port1);
			server_socket = new DatagramSocket();

		} catch(SocketException e) {
			System.err.println("Can't open socket in receiver");
			System.exit(1);
		}

		receive_from_client = new DelayedUDPReceiver(client_socket, delay_avg, delay_var, drop_rate, queue, clientHost);
		send_to_server = new DelayedUDPSender(server_socket, queue, serverHost);

		receive_from_server = new DelayedUDPReceiver(server_socket,delay_avg, delay_var, drop_rate, queue2, emptyHost);
		send_to_client = new DelayedUDPSender(client_socket, queue2, clientHost);

		
		receive_from_client.start();
		send_to_server.start();
		receive_from_server.start();
		send_to_client.start();

	}
}