package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class TStatic extends Token {
   public TStatic() {
      super.setText("static");
   }

   public TStatic(int line, int pos) {
      super.setText("static");
      this.setLine(line);
      this.setPos(pos);
   }

   public Object clone() {
      return new TStatic(this.getLine(), this.getPos());
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseTStatic(this);
   }

   public void setText(String text) {
      throw new RuntimeException("Cannot change TStatic text.");
   }
}
