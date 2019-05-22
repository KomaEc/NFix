package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class TOr extends Token {
   public TOr() {
      super.setText("|");
   }

   public TOr(int line, int pos) {
      super.setText("|");
      this.setLine(line);
      this.setPos(pos);
   }

   public Object clone() {
      return new TOr(this.getLine(), this.getPos());
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseTOr(this);
   }

   public void setText(String text) {
      throw new RuntimeException("Cannot change TOr text.");
   }
}
