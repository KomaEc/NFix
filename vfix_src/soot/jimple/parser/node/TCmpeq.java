package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class TCmpeq extends Token {
   public TCmpeq() {
      super.setText("==");
   }

   public TCmpeq(int line, int pos) {
      super.setText("==");
      this.setLine(line);
      this.setPos(pos);
   }

   public Object clone() {
      return new TCmpeq(this.getLine(), this.getPos());
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseTCmpeq(this);
   }

   public void setText(String text) {
      throw new RuntimeException("Cannot change TCmpeq text.");
   }
}
