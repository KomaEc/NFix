package org.apache.maven.doxia.module.xdoc;

import java.io.Writer;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.html.HTML.Attribute;
import javax.swing.text.html.HTML.Tag;
import org.apache.maven.doxia.sink.AbstractXmlSink;
import org.apache.maven.doxia.util.HtmlTools;
import org.apache.maven.doxia.util.LineBreaker;

public class XdocSink extends AbstractXmlSink implements XdocMarkup {
   protected LineBreaker out;
   protected StringBuffer buffer = new StringBuffer();
   protected boolean headFlag;
   protected boolean titleFlag;
   private boolean boxedFlag;
   private boolean verbatimFlag;
   private int[] cellJustif;
   private int cellCount;

   public XdocSink(Writer writer) {
      this.out = new LineBreaker(writer);
   }

   protected void resetState() {
      this.headFlag = false;
      this.buffer = new StringBuffer();
      this.boxedFlag = false;
      this.verbatimFlag = false;
      this.cellJustif = null;
      this.cellCount = 0;
   }

   public void head() {
      this.resetState();
      this.headFlag = true;
      this.markup("<?xml version=\"1.0\" ?>" + EOL);
      this.writeStartTag(DOCUMENT_TAG);
      this.writeStartTag(PROPERTIES_TAG);
   }

   public void head_() {
      this.headFlag = false;
      this.writeEndTag(PROPERTIES_TAG);
   }

   public void title_() {
      if (this.buffer.length() > 0) {
         this.writeStartTag(Tag.TITLE);
         this.content(this.buffer.toString());
         this.writeEndTag(Tag.TITLE);
         this.buffer = new StringBuffer();
      }

   }

   public void author_() {
      if (this.buffer.length() > 0) {
         this.writeStartTag(AUTHOR_TAG);
         this.content(this.buffer.toString());
         this.writeEndTag(AUTHOR_TAG);
         this.buffer = new StringBuffer();
      }

   }

   public void date_() {
      if (this.buffer.length() > 0) {
         this.writeStartTag(DATE_TAG);
         this.content(this.buffer.toString());
         this.writeEndTag(DATE_TAG);
         this.buffer = new StringBuffer();
      }

   }

   public void body() {
      this.writeStartTag(Tag.BODY);
   }

   public void body_() {
      this.writeEndTag(Tag.BODY);
      this.writeEndTag(DOCUMENT_TAG);
      this.out.flush();
      this.resetState();
   }

   public void section1() {
      this.onSection(1);
   }

   public void sectionTitle1() {
      this.onSectionTitle(1);
   }

   public void sectionTitle1_() {
      this.onSectionTitle_(1);
   }

   public void section1_() {
      this.onSection_(1);
   }

   public void section2() {
      this.onSection(2);
   }

   public void sectionTitle2() {
      this.onSectionTitle(2);
   }

   public void sectionTitle2_() {
      this.onSectionTitle_(2);
   }

   public void section2_() {
      this.onSection_(2);
   }

   public void section3() {
      this.onSection(3);
   }

   public void sectionTitle3() {
      this.onSectionTitle(3);
   }

   public void sectionTitle3_() {
      this.onSectionTitle_(3);
   }

   public void section3_() {
      this.onSection_(3);
   }

   public void section4() {
      this.onSection(4);
   }

   public void sectionTitle4() {
      this.onSectionTitle(4);
   }

   public void sectionTitle4_() {
      this.onSectionTitle_(4);
   }

   public void section4_() {
      this.onSection_(4);
   }

   public void section5() {
      this.onSection(5);
   }

   public void sectionTitle5() {
      this.onSectionTitle(5);
   }

   public void sectionTitle5_() {
      this.onSectionTitle_(5);
   }

   public void section5_() {
      this.onSection_(5);
   }

   private void onSection(int depth) {
      if (depth == 1) {
         this.markup('<' + SECTION_TAG.toString() + ' ' + Attribute.NAME + '=' + '"');
      } else if (depth == 2) {
         this.markup('<' + SUBSECTION_TAG.toString() + ' ' + Attribute.NAME + '=' + '"');
      }

   }

