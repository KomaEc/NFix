package org.apache.maven.doxia.module.apt;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import org.apache.maven.doxia.macro.MacroExecutionException;
import org.apache.maven.doxia.macro.MacroRequest;
import org.apache.maven.doxia.macro.manager.MacroNotFoundException;
import org.apache.maven.doxia.markup.Markup;
import org.apache.maven.doxia.parser.AbstractTextParser;
import org.apache.maven.doxia.sink.Sink;
import org.apache.maven.doxia.sink.SinkAdapter;
import org.codehaus.plexus.util.IOUtil;
import org.codehaus.plexus.util.StringUtils;

public class AptParser extends AbstractTextParser implements AptMarkup {
   private static final int TITLE = 0;
   private static final int SECTION1 = 1;
   private static final int SECTION2 = 2;
   private static final int SECTION3 = 3;
   private static final int SECTION4 = 4;
   private static final int SECTION5 = 5;
   private static final int PARAGRAPH = 6;
   private static final int VERBATIM = 7;
   private static final int FIGURE = 8;
   private static final int TABLE = 9;
   private static final int LIST_ITEM = 10;
   private static final int NUMBERED_LIST_ITEM = 11;
   private static final int DEFINITION_LIST_ITEM = 12;
   private static final int HORIZONTAL_RULE = 13;
   private static final int PAGE_BREAK = 14;
   private static final int LIST_BREAK = 15;
   private static final int MACRO = 16;
   private static final String[] TYPE_NAMES = new String[]{"TITLE", "SECTION1", "SECTION2", "SECTION3", "SECTION4", "SECTION5", "PARAGRAPH", "VERBATIM", "FIGURE", "TABLE", "LIST_ITEM", "NUMBERED_LIST_ITEM", "DEFINITION_LIST_ITEM", "HORIZONTAL_RULE", "PAGE_BREAK", "LIST_BREAK", "MACRO"};
   private static final char[] SPACES = new char[]{' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '};
   public static final int TAB_WIDTH = 8;
   private String sourceContent;
   private AptSource source;
   private Sink sink;
   private String line;
   private AptParser.Block block;
   private String blockFileName;
   private int blockLineNumber;

   public void parse(Reader source, Sink sink) throws AptParseException {
      try {
         try {
            StringWriter contentWriter = new StringWriter();
            IOUtil.copy((Reader)source, (Writer)contentWriter);
            this.sourceContent = contentWriter.toString();
         } catch (IOException var4) {
            throw new AptParseException("IOException: " + var4.getMessage(), var4);
         }

         this.source = new AptReaderSource(new StringReader(this.sourceContent));
         this.sink = sink;
         this.blockFileName = null;
         this.blockLineNumber = -1;
         this.nextLine();
         this.nextBlock(true);
         this.traverseHead();
         this.traverseBody();
         this.source = null;
         this.sink = null;
      } catch (AptParseException var5) {
         throw new AptParseException(var5.getMessage(), this.getSourceName(), this.getSourceLineNumber(), var5);
      }
   }

   public String getSourceName() {
      return this.blockFileName;
   }

   public int getSourceLineNumber() {
      return this.blockLineNumber;
   }

   private void traverseHead() throws AptParseException {
      this.sink.head();
      if (this.block != null && this.block.getType() == 0) {
         this.block.traverse();
         this.nextBlock();
      }

      this.sink.head_();
   }

   private void traverseBody() throws AptParseException {
      this.sink.body();
      if (this.block != null) {
         this.traverseSectionBlocks();
      }

      while(this.block != null) {
         this.traverseSection(0);
      }

      this.sink.body_();
   }

   private void traverseSection(int level) throws AptParseException {
      if (this.block != null) {
         int type = 1 + level;
         this.expectedBlock(type);
         switch(level) {
         case 0:
            this.sink.section1();
            break;
         case 1:
            this.sink.section2();
            break;
         case 2:
            this.sink.section3();
            break;
         case 3:
            this.sink.section4();
            break;
         case 4:
            this.sink.section5();
         }

         this.block.traverse();
         this.nextBlock();
         this.traverseSectionBlocks();

         while(this.block != null && this.block.getType() > type) {
            this.traverseSection(level + 1);
         }

         switch(level) {
         case 0:
            this.sink.section1_();
            break;
         case 1:
            this.sink.section2_();
            break;
         case 2:
            this.sink.section3_();
            break;
         case 3:
            this.sink.section4_();
            break;
         case 4:
            this.sink.section5_();
         }

      }
   }

   private void traverseSectionBlocks() throws AptParseException {
      while(true) {
         if (this.block != null) {
            switch(this.block.getType()) {
            case 6:
            case 7:
            case 8:
            case 9:
            case 13:
            case 14:
            case 16:
               this.block.traverse();
               this.nextBlock();
               continue;
            case 10:
               this.traverseList();
               continue;
            case 11:
               this.traverseNumberedList();
               continue;
            case 12:
               this.traverseDefinitionList();
               continue;
            case 15:
               this.nextBlock();
               continue;
            }
         }

         return;
      }
   }

