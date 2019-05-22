package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class TStaticinvoke extends Token {
   public TStaticinvoke() {
      super.setText("staticinvoke");
   }

   public TStaticinvoke(int line, int pos) {
      super.setText("staticinvoke");
      this.setLine(line);
      this.setPos(pos);
   }

   public Object clone() {
      return new TStaticinvoke(this.getLine(), this.getPos());
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseTStaticinvoke(this);
   }

   public void setText(String text) {
      throw new RuntimeException("Cannot change TStaticinvoke text.");
   }
}
