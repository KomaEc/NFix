package org.apache.maven.doxia.module.xhtml;

import java.util.Stack;
import javax.swing.text.html.HTML.Attribute;
import javax.swing.text.html.HTML.Tag;
import org.apache.maven.doxia.macro.MacroExecutionException;
import org.apache.maven.doxia.parser.AbstractXmlParser;
import org.apache.maven.doxia.sink.Sink;
import org.codehaus.plexus.util.xml.pull.XmlPullParser;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

public class XhtmlParser extends AbstractXmlParser implements XhtmlMarkup {
   private Stack linktypes = new Stack();
   private Stack sections = new Stack();
   private static final String LINK = "link";
   private static final String ANCHOR = "anchor";

   protected void handleStartTag(XmlPullParser parser, Sink sink) throws XmlPullParserException, MacroExecutionException {
      if (parser.getName().equals(Tag.TITLE.toString())) {
         sink.title();
      } else if (parser.getName().equals(Tag.ADDRESS.toString())) {
         sink.author();
      } else if (parser.getName().equals(Tag.BODY.toString())) {
         sink.body();
      } else if (!parser.getName().equals(Tag.H1.toString()) && !parser.getName().equals(Tag.H2.toString()) && !parser.getName().equals(Tag.H3.toString()) && !parser.getName().equals(Tag.H4.toString()) && !parser.getName().equals(Tag.H5.toString())) {
         if (parser.getName().equals(Tag.P.toString())) {
            sink.paragraph();
         } else if (parser.getName().equals(Tag.PRE.toString())) {
            sink.verbatim(true);
         } else if (!parser.getName().equals(Tag.CODE.toString()) && !parser.getName().equals(Tag.SAMP.toString()) && !parser.getName().equals(Tag.TT.toString())) {
            if (parser.getName().equals(Tag.UL.toString())) {
               sink.list();
            } else if (parser.getName().equals(Tag.OL.toString())) {
               sink.numberedList(0);
            } else if (parser.getName().equals(Tag.LI.toString())) {
               sink.listItem();
            } else if (parser.getName().equals(Tag.HEAD.toString())) {
               sink.head();
            } else if (!parser.getName().equals(Tag.B.toString()) && !parser.getName().equals(Tag.STRONG.toString())) {
               if (!parser.getName().equals(Tag.I.toString()) && !parser.getName().equals(Tag.EM.toString())) {
                  String src;
                  String title;
                  String alt;
                  if (parser.getName().equals(Tag.A.toString())) {
                     src = parser.getAttributeValue((String)null, Attribute.HREF.toString());
                     title = parser.getAttributeValue((String)null, Attribute.NAME.toString());
                     alt = parser.getAttributeValue((String)null, Attribute.ID.toString());
                     if (src != null) {
                        sink.link(src);
                        this.linktypes.push("link");
                     } else if (title != null) {
                        sink.anchor(title);
                        this.linktypes.push("anchor");
                     } else if (alt != null) {
                        sink.anchor(alt);
                        this.linktypes.push("anchor");
                     }
                  } else if (parser.getName().equals(Tag.BR.toString())) {
                     sink.lineBreak();
                  } else if (parser.getName().equals(Tag.HR.toString())) {
                     sink.horizontalRule();
                  } else if (parser.getName().equals(Tag.IMG.toString())) {
                     sink.figure();
                     src = parser.getAttributeValue((String)null, Attribute.SRC.toString());
                     title = parser.getAttributeValue((String)null, Attribute.TITLE.toString());
                     alt = parser.getAttributeValue((String)null, Attribute.ALT.toString());
                     if (src != null) {
                        sink.figureGraphics(src);
                     }

                     if (title != null) {
                        sink.figureCaption();
                        text(sink, title);
                        sink.figureCaption_();
                     } else if (alt != null) {
                        sink.figureCaption();
                        text(sink, alt);
                        sink.figureCaption_();
                     }

                     sink.figure_();
                  } else if (parser.getName().equals(Tag.TABLE.toString())) {
                     sink.table();
                  } else if (parser.getName().equals(Tag.TR.toString())) {
                     sink.tableRow();
                  } else if (parser.getName().equals(Tag.TH.toString())) {
                     sink.tableCell();
                  } else if (parser.getName().equals(Tag.TD.toString())) {
                     sink.tableCell();
                  }
               } else {
                  sink.italic();
               }
            } else {
               sink.bold();
            }
         } else {
            sink.monospaced();
         }
      } else {
         this.closeSubordinatedSections(parser.getName(), sink);
         this.startSection(this.sections.size(), sink);
         this.startSectionTitle(this.sections.size(), sink);
         this.sections.push(parser.getName());
      }

   }

