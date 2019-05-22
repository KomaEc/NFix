package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class TLBrace extends Token {
   public TLBrace() {
      super.setText("{");
   }

   public TLBrace(int line, int pos) {
      super.setText("{");
      this.setLine(line);
      this.setPos(pos);
   }

   public Object clone() {
      return new TLBrace(this.getLine(), this.getPos());
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseTLBrace(this);
   }

   public void setText(String text) {
      throw new RuntimeException("Cannot change TLBrace text.");
   }
}
