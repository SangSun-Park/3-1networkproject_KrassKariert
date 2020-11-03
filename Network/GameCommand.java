package Network;

import GamePlay.GameController;
import GamePlay.Player;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface GameCommand extends Remote
{
	ArrayList<Player> player_List = null;
	public void start(ArrayList<GameServerRunnable> member_List) throws RemoteException;

}
