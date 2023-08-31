package project.test.coms;

import org.junit.jupiter.api.Test;
import project.app.coms.msg.StaticMessage;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MessageTest {

    /**
     * Test for constructing message
     */
    @Test
    public void constructMsg(){
        String expected = "testing123";
        StaticMessage<String> msg = new StaticMessage<>("testing123");
        assertEquals(expected, msg.getContent());
    }
}
