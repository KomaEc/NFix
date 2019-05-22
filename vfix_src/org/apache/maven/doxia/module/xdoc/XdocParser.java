package org.apache.maven.doxia.module.xdoc;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import javax.swing.text.html.HTML.Attribute;
import javax.swing.text.html.HTML.Tag;
import org.apache.maven.doxia.macro.MacroExecutionException;
import org.apache.maven.doxia.macro.MacroRequest;
import org.apache.maven.doxia.macro.manager.MacroNotFoundException;
import org.apache.maven.doxia.parser.AbstractXmlParser;
import org.apache.maven.doxia.parser.ParseException;
import org.apache.maven.doxia.sink.Sink;
import org.apache.maven.doxia.util.HtmlTools;
import org.codehaus.plexus.util.IOUtil;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.xml.pull.XmlPullParser;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

public class XdocParser extends AbstractXmlParser implements XdocMarkup {
   private String sourceContent;
   private boolean isLink;
   private boolean isAnchor;
   private boolean isEmptyElement;
   private int orderedListDepth = 0;
   private String macroName;
   private Map macroParameters = new HashMap();

   public void parse(Reader source, Sink sink) throws ParseException {
      try {
         StringWriter contentWriter = new StringWriter();
         IOUtil.copy((Reader)source, (Writer)contentWriter);
         this.sourceContent = contentWriter.toString();
      } catch (IOException var7) {
         throw new ParseException("Error reading the input source: " + var7.getMessage(), var7);
      } finally {
         IOUtil.close(source);
      }

      StringReader tmp = new StringReader(this.sourceContent);
      super.parse(tmp, sink);
   }

