package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class TVoid extends Token {
   public TVoid() {
      super.setText("void");
   }

   public TVoid(int line, int pos) {
      super.setText("void");
      this.setLine(line);
      this.setPos(pos);
   }

   public Object clone() {
      return new TVoid(this.getLine(), this.getPos());
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseTVoid(this);
   }

   public void setText(String text) {
      throw new RuntimeException("Cannot change TVoid text.");
   }
}
