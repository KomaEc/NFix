package soot;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class AbstractTrap implements Trap, Serializable {
   protected transient SootClass exception;
   protected UnitBox beginUnitBox;
   protected UnitBox endUnitBox;
   protected UnitBox handlerUnitBox;
   protected List<UnitBox> unitBoxes;

   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
      in.defaultReadObject();
      this.exception = Scene.v().getSootClass((String)in.readObject());
   }

   private void writeObject(ObjectOutputStream out) throws IOException {
      out.defaultWriteObject();
      out.writeObject(this.exception.getName());
   }

   protected AbstractTrap(SootClass exception, UnitBox beginUnitBox, UnitBox endUnitBox, UnitBox handlerUnitBox) {
      this.exception = exception;
      this.beginUnitBox = beginUnitBox;
      this.endUnitBox = endUnitBox;
      this.handlerUnitBox = handlerUnitBox;
      this.unitBoxes = Collections.unmodifiableList(Arrays.asList(beginUnitBox, endUnitBox, handlerUnitBox));
   }

   public Unit getBeginUnit() {
      return this.beginUnitBox.getUnit();
   }

   public Unit getEndUnit() {
      return this.endUnitBox.getUnit();
   }

   public Unit getHandlerUnit() {
      return this.handlerUnitBox.getUnit();
   }

   public UnitBox getHandlerUnitBox() {
      return this.handlerUnitBox;
   }

   public UnitBox getBeginUnitBox() {
      return this.beginUnitBox;
   }

   public UnitBox getEndUnitBox() {
      return this.endUnitBox;
   }

   public List<UnitBox> getUnitBoxes() {
      return this.unitBoxes;
   }

   public void clearUnitBoxes() {
      Iterator var1 = this.getUnitBoxes().iterator();

      while(var1.hasNext()) {
         UnitBox box = (UnitBox)var1.next();
         box.setUnit((Unit)null);
      }

   }

   public SootClass getException() {
      return this.exception;
   }

   public void setBeginUnit(Unit beginUnit) {
      this.beginUnitBox.setUnit(beginUnit);
   }

   public void setEndUnit(Unit endUnit) {
      this.endUnitBox.setUnit(endUnit);
   }

   public void setHandlerUnit(Unit handlerUnit) {
      this.handlerUnitBox.setUnit(handlerUnit);
   }

   public void setException(SootClass exception) {
      this.exception = exception;
   }

   public Object clone() {
      throw new RuntimeException();
   }
}
