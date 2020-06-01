import ch.aplu.jcardgame.*;

public class Player implements IObserver{
    private IPlayingStrategy playingStrategy;
    private Hand cardsPlayed = new Hand(null);
    private Hand hand;
    private Hand trick;
    private Suit lead;
    private Suit trumps;
    private Card winningCard;
    
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
            case "human":
                playingStrategy = factory.getHumanStrategy();
                break;
        }
    }

    public Card play() throws InterruptedException {
        // Only delay when it's not a human turn
        if (!playingStrategy.toString().startsWith("Human")) { Thread.sleep(Whist.thinkingTime); }
        return this.playingStrategy.play(this);
    }

    public Hand getCardsPlayed() { return cardsPlayed; }
    public Hand getHand(){ return hand; }
    public void setHand(Hand hand) { this.hand = hand; }
    public Hand getTrick() { return trick; }
    public Suit getLead() { return lead; }
    public Suit getTrumps(){return trumps;}
    public Card getWinningCard() { return winningCard; }
    
    @Override
    public void update(Hand trick, Suit lead, Suit trumps, Card winningCard) {
        this.trick = trick;
        this.lead = lead;
        this.trumps = trumps;
        this.winningCard = winningCard;
        if (trick.getNumberOfCards() == 4) { // Complete trick always 4 cards
        	cardsPlayed.insert(trick, false);
        }
    }
}