   private void traverseList() throws AptParseException {
      if (this.block != null) {
         this.expectedBlock(10);
         int listIndent = this.block.getIndent();
         this.sink.list();
         this.sink.listItem();
         this.block.traverse();
         this.nextBlock();

         label45:
         while(this.block != null) {
            int blockIndent = this.block.getIndent();
            switch(this.block.getType()) {
            case 6:
               if (blockIndent < listIndent) {
                  break label45;
               }
            case 7:
            case 8:
            case 9:
            case 13:
            case 14:
               this.block.traverse();
               this.nextBlock();
               break;
            case 10:
               if (blockIndent < listIndent) {
                  break label45;
               }

               if (blockIndent > listIndent) {
                  this.traverseList();
               } else {
                  this.sink.listItem_();
                  this.sink.listItem();
                  this.block.traverse();
                  this.nextBlock();
               }
               break;
            case 11:
               if (blockIndent < listIndent) {
                  break label45;
               }

               this.traverseNumberedList();
               break;
            case 12:
               if (blockIndent < listIndent) {
                  break label45;
               }

               this.traverseDefinitionList();
               break;
            case 15:
               if (blockIndent >= listIndent) {
                  this.nextBlock();
               }
            default:
               break label45;
            }
         }

         this.sink.listItem_();
         this.sink.list_();
      }
   }

   private void traverseNumberedList() throws AptParseException {
      if (this.block != null) {
         this.expectedBlock(11);
         int listIndent = this.block.getIndent();
         this.sink.numberedList(((AptParser.NumberedListItem)this.block).getNumbering());
         this.sink.numberedListItem();
         this.block.traverse();
         this.nextBlock();

         label45:
         while(this.block != null) {
            int blockIndent = this.block.getIndent();
            switch(this.block.getType()) {
            case 6:
               if (blockIndent < listIndent) {
                  break label45;
               }
            case 7:
            case 8:
            case 9:
            case 13:
            case 14:
               this.block.traverse();
               this.nextBlock();
               break;
            case 10:
               if (blockIndent < listIndent) {
                  break label45;
               }

               this.traverseList();
               break;
            case 11:
               if (blockIndent < listIndent) {
                  break label45;
               }

               if (blockIndent > listIndent) {
                  this.traverseNumberedList();
               } else {
                  this.sink.numberedListItem_();
                  this.sink.numberedListItem();
                  this.block.traverse();
                  this.nextBlock();
               }
               break;
            case 12:
               if (blockIndent < listIndent) {
                  break label45;
               }

               this.traverseDefinitionList();
               break;
            case 15:
               if (blockIndent >= listIndent) {
                  this.nextBlock();
               }
            default:
               break label45;
            }
         }

         this.sink.numberedListItem_();
         this.sink.numberedList_();
      }
   }

   private void traverseDefinitionList() throws AptParseException {
      if (this.block != null) {
         this.expectedBlock(12);
         int listIndent = this.block.getIndent();
         this.sink.definitionList();
         this.sink.definitionListItem();
         this.block.traverse();
         this.nextBlock();

         label45:
         while(this.block != null) {
            int blockIndent = this.block.getIndent();
            switch(this.block.getType()) {
            case 6:
               if (blockIndent < listIndent) {
                  break label45;
               }
            case 7:
            case 8:
            case 9:
            case 13:
            case 14:
               this.block.traverse();
               this.nextBlock();
               break;
            case 10:
               if (blockIndent < listIndent) {
                  break label45;
               }

               this.traverseList();
               break;
            case 11:
               if (blockIndent < listIndent) {
                  break label45;
               }

               this.traverseNumberedList();
               break;
            case 12:
               if (blockIndent < listIndent) {
                  break label45;
               }

               if (blockIndent > listIndent) {
                  this.traverseDefinitionList();
               } else {
                  this.sink.definition_();
                  this.sink.definitionListItem_();
                  this.sink.definitionListItem();
                  this.block.traverse();
                  this.nextBlock();
               }
               break;
            case 15:
               if (blockIndent >= listIndent) {
                  this.nextBlock();
               }
            default:
               break label45;
            }
         }

         this.sink.definition_();
         this.sink.definitionListItem_();
         this.sink.definitionList_();
      }
   }

   private void nextLine() throws AptParseException {
      this.line = this.source.getNextLine();
   }

   private void nextBlock() throws AptParseException {
      this.nextBlock(false);
   }

