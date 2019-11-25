import java.awt.*;
import javax.lang.model.util.ElementScanner6;
import javax.swing.*;
import javax.swing.border.*;
import java.util.Random;
import javax.swing.border.TitledBorder;
import java.awt.event.*;
import java.util.concurrent.TimeUnit;

/************************************************************************
 * Low-Card Game Logic
 * 
 * @todo         #create action listeners.
 *               #place low card in winning[] array.
 *               #add JLabels.
 *               #decide how to select a card from your hand (button?).
 *               #decide how the computer plays. 
 *                   -intentionally lose? always win?
 *               #decide how to update cards or the computer's cards to                reflect one fewer card every round so that 
 *                   reflect one fewer card every round 
 *                   so that hands get smaller.
 * 
 * 
 ***********************************************************************/
public class phase3 implements ActionListener
{
   static int NUM_CARDS_PER_HAND = 7;
   static int  NUM_PLAYERS = 2;
   static JLabel[] computerLabels = new JLabel[NUM_CARDS_PER_HAND];
   static JLabel[] humanLabels = new JLabel[NUM_CARDS_PER_HAND];  
   static JLabel[] playedCardLabels  = new JLabel[NUM_PLAYERS]; 
   static JLabel[] playLabelText  = new JLabel[NUM_PLAYERS];
   static JButton cardButtons[] = new JButton[NUM_CARDS_PER_HAND];
   static Card[] compWinnings = new Card[57]; //fix size and instantiate in main
   static Card[] humanWinnings = new Card[57]; //fix size instantiate in main
   static CardGameFramework LowCardGame;
   static Icon tempIcon;
   static CardTable myCardTable;
   static int computerWinningsCounter = 0;
   static int humanWinningsCounter = 0;
   
