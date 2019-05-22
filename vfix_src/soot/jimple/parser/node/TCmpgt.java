package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class TCmpgt extends Token {
   public TCmpgt() {
      super.setText(">");
   }

   public TCmpgt(int line, int pos) {
      super.setText(">");
      this.setLine(line);
      this.setPos(pos);
   }

   public Object clone() {
      return new TCmpgt(this.getLine(), this.getPos());
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseTCmpgt(this);
   }

   public void setText(String text) {
      throw new RuntimeException("Cannot change TCmpgt text.");
   }
}
