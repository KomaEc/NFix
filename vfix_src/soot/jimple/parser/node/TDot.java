package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class TDot extends Token {
   public TDot() {
      super.setText(".");
   }

   public TDot(int line, int pos) {
      super.setText(".");
      this.setLine(line);
      this.setPos(pos);
   }

   public Object clone() {
      return new TDot(this.getLine(), this.getPos());
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseTDot(this);
   }

   public void setText(String text) {
      throw new RuntimeException("Cannot change TDot text.");
   }
}
