package telran.net;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class TcpClientServer implements Runnable {
Socket socket;
ObjectInputStream input;
ObjectOutputStream output;
ApplProtocol protocol;
TcpServer tcpServer;
final static int TOTAL_IDLE_TIMEOUT=30000;
int idleTime = 0;
public TcpClientServer(Socket socket, ApplProtocol protocol, TcpServer tcpServer) throws IOException {
	this.socket = socket;
	this.socket.setSoTimeout(tcpServer.IDLE_TIMEOUT);
	input = new ObjectInputStream(socket.getInputStream());
	output = new ObjectOutputStream(socket.getOutputStream());
	this.protocol = protocol;
	this.tcpServer = tcpServer;
}
	@Override
	public void run() {
		while(!tcpServer.isShutdown) {
			try {
				Request request = (Request) input.readObject();
				Response response = protocol.getResponse(request);
				output.writeObject(response);
			}catch(SocketTimeoutException e) {
				idleTime += tcpServer.IDLE_TIMEOUT;
				if(idleTime > TOTAL_IDLE_TIMEOUT && 
						tcpServer.clientCounter.get() > tcpServer.nThreads) {
					try {
						socket.close();
					}catch(IOException e1) {
						e1.printStackTrace();
					}
					if(tcpServer.isShutdown) {
						try {
							socket.close();
						}catch(IOException e2) {
							e2.printStackTrace();
						}
						System.out.println("socket closed - server has been shutdown");
						break;
					}
				}}
				catch(EOFException e) {
					System.out.println("client closed normally connection");
					break;
				} catch(Exception e) {
					System.out.println("client closed abnormally connection "
				+ e.getMessage());
					break;
				}
				
				
			}tcpServer.clientCounter.decrementAndGet();
			
		

	}

}