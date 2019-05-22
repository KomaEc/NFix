package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class TInt extends Token {
   public TInt() {
      super.setText("int");
   }

   public TInt(int line, int pos) {
      super.setText("int");
      this.setLine(line);
      this.setPos(pos);
   }

   public Object clone() {
      return new TInt(this.getLine(), this.getPos());
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseTInt(this);
   }

   public void setText(String text) {
      throw new RuntimeException("Cannot change TInt text.");
   }
}
