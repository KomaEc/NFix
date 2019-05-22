package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class TCmplt extends Token {
   public TCmplt() {
      super.setText("<");
   }

   public TCmplt(int line, int pos) {
      super.setText("<");
      this.setLine(line);
      this.setPos(pos);
   }

   public Object clone() {
      return new TCmplt(this.getLine(), this.getPos());
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseTCmplt(this);
   }

   public void setText(String text) {
      throw new RuntimeException("Cannot change TCmplt text.");
   }
}
