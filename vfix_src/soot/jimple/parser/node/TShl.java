package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class TShl extends Token {
   public TShl() {
      super.setText("<<");
   }

   public TShl(int line, int pos) {
      super.setText("<<");
      this.setLine(line);
      this.setPos(pos);
   }

   public Object clone() {
      return new TShl(this.getLine(), this.getPos());
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseTShl(this);
   }

   public void setText(String text) {
      throw new RuntimeException("Cannot change TShl text.");
   }
}
