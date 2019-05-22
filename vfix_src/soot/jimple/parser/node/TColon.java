package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class TColon extends Token {
   public TColon() {
      super.setText(":");
   }

   public TColon(int line, int pos) {
      super.setText(":");
      this.setLine(line);
      this.setPos(pos);
   }

   public Object clone() {
      return new TColon(this.getLine(), this.getPos());
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseTColon(this);
   }

   public void setText(String text) {
      throw new RuntimeException("Cannot change TColon text.");
   }
}
