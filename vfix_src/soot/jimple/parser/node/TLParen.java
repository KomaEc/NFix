package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class TLParen extends Token {
   public TLParen() {
      super.setText("(");
   }

   public TLParen(int line, int pos) {
      super.setText("(");
      this.setLine(line);
      this.setPos(pos);
   }

   public Object clone() {
      return new TLParen(this.getLine(), this.getPos());
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseTLParen(this);
   }

   public void setText(String text) {
      throw new RuntimeException("Cannot change TLParen text.");
   }
}
