package org.apache.commons.digester;

import org.xml.sax.Attributes;

public abstract class Rule {
   protected Digester digester = null;
   protected String namespaceURI = null;

   /** @deprecated */
   public Rule(Digester digester) {
      this.setDigester(digester);
   }

   public Rule() {
   }

   public Digester getDigester() {
      return this.digester;
   }

   public void setDigester(Digester digester) {
      this.digester = digester;
   }

   public String getNamespaceURI() {
      return this.namespaceURI;
   }

   public void setNamespaceURI(String namespaceURI) {
      this.namespaceURI = namespaceURI;
   }

   /** @deprecated */
   public void begin(Attributes attributes) throws Exception {
   }

   public void begin(String namespace, String name, Attributes attributes) throws Exception {
      this.begin(attributes);
   }

   /** @deprecated */
   public void body(String text) throws Exception {
   }

   public void body(String namespace, String name, String text) throws Exception {
      this.body(text);
   }

   /** @deprecated */
   public void end() throws Exception {
   }

   public void end(String namespace, String name) throws Exception {
      this.end();
   }

   public void finish() throws Exception {
   }
}
