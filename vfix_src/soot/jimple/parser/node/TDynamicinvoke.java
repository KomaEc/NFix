package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class TDynamicinvoke extends Token {
   public TDynamicinvoke() {
      super.setText("dynamicinvoke");
   }

   public TDynamicinvoke(int line, int pos) {
      super.setText("dynamicinvoke");
      this.setLine(line);
      this.setPos(pos);
   }

   public Object clone() {
      return new TDynamicinvoke(this.getLine(), this.getPos());
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseTDynamicinvoke(this);
   }

   public void setText(String text) {
      throw new RuntimeException("Cannot change TDynamicinvoke text.");
   }
}
