package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class TAnd extends Token {
   public TAnd() {
      super.setText("&");
   }

   public TAnd(int line, int pos) {
      super.setText("&");
      this.setLine(line);
      this.setPos(pos);
   }

   public Object clone() {
      return new TAnd(this.getLine(), this.getPos());
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseTAnd(this);
   }

   public void setText(String text) {
      throw new RuntimeException("Cannot change TAnd text.");
   }
}
