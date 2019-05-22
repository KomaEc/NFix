package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class TAbstract extends Token {
   public TAbstract() {
      super.setText("abstract");
   }

   public TAbstract(int line, int pos) {
      super.setText("abstract");
      this.setLine(line);
      this.setPos(pos);
   }

   public Object clone() {
      return new TAbstract(this.getLine(), this.getPos());
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseTAbstract(this);
   }

   public void setText(String text) {
      throw new RuntimeException("Cannot change TAbstract text.");
   }
}
