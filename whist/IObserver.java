import ch.aplu.jcardgame.*;

public interface IObserver {
    void update(Hand trick, Suit lead, Suit trumps, Card winningCard);
}
