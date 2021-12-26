package gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.Light;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.Card;
import model.ConcButton;
import model.ConcentrationModel;
import model.Observer;
import java.awt.Point;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;


/**
 * The ConcentrationGUI application is the UI for Concentration.
 *
 * @author Ethan Nunez
 */
public class ConcentrationGUI extends Application
        implements Observer< ConcentrationModel, Object > {
    private ConcentrationModel model;
    GridPane grid = new GridPane();
    private Button reset = new Button("Reset");
    private Button undo = new Button("Undo");
    private Button cheat = new Button("Cheat");
    private Label action = new Label("");
    private TextField moves = new TextField("moves:");

    private Image abra = new Image(new FileInputStream("src/gui/resources/abra.png")); private Image bulb = new Image(new FileInputStream("src/gui/resources/bulbasaur.png"));
    private Image charm = new Image(new FileInputStream("src/gui/resources/charmander.png")); private Image jig = new Image(new FileInputStream("src/gui/resources/jigglypuff.png"));
    private Image meow = new Image(new FileInputStream("src/gui/resources/meowth.png")); private Image pik = new Image(new FileInputStream("src/gui/resources/pikachu.png"));
    private Image squ = new Image(new FileInputStream("src/gui/resources/squirtle.png")); private Image veno = new Image(new FileInputStream("src/gui/resources/venomoth.png"));
    private ArrayList<ImageView> pokemons = new ArrayList<>(Arrays.asList(new ImageView(abra),new ImageView(bulb),
            new ImageView(charm),new ImageView(jig),new ImageView(meow),new ImageView(pik),new ImageView(squ),new ImageView(veno)));
    private HashMap<Integer,ImageView> poke;

    /**
     * initializes the model
     * @throws FileNotFoundException
     */
    public ConcentrationGUI() throws FileNotFoundException {
        this.model = new ConcentrationModel();
        initializeGUI();
    }

    /**
     * adds the observers
     */
    public void initializeGUI(){
        this.model.addObserver(this);
        update(this.model,null);
    }

    /**
     * this sets up the pokemon hashmap where the number of each card and links it with the pokemon image
     * @throws Exception
     */
    @Override
    public void init() throws Exception{
        poke = new HashMap<>();
        int index = 0;
        for (Card card:model.getCards()) {
            card.toggleFace();
            Integer num = card.getNumber();
            card.toggleFace();
            if(!poke.containsKey(num)){
                    poke.put(num,pokemons.get(index));
                    index++;
            }

        }
    }
    /**
     * defines all of the visual stuff here, Gridpane, the buttons and such
     *
     * @param stage
     * @throws Exception
     */
    @Override
    public void start( Stage stage ) throws Exception {
        Stage anotherStage = new Stage();
        BorderPane border = new BorderPane();
        HBox bottom = new HBox();
        bottom.getChildren().addAll(reset,undo,cheat, moves); bottom.setSpacing(20);
        // initializes the gridpane grid
        gridinit(grid);

        // accesses each button and sets the event for each button
        for (Node node:grid.getChildren()) {
            ConcButton but = (ConcButton) node;
            but.setOnAction(event -> {
                if(model.howManyCardsUp() % 2==0){
                    model.undo();
                    model.undo();
                    for (Node noded:grid.getChildren()) {
                        ConcButton butd = (ConcButton) noded;
                        butd.setGraphic(butd.pic());
                    }
                }
                model.selectCard(but.getIndex());
                but.setGraphic(but.pic());


            });
        }
        //Undo Event
        undo.setOnAction(event ->{
            model.undo();
            for (Node node:grid.getChildren()) {
                ConcButton but = (ConcButton) node;
                but.setGraphic(but.pic());
            }
        });


        //Cheat Event
        cheat.setOnAction(event ->{
            GridPane gridcheat = new GridPane();
            int row = 0;
            int col = 0;
            for (Node node:grid.getChildren()) {
                ConcButton but = (ConcButton) node;
                Card clone = new Card(but.getNum(),true);
                try {
                    gridcheat.add(new ConcButton(but.getNum(),clone,poke,but.getIndex()),col,row);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                col++;
                if(col == 4){
                    row++;
                    col = 0;
                }
            }
            for (Node node:gridcheat.getChildren()) {
                ConcButton but = (ConcButton) node;
                but.getCard().setFaceUp();
                but.getCard().toggleFace(false);
                but.setGraphic(but.pic());
            }
            Scene cheat = new Scene(gridcheat);
            anotherStage.setScene(cheat);
            anotherStage.show();
        });


        //setting the initial stage and grid up
        border.setBottom(bottom);
        border.setTop(action);
        border.setCenter(grid);
        Scene scene = new Scene(border);
        stage.setScene( scene );
        stage.show();
    }

    /**
     * The update calls display board to check if the text needs to be changed
     * this is called after model selects a card.
     * @param concentrationModel
     * @param o
     */
    @Override
    public void update( ConcentrationModel concentrationModel, Object o ) {
        displayBoard( this.model.getMoveCount(),
                this.model.howManyCardsUp(),
                o == null ? this.model.getCards()
                        : this.model.getCheat(),
                false
        );

        // display a win if all cards are face up (not cheating)
        if ( this.model.getCards().stream()
                .allMatch( face -> face.isFaceUp() ) ) {
            action.setText( "YOU WIN!" );
        }
    }

    /**
     * displayBoard is the changer of text based on cards and the amount of moves
     * it activates after every card flipped
     * @param n
     * @param up
     * @param faces
     * @param cheat
     */
    private void displayBoard( int n, int up, ArrayList< Card > faces,
                               boolean cheat ) {
        moves.setText("moves: "+ String.valueOf(model.getMoveCount()));
        switch ( up ) {
            case 0:
                action.setText( "Select the first card." );
                break;
            case 1:
                action.setText( "Select the second card." );
                break;
            case 2:
                action.setText( "No Match: Undo or select a card." );
                break;

        }

    }

    /**
     * Initializes the original gridpane with buttons using the poke hashmap which holds the
     * pokemon images and the cards number and its index
     * @param grid: the gridpane
     * @return
     * @throws FileNotFoundException
     */
    private GridPane gridinit(GridPane grid) throws FileNotFoundException {
        int row = 0;
        int col = 0;
        int index = 0;
        for (Card card:model.getCards()) {
            card.toggleFace();
            int num = card.getNumber();
            card.toggleFace();
            grid.add(new ConcButton(num,card,poke,index),col,row);
            index++;
            col++;
            if(col == 4){
                col = 0;
                row++;
            }
        }
        return grid;
    }
    /**
     * main entry point launches the JavaFX GUI.
     *
     * @param args not used
     */
    public static void main( String[] args ) {
        Application.launch( args );
    }
}
