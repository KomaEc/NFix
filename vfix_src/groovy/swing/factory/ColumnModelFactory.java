package groovy.swing.factory;

import groovy.lang.Closure;
import groovy.lang.GroovyObject;
import groovy.lang.MetaClass;
import groovy.util.AbstractFactory;
import groovy.util.FactoryBuilderSupport;
import java.lang.ref.SoftReference;
import java.util.Map;
import java.util.logging.Logger;
import javax.swing.JTable;
import javax.swing.table.TableColumnModel;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class ColumnModelFactory extends AbstractFactory implements GroovyObject {
   private static final Logger log = (Logger)((Logger)ScriptBytecodeAdapter.castToType($getCallSiteArray()[10].call($get$$class$java$util$logging$Logger(), (Object)$getCallSiteArray()[11].callGetProperty($get$$class$groovy$swing$factory$ColumnModelFactory())), $get$$class$java$util$logging$Logger()));
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private transient MetaClass metaClass;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524204459L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524204459 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClass;
   // $FF: synthetic field
   private static Class $class$java$lang$Object;
   // $FF: synthetic field
   private static Class $class$java$util$logging$Logger;
   // $FF: synthetic field
   private static Class $class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$javax$swing$table$DefaultTableColumnModel;
   // $FF: synthetic field
   private static Class $class$java$lang$Class;
   // $FF: synthetic field
   private static Class $class$groovy$swing$factory$ColumnModelFactory;

   public ColumnModelFactory() {
      CallSite[] var1 = $getCallSiteArray();
      this.metaClass = (MetaClass)ScriptBytecodeAdapter.castToType(this.$getStaticMetaClass(), $get$$class$groovy$lang$MetaClass());
   }

   public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) {
      CallSite[] var5 = $getCallSiteArray();
      if (value instanceof TableColumnModel) {
         return (Object)ScriptBytecodeAdapter.castToType(value, $get$$class$java$lang$Object());
      } else {
         Class jxTableClass = (Class)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$java$lang$Class());

         try {
            jxTableClass = (Class)ScriptBytecodeAdapter.castToType(var5[0].call($get$$class$java$lang$Class(), (Object)"org.jdesktop.swingx.JXTable"), $get$$class$java$lang$Class());
         } catch (ClassNotFoundException var10) {
         } finally {
            ;
         }

         return DefaultTypeTransformation.booleanUnbox(ScriptBytecodeAdapter.compareNotEqual(jxTableClass, (Object)null) && DefaultTypeTransformation.booleanUnbox(var5[1].call(jxTableClass, (Object)var5[2].call(var5[3].callGroovyObjectGetProperty(builder)))) ? Boolean.TRUE : Boolean.FALSE) ? (Object)ScriptBytecodeAdapter.castToType(var5[4].call(var5[5].call($get$$class$java$lang$Class(), (Object)"org.jdesktop.swingx.table.DefaultTableColumnModelExt")), $get$$class$java$lang$Object()) : (Object)ScriptBytecodeAdapter.castToType(var5[6].callConstructor($get$$class$javax$swing$table$DefaultTableColumnModel()), $get$$class$java$lang$Object());
      }
   }

   public void onNodeCompleted(FactoryBuilderSupport builder, Object parent, Object node) {
      CallSite[] var4 = $getCallSiteArray();
      if (!(parent instanceof JTable)) {
         var4[7].call(log, (Object)var4[8].call("ColumnModel must be a child of a table. Found: ", (Object)var4[9].call(parent)));
      }

      ScriptBytecodeAdapter.setProperty(node, $get$$class$groovy$swing$factory$ColumnModelFactory(), parent, "columnModel");
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$groovy$swing$factory$ColumnModelFactory()) {
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
      Class var10000 = $get$$class$groovy$swing$factory$ColumnModelFactory();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$3(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$groovy$swing$factory$ColumnModelFactory(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$3(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$groovy$swing$factory$ColumnModelFactory(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
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
      var0[0] = "forName";
      var0[1] = "isAssignableFrom";
      var0[2] = "getClass";
      var0[3] = "current";
      var0[4] = "newInstance";
      var0[5] = "forName";
      var0[6] = "<$constructor$>";
      var0[7] = "warning";
      var0[8] = "plus";
      var0[9] = "getClass";
      var0[10] = "getLogger";
      var0[11] = "name";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[12];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$groovy$swing$factory$ColumnModelFactory(), var0);
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
   private static Class $get$$class$java$util$logging$Logger() {
      Class var10000 = $class$java$util$logging$Logger;
      if (var10000 == null) {
         var10000 = $class$java$util$logging$Logger = class$("java.util.logging.Logger");
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
   private static Class $get$$class$javax$swing$table$DefaultTableColumnModel() {
      Class var10000 = $class$javax$swing$table$DefaultTableColumnModel;
      if (var10000 == null) {
         var10000 = $class$javax$swing$table$DefaultTableColumnModel = class$("javax.swing.table.DefaultTableColumnModel");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$lang$Class() {
      Class var10000 = $class$java$lang$Class;
      if (var10000 == null) {
         var10000 = $class$java$lang$Class = class$("java.lang.Class");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$swing$factory$ColumnModelFactory() {
      Class var10000 = $class$groovy$swing$factory$ColumnModelFactory;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$factory$ColumnModelFactory = class$("groovy.swing.factory.ColumnModelFactory");
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
