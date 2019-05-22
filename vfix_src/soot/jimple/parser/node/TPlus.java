package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class TPlus extends Token {
   public TPlus() {
      super.setText("+");
   }

   public TPlus(int line, int pos) {
      super.setText("+");
      this.setLine(line);
      this.setPos(pos);
   }

   public Object clone() {
      return new TPlus(this.getLine(), this.getPos());
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseTPlus(this);
   }

   public void setText(String text) {
      throw new RuntimeException("Cannot change TPlus text.");
   }
}
