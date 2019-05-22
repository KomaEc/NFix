package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class TComma extends Token {
   public TComma() {
      super.setText(",");
   }

   public TComma(int line, int pos) {
      super.setText(",");
      this.setLine(line);
      this.setPos(pos);
   }

   public Object clone() {
      return new TComma(this.getLine(), this.getPos());
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseTComma(this);
   }

   public void setText(String text) {
      throw new RuntimeException("Cannot change TComma text.");
   }
}
