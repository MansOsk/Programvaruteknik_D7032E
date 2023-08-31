package project.app.tool;

import project.Environment;
import project.app.exception.A2ABoundsException;
import project.app.exception.A2AIOException;
import project.app.object.cards.card.Card;
import project.app.object.cards.card.GenericCard;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class File {

    private final static String path = Environment.PATH;

    /**
     * Read's game resource file
     *
     * @param fileName Name of file for apple.
     * @return A generated list that contains every apple.
     * @throws A2AIOException File can't read.
     */
    public static ArrayList<Card> readApples(String fileName) throws A2AIOException {
        try{
            List<String> lines = Files.readAllLines(Paths.get(path+fileName), StandardCharsets.ISO_8859_1);
            Iterator<String> iter =  lines.iterator();

            ArrayList<Card> cards = new ArrayList<>();
            while(iter.hasNext()){
                cards.add(new GenericCard(iter.next()));
            }
            return cards;
        }catch (IOException e){
            throw new A2AIOException("Can't read " + fileName, e);
        }
    }

    /**
     * Read's game resource file based on maximum lines in params.
     *
     * @param fileName Name of file for apple.
     * @param max Max lines.
     * @return A generated list of every apple up to max.
     * @throws A2AIOException File can't read.
     * @throws A2ABoundsException Max is outside of maximum file line.
     */
    public static ArrayList<Card> readApples(String fileName, int max) throws A2AIOException, A2ABoundsException {
        ArrayList<Card> cards = readApples(fileName);
        ArrayList<Card> cardsNew = new ArrayList<>();

        for(int i = 0; i < max; i++){
            try{
                Card card = cards.get(i);
                cardsNew.add(card);
            }catch (IndexOutOfBoundsException e){
                throw new A2ABoundsException("Can't load " + e.getMessage());
            }
        }
        return cardsNew;
    }

    /**
     * Read's game resource file based on start and end of lines in params.
     *
     * @param fileName Name of file for apple.
     * @param start Start of file.
     * @param end End of file.
     * @return A generated list of every apple between start and end.
     * @throws A2AIOException File can't read.
     * @throws A2ABoundsException Start or end is outside of file scope.
     */
    public static ArrayList<Card> readApples(String fileName, int start, int end) throws A2AIOException, A2ABoundsException {
        ArrayList<Card> cards = readApples(fileName);
        ArrayList<Card> cardsNew = new ArrayList<>();

        for(int i = start - 1; i <= end; i++){
            Card card = cards.get(i);
            if(card == null){
                throw new A2ABoundsException();
            }
            cardsNew.add(card);
        }

        return cardsNew;
    }

}
