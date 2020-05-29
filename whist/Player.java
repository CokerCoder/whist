import ch.aplu.jcardgame.*;

import java.util.Observable;
import java.util.Observer;

public class Player implements Observer {
    private IPlayingStrategy playingStrategy;
    private Hand hand;
    private Hand trick;
    private Suit lead;
    private Suit trump;
    private Card winningCard;
    @Override
    public void update(Observable o, Object arg) {

    }
    public Player(String type) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        StrategyFactory factory = StrategyFactory.getInstance();
        switch (type) {
            case "legal":
                playingStrategy = factory.getLegalStrategy();
                break;
            case "smart":
                playingStrategy = factory.getSmartStrategy();
                break;
            case "random":
                playingStrategy = factory.getRandomStrategy();
                break;
        }
    }

    public Card play(){
        return this.playingStrategy.play(this);
    }
    public Hand getHand(){
        return hand;
    }

}
