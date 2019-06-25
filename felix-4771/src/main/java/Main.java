
import org.apache.felix.metatype.*;

public class Main {
    public void testAttributeWithoutContentOK() {
        Attribute attr = new Attribute();
        if (attr.getContent() == null) {
            System.out.println("ok");
        }
        attr.addContent(null, false);
        if (attr.getContent() == null) {
            System.out.println("ok");
        }
    }
    public static void main(String... args) {
        Main run = new Main();
        run.testAttributeWithoutContentOK();
    }
}
