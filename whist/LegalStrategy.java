import ch.aplu.jcardgame.*;

public class LegalStrategy implements IPlayingStrategy{
    @Override
    public Card play(Player player){
        Suit trumps = player.getTrumps();
        Suit lead = player.getLead();
        Hand hand = player.getHand();
        if(lead==null) return hand.getFirst();
        else if(!hand.getCardsWithSuit(lead).isEmpty()) {
            return hand.getCardsWithSuit(lead).get(0);
        }
        else if(!hand.getCardsWithSuit(trumps).isEmpty()){
            return hand.getCardsWithSuit(trumps).get(0);
        }
        return hand.getFirst();
    }
}
