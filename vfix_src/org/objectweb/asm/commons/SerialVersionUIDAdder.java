package org.objectweb.asm.commons;

import java.io.ByteArrayOutputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

public class SerialVersionUIDAdder extends ClassVisitor {
   private boolean computeSVUID;
   private boolean hasSVUID;
   private int access;
   private String name;
   private String[] interfaces;
   private Collection<SerialVersionUIDAdder.Item> svuidFields;
   private boolean hasStaticInitializer;
   private Collection<SerialVersionUIDAdder.Item> svuidConstructors;
   private Collection<SerialVersionUIDAdder.Item> svuidMethods;

   public SerialVersionUIDAdder(ClassVisitor cv) {
      this(327680, cv);
      if (this.getClass() != SerialVersionUIDAdder.class) {
         throw new IllegalStateException();
      }
   }

   protected SerialVersionUIDAdder(int api, ClassVisitor cv) {
      super(api, cv);
      this.svuidFields = new ArrayList();
      this.svuidConstructors = new ArrayList();
      this.svuidMethods = new ArrayList();
   }

   public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
      this.computeSVUID = (access & 16384) == 0;
      if (this.computeSVUID) {
         this.name = name;
         this.access = access;
         this.interfaces = new String[interfaces.length];
         System.arraycopy(interfaces, 0, this.interfaces, 0, interfaces.length);
      }

      super.visit(version, access, name, signature, superName, interfaces);
   }

   public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
      if (this.computeSVUID) {
         if ("<clinit>".equals(name)) {
            this.hasStaticInitializer = true;
         }

         int mods = access & 3391;
         if ((access & 2) == 0) {
            if ("<init>".equals(name)) {
               this.svuidConstructors.add(new SerialVersionUIDAdder.Item(name, mods, desc));
            } else if (!"<clinit>".equals(name)) {
               this.svuidMethods.add(new SerialVersionUIDAdder.Item(name, mods, desc));
            }
         }
      }

      return super.visitMethod(access, name, desc, signature, exceptions);
   }

   public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
      if (this.computeSVUID) {
         if ("serialVersionUID".equals(name)) {
            this.computeSVUID = false;
            this.hasSVUID = true;
         }

         if ((access & 2) == 0 || (access & 136) == 0) {
            int mods = access & 223;
            this.svuidFields.add(new SerialVersionUIDAdder.Item(name, mods, desc));
         }
      }

      return super.visitField(access, name, desc, signature, value);
   }

   public void visitInnerClass(String aname, String outerName, String innerName, int attr_access) {
      if (this.name != null && this.name.equals(aname)) {
         this.access = attr_access;
      }

      super.visitInnerClass(aname, outerName, innerName, attr_access);
   }

   public void visitEnd() {
      if (this.computeSVUID && !this.hasSVUID) {
         try {
            this.addSVUID(this.computeSVUID());
         } catch (Throwable var2) {
            throw new RuntimeException("Error while computing SVUID for " + this.name, var2);
         }
      }

      super.visitEnd();
   }

   public boolean hasSVUID() {
      return this.hasSVUID;
   }

   protected void addSVUID(long svuid) {
      FieldVisitor fv = super.visitField(24, "serialVersionUID", "J", (String)null, svuid);
      if (fv != null) {
         fv.visitEnd();
      }

   }

   protected long computeSVUID() throws IOException {
      DataOutputStream dos = null;
      long svuid = 0L;

      try {
         ByteArrayOutputStream bos = new ByteArrayOutputStream();
         dos = new DataOutputStream(bos);
         dos.writeUTF(this.name.replace('/', '.'));
         int access = this.access;
         if ((access & 512) != 0) {
            access = this.svuidMethods.size() > 0 ? access | 1024 : access & -1025;
         }

         dos.writeInt(access & 1553);
         Arrays.sort(this.interfaces);

         for(int i = 0; i < this.interfaces.length; ++i) {
            dos.writeUTF(this.interfaces[i].replace('/', '.'));
         }

         writeItems(this.svuidFields, dos, false);
         if (this.hasStaticInitializer) {
            dos.writeUTF("<clinit>");
            dos.writeInt(8);
            dos.writeUTF("()V");
         }

         writeItems(this.svuidConstructors, dos, true);
         writeItems(this.svuidMethods, dos, true);
         dos.flush();
         byte[] hashBytes = this.computeSHAdigest(bos.toByteArray());

         for(int i = Math.min(hashBytes.length, 8) - 1; i >= 0; --i) {
            svuid = svuid << 8 | (long)(hashBytes[i] & 255);
         }
      } finally {
         if (dos != null) {
            dos.close();
         }

      }

      return svuid;
   }

   protected byte[] computeSHAdigest(byte[] value) {
      try {
         return MessageDigest.getInstance("SHA").digest(value);
      } catch (Exception var3) {
         throw new UnsupportedOperationException(var3.toString());
      }
   }

   private static void writeItems(Collection<SerialVersionUIDAdder.Item> itemCollection, DataOutput dos, boolean dotted) throws IOException {
      int size = itemCollection.size();
      SerialVersionUIDAdder.Item[] items = (SerialVersionUIDAdder.Item[])itemCollection.toArray(new SerialVersionUIDAdder.Item[size]);
      Arrays.sort(items);

      for(int i = 0; i < size; ++i) {
         dos.writeUTF(items[i].name);
         dos.writeInt(items[i].access);
         dos.writeUTF(dotted ? items[i].desc.replace('/', '.') : items[i].desc);
      }

   }

   private static class Item implements Comparable<SerialVersionUIDAdder.Item> {
      final String name;
      final int access;
      final String desc;

      Item(String name, int access, String desc) {
         this.name = name;
         this.access = access;
         this.desc = desc;
      }

      public int compareTo(SerialVersionUIDAdder.Item other) {
         int retVal = this.name.compareTo(other.name);
         if (retVal == 0) {
            retVal = this.desc.compareTo(other.desc);
         }

         return retVal;
      }

      public boolean equals(Object o) {
         if (o instanceof SerialVersionUIDAdder.Item) {
            return this.compareTo((SerialVersionUIDAdder.Item)o) == 0;
         } else {
            return false;
         }
      }

      public int hashCode() {
         return (this.name + this.desc).hashCode();
      }
   }
}