   public static void main(String[] args)
   {
      int numPacksPerDeck = 1;
      int numJokersPerPack = 4;
      int numUnusedCardsPerPack = 0;
      Card[] unusedCardsPerPack = null;

      LowCardGame = new CardGameFramework(
         numPacksPerDeck, numJokersPerPack,
         numUnusedCardsPerPack, unusedCardsPerPack,
         NUM_PLAYERS, NUM_CARDS_PER_HAND
         );
      //System.out.println(LowCardGame.deal());
      LowCardGame.deal();
    
      //Icons loaded from GUICard 
      GUICard.loadCardIcons();
      
      // establish main frame in which program will run
      myCardTable 
         = new CardTable("CardTable", NUM_CARDS_PER_HAND, NUM_PLAYERS);
      myCardTable.setSize(800, 600);
      myCardTable.setLocationRelativeTo(null);
      myCardTable.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      // show everything to the user
      myCardTable.setVisible(true);
      
      // CREATE LABELS ----------------------------------------------------
      Game game = new Game();
      for (int card = 0; card < NUM_CARDS_PER_HAND; card++)
      {
         //back labels made for playing cards 
         computerLabels[card] = new JLabel(GUICard.getBackCardIcon());
         
         //labels for each card in the user's hand
         tempIcon = GUICard.getIcon(LowCardGame.getHand(1).inspectCard(card));

         //humanLabels[card] = new JLabel(tempIcon);
         cardButtons[card] = new JButton(tempIcon);
         cardButtons[card].setActionCommand(Integer.toString(card));
         //cardButtons[card].setSize(73,97);
         cardButtons[card].addActionListener(game);
      }
      
      // ADD LABELS TO PANELS -----------------------------------------
      for (int card = 0; card < NUM_CARDS_PER_HAND; card++)
      {
         // index label added to computer panel
         myCardTable.pnlComputerHand.add(computerLabels[card]);
         
         // index label added to human panel
         myCardTable.pnlHumanHand.add(cardButtons[card]);
      }


      /************* Test action listener Method **************/
      
      //System.out.println("Pre sort: " + LowCardGame.getHand(0).toString());

      // Sort the computer's hand, in order to get the lowest card
      LowCardGame.getHand(0).sort();

      System.out.println("Computer hand Pre: " + LowCardGame.getHand(0).toString());
      System.out.println("Player hand Pre : " + LowCardGame.getHand(1).toString());

      // Add playing cards into an array 
      Card[] cardInPlay = new Card[2];
      cardInPlay[0] = LowCardGame.playCard(0, 0);
      cardInPlay[1] = LowCardGame.playCard(1, 0);

      System.out.println("Computer hand at Play: " + LowCardGame.getHand(0).toString());
      System.out.println("Player hand at play: " + LowCardGame.getHand(1).toString());

      // Display icon for player
      Icon playerIcon = GUICard.getIcon( cardInPlay[1]);
      System.out.println("Player Card: " +  cardInPlay[1]);
      playedCardLabels[1] = new JLabel("You", JLabel.CENTER);
      playedCardLabels[1].setIcon(playerIcon);
      playedCardLabels[1].setHorizontalTextPosition(JLabel.CENTER);
      playedCardLabels[1].setVerticalTextPosition(JLabel.BOTTOM);
      
      // Dispay icon for computer 
      Icon compIcon = GUICard.getIcon(cardInPlay[0]);
      System.out.println("Computer Card: " + cardInPlay[0]);
      playedCardLabels[0] = new JLabel("Computer", JLabel.CENTER);
      playedCardLabels[0].setIcon(compIcon);
      playedCardLabels[0].setHorizontalTextPosition(JLabel.CENTER);
      playedCardLabels[0].setVerticalTextPosition(JLabel.BOTTOM);

      // Set the play area layout
      myCardTable.pnlPlayArea.setLayout(new GridLayout());
     
      // add line to delay
      myCardTable.pnlPlayArea.add(playedCardLabels[0]);
      
      myCardTable.pnlPlayArea.add(playedCardLabels[1]);

      System.out.println("Computer's Card: " + cardInPlay[0].getValue());
      System.out.println("Player's Card: " + cardInPlay[1].getValue());

      // Check who won this round 
      if(cardInPlay[0].getValue() < cardInPlay[1].getValue())
      {
         // Computer wins this round
         compWinnings[computerWinningsCounter] = cardInPlay[0];
         compWinnings[computerWinningsCounter] = cardInPlay[1];
         computerWinningsCounter += 2;
      }
      else if(cardInPlay[0].getValue() > cardInPlay[1].getValue())
      {
         // Human wins this round
         humanWinnings[humanWinningsCounter] = cardInPlay[0];
         humanWinnings[humanWinningsCounter] = cardInPlay[1];
         humanWinningsCounter += 2;
      }   
      else
      {
         // If there is a tie
         // lets think about this later
      }
      
      //myCardTable.pnlPlayArea.removeAll();
      myCardTable.pnlPlayArea.remove(playedCardLabels[0]);
      myCardTable.pnlPlayArea.remove(playedCardLabels[1]);
      
      System.out.println("I'm done!");

      
      
      LowCardGame.takeCard(0);
      LowCardGame.takeCard(1);

      System.out.println("Computer hand post: " + LowCardGame.getHand(0).toString());
      System.out.println("Player hand post: " + LowCardGame.getHand(1).toString());

      /************************ END TEST ************************/
      
      
      // show everything to the user
      myCardTable.setVisible(true);
   }
   public void delayUI() {
      try {
         TimeUnit.SECONDS.sleep(2);
      } catch (InterruptedException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }
/*
   @Override
   public void actionPerformed(ActionEvent e) 
   {
         String cardPlayed = e.getActionCommand();

         int cardNum = Integer.parseInt(cardPlayed);

         Icon playerIcon = GUICard.getIcon(LowCardGame.getHand(1).inspectCard(cardNum));

         LowCardGame.getHand(0).sort();
         Icon compIcon = GUICard.getIcon(LowCardGame.getHand(0).inspectCard(0));

         playedCardLabels[1] = new JLabel("You", JLabel.CENTER);
         playedCardLabels[1].setIcon(playerIcon);
         
         playedCardLabels[0] = new JLabel("Computer", JLabel.CENTER);
         playedCardLabels[0].setIcon(compIcon);


         myCardTable.pnlPlayArea.add(playedCardLabels[1]);
         // add line to delay

         myCardTable.pnlPlayArea.add(playedCardLabels[0]);

         //myCardTable.pnlPlayArea.add(cardButtons[cardNum]);
         
         /*
         if(cardPlayed.equals("0"))
            //myCardTable.pnlHumanHand.set
            System.out.println("Card 0");
         else if(cardPlayed.equals("1"))
            System.out.println("Card 1");
         else if(cardPlayed.equals("2"))
            System.out.println("Card 2");
         else if(cardPlayed.equals("3"))
            System.out.println("Card 3");
         else if(cardPlayed.equals("4"))
            System.out.println("Card 4");
         else if(cardPlayed.equals("5"))
            System.out.println("Card 5");
         else if(cardPlayed.equals("6"))
            System.out.println("Card 6");
         else
            System.out.println("Error");
         *
   }
   */

}

class Game implements ActionListener
   { 
      @Override
      public void actionPerformed(ActionEvent e) 
      {
         String cardPlayed = e.getActionCommand();
/*
         int cardNum = Integer.parseInt(cardPlayed);

         Icon tempIcon = GUICard.getIcon(LowCardGame.getHand(1).inspectCard(cardNum));

         */
         //Test
         
         if(cardPlayed.equals("0"))
            //myCardTable.pnlHumanHand.set
            System.out.println("Card 0");
         else if(cardPlayed.equals("1"))
            System.out.println("Card 1");
         else if(cardPlayed.equals("2"))
            System.out.println("Card 2");
         else if(cardPlayed.equals("3"))
            System.out.println("Card 3");
         else if(cardPlayed.equals("4"))
            System.out.println("Card 4");
         else if(cardPlayed.equals("5"))
            System.out.println("Card 5");
         else if(cardPlayed.equals("6"))
            System.out.println("Card 6");
         else
            System.out.println("Error");
        
      }
   }

/*-----------------------------
 * End of phase3 client (main)
 * ----------------------/

/****************************************
 * CardGameFrameWork class
 *****************************************/
class CardGameFramework {
   private static final int MAX_PLAYERS = 50;

