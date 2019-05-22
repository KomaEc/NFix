package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class TUshr extends Token {
   public TUshr() {
      super.setText(">>>");
   }

   public TUshr(int line, int pos) {
      super.setText(">>>");
      this.setLine(line);
      this.setPos(pos);
   }

   public Object clone() {
      return new TUshr(this.getLine(), this.getPos());
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseTUshr(this);
   }

   public void setText(String text) {
      throw new RuntimeException("Cannot change TUshr text.");
   }
}
