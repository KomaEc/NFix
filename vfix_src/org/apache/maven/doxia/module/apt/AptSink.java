package org.apache.maven.doxia.module.apt;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.Stack;
import org.apache.maven.doxia.sink.AbstractTextSink;
import org.apache.maven.doxia.util.HtmlTools;
import org.codehaus.plexus.util.StringUtils;

public class AptSink extends AbstractTextSink implements AptMarkup {
   private StringBuffer buffer = new StringBuffer();
   private StringBuffer tableCaptionBuffer = new StringBuffer();
   private String author;
   private String title;
   private String date;
   private boolean tableCaptionFlag;
   private boolean headerFlag;
   private boolean bufferFlag;
   private boolean itemFlag;
   private boolean verbatimFlag;
   private boolean boxed;
   private boolean gridFlag;
   private int cellCount;
   private PrintWriter writer;
   private int[] cellJustif;
   private String rowLine;
   private String listNestingIndent;
   private Stack listStyles;

   public AptSink(Writer writer) {
      this.writer = new PrintWriter(writer);
      this.listNestingIndent = "";
      this.listStyles = new Stack();
   }

   protected StringBuffer getBuffer() {
      return this.buffer;
   }

   protected void setHeadFlag(boolean headFlag) {
      this.headerFlag = headFlag;
   }

   protected void resetState() {
      this.headerFlag = false;
      this.resetBuffer();
      this.itemFlag = false;
      this.verbatimFlag = false;
      this.cellCount = 0;
   }

   protected void resetBuffer() {
      this.buffer = new StringBuffer();
   }

   protected void resetTableCaptionBuffer() {
      this.tableCaptionBuffer = new StringBuffer();
   }

   public void head() {
      this.resetState();
      this.headerFlag = true;
   }

   public void head_() {
      this.headerFlag = false;
      this.write(HEADER_START_MARKUP + EOL);
      if (this.title != null) {
         this.write(" " + this.title + EOL);
      }

      this.write(HEADER_START_MARKUP + EOL);
      if (this.author != null) {
         this.write(" " + this.author + EOL);
      }

      this.write(HEADER_START_MARKUP + EOL);
      if (this.date != null) {
         this.write(" " + this.date + EOL);
      }

      this.write(HEADER_START_MARKUP + EOL);
   }

   public void title_() {
      if (this.buffer.length() > 0) {
         this.title = this.buffer.toString();
         this.resetBuffer();
      }

   }

   public void author_() {
      if (this.buffer.length() > 0) {
         this.author = this.buffer.toString();
         this.resetBuffer();
      }

   }

   public void date_() {
      if (this.buffer.length() > 0) {
         this.date = this.buffer.toString();
         this.resetBuffer();
      }

   }

   public void section1_() {
      this.write(EOL);
   }

   public void section2_() {
      this.write(EOL);
   }

   public void section3_() {
      this.write(EOL);
   }

   public void section4_() {
      this.write(EOL);
   }

   public void section5_() {
      this.write(EOL);
   }

   public void sectionTitle1() {
      this.write(EOL);
   }

   public void sectionTitle1_() {
      this.write(EOL + EOL);
   }

   public void sectionTitle2() {
      this.write(EOL + SECTION_TITLE_START_MARKUP);
   }

   public void sectionTitle2_() {
      this.write(EOL + EOL);
   }

   public void sectionTitle3() {
      this.write(EOL + StringUtils.repeat(SECTION_TITLE_START_MARKUP, 2));
   }

   public void sectionTitle3_() {
      this.write(EOL + EOL);
   }

   public void sectionTitle4() {
      this.write(EOL + StringUtils.repeat(SECTION_TITLE_START_MARKUP, 3));
   }

   public void sectionTitle4_() {
      this.write(EOL + EOL);
   }

   public void sectionTitle5() {
      this.write(EOL + StringUtils.repeat(SECTION_TITLE_START_MARKUP, 4));
   }

   public void sectionTitle5_() {
      this.write(EOL + EOL);
   }

   public void list() {
      this.listNestingIndent = this.listNestingIndent + " ";
      this.listStyles.push(LIST_START_MARKUP);
      this.write(EOL);
   }

   public void list_() {
      if (this.listNestingIndent.length() <= 1) {
         this.write(EOL + this.listNestingIndent + LIST_END_MARKUP + EOL);
      } else {
         this.write(EOL);
      }

      this.listNestingIndent = StringUtils.chomp(this.listNestingIndent, " ");
      this.listStyles.pop();
      this.itemFlag = false;
   }

   public void listItem() {
      this.numberedListItem();
      this.itemFlag = true;
   }

