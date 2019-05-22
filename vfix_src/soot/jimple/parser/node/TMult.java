package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class TMult extends Token {
   public TMult() {
      super.setText("*");
   }

   public TMult(int line, int pos) {
      super.setText("*");
      this.setLine(line);
      this.setPos(pos);
   }

   public Object clone() {
      return new TMult(this.getLine(), this.getPos());
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseTMult(this);
   }

   public void setText(String text) {
      throw new RuntimeException("Cannot change TMult text.");
   }
}
