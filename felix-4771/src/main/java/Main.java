import org.apache.felix.metatype.Attribute;

public class Main {

    public void testAttributeWithoutContentOk() throws Exception
    {
        Attribute attr = new Attribute();

        assert(attr.getContent() == null);

        // Null-values are ignored, so attribute remains empty...
        attr.addContent(null, false);

        assert(attr.getContent() == null);
    }

    public void testAttributeWithNullContentOk() throws Exception {
        Attribute attr = new Attribute();
        attr.addContent((String)null, false);

        assert attr.getContent() == null;

    }


    public void testAttributeWithContentOk() throws Exception {
        Attribute attr = new Attribute();
        attr.addContent("foo", false);

        assert attr.getContent() != null;

    }

    public static void main(String... args) throws Exception {
        Main run = new Main();
        run.testAttributeWithoutContentOk();
        run.testAttributeWithNullContentOk();
        run.testAttributeWithContentOk();
    }
}