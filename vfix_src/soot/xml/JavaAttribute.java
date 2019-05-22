package soot.xml;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.tagkit.ColorTag;
import soot.tagkit.Host;
import soot.tagkit.JimpleLineNumberTag;
import soot.tagkit.LineNumberTag;
import soot.tagkit.LinkTag;
import soot.tagkit.PositionTag;
import soot.tagkit.SourceLnPosTag;
import soot.tagkit.SourcePositionTag;
import soot.tagkit.StringTag;
import soot.tagkit.Tag;

public class JavaAttribute {
   private static final Logger logger = LoggerFactory.getLogger(JavaAttribute.class);
   private int startLn;
   private ArrayList<Tag> tags;
   private ArrayList<PosColorAttribute> vbAttrs;
   public PrintWriter writerOut;

   public int startLn() {
      return this.startLn;
   }

   public void startLn(int x) {
      this.startLn = x;
   }

   public ArrayList<Tag> tags() {
      return this.tags;
   }

   public ArrayList<PosColorAttribute> vbAttrs() {
      return this.vbAttrs;
   }

   public void addTag(Tag t) {
      if (this.tags == null) {
         this.tags = new ArrayList();
      }

      this.tags.add(t);
   }

   public void addVbAttr(PosColorAttribute vbAttr) {
      if (this.vbAttrs == null) {
         this.vbAttrs = new ArrayList();
      }

      this.vbAttrs.add(vbAttr);
   }

   public boolean hasColorTag() {
      Iterator vbIt;
      if (this.tags != null) {
         vbIt = this.tags.iterator();

         while(vbIt.hasNext()) {
            Tag t = (Tag)vbIt.next();
            if (t instanceof ColorTag) {
               return true;
            }
         }
      }

      if (this.vbAttrs != null) {
         vbIt = this.vbAttrs.iterator();

         while(vbIt.hasNext()) {
            PosColorAttribute t = (PosColorAttribute)vbIt.next();
            if (t.hasColor()) {
               return true;
            }
         }
      }

      return false;
   }

   private void printAttributeTag(Tag t) {
      if (t instanceof LineNumberTag) {
         int lnNum = new Integer(((LineNumberTag)t).toString());
         this.printJavaLnAttr(lnNum, lnNum);
      } else if (t instanceof JimpleLineNumberTag) {
         JimpleLineNumberTag jlnTag = (JimpleLineNumberTag)t;
         this.printJimpleLnAttr(jlnTag.getStartLineNumber(), jlnTag.getEndLineNumber());
      } else if (t instanceof LinkTag) {
         LinkTag lt = (LinkTag)t;
         Host h = lt.getLink();
         this.printLinkAttr(this.formatForXML(lt.toString()), this.getJimpleLnOfHost(h), this.getJavaLnOfHost(h), lt.getClassName());
      } else if (t instanceof StringTag) {
         this.printTextAttr(this.formatForXML(((StringTag)t).toString()));
      } else if (t instanceof SourcePositionTag) {
         SourcePositionTag pt = (SourcePositionTag)t;
         this.printSourcePositionAttr(pt.getStartOffset(), pt.getEndOffset());
      } else if (t instanceof PositionTag) {
         PositionTag pt = (PositionTag)t;
         this.printJimplePositionAttr(pt.getStartOffset(), pt.getEndOffset());
      } else if (t instanceof ColorTag) {
         ColorTag ct = (ColorTag)t;
         this.printColorAttr(ct.getRed(), ct.getGreen(), ct.getBlue(), ct.isForeground());
      } else {
         this.printTextAttr(t.toString());
      }

   }

   private void printJavaLnAttr(int start_ln, int end_ln) {
      this.writerOut.println("<javaStartLn>" + start_ln + "</javaStartLn>");
      this.writerOut.println("<javaEndLn>" + end_ln + "</javaEndLn>");
   }

