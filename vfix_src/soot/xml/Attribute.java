package soot.xml;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import soot.tagkit.ColorTag;
import soot.tagkit.Host;
import soot.tagkit.JimpleLineNumberTag;
import soot.tagkit.LineNumberTag;
import soot.tagkit.LinkTag;
import soot.tagkit.PositionTag;
import soot.tagkit.SourceLnPosTag;
import soot.tagkit.StringTag;
import soot.tagkit.Tag;

public class Attribute {
   private ArrayList<ColorAttribute> colors;
   private int jimpleStartPos;
   private int jimpleEndPos;
   private int javaStartPos;
   private int javaEndPos;
   private int javaStartLn;
   private int javaEndLn;
   private int jimpleStartLn;
   private int jimpleEndLn;
   ArrayList<StringAttribute> texts;
   ArrayList<LinkAttribute> links;

   public ArrayList<ColorAttribute> colors() {
      return this.colors;
   }

   public void addColor(ColorAttribute ca) {
      if (this.colors == null) {
         this.colors = new ArrayList();
      }

      this.colors.add(ca);
   }

   public int jimpleStartPos() {
      return this.jimpleStartPos;
   }

   public void jimpleStartPos(int x) {
      this.jimpleStartPos = x;
   }

   public int jimpleEndPos() {
      return this.jimpleEndPos;
   }

   public void jimpleEndPos(int x) {
      this.jimpleEndPos = x;
   }

   public int javaStartPos() {
      return this.javaStartPos;
   }

   public void javaStartPos(int x) {
      this.javaStartPos = x;
   }

   public int javaEndPos() {
      return this.javaEndPos;
   }

   public void javaEndPos(int x) {
      this.javaEndPos = x;
   }

   public int jimpleStartLn() {
      return this.jimpleStartLn;
   }

   public void jimpleStartLn(int x) {
      this.jimpleStartLn = x;
   }

   public int jimpleEndLn() {
      return this.jimpleEndLn;
   }

   public void jimpleEndLn(int x) {
      this.jimpleEndLn = x;
   }

   public int javaStartLn() {
      return this.javaStartLn;
   }

   public void javaStartLn(int x) {
      this.javaStartLn = x;
   }

   public int javaEndLn() {
      return this.javaEndLn;
   }

   public void javaEndLn(int x) {
      this.javaEndLn = x;
   }

   public boolean hasColor() {
      return this.colors != null;
   }

   public void addText(StringAttribute s) {
      if (this.texts == null) {
         this.texts = new ArrayList();
      }

      this.texts.add(s);
   }

   public void addLink(LinkAttribute la) {
      if (this.links == null) {
         this.links = new ArrayList();
      }

      this.links.add(la);
   }

   public void addTag(Tag t) {
      if (t instanceof LineNumberTag) {
         int lnNum = new Integer(((LineNumberTag)t).toString());
         this.javaStartLn(lnNum);
         this.javaEndLn(lnNum);
      } else if (t instanceof JimpleLineNumberTag) {
         JimpleLineNumberTag jlnTag = (JimpleLineNumberTag)t;
         this.jimpleStartLn(jlnTag.getStartLineNumber());
         this.jimpleEndLn(jlnTag.getEndLineNumber());
      } else if (t instanceof SourceLnPosTag) {
         SourceLnPosTag jlnTag = (SourceLnPosTag)t;
         this.javaStartLn(jlnTag.startLn());
         this.javaEndLn(jlnTag.endLn());
         this.javaStartPos(jlnTag.startPos());
         this.javaEndPos(jlnTag.endPos());
      } else if (t instanceof LinkTag) {
         LinkTag lt = (LinkTag)t;
         Host h = lt.getLink();
         LinkAttribute link = new LinkAttribute(lt.getInfo(), this.getJimpleLnOfHost(h), this.getJavaLnOfHost(h), lt.getClassName(), lt.getAnalysisType());
         this.addLink(link);
      } else if (t instanceof StringTag) {
         StringTag st = (StringTag)t;
         StringAttribute string = new StringAttribute(this.formatForXML(st.getInfo()), st.getAnalysisType());
         this.addText(string);
      } else if (t instanceof PositionTag) {
         PositionTag pt = (PositionTag)t;
         this.jimpleStartPos(pt.getStartOffset());
         this.jimpleEndPos(pt.getEndOffset());
      } else if (t instanceof ColorTag) {
         ColorTag ct = (ColorTag)t;
         ColorAttribute ca = new ColorAttribute(ct.getRed(), ct.getGreen(), ct.getBlue(), ct.isForeground(), ct.getAnalysisType());
         this.addColor(ca);
      } else {
         StringAttribute sa = new StringAttribute(t.toString(), t.getName());
         this.addText(sa);
      }

   }

   private String formatForXML(String in) {
      in = in.replaceAll("<", "&lt;");
      in = in.replaceAll(">", "&gt;");
      in = in.replaceAll("&", "&amp;");
      in = in.replaceAll("\"", "&quot;");
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

   public String toString() {
      StringBuffer sb = new StringBuffer();
      sb.append("<srcPos sline=\"" + this.javaStartLn() + "\" eline=\"" + this.javaEndLn() + "\" spos=\"" + this.javaStartPos() + "\" epos=\"" + this.javaEndPos() + "\"/>");
      sb.append("<jmpPos sline=\"" + this.jimpleStartLn() + "\" eline=\"" + this.jimpleEndLn() + "\" spos=\"" + this.jimpleStartPos() + "\" epos=\"" + this.jimpleEndPos() + "\"/>");
      return sb.toString();
   }

   public boolean isEmpty() {
      return this.colors == null && this.texts == null && this.links == null;
   }

   public void print(PrintWriter writerOut) {
      if (!this.isEmpty()) {
         writerOut.println("<attribute>");
         writerOut.println("<srcPos sline=\"" + this.javaStartLn() + "\" eline=\"" + this.javaEndLn() + "\" spos=\"" + this.javaStartPos() + "\" epos=\"" + this.javaEndPos() + "\"/>");
         writerOut.println("<jmpPos sline=\"" + this.jimpleStartLn() + "\" eline=\"" + this.jimpleEndLn() + "\" spos=\"" + this.jimpleStartPos() + "\" epos=\"" + this.jimpleEndPos() + "\"/>");
         Iterator linksIt;
         if (this.colors != null) {
            linksIt = this.colors.iterator();

            while(linksIt.hasNext()) {
               ColorAttribute ca = (ColorAttribute)linksIt.next();
               writerOut.println("<color r=\"" + ca.red() + "\" g=\"" + ca.green() + "\" b=\"" + ca.blue() + "\" fg=\"" + ca.fg() + "\" aType=\"" + ca.analysisType() + "\"/>");
            }
         }

         if (this.texts != null) {
            linksIt = this.texts.iterator();

            while(linksIt.hasNext()) {
               StringAttribute sa = (StringAttribute)linksIt.next();
               writerOut.println("<text info=\"" + sa.info() + "\" aType=\"" + sa.analysisType() + "\"/>");
            }
         }

         if (this.links != null) {
            linksIt = this.links.iterator();

            while(linksIt.hasNext()) {
               LinkAttribute la = (LinkAttribute)linksIt.next();
               writerOut.println("<link label=\"" + this.formatForXML(la.info()) + "\" jmpLink=\"" + la.jimpleLink() + "\" srcLink=\"" + la.javaLink() + "\" clssNm=\"" + la.className() + "\" aType=\"" + la.analysisType() + "\"/>");
            }
         }

         writerOut.println("</attribute>");
      }
   }
}
