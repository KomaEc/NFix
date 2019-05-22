package org.codehaus.groovy.runtime;

import java.util.Iterator;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlGroovyMethods {
   public static Iterator<Node> iterator(final NodeList nodeList) {
      return new Iterator<Node>() {
         private int current;

         public boolean hasNext() {
            return this.current < nodeList.getLength();
         }

         public Node next() {
            return nodeList.item(this.current++);
         }

         public void remove() {
            throw new UnsupportedOperationException("Cannot remove() from a NodeList iterator");
         }
      };
   }
}
