package org.apache.maven.doxia.module.xhtml;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.Map;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.html.HTML.Attribute;
import javax.swing.text.html.HTML.Tag;
import org.apache.maven.doxia.module.xhtml.decoration.render.RenderingContext;
import org.apache.maven.doxia.sink.AbstractXmlSink;
import org.apache.maven.doxia.sink.StructureSink;
import org.apache.maven.doxia.util.HtmlTools;
import org.codehaus.plexus.util.StringUtils;

public class XhtmlSink extends AbstractXmlSink implements XhtmlMarkup {
   private StringBuffer buffer;
   private boolean headFlag;
   private boolean itemFlag;
   private boolean verbatimFlag;
   private int cellCount;
   private PrintWriter writer;
   private RenderingContext renderingContext;
   private int[] cellJustif;
   private int rowMarker;

   public XhtmlSink(Writer writer) {
      this(writer, (RenderingContext)null);
   }

   public XhtmlSink(Writer writer, RenderingContext renderingContext) {
      this.buffer = new StringBuffer();
      this.rowMarker = 0;
      this.writer = new PrintWriter(writer);
      this.renderingContext = renderingContext;
   }

   public XhtmlSink(Writer writer, RenderingContext renderingContext, Map directives) {
      this.buffer = new StringBuffer();
      this.rowMarker = 0;
      this.writer = new PrintWriter(writer);
      this.renderingContext = renderingContext;
   }

   protected StringBuffer getBuffer() {
      return this.buffer;
   }

   protected void setHeadFlag(boolean headFlag) {
      this.headFlag = headFlag;
   }

   protected void resetState() {
      this.headFlag = false;
      this.resetBuffer();
      this.itemFlag = false;
      this.verbatimFlag = false;
      this.cellCount = 0;
   }

   protected void resetBuffer() {
      this.buffer = new StringBuffer();
   }

