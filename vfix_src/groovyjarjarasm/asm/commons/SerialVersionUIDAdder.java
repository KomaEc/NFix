package groovyjarjarasm.asm.commons;

import groovyjarjarasm.asm.ClassAdapter;
import groovyjarjarasm.asm.ClassVisitor;
import groovyjarjarasm.asm.FieldVisitor;
import groovyjarjarasm.asm.MethodVisitor;
import java.io.ByteArrayOutputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class SerialVersionUIDAdder extends ClassAdapter {
   protected boolean computeSVUID;
   protected boolean hasSVUID;
   protected int access;
   protected String name;
   protected String[] interfaces;
   protected Collection svuidFields = new ArrayList();
   protected boolean hasStaticInitializer;
   protected Collection svuidConstructors = new ArrayList();
   protected Collection svuidMethods = new ArrayList();

   public SerialVersionUIDAdder(ClassVisitor var1) {
      super(var1);
   }

   public void visit(int var1, int var2, String var3, String var4, String var5, String[] var6) {
      this.computeSVUID = (var2 & 512) == 0;
      if (this.computeSVUID) {
         this.name = var3;
         this.access = var2;
         this.interfaces = var6;
      }

      super.visit(var1, var2, var3, var4, var5, var6);
   }

   public MethodVisitor visitMethod(int var1, String var2, String var3, String var4, String[] var5) {
      if (this.computeSVUID) {
         if ("<clinit>".equals(var2)) {
            this.hasStaticInitializer = true;
         }

         int var6 = var1 & 3391;
         if ((var1 & 2) == 0) {
            if ("<init>".equals(var2)) {
               this.svuidConstructors.add(new SerialVersionUIDAdder$Item(var2, var6, var3));
            } else if (!"<clinit>".equals(var2)) {
               this.svuidMethods.add(new SerialVersionUIDAdder$Item(var2, var6, var3));
            }
         }
      }

      return this.cv.visitMethod(var1, var2, var3, var4, var5);
   }

   public FieldVisitor visitField(int var1, String var2, String var3, String var4, Object var5) {
      if (this.computeSVUID) {
         if ("serialVersionUID".equals(var2)) {
            this.computeSVUID = false;
            this.hasSVUID = true;
         }

         int var6 = var1 & 223;
         if ((var1 & 2) == 0 || (var1 & 136) == 0) {
            this.svuidFields.add(new SerialVersionUIDAdder$Item(var2, var6, var3));
         }
      }

      return super.visitField(var1, var2, var3, var4, var5);
   }

   public void visitInnerClass(String var1, String var2, String var3, int var4) {
      if (var2 != null && var3 != null && this.name != null) {
         int var5 = this.name.length();
         int var6 = var3.length();
         int var7 = var2.length();
         if (var5 == var7 + 1 + var6 && this.name.startsWith(var2) && this.name.endsWith(var3) && this.name.charAt(var7) == '$') {
            this.access = var4;
         }
      }

      super.visitInnerClass(var1, var2, var3, var4);
   }

   public void visitEnd() {
      if (this.computeSVUID && !this.hasSVUID) {
         try {
            this.cv.visitField(24, "serialVersionUID", "J", (String)null, new Long(this.computeSVUID()));
         } catch (Throwable var2) {
            throw new RuntimeException("Error while computing SVUID for " + this.name, var2);
         }
      }

      super.visitEnd();
   }

   protected long computeSVUID() throws IOException {
      DataOutputStream var1 = null;
      long var2 = 0L;

      try {
         ByteArrayOutputStream var4 = new ByteArrayOutputStream();
         var1 = new DataOutputStream(var4);
         var1.writeUTF(this.name.replace('/', '.'));
         var1.writeInt(this.access & 1553);
         Arrays.sort(this.interfaces);

         for(int var5 = 0; var5 < this.interfaces.length; ++var5) {
            var1.writeUTF(this.interfaces[var5].replace('/', '.'));
         }

         writeItems(this.svuidFields, var1, false);
         if (this.hasStaticInitializer) {
            var1.writeUTF("<clinit>");
            var1.writeInt(8);
            var1.writeUTF("()V");
         }

         writeItems(this.svuidConstructors, var1, true);
         writeItems(this.svuidMethods, var1, true);
         var1.flush();
         byte[] var10 = this.computeSHAdigest(var4.toByteArray());

         for(int var6 = Math.min(var10.length, 8) - 1; var6 >= 0; --var6) {
            var2 = var2 << 8 | (long)(var10[var6] & 255);
         }
      } finally {
         if (var1 != null) {
            var1.close();
         }

      }

      return var2;
   }

   protected byte[] computeSHAdigest(byte[] var1) {
      try {
         return MessageDigest.getInstance("SHA").digest(var1);
      } catch (Exception var3) {
         throw new UnsupportedOperationException(var3.toString());
      }
   }

   private static void writeItems(Collection var0, DataOutput var1, boolean var2) throws IOException {
      int var3 = var0.size();
      SerialVersionUIDAdder$Item[] var4 = (SerialVersionUIDAdder$Item[])((SerialVersionUIDAdder$Item[])var0.toArray(new SerialVersionUIDAdder$Item[var3]));
      Arrays.sort(var4);

      for(int var5 = 0; var5 < var3; ++var5) {
         var1.writeUTF(var4[var5].name);
         var1.writeInt(var4[var5].access);
         var1.writeUTF(var2 ? var4[var5].desc.replace('/', '.') : var4[var5].desc);
      }

   }
}
