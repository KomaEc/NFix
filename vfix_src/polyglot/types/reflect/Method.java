package polyglot.types.reflect;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import polyglot.types.ClassType;
import polyglot.types.ConstructorInstance;
import polyglot.types.MethodInstance;
import polyglot.types.Type;
import polyglot.types.TypeSystem;

public class Method {
   protected ClassFile clazz;
   protected DataInputStream in;
   protected int modifiers;
   protected int name;
   protected int type;
   protected Attribute[] attrs;
   protected Exceptions exceptions;
   protected boolean synthetic;

   public Method(DataInputStream in, ClassFile clazz) {
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
            if ("Exceptions".equals(name.value())) {
               this.exceptions = new Exceptions(this.clazz, this.in, nameIndex, length);
               this.attrs[i] = this.exceptions;
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

   boolean isSynthetic() {
      return this.synthetic;
   }

   String name() {
      return (String)this.clazz.constants[this.name].value();
   }

   public MethodInstance methodInstance(TypeSystem ts, ClassType ct) {
      String name = (String)this.clazz.constants[this.name].value();
      String type = (String)this.clazz.constants[this.type].value();
      if (type.charAt(0) != '(') {
         throw new ClassFormatError("Bad method type descriptor.");
      } else {
         int index = type.indexOf(41, 1);
         List argTypes = this.clazz.typeListForString(ts, type.substring(1, index));
         Type returnType = this.clazz.typeForString(ts, type.substring(index + 1));
         List excTypes = new ArrayList();
         if (this.exceptions != null) {
            for(int i = 0; i < this.exceptions.exceptions.length; ++i) {
               String s = this.clazz.classNameCP(this.exceptions.exceptions[i]);
               excTypes.add(this.clazz.quietTypeForName(ts, s));
            }
         }

         return ts.methodInstance(ct.position(), ct, ts.flagsForBits(this.modifiers), returnType, name, argTypes, excTypes);
      }
   }

   public ConstructorInstance constructorInstance(TypeSystem ts, ClassType ct, Field[] fields) {
      MethodInstance mi = this.methodInstance(ts, ct);
      List formals = mi.formalTypes();
      if (ct.isInnerClass()) {
         int numSynthetic = 0;

         for(int i = 0; i < fields.length; ++i) {
            if (fields[i].isSynthetic()) {
               ++numSynthetic;
            }
         }

         if (numSynthetic <= formals.size()) {
            formals = formals.subList(numSynthetic, formals.size());
         }
      }

      return ts.constructorInstance(mi.position(), ct, mi.flags(), formals, mi.throwTypes());
   }
}