   private void nextBlock(boolean firstBlock) throws AptParseException {
      label118:
      while(this.line != null) {
         int length = this.line.length();
         int indent = 0;
         int i = 0;

         label115:
         while(true) {
            label120: {
               if (i < length) {
                  switch(this.line.charAt(i)) {
                  case '\t':
                     indent += 8;
                     break label120;
                  case ' ':
                     ++indent;
                     break label120;
                  case '~':
                     if (charAt(this.line, length, i + 1) == '~') {
                        i = length;
                        break;
                     }
                  default:
                     break label115;
                  }
               }

               if (i == length) {
                  this.nextLine();
               }
               continue label118;
            }

            ++i;
         }

         this.blockFileName = this.source.getName();
         this.blockLineNumber = this.source.getLineNumber();
         this.block = null;
         switch(this.line.charAt(i)) {
         case '\u000e':
            if (indent == 0) {
               this.block = new AptParser.PageBreak(indent, this.line);
            }
            break;
         case '%':
            if (indent == 0 && charAt(this.line, length, i + 1) == '{') {
               this.block = new AptParser.MacroBlock(indent, this.line);
            }
            break;
         case '*':
            if (indent == 0) {
               if (charAt(this.line, length, i + 1) == '-' && charAt(this.line, length, i + 2) == '-') {
                  this.block = new AptParser.Table(indent, this.line);
               } else if (charAt(this.line, length, i + 1) == '*') {
                  if (charAt(this.line, length, i + 2) == '*') {
                     if (charAt(this.line, length, i + 3) == '*') {
                        this.block = new AptParser.Section5(indent, this.line);
                     } else {
                        this.block = new AptParser.Section4(indent, this.line);
                     }
                  } else {
                     this.block = new AptParser.Section3(indent, this.line);
                  }
               } else {
                  this.block = new AptParser.Section2(indent, this.line);
               }
            } else {
               this.block = new AptParser.ListItem(indent, this.line);
            }
            break;
         case '+':
            if (indent == 0 && charAt(this.line, length, i + 1) == '-' && charAt(this.line, length, i + 2) == '-') {
               this.block = new AptParser.Verbatim(indent, this.line);
            }
            break;
         case '-':
            if (charAt(this.line, length, i + 1) == '-' && charAt(this.line, length, i + 2) == '-') {
               if (indent == 0) {
                  this.block = new AptParser.Verbatim(indent, this.line);
               } else if (firstBlock) {
                  this.block = new AptParser.Title(indent, this.line);
               }
            }
            break;
         case '=':
            if (indent == 0 && charAt(this.line, length, i + 1) == '=' && charAt(this.line, length, i + 2) == '=') {
               this.block = new AptParser.HorizontalRule(indent, this.line);
            }
            break;
         case '[':
            if (charAt(this.line, length, i + 1) == ']') {
               this.block = new AptParser.ListBreak(indent, this.line);
            } else if (indent == 0) {
               this.block = new AptParser.Figure(indent, this.line);
            } else if (charAt(this.line, length, i + 1) == '[') {
               byte numbering;
               switch(charAt(this.line, length, i + 2)) {
               case '1':
               default:
                  numbering = 0;
                  break;
               case 'A':
                  numbering = 2;
                  break;
               case 'I':
                  numbering = 4;
                  break;
               case 'a':
                  numbering = 1;
                  break;
               case 'i':
                  numbering = 3;
               }

               this.block = new AptParser.NumberedListItem(indent, this.line, numbering);
            } else {
               this.block = new AptParser.DefinitionListItem(indent, this.line);
            }
         }

         if (this.block == null) {
            if (indent == 0) {
               this.block = new AptParser.Section1(indent, this.line);
            } else {
               this.block = new AptParser.Paragraph(indent, this.line);
            }
         }

         return;
      }

      this.block = null;
   }

   private void expectedBlock(int type) throws AptParseException {
      int blockType = this.block.getType();
      if (blockType != type) {
         throw new AptParseException("expected " + TYPE_NAMES[type] + ", found " + TYPE_NAMES[blockType]);
      }
   }

   private static boolean isOctalChar(char c) {
      return c >= '0' && c <= '7';
   }

   private static boolean isHexChar(char c) {
      return c >= '0' && c <= '9' || c >= 'a' && c <= 'f' || c >= 'A' && c <= 'F';
   }

   private static char charAt(String string, int length, int i) {
      return i < length ? string.charAt(i) : '\u0000';
   }

   private static int skipSpace(String string, int length, int i) {
      while(true) {
         if (i < length) {
            switch(string.charAt(i)) {
            case '\t':
            case ' ':
               ++i;
               continue;
            }
         }

         return i;
      }
   }

