package polyglot.types.reflect;

import java.io.DataInputStream;
import java.io.IOException;

class ConstantValue extends Attribute {
   int index;

   ConstantValue(DataInputStream in, int nameIndex, int length) throws IOException {
      super(nameIndex, length);
      this.index = in.readUnsignedShort();
   }
}
