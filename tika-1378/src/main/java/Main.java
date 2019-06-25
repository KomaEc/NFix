
import org.apache.tika.language.translate.*;

public class Main {

    public void testSettersAndIsAvailable() throws Throwable {
        MicrosoftTranslator translator = new MicrosoftTranslator();
    	//try{
    		translator.setId("foo");
    		translator.setSecret("bar");
    	//}
    	//catch(Exception e){
    		//e.printStackTrace();
    		//fail(e.getMessage());
    	//}
    	//reset
    	translator = new MicrosoftTranslator();
    	//try{
    		translator.setSecret("bar");
    		translator.setId("foo");
    	//}
    	//catch(Exception e){
    	//	e.printStackTrace();
    	//	fail(e.getMessage());
    	//}
    }

    public static void main(String... args) throws Throwable {
        Main run = new Main();
        run.testSettersAndIsAvailable();
    }
}