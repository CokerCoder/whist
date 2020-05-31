import ch.aplu.jcardgame.*;

public interface IPlayingStrategy {
    Card play(Player player) throws InterruptedException;
}