   private int numPlayers;
   private int numPacks; // # standard 52-card packs per deck
                         // ignoring jokers or unused cards
   private int numJokersPerPack; // if 2 per pack & 3 packs per deck, get 6
   private int numUnusedCardsPerPack; // # cards removed from each pack
   private int numCardsPerHand; // # cards to deal each player
   private Deck deck; // holds the initial full deck and gets
                      // smaller (usually) during play
   private Hand[] hand; // one Hand for each player
   private Card[] unusedCardsPerPack; // an array holding the cards not used
                                      // in the game. e.g. pinochle does not
                                      // use cards 2-8 of any suit

   public CardGameFramework(int numPacks, int numJokersPerPack, int numUnusedCardsPerPack, Card[] unusedCardsPerPack,
         int numPlayers, int numCardsPerHand) {
      int k;

      // filter bad values
      if (numPacks < 1 || numPacks > 6)
         numPacks = 1;
      if (numJokersPerPack < 0 || numJokersPerPack > 4)
         numJokersPerPack = 0;
      if (numUnusedCardsPerPack < 0 || numUnusedCardsPerPack > 50) // > 1 card
         numUnusedCardsPerPack = 0;
      if (numPlayers < 1 || numPlayers > MAX_PLAYERS)
         numPlayers = 4;
      // one of many ways to assure at least one full deal to all players
      if (numCardsPerHand < 1 || numCardsPerHand > numPacks * (52 - numUnusedCardsPerPack) / numPlayers)
         numCardsPerHand = numPacks * (52 - numUnusedCardsPerPack) / numPlayers;

      // allocate
      this.unusedCardsPerPack = new Card[numUnusedCardsPerPack];
      this.hand = new Hand[numPlayers];
      for (k = 0; k < numPlayers; k++)
         this.hand[k] = new Hand();
      deck = new Deck(numPacks);

      // assign to members
      this.numPacks = numPacks;
      this.numJokersPerPack = numJokersPerPack;
      this.numUnusedCardsPerPack = numUnusedCardsPerPack;
      this.numPlayers = numPlayers;
      this.numCardsPerHand = numCardsPerHand;
      for (k = 0; k < numUnusedCardsPerPack; k++)
         this.unusedCardsPerPack[k] = unusedCardsPerPack[k];

      // prepare deck and shuffle
      newGame();
   }

