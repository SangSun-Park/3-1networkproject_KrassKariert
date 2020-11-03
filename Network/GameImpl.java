package Network;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import GamePlay.GameController;
import GamePlay.Player;

import java.rmi.Remote;
import java.util.ArrayList;

public class GameImpl extends UnicastRemoteObject implements GameCommand
{

	static GameController game = null;
	
	protected GameImpl() throws RemoteException
	{
		super();
		// TODO Auto-generated constructor stub
	}
	
	public void start(ArrayList<GameServerRunnable> member_List) throws RemoteException{
		if(member_List.size() < 3 || member_List.size() > 5) {
			System.out.println("Can't play");
			return;
		}
		for(int i = 0; i < member_List.size(); i++) {
			this.player_List.add(new Player());
			//member_List.get(i).num = i;
		}
		this.game = new GameController(this.player_List);
	}
}
