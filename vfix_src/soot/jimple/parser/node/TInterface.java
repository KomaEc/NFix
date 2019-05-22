package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class TInterface extends Token {
   public TInterface() {
      super.setText("interface");
   }

   public TInterface(int line, int pos) {
      super.setText("interface");
      this.setLine(line);
      this.setPos(pos);
   }

   public Object clone() {
      return new TInterface(this.getLine(), this.getPos());
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseTInterface(this);
   }

   public void setText(String text) {
      throw new RuntimeException("Cannot change TInterface text.");
   }
}
