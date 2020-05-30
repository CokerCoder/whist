import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.CardAdapter;
import ch.aplu.jcardgame.CardListener;


public class HumanStrategy implements IPlayingStrategy{

    private Card selected;
    @Override
    public Card play(Player player) throws InterruptedException {

        selected = null;

        // Set up human player for interaction
        CardListener cardListener = new CardAdapter()  // Human Player plays card
        {
            public void leftDoubleClicked(Card card) { selected = card;
                player.getHand().setTouchEnabled(false); }
        };
        player.getHand().addCardListener(cardListener);
        player.getHand().setTouchEnabled(true);

        while (selected == null) {
            Thread.sleep(100);
        }

        return selected;
    }

}