   protected void handleStartTag(XmlPullParser parser, Sink sink) throws XmlPullParserException, MacroExecutionException {
      this.isEmptyElement = parser.isEmptyElementTag();
      if (!parser.getName().equals(DOCUMENT_TAG.toString())) {
         if (parser.getName().equals(Tag.TITLE.toString())) {
            sink.title();
         } else if (parser.getName().equals(AUTHOR_TAG.toString())) {
            sink.author();
         } else if (parser.getName().equals(Tag.BODY.toString())) {
            sink.body();
         } else if (parser.getName().equals(SECTION_TAG.toString())) {
            sink.section1();
            sink.sectionTitle1();
            sink.anchor(HtmlTools.encodeId(parser.getAttributeValue((String)null, Attribute.NAME.toString())));
            sink.anchor_();
            sink.text(parser.getAttributeValue((String)null, Attribute.NAME.toString()));
            sink.sectionTitle1_();
         } else if (parser.getName().equals(SUBSECTION_TAG.toString())) {
            sink.section2();
            sink.sectionTitle2();
            sink.anchor(HtmlTools.encodeId(parser.getAttributeValue((String)null, Attribute.NAME.toString())));
            sink.anchor_();
            sink.text(parser.getAttributeValue((String)null, Attribute.NAME.toString()));
            sink.sectionTitle2_();
         } else if (parser.getName().equals(Tag.H4.toString())) {
            sink.sectionTitle3();
         } else if (parser.getName().equals(Tag.H5.toString())) {
            sink.sectionTitle4();
         } else if (parser.getName().equals(Tag.H6.toString())) {
            sink.sectionTitle5();
         } else if (parser.getName().equals(Tag.P.toString())) {
            sink.paragraph();
         } else if (parser.getName().equals(SOURCE_TAG.toString())) {
            sink.verbatim(true);
         } else if (parser.getName().equals(Tag.UL.toString())) {
            sink.list();
         } else if (parser.getName().equals(Tag.OL.toString())) {
            sink.numberedList(0);
            ++this.orderedListDepth;
         } else if (parser.getName().equals(Tag.LI.toString())) {
            if (this.orderedListDepth == 0) {
               sink.listItem();
            } else {
               sink.numberedListItem();
            }
         } else if (parser.getName().equals(Tag.DL.toString())) {
            sink.definitionList();
         } else if (parser.getName().equals(Tag.DT.toString())) {
            sink.definitionListItem();
            sink.definedTerm();
         } else if (parser.getName().equals(Tag.DD.toString())) {
            sink.definition();
         } else if (parser.getName().equals(PROPERTIES_TAG.toString())) {
            sink.head();
         } else if (parser.getName().equals(Tag.B.toString())) {
            sink.bold();
         } else if (parser.getName().equals(Tag.I.toString())) {
            sink.italic();
         } else if (parser.getName().equals(Tag.TT.toString())) {
            sink.monospaced();
         } else {
            String src;
            String alt;
            if (parser.getName().equals(Tag.A.toString())) {
               src = parser.getAttributeValue((String)null, Attribute.HREF.toString());
               if (src != null) {
                  sink.link(src);
                  this.isLink = true;
               } else {
                  alt = parser.getAttributeValue((String)null, Attribute.NAME.toString());
                  if (alt != null) {
                     sink.anchor(alt);
                     this.isAnchor = true;
                  } else {
                     this.handleRawText(sink, parser);
                  }
               }
            } else if (parser.getName().equals(MACRO_TAG.toString())) {
               if (!this.secondParsing) {
                  this.macroName = parser.getAttributeValue((String)null, Attribute.NAME.toString());
                  if (StringUtils.isEmpty(this.macroName)) {
                     throw new IllegalArgumentException("The '" + Attribute.NAME.toString() + "' attribute for the '" + MACRO_TAG.toString() + "' tag is required.");
                  }
               }
            } else if (parser.getName().equals(Tag.PARAM.toString())) {
               if (!this.secondParsing && StringUtils.isNotEmpty(this.macroName)) {
                  if (this.macroParameters == null) {
                     this.macroParameters = new HashMap();
                  }

                  src = parser.getAttributeValue((String)null, Attribute.NAME.toString());
                  alt = parser.getAttributeValue((String)null, Attribute.VALUE.toString());
                  if (StringUtils.isEmpty(src) || StringUtils.isEmpty(alt)) {
                     throw new IllegalArgumentException("'" + Attribute.NAME.toString() + "' and '" + Attribute.VALUE.toString() + "' attributes for the '" + Tag.PARAM.toString() + "' tag are required inside the '" + MACRO_TAG.toString() + "' tag.");
                  }

                  this.macroParameters.put(src, alt);
               }
            } else if (parser.getName().equals(Tag.TABLE.toString())) {
               sink.table();
            } else if (parser.getName().equals(Tag.TR.toString())) {
               sink.tableRow();
            } else if (parser.getName().equals(Tag.TH.toString())) {
               src = parser.getAttributeValue((String)null, Attribute.WIDTH.toString());
               if (src == null) {
                  sink.tableHeaderCell();
               } else {
                  sink.tableHeaderCell(src);
               }
            } else if (parser.getName().equals(Tag.TD.toString())) {
               src = parser.getAttributeValue((String)null, Attribute.WIDTH.toString());
               if (src == null) {
                  sink.tableCell();
               } else {
                  sink.tableCell(src);
               }
            } else if (parser.getName().equals(Tag.BR.toString())) {
               sink.lineBreak();
            } else if (parser.getName().equals(Tag.HR.toString())) {
               sink.horizontalRule();
            } else if (parser.getName().equals(Tag.IMG.toString())) {
               src = parser.getAttributeValue((String)null, Attribute.SRC.toString());
               alt = parser.getAttributeValue((String)null, Attribute.ALT.toString());
               sink.figure();
               sink.figureGraphics(src);
               if (alt != null) {
                  sink.figureCaption();
                  sink.text(alt);
                  sink.figureCaption_();
               }

               sink.figure_();
            } else {
               this.handleRawText(sink, parser);
            }
         }

      }
   }

