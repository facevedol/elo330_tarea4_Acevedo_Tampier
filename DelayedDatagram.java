import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.net.DatagramPacket;


public class DelayedDatagram implements Delayed {
	
	private int length;
	private byte[] buffer;
	private long timeToSend;

	public DelayedDatagram(byte[] d, long tts, int length){
		this.buffer = d;
		this.timeToSend = tts;
		this.length = length;
	}

	@Override
	public int compareTo(Delayed o) {
		int result = 0;
		if (this.getDelay(TimeUnit.MILLISECONDS) < ((DelayedDatagram)o).getDelay(TimeUnit.MILLISECONDS)) {
			result = -1;
		} else {
			result = 1;
		}
		return result;
	}

	@Override
	public long getDelay(TimeUnit unit) {
		long delay = unit.convert((this.timeToSend - System.currentTimeMillis()), TimeUnit.MILLISECONDS);
		return delay;
	}

	public byte[] getData() {
		return this.buffer;
	}

	public int getLength(){
		return this.length;
	}

}