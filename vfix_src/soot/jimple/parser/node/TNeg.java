package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class TNeg extends Token {
   public TNeg() {
      super.setText("neg");
   }

   public TNeg(int line, int pos) {
      super.setText("neg");
      this.setLine(line);
      this.setPos(pos);
   }

   public Object clone() {
      return new TNeg(this.getLine(), this.getPos());
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseTNeg(this);
   }

   public void setText(String text) {
      throw new RuntimeException("Cannot change TNeg text.");
   }
}
