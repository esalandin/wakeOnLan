import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

// https://it.wikipedia.org/wiki/Wake_on_LAN

public class wakeOnLan {
  	static final String defaultHost="esalandin.ddns.net";
  	static final long iMacEmanueleMACAddr= 0x406c8f1c7378L;

	public static void main(String[] args) {
		final int port= 9;
		String hostName= args.length>0? args[0]: defaultHost;
		byte[] sendBuf = new byte[6+6*16];
		for (int i=0; i<6; ++i)
			sendBuf[i]= (byte)0xFF;
		for (int i=0; i<16; ++i)
     		copyMacAddress(sendBuf, 6+i*6, iMacEmanueleMACAddr);
		InetAddress address= null;
		try {
			address = InetAddress.getByName(hostName);
		} catch (UnknownHostException e) {
			System.err.println("InetAddress.getByName: " + e);
			return;
		}
		DatagramSocket socket= null;
		try {
			socket = new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
			return;
		}		
		DatagramPacket packet = new DatagramPacket(sendBuf, sendBuf.length, address, port);
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
		socket.close();
	}

	private static void copyMacAddress(byte[] a, int index, long macAdd) {
		for (int i=0; i<6; ++i) {
			byte b= (byte) ((macAdd >> (8*(5-i))) & 0xFF);
			a[index+i]= b;
		}
	}
}