   // constructor overload/default for game like bridge
   public CardGameFramework() {
      this(1, 0, 0, null, 4, 13);
   }

   public Hand getHand(int k) {
      // hands start from 0 like arrays

      // on error return automatic empty hand
      if (k < 0 || k >= numPlayers)
         return new Hand();

      return hand[k];
   }

   public Card getCardFromDeck() {
      return deck.dealCard();
   }

   public int getNumCardsRemainingInDeck() {
      return deck.getNumCards();
   }

   public void newGame() {
      int k, j;

      // clear the hands
      for (k = 0; k < numPlayers; k++)
         hand[k].resetHand();

      // restock the deck
      deck.init(numPacks);

      // remove unused cards
      for (k = 0; k < numUnusedCardsPerPack; k++)
         deck.removeCard(unusedCardsPerPack[k]);

      // add jokers
      for (k = 0; k < numPacks; k++)
         for (j = 0; j < numJokersPerPack; j++)
            deck.addCard(new Card('X', Card.Suit.values()[j]));

      // shuffle the cards
      deck.shuffle();
   }

   public boolean deal() {
      // returns false if not enough cards, but deals what it can
      int k, j;
      boolean enoughCards;

      // clear all hands
      for (j = 0; j < numPlayers; j++)
         hand[j].resetHand();

      enoughCards = true;
      for (k = 0; k < numCardsPerHand && enoughCards; k++) {
         for (j = 0; j < numPlayers; j++)
            if (deck.getNumCards() > 0)
               hand[j].takeCard(deck.dealCard());
            else {
               enoughCards = false;
               break;
            }
      }

      return enoughCards;
   }

   void sortHands() {
      int k;

      for (k = 0; k < numPlayers; k++)
         hand[k].sort();
   }

   Card playCard(int playerIndex, int cardIndex) {
      // returns bad card if either argument is bad
      if (playerIndex < 0 || playerIndex > numPlayers - 1 || cardIndex < 0 || cardIndex > numCardsPerHand - 1) {
         // Creates a card that does not work
         return new Card('M', Card.Suit.SPADES);
      }

      // return the card played
      return hand[playerIndex].playCard(cardIndex);

   }

   boolean takeCard(int playerIndex) {
      // returns false if either argument is bad
      if (playerIndex < 0 || playerIndex > numPlayers - 1)
         return false;

      // Are there enough Cards?
      if (deck.getNumCards() <= 0)
         return false;

      return hand[playerIndex].takeCard(deck.dealCard());
   }

}

/*********************************************************************
 * CardTable
 * 
 * description: creates CardTable class that extends JFrame usage: controls the
 * positioning of the panels and cards of the GUI
 **********************************************************************/

class CardTable extends JFrame {
   private static final long serialVersionUID = 1L;
   // members establish the grid layout for the JPanels
   static int MAX_CARDS_PER_HAND = 56;
   static int MAX_PLAYERS = 2;
   private int numCardsPerHand;
   private int numPlayers;
   public JPanel pnlComputerHand, pnlHumanHand, pnlPlayArea;

