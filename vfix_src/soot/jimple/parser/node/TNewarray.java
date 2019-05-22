package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class TNewarray extends Token {
   public TNewarray() {
      super.setText("newarray");
   }

   public TNewarray(int line, int pos) {
      super.setText("newarray");
      this.setLine(line);
      this.setPos(pos);
   }

   public Object clone() {
      return new TNewarray(this.getLine(), this.getPos());
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseTNewarray(this);
   }

   public void setText(String text) {
      throw new RuntimeException("Cannot change TNewarray text.");
   }
}
