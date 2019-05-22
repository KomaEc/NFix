package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class TVirtualinvoke extends Token {
   public TVirtualinvoke() {
      super.setText("virtualinvoke");
   }

   public TVirtualinvoke(int line, int pos) {
      super.setText("virtualinvoke");
      this.setLine(line);
      this.setPos(pos);
   }

   public Object clone() {
      return new TVirtualinvoke(this.getLine(), this.getPos());
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseTVirtualinvoke(this);
   }

   public void setText(String text) {
      throw new RuntimeException("Cannot change TVirtualinvoke text.");
   }
}
