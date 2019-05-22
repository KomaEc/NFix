package groovyjarjarasm.asm.commons;

class SerialVersionUIDAdder$Item implements Comparable {
   final String name;
   final int access;
   final String desc;

   SerialVersionUIDAdder$Item(String var1, int var2, String var3) {
      this.name = var1;
      this.access = var2;
      this.desc = var3;
   }

   public int compareTo(Object var1) {
      SerialVersionUIDAdder$Item var2 = (SerialVersionUIDAdder$Item)var1;
      int var3 = this.name.compareTo(var2.name);
      if (var3 == 0) {
         var3 = this.desc.compareTo(var2.desc);
      }

      return var3;
   }
}
