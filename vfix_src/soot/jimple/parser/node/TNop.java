package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class TNop extends Token {
   public TNop() {
      super.setText("nop");
   }

   public TNop(int line, int pos) {
      super.setText("nop");
      this.setLine(line);
      this.setPos(pos);
   }

   public Object clone() {
      return new TNop(this.getLine(), this.getPos());
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseTNop(this);
   }

   public void setText(String text) {
      throw new RuntimeException("Cannot change TNop text.");
   }
}