   public void listItem_() {
      this.write(EOL);
   }

   public void numberedList(int numbering) {
      this.listNestingIndent = this.listNestingIndent + " ";
      this.write(EOL);
      String style;
      switch(numbering) {
      case 0:
      default:
         style = String.valueOf('1');
         break;
      case 1:
         style = String.valueOf('a');
         break;
      case 2:
         style = String.valueOf('A');
         break;
      case 3:
         style = String.valueOf('i');
         break;
      case 4:
         style = String.valueOf('I');
      }

      this.listStyles.push(style);
   }

   public void numberedList_() {
      if (this.listNestingIndent.length() <= 1) {
         this.write(EOL + this.listNestingIndent + LIST_END_MARKUP + EOL);
      } else {
         this.write(EOL);
      }

      this.listNestingIndent = StringUtils.chomp(this.listNestingIndent, " ");
      this.listStyles.pop();
      this.itemFlag = false;
   }

   public void numberedListItem() {
      String style = (String)this.listStyles.peek();
      if (style.equals(String.valueOf('*'))) {
         this.write(EOL + this.listNestingIndent + '*' + ' ');
      } else {
         this.write(EOL + this.listNestingIndent + '[' + '[' + style + ']' + ']' + ' ');
      }

      this.itemFlag = true;
   }

   public void numberedListItem_() {
      this.write(EOL);
   }

   public void definitionList() {
      this.write(EOL);
   }

   public void definitionList_() {
      this.write(EOL);
      this.itemFlag = false;
   }

   public void definedTerm() {
      this.write(EOL + " [");
   }

   public void definedTerm_() {
      this.write("]");
   }

   public void definition() {
      this.itemFlag = true;
   }

   public void definition_() {
      this.write(EOL);
   }

   public void pageBreak() {
      this.write(EOL + '\f' + EOL);
   }

   public void paragraph() {
      if (!this.itemFlag) {
         this.write(EOL + " ");
      }

   }

   public void paragraph_() {
      if (this.itemFlag) {
         this.itemFlag = false;
      } else {
         this.write(EOL + EOL);
      }

   }

   public void verbatim(boolean boxed) {
      this.verbatimFlag = true;
      this.boxed = boxed;
      if (boxed) {
         this.write(EOL + BOXED_VERBATIM_START_MARKUP + EOL);
      } else {
         this.write(EOL + NON_BOXED_VERBATIM_START_MARKUP + EOL);
      }

   }

   public void verbatim_() {
      if (this.boxed) {
         this.write(EOL + BOXED_VERBATIM_END_MARKUP + EOL);
      } else {
         this.write(EOL + NON_BOXED_VERBATIM_END_MARKUP + EOL);
      }

      this.boxed = false;
      this.verbatimFlag = false;
   }

   public void horizontalRule() {
      this.write(EOL + HORIZONTAL_RULE_MARKUP + EOL);
   }

   public void table() {
      this.write(EOL);
   }

   public void table_() {
      if (this.rowLine != null) {
         this.write(this.rowLine);
      }

      this.rowLine = null;
      if (this.tableCaptionBuffer.length() > 0) {
         this.text(this.tableCaptionBuffer.toString() + EOL);
      }

      this.resetTableCaptionBuffer();
   }

   public void tableRows(int[] justification, boolean grid) {
      this.cellJustif = justification;
      this.gridFlag = grid;
   }

   public void tableRows_() {
      this.cellJustif = null;
      this.gridFlag = false;
   }

   public void tableRow() {
      this.bufferFlag = true;
      this.cellCount = 0;
   }

   public void tableRow_() {
      this.bufferFlag = false;
      this.buildRowLine();
      this.write(this.rowLine);
      if (this.gridFlag) {
         this.write(TABLE_ROW_SEPARATOR_MARKUP);
      }

      this.write(this.buffer.toString());
      this.resetBuffer();
      this.write(EOL);
      this.cellCount = 0;
   }

   private void buildRowLine() {
      StringBuffer rowLine = new StringBuffer();
      rowLine.append(TABLE_ROW_START_MARKUP);

      for(int i = 0; i < this.cellCount; ++i) {
         if (this.cellJustif != null) {
            switch(this.cellJustif[i]) {
            case 1:
               rowLine.append(TABLE_COL_LEFT_ALIGNED_MARKUP);
               break;
            case 2:
               rowLine.append(TABLE_COL_RIGHT_ALIGNED_MARKUP);
               break;
            default:
               rowLine.append(TABLE_COL_CENTERED_ALIGNED_MARKUP);
            }
         } else {
            rowLine.append(TABLE_COL_CENTERED_ALIGNED_MARKUP);
         }
      }

      rowLine.append(EOL);
      this.rowLine = rowLine.toString();
   }

