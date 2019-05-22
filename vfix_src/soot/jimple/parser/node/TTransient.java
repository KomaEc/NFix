package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class TTransient extends Token {
   public TTransient() {
      super.setText("transient");
   }

   public TTransient(int line, int pos) {
      super.setText("transient");
      this.setLine(line);
      this.setPos(pos);
   }

   public Object clone() {
      return new TTransient(this.getLine(), this.getPos());
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseTTransient(this);
   }

   public void setText(String text) {
      throw new RuntimeException("Cannot change TTransient text.");
   }
}
