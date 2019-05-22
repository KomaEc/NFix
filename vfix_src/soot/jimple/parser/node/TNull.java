package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class TNull extends Token {
   public TNull() {
      super.setText("null");
   }

   public TNull(int line, int pos) {
      super.setText("null");
      this.setLine(line);
      this.setPos(pos);
   }

   public Object clone() {
      return new TNull(this.getLine(), this.getPos());
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseTNull(this);
   }

   public void setText(String text) {
      throw new RuntimeException("Cannot change TNull text.");
   }
}
