package soot;

import java.util.Iterator;
import java.util.Stack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.tagkit.ColorTag;
import soot.tagkit.Host;
import soot.tagkit.JimpleLineNumberTag;
import soot.tagkit.PositionTag;
import soot.tagkit.Tag;

public class AttributesUnitPrinter {
   private static final Logger logger = LoggerFactory.getLogger(AttributesUnitPrinter.class);
   private Stack<Integer> startOffsets;
   private int endOffset;
   private int startStmtOffset;
   private int startLn;
   private int currentLn;
   private int lastNewline;
   private UnitPrinter printer;

   public AttributesUnitPrinter(int currentLnNum) {
      this.currentLn = currentLnNum;
   }

   public void startUnit(Unit u) {
      this.startLn = this.currentLn;
      this.startStmtOffset = this.output().length() - this.lastNewline;
   }

   public void endUnit(Unit u) {
      int endStmtOffset = this.output().length() - this.lastNewline;
      if (this.hasTag(u)) {
         u.addTag(new JimpleLineNumberTag(this.startLn, this.currentLn));
      }

      if (this.hasColorTag(u)) {
         u.addTag(new PositionTag(this.startStmtOffset, endStmtOffset));
      }

   }

   public void startValueBox(ValueBox u) {
      if (this.startOffsets == null) {
         this.startOffsets = new Stack();
      }

      this.startOffsets.push(new Integer(this.output().length() - this.lastNewline));
   }

   public void endValueBox(ValueBox u) {
      this.endOffset = this.output().length() - this.lastNewline;
      if (this.hasColorTag(u)) {
         u.addTag(new PositionTag((Integer)this.startOffsets.pop(), this.endOffset));
      }

   }

   private boolean hasTag(Host h) {
      if (h instanceof Unit) {
         Iterator usesAndDefsIt = ((Unit)h).getUseAndDefBoxes().iterator();

         while(usesAndDefsIt.hasNext()) {
            if (this.hasTag((Host)usesAndDefsIt.next())) {
               return true;
            }
         }
      }

      return !h.getTags().isEmpty();
   }

   private boolean hasColorTag(Host h) {
      Iterator var2 = h.getTags().iterator();

      Tag t;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         t = (Tag)var2.next();
      } while(!(t instanceof ColorTag));

      return true;
   }

   public void setEndLn(int ln) {
      this.currentLn = ln;
   }

   public int getEndLn() {
      return this.currentLn;
   }

   public void newline() {
      ++this.currentLn;
      this.lastNewline = this.output().length();
   }

   private StringBuffer output() {
      return this.printer.output();
   }

   public void setUnitPrinter(UnitPrinter up) {
      this.printer = up;
   }
}