   private static void doTraverseText(String text, int begin, int end, Sink sink) throws AptParseException {
      boolean anchor = false;
      boolean link = false;
      boolean italic = false;
      boolean bold = false;
      boolean monospaced = false;
      StringBuffer buffer = new StringBuffer(end - begin);

      for(int i = begin; i < end; ++i) {
         char c = text.charAt(i);
         switch(c) {
         case '<':
            if (!italic && !bold && !monospaced) {
               if (i + 1 < end && text.charAt(i + 1) == '<') {
                  if (i + 2 < end && text.charAt(i + 2) == '<') {
                     i += 2;
                     monospaced = true;
                     flushTraversed(buffer, sink);
                     sink.monospaced();
                     break;
                  }

                  ++i;
                  bold = true;
                  flushTraversed(buffer, sink);
                  sink.bold();
                  break;
               }

               italic = true;
               flushTraversed(buffer, sink);
               sink.italic();
               break;
            }

            buffer.append(c);
            break;
         case '>':
            if (monospaced && i + 2 < end && text.charAt(i + 1) == '>' && text.charAt(i + 2) == '>') {
               i += 2;
               monospaced = false;
               flushTraversed(buffer, sink);
               sink.monospaced_();
            } else if (bold && i + 1 < end && text.charAt(i + 1) == '>') {
               ++i;
               bold = false;
               flushTraversed(buffer, sink);
               sink.bold_();
            } else if (italic) {
               italic = false;
               flushTraversed(buffer, sink);
               sink.italic_();
            } else {
               buffer.append(c);
            }
            break;
         case '\\':
            if (i + 1 >= end) {
               buffer.append('\\');
            } else {
               char escaped = text.charAt(i + 1);
               int octalChars;
               switch(escaped) {
               case '\n':
               case '\r':
                  ++i;

                  while(i + 1 < end && Character.isWhitespace(text.charAt(i + 1))) {
                     ++i;
                  }

                  flushTraversed(buffer, sink);
                  sink.lineBreak();
                  break;
               case ' ':
                  ++i;
                  flushTraversed(buffer, sink);
                  sink.nonBreakingSpace();
                  break;
               case '*':
               case '+':
               case '-':
               case '<':
               case '=':
               case '>':
               case '[':
               case '\\':
               case ']':
               case '{':
               case '|':
               case '}':
               case '~':
                  ++i;
                  buffer.append(escaped);
                  break;
               case 'u':
                  if (i + 5 < end && isHexChar(text.charAt(i + 2)) && isHexChar(text.charAt(i + 3)) && isHexChar(text.charAt(i + 4)) && isHexChar(text.charAt(i + 5))) {
                     octalChars = 63;

                     try {
                        octalChars = Integer.parseInt(text.substring(i + 2, i + 6), 16);
                     } catch (NumberFormatException var17) {
                     }

                     i += 5;
                     buffer.append((char)octalChars);
                     break;
                  }

                  buffer.append('\\');
                  break;
               case 'x':
                  if (i + 3 < end && isHexChar(text.charAt(i + 2)) && isHexChar(text.charAt(i + 3))) {
                     octalChars = 63;

                     try {
                        octalChars = Integer.parseInt(text.substring(i + 2, i + 4), 16);
                     } catch (NumberFormatException var18) {
                     }

                     i += 3;
                     buffer.append((char)octalChars);
                     break;
                  }

                  buffer.append('\\');
                  break;
               default:
                  if (isOctalChar(escaped)) {
                     octalChars = 1;
                     if (isOctalChar(charAt(text, end, i + 2))) {
                        ++octalChars;
                        if (isOctalChar(charAt(text, end, i + 3))) {
                           ++octalChars;
                        }
                     }

                     int value = 63;

                     try {
                        value = Integer.parseInt(text.substring(i + 1, i + 1 + octalChars), 8);
                     } catch (NumberFormatException var16) {
                     }

                     i += octalChars;
                     buffer.append((char)value);
                  } else {
                     buffer.append('\\');
                  }
               }
            }
            break;
         case '{':
            if (!anchor && !link) {
               if (i + 1 < end && text.charAt(i + 1) == '{') {
                  ++i;
                  link = true;
                  flushTraversed(buffer, sink);
                  String linkAnchor = null;
                  if (i + 1 < end && text.charAt(i + 1) == '{') {
                     ++i;
                     StringBuffer buf = new StringBuffer();
                     i = skipTraversedLinkAnchor(text, i + 1, end, buf);
                     linkAnchor = buf.toString();
                  }

                  if (linkAnchor == null) {
                     linkAnchor = getTraversedLink(text, i + 1, end);
                  }

                  sink.link(linkAnchor);
                  break;
               }

               anchor = true;
               flushTraversed(buffer, sink);
               sink.anchor(getTraversedAnchor(text, i + 1, end));
               break;
            }

            buffer.append(c);
            break;
         case '}':
            if (link && i + 1 < end && text.charAt(i + 1) == '}') {
               ++i;
               link = false;
               flushTraversed(buffer, sink);
               sink.link_();
            } else if (anchor) {
               anchor = false;
               flushTraversed(buffer, sink);
               sink.anchor_();
            } else {
               buffer.append(c);
            }
            break;
         default:
            if (Character.isWhitespace(c)) {
               buffer.append(' ');

               while(i + 1 < end && Character.isWhitespace(text.charAt(i + 1))) {
                  ++i;
               }
            } else {
               buffer.append(c);
            }
         }
      }

      if (monospaced) {
         throw new AptParseException("missing '" + MONOSPACED_END_MARKUP + "'");
      } else if (bold) {
         throw new AptParseException("missing '" + BOLD_END_MARKUP + "'");
      } else if (italic) {
         throw new AptParseException("missing '" + ITALIC_END_MARKUP + "'");
      } else if (link) {
         throw new AptParseException("missing '" + LINK_END_MARKUP + "'");
      } else if (anchor) {
         throw new AptParseException("missing '" + ANCHOR_END_MARKUP + "'");
      } else {
         flushTraversed(buffer, sink);
      }
   }

   private static void flushTraversed(StringBuffer buffer, Sink sink) {
      if (buffer.length() > 0) {
         sink.text(buffer.toString());
         buffer.setLength(0);
      }

   }

