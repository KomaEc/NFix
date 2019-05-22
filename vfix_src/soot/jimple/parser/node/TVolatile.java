package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class TVolatile extends Token {
   public TVolatile() {
      super.setText("volatile");
   }

   public TVolatile(int line, int pos) {
      super.setText("volatile");
      this.setLine(line);
      this.setPos(pos);
   }

   public Object clone() {
      return new TVolatile(this.getLine(), this.getPos());
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseTVolatile(this);
   }

   public void setText(String text) {
      throw new RuntimeException("Cannot change TVolatile text.");
   }
}
