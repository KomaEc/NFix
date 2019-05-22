package org.apache.maven.doxia.sink;

import org.apache.maven.doxia.logging.LogEnabled;

public interface Sink extends LogEnabled {
   String ROLE = Sink.class.getName();
   int NUMBERING_DECIMAL = 0;
   int NUMBERING_LOWER_ALPHA = 1;
   int NUMBERING_UPPER_ALPHA = 2;
   int NUMBERING_LOWER_ROMAN = 3;
   int NUMBERING_UPPER_ROMAN = 4;
   int SECTION_LEVEL_1 = 1;
   int SECTION_LEVEL_2 = 2;
   int SECTION_LEVEL_3 = 3;
   int SECTION_LEVEL_4 = 4;
   int SECTION_LEVEL_5 = 5;
   int JUSTIFY_CENTER = 0;
   int JUSTIFY_LEFT = 1;
   int JUSTIFY_RIGHT = 2;

   void head();

   void head(SinkEventAttributes var1);

   void head_();

   void title();

   void title(SinkEventAttributes var1);

   void title_();

   void author();

   void author(SinkEventAttributes var1);

   void author_();

   void date();

   void date(SinkEventAttributes var1);

   void date_();

   void body();

   void body(SinkEventAttributes var1);

   void body_();

   void sectionTitle();

   void sectionTitle_();

   void section1();

   void section1_();

   void sectionTitle1();

   void sectionTitle1_();

   void section2();

   void section2_();

   void sectionTitle2();

   void sectionTitle2_();

   void section3();

   void section3_();

   void sectionTitle3();

   void sectionTitle3_();

   void section4();

   void section4_();

   void sectionTitle4();

   void sectionTitle4_();

   void section5();

   void section5_();

   void sectionTitle5();

   void sectionTitle5_();

   void section(int var1, SinkEventAttributes var2);

   void section_(int var1);

   void sectionTitle(int var1, SinkEventAttributes var2);

   void sectionTitle_(int var1);

   void list();

   void list(SinkEventAttributes var1);

   void list_();

   void listItem();

   void listItem(SinkEventAttributes var1);

   void listItem_();

   void numberedList(int var1);

   void numberedList(int var1, SinkEventAttributes var2);

   void numberedList_();

   void numberedListItem();

   void numberedListItem(SinkEventAttributes var1);

   void numberedListItem_();

   void definitionList();

   void definitionList(SinkEventAttributes var1);

   void definitionList_();

   void definitionListItem();

   void definitionListItem(SinkEventAttributes var1);

   void definitionListItem_();

   void definition();

   void definition(SinkEventAttributes var1);

   void definition_();

   void definedTerm();

   void definedTerm(SinkEventAttributes var1);

   void definedTerm_();

   void figure();

   void figure(SinkEventAttributes var1);

   void figure_();

   void figureCaption();

   void figureCaption(SinkEventAttributes var1);

   void figureCaption_();

   void figureGraphics(String var1);

   void figureGraphics(String var1, SinkEventAttributes var2);

   void table();

   void table(SinkEventAttributes var1);

   void table_();

   void tableRows(int[] var1, boolean var2);

   void tableRows_();

   void tableRow();

   void tableRow(SinkEventAttributes var1);

   void tableRow_();

   void tableCell();

   /** @deprecated */
   void tableCell(String var1);

   void tableCell(SinkEventAttributes var1);

   void tableCell_();

   void tableHeaderCell();

   /** @deprecated */
   void tableHeaderCell(String var1);

   void tableHeaderCell(SinkEventAttributes var1);

   void tableHeaderCell_();

   void tableCaption();

   void tableCaption(SinkEventAttributes var1);

   void tableCaption_();

   void paragraph();

   void paragraph(SinkEventAttributes var1);

   void paragraph_();

   /** @deprecated */
   void verbatim(boolean var1);

   void verbatim(SinkEventAttributes var1);

   void verbatim_();

   void horizontalRule();

   void horizontalRule(SinkEventAttributes var1);

   void pageBreak();

   void anchor(String var1);

   void anchor(String var1, SinkEventAttributes var2);

   void anchor_();

   void link(String var1);

   void link(String var1, SinkEventAttributes var2);

   void link_();

   void italic();

   void italic_();

   void bold();

   void bold_();

   void monospaced();

   void monospaced_();

   void lineBreak();

   void lineBreak(SinkEventAttributes var1);

   void nonBreakingSpace();

   void text(String var1);

   void text(String var1, SinkEventAttributes var2);

   void rawText(String var1);

   void comment(String var1);

   void unknown(String var1, Object[] var2, SinkEventAttributes var3);

   void flush();

   void close();
}
