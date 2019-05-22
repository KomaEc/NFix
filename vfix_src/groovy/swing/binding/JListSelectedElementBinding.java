package groovy.swing.binding;

import groovy.lang.Closure;
import groovy.lang.GroovyObject;
import groovy.lang.MetaClass;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.ref.SoftReference;
import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.codehaus.groovy.binding.PropertyBinding;
import org.codehaus.groovy.binding.SourceBinding;
import org.codehaus.groovy.binding.TargetBinding;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class JListSelectedElementBinding extends AbstractSyntheticBinding implements PropertyChangeListener, ListSelectionListener, GroovyObject {
   private JList boundList;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private transient MetaClass metaClass;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524204961L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524204961 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClass;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$binding$PropertyBinding;
   // $FF: synthetic field
   private static Class $class$groovy$swing$binding$JListSelectedElementBinding;
   // $FF: synthetic field
   private static Class $class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$groovy$swing$binding$AbstractSyntheticBinding;
   // $FF: synthetic field
   private static Class $class$javax$swing$JList;

   protected JListSelectedElementBinding(PropertyBinding source, TargetBinding target, String propertyName) {
      CallSite[] var4 = $getCallSiteArray();
      Object[] var10000 = new Object[]{source, target, $get$$class$javax$swing$JList(), propertyName};
      int var10002 = ScriptBytecodeAdapter.selectConstructorAndTransformArguments(var10000, 1, $get$$class$groovy$swing$binding$AbstractSyntheticBinding());
      Object[] var10001 = var10000;
      if ((var10002 & 1) != 0) {
         var10001 = (Object[])var10000[0];
      }

      switch(var10002 >> 8) {
      case 0:
         super((PropertyBinding)var10001[0], (TargetBinding)var10001[1], (Class)var10001[2], (String)var10001[3]);
         this.metaClass = (MetaClass)ScriptBytecodeAdapter.castToType(this.$getStaticMetaClass(), $get$$class$groovy$lang$MetaClass());
         return;
      default:
         throw new IllegalArgumentException("illegal constructor number");
      }
   }

   public synchronized void syntheticBind() {
      CallSite[] var1 = $getCallSiteArray();
      this.boundList = (JList)ScriptBytecodeAdapter.castToType((JList)ScriptBytecodeAdapter.castToType(var1[0].call((PropertyBinding)ScriptBytecodeAdapter.castToType(var1[1].callGroovyObjectGetProperty(this), $get$$class$org$codehaus$groovy$binding$PropertyBinding())), $get$$class$javax$swing$JList()), $get$$class$javax$swing$JList());
      var1[2].call(this.boundList, "selectionModel", this);
      var1[3].call(this.boundList, (Object)this);
   }

   public synchronized void syntheticUnbind() {
      CallSite[] var1 = $getCallSiteArray();
      var1[4].call(this.boundList, "selectionModel", this);
      var1[5].call(this.boundList, (Object)this);
      this.boundList = (JList)ScriptBytecodeAdapter.castToType((JList)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$javax$swing$JList()), $get$$class$javax$swing$JList());
   }

   public void setTargetBinding(TargetBinding target) {
      CallSite[] var2 = $getCallSiteArray();
      ScriptBytecodeAdapter.invokeMethodOnSuperN($get$$class$groovy$swing$binding$AbstractSyntheticBinding(), this, "setTargetBinding", new Object[]{target});
   }

   public void propertyChange(PropertyChangeEvent event) {
      CallSite[] var2 = $getCallSiteArray();
      var2[6].callCurrent(this);
   }

   public void valueChanged(ListSelectionEvent e) {
      CallSite[] var2 = $getCallSiteArray();
      var2[7].callCurrent(this);
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$groovy$swing$binding$JListSelectedElementBinding()) {
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
   public Object this$dist$invoke$4(String name, Object args) {
      CallSite[] var3 = $getCallSiteArray();
      Class var10000 = $get$$class$groovy$swing$binding$JListSelectedElementBinding();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$4(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$groovy$swing$binding$JListSelectedElementBinding(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$4(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$groovy$swing$binding$JListSelectedElementBinding(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
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

   public JList getBoundList() {
      return this.boundList;
   }

   public void setBoundList(JList var1) {
      this.boundList = var1;
   }

   // $FF: synthetic method
   public String super$1$toString() {
      return super.toString();
   }

   // $FF: synthetic method
   public void super$3$setTargetBinding(TargetBinding var1) {
      super.setTargetBinding(var1);
   }

   // $FF: synthetic method
   public void super$1$notify() {
      super.notify();
   }

   // $FF: synthetic method
   public void super$1$notifyAll() {
      super.notifyAll();
   }

   // $FF: synthetic method
   public void super$3$bind() {
      super.bind();
   }

   // $FF: synthetic method
   public Closure super$2$getConverter() {
      return super.getConverter();
   }

   // $FF: synthetic method
   public void super$3$setSourceBinding(SourceBinding var1) {
      super.setSourceBinding(var1);
   }

   // $FF: synthetic method
   public Closure super$2$getReverseConverter() {
      return super.getReverseConverter();
   }

   // $FF: synthetic method
   public void super$2$setReverseConverter(Closure var1) {
      super.setReverseConverter(var1);
   }

   // $FF: synthetic method
   public void super$2$update() {
      super.update();
   }

   // $FF: synthetic method
   public Object super$1$clone() {
      return super.clone();
   }

   // $FF: synthetic method
   public TargetBinding super$2$getTargetBinding() {
      return super.getTargetBinding();
   }

   // $FF: synthetic method
   public void super$3$unbind() {
      super.unbind();
   }

   // $FF: synthetic method
   public void super$1$wait() {
      super.wait();
   }

   // $FF: synthetic method
   public void super$2$reverseUpdate() {
      super.reverseUpdate();
   }

   // $FF: synthetic method
   public void super$1$wait(long var1, int var3) {
      super.wait(var1, var3);
   }

   // $FF: synthetic method
   public Class super$1$getClass() {
      return super.getClass();
   }

   // $FF: synthetic method
   public void super$1$finalize() {
      super.finalize();
   }

   // $FF: synthetic method
   public SourceBinding super$2$getSourceBinding() {
      return super.getSourceBinding();
   }

   // $FF: synthetic method
   public void super$2$setConverter(Closure var1) {
      super.setConverter(var1);
   }

   // $FF: synthetic method
   public void super$1$wait(long var1) {
      super.wait(var1);
   }

   // $FF: synthetic method
   public boolean super$1$equals(Object var1) {
      return super.equals(var1);
   }

   // $FF: synthetic method
   public void super$3$rebind() {
      super.rebind();
   }

   // $FF: synthetic method
   public Closure super$2$getValidator() {
      return super.getValidator();
   }

   // $FF: synthetic method
   public int super$1$hashCode() {
      return super.hashCode();
   }

   // $FF: synthetic method
   public void super$2$setValidator(Closure var1) {
      super.setValidator(var1);
   }

   // $FF: synthetic method
   private static void $createCallSiteArray_1(String[] var0) {
      var0[0] = "getBean";
      var0[1] = "sourceBinding";
      var0[2] = "addPropertyChangeListener";
      var0[3] = "addListSelectionListener";
      var0[4] = "removePropertyChangeListener";
      var0[5] = "removeListSelectionListener";
      var0[6] = "update";
      var0[7] = "update";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[8];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$groovy$swing$binding$JListSelectedElementBinding(), var0);
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
   private static Class $get$$class$org$codehaus$groovy$binding$PropertyBinding() {
      Class var10000 = $class$org$codehaus$groovy$binding$PropertyBinding;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$binding$PropertyBinding = class$("org.codehaus.groovy.binding.PropertyBinding");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$swing$binding$JListSelectedElementBinding() {
      Class var10000 = $class$groovy$swing$binding$JListSelectedElementBinding;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$binding$JListSelectedElementBinding = class$("groovy.swing.binding.JListSelectedElementBinding");
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
   private static Class $get$$class$groovy$swing$binding$AbstractSyntheticBinding() {
      Class var10000 = $class$groovy$swing$binding$AbstractSyntheticBinding;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$binding$AbstractSyntheticBinding = class$("groovy.swing.binding.AbstractSyntheticBinding");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$javax$swing$JList() {
      Class var10000 = $class$javax$swing$JList;
      if (var10000 == null) {
         var10000 = $class$javax$swing$JList = class$("javax.swing.JList");
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
