package polyglot.types.reflect;

public abstract class Attribute {
   protected int nameIndex;
   protected int length;

   public Attribute(int nameIndex, int length) {
      this.nameIndex = nameIndex;
      this.length = length;
   }
}
