package telran.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class TcpServer implements Runnable {
public static final int IDLE_TIMEOUT = 100;
private ServerSocket serverSocket;
private int port;
private ApplProtocol protocol;
boolean isShutdown = false;
int nThreads = Runtime.getRuntime().availableProcessors();
ExecutorService threadPool = Executors.newFixedThreadPool(nThreads);
AtomicInteger clientCounter = new AtomicInteger(0);
public TcpServer(int port, ApplProtocol protocol) throws IOException{
	this.port = port;
	this.protocol = protocol;
	serverSocket = new ServerSocket(port);
	serverSocket.setSoTimeout(IDLE_TIMEOUT);
}
public void shutdown() {
	threadPool.shutdown();
	isShutdown = true;
}
	@Override
	public void run() {
		System.out.println("Server is listening on port " + port);
		while(!isShutdown) {
			try {
			Socket socket = serverSocket.accept();
			TcpClientServer clientServer =
					new TcpClientServer(socket, protocol, this);
			clientCounter.incrementAndGet();
			if(!isShutdown) {
				threadPool.execute(clientServer);
			}
			} catch(SocketTimeoutException e) {
				
			}
			catch(Exception e) {
				e.printStackTrace();
				break;
			}
			
		}
		
	
}
}
