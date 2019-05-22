package polyglot.types.reflect;

import java.io.DataInputStream;
import java.io.IOException;

public class Exceptions extends Attribute {
   int[] exceptions;
   ClassFile clazz;

   public Exceptions(ClassFile clazz, int nameIndex, int[] exceptions) {
      super(nameIndex, 2 * exceptions.length + 2);
      this.clazz = clazz;
      this.exceptions = exceptions;
   }

   public Exceptions(ClassFile clazz, DataInputStream in, int nameIndex, int length) throws IOException {
      super(nameIndex, length);
      this.clazz = clazz;
      int count = in.readUnsignedShort();
      this.exceptions = new int[count];

      for(int i = 0; i < count; ++i) {
         this.exceptions[i] = in.readUnsignedShort();
      }

   }
}
