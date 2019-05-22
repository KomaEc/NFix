package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class TRBracket extends Token {
   public TRBracket() {
      super.setText("]");
   }

   public TRBracket(int line, int pos) {
      super.setText("]");
      this.setLine(line);
      this.setPos(pos);
   }

   public Object clone() {
      return new TRBracket(this.getLine(), this.getPos());
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseTRBracket(this);
   }

   public void setText(String text) {
      throw new RuntimeException("Cannot change TRBracket text.");
   }
}
