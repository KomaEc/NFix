package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class TMinus extends Token {
   public TMinus() {
      super.setText("-");
   }

   public TMinus(int line, int pos) {
      super.setText("-");
      this.setLine(line);
      this.setPos(pos);
   }

   public Object clone() {
      return new TMinus(this.getLine(), this.getPos());
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseTMinus(this);
   }

   public void setText(String text) {
      throw new RuntimeException("Cannot change TMinus text.");
   }
}
