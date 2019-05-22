package org.apache.maven.doxia.module.xdoc;

import org.apache.maven.doxia.sink.SinkAdapter;
import org.apache.maven.doxia.sink.StructureSink;
import org.apache.maven.doxia.util.HtmlTools;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.xml.XMLWriter;

public class XmlWriterXdocSink extends SinkAdapter {
   private final XMLWriter writer;
   private StringBuffer buffer = new StringBuffer();
   private boolean headFlag;
   private boolean verbatimFlag;
   private int[] cellJustif;
   private int cellCount;
   private int itemFlag;
   private boolean sectionTitleFlag;

   public XmlWriterXdocSink(XMLWriter out) {
      if (out == null) {
         throw new IllegalArgumentException("Argument can't be null!");
      } else {
         this.writer = out;
      }
   }

   protected void resetState() {
      this.headFlag = false;
      this.buffer = new StringBuffer();
      this.itemFlag = 0;
      this.verbatimFlag = false;
      this.cellJustif = null;
      this.cellCount = 0;
      this.sectionTitleFlag = false;
   }

   public void head() {
      this.resetState();
      this.headFlag = true;
      this.writer.startElement("document");
      this.writer.startElement("properties");
   }

   public void head_() {
      this.headFlag = false;
      this.writer.endElement();
   }

   public void title_() {
      if (this.buffer.length() > 0) {
         this.writer.startElement("title");
         this.content(this.buffer.toString());
         this.writer.endElement();
         this.buffer = new StringBuffer();
      }

   }

   public void author_() {
      if (this.buffer.length() > 0) {
         this.writer.startElement("author");
         this.content(this.buffer.toString());
         this.writer.endElement();
         this.buffer = new StringBuffer();
      }

   }

   public void date_() {
      if (this.buffer.length() > 0) {
         this.writer.startElement("date");
         this.content(this.buffer.toString());
         this.writer.endElement();
         this.buffer = new StringBuffer();
      }

   }

   public void body() {
      this.writer.startElement("body");
   }

   public void body_() {
      this.writer.endElement();
      this.writer.endElement();
      this.resetState();
   }

   public void section1() {
      this.writer.startElement("section");
   }

   public void section2() {
      this.writer.startElement("subsection");
   }

   public void section3() {
      this.writer.startElement("subsection");
   }

   public void section4() {
      this.writer.startElement("subsection");
   }

   public void section5() {
      this.writer.startElement("subsection");
   }

   public void sectionTitle() {
      this.sectionTitleFlag = true;
      this.buffer = new StringBuffer();
   }

   public void sectionTitle_() {
      this.sectionTitleFlag = false;
      this.writer.addAttribute("name", this.buffer.toString());
   }

   public void section1_() {
      this.writer.endElement();
   }

   public void section2_() {
      this.writer.endElement();
   }

   public void section3_() {
      this.writer.endElement();
   }

   public void section4_() {
      this.writer.endElement();
   }

   public void section5_() {
      this.writer.endElement();
   }

   public void list() {
      this.writer.startElement("ul");
   }

   public void list_() {
      this.writer.endElement();
   }

   public void listItem() {
      this.writer.startElement("li");
      ++this.itemFlag;
   }

   public void listItem_() {
      this.writer.endElement();
   }

   public void numberedList(int numbering) {
      String style;
      switch(numbering) {
      case 0:
      default:
         style = "decimal";
         break;
      case 1:
         style = "lower-alpha";
         break;
      case 2:
         style = "upper-alpha";
         break;
      case 3:
         style = "lower-roman";
         break;
      case 4:
         style = "upper-roman";
      }

      this.writer.startElement("ol");
      this.writer.addAttribute("style", "list-style-type: " + style);
   }

   public void numberedList_() {
      this.writer.endElement();
   }

   public void numberedListItem() {
      this.writer.startElement("li");
      ++this.itemFlag;
   }

   public void numberedListItem_() {
      this.writer.endElement();
   }

   public void definitionList() {
      this.writer.startElement("dl");
      this.writer.addAttribute("compact", "compact");
   }

   public void definitionList_() {
      this.writer.endElement();
   }

   public void definedTerm() {
      this.writer.startElement("dt");
      this.writer.startElement("b");
   }

   public void definedTerm_() {
      this.writer.endElement();
      this.writer.endElement();
   }

   public void definition() {
      this.writer.startElement("dd");
      ++this.itemFlag;
   }

   public void definition_() {
      this.writer.endElement();
   }

   public void paragraph() {
      if (this.itemFlag == 0) {
         this.writer.startElement("p");
      }

   }