   /**
    * constructor filters input, adds any panels to the Jframe and establishes
    * layouts accordingly.
    * 
    * @param title
    * @param numCardsPerHand
    * @param numPlayers
    */
   public CardTable(String title, int numCardsPerHand, int numPlayers) {
      // displays title on window
      super(title);

      // lays out the border
      setLayout(new BorderLayout());

      // values that will be used
      this.numCardsPerHand = numCardsPerHand;
      this.numPlayers = numPlayers;

      // field panels defined
      pnlComputerHand = new JPanel(new GridLayout(1, numCardsPerHand));
      pnlHumanHand = new JPanel(new GridLayout(1, numCardsPerHand));
      pnlPlayArea = new JPanel(new GridLayout(2, numPlayers));

      // place panels on grid
      add(pnlPlayArea, BorderLayout.CENTER);
      add(pnlComputerHand, BorderLayout.NORTH);
      add(pnlHumanHand, BorderLayout.SOUTH);

      // labels the borders
      pnlPlayArea.setBorder(new TitledBorder("Community"));
      pnlComputerHand.setBorder(new TitledBorder("Opponent"));
      pnlHumanHand.setBorder(new TitledBorder("You"));

   }

   // accessors
   public int getNumCardsPerHand() {
      return numCardsPerHand;
   }

   public int getNumPlayers() {
      return numPlayers;
   }
}

/*-----------------------------------------------------
 * End Of CardTable
 *-----------------------------------------------/


 /****************************************************************
 * GUI Card
 * 
 * description:  creates a new GUICard class 
 * usage:        manages the reading and building of the card
 *               image Icons.
 *****************************************************************/
class GUICard {
   // members to facilitate GUICard class
   private static Icon[][] iconCards = new ImageIcon[14][4];
   private static Icon iconBack;
   static boolean iconsLoaded = false;

   // identifies card icon filenames
   private static char[] cardSuits = { 'C', 'D', 'H', 'S' };

   /**
    * generates the image icon array from files
    */
   static void loadCardIcons() {
      if (iconsLoaded)
         return;

      // generates file names of icon and adds to Icon's array
      String filename = "";
      for (int i = 0; i < Card.valuRanks.length; ++i) {
         for (int j = 0; j < cardSuits.length; ++j) {
            filename = "images/" + Card.valuRanks[i] + cardSuits[j] + ".gif";
            iconCards[i][j] = new ImageIcon(filename);
         }
      }
      // fills the back of card
      iconBack = new ImageIcon("images/BK.gif");
      iconsLoaded = true;
   }

   // returns back icon for back of card
   public static Icon getBackCardIcon() {
      loadCardIcons();
      return iconBack;
   }

   // returns specific icon
   public static Icon getIcon(Card card) {
      loadCardIcons();
      return iconCards[valueAsInt(card)][suitAsInt(card)];
   }

   // index of card value returned
   private static int valueAsInt(Card card) {
      int i = 0;
      while (card.getValue() != Card.valuRanks[i])
         ++i;

      return i;
   }

   // returns index of card suit
   private static int suitAsInt(Card card) {
      int i;
      Card.Suit suit = card.getSuit();
      Card.Suit[] allSuits = Card.Suit.values();

      for (i = 0; i < allSuits.length; ++i)
         if (suit == allSuits[i])
            return i;

      // invalid suit
      return -1;
   }

}

/*-----------------------------------------------------
 * End Of GUI Card
 *-----------------------------------------------/



/****************************************************************
 * Card
 * imported Card class 
 ***************************************************************/
class Card {
   public enum Suit {
      CLUBS, DIAMONDS, HEARTS, SPADES
   };

   // card values
   private char value;
   private Suit suit;
   public static char[] valuRanks = { 'A', '2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K', 'X' };

   // Checks for illegal card data
   private boolean errorFlag;

   /**
    * Constructor
    * 
    * @param value
    * @param suit
    */
   public Card(char value, Suit suit) {
      set(value, suit);
   }

   /**
    * Overloaded constructor
    */
   public Card() {
      this('A', Suit.SPADES);
   }

   /**
    * Copy Constructor
    * 
    * @param otherCard
    */
   public Card(Card otherCard) {
      if (otherCard == null) {
         System.out.println("Fatal Error!");
         System.exit(0);
      }
      set(otherCard.value, otherCard.suit);
   }

