package polyglot.types.reflect;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import polyglot.types.ClassType;
import polyglot.types.FieldInstance;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.types.TypeSystem;

public class Field {
   DataInputStream in;
   ClassFile clazz;
   int modifiers;
   int name;
   int type;
   Attribute[] attrs;
   ConstantValue constantValue;
   boolean synthetic;

   boolean isString(Type t) {
      return t.isClass() && t.toClass().isTopLevel() && t.toClass().fullName().equals("java.lang.String");
   }

   public int modifiers() {
      return this.modifiers;
   }

   public FieldInstance fieldInstance(TypeSystem ts, ClassType ct) {
      String name = (String)this.clazz.constants[this.name].value();
      String type = (String)this.clazz.constants[this.type].value();
      FieldInstance fi = ts.fieldInstance(ct.position(), ct, ts.flagsForBits(this.modifiers), this.clazz.typeForString(ts, type), name);
      Constant c = this.constantValue();
      if (c != null) {
         Object o = null;

         try {
            switch(c.tag()) {
            case 3:
               o = new Integer(this.getInt());
               break;
            case 4:
               o = new Float(this.getFloat());
               break;
            case 5:
               o = new Long(this.getLong());
               break;
            case 6:
               o = new Double(this.getDouble());
            case 7:
            default:
               break;
            case 8:
               o = this.getString();
            }
         } catch (SemanticException var9) {
            throw new ClassFormatError("Unexpected constant pool entry.");
         }

         if (o != null) {
            return fi.constantValue(o);
         }
      }

      return fi;
   }

   boolean isSynthetic() {
      return this.synthetic;
   }

   Constant constantValue() {
      if (this.constantValue != null) {
         int index = this.constantValue.index;
         return this.clazz.constants[index];
      } else {
         return null;
      }
   }

   int getInt() throws SemanticException {
      Constant c = this.constantValue();
      if (c != null && c.tag() == 3) {
         Integer v = (Integer)c.value();
         return v;
      } else {
         throw new SemanticException("Could not find expected constant pool entry with tag INTEGER.");
      }
   }

   float getFloat() throws SemanticException {
      Constant c = this.constantValue();
      if (c != null && c.tag() == 4) {
         Float v = (Float)c.value();
         return v;
      } else {
         throw new SemanticException("Could not find expected constant pool entry with tag FLOAT.");
      }
   }

   double getDouble() throws SemanticException {
      Constant c = this.constantValue();
      if (c != null && c.tag() == 6) {
         Double v = (Double)c.value();
         return v;
      } else {
         throw new SemanticException("Could not find expected constant pool entry with tag DOUBLE.");
      }
   }

   long getLong() throws SemanticException {
      Constant c = this.constantValue();
      if (c != null && c.tag() == 5) {
         Long v = (Long)c.value();
         return v;
      } else {
         throw new SemanticException("Could not find expected constant pool entry with tag LONG.");
      }
   }

   String getString() throws SemanticException {
      Constant c = this.constantValue();
      if (c != null && c.tag() == 8) {
         Integer i = (Integer)c.value();
         c = this.clazz.constants[i];
         if (c != null && c.tag() == 1) {
            String v = (String)c.value();
            return v;
         }
      }

      throw new SemanticException("Could not find expected constant pool entry with tag STRING or UTF8.");
   }

   String name() {
      return (String)this.clazz.constants[this.name].value();
   }

   Field(DataInputStream in, ClassFile clazz) throws IOException {
      this.clazz = clazz;
      this.in = in;
   }

   public void initialize() throws IOException {
      this.modifiers = this.in.readUnsignedShort();
      this.name = this.in.readUnsignedShort();
      this.type = this.in.readUnsignedShort();
      int numAttributes = this.in.readUnsignedShort();
      this.attrs = new Attribute[numAttributes];

      for(int i = 0; i < numAttributes; ++i) {
         int nameIndex = this.in.readUnsignedShort();
         int length = this.in.readInt();
         Constant name = this.clazz.constants[nameIndex];
         if (name != null) {
            if ("ConstantValue".equals(name.value())) {
               this.constantValue = new ConstantValue(this.in, nameIndex, length);
               this.attrs[i] = this.constantValue;
            }

            if ("Synthetic".equals(name.value())) {
               this.synthetic = true;
            }
         }

         if (this.attrs[i] == null) {
            long n = this.in.skip((long)length);
            if (n != (long)length) {
               throw new EOFException();
            }
         }
      }

   }
}
