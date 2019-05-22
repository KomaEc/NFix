package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class TStrictfp extends Token {
   public TStrictfp() {
      super.setText("strictfp");
   }

   public TStrictfp(int line, int pos) {
      super.setText("strictfp");
      this.setLine(line);
      this.setPos(pos);
   }

   public Object clone() {
      return new TStrictfp(this.getLine(), this.getPos());
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseTStrictfp(this);
   }

   public void setText(String text) {
      throw new RuntimeException("Cannot change TStrictfp text.");
   }
}
