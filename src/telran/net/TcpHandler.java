package telran.net;

import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class TcpHandler implements Closeable {
	private String host;
	private int port;
	private Socket socket;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	public TcpHandler(String host, int port) throws Exception{
		this.port = port;
		this.host = host;
		connect();
		
	}

	private void connect() throws UnknownHostException, IOException {
		socket = new Socket(host, port);
		output = new ObjectOutputStream(socket.getOutputStream());
		input = new ObjectInputStream(socket.getInputStream());
	}

	@Override
	public void close() throws IOException {
		socket.close();
	}
	
	public <T> T send(String requestType, Serializable requestData){
		Request request = new Request(requestType, requestData);
		boolean running = true;
		while(true) {
			running = false;
		try {
			output.writeObject(request);
			Response response = (Response) input.readObject();
			if(response.code() != ResponseCode.OK) {
				throw new RuntimeException(response.responseData().toString());
			}
			@SuppressWarnings("unchecked")
			T res = (T) response.responseData();
			return res;
		}
		catch (Exception e) {
			if(e instanceof SocketException) {
				running = true;
				try {
					connect();
				}catch(Exception e1) {
					
				}
			}else {
				throw new RuntimeException(e.getMessage());
			}
			
		}
	}
	}

}