   private static int skipTraversedLinkAnchor(String text, int begin, int end, StringBuffer linkAnchor) throws AptParseException {
      int i;
      label26:
      for(i = begin; i < end; ++i) {
         char c = text.charAt(i);
         switch(c) {
         case '\\':
            if (i + 1 < end) {
               ++i;
               linkAnchor.append(text.charAt(i));
            } else {
               linkAnchor.append('\\');
            }
            break;
         case '}':
            break label26;
         default:
            linkAnchor.append(c);
         }
      }

      if (i == end) {
         throw new AptParseException("missing '}'");
      } else {
         return i;
      }
   }

   private static String getTraversedLink(String text, int begin, int end) throws AptParseException {
      char previous2 = '{';
      char previous = '{';

      int i;
      for(i = begin; i < end; ++i) {
         char c = text.charAt(i);
         if (c == '}' && previous == '}' && previous2 != '\\') {
            break;
         }

         previous2 = previous;
         previous = c;
      }

      if (i == end) {
         throw new AptParseException("missing '{{'");
      } else {
         return doGetTraversedLink(text, begin, i - 1);
      }
   }

   private static String getTraversedAnchor(String text, int begin, int end) throws AptParseException {
      char previous = '{';

      int i;
      for(i = begin; i < end; ++i) {
         char c = text.charAt(i);
         if (c == '}' && previous != '\\') {
            break;
         }

         previous = c;
      }

      if (i == end) {
         throw new AptParseException("missing '}'");
      } else {
         return doGetTraversedLink(text, begin, i);
      }
   }

   private static String doGetTraversedLink(String text, int begin, int end) throws AptParseException {
      final StringBuffer buffer = new StringBuffer(end - begin);
      Sink sink = new SinkAdapter() {
         public void lineBreak() {
            buffer.append(' ');
         }

         public void nonBreakingSpace() {
            buffer.append(' ');
         }

         public void text(String text) {
            buffer.append(text);
         }
      };
      doTraverseText(text, begin, end, sink);
      return buffer.toString().trim();
   }

   private static String replaceAll(String string, String oldSub, String newSub) {
      StringBuffer replaced = new StringBuffer();
      int oldSubLength = oldSub.length();

      int begin;
      int end;
      for(begin = 0; (end = string.indexOf(oldSub, begin)) >= 0; begin = end + oldSubLength) {
         if (end > begin) {
            replaced.append(string.substring(begin, end));
         }

         replaced.append(newSub);
      }

      if (begin < string.length()) {
         replaced.append(string.substring(begin));
      }

      return replaced.toString();
   }

   private class MacroBlock extends AptParser.Block {
      public MacroBlock(int indent, String firstLine) throws AptParseException {
         super(16, indent);
         this.text = firstLine;
      }

      public void traverse() throws AptParseException {
         if (!AptParser.this.secondParsing) {
            String s = this.text;
            s = s.substring(2, s.length() - 1);
            s = this.escapeForMacro(s);
            String[] params = StringUtils.split(s, "|");
            String macroId = params[0];
            Map parameters = new HashMap();

            for(int i = 1; i < params.length; ++i) {
               String[] param = StringUtils.split(params[i], "=");
               String key = this.unescapeForMacro(param[0]);
               String value = this.unescapeForMacro(param[1]);
               parameters.put(key, value);
            }

            parameters.put("sourceContent", AptParser.this.sourceContent);
            AptParser aptParser = new AptParser();
            aptParser.setSecondParsing(true);
            parameters.put("parser", aptParser);
            MacroRequest request = new MacroRequest(parameters, AptParser.this.getBasedir());

            try {
               AptParser.this.executeMacro(macroId, request, AptParser.this.sink);
            } catch (MacroExecutionException var9) {
               throw new AptParseException("Unable to execute macro in the APT document", var9);
            } catch (MacroNotFoundException var10) {
               throw new AptParseException("Unable to find macro used in the APT document", var10);
            }
         }
      }

      private String escapeForMacro(String s) {
         if (s != null && s.length() >= 1) {
            String result = StringUtils.replace(s, "\\=", "\u0011");
            result = StringUtils.replace(result, "\\|", "\u0012");
            return result;
         } else {
            return s;
         }
      }

      private String unescapeForMacro(String s) {
         if (s != null && s.length() >= 1) {
            String result = StringUtils.replace(s, "\u0011", "=");
            result = StringUtils.replace(result, "\u0012", "|");
            return result;
         } else {
            return s;
         }
      }
   }

   private class PageBreak extends AptParser.Block {
      public PageBreak(int indent, String firstLine) throws AptParseException {
         super(14, indent);
      }

      public void traverse() throws AptParseException {
         AptParser.this.sink.pageBreak();
      }
   }

   private class HorizontalRule extends AptParser.Block {
      public HorizontalRule(int indent, String firstLine) throws AptParseException {
         super(13, indent);
      }

      public void traverse() throws AptParseException {
         AptParser.this.sink.horizontalRule();
      }
   }

   private class DefinitionListItem extends AptParser.Block {
      public DefinitionListItem(int indent, String firstLine) throws AptParseException {
         super(12, indent, firstLine);
      }

