package soot.tagkit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class InnerClassAttribute implements Tag {
   private ArrayList<InnerClassTag> list = null;

   public InnerClassAttribute() {
   }

   public InnerClassAttribute(ArrayList<InnerClassTag> list) {
      this.list = list;
   }

   public String getClassSpecs() {
      if (this.list == null) {
         return "";
      } else {
         StringBuffer sb = new StringBuffer();
         Iterator var2 = this.list.iterator();

         while(var2.hasNext()) {
            InnerClassTag ict = (InnerClassTag)var2.next();
            sb.append(".inner_class_spec_attr ");
            sb.append(ict.getInnerClass());
            sb.append(" ");
            sb.append(ict.getOuterClass());
            sb.append(" ");
            sb.append(ict.getShortName());
            sb.append(" ");
            sb.append(ict.getAccessFlags());
            sb.append(" ");
            sb.append(".end .inner_class_spec_attr ");
         }

         return sb.toString();
      }
   }

   public String getName() {
      return "InnerClassAttribute";
   }

   public byte[] getValue() throws AttributeValueException {
      return new byte[1];
   }

   public List<InnerClassTag> getSpecs() {
      return (List)(this.list == null ? Collections.emptyList() : this.list);
   }

   public void add(InnerClassTag newt) {
      if (this.list != null) {
         String new_inner = newt.getInnerClass();
         Iterator var3 = this.list.iterator();

         while(var3.hasNext()) {
            InnerClassTag ict = (InnerClassTag)var3.next();
            String inner = ict.getInnerClass();
            if (new_inner.equals(inner)) {
               if (ict.accessFlags != 0 && newt.accessFlags > 0 && ict.accessFlags != newt.accessFlags) {
                  throw new RuntimeException("Error: trying to add an InnerClassTag twice with different access flags! (" + ict.accessFlags + " and " + newt.accessFlags + ")");
               }

               if (ict.accessFlags == 0 && newt.accessFlags != 0) {
                  this.list.remove(ict);
                  this.list.add(newt);
               }

               return;
            }
         }
      }

      if (this.list == null) {
         this.list = new ArrayList();
      }

      this.list.add(newt);
   }
}
