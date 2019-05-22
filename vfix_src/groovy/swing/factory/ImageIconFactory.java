package groovy.swing.factory;

import groovy.lang.Closure;
import groovy.lang.GString;
import groovy.lang.GroovyObject;
import groovy.lang.MetaClass;
import groovy.util.AbstractFactory;
import groovy.util.FactoryBuilderSupport;
import java.awt.Image;
import java.io.File;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.Map;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class ImageIconFactory extends AbstractFactory implements GroovyObject {
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private transient MetaClass metaClass;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524204466L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524204466 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$javax$swing$ImageIcon;
   // $FF: synthetic field
   private static Class $class$groovy$swing$factory$ImageIconFactory;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClass;
   // $FF: synthetic field
   private static Class $class$java$lang$Object;
   // $FF: synthetic field
   private static Class $class$java$lang$RuntimeException;
   // $FF: synthetic field
   private static Class $class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$java$io$File;

   public ImageIconFactory() {
      CallSite[] var1 = $getCallSiteArray();
      this.metaClass = (MetaClass)ScriptBytecodeAdapter.castToType(this.$getStaticMetaClass(), $get$$class$groovy$lang$MetaClass());
   }

   public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) {
      CallSite[] var5 = $getCallSiteArray();
      if (ScriptBytecodeAdapter.compareEqual(value, (Object)null)) {
         if (DefaultTypeTransformation.booleanUnbox(var5[0].call(attributes, (Object)"image"))) {
            value = var5[1].call(attributes, (Object)"image");
            if (!(value instanceof Image)) {
               throw (Throwable)var5[2].callConstructor($get$$class$java$lang$RuntimeException(), (Object)(new GStringImpl(new Object[]{name}, new String[]{"In ", " image: attributes must be of type java.awt.Image"})));
            }
         } else if (DefaultTypeTransformation.booleanUnbox(var5[3].call(attributes, (Object)"url"))) {
            value = var5[4].call(attributes, (Object)"url");
            if (!(value instanceof URL)) {
               throw (Throwable)var5[5].callConstructor($get$$class$java$lang$RuntimeException(), (Object)(new GStringImpl(new Object[]{name}, new String[]{"In ", " url: attributes must be of type java.net.URL"})));
            }
         } else if (DefaultTypeTransformation.booleanUnbox(var5[6].call(attributes, (Object)"file"))) {
            value = var5[7].call(attributes, (Object)"file");
            if (value instanceof GString) {
               value = (String)ScriptBytecodeAdapter.asType(value, $get$$class$java$lang$String());
            }

            if (value instanceof File) {
               value = var5[8].call(value);
            } else if (!(value instanceof String)) {
               throw (Throwable)var5[9].callConstructor($get$$class$java$lang$RuntimeException(), (Object)(new GStringImpl(new Object[]{name}, new String[]{"In ", " file: attributes must be of type java.io.File or a string"})));
            }
         }
      } else if (value instanceof GString) {
         value = (String)ScriptBytecodeAdapter.asType(value, $get$$class$java$lang$String());
      }

      Object resource = null;
      if (DefaultTypeTransformation.booleanUnbox(ScriptBytecodeAdapter.compareEqual(value, (Object)null) && DefaultTypeTransformation.booleanUnbox(var5[10].call(attributes, (Object)"resource")) ? Boolean.TRUE : Boolean.FALSE)) {
         resource = var5[11].call(attributes, (Object)"resource");
      } else if (DefaultTypeTransformation.booleanUnbox(value instanceof String && !DefaultTypeTransformation.booleanUnbox(var5[12].call(var5[13].callConstructor($get$$class$java$io$File(), (Object)value))) ? Boolean.TRUE : Boolean.FALSE)) {
         resource = value;
      }

      if (ScriptBytecodeAdapter.compareNotEqual(resource, (Object)null)) {
         Object klass = var5[14].callGetProperty(var5[15].callGroovyObjectGetProperty(builder));
         Object origValue = value;
         if (DefaultTypeTransformation.booleanUnbox(var5[16].call(attributes, (Object)"class"))) {
            klass = var5[17].call(attributes, (Object)"class");
         }

         if (ScriptBytecodeAdapter.compareEqual(klass, (Object)null)) {
            klass = $get$$class$groovy$swing$factory$ImageIconFactory();
         } else if (!(klass instanceof Class)) {
            klass = var5[18].callGetProperty(klass);
         }

         value = var5[19].call(klass, resource);
         if (ScriptBytecodeAdapter.compareEqual(value, (Object)null)) {
            throw (Throwable)var5[20].callConstructor($get$$class$java$lang$RuntimeException(), (Object)(new GStringImpl(new Object[]{name, origValue}, new String[]{"In ", " the value argument '", "' does not refer to a file or a class resource"})));
         }
      }

      if (ScriptBytecodeAdapter.compareEqual(value, (Object)null)) {
         throw (Throwable)var5[21].callConstructor($get$$class$java$lang$RuntimeException(), (Object)(new GStringImpl(new Object[]{name}, new String[]{"", " has neither a value argument or one of image:, url:, file:, or resource:"})));
      } else {
         return DefaultTypeTransformation.booleanUnbox(var5[22].call(attributes, (Object)"description")) ? (Object)ScriptBytecodeAdapter.castToType(var5[23].callConstructor($get$$class$javax$swing$ImageIcon(), value, var5[24].call(attributes, (Object)"description")), $get$$class$java$lang$Object()) : (Object)ScriptBytecodeAdapter.castToType(var5[25].callConstructor($get$$class$javax$swing$ImageIcon(), (Object)value), $get$$class$java$lang$Object());
      }
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$groovy$swing$factory$ImageIconFactory()) {
         return ScriptBytecodeAdapter.initMetaClass(this);
      } else {
         ClassInfo var1 = $staticClassInfo;
         if (var1 == null) {
            $staticClassInfo = var1 = ClassInfo.getClassInfo(this.getClass());
         }

         return var1.getMetaClass();
      }
   }

   // $FF: synthetic method
   public Object this$dist$invoke$3(String name, Object args) {
      CallSite[] var3 = $getCallSiteArray();
      Class var10000 = $get$$class$groovy$swing$factory$ImageIconFactory();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$3(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$groovy$swing$factory$ImageIconFactory(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$3(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$groovy$swing$factory$ImageIconFactory(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public MetaClass getMetaClass() {
      MetaClass var10000 = this.metaClass;
      if (var10000 != null) {
         return var10000;
      } else {
         this.metaClass = this.$getStaticMetaClass();
         return this.metaClass;
      }
   }

   // $FF: synthetic method
   public void setMetaClass(MetaClass var1) {
      this.metaClass = var1;
   }

   // $FF: synthetic method
   public Object invokeMethod(String var1, Object var2) {
      return this.getMetaClass().invokeMethod(this, var1, var2);
   }

   // $FF: synthetic method
   public Object getProperty(String var1) {
      return this.getMetaClass().getProperty(this, var1);
   }

   // $FF: synthetic method
   public void setProperty(String var1, Object var2) {
      this.getMetaClass().setProperty(this, var1, var2);
   }

   // $FF: synthetic method
   public void super$1$wait() {
      super.wait();
   }

   // $FF: synthetic method
   public String super$1$toString() {
      return super.toString();
   }

   // $FF: synthetic method
   public boolean super$2$isLeaf() {
      return super.isLeaf();
   }

   // $FF: synthetic method
   public void super$2$setChild(FactoryBuilderSupport var1, Object var2, Object var3) {
      super.setChild(var1, var2, var3);
   }

   // $FF: synthetic method
   public void super$1$wait(long var1, int var3) {
      super.wait(var1, var3);
   }

   // $FF: synthetic method
   public void super$1$notify() {
      super.notify();
   }

   // $FF: synthetic method
   public void super$2$onNodeCompleted(FactoryBuilderSupport var1, Object var2, Object var3) {
      super.onNodeCompleted(var1, var2, var3);
   }

   // $FF: synthetic method
   public Class super$1$getClass() {
      return super.getClass();
   }

   // $FF: synthetic method
   public void super$1$notifyAll() {
      super.notifyAll();
   }

   // $FF: synthetic method
   public void super$1$finalize() {
      super.finalize();
   }

   // $FF: synthetic method
   public void super$2$onFactoryRegistration(FactoryBuilderSupport var1, String var2, String var3) {
      super.onFactoryRegistration(var1, var2, var3);
   }

   // $FF: synthetic method
   public boolean super$2$onNodeChildren(FactoryBuilderSupport var1, Object var2, Closure var3) {
      return super.onNodeChildren(var1, var2, var3);
   }

   // $FF: synthetic method
   public void super$1$wait(long var1) {
      super.wait(var1);
   }

   // $FF: synthetic method
   public boolean super$2$isHandlesNodeChildren() {
      return super.isHandlesNodeChildren();
   }

   // $FF: synthetic method
   public void super$2$setParent(FactoryBuilderSupport var1, Object var2, Object var3) {
      super.setParent(var1, var2, var3);
   }

   // $FF: synthetic method
   public boolean super$2$onHandleNodeAttributes(FactoryBuilderSupport var1, Object var2, Map var3) {
      return super.onHandleNodeAttributes(var1, var2, var3);
   }

   // $FF: synthetic method
   public boolean super$1$equals(Object var1) {
      return super.equals(var1);
   }

   // $FF: synthetic method
   public Object super$1$clone() {
      return super.clone();
   }

   // $FF: synthetic method
   public int super$1$hashCode() {
      return super.hashCode();
   }

   // $FF: synthetic method
   private static void $createCallSiteArray_1(String[] var0) {
      var0[0] = "containsKey";
      var0[1] = "remove";
      var0[2] = "<$constructor$>";
      var0[3] = "containsKey";
      var0[4] = "remove";
      var0[5] = "<$constructor$>";
      var0[6] = "containsKey";
      var0[7] = "remove";
      var0[8] = "toURL";
      var0[9] = "<$constructor$>";
      var0[10] = "containsKey";
      var0[11] = "remove";
      var0[12] = "exists";
      var0[13] = "<$constructor$>";
      var0[14] = "owner";
      var0[15] = "context";
      var0[16] = "containsKey";
      var0[17] = "remove";
      var0[18] = "class";
      var0[19] = "getResource";
      var0[20] = "<$constructor$>";
      var0[21] = "<$constructor$>";
      var0[22] = "containsKey";
      var0[23] = "<$constructor$>";
      var0[24] = "remove";
      var0[25] = "<$constructor$>";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[26];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$groovy$swing$factory$ImageIconFactory(), var0);
   }

   // $FF: synthetic method
   private static CallSite[] $getCallSiteArray() {
      CallSiteArray var0;
      if ($callSiteArray == null || (var0 = (CallSiteArray)$callSiteArray.get()) == null) {
         var0 = $createCallSiteArray();
         $callSiteArray = new SoftReference(var0);
      }

      return var0.array;
   }

   // $FF: synthetic method
   private static Class $get$$class$javax$swing$ImageIcon() {
      Class var10000 = $class$javax$swing$ImageIcon;
      if (var10000 == null) {
         var10000 = $class$javax$swing$ImageIcon = class$("javax.swing.ImageIcon");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$swing$factory$ImageIconFactory() {
      Class var10000 = $class$groovy$swing$factory$ImageIconFactory;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$factory$ImageIconFactory = class$("groovy.swing.factory.ImageIconFactory");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$lang$MetaClass() {
      Class var10000 = $class$groovy$lang$MetaClass;
      if (var10000 == null) {
         var10000 = $class$groovy$lang$MetaClass = class$("groovy.lang.MetaClass");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$lang$Object() {
      Class var10000 = $class$java$lang$Object;
      if (var10000 == null) {
         var10000 = $class$java$lang$Object = class$("java.lang.Object");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$lang$RuntimeException() {
      Class var10000 = $class$java$lang$RuntimeException;
      if (var10000 == null) {
         var10000 = $class$java$lang$RuntimeException = class$("java.lang.RuntimeException");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$lang$String() {
      Class var10000 = $class$java$lang$String;
      if (var10000 == null) {
         var10000 = $class$java$lang$String = class$("java.lang.String");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$io$File() {
      Class var10000 = $class$java$io$File;
      if (var10000 == null) {
         var10000 = $class$java$io$File = class$("java.io.File");
      }

      return var10000;
   }

   // $FF: synthetic method
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }
}
