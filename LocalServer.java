/*
* Local Server implemented using Java which serves most of the files
* Author: satanic-devil
* 
*
*/

import java.io.*;
import java.net.*;

class LocalServer{
	private final int LISTENING_PORT = 9000;
	private final int BUFFER_SIZE = 8192;
	private Socket socket = null;
	private String fileName;
	private InputStream socketInputStream = null;
	private OutputStream socketOutputStream = null;
	private final String SERVER_STARTED_MSG = "SERVER STARTED\nhttp://localhost:" + LISTENING_PORT;
	private StringBuffer requestHeader, responseHeader;
	private int noOfBytes;
	private byte buffer[] = new byte[ BUFFER_SIZE ];
	private boolean displayHeaders = false;

	//Pass an extra argument to see the header details
	public static void main(String args[]){
		new LocalServer( (args.length>0)?true:false );
	}

	LocalServer(boolean displayHeaders){
		this.displayHeaders = displayHeaders;
		start();
	}
	
	
	private void display(String msg){
		System.out.println(msg);
	}

	private void start(){
		
		try( ServerSocket serverSocket = new ServerSocket(LISTENING_PORT)){
		
		display( SERVER_STARTED_MSG + "\n\n");
		while(true){
			
			socket = serverSocket.accept();
			
			display("Connection Request : " + socket.getRemoteSocketAddress());

			getRequestHeader();
			
			if( displayHeaders) display( requestHeader.toString());
		
			sendResponse();
			closeConnection();
			
		}
		}catch(Exception e){
			System.out.println("Error : "  + e.getMessage());
			e.printStackTrace();
		}
	}

	private void getRequestHeader() throws Exception{
		requestHeader = new StringBuffer();
		socketInputStream = socket.getInputStream();
		while( (noOfBytes = socketInputStream.read(buffer)) > 0 ){
			requestHeader.append(new String(buffer, 0, noOfBytes));
			if( requestHeader.indexOf("\r\n\r\n") != -1 ) break;
		}
		
		if( requestHeader.toString().length() != 0 ) getRequestedFileName();
		else display("Invalid Request Header");
	
	}

	private void getRequestedFileName(){
		fileName = requestHeader.toString().split("\\s")[1];
		if( fileName.length() == 1 ) 
			fileName = "index.html";
		else
			fileName = fileName.substring(1);
	}


	private void sendErrorResponse() throws Exception{
		socketOutputStream.write("HTTP/1.1 404 OK\nConnection:close\r\n\r\n".getBytes());
	}

	private void sendResponse() throws Exception{
		socketOutputStream = socket.getOutputStream();
		File file = new File(fileName);
		
		if(displayHeaders) display( "File Name: " + fileName + " Exists : " + file.exists() + "\n");
		if( file.exists() ){
			sendResponseHeader(file.length());
			sendResponseBody(file);
		} else
			sendErrorResponse();
		
	}


	//Make sure the end of the header is terminated by two \r\n\r\n and nothing else
	private void sendResponseHeader(long size) throws Exception{
		String header = "HTTP/1.1 200 OK\nContent-Length:"+size+"\r\n\r\n";

		socketOutputStream.write(header.getBytes());
	}

	private void sendResponseBody(File file) throws Exception{
		
		FileInputStream fileInputStream = new FileInputStream(file);
		while( ( noOfBytes = fileInputStream.read(buffer)) > 0){
			socketOutputStream.write(buffer, 0, noOfBytes);

		}
		
		
		fileInputStream.close();
	}


	private void closeConnection() throws Exception{
		socketOutputStream.flush();
		socketOutputStream.close();
		socketInputStream.close();
		socket.close();	
	}
}

