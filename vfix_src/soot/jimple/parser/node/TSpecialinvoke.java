package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class TSpecialinvoke extends Token {
   public TSpecialinvoke() {
      super.setText("specialinvoke");
   }

   public TSpecialinvoke(int line, int pos) {
      super.setText("specialinvoke");
      this.setLine(line);
      this.setPos(pos);
   }

   public Object clone() {
      return new TSpecialinvoke(this.getLine(), this.getPos());
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseTSpecialinvoke(this);
   }

   public void setText(String text) {
      throw new RuntimeException("Cannot change TSpecialinvoke text.");
   }
}
