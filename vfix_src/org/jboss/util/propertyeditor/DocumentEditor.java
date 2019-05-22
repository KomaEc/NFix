package org.jboss.util.propertyeditor;

import java.beans.PropertyEditorSupport;
import java.io.IOException;
import java.io.StringReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.jboss.util.NestedRuntimeException;
import org.jboss.util.xml.DOMWriter;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class DocumentEditor extends PropertyEditorSupport {
   public String getAsText() {
      return DOMWriter.printNode((Node)this.getValue(), false);
   }

   public void setAsText(String text) {
      this.setValue(this.getAsDocument(text));
   }

   protected Document getAsDocument(String text) {
      try {
         DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
         DocumentBuilder db = dbf.newDocumentBuilder();
         StringReader sr = new StringReader(text);
         InputSource is = new InputSource(sr);
         Document d = db.parse(is);
         return d;
      } catch (ParserConfigurationException var7) {
         throw new NestedRuntimeException(var7);
      } catch (SAXException var8) {
         throw new NestedRuntimeException(var8);
      } catch (IOException var9) {
         throw new NestedRuntimeException(var9);
      }
   }
}
