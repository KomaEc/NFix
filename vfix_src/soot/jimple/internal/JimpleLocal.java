package soot.jimple.internal;

import java.util.Collections;
import java.util.List;
import soot.Local;
import soot.Scene;
import soot.Type;
import soot.Unit;
import soot.UnitPrinter;
import soot.ValueBox;
import soot.baf.Baf;
import soot.jimple.ConvertToBaf;
import soot.jimple.JimpleToBafContext;
import soot.jimple.JimpleValueSwitch;
import soot.util.ArrayNumberer;
import soot.util.Numberable;
import soot.util.Switch;

public class JimpleLocal implements Local, ConvertToBaf {
   protected String name;
   Type type;
   private int number = 0;

   public JimpleLocal(String name, Type type) {
      this.setName(name);
      this.setType(type);
      ArrayNumberer<Local> numberer = Scene.v().getLocalNumberer();
      if (numberer != null) {
         numberer.add((Numberable)this);
      }

   }

   public boolean equivTo(Object o) {
      return this.equals(o);
   }

   public int equivHashCode() {
      int prime = true;
      int result = 1;
      int result = 31 * result + (this.name == null ? 0 : this.name.hashCode());
      result = 31 * result + (this.type == null ? 0 : this.type.hashCode());
      return result;
   }

   public Object clone() {
      JimpleLocal local = new JimpleLocal((String)null, this.type);
      local.name = this.name;
      return local;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name == null ? null : name.intern();
   }

   public Type getType() {
      return this.type;
   }

   public void setType(Type t) {
      this.type = t;
   }

   public String toString() {
      return this.getName();
   }

   public void toString(UnitPrinter up) {
      up.local(this);
   }

   public final List<ValueBox> getUseBoxes() {
      return Collections.emptyList();
   }

   public void apply(Switch sw) {
      ((JimpleValueSwitch)sw).caseLocal(this);
   }

   public void convertToBaf(JimpleToBafContext context, List<Unit> out) {
      Unit u = Baf.v().newLoadInst(this.getType(), context.getBafLocalOfJimpleLocal(this));
      u.addAllTagsOf(context.getCurrentUnit());
      out.add(u);
   }

   public final int getNumber() {
      return this.number;
   }

   public final void setNumber(int number) {
      this.number = number;
   }
}
