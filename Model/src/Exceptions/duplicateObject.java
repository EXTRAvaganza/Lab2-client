package Exceptions;

public class duplicateObject extends Throwable {
    public static void message(String r)
    {
        System.out.println("Произошла исключительная ситуация:\n"+r);
    }
}
