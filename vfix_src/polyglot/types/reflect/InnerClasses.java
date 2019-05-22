package polyglot.types.reflect;

import java.io.DataInputStream;
import java.io.IOException;

public class InnerClasses extends Attribute {
   InnerClasses.Info[] classes;

   public InnerClasses(DataInputStream in, int nameIndex, int length) throws IOException {
      super(nameIndex, length);
      int count = in.readUnsignedShort();
      this.classes = new InnerClasses.Info[count];

      for(int i = 0; i < count; ++i) {
         this.classes[i] = new InnerClasses.Info();
         this.classes[i].classIndex = in.readUnsignedShort();
         this.classes[i].outerClassIndex = in.readUnsignedShort();
         this.classes[i].nameIndex = in.readUnsignedShort();
         this.classes[i].modifiers = in.readUnsignedShort();
      }

   }

   static class Info {
      int classIndex;
      int outerClassIndex;
      int nameIndex;
      int modifiers;
   }
}
