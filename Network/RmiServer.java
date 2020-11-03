package Network;

import java.rmi.Naming;
import java.util.ArrayList;

public class RmiServer
{
	private static ArrayList<String> roomList;

	public RmiServer(String server, String roomName)
	{
		try
		{
			GameCommand c = new GameImpl();
			Naming.rebind("rmi://" + server + ":1099/" + roomName, c);
		}
		catch (Exception e)
		{
			System.out.println("TroubleL: " + e);
		}
	}

	public static void MakeRoom(String roomName)
	{
		if (roomList.contains(roomName))
		{

		}
	}

	public static void main(String args[])
	{
		if (args.length != 3)
		{
			System.out.println("Usage: Classname ServerName");
			System.exit(1);
		}
		String server = args[0];

		System.out.println("started at " + server + " and use default port(1099)");
		roomList = new ArrayList<String>();
	}
}
