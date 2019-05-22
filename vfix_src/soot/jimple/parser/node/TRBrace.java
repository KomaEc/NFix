package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class TRBrace extends Token {
   public TRBrace() {
      super.setText("}");
   }

   public TRBrace(int line, int pos) {
      super.setText("}");
      this.setLine(line);
      this.setPos(pos);
   }

   public Object clone() {
      return new TRBrace(this.getLine(), this.getPos());
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseTRBrace(this);
   }

   public void setText(String text) {
      throw new RuntimeException("Cannot change TRBrace text.");
   }
}
