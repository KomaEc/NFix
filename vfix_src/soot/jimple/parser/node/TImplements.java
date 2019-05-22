package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class TImplements extends Token {
   public TImplements() {
      super.setText("implements");
   }

   public TImplements(int line, int pos) {
      super.setText("implements");
      this.setLine(line);
      this.setPos(pos);
   }

   public Object clone() {
      return new TImplements(this.getLine(), this.getPos());
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseTImplements(this);
   }

   public void setText(String text) {
      throw new RuntimeException("Cannot change TImplements text.");
   }
}
