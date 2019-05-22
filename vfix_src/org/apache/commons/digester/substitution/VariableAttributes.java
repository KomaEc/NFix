package org.apache.commons.digester.substitution;

import java.util.ArrayList;
import org.xml.sax.Attributes;

public class VariableAttributes implements Attributes {
   private ArrayList values = new ArrayList(10);
   private Attributes attrs;
   private VariableExpander expander;

   public void init(Attributes attrs, VariableExpander expander) {
      this.attrs = attrs;
      this.expander = expander;
      this.values.clear();
   }

   public String getValue(int index) {
      if (index >= this.values.size()) {
         this.values.ensureCapacity(index + 1);

         for(int i = this.values.size(); i <= index; ++i) {
            this.values.add((Object)null);
         }
      }

      String s = (String)this.values.get(index);
      if (s == null) {
         s = this.attrs.getValue(index);
         if (s != null) {
            s = this.expander.expand(s);
            this.values.set(index, s);
         }
      }

      return s;
   }

   public String getValue(String qname) {
      int index = this.attrs.getIndex(qname);
      return index == -1 ? null : this.getValue(index);
   }

   public String getValue(String uri, String localname) {
      int index = this.attrs.getIndex(uri, localname);
      return index == -1 ? null : this.getValue(index);
   }

   public int getIndex(String qname) {
      return this.attrs.getIndex(qname);
   }

   public int getIndex(String uri, String localpart) {
      return this.attrs.getIndex(uri, localpart);
   }

   public int getLength() {
      return this.attrs.getLength();
   }

   public String getLocalName(int index) {
      return this.attrs.getLocalName(index);
   }

   public String getQName(int index) {
      return this.attrs.getQName(index);
   }

   public String getType(int index) {
      return this.attrs.getType(index);
   }

   public String getType(String qname) {
      return this.attrs.getType(qname);
   }

   public String getType(String uri, String localname) {
      return this.attrs.getType(uri, localname);
   }

   public String getURI(int index) {
      return this.attrs.getURI(index);
   }
}
