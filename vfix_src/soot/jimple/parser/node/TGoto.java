package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class TGoto extends Token {
   public TGoto() {
      super.setText("goto");
   }

   public TGoto(int line, int pos) {
      super.setText("goto");
      this.setLine(line);
      this.setPos(pos);
   }

   public Object clone() {
      return new TGoto(this.getLine(), this.getPos());
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseTGoto(this);
   }

   public void setText(String text) {
      throw new RuntimeException("Cannot change TGoto text.");
   }
}