   public void paragraph_() {
      if (this.itemFlag == 0) {
         this.writer.endElement();
      } else {
         --this.itemFlag;
      }

   }

   public void verbatim(boolean boxed) {
      this.verbatimFlag = true;
      if (boxed) {
         this.writer.startElement("source");
      } else {
         this.writer.startElement("pre");
      }

   }

   public void verbatim_() {
      this.writer.endElement();
      this.verbatimFlag = false;
   }

   public void horizontalRule() {
      this.writer.startElement("hr");
      this.writer.endElement();
   }

   public void table() {
      this.writer.startElement("table");
      this.writer.addAttribute("align", "center");
   }

   public void table_() {
      this.writer.endElement();
   }

   public void tableRows(int[] justification, boolean grid) {
      this.writer.startElement("table");
      this.writer.addAttribute("align", "center");
      this.writer.addAttribute("border", String.valueOf(grid ? 1 : 0));
      this.cellJustif = justification;
   }

   public void tableRows_() {
      this.writer.endElement();
   }

   public void tableRow() {
      this.writer.startElement("tr");
      this.writer.addAttribute("valign", "top");
      this.cellCount = 0;
   }

   public void tableRow_() {
      this.writer.endElement();
      this.cellCount = 0;
   }

   public void tableCell() {
      this.tableCell(false);
   }

   public void tableHeaderCell() {
      this.tableCell(true);
   }

   public void tableCell(boolean headerRow) {
      String justif = null;
      if (this.cellJustif != null) {
         switch(this.cellJustif[this.cellCount]) {
         case 0:
         default:
            justif = "center";
            break;
         case 1:
            justif = "left";
            break;
         case 2:
            justif = "right";
         }
      }

      this.writer.startElement("t" + (headerRow ? 'h' : 'd'));
      if (justif != null) {
         this.writer.addAttribute("align", justif);
      }

   }

   public void tableCell_() {
      this.tableCell_(false);
   }

   public void tableHeaderCell_() {
      this.tableCell_(true);
   }

   public void tableCell_(boolean headerRow) {
      this.writer.endElement();
      ++this.cellCount;
   }

   public void tableCaption() {
      this.writer.startElement("p");
      this.writer.startElement("i");
   }

   public void tableCaption_() {
      this.writer.endElement();
      this.writer.endElement();
   }

   public void anchor(String name) {
      if (!this.headFlag) {
         String id = StructureSink.linkToKey(name);
         this.writer.startElement("a");
         this.writer.addAttribute("id", id);
         this.writer.addAttribute("name", id);
      }

   }

   public void anchor_() {
      if (!this.headFlag) {
         this.writer.endElement();
      }

   }

   public void link(String name) {
      if (!this.headFlag) {
         this.writer.startElement("a");
         this.writer.addAttribute("href", name);
      }

   }

   public void link_() {
      if (!this.headFlag) {
         this.writer.endElement();
      }

   }

   public void italic() {
      if (!this.headFlag) {
         this.writer.startElement("i");
      }

   }

   public void italic_() {
      if (!this.headFlag) {
         this.writer.endElement();
      }

   }

   public void bold() {
      if (!this.headFlag) {
         this.writer.startElement("b");
      }

   }

   public void bold_() {
      if (!this.headFlag) {
         this.writer.endElement();
      }

   }

   public void monospaced() {
      if (!this.headFlag) {
         this.writer.startElement("tt");
      }

   }

   public void monospaced_() {
      if (!this.headFlag) {
         this.writer.endElement();
      }

   }

   public void lineBreak() {
      if (this.headFlag) {
         this.buffer.append('\n');
      } else {
         this.writer.startElement("br");
         this.writer.endElement();
      }

   }

   public void nonBreakingSpace() {
      if (this.headFlag) {
         this.buffer.append(' ');
      } else {
         this.writer.writeText("&#160;");
      }

   }

   public void text(String text) {
      if (this.headFlag) {
         this.buffer.append(text);
      } else if (this.sectionTitleFlag) {
         this.buffer.append(text);
      } else if (this.verbatimFlag) {
         this.verbatimContent(text);
      } else {
         this.content(text);
      }

   }

   protected void content(String text) {
      this.writer.writeText(escapeHTML(text));
   }

   protected void verbatimContent(String text) {
      this.writer.writeText(StringUtils.replace(text, " ", "&nbsp;"));
   }

   public static String escapeHTML(String text) {
      return HtmlTools.escapeHTML(text);
   }

   public static String encodeURL(String text) {
      return HtmlTools.encodeURL(text);
   }

   public void flush() {
   }

   public void close() {
   }
}
