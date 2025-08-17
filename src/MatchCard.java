import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Objects;
import javax.swing.*;


public class MatchCard {
    class Card{
        private String name;
        private ImageIcon imageIcon;

        Card(String cardName, ImageIcon cardImageIcon){
            this.name = cardName;
            this.imageIcon = cardImageIcon;
        }

        public String toString(){
            return name;
        }
    }

//    track cardname

    String[] cardlist ={
            "darkness",
            "double",
            "fairy",
            "fighting",
            "fire",
            "grass",
            "lightning",
            "metal",
            "psychic",
            "water"
    };

    int rows = 4;
    int cols = 5;
    int cardWidth = 90;
    int cardHeight = 120;

    ArrayList<Card> cardSet;
    ImageIcon cardBackImageIcon;

    int boardWidth = cols * cardWidth;
    int boardHeight = rows * cardHeight;

    JFrame frame = new JFrame("Pokemon Match Cards");
    JLabel textLabel = new JLabel();
    JPanel textPanel = new JPanel();
    JPanel boardPanel = new JPanel();
    JPanel restartGamePanel = new JPanel();
    JButton restartButton = new JButton();

    int errorCount = 0;
    ArrayList<JButton> board;
    Timer hideCardTimer;
    boolean gameReady = false;
    JButton cardOneSelected;
    JButton cardTwoSelected;
    public MatchCard(){
        setUpCards();
        shuffleCard();
//        frame.setVisible(true);
        frame.setLayout(new BorderLayout());
        frame.setSize(boardWidth,boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        textLabel.setFont(new Font("Arial",Font.PLAIN,20));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setText("Errors: "+ Integer.toString(errorCount));

        textPanel.setPreferredSize(new Dimension(boardWidth,30));
        textPanel.add(textLabel);

        frame.add(textPanel,BorderLayout.NORTH);

//        Card Game board
        board = new ArrayList<JButton>();
        boardPanel.setLayout(new GridLayout(rows,cols));
        for(int i=0; i<cardSet.size(); i++){
            JButton tile = new JButton();
            tile.setPreferredSize(new Dimension(cardWidth,cardHeight));
            tile.setOpaque(true);
            tile.setIcon(cardSet.get(i).imageIcon);
            tile.setFocusable(false);
            tile.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(!gameReady){
                        return;
                    }
                    JButton tile = (JButton) e.getSource();
                    if(tile.getIcon() == cardBackImageIcon){
                        if(cardOneSelected == null){
                            cardOneSelected = tile;
                            int index = board.indexOf(cardOneSelected);
                            cardOneSelected.setIcon(cardSet.get(index).imageIcon);
                        }
                        else if(cardTwoSelected == null){
                            cardTwoSelected = tile;
                            int index = board.indexOf(cardTwoSelected);
                            cardTwoSelected.setIcon(cardSet.get(index).imageIcon);

                            if(cardOneSelected.getIcon() != cardTwoSelected.getIcon()){
                                errorCount += 1;
                                textLabel.setText("Errors: " + Integer.toString(errorCount));
                                hideCardTimer.start();
                            }
                            else{
                                cardOneSelected = null;
                                cardTwoSelected = null;
                            }
                        }
                    }
                }
            });
            board.add(tile);
            boardPanel.add(tile);
        }
        frame.add(boardPanel);

        restartButton.setFont(new Font("Arial",Font.PLAIN,16));
        restartButton.setText("Restart");
        restartButton.setPreferredSize(new Dimension(boardWidth,30));
        restartButton.setFocusable(false);
        restartButton.setEnabled(true);
        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!gameReady){
                    return;
                }
                gameReady = false;
                restartButton.setEnabled(false);
                cardTwoSelected = null;
                cardOneSelected = null;
                shuffleCard();

//                re assign buttons with new Cards
                for(int i=0; i<board.size(); i++){
                    board.get(i).setIcon(cardSet.get(i).imageIcon);
                }

                errorCount= 0;
                textLabel.setText("Errors: "+ Integer.toString(errorCount));
                hideCardTimer.start();
            }
        });
        restartGamePanel.add(restartButton);
        frame.add(restartGamePanel,BorderLayout.SOUTH);

        frame.pack();
        frame.setVisible(true);

        hideCardTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hideCards();
            }
        });
        hideCardTimer.setRepeats(false);
        hideCardTimer.start();
        System.out.println("Match cards");
    }

    private void setUpCards(){
        cardSet = new ArrayList<Card>();
        for(String cardName: cardlist){
//            load each card image
            Image cardImg = new ImageIcon(Objects.requireNonNull(getClass().getResource("./img/" + cardName + ".jpg"))).getImage();
            ImageIcon cardImageIcon= new ImageIcon(cardImg.getScaledInstance(cardWidth, cardHeight, Image.SCALE_SMOOTH));

//            create card object and add to cardSet
            Card card = new Card(cardName,cardImageIcon);
            cardSet.add(card);
        }
        cardSet.addAll(cardSet);

//        load the back card image
        Image cardbackImage = new ImageIcon(getClass().getResource("./img/back.jpg")).getImage();
        cardBackImageIcon = new ImageIcon(cardbackImage.getScaledInstance(cardWidth,cardHeight, Image.SCALE_SMOOTH));
    }

    private void shuffleCard(){
        for(int i=0; i<cardSet.size(); i++){
            int j = ((int) Math.random() * cardSet.size());
//            swap
            Card temp = cardSet.get(i);
            cardSet.set(i,cardSet.get(j));
            cardSet.set(j,temp);
        }
        System.out.println(cardSet);
    }

    private void hideCards(){
        if(gameReady && cardOneSelected != null && cardTwoSelected != null){
            cardOneSelected.setIcon(cardBackImageIcon);
            cardTwoSelected.setIcon(cardBackImageIcon);
            cardOneSelected = null;
            cardTwoSelected = null;
        }
        else{
            for(int i=0; i<cardSet.size(); i++){
                board.get(i).setIcon(cardBackImageIcon);
            }
            gameReady = true;
            restartButton.setEnabled(true);
        }

    }
}
