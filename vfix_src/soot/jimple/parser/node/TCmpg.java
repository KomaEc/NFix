package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class TCmpg extends Token {
   public TCmpg() {
      super.setText("cmpg");
   }

   public TCmpg(int line, int pos) {
      super.setText("cmpg");
      this.setLine(line);
      this.setPos(pos);
   }

   public Object clone() {
      return new TCmpg(this.getLine(), this.getPos());
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseTCmpg(this);
   }

   public void setText(String text) {
      throw new RuntimeException("Cannot change TCmpg text.");
   }
}
