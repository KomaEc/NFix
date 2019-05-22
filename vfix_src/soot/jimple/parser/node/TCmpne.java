package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class TCmpne extends Token {
   public TCmpne() {
      super.setText("!=");
   }

   public TCmpne(int line, int pos) {
      super.setText("!=");
      this.setLine(line);
      this.setPos(pos);
   }

   public Object clone() {
      return new TCmpne(this.getLine(), this.getPos());
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseTCmpne(this);
   }

   public void setText(String text) {
      throw new RuntimeException("Cannot change TCmpne text.");
   }
}
