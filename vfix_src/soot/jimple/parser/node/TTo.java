package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class TTo extends Token {
   public TTo() {
      super.setText("to");
   }

   public TTo(int line, int pos) {
      super.setText("to");
      this.setLine(line);
      this.setPos(pos);
   }

   public Object clone() {
      return new TTo(this.getLine(), this.getPos());
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseTTo(this);
   }

   public void setText(String text) {
      throw new RuntimeException("Cannot change TTo text.");
   }
}
