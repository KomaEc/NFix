package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class TSynchronized extends Token {
   public TSynchronized() {
      super.setText("synchronized");
   }

   public TSynchronized(int line, int pos) {
      super.setText("synchronized");
      this.setLine(line);
      this.setPos(pos);
   }

   public Object clone() {
      return new TSynchronized(this.getLine(), this.getPos());
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseTSynchronized(this);
   }

   public void setText(String text) {
      throw new RuntimeException("Cannot change TSynchronized text.");
   }
}
