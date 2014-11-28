import java.net.InetAddress;

public class MySocketAddress{

	private InetAddress addr; 
	private int port;

	public MySocketAddress(){

		this.port = 0;
	}

	public void setFullAddress(InetAddress addr, int port){
		this.addr = addr;
		this.port = port;
	}
	public int getPort(){
		return this.port;
	}
	public InetAddress getAddr(){
		return this.addr;
	}

}