   protected void handleEndTag(XmlPullParser parser, Sink sink) throws XmlPullParserException, MacroExecutionException {
      if (parser.getName().equals(Tag.TITLE.toString())) {
         sink.title_();
      } else if (parser.getName().equals(Tag.ADDRESS.toString())) {
         sink.author_();
      } else if (parser.getName().equals(Tag.BODY.toString())) {
         this.closeSubordinatedSections("h0", sink);
         sink.body_();
      } else if (!parser.getName().equals(Tag.H1.toString()) && !parser.getName().equals(Tag.H2.toString()) && !parser.getName().equals(Tag.H3.toString()) && !parser.getName().equals(Tag.H4.toString()) && !parser.getName().equals(Tag.H5.toString())) {
         if (parser.getName().equals(Tag.P.toString())) {
            sink.paragraph_();
         } else if (parser.getName().equals(Tag.PRE.toString())) {
            sink.verbatim_();
         } else if (!parser.getName().equals(Tag.CODE.toString()) && !parser.getName().equals(Tag.SAMP.toString()) && !parser.getName().equals(Tag.TT.toString())) {
            if (parser.getName().equals(Tag.UL.toString())) {
               sink.list_();
            } else if (parser.getName().equals(Tag.OL.toString())) {
               sink.numberedList_();
            } else if (parser.getName().equals(Tag.LI.toString())) {
               sink.listItem_();
            } else if (parser.getName().equals(Tag.HEAD.toString())) {
               sink.head_();
            } else if (!parser.getName().equals(Tag.B.toString()) && !parser.getName().equals(Tag.STRONG.toString())) {
               if (!parser.getName().equals(Tag.I.toString()) && !parser.getName().equals(Tag.EM.toString())) {
                  if (parser.getName().equals(Tag.A.toString())) {
                     String linktype = (String)this.linktypes.pop();
                     if (linktype == "link") {
                        sink.link_();
                     } else {
                        sink.anchor_();
                     }
                  } else if (parser.getName().equals(Tag.TABLE.toString())) {
                     sink.table_();
                  } else if (parser.getName().equals(Tag.TR.toString())) {
                     sink.tableRow_();
                  } else if (parser.getName().equals(Tag.TH.toString())) {
                     sink.tableCell_();
                  } else if (parser.getName().equals(Tag.TD.toString())) {
                     sink.tableCell_();
                  }
               } else {
                  sink.italic_();
               }
            } else {
               sink.bold_();
            }
         } else {
            sink.monospaced_();
         }
      } else {
         this.closeSectionTitle(this.sections.size() - 1, sink);
      }

   }

   protected void handleText(XmlPullParser parser, Sink sink) throws XmlPullParserException {
      text(sink, parser.getText());
   }

   private static void text(Sink sink, String text) {
      if (text.startsWith("&nbsp;")) {
         sink.nonBreakingSpace();
      }

      String[] s = text.split("&nbsp;");

      for(int i = 0; i < s.length; ++i) {
         sink.text(s[i]);
         if (i + 1 < s.length) {
            sink.nonBreakingSpace();
         }
      }

      if (text.endsWith("&nbsp;")) {
         sink.nonBreakingSpace();
      }

   }

   private void closeSubordinatedSections(String level, Sink sink) {
      if (this.sections.size() > 0) {
         String heading = (String)this.sections.peek();
         int otherlevel = Integer.parseInt(heading.substring(1));
         int mylevel = Integer.parseInt(level.substring(1));
         if (otherlevel >= mylevel) {
            this.closeSection(this.sections.size(), sink);
            this.closeSubordinatedSections(level, sink);
         }
      }

   }

   private void closeSection(int level, Sink sink) {
      this.sections.pop();
      switch(level) {
      case 1:
         sink.section1_();
         break;
      case 2:
         sink.section2_();
         break;
      case 3:
         sink.section3_();
         break;
      case 4:
         sink.section4_();
         break;
      case 5:
         sink.section5_();
      }

   }

   private void startSection(int level, Sink sink) {
      switch(level) {
      case 0:
         sink.section1();
         break;
      case 1:
         sink.section2();
         break;
      case 2:
         sink.section3();
         break;
      case 3:
         sink.section4();
         break;
      case 4:
         sink.section5();
      }

   }

   private void closeSectionTitle(int level, Sink sink) {
      switch(level) {
      case 0:
         sink.sectionTitle1_();
         break;
      case 1:
         sink.sectionTitle2_();
         break;
      case 2:
         sink.sectionTitle3_();
         break;
      case 3:
         sink.sectionTitle4_();
         break;
      case 4:
         sink.sectionTitle5_();
      }

   }

   private void startSectionTitle(int level, Sink sink) {
      switch(level) {
      case 0:
         sink.sectionTitle1();
         break;
      case 1:
         sink.sectionTitle2();
         break;
      case 2:
         sink.sectionTitle3();
         break;
      case 3:
         sink.sectionTitle4();
         break;
      case 4:
         sink.sectionTitle5();
      }

   }
}
