package GamePlay;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Player
{
	public ArrayList<Card> hands;
	public Card[] spare;
	public int life;
	public int playerTurnNumber;

	public Player()
	{
		hands = new ArrayList<Card>();
		spare = new Card[2];
	}
	
	// ������ ù ���۽� �÷��̾ �ʱ�ȭ ����.
	public void InitializePlayer(int turn)
	{
		life = 2;
		playerTurnNumber = turn;
	}
	
	 // ī�带 ������ ���з� �߰���. �̋� ī���� ��ġ�� ����.
	public void AddCard(Card card)
	{
		int i = -1;
		Scanner sc = new Scanner(System.in);
		while (true)
		{
			try
			{
				i = sc.nextInt();
				if (i < 1 || i > hands.size())
				{
					System.out.println(card.CardName() + "ī�� �Դϴ�. ���� ��� ���� �������?");
					System.out.println("1~" + hands.size() + "�� ���ڸ� �Է����ּ���.");
					continue;
				}
				break;
			}
			catch (InputMismatchException ie)
			{
				System.err.println("���ڸ� �Է����ּ���.");
				sc.nextLine();
			}
		}
		hands.add(i, card);
	}
	
	// �տ� � �а� �ִ��� ǥ������.
	public void PrintHands()
	{
		System.out.print("Index: ");
		for (int i = 0; i < spare.length; i++)
		{
			System.out.print(String.format(" %2d ", i + 1));
		}
		System.out.println();

		System.out.print("Spare: ");
		for (int i = 0; i < spare.length; i++)
		{
			if (spare[i] == null) System.out.print("  - ");
			else System.out.print(spare[i].CardName());
		}
		System.out.println();

		System.out.print("Index: ");
		for (int i = 0; i < hands.size(); i++)
		{
			System.out.print(String.format(" %2d ", i + 1));
		}
		System.out.println();

		System.out.print("Hands: ");
		for (int i = 0; i < hands.size(); i++)
		{
			System.out.print(hands.get(i).CardName());
		}
		System.out.println();
	}
	
	// ���� ���� ī�带 ������.
	public void SelectCardList(int startIndex, int endIndex)
	{
		if ((endIndex - startIndex) >= 3)
		{
			System.out.println("ī��� �ִ� 3������� �� �� �ֽ��ϴ�.");
			return;
		}

		boolean hasJoker = false;
		int jokerNumber = -1;
		ArrayList<Card> cardList = new ArrayList<Card>();
		for (int i = startIndex - 1; i < endIndex; i++)
		{
			Card card = hands.get(i);
			Card.CardType type = card.GetType();

			if (type == Card.CardType.joker)
			{
				if (hasJoker == false) // ���� ��Ŀ�� �߰��� ���� ���ٸ� ��Ŀ�� ���ڸ� ������ ��.
				{
					System.out.println("��Ŀ�� ���ڸ� �������ּ���.");
					int num;
					Scanner sc = new Scanner(System.in);
					while (true)
					{
						try
						{
							num = sc.nextInt();
							if (num < 1 || num > 12)
							{
								System.out.println("1~12�� ���ڸ� �Է����ּ���.");
								continue;
							}
							hasJoker = true;
							jokerNumber = num;
							break;
						}
						catch (InputMismatchException ie)
						{
							System.err.println("���ڸ� �Է����ּ���.");
							sc.nextLine();
						}
					}
				}

				// ���ڰ� ����� ��Ŀ�� �־���.
				card = new Card(Card.CardType.number, jokerNumber);
			}

			cardList.add(card);
		}
		Combination comb = new Combination(cardList);
		Combination.ComType cType = comb.GetCombinationType();
		if (cType == Combination.ComType.none)
		{
			System.out.println("�� �� ���� ī�� �����Դϴ�.");
		}
		else
		{
			System.out.println("���õ� Index " + startIndex + ", " + endIndex);
			String str = "ī�� " + comb.GetAmount() + "������ �̷���� �ִ� ���� " + comb.GetHighNumber() + "�� ";
			// ī�带 ���� �� �̹� �����ִ� ī��� ��, �������� Ȯ��
			switch (cType)
			{
			case single:
				str += "Single";
				break;
			case straight:
				str += "Straight";
				break;
			case pare:
				str += "Pare";
				break;
			}
			System.out.println(str);
		}
	}

}