   // Accessors
   public Suit getSuit() {
      return suit;
   }

   public char getValue() {
      return value;
   }

   public boolean getErrorFlag() {
      return errorFlag;
   }

   // Mutators
   public boolean set(char value, Suit suit) {
      if (isValid(value, suit)) {
         this.value = value;
         this.suit = suit;
         errorFlag = false;
         return true;
      }
      errorFlag = true;
      return false;
   }

   // Valid card data returned
   public String toString() {
      if (errorFlag)
         return "[Card Not Valid]";
      return value + " of " + suit;

   }

   // Returns true if all field members are identical, false otherwise
   public boolean equals(Card card) {
      if (card == null)
         return false;

      // comparing member values
      if (this.value == card.value) {
         if (this.suit == card.suit)
            return true;
      }
      return false;
   }

   // Determine validity for the value
   private boolean isValid(char value, Suit suit) {
      // checks if value is in the field of valid values
      for (int i = 0; i < valuRanks.length; ++i) {
         if (value == valuRanks[i])
            return true;
      }
      // invalid value
      return false;
   }

   public static void arraySort(Card[] cArray, int arraySize) {
      for (int i = 0; i < arraySize - 1; i++) {
         for (int j = 0; j < arraySize - i - 1; j++) {
            if (cardAsInt(cArray[j]) > cardAsInt(cArray[j + 1])) {
               Card temp = cArray[j];
               cArray[j] = cArray[j + 1];
               cArray[j + 1] = temp;
            }
         }
      }
   }

   /**
    * It returns an integer according to the value
    * 
    * @param card
    * @return -1 if the value is not valid
    */
   public static int valueAsInt(Card card) {
      for (int i = 0; i < valuRanks.length; i++) {
         if (valuRanks[i] == card.getValue()) {
            return i;
         }
      }
      // Not matching any values in the ranks
      return -1;
   }

   /**
    * It returns the suit position as an integer
    * 
    * @param card
    * @return an integer
    */
   public static int suitAsInt(Card card) {
      return card.getSuit().ordinal();
   }

   /**
    * It returns a specific integer for a card according to the value and the suit
    * 
    * @param card
    * @return an integer for the card
    */
   public static int cardAsInt(Card card) {
      int total = Card.valueAsInt(card) * 4 + Card.suitAsInt(card);
      return total;
   }
}

/*-----------------------------------------------------
 * End Of Card
 *----------------------------------------------/


 /****************************************************************
 * Hand 
 * imported Hand class 
 ***************************************************************/
class Hand {
   // a hand can only have 52 cards maximum
   public static final int MAX_CARDS = CardTable.MAX_CARDS_PER_HAND;

   private Card[] myCards;
   private int numCards;

   // Constructor
   public Hand() {
      myCards = new Card[MAX_CARDS];
      numCards = 0;
   }

   /**
    * resets hand
    */
   public void resetHand() {
      numCards = 0;
   }

   /**
    * Adds a card to the next available position
    * 
    * @param card
    * @return boolean
    */
   public boolean takeCard(Card card) {
      if (numCards < MAX_CARDS && card != null && !card.getErrorFlag()) {
         Card tempCard = new Card(card);
         myCards[numCards] = tempCard;
         numCards++;
         return true;
      }
      return false;
   }

   // stringifies the hand
   public String toString() {
      String myHand = "";
      if (numCards > 0) {

         myHand += myCards[0];
         for (int i = 1; i < numCards; i++) {
            myHand += " , " + myCards[i];
         }

      }
      return "Hand = " + "(" + myHand + ")";
   }

   // Accessor
   public int getNumCards() {
      return numCards;
   }

   // Accessor
   public Card inspectCard(int k) {

      if (k >= 0 && k < numCards) // assume valid k starts from 0
      {
         Card aCard = new Card(myCards[k]); // prevent privacy leaks
         return aCard;
      }

      // return a dummy invalid card
      Card badCard = new Card(' ', Card.Suit.SPADES);
      return badCard;

   }

