package Network;

import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.net.InetAddress;
import javax.net.SocketFactory;
import GamePlay.Player;

public class GameClient
{
	static String server = "localhost";
	static int port = 1099;
	static Socket clientSocket = null;
	static SocketFactory socketFactory = SocketFactory.getDefault();
	String roomName = "";
	
	public static void main(String[] args)
	{
		/*if (args.length != 2)
		{
			System.out.println("Usage: Classname Servername Serverport");
			System.exit(1);
		}
		
		server = args[0];
		port = Integer.parseInt(args[1]);
		*/
		try
		{
			//clientSocket = socketFactory.createSocket(server, port);
			clientSocket = new Socket(server, port);
		}
		catch (BindException b)
		{
			System.out.println("Can't bind on: " + port);
			System.exit(1);
		}
		catch (IOException ie)
		{
			System.out.println(ie);
			System.exit(1);
		}
		new Thread(new ClientReceiver(clientSocket)).start();
		new Thread(new ClientSender(clientSocket)).start();
	}
}

class ClientSender implements Runnable
{
	private Socket clientSocket = null;

	ClientSender(Socket socket)
	{
		this.clientSocket = socket;
	}

	@Override
	public void run()
	{
		// TODO Auto-generated method stub
		Scanner keyIn = null;
		PrintWriter out = null;
		try
		{
			keyIn = new Scanner(System.in);
			out = new PrintWriter(clientSocket.getOutputStream(), true);

			String userInput = "";
			System.out.println("Your ID is " + clientSocket.getLocalPort());
			System.out.println("Please Type '-help' to show command");
			while ((userInput = keyIn.nextLine()) != null)
			{
				out.println(userInput);
				out.flush();
				if (userInput.equalsIgnoreCase("-exit")) break;
				if (userInput.equalsIgnoreCase("-help"))
				{
					System.out.println("'-exit' to leave");
					System.out.println("'-list' to leave");
					System.out.println("'-join (name)' to join or make room");
				}
			}
			keyIn.close();
			out.close();
			clientSocket.close();
		}
		catch (IOException ie)
		{
			try
			{
				if (out != null) out.close();
				if (keyIn != null) keyIn.close();
				if (clientSocket != null) clientSocket.close();
			}
			catch (IOException e)
			{
				System.out.println(e);
			}
		}
	}

}

class ClientReceiver implements Runnable
{
	private Socket clientSocket = null;

	ClientReceiver(Socket socket)
	{
		this.clientSocket = socket;
	}

	@Override
	public void run()
	{
		// TODO Auto-generated method stub
		while (clientSocket.isConnected())
		{
			BufferedReader in = null;
			try
			{
				in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				String readSome = null;
				while ((readSome = in.readLine()) != null)
				{
					System.out.println(readSome);
				}
				in.close();
				clientSocket.close();
			}
			catch (IOException ie)
			{
				System.out.println(ie);
			}
			System.out.println("Leave.");
			System.exit(1);
		}

	}
}
