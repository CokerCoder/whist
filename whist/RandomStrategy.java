import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

import java.util.ArrayList;

public class RandomStrategy implements IPlayingStrategy{
    @Override
    public Card play(Player player){
        return randomCard(player.getHand());
    }
    // return random Card from Hand
    public static Card randomCard(Hand hand){
        int x = Whist.random.nextInt(hand.getNumberOfCards());
        return hand.get(x);
    }

    // return random Card from ArrayList
    public static Card randomCard(ArrayList<Card> list){
        int x = Whist.random.nextInt(list.size());
        return list.get(x);
    }

}