   /**
    * Sort the cards in the hand in ascending order
    */
   public void sort() {
      Card.arraySort(myCards, numCards);
   }

   /**
    * Return a card on the top of the hand
    * 
    * @return a good card if there is any
    */
   public Card playCard() {
      if (numCards > 0) {
         numCards--;
         return myCards[numCards];
      }

      // No more cards, return a card that does not work
      return new Card('M', Card.Suit.SPADES);

   }

   /**
    * Remove a card from the given location and slide down cards behind down one
    * spot
    * 
    * @param cardIndex the location where the card is removed from the hand
    * @return the card at the given location or a bad card if location is invalid
    */
   public Card playCard(int cardIndex) {
      if (cardIndex < 0 || cardIndex >= numCards) // out of bound error
      {
         // Creates a card that does not work
         return new Card('M', Card.Suit.SPADES);
      }
      // Decreases numCards.
      Card card = myCards[cardIndex];
      numCards--;

      // Slide down cards that followed down one spot
      for (int i = cardIndex; i < numCards; i++) {
         myCards[i] = myCards[i + 1];
      }

      myCards[numCards] = null;

      return card;
   }

}
/*-----------------------------------------------------
 * End Of Hand
 *----------------------------------------------/


 /****************************************************************
 * Deck import
 * imported Deck class
 ***************************************************************/

class Deck {
   // playing card pack values
   public static final int MAX_CARDS = 312; // 6 packs x 52 cards
   public static final int ONE_PACK = 52; // standard 52 cards
   public static boolean beenHereBefore = false;

   // creates a new object with one pack of cards
   private static Card[] masterPack = new Card[ONE_PACK];

   private Card[] cards; // cards in the deck
   private int topCard; // keep track of number of cards in the deck
   private int numPacks; // keep track of number of packs in the deck

   /**
    * Constructor that takes in a number of packs as an argument and then creates a
    * deck of cards with that many packs of cards (56 x numPacks)
    * 
    * @param numPacks
    */
   public Deck(int numPacks) {
      // maximum packs is 6
      if ((numPacks * ONE_PACK) > MAX_CARDS)
         numPacks = 6;

      this.numPacks = numPacks;

      allocateMasterPack();

      // create the cards array with (52+4) x numPacks cards
      // standard is 52 cards but possible add 4 jokers per pack
      cards = new Card[numPacks * (ONE_PACK + 4)];

      // populate the cards in the deck
      init(numPacks);
   }

   /*
    * Overloaded no argument constructor that creates a pack of cards using just
    * one deck
    */
   public Deck() {
      this.numPacks = 1;

      allocateMasterPack();

      // Create the cards array using one pack of cards
      cards = new Card[ONE_PACK + 4]; // add 4 joker spaces

      // Initialize the last index of the array to be the top card of the deck
      topCard = ONE_PACK; // no jokers that will be added if needed

      // Loop through the cards array, populating it with Cards
      for (int i = 0; i < topCard; i++) {
         // Create a new Card Object, copying it from the masterPack
         cards[i] = new Card(masterPack[i]);
      }
   }

   /**
    * Method to re-populate cards[] with 52 x numPacks cards.
    * 
    * @param numPacks
    */
   public void init(int numPacks) {

      // Initialize the last index of the array to be the top card of the deck
      topCard = numPacks * ONE_PACK; // no jokers that can be added when needed

      this.numPacks = numPacks;

      // Populate the card array with Card objects, copying values from
      // masterPack
      for (int masterCounter = 0, i = 0; i < topCard; i++, masterCounter++) {
         // Create a new Card Object, copying it from the masterPack
         cards[i] = new Card(masterPack[masterCounter]);

         // If the cards array is more than one pack, reset the index of
         // masterPack
         // in order to loop through it again
         if (masterCounter == ONE_PACK - 1)
            masterCounter = -1;
      }
   }

