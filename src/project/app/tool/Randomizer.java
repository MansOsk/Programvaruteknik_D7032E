package project.app.tool;

import java.util.*;

public class Randomizer {
    private final static Randomizer RANDOMIZER = new Randomizer();
    private final Random rnd = new Random();

    private Randomizer(){}

    public static int NextInt(int max){
        return RANDOMIZER.rnd.nextInt(max);
    }

    public static <T> ArrayList<T> PickRandomElements(ArrayList<T> elements, int amount){
        ArrayList<T> sum = new ArrayList<>();
        for(int i = amount; i > 0; i--){
            if(elements.isEmpty()){
                return sum;
            }
            int rnd = NextInt(elements.size());
            sum.add(elements.get(rnd));
            elements.remove(rnd);
        }
        return sum;
    }

    public static <T> ArrayList<T> shuffle(ArrayList<T> elements){
        for(int i = elements.size() - 1; i > 0; i--) {
            int index = NextInt(i + 1);
            T a = elements.get(index); elements.set(index, elements.get(i)); elements.set(i, a); // SWAP
        }
        return elements;
    }
}
