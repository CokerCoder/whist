import ch.aplu.jcardgame.*;

public class LegalStrategy implements IPlayingStrategy{
    @Override
    public Card play(Player player){
        Suit trumps = player.getTrumps();
        Suit lead = player.getLead();
        Hand hand = player.getHand();

        if (lead==null) {
            return RandomStrategy.randomCard(hand);
        }
        else if (!hand.getCardsWithSuit(lead).isEmpty()){
            return RandomStrategy.randomCard(hand.getCardsWithSuit(lead));
        }
        else if (!hand.getCardsWithSuit(trumps).isEmpty()){
            return RandomStrategy.randomCard(hand.getCardsWithSuit(trumps));
        }
        return RandomStrategy.randomCard(hand);
    }
}
