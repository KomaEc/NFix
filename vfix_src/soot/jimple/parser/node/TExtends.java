package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class TExtends extends Token {
   public TExtends() {
      super.setText("extends");
   }

   public TExtends(int line, int pos) {
      super.setText("extends");
      this.setLine(line);
      this.setPos(pos);
   }

   public Object clone() {
      return new TExtends(this.getLine(), this.getPos());
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseTExtends(this);
   }

   public void setText(String text) {
      throw new RuntimeException("Cannot change TExtends text.");
   }
}
