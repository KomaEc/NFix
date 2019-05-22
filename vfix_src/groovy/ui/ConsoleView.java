package groovy.ui;

import groovy.lang.Binding;
import groovy.lang.MetaClass;
import groovy.lang.Reference;
import groovy.lang.Script;
import java.awt.Window;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.File;
import java.lang.ref.SoftReference;
import javax.swing.event.DocumentListener;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.GeneratedClosure;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class ConsoleView extends Script {
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524205083L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524205083 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$groovy$ui$view$GTKDefaults;
   // $FF: synthetic field
   private static Class $class$groovy$lang$Script;
   // $FF: synthetic field
   private static Class $class$groovy$ui$ConsoleView;
   // $FF: synthetic field
   private static Class $class$javax$swing$UIManager;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$runtime$InvokerHelper;
   // $FF: synthetic field
   private static Class $class$java$awt$dnd$DropTargetListener;
   // $FF: synthetic field
   private static Class $class$javax$swing$event$DocumentListener;
   // $FF: synthetic field
   private static Class $class$groovy$ui$view$Defaults;
   // $FF: synthetic field
   private static Class $class$groovy$ui$view$WindowsDefaults;
   // $FF: synthetic field
   private static Class $class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$groovy$ui$view$MacOSXDefaults;

   public ConsoleView() {
      CallSite[] var1 = $getCallSiteArray();
   }

   public ConsoleView(Binding context) {
      CallSite[] var2 = $getCallSiteArray();
      ScriptBytecodeAdapter.invokeMethodOnSuperN($get$$class$groovy$lang$Script(), this, "setBinding", new Object[]{context});
   }

   public static void main(String... args) {
      CallSite[] var1 = $getCallSiteArray();
      var1[0].call($get$$class$org$codehaus$groovy$runtime$InvokerHelper(), $get$$class$groovy$ui$ConsoleView(), args);
   }

   public Object run() {
      CallSite[] var1 = $getCallSiteArray();
      Object var2 = var1[1].call($get$$class$javax$swing$UIManager());
      if (!ScriptBytecodeAdapter.isCase(var2, "com.sun.java.swing.plaf.windows.WindowsLookAndFeel") && !ScriptBytecodeAdapter.isCase(var2, "com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel")) {
         if (!ScriptBytecodeAdapter.isCase(var2, "apple.laf.AquaLookAndFeel") && !ScriptBytecodeAdapter.isCase(var2, "com.apple.laf.AquaLookAndFeel")) {
            if (ScriptBytecodeAdapter.isCase(var2, "com.sun.java.swing.plaf.gtk.GTKLookAndFeel")) {
               var1[4].callCurrent(this, (Object)$get$$class$groovy$ui$view$GTKDefaults());
            } else {
               var1[5].callCurrent(this, (Object)$get$$class$groovy$ui$view$Defaults());
            }
         } else {
            var1[3].callCurrent(this, (Object)$get$$class$groovy$ui$view$MacOSXDefaults());
         }
      } else {
         var1[2].callCurrent(this, (Object)$get$$class$groovy$ui$view$WindowsDefaults());
      }

      ScriptBytecodeAdapter.setProperty(this, $get$$class$groovy$ui$ConsoleView(), var1[6].callGetProperty(var1[7].callGroovyObjectGetProperty(this)), "delegate");
      ScriptBytecodeAdapter.setGroovyObjectProperty(var1[8].call(var1[9].call(var1[10].callGroovyObjectGetProperty(this), (Object)"rootContainerDelegate")), $get$$class$groovy$ui$ConsoleView(), this, "consoleFrame");
      var1[11].callCurrent(this, var1[12].callGroovyObjectGetProperty(this), new GeneratedClosure(this, this) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$groovy$ui$ConsoleView$_run_closure1;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(Object it) {
            CallSite[] var2 = $getCallSiteArray();
            ScriptBytecodeAdapter.setProperty(var2[0].callGroovyObjectGetProperty(this), $get$$class$groovy$ui$ConsoleView$_run_closure1(), var2[1].callGetProperty(var2[2].callGroovyObjectGetProperty(this)), "delegate");
            var2[3].call(var2[4].call(var2[5].callGroovyObjectGetProperty(this), (Object)"menuBarDelegate"), var2[6].callGroovyObjectGetProperty(this));
            var2[7].callCurrent(this, (Object)var2[8].callGroovyObjectGetProperty(this));
            var2[9].callCurrent(this, (Object)var2[10].callGroovyObjectGetProperty(this));
            return var2[11].callCurrent(this, (Object)var2[12].callGroovyObjectGetProperty(this));
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[13].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$ui$ConsoleView$_run_closure1()) {
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
            var0[0] = "delegate";
            var0[1] = "menuBarDelegate";
            var0[2] = "binding";
            var0[3] = "call";
            var0[4] = "getAt";
            var0[5] = "binding";
            var0[6] = "menuBarClass";
            var0[7] = "build";
            var0[8] = "contentPaneClass";
            var0[9] = "build";
            var0[10] = "toolBarClass";
            var0[11] = "build";
            var0[12] = "statusBarClass";
            var0[13] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[14];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$ui$ConsoleView$_run_closure1(), var0);
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
         private static Class $get$$class$java$lang$Object() {
            Class var10000 = $class$java$lang$Object;
            if (var10000 == null) {
               var10000 = $class$java$lang$Object = class$("java.lang.Object");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$groovy$ui$ConsoleView$_run_closure1() {
            Class var10000 = $class$groovy$ui$ConsoleView$_run_closure1;
            if (var10000 == null) {
               var10000 = $class$groovy$ui$ConsoleView$_run_closure1 = class$("groovy.ui.ConsoleView$_run_closure1");
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
      });
      ScriptBytecodeAdapter.setProperty(var1[13].callGroovyObjectGetProperty(this), $get$$class$groovy$ui$ConsoleView(), var1[14].callGroovyObjectGetProperty(this), "promptStyle");
      ScriptBytecodeAdapter.setProperty(var1[15].callGroovyObjectGetProperty(this), $get$$class$groovy$ui$ConsoleView(), var1[16].callGroovyObjectGetProperty(this), "commandStyle");
      ScriptBytecodeAdapter.setProperty(var1[17].callGroovyObjectGetProperty(this), $get$$class$groovy$ui$ConsoleView(), var1[18].callGroovyObjectGetProperty(this), "outputStyle");
      ScriptBytecodeAdapter.setProperty(var1[19].callGroovyObjectGetProperty(this), $get$$class$groovy$ui$ConsoleView(), var1[20].callGroovyObjectGetProperty(this), "stacktraceStyle");
      ScriptBytecodeAdapter.setProperty(var1[21].callGroovyObjectGetProperty(this), $get$$class$groovy$ui$ConsoleView(), var1[22].callGroovyObjectGetProperty(this), "hyperlinkStyle");
      ScriptBytecodeAdapter.setProperty(var1[23].callGroovyObjectGetProperty(this), $get$$class$groovy$ui$ConsoleView(), var1[24].callGroovyObjectGetProperty(this), "resultStyle");
      if (var1[25].callGroovyObjectGetProperty(this) instanceof Window) {
         ScriptBytecodeAdapter.setProperty(ScriptBytecodeAdapter.getMethodPointer(var1[26].callGroovyObjectGetProperty(this), "exit"), $get$$class$groovy$ui$ConsoleView(), var1[27].callGroovyObjectGetProperty(this), "windowClosing");
      }

      ScriptBytecodeAdapter.setProperty(var1[28].callGroovyObjectGetProperty(this), $get$$class$groovy$ui$ConsoleView(), var1[29].callGroovyObjectGetProperty(this), "inputEditor");
      ScriptBytecodeAdapter.setProperty(var1[30].callGetProperty(var1[31].callGroovyObjectGetProperty(this)), $get$$class$groovy$ui$ConsoleView(), var1[32].callGroovyObjectGetProperty(this), "inputArea");
      ScriptBytecodeAdapter.setProperty(var1[33].callGroovyObjectGetProperty(this), $get$$class$groovy$ui$ConsoleView(), var1[34].callGroovyObjectGetProperty(this), "outputArea");
      ScriptBytecodeAdapter.setProperty(var1[35].callGroovyObjectGetProperty(this), $get$$class$groovy$ui$ConsoleView(), var1[36].callGroovyObjectGetProperty(this), "outputWindow");
      ScriptBytecodeAdapter.setProperty(var1[37].callGroovyObjectGetProperty(this), $get$$class$groovy$ui$ConsoleView(), var1[38].callGroovyObjectGetProperty(this), "statusLabel");
      ScriptBytecodeAdapter.setProperty(var1[39].callGroovyObjectGetProperty(this), $get$$class$groovy$ui$ConsoleView(), var1[40].callGroovyObjectGetProperty(this), "frame");
      ScriptBytecodeAdapter.setProperty(var1[41].callGroovyObjectGetProperty(this), $get$$class$groovy$ui$ConsoleView(), var1[42].callGroovyObjectGetProperty(this), "rowNumAndColNum");
      ScriptBytecodeAdapter.setProperty(var1[43].callGroovyObjectGetProperty(this), $get$$class$groovy$ui$ConsoleView(), var1[44].callGroovyObjectGetProperty(this), "toolbar");
      ScriptBytecodeAdapter.setProperty(var1[45].callGroovyObjectGetProperty(this), $get$$class$groovy$ui$ConsoleView(), var1[46].callGroovyObjectGetProperty(this), "saveAction");
      ScriptBytecodeAdapter.setProperty(var1[47].callGroovyObjectGetProperty(this), $get$$class$groovy$ui$ConsoleView(), var1[48].callGroovyObjectGetProperty(this), "prevHistoryAction");
      ScriptBytecodeAdapter.setProperty(var1[49].callGroovyObjectGetProperty(this), $get$$class$groovy$ui$ConsoleView(), var1[50].callGroovyObjectGetProperty(this), "nextHistoryAction");
      ScriptBytecodeAdapter.setProperty(var1[51].callGroovyObjectGetProperty(this), $get$$class$groovy$ui$ConsoleView(), var1[52].callGroovyObjectGetProperty(this), "fullStackTracesAction");
      ScriptBytecodeAdapter.setProperty(var1[53].callGroovyObjectGetProperty(this), $get$$class$groovy$ui$ConsoleView(), var1[54].callGroovyObjectGetProperty(this), "showToolbarAction");
      ScriptBytecodeAdapter.setProperty(var1[55].callGroovyObjectGetProperty(this), $get$$class$groovy$ui$ConsoleView(), var1[56].callGroovyObjectGetProperty(this), "detachedOutputAction");
      ScriptBytecodeAdapter.setProperty(var1[57].callGroovyObjectGetProperty(this), $get$$class$groovy$ui$ConsoleView(), var1[58].callGroovyObjectGetProperty(this), "autoClearOutputAction");
      ScriptBytecodeAdapter.setProperty(var1[59].callGroovyObjectGetProperty(this), $get$$class$groovy$ui$ConsoleView(), var1[60].callGroovyObjectGetProperty(this), "showOutputWindowAction");
      ScriptBytecodeAdapter.setProperty(var1[61].callGroovyObjectGetProperty(this), $get$$class$groovy$ui$ConsoleView(), var1[62].callGroovyObjectGetProperty(this), "hideOutputWindowAction1");
      ScriptBytecodeAdapter.setProperty(var1[63].callGroovyObjectGetProperty(this), $get$$class$groovy$ui$ConsoleView(), var1[64].callGroovyObjectGetProperty(this), "hideOutputWindowAction2");
      ScriptBytecodeAdapter.setProperty(var1[65].callGroovyObjectGetProperty(this), $get$$class$groovy$ui$ConsoleView(), var1[66].callGroovyObjectGetProperty(this), "hideOutputWindowAction3");
      ScriptBytecodeAdapter.setProperty(var1[67].callGroovyObjectGetProperty(this), $get$$class$groovy$ui$ConsoleView(), var1[68].callGroovyObjectGetProperty(this), "hideOutputWindowAction4");
      ScriptBytecodeAdapter.setProperty(var1[69].callGroovyObjectGetProperty(this), $get$$class$groovy$ui$ConsoleView(), var1[70].callGroovyObjectGetProperty(this), "interruptAction");
      ScriptBytecodeAdapter.setProperty(var1[71].callGroovyObjectGetProperty(this), $get$$class$groovy$ui$ConsoleView(), var1[72].callGroovyObjectGetProperty(this), "origDividerSize");
      ScriptBytecodeAdapter.setProperty(var1[73].callGroovyObjectGetProperty(this), $get$$class$groovy$ui$ConsoleView(), var1[74].callGroovyObjectGetProperty(this), "splitPane");
      ScriptBytecodeAdapter.setProperty(var1[75].callGroovyObjectGetProperty(this), $get$$class$groovy$ui$ConsoleView(), var1[76].callGroovyObjectGetProperty(this), "blank");
      ScriptBytecodeAdapter.setProperty(var1[77].callGroovyObjectGetProperty(this), $get$$class$groovy$ui$ConsoleView(), var1[78].callGroovyObjectGetProperty(this), "scrollArea");
      var1[79].call(var1[80].callGetProperty(var1[81].callGroovyObjectGetProperty(this)), var1[82].callGroovyObjectGetProperty(this));
      var1[83].call(var1[84].callGetProperty(var1[85].callGroovyObjectGetProperty(this)), var1[86].callGroovyObjectGetProperty(this));
      var1[87].call(var1[88].callGetProperty(var1[89].callGroovyObjectGetProperty(this)), var1[90].callGroovyObjectGetProperty(this));
      var1[91].call(var1[92].callGetProperty(var1[93].callGroovyObjectGetProperty(this)), var1[94].callGroovyObjectGetProperty(this));
      var1[95].call(var1[96].callGetProperty(var1[97].callGroovyObjectGetProperty(this)), var1[98].callGroovyObjectGetProperty(this));
      var1[99].call(var1[100].callGetProperty(var1[101].callGroovyObjectGetProperty(this)), var1[102].callGroovyObjectGetProperty(this));
      var1[103].call(var1[104].callGetProperty(var1[105].callGetProperty(var1[106].callGroovyObjectGetProperty(this))), (Object)ScriptBytecodeAdapter.createPojoWrapper((DocumentListener)ScriptBytecodeAdapter.asType(new GeneratedClosure(this, this) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$groovy$ui$ConsoleView$_run_closure2;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(Object it) {
            CallSite[] var2 = $getCallSiteArray();
            return var2[0].call(var2[1].callGroovyObjectGetProperty(this), (Object)Boolean.TRUE);
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[2].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$ui$ConsoleView$_run_closure2()) {
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
            var0[0] = "setDirty";
            var0[1] = "controller";
            var0[2] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[3];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$ui$ConsoleView$_run_closure2(), var0);
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
         private static Class $get$$class$java$lang$Object() {
            Class var10000 = $class$java$lang$Object;
            if (var10000 == null) {
               var10000 = $class$java$lang$Object = class$("java.lang.Object");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$groovy$ui$ConsoleView$_run_closure2() {
            Class var10000 = $class$groovy$ui$ConsoleView$_run_closure2;
            if (var10000 == null) {
               var10000 = $class$groovy$ui$ConsoleView$_run_closure2 = class$("groovy.ui.ConsoleView$_run_closure2");
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
      }, $get$$class$javax$swing$event$DocumentListener()), $get$$class$javax$swing$event$DocumentListener()));
      ScriptBytecodeAdapter.setProperty(var1[107].callGetProperty(var1[108].callGetProperty(var1[109].callGroovyObjectGetProperty(this))), $get$$class$groovy$ui$ConsoleView(), var1[110].callGroovyObjectGetProperty(this), "rootElement");
      Object dtListener = new Reference((DropTargetListener)ScriptBytecodeAdapter.asType(ScriptBytecodeAdapter.createMap(new Object[]{"dragEnter", new GeneratedClosure(this, this) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$groovy$ui$ConsoleView$_run_closure3;
         // $FF: synthetic field
         private static Class $class$java$awt$datatransfer$DataFlavor;
         // $FF: synthetic field
         private static Class $class$java$awt$dnd$DnDConstants;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(DropTargetDragEvent evt) {
            DropTargetDragEvent evtx = new Reference(evt);
            CallSite[] var3 = $getCallSiteArray();
            return DefaultTypeTransformation.booleanUnbox(var3[0].call(var3[1].callGetProperty(evtx.get()), var3[2].callGetProperty($get$$class$java$awt$datatransfer$DataFlavor()))) ? var3[3].call(evtx.get(), var3[4].callGetProperty($get$$class$java$awt$dnd$DnDConstants())) : var3[5].call(evtx.get());
         }

         public Object call(DropTargetDragEvent evt) {
            DropTargetDragEvent evtx = new Reference(evt);
            CallSite[] var3 = $getCallSiteArray();
            return var3[6].callCurrent(this, (Object)evtx.get());
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$ui$ConsoleView$_run_closure3()) {
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
            var0[0] = "isDataFlavorSupported";
            var0[1] = "dropTargetContext";
            var0[2] = "javaFileListFlavor";
            var0[3] = "acceptDrag";
            var0[4] = "ACTION_COPY";
            var0[5] = "rejectDrag";
            var0[6] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[7];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$ui$ConsoleView$_run_closure3(), var0);
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
         private static Class $get$$class$groovy$ui$ConsoleView$_run_closure3() {
            Class var10000 = $class$groovy$ui$ConsoleView$_run_closure3;
            if (var10000 == null) {
               var10000 = $class$groovy$ui$ConsoleView$_run_closure3 = class$("groovy.ui.ConsoleView$_run_closure3");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$java$awt$datatransfer$DataFlavor() {
            Class var10000 = $class$java$awt$datatransfer$DataFlavor;
            if (var10000 == null) {
               var10000 = $class$java$awt$datatransfer$DataFlavor = class$("java.awt.datatransfer.DataFlavor");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$java$awt$dnd$DnDConstants() {
            Class var10000 = $class$java$awt$dnd$DnDConstants;
            if (var10000 == null) {
               var10000 = $class$java$awt$dnd$DnDConstants = class$("java.awt.dnd.DnDConstants");
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
      }, "dragOver", new GeneratedClosure(this, this) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$groovy$ui$ConsoleView$_run_closure4;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(DropTargetDragEvent evt) {
            CallSite[] var2 = $getCallSiteArray();
            return null;
         }

         public Object call(DropTargetDragEvent evt) {
            CallSite[] var2 = $getCallSiteArray();
            return var2[0].callCurrent(this, (Object)evt);
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$ui$ConsoleView$_run_closure4()) {
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
            return new CallSiteArray($get$$class$groovy$ui$ConsoleView$_run_closure4(), var0);
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
         private static Class $get$$class$groovy$ui$ConsoleView$_run_closure4() {
            Class var10000 = $class$groovy$ui$ConsoleView$_run_closure4;
            if (var10000 == null) {
               var10000 = $class$groovy$ui$ConsoleView$_run_closure4 = class$("groovy.ui.ConsoleView$_run_closure4");
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
      }, "dropActionChanged", new GeneratedClosure(this, this) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$groovy$ui$ConsoleView$_run_closure5;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(DropTargetDragEvent evt) {
            CallSite[] var2 = $getCallSiteArray();
            return null;
         }

         public Object call(DropTargetDragEvent evt) {
            CallSite[] var2 = $getCallSiteArray();
            return var2[0].callCurrent(this, (Object)evt);
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$ui$ConsoleView$_run_closure5()) {
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
            return new CallSiteArray($get$$class$groovy$ui$ConsoleView$_run_closure5(), var0);
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
         private static Class $get$$class$groovy$ui$ConsoleView$_run_closure5() {
            Class var10000 = $class$groovy$ui$ConsoleView$_run_closure5;
            if (var10000 == null) {
               var10000 = $class$groovy$ui$ConsoleView$_run_closure5 = class$("groovy.ui.ConsoleView$_run_closure5");
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
      }, "dragExit", new GeneratedClosure(this, this) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$groovy$ui$ConsoleView$_run_closure6;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(DropTargetEvent evt) {
            CallSite[] var2 = $getCallSiteArray();
            return null;
         }

         public Object call(DropTargetEvent evt) {
            CallSite[] var2 = $getCallSiteArray();
            return var2[0].callCurrent(this, (Object)evt);
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$ui$ConsoleView$_run_closure6()) {
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
            return new CallSiteArray($get$$class$groovy$ui$ConsoleView$_run_closure6(), var0);
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
         private static Class $get$$class$groovy$ui$ConsoleView$_run_closure6() {
            Class var10000 = $class$groovy$ui$ConsoleView$_run_closure6;
            if (var10000 == null) {
               var10000 = $class$groovy$ui$ConsoleView$_run_closure6 = class$("groovy.ui.ConsoleView$_run_closure6");
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
      }, "drop", new GeneratedClosure(this, this) {
         // $FF: synthetic field
         private static final Integer $const$0 = (Integer)0;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$groovy$ui$ConsoleView$_run_closure7;
         // $FF: synthetic field
         private static Class $class$java$awt$datatransfer$DataFlavor;
         // $FF: synthetic field
         private static Class $class$java$awt$dnd$DnDConstants;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(DropTargetDropEvent evt) {
            DropTargetDropEvent evtx = new Reference(evt);
            CallSite[] var3 = $getCallSiteArray();
            var3[0].call(evtx.get(), var3[1].callGetProperty($get$$class$java$awt$dnd$DnDConstants()));
            return DefaultTypeTransformation.booleanUnbox(var3[2].call(var3[3].callGroovyObjectGetProperty(this))) ? var3[4].call(var3[5].callGroovyObjectGetProperty(this), var3[6].call(var3[7].call(var3[8].callGetProperty(evtx.get()), var3[9].callGetProperty($get$$class$java$awt$datatransfer$DataFlavor())), (Object)$const$0)) : null;
         }

         public Object call(DropTargetDropEvent evt) {
            DropTargetDropEvent evtx = new Reference(evt);
            CallSite[] var3 = $getCallSiteArray();
            return var3[10].callCurrent(this, (Object)evtx.get());
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$ui$ConsoleView$_run_closure7()) {
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
            var0[0] = "acceptDrop";
            var0[1] = "ACTION_COPY";
            var0[2] = "askToSaveFile";
            var0[3] = "controller";
            var0[4] = "loadScriptFile";
            var0[5] = "controller";
            var0[6] = "getAt";
            var0[7] = "getTransferData";
            var0[8] = "transferable";
            var0[9] = "javaFileListFlavor";
            var0[10] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[11];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$ui$ConsoleView$_run_closure7(), var0);
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
         private static Class $get$$class$groovy$ui$ConsoleView$_run_closure7() {
            Class var10000 = $class$groovy$ui$ConsoleView$_run_closure7;
            if (var10000 == null) {
               var10000 = $class$groovy$ui$ConsoleView$_run_closure7 = class$("groovy.ui.ConsoleView$_run_closure7");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$java$awt$datatransfer$DataFlavor() {
            Class var10000 = $class$java$awt$datatransfer$DataFlavor;
            if (var10000 == null) {
               var10000 = $class$java$awt$datatransfer$DataFlavor = class$("java.awt.datatransfer.DataFlavor");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$java$awt$dnd$DnDConstants() {
            Class var10000 = $class$java$awt$dnd$DnDConstants;
            if (var10000 == null) {
               var10000 = $class$java$awt$dnd$DnDConstants = class$("java.awt.dnd.DnDConstants");
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
      }}), $get$$class$java$awt$dnd$DropTargetListener()));
      var1[111].call(ScriptBytecodeAdapter.createList(new Object[]{var1[112].callGroovyObjectGetProperty(this), var1[113].callGroovyObjectGetProperty(this), var1[114].callGroovyObjectGetProperty(this)}), (Object)(new GeneratedClosure(this, this, dtListener) {
         private Reference<T> dtListener;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$groovy$ui$ConsoleView$_run_closure8;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$java$awt$dnd$DropTarget;
         // $FF: synthetic field
         private static Class $class$java$awt$dnd$DnDConstants;

         public {
            CallSite[] var4 = $getCallSiteArray();
            this.dtListener = (Reference)dtListener;
         }

         public Object doCall(Object it) {
            Object itx = new Reference(it);
            CallSite[] var3 = $getCallSiteArray();
            return var3[0].callConstructor($get$$class$java$awt$dnd$DropTarget(), itx.get(), var3[1].callGetProperty($get$$class$java$awt$dnd$DnDConstants()), this.dtListener.get());
         }

         public Object getDtListener() {
            CallSite[] var1 = $getCallSiteArray();
            return this.dtListener.get();
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[2].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$ui$ConsoleView$_run_closure8()) {
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
            var0[0] = "<$constructor$>";
            var0[1] = "ACTION_COPY";
            var0[2] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[3];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$ui$ConsoleView$_run_closure8(), var0);
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
         private static Class $get$$class$groovy$ui$ConsoleView$_run_closure8() {
            Class var10000 = $class$groovy$ui$ConsoleView$_run_closure8;
            if (var10000 == null) {
               var10000 = $class$groovy$ui$ConsoleView$_run_closure8 = class$("groovy.ui.ConsoleView$_run_closure8");
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
         private static Class $get$$class$java$awt$dnd$DropTarget() {
            Class var10000 = $class$java$awt$dnd$DropTarget;
            if (var10000 == null) {
               var10000 = $class$java$awt$dnd$DropTarget = class$("java.awt.dnd.DropTarget");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$java$awt$dnd$DnDConstants() {
            Class var10000 = $class$java$awt$dnd$DnDConstants;
            if (var10000 == null) {
               var10000 = $class$java$awt$dnd$DnDConstants = class$("java.awt.dnd.DnDConstants");
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
      return null;
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$groovy$ui$ConsoleView()) {
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
      Class var10000 = $get$$class$groovy$ui$ConsoleView();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$4(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$groovy$ui$ConsoleView(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$4(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$groovy$ui$ConsoleView(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object super$3$getProperty(String var1) {
      return super.getProperty(var1);
   }

   // $FF: synthetic method
   public String super$1$toString() {
      return super.toString();
   }

   // $FF: synthetic method
   public void super$3$setProperty(String var1, Object var2) {
      super.setProperty(var1, var2);
   }

   // $FF: synthetic method
   public void super$1$notify() {
      super.notify();
   }

   // $FF: synthetic method
   public void super$3$println() {
      super.println();
   }

   // $FF: synthetic method
   public void super$1$notifyAll() {
      super.notifyAll();
   }

   // $FF: synthetic method
   public void super$3$print(Object var1) {
      super.print(var1);
   }

   // $FF: synthetic method
   public void super$3$printf(String var1, Object[] var2) {
      super.printf(var1, var2);
   }

   // $FF: synthetic method
   public Object super$1$clone() {
      return super.clone();
   }

   // $FF: synthetic method
   public Object super$3$evaluate(String var1) {
      return super.evaluate(var1);
   }

   // $FF: synthetic method
   public void super$1$wait() {
      super.wait();
   }

   // $FF: synthetic method
   public MetaClass super$2$getMetaClass() {
      return super.getMetaClass();
   }

   // $FF: synthetic method
   public void super$1$wait(long var1, int var3) {
      super.wait(var1, var3);
   }

   // $FF: synthetic method
   public void super$2$setMetaClass(MetaClass var1) {
      super.setMetaClass(var1);
   }

   // $FF: synthetic method
   public Class super$1$getClass() {
      return super.getClass();
   }

   // $FF: synthetic method
   public Binding super$3$getBinding() {
      return super.getBinding();
   }

   // $FF: synthetic method
   public void super$1$finalize() {
      super.finalize();
   }

   // $FF: synthetic method
   public void super$3$printf(String var1, Object var2) {
      super.printf(var1, var2);
   }

   // $FF: synthetic method
   public void super$3$setBinding(Binding var1) {
      super.setBinding(var1);
   }

   // $FF: synthetic method
   public void super$1$wait(long var1) {
      super.wait(var1);
   }

   // $FF: synthetic method
   public void super$3$run(File var1, String[] var2) {
      super.run(var1, var2);
   }

   // $FF: synthetic method
   public Object super$3$evaluate(File var1) {
      return super.evaluate(var1);
   }

   // $FF: synthetic method
   public void super$3$println(Object var1) {
      super.println(var1);
   }

   // $FF: synthetic method
   public boolean super$1$equals(Object var1) {
      return super.equals(var1);
   }

   // $FF: synthetic method
   public Object super$3$invokeMethod(String var1, Object var2) {
      return super.invokeMethod(var1, var2);
   }

   // $FF: synthetic method
   public int super$1$hashCode() {
      return super.hashCode();
   }

   // $FF: synthetic method
   private static void $createCallSiteArray_1(String[] var0) {
      var0[0] = "runScript";
      var0[1] = "getSystemLookAndFeelClassName";
      var0[2] = "build";
      var0[3] = "build";
      var0[4] = "build";
      var0[5] = "build";
      var0[6] = "rootContainerDelegate";
      var0[7] = "binding";
      var0[8] = "call";
      var0[9] = "getAt";
      var0[10] = "binding";
      var0[11] = "container";
      var0[12] = "consoleFrame";
      var0[13] = "promptStyle";
      var0[14] = "controller";
      var0[15] = "commandStyle";
      var0[16] = "controller";
      var0[17] = "outputStyle";
      var0[18] = "controller";
      var0[19] = "stacktraceStyle";
      var0[20] = "controller";
      var0[21] = "hyperlinkStyle";
      var0[22] = "controller";
      var0[23] = "resultStyle";
      var0[24] = "controller";
      var0[25] = "consoleFrame";
      var0[26] = "controller";
      var0[27] = "consoleFrame";
      var0[28] = "inputEditor";
      var0[29] = "controller";
      var0[30] = "textEditor";
      var0[31] = "inputEditor";
      var0[32] = "controller";
      var0[33] = "outputArea";
      var0[34] = "controller";
      var0[35] = "outputWindow";
      var0[36] = "controller";
      var0[37] = "status";
      var0[38] = "controller";
      var0[39] = "consoleFrame";
      var0[40] = "controller";
      var0[41] = "rowNumAndColNum";
      var0[42] = "controller";
      var0[43] = "toolbar";
      var0[44] = "controller";
      var0[45] = "saveAction";
      var0[46] = "controller";
      var0[47] = "historyPrevAction";
      var0[48] = "controller";
      var0[49] = "historyNextAction";
      var0[50] = "controller";
      var0[51] = "fullStackTracesAction";
      var0[52] = "controller";
      var0[53] = "showToolbarAction";
      var0[54] = "controller";
      var0[55] = "detachedOutputAction";
      var0[56] = "controller";
      var0[57] = "autoClearOutputAction";
      var0[58] = "controller";
      var0[59] = "showOutputWindowAction";
      var0[60] = "controller";
      var0[61] = "hideOutputWindowAction1";
      var0[62] = "controller";
      var0[63] = "hideOutputWindowAction2";
      var0[64] = "controller";
      var0[65] = "hideOutputWindowAction3";
      var0[66] = "controller";
      var0[67] = "hideOutputWindowAction4";
      var0[68] = "controller";
      var0[69] = "interruptAction";
      var0[70] = "controller";
      var0[71] = "origDividerSize";
      var0[72] = "controller";
      var0[73] = "splitPane";
      var0[74] = "controller";
      var0[75] = "blank";
      var0[76] = "controller";
      var0[77] = "scrollArea";
      var0[78] = "controller";
      var0[79] = "addComponentListener";
      var0[80] = "outputArea";
      var0[81] = "controller";
      var0[82] = "controller";
      var0[83] = "addComponentListener";
      var0[84] = "inputArea";
      var0[85] = "controller";
      var0[86] = "controller";
      var0[87] = "addHyperlinkListener";
      var0[88] = "outputArea";
      var0[89] = "controller";
      var0[90] = "controller";
      var0[91] = "addHyperlinkListener";
      var0[92] = "outputArea";
      var0[93] = "controller";
      var0[94] = "controller";
      var0[95] = "addFocusListener";
      var0[96] = "outputArea";
      var0[97] = "controller";
      var0[98] = "controller";
      var0[99] = "addCaretListener";
      var0[100] = "inputArea";
      var0[101] = "controller";
      var0[102] = "controller";
      var0[103] = "addDocumentListener";
      var0[104] = "document";
      var0[105] = "inputArea";
      var0[106] = "controller";
      var0[107] = "defaultRootElement";
      var0[108] = "document";
      var0[109] = "inputArea";
      var0[110] = "controller";
      var0[111] = "each";
      var0[112] = "consoleFrame";
      var0[113] = "inputArea";
      var0[114] = "outputArea";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[115];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$groovy$ui$ConsoleView(), var0);
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
   private static Class $get$$class$groovy$ui$view$GTKDefaults() {
      Class var10000 = $class$groovy$ui$view$GTKDefaults;
      if (var10000 == null) {
         var10000 = $class$groovy$ui$view$GTKDefaults = class$("groovy.ui.view.GTKDefaults");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$lang$Script() {
      Class var10000 = $class$groovy$lang$Script;
      if (var10000 == null) {
         var10000 = $class$groovy$lang$Script = class$("groovy.lang.Script");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$ui$ConsoleView() {
      Class var10000 = $class$groovy$ui$ConsoleView;
      if (var10000 == null) {
         var10000 = $class$groovy$ui$ConsoleView = class$("groovy.ui.ConsoleView");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$javax$swing$UIManager() {
      Class var10000 = $class$javax$swing$UIManager;
      if (var10000 == null) {
         var10000 = $class$javax$swing$UIManager = class$("javax.swing.UIManager");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$runtime$InvokerHelper() {
      Class var10000 = $class$org$codehaus$groovy$runtime$InvokerHelper;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$runtime$InvokerHelper = class$("org.codehaus.groovy.runtime.InvokerHelper");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$awt$dnd$DropTargetListener() {
      Class var10000 = $class$java$awt$dnd$DropTargetListener;
      if (var10000 == null) {
         var10000 = $class$java$awt$dnd$DropTargetListener = class$("java.awt.dnd.DropTargetListener");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$javax$swing$event$DocumentListener() {
      Class var10000 = $class$javax$swing$event$DocumentListener;
      if (var10000 == null) {
         var10000 = $class$javax$swing$event$DocumentListener = class$("javax.swing.event.DocumentListener");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$ui$view$Defaults() {
      Class var10000 = $class$groovy$ui$view$Defaults;
      if (var10000 == null) {
         var10000 = $class$groovy$ui$view$Defaults = class$("groovy.ui.view.Defaults");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$ui$view$WindowsDefaults() {
      Class var10000 = $class$groovy$ui$view$WindowsDefaults;
      if (var10000 == null) {
         var10000 = $class$groovy$ui$view$WindowsDefaults = class$("groovy.ui.view.WindowsDefaults");
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
   private static Class $get$$class$groovy$ui$view$MacOSXDefaults() {
      Class var10000 = $class$groovy$ui$view$MacOSXDefaults;
      if (var10000 == null) {
         var10000 = $class$groovy$ui$view$MacOSXDefaults = class$("groovy.ui.view.MacOSXDefaults");
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
