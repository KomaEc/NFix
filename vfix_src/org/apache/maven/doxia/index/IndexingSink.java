package org.apache.maven.doxia.index;

import java.util.Stack;
import org.apache.maven.doxia.sink.SinkAdapter;
import org.apache.maven.doxia.util.HtmlTools;

public class IndexingSink extends SinkAdapter {
   private static final int TYPE_SECTION_1 = 1;
   private static final int TYPE_SECTION_2 = 2;
   private static final int TYPE_SECTION_3 = 3;
   private static final int TYPE_SECTION_4 = 4;
   private static final int TYPE_SECTION_5 = 5;
   private static final int TYPE_DEFINED_TERM = 6;
   private static final int TYPE_FIGURE = 7;
   private static final int TYPE_TABLE = 8;
   private static final int TITLE = 9;
   private int type;
   private String title;
   private Stack stack = new Stack();

   public IndexingSink(IndexEntry sectionEntry) {
      this.stack.push(sectionEntry);
   }

   public String getTitle() {
      return this.title;
   }

   public void title() {
      super.title();
      this.type = 9;
   }

   public void sectionTitle1() {
      this.type = 1;
   }

   public void section1_() {
      this.pop();
   }

   public void sectionTitle2() {
      this.type = 2;
   }

   public void section2_() {
      this.pop();
   }

   public void sectionTitle3() {
      this.type = 3;
   }

   public void section3_() {
      this.pop();
   }

   public void sectionTitle4() {
      this.type = 4;
   }

   public void section4_() {
      this.pop();
   }

   public void sectionTitle5() {
      this.type = 5;
   }

   public void section5_() {
      this.pop();
   }

   public void text(String text) {
      switch(this.type) {
      case 1:
      case 2:
      case 3:
      case 4:
      case 5:
         String id = HtmlTools.encodeId(text);
         IndexEntry entry = new IndexEntry(this.peek(), id);
         entry.setTitle(text);
         this.push(entry);
      case 6:
      case 7:
      case 8:
      default:
         break;
      case 9:
         this.title = text;
      }

      this.type = 0;
   }

   public void push(IndexEntry entry) {
      this.stack.push(entry);
   }

   public void pop() {
      this.stack.pop();
   }

   public IndexEntry peek() {
      return (IndexEntry)this.stack.peek();
   }
}
