package soot.coffi;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.SootMethod;

public class method_info {
   private static final Logger logger = LoggerFactory.getLogger(method_info.class);
   public int access_flags;
   public int name_index;
   public int descriptor_index;
   public int attributes_count;
   public attribute_info[] attributes;
   public Code_attribute code_attr;
   public Instruction instructions;
   public CFG cfg;
   public SootMethod jmethod;
   List instructionList;

   public String toName(cp_info[] constant_pool) {
      CONSTANT_Utf8_info ci = (CONSTANT_Utf8_info)((CONSTANT_Utf8_info)constant_pool[this.name_index]);
      return ci.convert();
   }

   Code_attribute locate_code_attribute() {
      for(int i = 0; i < this.attributes_count; ++i) {
         attribute_info ai = this.attributes[i];
         if (ai instanceof Code_attribute) {
            return (Code_attribute)ai;
         }
      }

      return null;
   }

   public String prototype(cp_info[] constant_pool) {
      Code_attribute c = this.locate_code_attribute();
      String access = ClassFile.access_string(this.access_flags, " ");
      String rt = ClassFile.parseMethodDesc_return(cp_info.getTypeDescr(constant_pool, this.descriptor_index));
      String name = this.toName(constant_pool);
      String params = ClassFile.parseMethodDesc_params(cp_info.getTypeDescr(constant_pool, this.descriptor_index));
      return access.length() > 0 ? access + " " + rt + " " + name + "(" + params + ")" : rt + " " + name + "(" + params + ")";
   }

   void print(cp_info[] constant_pool) {
      logger.debug("" + this.prototype(constant_pool));
      ByteCode.showCode(this.instructions, constant_pool);
   }
}
