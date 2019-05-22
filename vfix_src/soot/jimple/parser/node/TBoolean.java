package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class TBoolean extends Token {
   public TBoolean() {
      super.setText("boolean");
   }

   public TBoolean(int line, int pos) {
      super.setText("boolean");
      this.setLine(line);
      this.setPos(pos);
   }

   public Object clone() {
      return new TBoolean(this.getLine(), this.getPos());
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseTBoolean(this);
   }

   public void setText(String text) {
      throw new RuntimeException("Cannot change TBoolean text.");
   }
}
