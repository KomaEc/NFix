package soot.tagkit;

import java.util.HashMap;
import java.util.Map;
import soot.Unit;

public class TryCatchTag implements Tag {
   public static final String NAME = "TryCatchTag";
   protected Map<Unit, Unit> handlerUnitToFallThroughUnit = new HashMap();

   public void register(Unit handler, Unit fallThrough) {
      this.handlerUnitToFallThroughUnit.put(handler, fallThrough);
   }

   public Unit getFallThroughUnitOf(Unit handlerUnit) {
      return (Unit)this.handlerUnitToFallThroughUnit.get(handlerUnit);
   }

   public String getName() {
      return "TryCatchTag";
   }

   public byte[] getValue() throws AttributeValueException {
      throw new UnsupportedOperationException();
   }
}