   public void tableCell() {
      this.tableCell(false);
   }

   public void tableHeaderCell() {
      this.tableCell(true);
   }

   public void tableCell(boolean headerRow) {
      if (headerRow) {
         this.buffer.append(TABLE_CELL_SEPARATOR_MARKUP);
      }

   }

   public void tableCell_() {
      this.tableCell_(false);
   }

   public void tableHeaderCell_() {
      this.tableCell_(true);
   }

   private void tableCell_(boolean headerRow) {
      this.buffer.append(TABLE_CELL_SEPARATOR_MARKUP);
      ++this.cellCount;
   }

   public void tableCaption() {
      this.tableCaptionFlag = true;
   }

   public void tableCaption_() {
      this.tableCaptionFlag = false;
   }

   public void figureCaption_() {
      this.write(EOL);
   }

   public void figureGraphics(String name) {
      this.write(EOL + "[" + name + "] ");
   }

   public void anchor(String name) {
      this.write(ANCHOR_START_MARKUP);
   }

   public void anchor_() {
      this.write(ANCHOR_END_MARKUP);
   }

   public void link(String name) {
      if (!this.headerFlag) {
         this.write(LINK_START_1_MARKUP);
         this.text(name);
         this.write(LINK_START_2_MARKUP);
      }

   }

   public void link_() {
      if (!this.headerFlag) {
         this.write(LINK_END_MARKUP);
      }

   }

   public void link(String name, String target) {
      if (!this.headerFlag) {
         this.write(LINK_START_1_MARKUP);
         this.text(target);
         this.write(LINK_START_2_MARKUP);
         this.text(name);
      }

   }

   public void italic() {
      if (!this.headerFlag) {
         this.write(ITALIC_START_MARKUP);
      }

   }

   public void italic_() {
      if (!this.headerFlag) {
         this.write(ITALIC_END_MARKUP);
      }

   }

   public void bold() {
      if (!this.headerFlag) {
         this.write(BOLD_START_MARKUP);
      }

   }

   public void bold_() {
      if (!this.headerFlag) {
         this.write(BOLD_END_MARKUP);
      }

   }

   public void monospaced() {
      if (!this.headerFlag) {
         this.write(MONOSPACED_START_MARKUP);
      }

   }

   public void monospaced_() {
      if (!this.headerFlag) {
         this.write(MONOSPACED_END_MARKUP);
      }

   }

   public void lineBreak() {
      if (!this.headerFlag && !this.bufferFlag) {
         this.write("\\" + EOL);
      } else {
         this.buffer.append(EOL);
      }

   }

   public void nonBreakingSpace() {
      if (!this.headerFlag && !this.bufferFlag) {
         this.write(NON_BREAKING_SPACE_MARKUP);
      } else {
         this.buffer.append(NON_BREAKING_SPACE_MARKUP);
      }

   }

   public void text(String text) {
      if (this.tableCaptionFlag) {
         this.tableCaptionBuffer.append(text);
      } else if (!this.headerFlag && !this.bufferFlag) {
         if (this.verbatimFlag) {
            this.verbatimContent(text);
         } else {
            this.content(text);
         }
      } else {
         this.buffer.append(text);
      }

   }

   public void rawText(String text) {
      this.write(text);
   }

   protected void write(String text) {
      this.writer.write(text);
   }

   protected void content(String text) {
      this.write(escapeAPT(text));
   }

   protected void verbatimContent(String text) {
      this.write(escapeAPT(text));
   }

   public static String encodeFragment(String text) {
      return HtmlTools.encodeFragment(text);
   }

   public static String encodeURL(String text) {
      return HtmlTools.encodeURL(text);
   }

   public void flush() {
      this.writer.flush();
   }

   public void close() {
      this.writer.close();
   }

   private static String escapeAPT(String text) {
      if (text == null) {
         return "";
      } else {
         int length = text.length();
         StringBuffer buffer = new StringBuffer(length);

         for(int i = 0; i < length; ++i) {
            char c = text.charAt(i);
            switch(c) {
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
            case '}':
            case '~':
               buffer.append('\\');
               buffer.append(c);
               break;
            default:
               if (c > 127) {
                  buffer.append("\\u");
                  String hex = Integer.toHexString(c);
                  if (hex.length() == 2) {
                     buffer.append("00");
                  } else if (hex.length() == 3) {
                     buffer.append("0");
                  }

                  buffer.append(hex);
               } else {
                  buffer.append(c);
               }
            }
         }

         return buffer.toString();
      }
   }
}
