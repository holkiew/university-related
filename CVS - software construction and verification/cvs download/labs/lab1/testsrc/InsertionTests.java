import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class InsertionTests {

    @Test
    public void testPush() {
        final MyIntegerList mil = new MyIntegerList();

        assertEquals(0, mil.size());
        assertEquals("[]", mil.toString());

        mil.push(1);
        assertEquals(1, mil.size());
        assertNotEquals("[]", mil.toString());
        assertEquals("[1]", mil.toString());

        mil.push(2);
        assertEquals(2, mil.size());
        assertEquals("[1,2]", mil.toString());

        mil.push(1);
        assertEquals(3, mil.size());
        assertEquals("[1,2,1]", mil.toString());

        mil.push(4);
        assertEquals(4, mil.size());
        assertEquals("[1,2,1,4]", mil.toString());
    }

}