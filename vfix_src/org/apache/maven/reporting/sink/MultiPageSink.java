package org.apache.maven.reporting.sink;

import org.apache.maven.doxia.sink.Sink;
import org.apache.maven.doxia.sink.SinkAdapter;

public class MultiPageSink extends SinkAdapter {
   private String outputName;
   private Sink sink;

   public MultiPageSink(String outputName, Sink sink) {
      this.outputName = outputName;
      this.sink = sink;
   }

   public String getOutputName() {
      return this.outputName;
   }

   public Sink getEmbeddedSink() {
      return this.sink;
   }

   public void closeSink() {
      this.sink.body_();
      this.sink.flush();
      this.sink.close();
   }

   public void anchor_() {
      this.sink.anchor_();
   }

   public void anchor(String arg0) {
      this.sink.anchor(arg0);
   }

   public void author_() {
      this.sink.author_();
   }

   public void author() {
      this.sink.author();
   }

   public void body() {
      this.sink.body();
   }

   public void body_() {
   }

   public void bold_() {
      this.sink.bold_();
   }

   public void bold() {
      this.sink.bold();
   }

   public void close() {
   }

   public void date_() {
      this.sink.date_();
   }

   public void date() {
      this.sink.date();
   }

   public void definedTerm_() {
      this.sink.definedTerm_();
   }

   public void definedTerm() {
      this.sink.definedTerm();
   }

   public void definition_() {
      this.sink.definition_();
   }

   public void definition() {
      this.sink.definition();
   }

   public void definitionList_() {
      this.sink.definitionList_();
   }

   public void definitionList() {
      this.sink.definitionList();
   }

   public void definitionListItem_() {
      this.sink.definitionListItem_();
   }

   public void definitionListItem() {
      this.sink.definitionListItem();
   }

   public void figure_() {
      this.sink.figure_();
   }

   public void figure() {
      this.sink.figure();
   }

   public void figureCaption_() {
      this.sink.figureCaption_();
   }

   public void figureCaption() {
      this.sink.figureCaption();
   }

   public void figureGraphics(String arg0) {
      this.sink.figureGraphics(arg0);
   }

   public void flush() {
      this.sink.flush();
   }

   public void head_() {
      this.sink.head_();
   }

   public void head() {
      this.sink.head();
   }

   public void horizontalRule() {
      this.sink.horizontalRule();
   }

   public void italic_() {
      this.sink.italic_();
   }

   public void italic() {
      this.sink.italic();
   }

   public void lineBreak() {
      this.sink.lineBreak();
   }

   public void link_() {
      this.sink.link_();
   }

   public void link(String arg0) {
      this.sink.link(arg0);
   }

   public void list_() {
      this.sink.list_();
   }

   public void list() {
      this.sink.list();
   }

   public void listItem_() {
      this.sink.listItem_();
   }

   public void listItem() {
      this.sink.listItem();
   }

   public void monospaced_() {
      this.sink.monospaced_();
   }

   public void monospaced() {
      this.sink.monospaced();
   }

   public void nonBreakingSpace() {
      this.sink.nonBreakingSpace();
   }

   public void numberedList_() {
      this.sink.numberedList_();
   }

   public void numberedList(int arg0) {
      this.sink.numberedList(arg0);
   }

   public void numberedListItem_() {
      this.sink.numberedListItem_();
   }

   public void numberedListItem() {
      this.sink.numberedListItem();
   }

   public void pageBreak() {
      this.sink.pageBreak();
   }

   public void paragraph_() {
      this.sink.paragraph_();
   }

   public void paragraph() {
      this.sink.paragraph();
   }

   public void rawText(String arg0) {
      this.sink.rawText(arg0);
   }

   public void section1_() {
      this.sink.section1_();
   }

   public void section1() {
      this.sink.section1();
   }

   public void section2_() {
      this.sink.section2_();
   }

   public void section2() {
      this.sink.section2();
   }

   public void section3_() {
      this.sink.section3_();
   }

   public void section3() {
      this.sink.section3();
   }

   public void section4_() {
      this.sink.section4_();
   }

   public void section4() {
      this.sink.section4();
   }

   public void section5_() {
      this.sink.section5_();
   }

   public void section5() {
      this.sink.section5();
   }

   public void sectionTitle_() {
      this.sink.sectionTitle_();
   }

   public void sectionTitle() {
      this.sink.sectionTitle();
   }

   public void sectionTitle1_() {
      this.sink.sectionTitle1_();
   }

   public void sectionTitle1() {
      this.sink.sectionTitle1();
   }

   public void sectionTitle2_() {
      this.sink.sectionTitle2_();
   }

   public void sectionTitle2() {
      this.sink.sectionTitle2();
   }

   public void sectionTitle3_() {
      this.sink.sectionTitle3_();
   }

   public void sectionTitle3() {
      this.sink.sectionTitle3();
   }

   public void sectionTitle4_() {
      this.sink.sectionTitle4_();
   }

   public void sectionTitle4() {
      this.sink.sectionTitle4();
   }

   public void sectionTitle5_() {
      this.sink.sectionTitle5_();
   }

   public void sectionTitle5() {
      this.sink.sectionTitle5();
   }

   public void table_() {
      this.sink.table_();
   }

   public void table() {
      this.sink.table();
   }

   public void tableCaption_() {
      this.sink.tableCaption_();
   }

   public void tableCaption() {
      this.sink.tableCaption();
   }

   public void tableCell_() {
      this.sink.tableCell_();
   }

   public void tableCell() {
      this.sink.tableCell();
   }

   public void tableHeaderCell_() {
      this.sink.tableHeaderCell_();
   }

   public void tableHeaderCell() {
      this.sink.tableHeaderCell();
   }

   public void tableRow_() {
      this.sink.tableRow_();
   }

   public void tableRow() {
      this.sink.tableRow();
   }

   public void tableRows_() {
      this.sink.tableRows_();
   }

   public void tableRows(int[] arg0, boolean arg1) {
      this.sink.tableRows(arg0, arg1);
   }

   public void text(String arg0) {
      this.sink.text(arg0);
   }

   public void title_() {
      this.sink.title_();
   }

   public void title() {
      this.sink.title();
   }

   public void verbatim_() {
      this.sink.verbatim_();
   }

   public void verbatim(boolean arg0) {
      this.sink.verbatim(arg0);
   }
}
