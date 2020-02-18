package Exceptions;

public class duplicateDirector extends Throwable {
    public void message(String r)
    {
        System.out.println("Произошла исключительная ситуация:\n"+r);
    }
}
