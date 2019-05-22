package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class TTableswitch extends Token {
   public TTableswitch() {
      super.setText("tableswitch");
   }

   public TTableswitch(int line, int pos) {
      super.setText("tableswitch");
      this.setLine(line);
      this.setPos(pos);
   }

   public Object clone() {
      return new TTableswitch(this.getLine(), this.getPos());
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseTTableswitch(this);
   }

   public void setText(String text) {
      throw new RuntimeException("Cannot change TTableswitch text.");
   }
}