   /**
    * Shuffles the deck of Cards
    */
   public void shuffle() {
      Random shuffle = new Random();
      Card tempCard;
      int randCard;

      // loops through the entire deck
      for (int x = 0; x < topCard; x++) {
         // Picks a random card from the deck
         randCard = shuffle.nextInt(ONE_PACK);
         // assigns the random card to a placeholder
         tempCard = cards[randCard];
         // assigns the random card to the next card in the deck
         cards[randCard] = cards[x];
         // assigns the next card in the deck to the card in
         // the place holder
         cards[x] = tempCard;
      }
   }

   /**
    * Deals a card by taking the top of the deck and makes sure there are still
    * cards available.
    * 
    * @return the top Card from the deck.
    */
   public Card dealCard() {
      Card dealCard;

      // checks if there are cards in the deck
      if (topCard > 0) {
         // assigns the top card to the dealCard variable
         dealCard = inspectCard(topCard - 1);

         // removes the topcard from the deck
         cards[topCard - 1] = null;

         // decreases card count
         topCard--;
         return dealCard;
      }
      // returns null if no more cards
      return null;
   }

   /**
    * Returns the number of cards in a deck.
    * 
    * @return the number of cards in the deck
    */
   public int getTopCard() {
      return topCard;
   }

   /**
    * Accessor for an individual card. Returns a card or returns a card with an
    * error flag.
    * 
    * @return the card at index k
    * @return a card with with an error flag
    */
   public Card inspectCard(int k)
   {
      Card returnCard;

      // If k is out of bounds, return a card with an error flag
      if (k < 0 || k >= topCard)
      {
         // Create an invalid card with errorFlag = true
         returnCard = new Card('E', Card.Suit.CLUBS);
      }
      else
      {
         // Otherwise return the card at k index
         returnCard = cards[k];
      }
      return returnCard;
   }

   /**
    * This method creates new cards and fills the masterPack.
    */
   private static void allocateMasterPack()
   {
      // Check if this method has already been run. Return if it has.
      if (beenHereBefore)
         return;

      char[] value =
      { 'A', '2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K' };

      Card.Suit[] suits =
      { Card.Suit.CLUBS, Card.Suit.DIAMONDS, Card.Suit.HEARTS,
         Card.Suit.SPADES };

      int curIndex = 0;

      // Loop through the value array
      for (int x = 0; x < value.length; x++)
      {
         // Loop through the suits array
         for (int y = 0; y < suits.length; y++)
         {
            // Create a new Card object with the correct suit and value
            masterPack[curIndex] = new Card(value[x], suits[y]);
            curIndex++;
         }
      }

      beenHereBefore = true;
   }

   /**
    * Adds the card to the top of the deck based on the number of packs
    * 
    * @param card the card to be added
    * @return false if the card is already there or no rooms, otherwise return
    *         true
    */
   public boolean addCard(Card card)
   {
      if (cards.length == topCard)
      {
         return false; // no room for the card
      }

      for (int i = 0; i < topCard; i++)
      {
         if (cards[i].equals(card))
         {
            return false; // the card is there already
         }
      }

      for (int n = 0; n < numPacks; n++)
      {
         cards[topCard] = card; // add the card for each pack
         topCard++;
      }
      return true;
   }

   /**
    * Remove all the instances of a specific card from the deck
    * 
    * @param card the specific card
    * @return true if success, otherwise false
    */
   public boolean removeCard(Card card)
   {

      boolean found = false; // card may not be in the deck
      int i = 0;

      // remove all the instances of the card
      while (i < topCard)
      {
         if (cards[i].equals(card))
         {
            // Replace the card with the top card of the deck
            // Don't increment i since we need to check the replacement card
            cards[i] = cards[topCard - 1];
            cards[topCard - 1] = null;
            topCard--;
            found = true;
         }
         else
         {
            i++;
         }
      }
      return found;
   }

   public void sort()
   {
      Card.arraySort(cards, topCard);
   }

   public int getNumCards()
   {
      return topCard;
   }
}
/*-----------------------------------------------------
 * End Of Deck
 *----------------------------------------------*/
