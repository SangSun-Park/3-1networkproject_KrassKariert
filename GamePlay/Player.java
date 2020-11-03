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
	
	// 게임의 첫 시작시 플레이어를 초기화 해줌.
	public void InitializePlayer(int turn)
	{
		life = 2;
		playerTurnNumber = turn;
	}
	
	 // 카드를 덱에서 손패로 추가함. 이떄 카드의 위치는 선택.
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
					System.out.println(card.CardName() + "카드 입니다. 패의 어느 곳에 넣을까요?");
					System.out.println("1~" + hands.size() + "의 숫자를 입력해주세요.");
					continue;
				}
				break;
			}
			catch (InputMismatchException ie)
			{
				System.err.println("숫자를 입력해주세요.");
				sc.nextLine();
			}
		}
		hands.add(i, card);
	}
	
	// 손에 어떤 패가 있는지 표시해줌.
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
	
	// 내고 싶은 카드를 선택함.
	public void SelectCardList(int startIndex, int endIndex)
	{
		if ((endIndex - startIndex) >= 3)
		{
			System.out.println("카드는 최대 3장까지만 낼 수 있습니다.");
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
				if (hasJoker == false) // 아직 조커를 발견한 적이 없다면 조커의 숫자를 설정해 줌.
				{
					System.out.println("조커의 숫자를 설정해주세요.");
					int num;
					Scanner sc = new Scanner(System.in);
					while (true)
					{
						try
						{
							num = sc.nextInt();
							if (num < 1 || num > 12)
							{
								System.out.println("1~12의 숫자를 입력해주세요.");
								continue;
							}
							hasJoker = true;
							jokerNumber = num;
							break;
						}
						catch (InputMismatchException ie)
						{
							System.err.println("숫자를 입력해주세요.");
							sc.nextLine();
						}
					}
				}

				// 숫자가 변경된 조커를 넣어줌.
				card = new Card(Card.CardType.number, jokerNumber);
			}

			cardList.add(card);
		}
		Combination comb = new Combination(cardList);
		Combination.ComType cType = comb.GetCombinationType();
		if (cType == Combination.ComType.none)
		{
			System.out.println("낼 수 없는 카드 조합입니다.");
		}
		else
		{
			System.out.println("선택된 Index " + startIndex + ", " + endIndex);
			String str = "카드 " + comb.GetAmount() + "장으로 이루어진 최대 숫자 " + comb.GetHighNumber() + "의 ";
			// 카드를 냈을 때 이미 내져있는 카드와 비교, 가능한지 확인
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
