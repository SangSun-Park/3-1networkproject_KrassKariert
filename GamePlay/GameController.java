package GamePlay;

import java.util.ArrayList;
import java.util.Random;

public class GameController
{
	private ArrayList<Card> cardDeck = new ArrayList<Card>();
	private int curTurn;
	private int numberOfPlayer;

	private int curWinPlayer;
	private Combination curCom;

	private ArrayList<Player> playerList = new ArrayList<Player>();

	int round;

	public GameController(ArrayList<Player> playerList)
	{
		round = 0;
		this.playerList = playerList;
		numberOfPlayer = this.playerList.size();
		curTurn = 1;
		curWinPlayer = -1;
		curCom = null;

		for (int i = 0; i < numberOfPlayer; i++) {
			playerList.get(i).InitializePlayer(i);
		}		
		InitializeGame();
	}

	// ���� ó�� ������ ������ �� �ʱ�ȭ ����. �÷��̾� �����鵵 �Բ� �ʱ�ȭ
	public void InitializeGame()
	{
		ShuffleDeck();
		int handAmt = (numberOfPlayer == 5 ? 7 : 10);
		for (int i = 0; i < numberOfPlayer; i++)
			SetPlayerHands(i, handAmt);
		round++;
	}

	private void SetPlayerHands(int playerNumber, int handAmount)
	{
		Player player = playerList.get(playerNumber);
		player.hands.clear();
		for (int i = 0; i < handAmount; i++)
		{
			Card card = DrawCard();
			player.hands.add(card);
		}

		for (int i = 0; i < 2; i++)
		{
			// ������ �����ī�� �߰�
			Card card = DrawCard();
			player.spare[i] = card;
		}
	}

	// ������ ������ �� ó���� ���� ������� �ϹǷ� ���� �ʱ�ȭ �� ���� �������� ���ġ��
	private void ShuffleDeck()
	{
		cardDeck.clear();

		ArrayList<Card> tmpDeck = new ArrayList<Card>();
		for (int i = 0; i < 4; i++)
		{
			for (int n = 1; n <= 12; n++)
			{
				tmpDeck.add(new Card(Card.CardType.number, n));
			}
		}
		for (int i = 0; i < 2; i++)
		{
			tmpDeck.add(new Card(Card.CardType.joker, -1));
			tmpDeck.add(new Card(Card.CardType.stop, -1));
			tmpDeck.add(new Card(Card.CardType.redraw, -1));
		}

		long seed = System.currentTimeMillis();
		Random rand = new Random(seed);
		for (int i = tmpDeck.size(); i > 0; i--)
		{
			int index = rand.nextInt(i);
			cardDeck.add(tmpDeck.get(index));
			tmpDeck.remove(index);
		}
	}

	// ������ ī�带 �̾Ƽ� ��ȯ
	public Card DrawCard()
	{
		int index = cardDeck.size() - 1;
		Card tmp = cardDeck.get(index);
		cardDeck.remove(index);

		return tmp;
	}
}
