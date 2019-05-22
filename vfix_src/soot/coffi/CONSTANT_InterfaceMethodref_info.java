package soot.coffi;

import soot.Value;

class CONSTANT_InterfaceMethodref_info extends cp_info implements ICONSTANT_Methodref_info {
   public int class_index;
   public int name_and_type_index;

   public int size() {
      return 5;
   }

   public String toString(cp_info[] constant_pool) {
      CONSTANT_Class_info cc = (CONSTANT_Class_info)((CONSTANT_Class_info)constant_pool[this.class_index]);
      CONSTANT_NameAndType_info cn = (CONSTANT_NameAndType_info)((CONSTANT_NameAndType_info)constant_pool[this.name_and_type_index]);
      return cc.toString(constant_pool) + "." + cn.toString(constant_pool);
   }

   public String typeName() {
      return "interfacemethodref";
   }

   public int compareTo(cp_info[] constant_pool, cp_info cp, cp_info[] cp_constant_pool) {
      if (this.tag != cp.tag) {
         return this.tag - cp.tag;
      } else {
         CONSTANT_InterfaceMethodref_info cu = (CONSTANT_InterfaceMethodref_info)cp;
         int i = constant_pool[this.class_index].compareTo(constant_pool, cp_constant_pool[cu.class_index], cp_constant_pool);
         return i != 0 ? i : constant_pool[this.name_and_type_index].compareTo(constant_pool, cp_constant_pool[cu.name_and_type_index], cp_constant_pool);
      }
   }

   public Value createJimpleConstantValue(cp_info[] constant_pool) {
      throw new UnsupportedOperationException("cannot convert to Jimple: " + this.typeName());
   }

   public int getClassIndex() {
      return this.class_index;
   }

   public int getNameAndTypeIndex() {
      return this.name_and_type_index;
   }
}
