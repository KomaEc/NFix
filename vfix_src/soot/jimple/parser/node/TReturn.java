package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class TReturn extends Token {
   public TReturn() {
      super.setText("return");
   }

   public TReturn(int line, int pos) {
      super.setText("return");
      this.setLine(line);
      this.setPos(pos);
   }

   public Object clone() {
      return new TReturn(this.getLine(), this.getPos());
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseTReturn(this);
   }

   public void setText(String text) {
      throw new RuntimeException("Cannot change TReturn text.");
   }
}
