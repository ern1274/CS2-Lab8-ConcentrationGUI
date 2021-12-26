package model;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
/**
 * The ConcButton class is the Button for Concentration.
 *
 * @author Ethan
 */
public class ConcButton extends Button {

    private int id;
    private ImageView pokemon;
    private Image ball = new Image(new FileInputStream("src/gui/resources/pokeball.png"));
    private Card card;
    private int index;

    /**
     * Initializes all of the button stuff specifically the index of the card and the image
     * and the card number
     * @param id: the cards number
     * @param card: the card itself
     * @param poke: the hashmap for pokemon images
     * @param index: the int that stored where the card was in the original card array
     * @throws FileNotFoundException
     */
    public ConcButton(int id, Card card, HashMap<Integer,ImageView> poke,int index) throws FileNotFoundException {
        HashMap<Integer,ImageView> poki = (HashMap<Integer, ImageView>) poke.clone();
        this.id = id;
        this.card = card;
        pokemon = new ImageView();
        pokemon.setImage(poki.get(id).getImage());
        this.index = index;
        if(!card.isFaceUp()) {
            this.setGraphic(new ImageView(ball));
        }
        else{
            this.setGraphic(pokemon);
        }

    }

    /**
     * returns the cards number
     * @return
     */
    public Integer getNum(){
        return id;
    }

    /**
     * returns the card that this button and pokemon is linked to
     * @return
     */
    public Card getCard(){
        return this.card;
    }

    /**
     * alters the picture based on the cards orientation
     * @return
     */
    public ImageView pic(){
        if(card.isFaceUp()){
            return pokemon;
        }
        else{
            return new ImageView(ball);
        }
    }

    /**
     * returns the index of where the card was in the original array of cards
     * @return
     */
    public int getIndex(){
        return index;
    }
}
