package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class TCmp extends Token {
   public TCmp() {
      super.setText("cmp");
   }

   public TCmp(int line, int pos) {
      super.setText("cmp");
      this.setLine(line);
      this.setPos(pos);
   }

   public Object clone() {
      return new TCmp(this.getLine(), this.getPos());
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseTCmp(this);
   }

   public void setText(String text) {
      throw new RuntimeException("Cannot change TCmp text.");
   }
}
