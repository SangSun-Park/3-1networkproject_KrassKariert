package Network;

public class RmiClient
{
	public static void main(String args[])
	{
		if(args.length != 2) {
			System.out.println("Usage: Classname Servername RoomName");
			System.exit(1);
		}
		
		String server = args[0];
		String gameRoom = args[1];
	}
}
