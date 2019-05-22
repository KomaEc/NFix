package soot.jimple.parser.node;

import soot.jimple.parser.analysis.Analysis;

public final class TLookupswitch extends Token {
   public TLookupswitch() {
      super.setText("lookupswitch");
   }

   public TLookupswitch(int line, int pos) {
      super.setText("lookupswitch");
      this.setLine(line);
      this.setPos(pos);
   }

   public Object clone() {
      return new TLookupswitch(this.getLine(), this.getPos());
   }

   public void apply(Switch sw) {
      ((Analysis)sw).caseTLookupswitch(this);
   }

   public void setText(String text) {
      throw new RuntimeException("Cannot change TLookupswitch text.");
   }
}
