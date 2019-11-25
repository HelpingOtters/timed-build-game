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
public class Phase3 implements ActionListener
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
      Phase3 theGame = new Phase3();
   }
   
   public Phase3()
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
      //Game game = new Game();  MAXHALBERT
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
         // cardButtons[card].addActionListener(game);  MAXHALBERT
         cardButtons[card].addActionListener(this);
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
      cardInPlay[0] = LowCardGame.getHand(0).inspectCard(0);
      cardInPlay[1] = LowCardGame.getHand(1).inspectCard(0);

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

   @Override
   public void actionPerformed(ActionEvent e) 
   {
         String cardPlayed = e.getActionCommand();
         int cardNum = Integer.parseInt(cardPlayed);
         
         System.out.println("Card Number selected: " + cardNum);
         
         Icon playerIcon = GUICard.getIcon(LowCardGame.getHand(1).inspectCard(cardNum));
         
         LowCardGame.getHand(0).sort();
         Icon compIcon = GUICard.getIcon(LowCardGame.getHand(0).inspectCard(0));
         
         //playedCardLabels[1] = new JLabel("You", JLabel.CENTER);
         //playedCardLabels[1].setIcon(playerIcon);
         playedCardLabels[1] = new JLabel(playerIcon);
         
         //playedCardLabels[0] = new JLabel("Computer", JLabel.CENTER);
         //playedCardLabels[0].setIcon(compIcon);
         playedCardLabels[0] = new JLabel(compIcon);
         
         myCardTable.pnlPlayArea.invalidate();
         myCardTable.pnlPlayArea.removeAll();
         myCardTable.pnlPlayArea.add(playedCardLabels[1]);
         // add line to delay
         myCardTable.pnlPlayArea.add(playedCardLabels[0]);
         JLabel text = new JLabel("You", JLabel.CENTER);
         myCardTable.pnlPlayArea.add(text);
         JLabel text1 = new JLabel("Computer", JLabel.CENTER);
         myCardTable.pnlPlayArea.add(text1);
         myCardTable.pnlPlayArea.repaint();
         
         // Remove the card button from the player area
         myCardTable.pnlHumanHand.invalidate();
         myCardTable.pnlHumanHand.remove(cardButtons[cardNum]);
         
         myCardTable.pnlHumanHand.repaint();
         
         // Remove a label from the computer area
         myCardTable.pnlComputerHand.invalidate();
         
         myCardTable.pnlComputerHand.remove(computerLabels[cardNum]);
         myCardTable.pnlComputerHand.repaint();
         
         myCardTable.setVisible(true);
         
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
         */
   }


}

/**
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
         /*
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
   */