      public void traverse() throws AptParseException {
         int i = this.skipSpaceFrom(0);
         int j = this.skipFromLeftToRightBracket(i);
         AptParser.this.sink.definedTerm();
         this.traverseText(i + 1, j);
         AptParser.this.sink.definedTerm_();
         j = this.skipSpaceFrom(j + 1);
         if (j == this.textLength) {
            throw new AptParseException("no definition");
         } else {
            AptParser.this.sink.definition();
            AptParser.this.sink.paragraph();
            this.traverseText(j);
            AptParser.this.sink.paragraph_();
         }
      }
   }

   private class NumberedListItem extends AptParser.Block {
      private int numbering;

      public NumberedListItem(int indent, String firstLine, int number) throws AptParseException {
         super(11, indent, firstLine);
         this.numbering = number;
      }

      public int getNumbering() {
         return this.numbering;
      }

      public void traverse() throws AptParseException {
         AptParser.this.sink.paragraph();
         this.traverseText(this.skipItemNumber());
         AptParser.this.sink.paragraph_();
      }

      private int skipItemNumber() throws AptParseException {
         int i = this.skipSpaceFrom(0);

         for(char prevChar = ' '; i < this.textLength; ++i) {
            char c = this.text.charAt(i);
            if (c == ']' && prevChar == ']') {
               break;
            }

            prevChar = c;
         }

         if (i == this.textLength) {
            throw new AptParseException("missing ']]'");
         } else {
            return this.skipSpaceFrom(i + 1);
         }
      }
   }

   private class ListItem extends AptParser.Block {
      public ListItem(int indent, String firstLine) throws AptParseException {
         super(10, indent, firstLine);
      }

      public void traverse() throws AptParseException {
         AptParser.this.sink.paragraph();
         this.traverseText(this.skipLeadingBullets());
         AptParser.this.sink.paragraph_();
      }
   }

   private class Table extends AptParser.Block {
      public Table(int indent, String firstLine) throws AptParseException {
         super(9, indent, firstLine);
      }

      public void traverse() throws AptParseException {
         int captionIndex = -1;
         int nextLineIndex = 0;
         int init = 2;
         int[] justification = null;
         int rows = 0;
         int columns = 0;
         StringBuffer[] cells = null;
         boolean[] headers = null;
         AptParser.this.sink.table();

         while(nextLineIndex < this.textLength) {
            int i = this.text.indexOf("*--", nextLineIndex);
            if (i < 0) {
               captionIndex = nextLineIndex;
               break;
            }

            i = this.text.indexOf(10, nextLineIndex);
            String line;
            if (i < 0) {
               line = this.text.substring(nextLineIndex);
               nextLineIndex = this.textLength;
            } else {
               line = this.text.substring(nextLineIndex, i);
               nextLineIndex = i + 1;
            }

            int lineLength = line.length();
            if (line.indexOf("*--") == 0) {
               if (init == 2) {
                  init = 1;
                  justification = this.parseJustification(line, lineLength);
                  columns = justification.length;
                  cells = new StringBuffer[columns];
                  headers = new boolean[columns];

                  for(i = 0; i < columns; ++i) {
                     cells[i] = new StringBuffer();
                     headers[i] = false;
                  }
               } else if (this.traverseRow(cells, headers)) {
                  ++rows;
               }
            } else {
               if (init == 1) {
                  init = 0;
                  boolean grid = AptParser.charAt(line, lineLength, 0) == '|';
                  AptParser.this.sink.tableRows(justification, grid);
               }

               line = AptParser.replaceAll(line, "\\|", "\\174");
               StringTokenizer cellLines = new StringTokenizer(line, "|", true);
               i = 0;
               boolean processedGrid = false;

               while(cellLines.hasMoreTokens()) {
                  String cellLine = cellLines.nextToken();
                  if ("|".equals(cellLine)) {
                     if (processedGrid) {
                        headers[i] = true;
                     } else {
                        processedGrid = true;
                        headers[i] = false;
                     }
                  } else {
                     processedGrid = false;
                     cellLine = AptParser.replaceAll(cellLine, "\\ ", "\\240");
                     cellLine = cellLine.trim();
                     StringBuffer cell = cells[i];
                     if (cellLine.length() > 0) {
                        if (cell.length() > 0) {
                           cell.append("\\\n");
                        }

                        cell.append(cellLine);
                     }

                     ++i;
                     if (i == columns) {
                        break;
                     }
                  }
               }
            }
         }

         if (rows == 0) {
            throw new AptParseException("no table rows");
         } else {
            AptParser.this.sink.tableRows_();
            if (captionIndex >= 0) {
               AptParser.this.sink.tableCaption();
               AptParser.doTraverseText(this.text, captionIndex, this.textLength, AptParser.this.sink);
               AptParser.this.sink.tableCaption_();
            }

            AptParser.this.sink.table_();
         }
      }

      private int[] parseJustification(String jline, int lineLength) throws AptParseException {
         int columns = 0;
         int i = 2;

         while(i < lineLength) {
            switch(jline.charAt(i)) {
            case '*':
            case '+':
            case ':':
               ++columns;
            default:
               ++i;
            }
         }

         if (columns == 0) {
            throw new AptParseException("no columns specified");
         } else {
            int[] justification = new int[columns];
            columns = 0;

            for(int ix = 2; ix < lineLength; ++ix) {
               switch(jline.charAt(ix)) {
               case '*':
                  justification[columns++] = 0;
                  break;
               case '+':
                  justification[columns++] = 1;
                  break;
               case ':':
                  justification[columns++] = 2;
               }
            }

            return justification;
         }
      }