   protected void handleEndTag(XmlPullParser parser, Sink sink) throws XmlPullParserException, MacroExecutionException {
      if (!parser.getName().equals(DOCUMENT_TAG.toString())) {
         if (parser.getName().equals(Tag.TITLE.toString())) {
            sink.title_();
         } else if (parser.getName().equals(AUTHOR_TAG.toString())) {
            sink.author_();
         } else if (parser.getName().equals(Tag.BODY.toString())) {
            sink.body_();
         } else if (parser.getName().equals(Tag.P.toString())) {
            sink.paragraph_();
         } else if (parser.getName().equals(SOURCE_TAG.toString())) {
            sink.verbatim_();
         } else if (parser.getName().equals(Tag.UL.toString())) {
            sink.list_();
         } else if (parser.getName().equals(Tag.OL.toString())) {
            sink.numberedList_();
            --this.orderedListDepth;
         } else if (parser.getName().equals(Tag.LI.toString())) {
            if (this.orderedListDepth == 0) {
               sink.listItem_();
            } else {
               sink.numberedListItem_();
            }
         } else if (parser.getName().equals(Tag.DL.toString())) {
            sink.definitionList_();
         } else if (parser.getName().equals(Tag.DT.toString())) {
            sink.definedTerm_();
         } else if (parser.getName().equals(Tag.DD.toString())) {
            sink.definition_();
            sink.definitionListItem_();
         } else if (parser.getName().equals(PROPERTIES_TAG.toString())) {
            sink.head_();
         } else if (parser.getName().equals(Tag.B.toString())) {
            sink.bold_();
         } else if (parser.getName().equals(Tag.I.toString())) {
            sink.italic_();
         } else if (parser.getName().equals(Tag.TT.toString())) {
            sink.monospaced_();
         } else if (parser.getName().equals(Tag.A.toString())) {
            if (this.isLink) {
               sink.link_();
               this.isLink = false;
            } else if (this.isAnchor) {
               sink.anchor_();
               this.isAnchor = false;
            }
         } else if (parser.getName().equals(MACRO_TAG.toString())) {
            if (!this.secondParsing && StringUtils.isNotEmpty(this.macroName)) {
               this.macroParameters.put("sourceContent", this.sourceContent);
               XdocParser xdocParser = new XdocParser();
               xdocParser.setSecondParsing(true);
               this.macroParameters.put("parser", xdocParser);
               MacroRequest request = new MacroRequest(this.macroParameters, this.getBasedir());

               try {
                  this.executeMacro(this.macroName, request, sink);
               } catch (MacroNotFoundException var6) {
                  throw new MacroExecutionException("Macro not found: " + this.macroName, var6);
               }
            }

            this.macroName = null;
            this.macroParameters = null;
         } else if (!parser.getName().equals(Tag.PARAM.toString())) {
            if (parser.getName().equals(Tag.TABLE.toString())) {
               sink.table_();
            } else if (parser.getName().equals(Tag.TR.toString())) {
               sink.tableRow_();
            } else if (parser.getName().equals(Tag.TH.toString())) {
               sink.tableHeaderCell_();
            } else if (parser.getName().equals(Tag.TD.toString())) {
               sink.tableCell_();
            } else if (parser.getName().equals(SECTION_TAG.toString())) {
               sink.section1_();
            } else if (parser.getName().equals(SUBSECTION_TAG.toString())) {
               sink.section2_();
            } else if (parser.getName().equals(Tag.H4.toString())) {
               sink.sectionTitle3_();
            } else if (parser.getName().equals(Tag.H5.toString())) {
               sink.sectionTitle4_();
            } else if (parser.getName().equals(Tag.H6.toString())) {
               sink.sectionTitle5_();
            } else if (!this.isEmptyElement) {
               sink.rawText('<' + String.valueOf('/'));
               sink.rawText(parser.getName());
               sink.rawText(String.valueOf('>'));
            } else {
               this.isEmptyElement = false;
            }
         }

      }
   }

   protected void handleText(XmlPullParser parser, Sink sink) throws XmlPullParserException {
      String text = parser.getText();
      if (!"".equals(text.trim())) {
         sink.text(text);
      }

   }

   private void handleRawText(Sink sink, XmlPullParser parser) {
      sink.rawText(String.valueOf('<'));
      sink.rawText(parser.getName());
      int count = parser.getAttributeCount();

      for(int i = 0; i < count; ++i) {
         sink.rawText(String.valueOf(' '));
         sink.rawText(parser.getAttributeName(i));
         sink.rawText(String.valueOf('='));
         sink.rawText(String.valueOf('"'));
         sink.rawText(parser.getAttributeValue(i));
         sink.rawText(String.valueOf('"'));
      }

      sink.rawText(String.valueOf('>'));
   }
}
