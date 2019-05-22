package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class TLBracket extends Token {
   public TLBracket() {
      super.setText("[");
   }

   public TLBracket(int line, int pos) {
      super.setText("[");
      this.setLine(line);
      this.setPos(pos);
   }

   public Object clone() {
      return new TLBracket(this.getLine(), this.getPos());
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseTLBracket(this);
   }

   public void setText(String text) {
      throw new RuntimeException("Cannot change TLBracket text.");
   }
}
