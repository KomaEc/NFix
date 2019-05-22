package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class TMod extends Token {
   public TMod() {
      super.setText("%");
   }

   public TMod(int line, int pos) {
      super.setText("%");
      this.setLine(line);
      this.setPos(pos);
   }

   public Object clone() {
      return new TMod(this.getLine(), this.getPos());
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseTMod(this);
   }

   public void setText(String text) {
      throw new RuntimeException("Cannot change TMod text.");
   }
}
