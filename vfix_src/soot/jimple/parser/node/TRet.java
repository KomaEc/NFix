package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class TRet extends Token {
   public TRet() {
      super.setText("ret");
   }

   public TRet(int line, int pos) {
      super.setText("ret");
      this.setLine(line);
      this.setPos(pos);
   }

   public Object clone() {
      return new TRet(this.getLine(), this.getPos());
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseTRet(this);
   }

   public void setText(String text) {
      throw new RuntimeException("Cannot change TRet text.");
   }
}
