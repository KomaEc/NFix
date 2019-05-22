package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class TNative extends Token {
   public TNative() {
      super.setText("native");
   }

   public TNative(int line, int pos) {
      super.setText("native");
      this.setLine(line);
      this.setPos(pos);
   }

   public Object clone() {
      return new TNative(this.getLine(), this.getPos());
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseTNative(this);
   }

   public void setText(String text) {
      throw new RuntimeException("Cannot change TNative text.");
   }
}
