package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class TRParen extends Token {
   public TRParen() {
      super.setText(")");
   }

   public TRParen(int line, int pos) {
      super.setText(")");
      this.setLine(line);
      this.setPos(pos);
   }

   public Object clone() {
      return new TRParen(this.getLine(), this.getPos());
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseTRParen(this);
   }

   public void setText(String text) {
      throw new RuntimeException("Cannot change TRParen text.");
   }
}
