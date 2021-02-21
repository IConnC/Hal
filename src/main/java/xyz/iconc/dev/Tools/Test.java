package xyz.iconc.dev.Tools;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Test {
    private static final Logger logger = LogManager.getLogger(Test.class);

    public static void main (String [] args) throws InterruptedException {
        int number = 50;
        System.out.println(Math.ceil((float) number / 1000f));
        System.out.println("----------");
        while (number > 0) {
            System.out.println((float) number / 1000f);
        }
        /** String message = "Hello there!";
        logger.trace(message);
        logger.debug(message);
        logger.info(message);
        logger.warn(message);
        logger.error(message);
        logger.fatal(message);
        for(int i = 0; i < 2000; i++) {
            logger.info("This is the " + i + " time I say 'Hello World'.");
            Thread.sleep(10000);
        }**/
    }

}
