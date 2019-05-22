package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class EOF extends Token {
   public EOF() {
      this.setText("");
   }

   public EOF(int line, int pos) {
      this.setText("");
      this.setLine(line);
      this.setPos(pos);
   }

   public Object clone() {
      return new EOF(this.getLine(), this.getPos());
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseEOF(this);
   }
}