      private boolean traverseRow(StringBuffer[] cells, boolean[] headers) throws AptParseException {
         boolean traversed = false;

         int i;
         for(i = 0; i < cells.length; ++i) {
            if (cells[i].length() > 0) {
               traversed = true;
               break;
            }
         }

         if (traversed) {
            AptParser.this.sink.tableRow();

            for(i = 0; i < cells.length; ++i) {
               StringBuffer cell = cells[i];
               if (headers[i]) {
                  AptParser.this.sink.tableHeaderCell();
               } else {
                  AptParser.this.sink.tableCell();
               }

               if (cell.length() > 0) {
                  AptParser.doTraverseText(cell.toString(), 0, cell.length(), AptParser.this.sink);
                  cell.setLength(0);
               }

               if (headers[i]) {
                  AptParser.this.sink.tableHeaderCell_();
               } else {
                  AptParser.this.sink.tableCell_();
               }
            }

            AptParser.this.sink.tableRow_();
         }

         return traversed;
      }
   }

   private class Figure extends AptParser.Block {
      public Figure(int indent, String firstLine) throws AptParseException {
         super(8, indent, firstLine);
      }

      public void traverse() throws AptParseException {
         AptParser.this.sink.figure();
         int i = this.skipFromLeftToRightBracket(0);
         AptParser.this.sink.figureGraphics(this.text.substring(1, i));
         i = this.skipSpaceFrom(i + 1);
         if (i < this.textLength) {
            AptParser.this.sink.figureCaption();
            this.traverseText(i);
            AptParser.this.sink.figureCaption_();
         }

         AptParser.this.sink.figure_();
      }
   }

   private class Verbatim extends AptParser.Block {
      private boolean boxed;

      public Verbatim(int indent, String firstLine) throws AptParseException {
         super(7, indent, (String)null);
         StringBuffer buffer = new StringBuffer();
         char firstChar = firstLine.charAt(0);
         this.boxed = firstChar == '+';

         while(AptParser.this.line != null) {
            String l = AptParser.this.line;
            int length = l.length();
            if (AptParser.charAt(l, length, 0) == firstChar && AptParser.charAt(l, length, 1) == '-' && AptParser.charAt(l, length, 2) == '-') {
               AptParser.this.nextLine();
               break;
            }

            int column = 0;

            for(int i = 0; i < length; ++i) {
               char c = l.charAt(i);
               if (c == '\t') {
                  int prevColumn = column;
                  column = (column + 1 + 8 - 1) / 8 * 8;
                  buffer.append(AptParser.SPACES, 0, column - prevColumn);
               } else {
                  ++column;
                  buffer.append(c);
               }
            }

            buffer.append(Markup.EOL);
            AptParser.this.nextLine();
         }

         this.textLength = buffer.length();
         if (this.textLength > 0) {
            --this.textLength;
            buffer.setLength(this.textLength);
         }

         this.text = buffer.toString();
      }

      public void traverse() throws AptParseException {
         AptParser.this.sink.verbatim(this.boxed);
         AptParser.this.sink.text(this.text);
         AptParser.this.sink.verbatim_();
      }
   }

   private class Paragraph extends AptParser.Block {
      public Paragraph(int indent, String firstLine) throws AptParseException {
         super(6, indent, firstLine);
      }

      public void traverse() throws AptParseException {
         AptParser.this.sink.paragraph();
         this.traverseText(this.skipSpaceFrom(0));
         AptParser.this.sink.paragraph_();
      }
   }

   private class Section5 extends AptParser.Section {
      public Section5(int indent, String firstLine) throws AptParseException {
         super(5, indent, firstLine);
      }

      public void Title() {
         AptParser.this.sink.sectionTitle5();
      }

      public void Title_() {
         AptParser.this.sink.sectionTitle5_();
      }
   }

   private class Section4 extends AptParser.Section {
      public Section4(int indent, String firstLine) throws AptParseException {
         super(4, indent, firstLine);
      }

      public void Title() {
         AptParser.this.sink.sectionTitle4();
      }

      public void Title_() {
         AptParser.this.sink.sectionTitle4_();
      }
   }

   private class Section3 extends AptParser.Section {
      public Section3(int indent, String firstLine) throws AptParseException {
         super(3, indent, firstLine);
      }

      public void Title() {
         AptParser.this.sink.sectionTitle3();
      }

      public void Title_() {
         AptParser.this.sink.sectionTitle3_();
      }
   }

   private class Section2 extends AptParser.Section {
      public Section2(int indent, String firstLine) throws AptParseException {
         super(2, indent, firstLine);
      }

      public void Title() {
         AptParser.this.sink.sectionTitle2();
      }

      public void Title_() {
         AptParser.this.sink.sectionTitle2_();
      }
   }

