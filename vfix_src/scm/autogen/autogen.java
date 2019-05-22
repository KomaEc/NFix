package scm.autogen;

import jas.RuntimeConstants;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

class autogen implements RuntimeConstants {
   static String[][] procs = new String[][]{{"ClassEnv", "CP"}, {"jas-class-addcpe", "addCPItem"}, {"ClassEnv", "Var"}, {"jas-class-addfield", "addField"}, {"ClassEnv", "CP"}, {"jas-class-addinterface", "addInterface"}, {"ClassEnv", "CP"}, {"jas-class-setclass", "setClass"}, {"ClassEnv", "CP"}, {"jas-class-setsuperclass", "setSuperClass"}, {"ClassEnv", "short", "String", "String", "CodeAttr", "ExceptAttr"}, {"jas-class-addmethod", "addMethod"}, {"ClassEnv", "short"}, {"jas-class-setaccess", "setClassAccess"}, {"ClassEnv", "String"}, {"jas-class-setsource", "setSource"}, {"ClassEnv", "scmOutputStream"}, {"jas-class-write", "write"}, {"ExceptAttr", "CP"}, {"jas-exception-add", "addException"}, {"CodeAttr", "Insn"}, {"jas-code-addinsn", "addInsn"}, {"CodeAttr", "short"}, {"jas-code-stack-size", "setStackSize"}, {"CodeAttr", "short"}, {"jas-code-var-size", "setVarSize"}, {"CodeAttr", "Catchtable"}, {"jas-set-catchtable", "setCatchtable"}, {"Catchtable", "CatchEntry"}, {"jas-add-catch-entry", "addEntry"}};
   static String[][] types = new String[][]{{"String"}, {"make-ascii-cpe", "AsciiCP"}, {"String"}, {"make-class-cpe", "ClassCP"}, {"String", "String"}, {"make-name-type-cpe", "NameTypeCP"}, {"String", "String", "String"}, {"make-field-cpe", "FieldCP"}, {"String", "String", "String"}, {"make-interface-cpe", "InterfaceCP"}, {"String", "String", "String"}, {"make-method-cpe", "MethodCP"}, {"int"}, {"make-integer-cpe", "IntegerCP"}, {"float"}, {"make-float-cpe", "FloatCP"}, {"long"}, {"make-long-cpe", "LongCP"}, {"double"}, {"make-double-cpe", "DoubleCP"}, {"String"}, {"make-string-cpe", "StringCP"}, {"short", "CP", "CP", "ConstAttr"}, {"make-field", "Var"}, {"CP"}, {"make-const", "ConstAttr"}, {"String"}, {"make-outputstream", "scmOutputStream"}, {"String"}, {"make-label", "Label"}, new String[0], {"make-class-env", "ClassEnv"}, new String[0], {"make-code", "CodeAttr"}, new String[0], {"make-exception", "ExceptAttr"}, new String[0], {"make-catchtable", "Catchtable"}, {"Label", "Label", "Label", "CP"}, {"make-catch-entry", "CatchEntry"}, {"int", "int"}, {"iinc", "IincInsn"}, {"CP", "int"}, {"multianewarray", "MultiarrayInsn"}, {"CP", "int"}, {"invokeinterface", "InvokeinterfaceInsn"}};

   public static void main(String[] argv) throws IOException {
      PrintStream initer = new PrintStream(new FileOutputStream("AutoInit.java"));
      initer.println("package scm;\n\nimport jas.*;");
      initer.println("class AutoInit\n{\n  static void fillit(Env e)\n  {");
      PrintStream doit = new PrintStream(new FileOutputStream("AutoTypes.java"));
      doit.println("package scm;\n\nimport jas.*;");

      int x;
      for(x = 0; x < types.length; x += 2) {
         typeinfo tp = new typeinfo(types[x + 1][0], types[x + 1][1], types[x]);
         tp.write(doit);
         initer.println("e.definevar(Symbol.intern(\"" + types[x + 1][0] + "\"), new scm" + types[x + 1][1] + "());");
      }

      doit = new PrintStream(new FileOutputStream("AutoProcs.java"));
      doit.println("package scm;\n\nimport jas.*;");

      for(x = 0; x < procs.length; x += 2) {
         procinfo p = new procinfo(procs[x + 1][0], procs[x + 1][1], procs[x]);
         initer.println("e.definevar(Symbol.intern(\"" + procs[x + 1][0] + "\"), new scm" + procs[x + 1][1] + "());");
         p.write(doit);
      }

      initer.println("  }\n}");
   }
}