   public void head() {
      this.resetState();
      this.headFlag = true;
      this.write("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
      this.write("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
      this.writeStartTag(Tag.HEAD);
   }

   public void head_() {
      this.headFlag = false;
      this.writeEndTag(Tag.HEAD);
   }

   public void title() {
      this.writeStartTag(Tag.TITLE);
   }

   public void title_() {
      this.write(this.buffer.toString());
      this.writeEndTag(Tag.TITLE);
      this.resetBuffer();
   }

   public void author_() {
      if (this.buffer.length() > 0) {
         MutableAttributeSet att = new SimpleAttributeSet();
         att.addAttribute(Attribute.NAME, "author");
         att.addAttribute(Attribute.CONTENT, this.buffer.toString());
         this.writeSimpleTag(Tag.META, att);
         this.resetBuffer();
      }

   }

   public void date_() {
      if (this.buffer.length() > 0) {
         MutableAttributeSet att = new SimpleAttributeSet();
         att.addAttribute(Attribute.NAME, "date");
         att.addAttribute(Attribute.CONTENT, this.buffer.toString());
         this.writeSimpleTag(Tag.META, att);
         this.resetBuffer();
      }

   }

   public void body() {
      this.writeStartTag(Tag.BODY);
   }

   public void body_() {
      this.writeEndTag(Tag.BODY);
      this.writeEndTag(Tag.HTML);
   }

   public void section1() {
      MutableAttributeSet att = new SimpleAttributeSet();
      att.addAttribute(Attribute.CLASS, "section");
      this.writeStartTag(Tag.DIV, att);
   }

   public void section2() {
      MutableAttributeSet att = new SimpleAttributeSet();
      att.addAttribute(Attribute.CLASS, "section");
      this.writeStartTag(Tag.DIV, att);
   }

   public void section3() {
      MutableAttributeSet att = new SimpleAttributeSet();
      att.addAttribute(Attribute.CLASS, "section");
      this.writeStartTag(Tag.DIV, att);
   }

   public void section4() {
      MutableAttributeSet att = new SimpleAttributeSet();
      att.addAttribute(Attribute.CLASS, "section");
      this.writeStartTag(Tag.DIV, att);
   }

   public void section5() {
      MutableAttributeSet att = new SimpleAttributeSet();
      att.addAttribute(Attribute.CLASS, "section");
      this.writeStartTag(Tag.DIV, att);
   }

   public void section1_() {
      this.writeEndTag(Tag.DIV);
   }

   public void section2_() {
      this.writeEndTag(Tag.DIV);
   }

   public void section3_() {
      this.writeEndTag(Tag.DIV);
   }

   public void section4_() {
      this.writeEndTag(Tag.DIV);
   }

   public void section5_() {
      this.writeEndTag(Tag.DIV);
   }

   public void sectionTitle1() {
      this.writeStartTag(Tag.H2);
   }

   public void sectionTitle1_() {
      this.writeEndTag(Tag.H2);
   }

   public void sectionTitle2() {
      this.writeStartTag(Tag.H3);
   }

   public void sectionTitle2_() {
      this.writeEndTag(Tag.H3);
   }

   public void sectionTitle3() {
      this.writeStartTag(Tag.H4);
   }

   public void sectionTitle3_() {
      this.writeEndTag(Tag.H4);
   }

   public void sectionTitle4() {
      this.writeStartTag(Tag.H5);
   }

   public void sectionTitle4_() {
      this.writeEndTag(Tag.H5);
   }

   public void sectionTitle5() {
      this.writeStartTag(Tag.H6);
   }

   public void sectionTitle5_() {
      this.writeEndTag(Tag.H6);
   }

   public void list() {
      this.writeStartTag(Tag.UL);
   }

   public void list_() {
      this.writeEndTag(Tag.UL);
      this.itemFlag = false;
   }

   public void listItem() {
      this.writeStartTag(Tag.LI);
      this.itemFlag = true;
   }

   public void listItem_() {
      this.writeEndTag(Tag.LI);
   }

   public void numberedList(int numbering) {
      String type;
      switch(numbering) {
      case 0:
      default:
         type = "1";
         break;
      case 1:
         type = "a";
         break;
      case 2:
         type = "A";
         break;
      case 3:
         type = "i";
         break;
      case 4:
         type = "I";
      }

      MutableAttributeSet att = new SimpleAttributeSet();
      att.addAttribute(Attribute.TYPE, type);
      this.writeStartTag(Tag.OL, att);
   }

   public void numberedList_() {
      this.writeEndTag(Tag.OL);
      this.itemFlag = false;
   }

   public void numberedListItem() {
      this.writeStartTag(Tag.LI);
      this.itemFlag = true;
   }

   public void numberedListItem_() {
      this.writeEndTag(Tag.LI);
   }

   public void definitionList() {
      this.writeStartTag(Tag.DL);
   }

   public void definitionList_() {
      this.writeEndTag(Tag.DL);
      this.itemFlag = false;
   }

   public void definedTerm() {
      this.writeStartTag(Tag.DT);
   }

   public void definedTerm_() {
      this.writeEndTag(Tag.DT);
   }

   public void definition() {
      this.writeStartTag(Tag.DD);
      this.itemFlag = true;
   }

   public void definition_() {
      this.writeEndTag(Tag.DD);
   }

   public void paragraph() {
      if (!this.itemFlag) {
         this.writeStartTag(Tag.P);
      }

   }

   public void paragraph_() {
      if (this.itemFlag) {
         this.itemFlag = false;
      } else {
         this.writeEndTag(Tag.P);
      }

   }

   public void verbatim(boolean boxed) {
      this.verbatimFlag = true;
      MutableAttributeSet att = new SimpleAttributeSet();
      att.addAttribute(Attribute.CLASS, "source");
      this.writeStartTag(Tag.DIV, att);
      this.writeStartTag(Tag.PRE);
   }

   public void verbatim_() {
      this.writeEndTag(Tag.PRE);
      this.writeEndTag(Tag.DIV);
      this.verbatimFlag = false;
   }

   public void horizontalRule() {
      this.writeSimpleTag(Tag.HR);
   }

   public void table() {
      MutableAttributeSet att = new SimpleAttributeSet();
      att.addAttribute(Attribute.CLASS, "bodyTable");
      this.writeStartTag(Tag.TABLE, att);
   }

   public void table_() {
      this.writeEndTag(Tag.TABLE);
   }

   public void tableRows(int[] justification, boolean grid) {
      this.writeStartTag(TBODY_TAG);
      this.cellJustif = justification;
   }

   public void tableRows_() {
      this.writeEndTag(TBODY_TAG);
      this.cellJustif = null;
   }

   public void tableRow() {
      SimpleAttributeSet att;
      if (this.rowMarker == 0) {
         att = new SimpleAttributeSet();
         att.addAttribute(Attribute.CLASS, "a");
         this.writeStartTag(Tag.TR, att);
         this.rowMarker = 1;
      } else {
         att = new SimpleAttributeSet();
         att.addAttribute(Attribute.CLASS, "b");
         this.writeStartTag(Tag.TR, att);
         this.rowMarker = 0;
      }

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

   public void tableCell(String width) {
      this.tableCell(false, width);
   }

   public void tableHeaderCell(String width) {
      this.tableCell(true, width);
   }

   public void tableCell(boolean headerRow, String width) {
      String justif = null;
      if (this.cellJustif != null) {
         switch(this.cellJustif[this.cellCount]) {
         case 0:
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
      MutableAttributeSet att = new SimpleAttributeSet();
      if (width != null) {
         att.addAttribute(Attribute.WIDTH, width);
      }

      if (justif != null) {
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
      this.writeStartTag(Tag.CAPTION);
   }

   public void tableCaption_() {
      this.writeEndTag(Tag.CAPTION);
   }

   public void figure() {
      this.write(String.valueOf('<') + Tag.IMG);
   }

   public void figure_() {
      this.write(' ' + String.valueOf('/') + '>');
   }

   public void figureCaption() {
      this.write(String.valueOf(' ') + Attribute.ALT + '=' + '"');
   }

   public void figureCaption_() {
      this.write(String.valueOf('"'));
   }

   public void figureGraphics(String name) {
      this.write(String.valueOf(' ') + Attribute.SRC + '=' + '"' + name + '"');
   }

   public void anchor(String name) {
      if (!this.headFlag) {
         String id = HtmlTools.encodeId(name);
         MutableAttributeSet att = new SimpleAttributeSet();
         if (id != null) {
            att.addAttribute(Attribute.NAME, id);
         }

         this.writeStartTag(Tag.A, att);
      }

   }

   public void anchor_() {
      if (!this.headFlag) {
         this.writeEndTagWithoutEOL(Tag.A);
      }

   }

   public void link(String name) {
      this.link(name, (String)null);
   }

   public void link(String name, String target) {
      if (!this.headFlag) {
         MutableAttributeSet att = new SimpleAttributeSet();
         if (target != null) {
            att.addAttribute(Attribute.TARGET, target);
         }

         if (!StructureSink.isExternalLink(name) && !this.isExternalHtml(name)) {
            att.addAttribute(Attribute.HREF, "#" + HtmlTools.escapeHTML(name));
            this.writeStartTag(Tag.A, att);
         } else {
            if (this.isExternalLink(name)) {
               att.addAttribute(Attribute.CLASS, "externalLink");
            }

            att.addAttribute(Attribute.HREF, HtmlTools.escapeHTML(name));
            this.writeStartTag(Tag.A, att);
         }

      }
   }

   private boolean isExternalLink(String href) {
      String text = href.toLowerCase();
      return text.indexOf("http:/") == 0 || text.indexOf("https:/") == 0 || text.indexOf("ftp:/") == 0 || text.indexOf("mailto:") == 0 || text.indexOf("file:/") == 0;
   }

   private boolean isExternalHtml(String href) {
      String text = href.toLowerCase();
      return text.indexOf(".html#") != -1 || text.indexOf(".htm#") != -1 || text.endsWith(".htm") || text.endsWith(".html") || !HtmlTools.isId(text);
   }

   public void link_() {
      if (!this.headFlag) {
         this.writeEndTagWithoutEOL(Tag.A);
      }

   }

   public void italic() {
      if (!this.headFlag) {
         this.writeStartTag(Tag.I);
      }

   }

   public void italic_() {
      if (!this.headFlag) {
         this.writeEndTagWithoutEOL(Tag.I);
      }

   }

   public void bold() {
      if (!this.headFlag) {
         this.writeStartTag(Tag.B);
      }

   }

   public void bold_() {
      if (!this.headFlag) {
         this.writeEndTagWithoutEOL(Tag.B);
      }

   }

   public void monospaced() {
      if (!this.headFlag) {
         this.writeStartTag(Tag.TT);
      }

   }

   public void monospaced_() {
      if (!this.headFlag) {
         this.writeEndTagWithoutEOL(Tag.TT);
      }

   }

   public void lineBreak() {
      if (this.headFlag) {
         this.buffer.append(EOL);
      } else {
         this.writeSimpleTag(Tag.BR);
      }

   }

   public void nonBreakingSpace() {
      if (this.headFlag) {
         this.buffer.append(' ');
      } else {
         this.write("&#160;");
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

   public void rawText(String text) {
      this.write(text);
   }

   public void flush() {
      this.writer.flush();
   }

   public void close() {
      this.writer.close();
   }

   protected void write(String text) {
      if (this.renderingContext != null) {
         String relativePathToBasedir = this.renderingContext.getRelativePath();
         if (relativePathToBasedir == null) {
            text = StringUtils.replace(text, "$relativePath", ".");
         } else {
            text = StringUtils.replace(text, "$relativePath", relativePathToBasedir);
         }
      }

      this.writer.write(text);
   }

   protected void content(String text) {
      this.write(escapeHTML(text));
   }

   protected void verbatimContent(String text) {
      this.write(escapeHTML(text));
   }

   public static String escapeHTML(String text) {
      return HtmlTools.escapeHTML(text);
   }

   public static String encodeFragment(String text) {
      return encodeURL(StructureSink.linkToKey(text));
   }

   public static String encodeURL(String text) {
      return HtmlTools.encodeURL(text);
   }

   public RenderingContext getRenderingContext() {
      return this.renderingContext;
   }
}
