package com.github.javaparser.printer;

import com.github.javaparser.Position;
import com.github.javaparser.utils.Utils;
import java.util.Deque;
import java.util.LinkedList;

public class SourcePrinter {
   private final String endOfLineCharacter;
   private final String indentation;
   private final int tabWidth;
   private final PrettyPrinterConfiguration.IndentType indentType;
   private final Deque<String> indents;
   private final Deque<String> reindentedIndents;
   private String lastPrintedIndent;
   private final StringBuilder buf;
   private Position cursor;
   private boolean indented;

   SourcePrinter() {
      this(new PrettyPrinterConfiguration());
   }

   SourcePrinter(final PrettyPrinterConfiguration configuration) {
      this.indents = new LinkedList();
      this.reindentedIndents = new LinkedList();
      this.lastPrintedIndent = "";
      this.buf = new StringBuilder();
      this.cursor = new Position(1, 0);
      this.indented = false;
      this.indentation = configuration.getIndent();
      this.endOfLineCharacter = configuration.getEndOfLineCharacter();
      this.tabWidth = configuration.getTabWidth();
      this.indentType = configuration.getIndentType();
      this.indents.push("");
   }

   public SourcePrinter indent() {
      String currentIndent = (String)this.indents.peek();
      switch(this.indentType) {
      case SPACES:
      case TABS_WITH_SPACE_ALIGN:
         this.indents.push(currentIndent + this.indentation);
         break;
      case TABS:
         this.indents.push(this.indentation + currentIndent);
         break;
      default:
         throw new AssertionError("Unhandled indent type");
      }

      return this;
   }

   public SourcePrinter indentWithAlignTo(int column) {
      this.indents.push(this.calculateIndentWithAlignTo(column));
      return this;
   }

   private String calculateIndentWithAlignTo(int column) {
      if (column < this.lastPrintedIndent.length()) {
         throw new IllegalStateException("Attempt to indent less than the previous indent.");
      } else {
         StringBuilder newIndent = new StringBuilder(this.lastPrintedIndent);
         switch(this.indentType) {
         case SPACES:
         case TABS_WITH_SPACE_ALIGN:
            while(newIndent.length() < column) {
               newIndent.append(' ');
            }

            return newIndent.toString();
         case TABS:
            int logicalIndentLength;
            for(logicalIndentLength = newIndent.length(); logicalIndentLength + this.tabWidth <= column; logicalIndentLength += this.tabWidth) {
               newIndent.insert(0, '\t');
            }

            while(logicalIndentLength < column) {
               newIndent.append(' ');
               ++logicalIndentLength;
            }

            StringBuilder fullTab = new StringBuilder();

            for(int i = 0; i < this.tabWidth; ++i) {
               fullTab.append(' ');
            }

            String fullTabString = fullTab.toString();
            if (newIndent.length() >= this.tabWidth && newIndent.substring(newIndent.length() - this.tabWidth).equals(fullTabString)) {
               int i = newIndent.indexOf(fullTabString);
               newIndent.replace(i, i + this.tabWidth, "\t");
            }
            break;
         default:
            throw new AssertionError("Unhandled indent type");
         }

         return newIndent.toString();
      }
   }

   public SourcePrinter unindent() {
      if (this.indents.isEmpty()) {
         throw new IllegalStateException("Indent/unindent calls are not well-balanced.");
      } else {
         this.indents.pop();
         return this;
      }
   }

   private void append(String arg) {
      this.buf.append(arg);
      this.cursor = this.cursor.withColumn(this.cursor.column + arg.length());
   }

   public SourcePrinter print(final String arg) {
      if (!this.indented) {
         this.lastPrintedIndent = (String)this.indents.peek();
         this.append(this.lastPrintedIndent);
         this.indented = true;
      }

      this.append(arg);
      return this;
   }

   public SourcePrinter println(final String arg) {
      this.print(arg);
      this.println();
      return this;
   }

   public SourcePrinter println() {
      this.buf.append(this.endOfLineCharacter);
      this.cursor = Position.pos(this.cursor.line + 1, 0);
      this.indented = false;
      return this;
   }

   public Position getCursor() {
      return this.cursor;
   }

   public String getSource() {
      return this.buf.toString();
   }

   public String toString() {
      return this.getSource();
   }

   public String normalizeEolInTextBlock(String content) {
      return Utils.normalizeEolInTextBlock(content, this.endOfLineCharacter);
   }

   public void reindentWithAlignToCursor() {
      String newIndent = this.calculateIndentWithAlignTo(this.cursor.column);
      this.reindentedIndents.push(this.indents.pop());
      this.indents.push(newIndent);
   }

   public void reindentToPreviousLevel() {
      if (this.reindentedIndents.isEmpty()) {
         throw new IllegalStateException("Reindent calls are not well-balanced.");
      } else {
         this.indents.pop();
         this.indents.push(this.reindentedIndents.pop());
      }
   }

   public void duplicateIndent() {
      this.indents.push(this.indents.peek());
   }
}
