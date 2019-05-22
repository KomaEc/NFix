package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class TInterfaceinvoke extends Token {
   public TInterfaceinvoke() {
      super.setText("interfaceinvoke");
   }

   public TInterfaceinvoke(int line, int pos) {
      super.setText("interfaceinvoke");
      this.setLine(line);
      this.setPos(pos);
   }

   public Object clone() {
      return new TInterfaceinvoke(this.getLine(), this.getPos());
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseTInterfaceinvoke(this);
   }

   public void setText(String text) {
      throw new RuntimeException("Cannot change TInterfaceinvoke text.");
   }
}