   private void onSectionTitle(int depth) {
      if (depth == 3) {
         this.writeStartTag(Tag.H4);
      } else if (depth == 4) {
         this.writeStartTag(Tag.H5);
      } else if (depth == 5) {
         this.writeStartTag(Tag.H6);
      }

      this.titleFlag = true;
   }

   private void onSectionTitle_(int depth) {
      if (depth != 1 && depth != 2) {
         if (depth == 3) {
            this.writeEndTag(Tag.H4);
         } else if (depth == 4) {
            this.writeEndTag(Tag.H5);
         } else if (depth == 5) {
            this.writeEndTag(Tag.H6);
         }
      } else {
         this.markup('"' + String.valueOf('>'));
      }

      this.titleFlag = false;
   }

   private void onSection_(int depth) {
      if (depth == 1) {
         this.writeEndTag(SECTION_TAG);
      } else if (depth == 2) {
         this.writeEndTag(SUBSECTION_TAG);
      }

   }

   public void list() {
      this.writeStartTag(Tag.UL);
   }

   public void list_() {
      this.writeEndTag(Tag.UL);
   }

   public void listItem() {
      this.writeStartTag(Tag.LI);
   }

   public void listItem_() {
      this.writeEndTag(Tag.LI);
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

      MutableAttributeSet att = new SimpleAttributeSet();
      att.addAttribute(Attribute.STYLE, "list-style-type: " + style);
      this.writeStartTag(Tag.OL, att);
   }

   public void numberedList_() {
      this.writeEndTag(Tag.OL);
   }

   public void numberedListItem() {
      this.writeStartTag(Tag.LI);
   }

   public void numberedListItem_() {
      this.writeEndTag(Tag.LI);
   }

   public void definitionList() {
      this.writeStartTag(Tag.DL);
   }

   public void definitionList_() {
      this.writeEndTag(Tag.DL);
   }

   public void definedTerm() {
      this.writeStartTag(Tag.DT);
   }

   public void definedTerm_() {
      this.writeEndTag(Tag.DT);
   }

   public void definition() {
      this.writeStartTag(Tag.DD);
   }

   public void definition_() {
      this.writeEndTag(Tag.DD);
   }

   public void figure() {
      this.markup(String.valueOf('<') + Tag.IMG);
   }

   public void figure_() {
      this.markup(' ' + String.valueOf('/') + '>');
   }

   public void figureGraphics(String s) {
      this.markup(String.valueOf(' ') + Attribute.SRC + '=' + '"' + s + '"');
   }

   public void figureCaption() {
      this.markup(String.valueOf(' ') + Attribute.ALT + '=' + '"');
   }

   public void figureCaption_() {
      this.markup(String.valueOf('"'));
   }

   public void paragraph() {
      this.writeStartTag(Tag.P);
   }

   public void paragraph_() {
      this.writeEndTag(Tag.P);
   }

   public void verbatim(boolean boxed) {
      this.verbatimFlag = true;
      this.boxedFlag = boxed;
      if (boxed) {
         this.writeStartTag(SOURCE_TAG);
      } else {
         this.writeStartTag(Tag.PRE);
      }

   }

   public void verbatim_() {
      if (this.boxedFlag) {
         this.writeEndTag(SOURCE_TAG);
      } else {
         this.writeEndTag(Tag.PRE);
      }

      this.verbatimFlag = false;
      this.boxedFlag = false;
   }

   public void horizontalRule() {
      this.writeSimpleTag(Tag.HR);
   }

   public void table() {
      MutableAttributeSet att = new SimpleAttributeSet();
      att.addAttribute(Attribute.ALIGN, "center");
      this.writeStartTag(Tag.TABLE, att);
   }

   public void table_() {
      this.writeEndTag(Tag.TABLE);
   }

   public void tableRows(int[] justification, boolean grid) {
      MutableAttributeSet att = new SimpleAttributeSet();
      att.addAttribute(Attribute.ALIGN, "center");
      att.addAttribute(Attribute.BORDER, grid ? "1" : "0");
      this.writeStartTag(Tag.TABLE, att);
   }

   public void tableRows_() {
      this.writeEndTag(Tag.TABLE);
   }

