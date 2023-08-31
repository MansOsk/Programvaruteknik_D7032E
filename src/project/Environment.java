package project;

/**
 * Holding environment variables, keeping it simple. Can also be named options.
 * Inorder to simplify for teacher without adding them to your own environment.
 * .env files can be used with extension <a href="https://github.com/cdimascio/dotenv-java">...</a> but will avoid downloading external.
 * <p>
 * Theoretically can be controlled in application through option feature, not done practically.
 */
public class Environment {
    public static int MAX_PLAYERS = 4;  // Max players, human-players + bots. HOST 2 -> Fill with 2 bots to achieve 4.
    public static int WIN = 8; // Cards to achieve win condition.
    public static int MAX_HAND = 7; // Max cards in hand player can have.

    /**
     * Used if wanting to use own resources, without modifying on multiple locations.
     */
    public static final String PATH = "./resources/";
    public static final String GREEN_APPLES = "greenApples.txt";
    public static final String RED_APPLES = "redApples.txt";

    public static final int PORT = 2048;

    /**
     * Print incoming and outgoing traffic and check msg types for client.
     * Complete server printout.
     */
    public static final boolean DEBUG = false; // Prints operations in terminal, instructions and content from messages.

    /**
     * Where multiple cycles of testing is used (with random generated parameters). Must be greater than 0.
     *
     */
    public static final int TEST_PER_CASE = 2; // TEST_PER_CASE = 1 default. Recommended to use small for GameLoopTest.

    /**
     * Max size allocation for resources when testing. 7 cards in deck when testing etc.
     */
    public static final int TEST_MAX_SIZE = 7;

    /**
     * Test response, when replacing keyboard for user.
     * Bot can also be used only in testing, but want to test network func. with players.
     */
    public static final String TEST_RESPONSE = "0";
}
