package java_cup.runtime;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public abstract class XMLElement {
   protected String tagname;

   public abstract List<XMLElement> selectById(String var1);

   public static void dump(XMLStreamWriter writer, XMLElement elem, String... blacklist) throws XMLStreamException {
      dump((ScannerBuffer)null, writer, elem, blacklist);
   }

   public static void dump(ScannerBuffer buffer, XMLStreamWriter writer, XMLElement elem, String... blacklist) throws XMLStreamException {
      writer.writeStartDocument("utf-8", "1.0");
      writer.writeProcessingInstruction("xml-stylesheet", "href=\"tree.xsl\" type=\"text/xsl\"");
      writer.writeStartElement("document");
      if (blacklist.length > 0) {
         writer.writeStartElement("blacklist");
         String[] var7 = blacklist;
         int var6 = blacklist.length;

         for(int var5 = 0; var5 < var6; ++var5) {
            String s = var7[var5];
            writer.writeStartElement("symbol");
            writer.writeCharacters(s);
            writer.writeEndElement();
         }

         writer.writeEndElement();
      }

      writer.writeStartElement("parsetree");
      elem.dump(writer);
      writer.writeEndElement();
      if (buffer != null) {
         writer.writeStartElement("tokensequence");
         Iterator var9 = buffer.getBuffered().iterator();

         while(var9.hasNext()) {
            Symbol s = (Symbol)var9.next();
            if (s instanceof ComplexSymbolFactory.ComplexSymbol) {
               ComplexSymbolFactory.ComplexSymbol cs = (ComplexSymbolFactory.ComplexSymbol)s;
               if (cs.value != null) {
                  writer.writeStartElement("token");
                  writer.writeAttribute("name", cs.getName());
                  cs.getLeft().toXML(writer, "left");
                  writer.writeCharacters("" + cs.value);
                  cs.getRight().toXML(writer, "right");
                  writer.writeEndElement();
               } else {
                  writer.writeStartElement("keyword");
                  writer.writeAttribute("left", "" + cs.getLeft());
                  writer.writeAttribute("right", "" + cs.getRight());
                  writer.writeCharacters(cs.getName());
                  writer.writeEndElement();
               }
            } else if (s instanceof Symbol) {
               writer.writeStartElement("token");
               writer.writeCharacters(s.toString());
               writer.writeEndElement();
            }
         }

         writer.writeEndElement();
      }

      writer.writeEndElement();
      writer.writeEndDocument();
      writer.flush();
      writer.close();
   }

   public String getTagname() {
      return this.tagname;
   }

   public abstract ComplexSymbolFactory.Location right();

   public abstract ComplexSymbolFactory.Location left();

   protected abstract void dump(XMLStreamWriter var1) throws XMLStreamException;

   public List<XMLElement> getChildren() {
      return new LinkedList();
   }

   public boolean hasChildren() {
      return false;
   }

   public static class Error extends XMLElement {
      ComplexSymbolFactory.Location l;
      ComplexSymbolFactory.Location r;

      public boolean hasChildren() {
         return false;
      }

      public List<XMLElement> selectById(String s) {
         return new LinkedList();
      }

      public Error(ComplexSymbolFactory.Location l, ComplexSymbolFactory.Location r) {
         this.l = l;
         this.r = r;
      }

      public ComplexSymbolFactory.Location left() {
         return this.l;
      }

      public ComplexSymbolFactory.Location right() {
         return this.r;
      }

      public String toString() {
         return "<error left=\"" + this.l + "\" right=\"" + this.r + "\"/>";
      }

      protected void dump(XMLStreamWriter writer) throws XMLStreamException {
         writer.writeStartElement("error");
         writer.writeAttribute("left", "" + this.left());
         writer.writeAttribute("right", "" + this.right());
         writer.writeEndElement();
      }
   }

   public static class NonTerminal extends XMLElement {
      private int variant;
      LinkedList<XMLElement> list;

      public boolean hasChildren() {
         return !this.list.isEmpty();
      }

      public List<XMLElement> getChildren() {
         return this.list;
      }

      public List<XMLElement> selectById(String s) {
         LinkedList<XMLElement> response = new LinkedList();
         if (this.tagname.equals(s)) {
            response.add(this);
         }

         Iterator var4 = this.list.iterator();

         while(var4.hasNext()) {
            XMLElement e = (XMLElement)var4.next();
            List<XMLElement> selection = e.selectById(s);
            response.addAll(selection);
         }

         return response;
      }

      public int getVariant() {
         return this.variant;
      }

      public NonTerminal(String tagname, int variant, XMLElement... l) {
         this.tagname = tagname;
         this.variant = variant;
         this.list = new LinkedList(Arrays.asList(l));
      }

      public ComplexSymbolFactory.Location left() {
         Iterator var2 = this.list.iterator();

         while(var2.hasNext()) {
            XMLElement e = (XMLElement)var2.next();
            ComplexSymbolFactory.Location loc = e.left();
            if (loc != null) {
               return loc;
            }
         }

         return null;
      }

      public ComplexSymbolFactory.Location right() {
         Iterator it = this.list.descendingIterator();

         while(it.hasNext()) {
            ComplexSymbolFactory.Location loc = ((XMLElement)it.next()).right();
            if (loc != null) {
               return loc;
            }
         }

         return null;
      }

      public String toString() {
         if (this.list.isEmpty()) {
            return "<nonterminal id=\"" + this.tagname + "\" variant=\"" + this.variant + "\" />";
         } else {
            String ret = "<nonterminal id=\"" + this.tagname + "\" left=\"" + this.left() + "\" right=\"" + this.right() + "\" variant=\"" + this.variant + "\">";

            XMLElement e;
            for(Iterator var3 = this.list.iterator(); var3.hasNext(); ret = ret + e.toString()) {
               e = (XMLElement)var3.next();
            }

            return ret + "</nonterminal>";
         }
      }

      protected void dump(XMLStreamWriter writer) throws XMLStreamException {
         writer.writeStartElement("nonterminal");
         writer.writeAttribute("id", this.tagname);
         writer.writeAttribute("variant", String.valueOf(this.variant));
         ComplexSymbolFactory.Location loc = this.left();
         if (loc != null) {
            loc.toXML(writer, "left");
         }

         Iterator var4 = this.list.iterator();

         while(var4.hasNext()) {
            XMLElement e = (XMLElement)var4.next();
            e.dump(writer);
         }

         loc = this.right();
         if (loc != null) {
            loc.toXML(writer, "right");
         }

         writer.writeEndElement();
      }
   }

   public static class Terminal extends XMLElement {
      ComplexSymbolFactory.Location l;
      ComplexSymbolFactory.Location r;
      Object value;

      public boolean hasChildren() {
         return false;
      }

      public List<XMLElement> selectById(String s) {
         List<XMLElement> ret = new LinkedList();
         if (this.tagname.equals(s)) {
            ret.add(this);
         }

         return ret;
      }

      public Terminal(ComplexSymbolFactory.Location l, String symbolname, ComplexSymbolFactory.Location r) {
         this(l, symbolname, (Object)null, r);
      }

      public Terminal(ComplexSymbolFactory.Location l, String symbolname, Object i, ComplexSymbolFactory.Location r) {
         this.l = l;
         this.r = r;
         this.value = i;
         this.tagname = symbolname;
      }

      public Object value() {
         return this.value;
      }

      public ComplexSymbolFactory.Location left() {
         return this.l;
      }

      public ComplexSymbolFactory.Location right() {
         return this.r;
      }

      public String toString() {
         return this.value == null ? "<terminal id=\"" + this.tagname + "\"/>" : "<terminal id=\"" + this.tagname + "\" left=\"" + this.l + "\" right=\"" + this.r + "\">" + this.value + "</terminal>";
      }

      protected void dump(XMLStreamWriter writer) throws XMLStreamException {
         writer.writeStartElement("terminal");
         writer.writeAttribute("id", this.tagname);
         writer.writeAttribute("left", "" + this.left());
         writer.writeAttribute("right", "" + this.right());
         if (this.value != null) {
            writer.writeCharacters("" + this.value);
         }

         writer.writeEndElement();
      }
   }
}