   private class Section1 extends AptParser.Section {
      public Section1(int indent, String firstLine) throws AptParseException {
         super(1, indent, firstLine);
      }

      public void Title() {
         AptParser.this.sink.sectionTitle1();
      }

      public void Title_() {
         AptParser.this.sink.sectionTitle1_();
      }
   }

   private class Section extends AptParser.Block {
      public Section(int type, int indent, String firstLine) throws AptParseException {
         super(type, indent, firstLine);
      }

      public void traverse() throws AptParseException {
         this.Title();
         this.traverseText(this.skipLeadingBullets());
         this.Title_();
      }

      public void Title() {
         AptParser.this.sink.sectionTitle();
      }

      public void Title_() {
         AptParser.this.sink.sectionTitle_();
      }
   }

   private class Title extends AptParser.Block {
      public Title(int indent, String firstLine) throws AptParseException {
         super(0, indent, firstLine);
      }

      public void traverse() throws AptParseException {
         StringTokenizer lines = new StringTokenizer(this.text, Markup.EOL);
         int separator = -1;
         boolean firstLine = true;
         boolean title = false;
         boolean author = false;
         boolean date = false;

         label64:
         while(lines.hasMoreTokens()) {
            String line = lines.nextToken().trim();
            int lineLength = line.length();
            if (AptParser.charAt(line, lineLength, 0) == '-' && AptParser.charAt(line, lineLength, 1) == '-' && AptParser.charAt(line, lineLength, 2) == '-') {
               switch(separator) {
               case 0:
                  if (!title) {
                     throw new AptParseException("missing title");
                  }

                  AptParser.this.sink.title_();
                  break;
               case 1:
                  if (author) {
                     AptParser.this.sink.author_();
                  }
                  break;
               case 2:
                  break label64;
               }

               ++separator;
               firstLine = true;
            } else {
               if (firstLine) {
                  firstLine = false;
                  switch(separator) {
                  case 0:
                     title = true;
                     AptParser.this.sink.title();
                     break;
                  case 1:
                     author = true;
                     AptParser.this.sink.author();
                     break;
                  case 2:
                     date = true;
                     AptParser.this.sink.date();
                  }
               } else {
                  AptParser.this.sink.lineBreak();
               }

               AptParser.doTraverseText(line, 0, lineLength, AptParser.this.sink);
            }
         }

         switch(separator) {
         case 0:
            if (!title) {
               throw new AptParseException("missing title");
            }

            AptParser.this.sink.title_();
            break;
         case 1:
            if (author) {
               AptParser.this.sink.author_();
            }
            break;
         case 2:
            if (date) {
               AptParser.this.sink.date_();
            }
         }

      }
   }

   private class ListBreak extends AptParser.Block {
      public ListBreak(int indent, String firstLine) throws AptParseException {
         super(15, indent);
      }

      public void traverse() throws AptParseException {
         throw new AptParseException("internal error: traversing list break");
      }
   }

   private abstract class Block {
      protected int type;
      protected int indent;
      protected String text;
      protected int textLength;

      public Block(int type, int indent) throws AptParseException {
         this(type, indent, (String)null);
      }

      public Block(int type, int indent, String firstLine) throws AptParseException {
         this.type = type;
         this.indent = indent;
         AptParser.this.nextLine();
         if (firstLine == null) {
            this.text = null;
            this.textLength = 0;
         } else {
            StringBuffer buffer = new StringBuffer(firstLine);

            while(AptParser.this.line != null) {
               String l = AptParser.this.line;
               int length = l.length();
               int i = 0;
               int ix = AptParser.skipSpace(l, length, i);
               if (ix == length || AptParser.charAt(l, length, ix) == '~' && AptParser.charAt(l, length, ix + 1) == '~') {
                  AptParser.this.nextLine();
                  break;
               }

               buffer.append(Markup.EOL);
               buffer.append(l);
               AptParser.this.nextLine();
            }

            this.text = buffer.toString();
            this.textLength = this.text.length();
         }

      }

      public final int getType() {
         return this.type;
      }

      public final int getIndent() {
         return this.indent;
      }

      public abstract void traverse() throws AptParseException;

      protected void traverseText(int begin) throws AptParseException {
         this.traverseText(begin, this.text.length());
      }

      protected void traverseText(int begin, int end) throws AptParseException {
         AptParser.doTraverseText(this.text, begin, end, AptParser.this.sink);
      }

      protected int skipLeadingBullets() {
         int i;
         for(i = this.skipSpaceFrom(0); i < this.textLength && this.text.charAt(i) == '*'; ++i) {
         }

         return this.skipSpaceFrom(i);
      }

      protected int skipFromLeftToRightBracket(int i) throws AptParseException {
         char previous = '[';
         ++i;

         while(i < this.textLength) {
            char c = this.text.charAt(i);
            if (c == ']' && previous != '\\') {
               break;
            }

            previous = c;
            ++i;
         }

         if (i == this.textLength) {
            throw new AptParseException("missing ']'");
         } else {
            return i;
         }
      }

      protected final int skipSpaceFrom(int i) {
         return AptParser.skipSpace(this.text, this.textLength, i);
      }
   }
}
