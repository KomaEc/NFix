package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class TCmple extends Token {
   public TCmple() {
      super.setText("<=");
   }

   public TCmple(int line, int pos) {
      super.setText("<=");
      this.setLine(line);
      this.setPos(pos);
   }

   public Object clone() {
      return new TCmple(this.getLine(), this.getPos());
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseTCmple(this);
   }

   public void setText(String text) {
      throw new RuntimeException("Cannot change TCmple text.");
   }
}
