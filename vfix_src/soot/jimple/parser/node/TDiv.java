package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class TDiv extends Token {
   public TDiv() {
      super.setText("/");
   }

   public TDiv(int line, int pos) {
      super.setText("/");
      this.setLine(line);
      this.setPos(pos);
   }

   public Object clone() {
      return new TDiv(this.getLine(), this.getPos());
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseTDiv(this);
   }

   public void setText(String text) {
      throw new RuntimeException("Cannot change TDiv text.");
   }
}
