
public class CastTest {
  /* A constant with the same name appears in the interface CastFace. */
  public static final int NUM_STATES = 50;
  
  /* A method with the same prototype appears in the interface CastFace. */
  int getOne() {
    return 1;
  }
  
  public int getAnswer() {
    return -1;
  }
  
  public void useArgs(int i, boolean b) {
    return;
  }
  
  public void takeIn(int n) {
    return;
  }
  
  /* This method will be overridden by the subclass. */
  public double areYouHappy() {
    return 0;
  }
  
  public static void main(String[] args) {
    System.out.println("Test");
    
    // Execute the main() method of CastSub
    CastSub.main(null);

    CastTest x1 = new CastTest();
    CastTest x2 = new CastTest();
    CastTest x3 = new CastTest();

    CastSub y1 = new CastSub();
    CastSub y2 = new CastSub();
    CastSub y3 = new CastSub();
    
    @SuppressWarnings("unused")
    CastTest[] xa = {x1, x2, x3};
    
    /*
     * CastSub[] ya = xa;            // compile-time error
     * CastSub[] ya = (CastSub[])xa; // must cast to subclass, runtime error
     * xa = ya;                      // can assign back
     */
                                  
    CastSub[] ya = new CastSub[]{y1, y2, y3};
    xa = ya;                      // no cast to superclass, no error
    //ya = xa;                    // cannot assign back
    
  }
}

interface CastFace {
  /* No problems here.  Even if NUM_STATES has the same value here
   * as in CastTest, there is no issue.
   */
  public static final int NUM_STATES = 13;
  
  abstract int getOne();
  
  /* This method will prevent CastSub from implementing CastFace and
   *  extending CastTest concurrently due to different return type.
   *  
   * abstract boolean getAnswer();
   */
  
  abstract void takeIn(boolean bool);
  
  abstract void useArgs(int i2, boolean b2);

}

class CastSub extends CastTest implements CastFace {
  /* This method must have a 'public' identifier because the one in
   * CastFace is implicitly public and this method serves to
   * implement it.
   */
  @Override
  public int getOne() {
    return 1;
  }
  
  /* We must implement the takeIn() method from CastFace. No need to
   * override the takeIn() method from CastTest.
   */
  @Override
  public void takeIn(boolean bool) {
    return;
  }
  
  /* No problems here. */
  @Override
  public void useArgs(int i, boolean b) {
    return;
  }
  
  @Override
  public double areYouHappy() {
    return 1;
  }
  
  public static void main(String[] args) {
    System.out.println("Test2");
    
    /* Does not work because NUM_STATES is ambiguous.  Even if NUM_STATES
     * has the same value in the superclass and interface, this line
     * still will not compile.
     * 
     * System.out.println("The value of NUM_STATES is: " + NUM_STATES);
     */
    System.out.println("The value of NUM_STATES from CastTest: " +
        CastTest.NUM_STATES);
    System.out.println("The value of NUM_STATES from CastFace: " +
        CastFace.NUM_STATES);
    
    CastSub subClassVar = new CastSub();
    /* Java calls the subclass method, even with the cast to the
     * superclass.
     */
    System.out.println(((CastTest)subClassVar).areYouHappy());
    
    /* Causes a run-time error because we can't cast the superclass
     * variable to the subclass.
     * 
     * CastTest superClassVar = new CastTest();
     * System.out.println(((CastSub)superClassVar).areYouHappy());
     */
    
    CastSub anotherSubVar = new CastSub();
    CastTest holder = anotherSubVar; // implicit cast
    System.out.println(holder.areYouHappy()); // calls subclass method
    /* The only way for the subclass to call the superclass method is
     * to include the word super in the overriding method.
     */
  }
}