package soot.jimple.toolkits.infoflow;

import soot.Local;
import soot.Type;
import soot.jimple.internal.JimpleLocal;

public class FakeJimpleLocal extends JimpleLocal {
   Local realLocal;
   Object info;

   public FakeJimpleLocal(String name, Type t, Local realLocal) {
      this(name, t, realLocal, (Object)null);
   }

   public FakeJimpleLocal(String name, Type t, Local realLocal, Object info) {
      super(name, t);
      this.realLocal = realLocal;
      this.info = info;
   }

   public boolean equivTo(Object o) {
      if (o == null) {
         return false;
      } else if (!(o instanceof JimpleLocal)) {
         return false;
      } else if (this.getName() != null && this.getType() != null) {
         return this.getName().equals(((Local)o).getName()) && this.getType().equals(((Local)o).getType());
      } else if (this.getName() != null) {
         return this.getName().equals(((Local)o).getName()) && ((Local)o).getType() == null;
      } else if (this.getType() != null) {
         return ((Local)o).getName() == null && this.getType().equals(((Local)o).getType());
      } else {
         return ((Local)o).getName() == null && ((Local)o).getType() == null;
      }
   }

   public boolean equals(Object o) {
      return this.equivTo(o);
   }

   public Object clone() {
      return new FakeJimpleLocal(this.getName(), this.getType(), this.realLocal, this.info);
   }

   public Local getRealLocal() {
      return this.realLocal;
   }

   public Object getInfo() {
      return this.info;
   }

   public void setInfo(Object o) {
      this.info = o;
   }
}
