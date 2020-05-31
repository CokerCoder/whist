import java.util.ArrayList;

import ch.aplu.jcardgame.*;

public class SmartStrategy implements IPlayingStrategy{

    @Override
    public Card play(Player player) {
        Suit trumps = player.getTrumps();
        Suit lead = player.getLead();
        Hand hand = player.getHand();
        Hand trick = player.getTrick();
        Card winningCard = player.getWinningCard();
        
        int leadSize = hand.getNumberOfCardsWithSuit(lead);
        int trumpSize = hand.getNumberOfCardsWithSuit(trumps);
        ArrayList<Card> leadCards = hand.getCardsWithSuit(lead);
        ArrayList<Card> trumpCards = hand.getCardsWithSuit(trumps);
        ArrayList<Card> trickCards = trick.getCardsWithSuit(lead);
        
        // First to lead
        if (lead==null) { 
        	// Check for ace trump card
        	if (trumpSize > 0 && trumpCards.get(0).getRankId() == 0) {
        		return hand.getCardsWithSuit(trumps).get(0);
            }
        	return hand.getFirst(); // TODO: maybe other possible ways?
        }
        // Follow lead
        else if (leadSize > 0) {
        	// Trump card played or can't beat lead play
        	if (!lead.equals(trumps) && winningCard.getSuit().equals(trumps)
        			|| !Whist.rankGreater(leadCards.get(0), trickCards.get(0))) {
        		// Play lowest card
        		return hand.getCardsWithSuit(lead).get(leadSize-1);
        	} else {
        		// Play highest card
        		return hand.getCardsWithSuit(lead).get(0);
        	}
        }
        // Play trump
        else if (trumpSize > 0) {
        	// Trump not played yet
        	if (!winningCard.getSuit().equals(trumps)) {
        		// Play lowest trump 
        		return hand.getCardsWithSuit(trumps).get(trumpSize-1);
        	} 
        	// Can beat trump
        	else if (Whist.rankGreater(trumpCards.get(0), winningCard)) {
        		// Play highest trump
        		return hand.getCardsWithSuit(trumps).get(0);
        	}
        }
        // Play any card
        // TODO: maybe get it to play the lowest non-trump card
//        hand.sort(Hand.SortType.RANKPRIORITY, false); 
        // An idea was to sort hand by rank but this changes graphics
        return hand.getFirst();
    }
}
