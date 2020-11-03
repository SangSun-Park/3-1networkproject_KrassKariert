package Network;

import java.io.*;
import java.net.*;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.Naming;
import java.util.ArrayList;

import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocketFactory;

public class GameServer implements Runnable
{
	private ArrayList<String> roomList = new ArrayList<String>();
	private ArrayList<GameServerRunnable> clients = new ArrayList<GameServerRunnable>();

	private static int port = 1099;
	private static int rmiport = 1199;

	
	public GameServer()
	{
		try {
		}catch(Exception e) {
			System.out.println("Trouble: " + e);
		}
	}

	public void SayClient(int clientID, String inputLine, boolean sayEveryone)
	{
		for (int i = 0; i < clients.size(); i++)
		{
			if (sayEveryone == false && clients.get(i).GetClientID() != clientID) continue;
			clients.get(i).out.println(inputLine);
		}
	}

	public void PrintRoomList(int clientID)
	{
		int index = -1;
		for (int i = 0; i < clients.size(); i++)
		{
			if (clients.get(i).GetClientID() == clientID)
			{
				index = i;
				break;
			}
		}
		if (index < 0) return;

		if (roomList.size() == 0)
		{
			clients.get(index).out.println("No room in server");
			return;
		}
		for (int i = 0; i < roomList.size(); i++)
		{
			clients.get(index).out.println((i + 1) + ". [" + roomList.get(i) + "]");
		}
	}

	// 유저 단위로 방이 나뉘어져야 하므로 방을 만들거나 들어감.
	public void JoinRoom(int clientID, String roomName)
	{
		int index = -1;
		for (int i = 0; i < clients.size(); i++)
		{
			if (clients.get(i).GetClientID() == clientID)
			{
				index = i;
				break;
			}
		}
		if (index < 0) return;

		if (roomList.contains(roomName))
		{
			// 방에 입장함
			clients.get(index).out.println("Join" + roomName);
		}
		else
		{
			// 방을 추가해줌
			roomList.add(roomName);
			SayClient(-1, "Room [" + roomName + "] is created.", true);
		}
	}

	@Override
	public void run()
	{
		// TODO Auto-generated method stub
		ServerSocket serverSocket = null;
		try
		{
			//ServerSocketFactory serverSocketFactory = SSLServerSocketFactory.getDefault();
			//serverSocket = serverSocketFactory.createServerSocket(port);
			serverSocket = new ServerSocket(port);
			System.out.println("Server started: socket created on " + port);

			while (true)
			{
				AddClient(serverSocket);
			}
		}
		catch (BindException be)
		{
			System.out.println("Can't bind on: " + port);
		}
		catch (IOException ie)
		{
			System.out.println(ie);
		}
		finally
		{
			try
			{
				if (serverSocket != null) serverSocket.close();

			}
			catch (IOException ie)
			{
				System.out.println(ie);
			}
		}
	}

	public void ClientSay(int clientID, String inputLine)
	{
		for (int i = 0; i < clients.size(); i++)
		{
			if (clients.get(i).GetClientID() == clientID)
			{
				System.out.println("Writer: " + clientID + "Say: " + inputLine);
			}
			else
			{
				System.out.println("Write " + clients.get(i).GetClientID() + ": " + inputLine);
				clients.get(i).out.println(inputLine);
			}
		}
	}

	public void AddClient(ServerSocket server)
	{
		Socket clientSocket = null;

		try
		{
			clientSocket = server.accept();
		}
		catch (IOException ie)
		{
			System.out.println("Accept fail: " + ie);
		}
		GameServerRunnable newClient = new GameServerRunnable(this, clientSocket);
		clients.add(newClient);
		int index = clients.size() - 1;
		new Thread(clients.get(index)).start();
		System.out.println("Client connected: " + clientSocket.getPort() + ", CurrentClient: " + clients.size());
	}

	public synchronized void RemoveClient(int clientID)
	{
		GameServerRunnable endClient = null;
		for (int i = 0; i < clients.size(); i++)
		{
			if (clients.get(i).GetClientID() == clientID)
			{
				endClient = clients.get(i);
				clients.remove(i);
				System.out.println(
						"Client removed: " + clientID + " at clients[" + i + "], CurrentClient: " + clients.size());
				endClient.close();
			}
		}
	}
	
	public void gamestart() throws RemoteException {

	}
	
	public static void startRegistry(int rmiPort) throws RemoteException{
		try {
			Registry registry = LocateRegistry.getRegistry(rmiPort);
			registry.list();
		}catch(RemoteException re) {
			System.out.println("RMI registry is not located at port " + rmiPort);
			Registry registry = LocateRegistry.createRegistry(rmiPort);
			System.out.println("RMI registry created at port " + rmiPort);
		}
	}

	public static void main(String[] args) throws IOException
	{
		/*if (args.length != 1)
		{
			System.out.println("Usage: Classname Serverport");
			System.exit(1);
		}
		int port = Integer.parseInt(args[0]);*/
		new Thread(new GameServer()).start();
		try {
			startRegistry(rmiport);
			GameCommand g = new GameImpl();
			Naming.rebind("rmi//localhost:1199/GameCommand", g);
		}catch(RemoteException re) {
			
		}

	}
}

class GameServerRunnable implements Runnable
{
	protected GameServer gameServer = null;
	protected Socket clientSocket = null;
	protected PrintWriter out = null;
	protected BufferedReader in = null;

	public int clientID = -1;

	public GameServerRunnable(GameServer server, Socket socket)
	{
		this.gameServer = server;
		this.clientSocket = socket;
		clientID = clientSocket.getPort();

		try
		{
			out = new PrintWriter(clientSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));	
		}
		catch (IOException ie)
		{
			System.out.println(ie);
		}
	}

	@Override
	public void run()
	{
		// TODO Auto-generated method stub
		try
		{

			String inputLine;
			while ((inputLine = in.readLine()) != null)
			{
				if (inputLine.equalsIgnoreCase("-exit")) break;
				if(inputLine.equalsIgnoreCase("-start")) gameServer.gamestart();
				if (inputLine.equalsIgnoreCase("-list")) gameServer.PrintRoomList(GetClientID());
				else if (inputLine.equalsIgnoreCase("-help") == false)
				{
					String[] parseChat = inputLine.split("\\s");

					if (parseChat[0].equalsIgnoreCase("-join"))
					{
						if (parseChat.length == 2)
						{
							gameServer.JoinRoom(GetClientID(), parseChat[1]);
						}
						else
						{
							gameServer.SayClient(GetClientID(), "Command Error. -join (RoomName)", false);
						}
						continue;
					}
					gameServer.ClientSay(GetClientID(), GetClientID() + ": " + inputLine);
				}
			}
			gameServer.RemoveClient(GetClientID());
		}
		catch (IOException ie)
		{
			gameServer.RemoveClient(GetClientID());
		}
	}

	public int GetClientID()
	{
		return clientID;
	}

	public void close()
	{
		try
		{
			if (in != null) in.close();
			if (out != null) out.close();
			if (clientSocket != null) clientSocket.close();
		}
		catch (IOException ie)
		{
			System.out.println(ie);
		}
	}

	

}