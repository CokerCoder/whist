// Whist.java
import ch.aplu.jcardgame.*;
import ch.aplu.jgamegrid.*;
import java.awt.Color;
import java.awt.Font;
import java.util.*;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

public class Whist extends CardGame implements ISubject{
	static Random random;
	// Display attributes
	final String[] trumpImage = {"bigspade.gif","bigheart.gif","bigdiamond.gif","bigclub.gif"};
	private static int handWidth = 400;
	private static int trickWidth = 40;
	private Actor[] scoreActors = {null, null, null, null };
	private final Location trickLocation = new Location(350, 350);
	private final Location textLocation = new Location(350, 450);
	private Location hideLocation = new Location(-500, - 500);
	private Location trumpsActorLocation = new Location(50, 50);
	Font bigFont = new Font("Serif", Font.BOLD, 36);
	private final Location[] handLocations = {
			new Location(350, 625),
			new Location(75, 350),
			new Location(350, 75),
			new Location(625, 350)
	};
	private final Location[] scoreLocations = {
			new Location(575, 675),
			new Location(25, 575),
			new Location(575, 25),
			new Location(650, 575)
	};


	private static String version;
	public static int nbPlayers; 
	public static int nbStartCards; 
	public static int winningScore;
	public static int thinkingTime;
	private static boolean enforceRules;
	private static String[] playerType;
	private List<Player> players = new ArrayList<>();
	private int[] scores = new int[nbPlayers];
	private Hand[] hands;
	private final Deck deck = new Deck(Suit.values(), Rank.values(), "cover");
	private Suit trumps;
	private Card winningCard;
	private Hand trick;
	private Suit lead;

	public static boolean rankGreater(Card card1, Card card2) {
		return card1.getRankId() < card2.getRankId(); // Warning: Reverse rank order of cards (see comment on enum)
	}
	// return random Enum value
	public static <T extends Enum<?>> T randomEnum(Class<T> clazz){
		int x = random.nextInt(clazz.getEnumConstants().length);
		return clazz.getEnumConstants()[x];
	}

	private void initScore() {
		 for (int i = 0; i < nbPlayers; i++) {
			 scores[i] = 0;
			 scoreActors[i] = new TextActor("0", Color.WHITE, bgColor, bigFont);
			 addActor(scoreActors[i], scoreLocations[i]);
		 }
	}

	private void updateScore(int player) {
		removeActor(scoreActors[player]);
		scoreActors[player] = new TextActor(String.valueOf(scores[player]), Color.WHITE, bgColor, bigFont);
		addActor(scoreActors[player], scoreLocations[player]);
	}

	private void initRound() throws ClassNotFoundException, InstantiationException, InterruptedException, IllegalAccessException {
		// Create players
		for (int i=0;i<nbPlayers;i++) {
			addObserver(new Player(playerType[i]));
		}
		// Create nbPlayer hand arrays
		hands = deck.dealingOut(nbPlayers, 0); // Last element of hands is leftover cards; these are ignore
		// Deal cards based on seed
		ArrayList<Integer> cards = new ArrayList<Integer>();
		for (int i = 0; i < deck.getNumberOfCards(); i++) { cards.add(i); }
		Collections.shuffle(cards, random); 
		for (int i = 0; i < nbPlayers; i++) {
			for (int j = 0; j < nbStartCards; j++) {
				int cardNumber = cards.remove(0);
				hands[i].insert(cardNumber, false);
			}
			hands[i].sort(Hand.SortType.SUITPRIORITY, true);
			players.get(i).setHand(hands[i]);
		}

	    RowLayout[] layouts = new RowLayout[nbPlayers];
	    for (int i = 0; i < nbPlayers; i++){
			layouts[i] = new RowLayout(handLocations[i], handWidth);
			layouts[i].setRotationAngle(90 * i);
//			layouts[i].setStepDelay(10);
			hands[i].setView(this, layouts[i]);
			hands[i].setView(this, layouts[i]);
			hands[i].setTargetArea(new TargetArea(trickLocation));
			hands[i].draw();
	    }
//	    for (int i = 1; i < nbPlayers; i++)  // This code can be used to visually hide the cards in a hand (make them face down)
//	      	hands[i].setVerso(true);
	    // End graphics
 	}

