import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OperationsTests {

    private MyIntegerList generatedPopulatedList(){
        final MyIntegerList mil = new MyIntegerList();

        mil.push(1);
        mil.push(2);
        mil.push(1);
        mil.push(6);
        mil.push(6);
        mil.push(7);
        mil.push(2);
        mil.push(2);
        mil.push(0);
        mil.push(5);

        return mil;
    }

    @Test
    void testSort() {
        final MyIntegerList mil = generatedPopulatedList();
        assertEquals(10, mil.size());
        assertEquals("[1,2,1,6,6,7,2,2,0,5]", mil.toString());


        mil.bubbleSort();
        assertEquals(10, mil.size());
        assertEquals("[0,1,1,2,2,2,5,6,6,7]", mil.toString());
    }


    @Test
    void size() {
        final MyIntegerList mil = generatedPopulatedList();
        assertEquals(10, mil.size());
        assertEquals("[1,2,1,6,6,7,2,2,0,5]", mil.toString());
    }


    @Test
    void sortedInsertion() {
        final MyIntegerList mil = generatedPopulatedList();
        mil.bubbleSort();
        mil.sortedInsertion(7);
        assertEquals("[0,1,1,2,2,2,5,6,6,7,0]", mil.toString());
        mil.sortedInsertion(3);
        assertEquals("[0,1,1,2,2,2,3,5,6,6,7,0]", mil.toString());
    }

    @Test
    void insertAt() {
        final MyIntegerList mil = generatedPopulatedList();
        mil.insertAt(55,5);
        assertEquals("[1,2,1,6,6,55,7,2,2,0,0]", mil.toString());
        mil.insertAt(99,1);
        assertEquals("[1,99,2,1,6,6,55,7,2,2,0,0]", mil.toString());
        mil.insertAt(100,0);
        assertEquals("[100,1,99,2,1,6,6,55,7,2,2,0,0]", mil.toString());
        mil.insertAt(999,14);
        assertEquals("[100,1,99,2,1,6,6,55,7,2,2,0,0,999]", mil.toString());
    }

    @Test
    void dequeue() {
        final MyIntegerList mil = generatedPopulatedList();
        int dequeue = mil.dequeue();
        assertEquals(5, dequeue);
        assertEquals("[1,2,1,6,6,7,2,2,0]", mil.toString());
        int dequeue2 = mil.dequeue();
        assertEquals("[1,2,1,6,6,7,2,2]", mil.toString());
        assertEquals(0, dequeue2);
    }

    @Test
    void indexOf() {
        final MyIntegerList mil = generatedPopulatedList();
        System.out.println(mil);
        //bug somewhere
        int i = mil.indexOf(7);
        System.out.println(i);
    }

    @Test
    void bubbleSort() {
        final MyIntegerList mil = generatedPopulatedList();
        mil.push(11);
        mil.push(3);
        mil.push(7);
        mil.push(-1);
        mil.push(-4);
        mil.bubbleSort();
        assertEquals("[-4,-1,0,1,1,2,2,2,3,5,6,6,7,7,11]", mil.toString());
    }

    @Test
    void elementsSum() {
        final MyIntegerList mil = new MyIntegerList(){{
            push(1); push(2); push(5);
        }};
        int i = mil.elementsSum();
        assertEquals(8, i);
    }

    @Test
    void elementsAvg() {
        final MyIntegerList mil = new MyIntegerList(){{
            push(1); push(2); push(5);
        }};
        assertEquals((double)(1+2+5)/3, mil.elementsAvg());
    }

    @Test
    void removeAt() {
        final MyIntegerList mil = new MyIntegerList(){{
            push(1); push(2); push(5);
        }};
        mil.removeAt(1);
        assertEquals("[1,5]", mil.toString());
    }

    @Test
    void removeRepetitions() {
        final MyIntegerList mil = generatedPopulatedList();
        mil.bubbleSort();
        mil.removeRepetitions();
        assertEquals("[0,1,2,5,6,7]", mil.toString());
    }

    @Test
    void isEmpty() {
        final MyIntegerList mil = new MyIntegerList(){{
            push(1); push(2); push(5);
        }};
        mil.pop();
        mil.pop();
        mil.pop();
        assertEquals(0, mil.size());
    }

    @Test
    void countDifferent() {
        final MyIntegerList mil = generatedPopulatedList();
        System.out.println(mil);
        int i = mil.countDifferent();
        System.out.println(i);
        assertEquals(6, mil.countDifferent());
    }

    @Test
    void toStringTest() {
    }
}