package soot.JastAddJ;

public class CONSTANT_Class_Info extends CONSTANT_Info {
   public int name_index;

   public CONSTANT_Class_Info(BytecodeParser parser) {
      super(parser);
      this.name_index = this.p.u2();
   }

   public String toString() {
      return "ClassInfo: " + this.name();
   }

   public String name() {
      String name = ((CONSTANT_Utf8_Info)this.p.constantPool[this.name_index]).string();
      name = name.replace('/', '.');
      return name;
   }

   public String simpleName() {
      String name = this.name();
      int pos = name.lastIndexOf(46);
      return name.substring(pos + 1, name.length());
   }

   public String packageDecl() {
      String name = this.name();
      int pos = name.lastIndexOf(46);
      return pos == -1 ? "" : name.substring(0, pos);
   }

   public Access access() {
      String name = this.name();
      int pos = name.lastIndexOf(46);
      String typeName = name.substring(pos + 1, name.length());
      String packageName = pos == -1 ? "" : name.substring(0, pos);
      return (Access)(typeName.indexOf(36) != -1 ? new BytecodeTypeAccess(packageName, typeName) : new TypeAccess(packageName, typeName));
   }
}
