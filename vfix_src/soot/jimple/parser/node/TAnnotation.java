package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class TAnnotation extends Token {
   public TAnnotation() {
      super.setText("annotation");
   }

   public TAnnotation(int line, int pos) {
      super.setText("annotation");
      this.setLine(line);
      this.setPos(pos);
   }

   public Object clone() {
      return new TAnnotation(this.getLine(), this.getPos());
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseTAnnotation(this);
   }

   public void setText(String text) {
      throw new RuntimeException("Cannot change TAnnotation text.");
   }
}