   public void tableRow() {
      MutableAttributeSet att = new SimpleAttributeSet();
      att.addAttribute(Attribute.VALIGN, "top");
      this.writeStartTag(Tag.TR, att);
      this.cellCount = 0;
   }

   public void tableRow_() {
      this.writeEndTag(Tag.TR);
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

      Tag t = headerRow ? Tag.TH : Tag.TD;
      MutableAttributeSet att = null;
      if (justif != null) {
         att = new SimpleAttributeSet();
         att.addAttribute(Attribute.ALIGN, justif);
      }

      this.writeStartTag(t, att);
   }

   public void tableCell_() {
      this.tableCell_(false);
   }

   public void tableHeaderCell_() {
      this.tableCell_(true);
   }

   public void tableCell_(boolean headerRow) {
      Tag t = headerRow ? Tag.TH : Tag.TD;
      this.writeEndTag(t);
      ++this.cellCount;
   }

   public void tableCaption() {
      this.writeStartTag(Tag.P);
      this.writeStartTag(Tag.I);
   }

   public void tableCaption_() {
      this.writeEndTag(Tag.I);
      this.writeEndTag(Tag.P);
   }

   public void anchor(String name) {
      if (!this.headFlag && !this.titleFlag) {
         String id = HtmlTools.encodeId(name);
         MutableAttributeSet att = new SimpleAttributeSet();
         att.addAttribute(Attribute.ID, id);
         att.addAttribute(Attribute.NAME, id);
         this.writeStartTag(Tag.A, att);
      }

   }

   public void anchor_() {
      if (!this.headFlag && !this.titleFlag) {
         this.writeEndTagWithoutEOL(Tag.A);
      }

   }

   public void link(String name) {
      if (!this.headFlag && !this.titleFlag) {
         MutableAttributeSet att = new SimpleAttributeSet();
         att.addAttribute(Attribute.HREF, name);
         this.writeStartTag(Tag.A, att);
      }

   }

   public void link_() {
      if (!this.headFlag && !this.titleFlag) {
         this.writeEndTagWithoutEOL(Tag.A);
      }

   }

   public void italic() {
      if (!this.headFlag && !this.titleFlag) {
         this.writeStartTag(Tag.I);
      }

   }

   public void italic_() {
      if (!this.headFlag && !this.titleFlag) {
         this.writeEndTagWithoutEOL(Tag.I);
      }

   }

   public void bold() {
      if (!this.headFlag && !this.titleFlag) {
         this.writeStartTag(Tag.B);
      }

   }

   public void bold_() {
      if (!this.headFlag && !this.titleFlag) {
         this.writeEndTagWithoutEOL(Tag.B);
      }

   }

   public void monospaced() {
      if (!this.headFlag && !this.titleFlag) {
         this.writeStartTag(Tag.TT);
      }

   }

   public void monospaced_() {
      if (!this.headFlag && !this.titleFlag) {
         this.writeEndTagWithoutEOL(Tag.TT);
      }

   }

   public void lineBreak() {
      if (!this.headFlag && !this.titleFlag) {
         this.writeSimpleTag(Tag.BR);
      } else {
         this.buffer.append(EOL);
      }

   }

   public void nonBreakingSpace() {
      if (!this.headFlag && !this.titleFlag) {
         this.markup("&#160;");
      } else {
         this.buffer.append(' ');
      }

   }

   public void text(String text) {
      if (this.headFlag) {
         this.buffer.append(text);
      } else if (this.verbatimFlag) {
         this.verbatimContent(text);
      } else {
         this.content(text);
      }

   }

   protected void markup(String text) {
      this.out.write(text, true);
   }

   protected void content(String text) {
      this.out.write(escapeHTML(text), false);
   }

   protected void verbatimContent(String text) {
      this.out.write(escapeHTML(text), true);
   }

   public static String escapeHTML(String text) {
      return HtmlTools.escapeHTML(text);
   }

   public static String encodeURL(String text) {
      return HtmlTools.encodeURL(text);
   }

   public void flush() {
      this.out.flush();
   }

   public void close() {
      this.out.close();
   }

   protected void write(String text) {
      this.markup(text);
   }
}
