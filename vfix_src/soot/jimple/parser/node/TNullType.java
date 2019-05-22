package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class TNullType extends Token {
   public TNullType() {
      super.setText("null_type");
   }

   public TNullType(int line, int pos) {
      super.setText("null_type");
      this.setLine(line);
      this.setPos(pos);
   }

   public Object clone() {
      return new TNullType(this.getLine(), this.getPos());
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseTNullType(this);
   }

   public void setText(String text) {
      throw new RuntimeException("Cannot change TNullType text.");
   }
}
