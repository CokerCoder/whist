import ch.aplu.jcardgame.Card;

public interface IPlayingStrategy {
    Card play(Player player) throws InterruptedException;
}
