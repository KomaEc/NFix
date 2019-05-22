package soot.jimple.spark.pag;

import soot.Type;
import soot.Value;

public class NewInstanceNode extends Node {
   private final Value value;

   NewInstanceNode(PAG pag, Value value, Type type) {
      super(pag, type);
      this.value = value;
   }

   public Value getValue() {
      return this.value;
   }
}
