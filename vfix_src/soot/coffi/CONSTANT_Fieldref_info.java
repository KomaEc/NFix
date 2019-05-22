package soot.coffi;

import soot.Scene;
import soot.Type;
import soot.Value;
import soot.jimple.Jimple;

class CONSTANT_Fieldref_info extends cp_info {
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
      return "fieldref";
   }

   public int compareTo(cp_info[] constant_pool, cp_info cp, cp_info[] cp_constant_pool) {
      if (this.tag != cp.tag) {
         return this.tag - cp.tag;
      } else {
         CONSTANT_Fieldref_info cu = (CONSTANT_Fieldref_info)cp;
         int i = constant_pool[this.class_index].compareTo(constant_pool, cp_constant_pool[cu.class_index], cp_constant_pool);
         return i != 0 ? i : constant_pool[this.name_and_type_index].compareTo(constant_pool, cp_constant_pool[cu.name_and_type_index], cp_constant_pool);
      }
   }

   public Value createJimpleConstantValue(cp_info[] constant_pool) {
      CONSTANT_Class_info cc = (CONSTANT_Class_info)((CONSTANT_Class_info)constant_pool[this.class_index]);
      CONSTANT_NameAndType_info cn = (CONSTANT_NameAndType_info)((CONSTANT_NameAndType_info)constant_pool[this.name_and_type_index]);
      String className = cc.toString(constant_pool);
      String nameAndType = cn.toString(constant_pool);
      String name = nameAndType.substring(0, nameAndType.indexOf(":"));
      String typeName = nameAndType.substring(nameAndType.indexOf(":") + 1);
      Type type = Util.v().jimpleTypeOfFieldDescriptor(typeName);
      return Jimple.v().newStaticFieldRef(Scene.v().makeFieldRef(Scene.v().getSootClass(className), name, type, true));
   }
}