	private Optional<Integer> playRound() throws InterruptedException {  // Returns winner, if any
		// Select and display trump suit
		trumps = randomEnum(Suit.class);
		final Actor trumpsActor = new Actor("sprites/"+trumpImage[trumps.ordinal()]);
		addActor(trumpsActor, trumpsActorLocation);
		// End trump suit
		winningCard = null;
		int winner =0;
		int nextPlayer = random.nextInt(nbPlayers); // randomly select player to lead for this round
		for (int i = 0; i < nbStartCards; i++) {
			trick = new Hand(deck);
			Card selected;
			lead = null;
			for (int j = 0; j < nbPlayers; j++) {
				if (nextPlayer+1 > nbPlayers) nextPlayer = 0;  // From last back to first
				if (playerType[nextPlayer].equals("human")) {
					setStatusText("Player " + nextPlayer + " double-click on card to follow.");
				} else {
					setStatusText("Player " + nextPlayer + " thinking...");
				}
				notifyObservers();
				selected = players.get(nextPlayer).play();
				// Set lead
				if (lead==null) {
					lead = (Suit) selected.getSuit();
					winner = nextPlayer;
					winningCard = selected;
				}
				// Follow with selected card
				trick.setView(this, new RowLayout(trickLocation, (trick.getNumberOfCards()+2)*trickWidth));
				trick.draw();
				selected.setVerso(false);  // In case it is upside down
				// Check: Following card must follow suit if possible
				if (selected.getSuit() != lead && hands[nextPlayer].getNumberOfCardsWithSuit(lead) > 0) {
					 // Rule violation
					String violation = "Follow rule broken by player " + nextPlayer + " attempting to play " + selected;
					System.out.println(violation);
					if (enforceRules)
						try {
							throw(new BrokeRuleException(violation));
						} catch (BrokeRuleException e) {
							e.printStackTrace();
							System.out.println("A cheating player spoiled the game!");
							System.exit(0);
						}
				}
				// End Check
				selected.transfer(trick, true); // transfer to trick (includes graphic effect)
				System.out.println("winning: suit = " + winningCard.getSuit() + ", rank = " + winningCard.getRankId());
				System.out.println(" played: suit = " +    selected.getSuit() + ", rank = " +    selected.getRankId());
				if ( // beat current winner with higher card
					(selected.getSuit() == winningCard.getSuit() && rankGreater(selected, winningCard)) ||
					// trumped when non-trump was winning
					(selected.getSuit() == trumps && winningCard.getSuit() != trumps)) {
						System.out.println("NEW WINNER");
						winner = nextPlayer;
						winningCard = selected;
				}
				nextPlayer++;
				// End Follow
			}
			delay(600);
			trick.setView(this, new RowLayout(hideLocation, 0));
			trick.draw();
			nextPlayer = winner;
			setStatusText("Player " + nextPlayer + " wins trick.");
			scores[nextPlayer]++;
			updateScore(nextPlayer);
			notifyObservers(); // Update cards played in each round
			if (winningScore == scores[nextPlayer]) return Optional.of(nextPlayer);
		}
		removeActor(trumpsActor);
		return Optional.empty();
	}

	public Whist(HashMap<Boolean, Integer> seed) throws IllegalAccessException, InstantiationException, ClassNotFoundException, InterruptedException {
		super(700, 700, 30);
		setTitle("Whist (V" + version + ") Constructed for UofM SWEN30006 with JGameGrid (www.aplu.ch)");
		setStatusText("Initializing...");
		initScore();
		Optional<Integer> winner;
		
		if(seed.containsKey(true)){
        	Whist.random = new Random((long) seed.get(true));
        }
        else{
        	Whist.random = new Random();	
        }
		do {
		  initRound();
		  winner = playRound();
		} while (!winner.isPresent());
		addActor(new Actor("sprites/gameover.gif"), textLocation);
		setStatusText("Game over. Winner is player: " + winner.get());
		refresh();
	}

	public static void main(String[] args) throws IOException, IllegalAccessException, ClassNotFoundException, InstantiationException, InterruptedException {
//		System.out.println("Working Directory = " + System.getProperty("user.dir"));

		// Read properties
		Properties whistProperties = new Properties();
		try (FileReader inStream = new FileReader("whist.properties")) {
			whistProperties.load(inStream);
		}
		String seedProp = whistProperties.getProperty("Seed");
		nbPlayers = Integer.parseInt(whistProperties.getProperty("nbPlayers"));
		nbStartCards = Integer.parseInt(whistProperties.getProperty("nbStartCards"));
		winningScore = Integer.parseInt(whistProperties.getProperty("winningScore"));
		enforceRules = Boolean.parseBoolean(whistProperties.getProperty("enforceRules"));
		version = whistProperties.getProperty("version");
		thinkingTime = Integer.parseInt(whistProperties.getProperty("thinkingTime"));

		// Read player types from property file
		playerType = whistProperties.getProperty("playerType").replaceAll(" +","").split(",");

		// Seed options
		HashMap<Boolean, Integer> seedMap = new HashMap<>();
		if (seedProp == null) {
			seedMap.put(false, 0);
		}
		else {
			seedMap.put(true, Integer.parseInt(seedProp));
		}

		new Whist(seedMap);
	}

	@Override
	public void addObserver(IObserver o) {
		players.add((Player) o);
	}

	@Override
	public void removeObserver(IObserver o) {
		players.remove((Player) o);
	}

	@Override
	public void notifyObservers() {
		for (Iterator<Player> it = players.iterator(); it.hasNext();) {
			IObserver o = it.next();
			o.update(trick, lead, trumps, winningCard);
		}
	}
}
