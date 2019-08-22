
class CQueue {
	
  Queue queue;
  // add more fields if necessary

  // Implement all the methods below
  CQueue(int max);

  void enqueue(int v);

  int dequeue();
  
  boolean isFull();
  
  boolean isEmpty();

  public static void main(String args[]) {
    // launch N threads that "produce" an integer value 
    // and another N threads that "consume" an integer value from the queue.
    // as an example N can be 50.
  }
}