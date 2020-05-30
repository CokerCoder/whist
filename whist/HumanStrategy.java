import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.CardAdapter;
import ch.aplu.jcardgame.CardListener;
import ch.aplu.jcardgame.Hand;
import ch.aplu.jcardgame.*;
import ch.aplu.jgamegrid.*;

import java.util.ArrayList;

public class HumanStrategy implements IPlayingStrategy{

    volatile Card selected;
    @Override
    public Card play(Player player){

        // Set up human player for interaction
        CardListener cardListener = new CardAdapter()  // Human Player plays card
        {
            public void leftDoubleClicked(Card card) { selected = card;
                player.getHand().setTouchEnabled(false); }
        };
        player.getHand().addCardListener(cardListener);

        player.getHand().setTouchEnabled(true);

        while (selected == null) {
            Thread.onSpinWait();
        }

        return selected;
    }

}