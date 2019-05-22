package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class TCatch extends Token {
   public TCatch() {
      super.setText("catch");
   }

   public TCatch(int line, int pos) {
      super.setText("catch");
      this.setLine(line);
      this.setPos(pos);
   }

   public Object clone() {
      return new TCatch(this.getLine(), this.getPos());
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseTCatch(this);
   }

   public void setText(String text) {
      throw new RuntimeException("Cannot change TCatch text.");
   }
}