   private void printJimpleLnAttr(int start_ln, int end_ln) {
      this.writerOut.println("<jimpleStartLn>" + start_ln + "</jimpleStartLn>");
      this.writerOut.println("<jimpleEndLn>" + end_ln + "</jimpleEndLn>");
   }

   private void printTextAttr(String text) {
      this.writerOut.println("<text>" + text + "</text>");
   }

   private void printLinkAttr(String label, int jimpleLink, int javaLink, String className) {
      this.writerOut.println("<link_attribute>");
      this.writerOut.println("<link_label>" + label + "</link_label>");
      this.writerOut.println("<jimple_link>" + jimpleLink + "</jimple_link>");
      this.writerOut.println("<java_link>" + javaLink + "</java_link>");
      this.writerOut.println("<className>" + className + "</className>");
      this.writerOut.println("</link_attribute>");
   }

   private void startPrintValBoxAttr() {
      this.writerOut.println("<value_box_attribute>");
   }

   private void printSourcePositionAttr(int start, int end) {
      this.writerOut.println("<javaStartPos>" + start + "</javaStartPos>");
      this.writerOut.println("<javaEndPos>" + end + "</javaEndPos>");
   }

   private void printJimplePositionAttr(int start, int end) {
      this.writerOut.println("<jimpleStartPos>" + start + "</jimpleStartPos>");
      this.writerOut.println("<jimpleEndPos>" + end + "</jimpleEndPos>");
   }

   private void printColorAttr(int r, int g, int b, boolean fg) {
      this.writerOut.println("<red>" + r + "</red>");
      this.writerOut.println("<green>" + g + "</green>");
      this.writerOut.println("<blue>" + b + "</blue>");
      if (fg) {
         this.writerOut.println("<fg>1</fg>");
      } else {
         this.writerOut.println("<fg>0</fg>");
      }

   }

   private void endPrintValBoxAttr() {
      this.writerOut.println("</value_box_attribute>");
   }

   public void printAllTags(PrintWriter writer) {
      this.writerOut = writer;
      Iterator vbIt;
      if (this.tags != null) {
         vbIt = this.tags.iterator();

         while(vbIt.hasNext()) {
            this.printAttributeTag((Tag)vbIt.next());
         }
      }

      if (this.vbAttrs != null) {
         vbIt = this.vbAttrs.iterator();

         while(vbIt.hasNext()) {
            PosColorAttribute attr = (PosColorAttribute)vbIt.next();
            if (attr.hasColor()) {
               this.startPrintValBoxAttr();
               this.printSourcePositionAttr(attr.javaStartPos(), attr.javaEndPos());
               this.printJimplePositionAttr(attr.jimpleStartPos(), attr.jimpleEndPos());
               this.endPrintValBoxAttr();
            }
         }
      }

   }

   public void printInfoTags(PrintWriter writer) {
      this.writerOut = writer;
      Iterator it = this.tags.iterator();

      while(it.hasNext()) {
         this.printAttributeTag((Tag)it.next());
      }

   }

   private String formatForXML(String in) {
      in = in.replaceAll("<", "&lt;");
      in = in.replaceAll(">", "&gt;");
      in = in.replaceAll("&", "&amp;");
      return in;
   }

   private int getJavaLnOfHost(Host h) {
      Iterator it = h.getTags().iterator();

      Tag t;
      do {
         if (!it.hasNext()) {
            return 0;
         }

         t = (Tag)it.next();
         if (t instanceof SourceLnPosTag) {
            return ((SourceLnPosTag)t).startLn();
         }
      } while(!(t instanceof LineNumberTag));

      return new Integer(((LineNumberTag)t).toString());
   }

   private int getJimpleLnOfHost(Host h) {
      Iterator it = h.getTags().iterator();

      Tag t;
      do {
         if (!it.hasNext()) {
            return 0;
         }

         t = (Tag)it.next();
      } while(!(t instanceof JimpleLineNumberTag));

      return ((JimpleLineNumberTag)t).getStartLineNumber();
   }
}
