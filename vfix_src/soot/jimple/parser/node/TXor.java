package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class TXor extends Token {
   public TXor() {
      super.setText("^");
   }

   public TXor(int line, int pos) {
      super.setText("^");
      this.setLine(line);
      this.setPos(pos);
   }

   public Object clone() {
      return new TXor(this.getLine(), this.getPos());
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseTXor(this);
   }

   public void setText(String text) {
      throw new RuntimeException("Cannot change TXor text.");
   }
}
