import java.util.*;

import javax.swing.JOptionPane;

public class Solitaire
{
	public static void main(String[] args)
	{
		new Solitaire();
	}

	private Stack<Card> stock;
	private Stack<Card> waste;
	private Stack<Card>[] foundations;
	private Stack<Card>[] piles;
	private SolitaireDisplay display;

	public Solitaire()
	{
		foundations = new Stack[4];
		piles = new Stack[7];

		//INSERT CODE HERE
			stock = new Stack<Card>();
			waste = new Stack<Card>();

			for(int i = 0; i < 4; i++)
			foundations[i] = new Stack<Card>();

			for(int i = 0; i < 7; i++)
			piles[i] = new Stack<Card>();
		
			createStock();
			deal();
			dealThreeCards();



		display = new SolitaireDisplay(this);
	}
	
	public void resetStock() {
		while (!waste.isEmpty()) {
			Card card = waste.pop();
			card.turnDown();
			stock.push(card);
		}
	}

	public void deal()
	{
		for(int i = 0; i < 7; i++)
		{
			for(int j = 0; j <= i; j++)
			piles[i].push(stock.pop());
			piles[i].peek().turnUp();
		}
	}
	public void dealThreeCards() {
		for (int i = 0; i < 3 && !stock.isEmpty(); i++) {
			Card card = stock.pop();
			card.turnUp();
			waste.push(card);
		}
		Stack<Card> temp = new Stack<Card>();
		for (int i = 0; i < 3; i++) {
			if (!waste.isEmpty()) {
				temp.push(waste.pop());
			}
		}
		while (!temp.isEmpty()) {
			waste.push(temp.pop());
		}
	}
	
	
	public ArrayList<Card> createStock()
	{
		ArrayList<Card> res = new ArrayList<Card>();
		String suite = "";
		for(int i = 1; i <= 4; i++)
			for(int j = 1; j<= 13; j++ )
			{
				if(i == 1)
				{suite = "c";}
				if(i == 2)
				{suite = "d";}
				if(i == 3)
				{suite = "h";}
				if(i == 4)
				{suite = "s";}
				res.add(new Card(j,suite));
				System.out.println(new Card(j,suite));


			}
		while(res.size() != 0)
		{
			int ran = ((int)(Math.random()*res.size()));
			stock.push((res.get(ran)));
			res.remove(res.get(ran));

		}	

		return res;
	}

	//returns the card on top of the stock,
	//or null if the stock is empty
	public Card getStockCard()
	{
		if(stock.isEmpty())
			return null;
		return stock.peek();
	}

	//returns the card on top of the waste,
	//or null if the waste is empty
	public Card getWasteCard()
	{
		if(waste.isEmpty())
			return null;
		return waste.peek();
	}

	//precondition:  0 <= index < 4
	//postcondition: returns the card on top of the given
	//               foundation, or null if the foundation
	//               is empty
	public Card getFoundationCard(int i)
	{
		if(foundations[i].isEmpty())
			return null;
		return foundations[i].peek();
	}

	//precondition:  0 <= index < 7
	//postcondition: returns a reference to the given pile
	public Stack<Card> getPile(int i)
	{
		// if(piles[i].isEmpty())
		// 	return null;
		return piles[i];
	}
	//called when the stock is clicked
	public void stockClicked() {
		if (!display.isWasteSelected() && !display.isPileSelected()) {
			if (!stock.isEmpty()) {
				dealThreeCards();
			} else {
				resetStock();
			}
		}
		display.unselect();
	}

	//called when the waste is clicked
	public void wasteClicked() {
		if (!display.isPileSelected()) {
			if (!display.isWasteSelected()) {
				display.selectWaste();
			} else {
				display.unselect();
			}
		}
	}
	
	
	//precondition:  0 <= index < 4
	//called when given foundation is clicked
	// Define a method to check for a win
private boolean checkForWin() {
    for (int i = 0; i < foundations.length; i++) {
        if (foundations[i].size() != 13) {
            return false; 
        }
    }
    return true; 
}
public void foundationClicked(int index) {
    if (display.isWasteSelected()) {
        Card wasteCard = getWasteCard();
        if (canAddToFoundation(wasteCard, index)) {
            foundations[index].push(waste.pop());
            display.unselect();
            if (checkForWin()) {
                celebrateWin(); 
            }
        }
    } else if (display.isPileSelected()) {
        Stack<Card> pile = getPile(display.selectedPile());
        if (!pile.isEmpty()) {
            Card pileCard = pile.peek();
            if (canAddToFoundation(pileCard, index)) {
                foundations[index].push(pile.pop());
                display.unselect();
                if (checkForWin()) {
                    celebrateWin(); 
                }
            }
        }
    }
}

private void celebrateWin() {
    JOptionPane.showMessageDialog(null, "Congratulations! You've won!", "Solitaire", JOptionPane.INFORMATION_MESSAGE);
}

	private boolean canAddToPile(Card card, int index) {
        if (getPile(index).isEmpty()) {
            return card.getRank() == 13;
        } else {
            Card topCard = getPile(index).peek();
            return card.isFaceUp() && card.isRed() != topCard.isRed() && card.getRank() == topCard.getRank() - 1;
        }
    }

	//precondition:  0 <= index < 7
	//called when given pile is clicked
	// called when a pile is clicked
public void pileClicked(int index) {
    if (display.isWasteSelected()) {
        PileClickedOnWaste(index);
    } else if (display.isPileSelected()) {
        PileClickedOnPile(index);
    } else {
        PileClickedWhenNoSelection(index);
    }
}

private void PileClickedOnWaste(int index) {
    Card wasteCard = getWasteCard();
    if (canAddToPile(wasteCard, index)) {
        getPile(index).push(waste.pop());
        display.unselect();
    }
}

private void PileClickedOnPile(int index) {
    int selectedPileIndex = display.selectedPile();
    if (index == selectedPileIndex) {
        getPile(index).peek().turnUp();
        display.unselect();
    } else {
        moveCardsBetweenPiles(selectedPileIndex, index);
    }
}

private void PileClickedWhenNoSelection(int index) {
    if (!getPile(index).isEmpty() && getPile(index).peek().isFaceUp()) {
        display.selectPile(index);
    } else if (!getPile(index).isEmpty()) {
        getPile(index).peek().turnUp();
        display.unselect();
    }
}

private void moveCardsBetweenPiles(int i1, int i2) {
    Stack<Card> selectedCards = removeFaceUpCards(i1);
    if (canAddToPile(selectedCards.peek(), i2)) {
        addToPile(selectedCards, i2);
        display.unselect();
    } else {
        addToPile(selectedCards, i1);
    }
}

	private Stack<Card> removeFaceUpCards(int index) {
        Stack<Card> faceUpCards = new Stack<>();
        Stack<Card> pile = getPile(index);
        while (!pile.isEmpty() && pile.peek().isFaceUp()) {
            faceUpCards.push(pile.pop());
        }
        return faceUpCards;
    }
	private void addToPile(Stack<Card> cards, int index) {
		Stack<Card> pile = getPile(index);
		while (!cards.isEmpty()) {
			pile.push(cards.pop());
		}
	}
	

    private boolean canAddToFoundation(Card card, int index) {
        if (foundations[index].isEmpty()) {
            return card.getRank() == 1; 
        } else {
            Card topCard = foundations[index].peek();
            return card.getSuit().equals(topCard.getSuit()) && card.getRank() == topCard.getRank() + 1;
        }
    }
	public Stack<Card> getWaste() {
		return waste;
	}
	

}