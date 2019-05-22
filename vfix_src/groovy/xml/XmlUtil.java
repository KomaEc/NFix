package groovy.xml;

import groovy.lang.Writable;
import groovy.util.Node;
import groovy.util.XmlNodePrinter;
import groovy.util.slurpersupport.GPathResult;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.codehaus.groovy.runtime.InvokerHelper;
import org.w3c.dom.Element;

public class XmlUtil {
   public static String serialize(Element element) {
      StringWriter sw = new StringWriter();
      serialize((Source)(new DOMSource(element)), (Writer)sw);
      return sw.toString();
   }

   public static void serialize(Element element, OutputStream os) {
      Source source = new DOMSource(element);
      serialize((Source)source, (OutputStream)os);
   }

   public static void serialize(Element element, Writer w) {
      Source source = new DOMSource(element);
      serialize((Source)source, (Writer)w);
   }

   public static String serialize(Node node) {
      return serialize(asString(node));
   }

   public static void serialize(Node node, OutputStream os) {
      serialize(asString(node), os);
   }

   public static void serialize(Node node, Writer w) {
      serialize(asString(node), w);
   }

   public static String serialize(GPathResult node) {
      return serialize(asString(node));
   }

   public static void serialize(GPathResult node, OutputStream os) {
      serialize(asString(node), os);
   }

   public static void serialize(GPathResult node, Writer w) {
      serialize(asString(node), w);
   }

   public static String serialize(Writable writable) {
      return serialize(asString(writable));
   }

   public static void serialize(Writable writable, OutputStream os) {
      serialize(asString(writable), os);
   }

   public static void serialize(Writable writable, Writer w) {
      serialize(asString(writable), w);
   }

   public static String serialize(String xmlString) {
      StringWriter sw = new StringWriter();
      serialize((Source)asStreamSource(xmlString), (Writer)sw);
      return sw.toString();
   }

   public static void serialize(String xmlString, OutputStream os) {
      serialize((Source)asStreamSource(xmlString), (OutputStream)os);
   }

   public static void serialize(String xmlString, Writer w) {
      serialize((Source)asStreamSource(xmlString), (Writer)w);
   }

   private static String asString(Node node) {
      StringWriter sw = new StringWriter();
      PrintWriter pw = new PrintWriter(sw);
      XmlNodePrinter nodePrinter = new XmlNodePrinter(pw);
      nodePrinter.setPreserveWhitespace(true);
      nodePrinter.print(node);
      return sw.toString();
   }

   private static String asString(GPathResult node) {
      try {
         Object builder = Class.forName("groovy.xml.StreamingMarkupBuilder").newInstance();
         Writable w = (Writable)InvokerHelper.invokeMethod(builder, "bindNode", node);
         return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + w.toString();
      } catch (Exception var3) {
         return "Couldn't convert node to string because: " + var3.getMessage();
      }
   }

   private static String asString(Writable writable) {
      if (writable instanceof GPathResult) {
         return asString((GPathResult)writable);
      } else {
         StringWriter sw = new StringWriter();

         try {
            writable.writeTo(sw);
         } catch (IOException var3) {
         }

         return sw.toString();
      }
   }

   private static StreamSource asStreamSource(String xmlString) {
      return new StreamSource(new StringReader(xmlString));
   }

   private static void serialize(Source source, OutputStream os) {
      try {
         serialize(source, new StreamResult(new OutputStreamWriter(os, "UTF-8")));
      } catch (UnsupportedEncodingException var3) {
      }

   }

   private static void serialize(Source source, Writer w) {
      serialize(source, new StreamResult(w));
   }

   private static void serialize(Source source, StreamResult target) {
      TransformerFactory factory = TransformerFactory.newInstance();
      setIndent(factory, 2);

      try {
         Transformer transformer = factory.newTransformer();
         transformer.setOutputProperty("indent", "yes");
         transformer.setOutputProperty("method", "xml");
         transformer.setOutputProperty("media-type", "text/xml");
         transformer.transform(source, target);
      } catch (TransformerException var4) {
      }

   }

   private static void setIndent(TransformerFactory factory, int indent) {
      try {
         factory.setAttribute("indent-number", indent);
      } catch (IllegalArgumentException var3) {
      }

   }
}
