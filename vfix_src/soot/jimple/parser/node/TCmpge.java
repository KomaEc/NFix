package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class TCmpge extends Token {
   public TCmpge() {
      super.setText(">=");
   }

   public TCmpge(int line, int pos) {
      super.setText(">=");
      this.setLine(line);
      this.setPos(pos);
   }

   public Object clone() {
      return new TCmpge(this.getLine(), this.getPos());
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseTCmpge(this);
   }

   public void setText(String text) {
      throw new RuntimeException("Cannot change TCmpge text.");
   }
}
