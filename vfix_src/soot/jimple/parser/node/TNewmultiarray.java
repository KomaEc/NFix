package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class TNewmultiarray extends Token {
   public TNewmultiarray() {
      super.setText("newmultiarray");
   }

   public TNewmultiarray(int line, int pos) {
      super.setText("newmultiarray");
      this.setLine(line);
      this.setPos(pos);
   }

   public Object clone() {
      return new TNewmultiarray(this.getLine(), this.getPos());
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseTNewmultiarray(this);
   }

   public void setText(String text) {
      throw new RuntimeException("Cannot change TNewmultiarray text.");
   }
}
