package groovy.ui.text;

import groovy.lang.GroovyObject;
import groovy.lang.MetaClass;
import groovy.lang.Reference;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.lang.ref.SoftReference;
import javax.swing.AbstractAction;
import javax.swing.text.AttributeSet;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.GeneratedClosure;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class AutoIndentAction extends AbstractAction implements GroovyObject {
   private AttributeSet simpleAttributeSet;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private transient MetaClass metaClass;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524204585L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524204585 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$java$lang$Integer;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClass;
   // $FF: synthetic field
   private static Class $class$javax$swing$text$SimpleAttributeSet;
   // $FF: synthetic field
   private static Class $class$javax$swing$text$AttributeSet;
   // $FF: synthetic field
   private static Class $class$groovy$ui$text$AutoIndentAction;
   // $FF: synthetic field
   private static Class $class$java$lang$String;

   public AutoIndentAction() {
      CallSite[] var1 = $getCallSiteArray();
      this.simpleAttributeSet = (AttributeSet)ScriptBytecodeAdapter.castToType(var1[0].callConstructor($get$$class$javax$swing$text$SimpleAttributeSet()), $get$$class$javax$swing$text$AttributeSet());
      this.metaClass = (MetaClass)ScriptBytecodeAdapter.castToType(this.$getStaticMetaClass(), $get$$class$groovy$lang$MetaClass());
   }

   public void actionPerformed(ActionEvent evt) {
      CallSite[] var2 = $getCallSiteArray();
      Object inputArea = var2[1].callGetProperty(evt);
      Object rootElement = var2[2].callGetProperty(var2[3].callGetProperty(inputArea));
      Object cursorPos = var2[4].call(inputArea);
      Integer rowNum = (Integer)ScriptBytecodeAdapter.castToType(var2[5].call(rootElement, cursorPos), $get$$class$java$lang$Integer());
      Object rowElement = var2[6].call(rootElement, (Object)rowNum);
      Integer startOffset = (Integer)ScriptBytecodeAdapter.castToType(var2[7].call(rowElement), $get$$class$java$lang$Integer());
      Integer endOffset = (Integer)ScriptBytecodeAdapter.castToType(var2[8].call(rowElement), $get$$class$java$lang$Integer());
      String rowContent = (String)ScriptBytecodeAdapter.castToType(var2[9].call(var2[10].callGetProperty(inputArea), startOffset, var2[11].call(endOffset, (Object)startOffset)), $get$$class$java$lang$String());
      String contentBeforeCursor = (String)ScriptBytecodeAdapter.castToType(var2[12].call(var2[13].callGetProperty(inputArea), startOffset, var2[14].call(cursorPos, (Object)startOffset)), $get$$class$java$lang$String());
      String whitespaceStr = new Reference("");
      Object matcher = ScriptBytecodeAdapter.findRegex(rowContent, "(?m)^(\\s*).*\\n$");
      var2[15].call(matcher, (Object)(new GeneratedClosure(this, this, whitespaceStr) {
         private Reference<T> whitespaceStr;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$groovy$ui$text$AutoIndentAction$_actionPerformed_closure1;
         // $FF: synthetic field
         private static Class $class$java$lang$String;

         public {
            CallSite[] var4 = $getCallSiteArray();
            this.whitespaceStr = (Reference)whitespaceStr;
         }

         public Object doCall(Object all, Object ws) {
            Object wsx = new Reference(ws);
            CallSite[] var4 = $getCallSiteArray();
            String var10000 = (String)ScriptBytecodeAdapter.castToType(wsx.get(), $get$$class$java$lang$String());
            this.whitespaceStr.set(var10000);
            return var10000;
         }

         public Object call(Object all, Object wsx) {
            Object ws = new Reference(wsx);
            CallSite[] var4 = $getCallSiteArray();
            return var4[0].callCurrent(this, all, ws.get());
         }

         public String getWhitespaceStr() {
            CallSite[] var1 = $getCallSiteArray();
            return (String)ScriptBytecodeAdapter.castToType(this.whitespaceStr.get(), $get$$class$java$lang$String());
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$ui$text$AutoIndentAction$_actionPerformed_closure1()) {
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
         private static void $createCallSiteArray_1(String[] var0) {
            var0[0] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[1];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$ui$text$AutoIndentAction$_actionPerformed_closure1(), var0);
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
         private static Class $get$$class$groovy$ui$text$AutoIndentAction$_actionPerformed_closure1() {
            Class var10000 = $class$groovy$ui$text$AutoIndentAction$_actionPerformed_closure1;
            if (var10000 == null) {
               var10000 = $class$groovy$ui$text$AutoIndentAction$_actionPerformed_closure1 = class$("groovy.ui.text.AutoIndentAction$_actionPerformed_closure1");
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
         static Class class$(String var0) {
            try {
               return Class.forName(var0);
            } catch (ClassNotFoundException var2) {
               throw new NoClassDefFoundError(var2.getMessage());
            }
         }
      }));
      if (ScriptBytecodeAdapter.matchRegex(contentBeforeCursor, "(\\s)*")) {
         whitespaceStr.set(contentBeforeCursor);
      }

      var2[16].call(var2[17].callGetProperty(inputArea), cursorPos, var2[18].call("\n", (Object)whitespaceStr.get()), this.simpleAttributeSet);
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$groovy$ui$text$AutoIndentAction()) {
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
      Class var10000 = $get$$class$groovy$ui$text$AutoIndentAction();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$3(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$groovy$ui$text$AutoIndentAction(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$3(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$groovy$ui$text$AutoIndentAction(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
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

   public AttributeSet getSimpleAttributeSet() {
      return this.simpleAttributeSet;
   }

   public void setSimpleAttributeSet(AttributeSet var1) {
      this.simpleAttributeSet = var1;
   }

   // $FF: synthetic method
   public String super$1$toString() {
      return super.toString();
   }

   // $FF: synthetic method
   public boolean super$2$isEnabled() {
      return super.isEnabled();
   }

   // $FF: synthetic method
   public void super$1$notify() {
      super.notify();
   }

   // $FF: synthetic method
   public void super$2$removePropertyChangeListener(PropertyChangeListener var1) {
      super.removePropertyChangeListener(var1);
   }

   // $FF: synthetic method
   public void super$1$notifyAll() {
      super.notifyAll();
   }

   // $FF: synthetic method
   public void super$2$firePropertyChange(String var1, Object var2, Object var3) {
      super.firePropertyChange(var1, var2, var3);
   }

   // $FF: synthetic method
   public Object super$2$clone() {
      return super.clone();
   }

   // $FF: synthetic method
   public void super$2$putValue(String var1, Object var2) {
      super.putValue(var1, var2);
   }

   // $FF: synthetic method
   public void super$1$wait() {
      super.wait();
   }

   // $FF: synthetic method
   public void super$2$addPropertyChangeListener(PropertyChangeListener var1) {
      super.addPropertyChangeListener(var1);
   }

   // $FF: synthetic method
   public void super$1$wait(long var1, int var3) {
      super.wait(var1, var3);
   }

   // $FF: synthetic method
   public Object[] super$2$getKeys() {
      return super.getKeys();
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
   public void super$1$wait(long var1) {
      super.wait(var1);
   }

   // $FF: synthetic method
   public Object super$2$getValue(String var1) {
      return super.getValue(var1);
   }

   // $FF: synthetic method
   public PropertyChangeListener[] super$2$getPropertyChangeListeners() {
      return super.getPropertyChangeListeners();
   }

   // $FF: synthetic method
   public void super$2$setEnabled(boolean var1) {
      super.setEnabled(var1);
   }

   // $FF: synthetic method
   public boolean super$1$equals(Object var1) {
      return super.equals(var1);
   }

   // $FF: synthetic method
   public int super$1$hashCode() {
      return super.hashCode();
   }

   // $FF: synthetic method
   private static void $createCallSiteArray_1(String[] var0) {
      var0[0] = "<$constructor$>";
      var0[1] = "source";
      var0[2] = "defaultRootElement";
      var0[3] = "document";
      var0[4] = "getCaretPosition";
      var0[5] = "getElementIndex";
      var0[6] = "getElement";
      var0[7] = "getStartOffset";
      var0[8] = "getEndOffset";
      var0[9] = "getText";
      var0[10] = "document";
      var0[11] = "minus";
      var0[12] = "getText";
      var0[13] = "document";
      var0[14] = "minus";
      var0[15] = "each";
      var0[16] = "insertString";
      var0[17] = "document";
      var0[18] = "plus";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[19];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$groovy$ui$text$AutoIndentAction(), var0);
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
   private static Class $get$$class$java$lang$Integer() {
      Class var10000 = $class$java$lang$Integer;
      if (var10000 == null) {
         var10000 = $class$java$lang$Integer = class$("java.lang.Integer");
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
   private static Class $get$$class$javax$swing$text$SimpleAttributeSet() {
      Class var10000 = $class$javax$swing$text$SimpleAttributeSet;
      if (var10000 == null) {
         var10000 = $class$javax$swing$text$SimpleAttributeSet = class$("javax.swing.text.SimpleAttributeSet");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$javax$swing$text$AttributeSet() {
      Class var10000 = $class$javax$swing$text$AttributeSet;
      if (var10000 == null) {
         var10000 = $class$javax$swing$text$AttributeSet = class$("javax.swing.text.AttributeSet");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$ui$text$AutoIndentAction() {
      Class var10000 = $class$groovy$ui$text$AutoIndentAction;
      if (var10000 == null) {
         var10000 = $class$groovy$ui$text$AutoIndentAction = class$("groovy.ui.text.AutoIndentAction");
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
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }
}
