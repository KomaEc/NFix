

import java.io.InputStream;
import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;

import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;

import java.io.File;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

public class Main {

    private Parser parser = new AutoDetectParser();

    private InputStream getTestDocument(String name) {
        return TikaInputStream.get(Main.class.getResourceAsStream(
                "/test-documents/" + name));
    }
    private static class XMLResult {
        public final String xml;
        public final Metadata metadata;

        public XMLResult(String xml, Metadata metadata) {
            this.xml = xml;
            this.metadata = metadata;
      }
    }

    private XMLResult getXML(String name) throws Exception {
        StringWriter sw = new StringWriter();
        SAXTransformerFactory factory = (SAXTransformerFactory)
                 SAXTransformerFactory.newInstance();
        TransformerHandler handler = factory.newTransformerHandler();
        handler.getTransformer().setOutputProperty(OutputKeys.METHOD, "xml");
        handler.getTransformer().setOutputProperty(OutputKeys.INDENT, "yes");
        handler.setResult(new StreamResult(sw));

        // Try with a document containing various tables and formattings
        InputStream input = getTestDocument(name);
        try {
            Metadata metadata = new Metadata();
            parser.parse(input, handler, metadata, new ParseContext());
            return new XMLResult(sw.toString(), metadata);
        } finally {
            input.close();
        }
    }

    public void testWordNullStyle() throws Exception {
        String xml = getXML("testWORD_null_style.docx").xml;        
        if (xml.contains("Test av styrt dokument")) {
            System.out.println("good");
        }
    }

    public static void main(String... args) throws Exception {
        Main run = new Main();
        run.testWordNullStyle();
    }


}