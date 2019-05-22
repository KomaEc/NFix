package jas;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class ArrayElemValPair extends ElemValPair {
   ArrayList list;

   void resolve(ClassEnv e) {
      super.resolve(e);
      if (this.list != null) {
         Iterator it = this.list.iterator();

         while(it.hasNext()) {
            ((ElemValPair)it.next()).resolve(e);
         }
      }

   }

   public ArrayElemValPair(String name, char kind, ArrayList list) {
      super(name, kind);
      this.list = list;
   }

   public void setNoName() {
      if (this.name.uniq.equals("default")) {
         super.setNoName();
      }

      if (this.list != null) {
         Iterator it = this.list.iterator();

         while(it.hasNext()) {
            ((ElemValPair)it.next()).setNoName();
         }

      }
   }

   public ArrayElemValPair(String name, char kind) {
      super(name, kind);
   }

   public void addElemValPair(ElemValPair elem) {
      if (this.list == null) {
         this.list = new ArrayList();
      }

      this.list.add(elem);
   }

   int size() {
      int i = super.size();
      i += 2;
      if (this.list != null) {
         for(Iterator it = this.list.iterator(); it.hasNext(); i += ((ElemValPair)it.next()).size()) {
         }
      }

      return i;
   }

   void write(ClassEnv e, DataOutputStream out) throws IOException, jasError {
      super.write(e, out);
      if (this.list != null) {
         out.writeShort(this.list.size());
      } else {
         out.writeShort(0);
      }

      if (this.list != null) {
         Iterator it = this.list.iterator();

         while(it.hasNext()) {
            ((ElemValPair)it.next()).write(e, out);
         }
      }

   }
}
