package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class TChar extends Token {
   public TChar() {
      super.setText("char");
   }

   public TChar(int line, int pos) {
      super.setText("char");
      this.setLine(line);
      this.setPos(pos);
   }

   public Object clone() {
      return new TChar(this.getLine(), this.getPos());
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseTChar(this);
   }

   public void setText(String text) {
      throw new RuntimeException("Cannot change TChar text.");
   }
}
