package java_cup.runtime;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public class ComplexSymbolFactory implements SymbolFactory {
   public Symbol newSymbol(String name, int id, ComplexSymbolFactory.Location left, ComplexSymbolFactory.Location right, Object value) {
      return new ComplexSymbolFactory.ComplexSymbol(name, id, left, right, value);
   }

   public Symbol newSymbol(String name, int id, ComplexSymbolFactory.Location left, ComplexSymbolFactory.Location right) {
      return new ComplexSymbolFactory.ComplexSymbol(name, id, left, right);
   }

   public Symbol newSymbol(String name, int id, Symbol left, Object value) {
      return new ComplexSymbolFactory.ComplexSymbol(name, id, left, value);
   }

   public Symbol newSymbol(String name, int id, Symbol left, Symbol right, Object value) {
      return new ComplexSymbolFactory.ComplexSymbol(name, id, left, right, value);
   }

   public Symbol newSymbol(String name, int id, Symbol left, Symbol right) {
      return new ComplexSymbolFactory.ComplexSymbol(name, id, left, right);
   }

   public Symbol newSymbol(String name, int id) {
      return new ComplexSymbolFactory.ComplexSymbol(name, id);
   }

   public Symbol newSymbol(String name, int id, Object value) {
      return new ComplexSymbolFactory.ComplexSymbol(name, id, value);
   }

   public Symbol startSymbol(String name, int id, int state) {
      return new ComplexSymbolFactory.ComplexSymbol(name, id, state);
   }

   public static class ComplexSymbol extends Symbol {
      protected String name;
      public ComplexSymbolFactory.Location xleft;
      public ComplexSymbolFactory.Location xright;

      public ComplexSymbol(String name, int id) {
         super(id);
         this.name = name;
      }

      public ComplexSymbol(String name, int id, Object value) {
         super(id, value);
         this.name = name;
      }

      public String toString() {
         return this.xleft != null && this.xright != null ? "Symbol: " + this.name + " (" + this.xleft + " - " + this.xright + ")" : "Symbol: " + this.name;
      }

      public String getName() {
         return this.name;
      }

      public ComplexSymbol(String name, int id, int state) {
         super(id, state);
         this.name = name;
      }

      public ComplexSymbol(String name, int id, Symbol left, Symbol right) {
         super(id, left, right);
         this.name = name;
         if (left != null) {
            this.xleft = ((ComplexSymbolFactory.ComplexSymbol)left).xleft;
         }

         if (right != null) {
            this.xright = ((ComplexSymbolFactory.ComplexSymbol)right).xright;
         }

      }

      public ComplexSymbol(String name, int id, ComplexSymbolFactory.Location left, ComplexSymbolFactory.Location right) {
         super(id, left.offset, right.offset);
         this.name = name;
         this.xleft = left;
         this.xright = right;
      }

      public ComplexSymbol(String name, int id, Symbol left, Symbol right, Object value) {
         super(id, left.left, right.right, value);
         this.name = name;
         if (left != null) {
            this.xleft = ((ComplexSymbolFactory.ComplexSymbol)left).xleft;
         }

         if (right != null) {
            this.xright = ((ComplexSymbolFactory.ComplexSymbol)right).xright;
         }

      }

      public ComplexSymbol(String name, int id, Symbol left, Object value) {
         super(id, left.right, left.right, value);
         this.name = name;
         if (left != null) {
            this.xleft = ((ComplexSymbolFactory.ComplexSymbol)left).xright;
            this.xright = ((ComplexSymbolFactory.ComplexSymbol)left).xright;
         }

      }

      public ComplexSymbol(String name, int id, ComplexSymbolFactory.Location left, ComplexSymbolFactory.Location right, Object value) {
         super(id, left.offset, right.offset, value);
         this.name = name;
         this.xleft = left;
         this.xright = right;
      }

      public ComplexSymbolFactory.Location getLeft() {
         return this.xleft;
      }

      public ComplexSymbolFactory.Location getRight() {
         return this.xright;
      }
   }

   public static class Location {
      private String unit;
      private int line;
      private int column;
      private int offset;

      public Location(ComplexSymbolFactory.Location other) {
         this(other.unit, other.line, other.column, other.offset);
      }

      public Location(String unit, int line, int column, int offset) {
         this(unit, line, column);
         this.offset = offset;
      }

      public Location(String unit, int line, int column) {
         this.unit = "unknown";
         this.offset = -1;
         this.unit = unit;
         this.line = line;
         this.column = column;
      }

      public Location(int line, int column, int offset) {
         this(line, column);
         this.offset = offset;
      }

      public Location(int line, int column) {
         this.unit = "unknown";
         this.offset = -1;
         this.line = line;
         this.column = column;
      }

      public int getColumn() {
         return this.column;
      }

      public int getLine() {
         return this.line;
      }

      public void move(int linediff, int coldiff, int offsetdiff) {
         if (this.line >= 0) {
            this.line += linediff;
         }

         if (this.column >= 0) {
            this.column += coldiff;
         }

         if (this.offset >= 0) {
            this.offset += offsetdiff;
         }

      }

      public static ComplexSymbolFactory.Location clone(ComplexSymbolFactory.Location other) {
         return new ComplexSymbolFactory.Location(other);
      }

      public String getUnit() {
         return this.unit;
      }

      public String toString() {
         return this.getUnit() + ":" + this.getLine() + "/" + this.getColumn() + "(" + this.offset + ")";
      }

      public void toXML(XMLStreamWriter writer, String orientation) throws XMLStreamException {
         writer.writeStartElement("location");
         writer.writeAttribute("compilationunit", this.unit);
         writer.writeAttribute("orientation", orientation);
         writer.writeAttribute("linenumber", String.valueOf(this.line));
         writer.writeAttribute("columnnumber", String.valueOf(this.column));
         writer.writeAttribute("offset", String.valueOf(this.offset));
         writer.writeEndElement();
      }

      public int getOffset() {
         return this.offset;
      }
   }
}
