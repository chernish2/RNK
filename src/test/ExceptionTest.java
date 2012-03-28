package test;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: chernish2
 * Date: 23.03.12
 * Time: 12:00
 * To change this template use File | Settings | File Templates.
 */
public class ExceptionTest {

    public static void main(String args[]){
        try {
            doErr();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private static void doErr() throws IOException{
        try {
            err();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private static void err() throws IOException {
        throw new IOException("shit happens");
    }
}
