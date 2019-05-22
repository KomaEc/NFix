package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class TDouble extends Token {
   public TDouble() {
      super.setText("double");
   }

   public TDouble(int line, int pos) {
      super.setText("double");
      this.setLine(line);
      this.setPos(pos);
   }

   public Object clone() {
      return new TDouble(this.getLine(), this.getPos());
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseTDouble(this);
   }

   public void setText(String text) {
      throw new RuntimeException("Cannot change TDouble text.");
   }
}
