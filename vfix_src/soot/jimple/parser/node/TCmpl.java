package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class TCmpl extends Token {
   public TCmpl() {
      super.setText("cmpl");
   }

   public TCmpl(int line, int pos) {
      super.setText("cmpl");
      this.setLine(line);
      this.setPos(pos);
   }

   public Object clone() {
      return new TCmpl(this.getLine(), this.getPos());
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseTCmpl(this);
   }

   public void setText(String text) {
      throw new RuntimeException("Cannot change TCmpl text.");
   }
}
