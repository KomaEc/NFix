package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class TFrom extends Token {
   public TFrom() {
      super.setText("from");
   }

   public TFrom(int line, int pos) {
      super.setText("from");
      this.setLine(line);
      this.setPos(pos);
   }

   public Object clone() {
      return new TFrom(this.getLine(), this.getPos());
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseTFrom(this);
   }

   public void setText(String text) {
      throw new RuntimeException("Cannot change TFrom text.");
   }
}
