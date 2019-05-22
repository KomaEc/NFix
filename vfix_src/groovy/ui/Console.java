package groovy.ui;

import groovy.grape.GrapeIvy;
import groovy.lang.Binding;
import groovy.lang.Closure;
import groovy.lang.GroovyObject;
import groovy.lang.GroovyShell;
import groovy.lang.MetaClass;
import groovy.lang.Reference;
import groovy.swing.SwingBuilder;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.io.StringWriter;
import java.lang.ref.SoftReference;
import java.math.BigDecimal;
import java.util.EventObject;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JApplet;
import javax.swing.JLabel;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.swing.RootPaneContainer;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.Element;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import org.apache.ivy.core.event.IvyListener;
import org.codehaus.groovy.control.ErrorCollector;
import org.codehaus.groovy.control.MultipleCompilationErrorsException;
import org.codehaus.groovy.control.messages.ExceptionMessage;
import org.codehaus.groovy.control.messages.SyntaxErrorMessage;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.ArrayUtil;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.GeneratedClosure;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;
import org.codehaus.groovy.syntax.SyntaxException;

public class Console implements CaretListener, HyperlinkListener, ComponentListener, FocusListener, GroovyObject {
   private static final String DEFAULT_SCRIPT_NAME_START = (String)"ConsoleScript";
   private static Object prefs = $getCallSiteArray()[502].call($get$$class$java$util$prefs$Preferences(), (Object)$get$$class$groovy$ui$Console());
   private static boolean captureStdOut;
   private static boolean captureStdErr;
   private static Object consoleControllers;
   private boolean fullStackTraces;
   private Action fullStackTracesAction;
   private boolean showScriptInOutput;
   private Action showScriptInOutputAction;
   private boolean visualizeScriptResults;
   private Action visualizeScriptResultsAction;
   private boolean showToolbar;
   private Component toolbar;
   private Action showToolbarAction;
   private boolean detachedOutput;
   private Action detachedOutputAction;
   private Action showOutputWindowAction;
   private Action hideOutputWindowAction1;
   private Action hideOutputWindowAction2;
   private Action hideOutputWindowAction3;
   private Action hideOutputWindowAction4;
   private int origDividerSize;
   private Component outputWindow;
   private Component copyFromComponent;
   private Component blank;
   private Component scrollArea;
   private boolean autoClearOutput;
   private Action autoClearOutputAction;
   private int maxHistory;
   private int maxOutputChars;
   private SwingBuilder swing;
   private RootPaneContainer frame;
   private ConsoleTextEditor inputEditor;
   private JSplitPane splitPane;
   private JTextPane inputArea;
   private JTextPane outputArea;
   private JLabel statusLabel;
   private JLabel rowNumAndColNum;
   private Element rootElement;
   private int cursorPos;
   private int rowNum;
   private int colNum;
   private Style promptStyle;
   private Style commandStyle;
   private Style outputStyle;
   private Style stacktraceStyle;
   private Style hyperlinkStyle;
   private Style resultStyle;
   private List history;
   private int historyIndex;
   private HistoryRecord pendingRecord;
   private Action prevHistoryAction;
   private Action nextHistoryAction;
   private boolean dirty;
   private Action saveAction;
   private int textSelectionStart;
   private int textSelectionEnd;
   private Object scriptFile;
   private File currentFileChooserDir;
   private File currentClasspathJarDir;
   private File currentClasspathDir;
   private GroovyShell shell;
   private int scriptNameCounter;
   private SystemOutputInterceptor systemOutInterceptor;
   private SystemOutputInterceptor systemErrorInterceptor;
   private Thread runThread;
   private Closure beforeExecution;
   private Closure afterExecution;
   public static String ICON_PATH = (String)"/groovy/ui/ConsoleIcon.png";
   public static String NODE_ICON_PATH = (String)"/groovy/ui/icons/bullet_green.png";
   private static Object groovyFileFilter;
   private boolean scriptRunning;
   private boolean stackOverFlowError;
   private Action interruptAction;
   private static Object frameConsoleDelegates;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private transient MetaClass metaClass;
   // $FF: synthetic field
   private static final Integer $const$0 = (Integer)10;
   // $FF: synthetic field
   private static final Integer $const$1 = (Integer)1;
   // $FF: synthetic field
   private static final Integer $const$2 = (Integer)0;
   // $FF: synthetic field
   private static final Integer $const$3 = (Integer)2;
   // $FF: synthetic field
   private static final BigDecimal $const$4 = (BigDecimal)(new BigDecimal("1.0"));
   // $FF: synthetic field
   private static final BigDecimal $const$5 = (BigDecimal)(new BigDecimal("0.5"));
   // $FF: synthetic field
   private static final Integer $const$6 = (Integer)-1;
   // $FF: synthetic field
   private static final Integer $const$7 = (Integer)120;
   // $FF: synthetic field
   private static final Integer $const$8 = (Integer)60;
   // $FF: synthetic field
   private static final Integer $const$9 = (Integer)40;
   // $FF: synthetic field
   private static final Integer $const$10 = (Integer)4;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524203161L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524203161 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$org$apache$ivy$core$event$IvyListener;
   // $FF: synthetic field
   private static Class $class$java$awt$Dimension;
   // $FF: synthetic field
   private static Class $class$java$awt$Font;
   // $FF: synthetic field
   private static Class $class$groovy$ui$ConsoleView;
   // $FF: synthetic field
   private static Class $class$javax$swing$JSplitPane;
   // $FF: synthetic field
   private static Class $class$groovy$inspect$swingui$ObjectBrowser;
   // $FF: synthetic field
   private static Class $class$java$lang$Math;
   // $FF: synthetic field
   private static Class $class$groovy$ui$Console;
   // $FF: synthetic field
   private static Class $class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$java$util$Set;
   // $FF: synthetic field
   private static Class $class$javax$swing$UIManager;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$control$MultipleCompilationErrorsException;
   // $FF: synthetic field
   private static Class $class$java$lang$Thread;
   // $FF: synthetic field
   private static Class $class$java$awt$event$ActionEvent;
   // $FF: synthetic field
   private static Class $class$groovy$lang$Binding;
   // $FF: synthetic field
   private static Class $class$javax$swing$text$SimpleAttributeSet;
   // $FF: synthetic field
   private static Class $class$java$util$prefs$Preferences;
   // $FF: synthetic field
   private static Class $class$javax$swing$SwingUtilities;
   // $FF: synthetic field
   private static Class $class$groovy$ui$SystemOutputInterceptor;
   // $FF: synthetic field
   private static Class $class$java$awt$Toolkit;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$runtime$StackTraceUtils;
   // $FF: synthetic field
   private static Class $class$java$awt$Component;
   // $FF: synthetic field
   private static Class $class$java$lang$Integer;
   // $FF: synthetic field
   private static Class $class$groovy$ui$text$FindReplaceUtility;
   // $FF: synthetic field
   private static Class $class$java$util$HashMap;
   // $FF: synthetic field
   private static Class $class$java$awt$EventQueue;
   // $FF: synthetic field
   private static Class $class$groovy$inspect$swingui$AstBrowser;
   // $FF: synthetic field
   private static Class $class$java$util$EventObject;
   // $FF: synthetic field
   private static Class $class$java$io$StringWriter;
   // $FF: synthetic field
   private static Class $class$groovy$ui$OutputTransforms;
   // $FF: synthetic field
   private static Class $class$groovy$ui$ConsoleActions;
   // $FF: synthetic field
   private static Class $class$java$util$List;
   // $FF: synthetic field
   private static Class $class$java$io$File;
   // $FF: synthetic field
   private static Class $class$groovy$ui$GroovyFileFilter;
   // $FF: synthetic field
   private static Class $class$javax$swing$text$StyleConstants;
   // $FF: synthetic field
   private static Class $class$groovy$grape$GrapeIvy;
   // $FF: synthetic field
   private static Class $class$javax$swing$JFileChooser;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$runtime$InvokerHelper;
   // $FF: synthetic field
   private static Class $class$groovy$ui$HistoryRecord;
   // $FF: synthetic field
   private static Class $class$java$awt$BorderLayout;
   // $FF: synthetic field
   private static Class $class$java$util$logging$Logger;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$control$ErrorCollector;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClass;
   // $FF: synthetic field
   private static Class $class$groovy$grape$Grape;
   // $FF: synthetic field
   private static Class $class$javax$swing$event$HyperlinkEvent$EventType;
   // $FF: synthetic field
   private static Class $class$java$lang$Object;
   // $FF: synthetic field
   private static Class $class$java$lang$Boolean;
   // $FF: synthetic field
   private static Class $class$java$lang$System;
   // $FF: synthetic field
   private static Class $class$javax$swing$JOptionPane;
   // $FF: synthetic field
   private static Class $class$groovy$swing$SwingBuilder;
   // $FF: synthetic field
   private static Class $class$java$io$PrintWriter;
   // $FF: synthetic field
   private static Class $class$groovy$lang$GroovyShell;

   public Console() {
      CallSite[] var1 = $getCallSiteArray();
      Object[] var10000 = new Object[]{var1[0].callConstructor($get$$class$groovy$lang$Binding())};
      int var10002 = ScriptBytecodeAdapter.selectConstructorAndTransformArguments(var10000, 4, $get$$class$groovy$ui$Console());
      Object[] var10001 = var10000;
      if ((var10002 & 1) != 0) {
         var10001 = (Object[])var10000[0];
      }

      switch(var10002 >> 8) {
      case 0:
         this();
         break;
      case 1:
         this((Binding)var10001[0]);
         break;
      case 2:
         this((ClassLoader)var10001[0]);
         break;
      case 3:
         this((ClassLoader)var10001[0], (Binding)var10001[1]);
         break;
      default:
         throw new IllegalArgumentException("illegal constructor number");
      }

   }

   public Console(Binding binding) {
      CallSite[] var2 = $getCallSiteArray();
      Object[] var10000 = new Object[]{null, binding};
      int var10002 = ScriptBytecodeAdapter.selectConstructorAndTransformArguments(var10000, 4, $get$$class$groovy$ui$Console());
      Object[] var10001 = var10000;
      if ((var10002 & 1) != 0) {
         var10001 = (Object[])var10000[0];
      }

      switch(var10002 >> 8) {
      case 0:
         this();
         break;
      case 1:
         this((Binding)var10001[0]);
         break;
      case 2:
         this((ClassLoader)var10001[0]);
         break;
      case 3:
         this((ClassLoader)var10001[0], (Binding)var10001[1]);
         break;
      default:
         throw new IllegalArgumentException("illegal constructor number");
      }

   }

   public Console(ClassLoader parent) {
      CallSite[] var2 = $getCallSiteArray();
      Object[] var10000 = new Object[]{parent, var2[1].callConstructor($get$$class$groovy$lang$Binding())};
      int var10002 = ScriptBytecodeAdapter.selectConstructorAndTransformArguments(var10000, 4, $get$$class$groovy$ui$Console());
      Object[] var10001 = var10000;
      if ((var10002 & 1) != 0) {
         var10001 = (Object[])var10000[0];
      }

      switch(var10002 >> 8) {
      case 0:
         this();
         break;
      case 1:
         this((Binding)var10001[0]);
         break;
      case 2:
         this((ClassLoader)var10001[0]);
         break;
      case 3:
         this((ClassLoader)var10001[0], (Binding)var10001[1]);
         break;
      default:
         throw new IllegalArgumentException("illegal constructor number");
      }

   }

   public Console(ClassLoader parent, Binding binding) {
      CallSite[] var3 = $getCallSiteArray();
      this.fullStackTraces = DefaultTypeTransformation.booleanUnbox(var3[2].call(prefs, "fullStackTraces", var3[3].call($get$$class$java$lang$Boolean(), (Object)var3[4].call($get$$class$java$lang$System(), "groovy.full.stacktrace", "false"))));
      this.showScriptInOutput = DefaultTypeTransformation.booleanUnbox(var3[5].call(prefs, "showScriptInOutput", Boolean.TRUE));
      this.visualizeScriptResults = DefaultTypeTransformation.booleanUnbox(var3[6].call(prefs, "visualizeScriptResults", Boolean.FALSE));
      this.showToolbar = DefaultTypeTransformation.booleanUnbox(var3[7].call(prefs, "showToolbar", Boolean.TRUE));
      this.detachedOutput = DefaultTypeTransformation.booleanUnbox(var3[8].call(prefs, "detachedOutput", Boolean.FALSE));
      this.autoClearOutput = DefaultTypeTransformation.booleanUnbox(var3[9].call(prefs, "autoClearOutput", Boolean.FALSE));
      this.maxHistory = DefaultTypeTransformation.intUnbox($const$0);
      this.maxOutputChars = DefaultTypeTransformation.intUnbox((Integer)ScriptBytecodeAdapter.asType(var3[10].call($get$$class$java$lang$System(), "groovy.console.output.limit", "20000"), $get$$class$java$lang$Integer()));
      this.history = (List)ScriptBytecodeAdapter.castToType(ScriptBytecodeAdapter.createList(new Object[0]), $get$$class$java$util$List());
      this.historyIndex = DefaultTypeTransformation.intUnbox($const$1);
      this.pendingRecord = (HistoryRecord)ScriptBytecodeAdapter.castToType(var3[11].callConstructor($get$$class$groovy$ui$HistoryRecord(), (Object)ScriptBytecodeAdapter.createMap(new Object[]{"allText", "", "selectionStart", $const$2, "selectionEnd", $const$2})), $get$$class$groovy$ui$HistoryRecord());
      this.currentFileChooserDir = (File)ScriptBytecodeAdapter.castToType(var3[12].callConstructor($get$$class$java$io$File(), (Object)var3[13].call(var3[14].call($get$$class$java$util$prefs$Preferences(), (Object)$get$$class$groovy$ui$Console()), "currentFileChooserDir", ".")), $get$$class$java$io$File());
      this.currentClasspathJarDir = (File)ScriptBytecodeAdapter.castToType(var3[15].callConstructor($get$$class$java$io$File(), (Object)var3[16].call(var3[17].call($get$$class$java$util$prefs$Preferences(), (Object)$get$$class$groovy$ui$Console()), "currentClasspathJarDir", ".")), $get$$class$java$io$File());
      this.currentClasspathDir = (File)ScriptBytecodeAdapter.castToType(var3[18].callConstructor($get$$class$java$io$File(), (Object)var3[19].call(var3[20].call($get$$class$java$util$prefs$Preferences(), (Object)$get$$class$groovy$ui$Console()), "currentClasspathDir", ".")), $get$$class$java$io$File());
      this.scriptNameCounter = DefaultTypeTransformation.intUnbox($const$2);
      this.runThread = (Thread)ScriptBytecodeAdapter.castToType((Thread)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$java$lang$Thread()), $get$$class$java$lang$Thread());
      this.scriptRunning = DefaultTypeTransformation.booleanUnbox(Boolean.FALSE);
      this.stackOverFlowError = DefaultTypeTransformation.booleanUnbox(Boolean.FALSE);
      this.metaClass = (MetaClass)ScriptBytecodeAdapter.castToType(this.$getStaticMetaClass(), $get$$class$groovy$lang$MetaClass());
      var3[21].callCurrent(this, parent, binding);

      try {
         var3[22].call($get$$class$java$lang$System(), "groovy.full.stacktrace", var3[23].call($get$$class$java$lang$System(), "groovy.full.stacktrace", var3[24].call($get$$class$java$lang$Boolean(), (Object)var3[25].call(prefs, "fullStackTraces", Boolean.FALSE))));
      } catch (SecurityException var9) {
         ScriptBytecodeAdapter.setProperty(Boolean.FALSE, $get$$class$groovy$ui$Console(), this.fullStackTracesAction, "enabled");
      } finally {
         ;
      }

      consoleControllers = var3[26].call(consoleControllers, (Object)this);
      Reference resolvedDependencies = new Reference((Set)ScriptBytecodeAdapter.castToType(ScriptBytecodeAdapter.createList(new Object[0]), $get$$class$java$util$Set()));
      Reference downloadedArtifacts = new Reference((Set)ScriptBytecodeAdapter.castToType(ScriptBytecodeAdapter.createList(new Object[0]), $get$$class$java$util$Set()));
      var3[27].call(var3[28].callGetProperty(var3[29].callGroovyObjectGetProperty((GrapeIvy)ScriptBytecodeAdapter.castToType(var3[30].callGetProperty($get$$class$groovy$grape$Grape()), $get$$class$groovy$grape$GrapeIvy()))), (Object)ScriptBytecodeAdapter.createPojoWrapper((IvyListener)ScriptBytecodeAdapter.asType(ScriptBytecodeAdapter.createMap(new Object[]{"progress", new Console._closure1(this, this, resolvedDependencies, downloadedArtifacts)}), $get$$class$org$apache$ivy$core$event$IvyListener()), $get$$class$org$apache$ivy$core$event$IvyListener()));
      ScriptBytecodeAdapter.setProperty(var3[31].call($get$$class$groovy$ui$OutputTransforms()), $get$$class$groovy$ui$Console(), var3[32].callGroovyObjectGetProperty(binding), "_outputTransforms");
   }

   public static void main(String... args) {
      CallSite[] var1 = $getCallSiteArray();
      if (DefaultTypeTransformation.booleanUnbox(ScriptBytecodeAdapter.compareEqual(var1[33].callGetProperty(args), $const$1) && ScriptBytecodeAdapter.compareEqual(var1[34].call(args, (Object)$const$2), "--help") ? Boolean.TRUE : Boolean.FALSE)) {
         var1[35].callStatic($get$$class$groovy$ui$Console(), (Object)"usage: groovyConsole [options] [filename]\noptions:\n  --help                               This Help message\n  -cp,-classpath,--classpath <path>    Specify classpath");
      } else {
         ScriptBytecodeAdapter.setProperty(Boolean.TRUE, $get$$class$groovy$ui$Console(), var1[36].call($get$$class$java$util$logging$Logger(), (Object)var1[37].callGetProperty($get$$class$org$codehaus$groovy$runtime$StackTraceUtils())), "useParentHandlers");
         var1[38].call($get$$class$javax$swing$UIManager(), (Object)var1[39].call($get$$class$javax$swing$UIManager()));
         Object console = var1[40].callConstructor($get$$class$groovy$ui$Console(), (Object)var1[41].callSafe(var1[42].callGetProperty($get$$class$groovy$ui$Console())));
         var1[43].call(console);
         if (ScriptBytecodeAdapter.compareEqual(var1[44].callGetProperty(args), $const$1)) {
            var1[45].call(console, (Object)ScriptBytecodeAdapter.createPojoWrapper((File)ScriptBytecodeAdapter.asType(var1[46].call(args, (Object)$const$2), $get$$class$java$io$File()), $get$$class$java$io$File()));
         }

      }
   }

   public void newScript(ClassLoader parent, Binding binding) {
      CallSite[] var3 = $getCallSiteArray();
      this.shell = (GroovyShell)ScriptBytecodeAdapter.castToType(var3[47].callConstructor($get$$class$groovy$lang$GroovyShell(), parent, binding), $get$$class$groovy$lang$GroovyShell());
   }

   public void run() {
      CallSite[] var1 = $getCallSiteArray();
      var1[48].callCurrent(this, (Object)frameConsoleDelegates);
   }

   public void run(JApplet applet) {
      JApplet applet = new Reference(applet);
      CallSite[] var3 = $getCallSiteArray();
      var3[49].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"rootContainerDelegate", new GeneratedClosure(this, this, applet) {
         private Reference<T> applet;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$javax$swing$JApplet;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$javax$swing$SwingUtilities;
         // $FF: synthetic field
         private static Class $class$groovy$ui$Console$_run_closure2;

         public {
            CallSite[] var4 = $getCallSiteArray();
            this.applet = (Reference)applet;
         }

         public Object doCall(Object it) {
            CallSite[] var2 = $getCallSiteArray();
            ScriptBytecodeAdapter.setGroovyObjectProperty(var2[0].call(var2[1].callGroovyObjectGetProperty(this), var2[2].call($get$$class$javax$swing$SwingUtilities(), (Object)var2[3].call(this.applet.get()))), $get$$class$groovy$ui$Console$_run_closure2(), this, "containingWindows");
            return this.applet.get();
         }

         public JApplet getApplet() {
            CallSite[] var1 = $getCallSiteArray();
            return (JApplet)ScriptBytecodeAdapter.castToType(this.applet.get(), $get$$class$javax$swing$JApplet());
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[4].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$ui$Console$_run_closure2()) {
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
            var0[0] = "plus";
            var0[1] = "containingWindows";
            var0[2] = "getRoot";
            var0[3] = "getParent";
            var0[4] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[5];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$ui$Console$_run_closure2(), var0);
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
         private static Class $get$$class$javax$swing$JApplet() {
            Class var10000 = $class$javax$swing$JApplet;
            if (var10000 == null) {
               var10000 = $class$javax$swing$JApplet = class$("javax.swing.JApplet");
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
         private static Class $get$$class$javax$swing$SwingUtilities() {
            Class var10000 = $class$javax$swing$SwingUtilities;
            if (var10000 == null) {
               var10000 = $class$javax$swing$SwingUtilities = class$("javax.swing.SwingUtilities");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$groovy$ui$Console$_run_closure2() {
            Class var10000 = $class$groovy$ui$Console$_run_closure2;
            if (var10000 == null) {
               var10000 = $class$groovy$ui$Console$_run_closure2 = class$("groovy.ui.Console$_run_closure2");
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
      }, "menuBarDelegate", new GeneratedClosure(this, this) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$groovy$ui$Console$_run_closure3;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(Object arg) {
            Object argx = new Reference(arg);
            CallSite[] var3 = $getCallSiteArray();
            Object var10000 = var3[0].callCurrent(this, (Object)argx.get());
            ScriptBytecodeAdapter.setProperty(var10000, $get$$class$groovy$ui$Console$_run_closure3(), var3[1].callGroovyObjectGetProperty(this), "JMenuBar");
            return var10000;
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$ui$Console$_run_closure3()) {
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
            var0[0] = "build";
            var0[1] = "current";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[2];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$ui$Console$_run_closure3(), var0);
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
         private static Class $get$$class$groovy$ui$Console$_run_closure3() {
            Class var10000 = $class$groovy$ui$Console$_run_closure3;
            if (var10000 == null) {
               var10000 = $class$groovy$ui$Console$_run_closure3 = class$("groovy.ui.Console$_run_closure3");
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
      }}));
   }

   public void run(Map defaults) {
      CallSite[] var2 = $getCallSiteArray();
      this.swing = (SwingBuilder)ScriptBytecodeAdapter.castToType(var2[50].callConstructor($get$$class$groovy$swing$SwingBuilder()), $get$$class$groovy$swing$SwingBuilder());
      var2[51].call(defaults, (Object)(new GeneratedClosure(this, this) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$groovy$ui$Console$_run_closure4;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(Object k, Object v) {
            Object kx = new Reference(k);
            Object vx = new Reference(v);
            CallSite[] var5 = $getCallSiteArray();
            CallSite var10000 = var5[0];
            Object var10001 = var5[1].callGroovyObjectGetProperty(this);
            Object var10002 = kx.get();
            Object var6 = vx.get();
            var10000.call(var10001, var10002, var6);
            return var6;
         }

         public Object call(Object k, Object v) {
            Object kx = new Reference(k);
            Object vx = new Reference(v);
            CallSite[] var5 = $getCallSiteArray();
            return var5[2].callCurrent(this, kx.get(), vx.get());
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$ui$Console$_run_closure4()) {
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
            var0[0] = "putAt";
            var0[1] = "swing";
            var0[2] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[3];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$ui$Console$_run_closure4(), var0);
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
         private static Class $get$$class$groovy$ui$Console$_run_closure4() {
            Class var10000 = $class$groovy$ui$Console$_run_closure4;
            if (var10000 == null) {
               var10000 = $class$groovy$ui$Console$_run_closure4 = class$("groovy.ui.Console$_run_closure4");
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
      var2[52].call($get$$class$java$lang$System(), "groovy.sanitized.stacktraces", "org.codehaus.groovy.runtime.\n                org.codehaus.groovy.\n                groovy.lang.\n                gjdk.groovy.lang.\n                sun.\n                java.lang.reflect.\n                java.lang.Thread\n                groovy.ui.Console");
      ScriptBytecodeAdapter.setGroovyObjectProperty(this, $get$$class$groovy$ui$Console(), this.swing, "controller");
      var2[53].call(this.swing, (Object)$get$$class$groovy$ui$ConsoleActions());
      var2[54].call(this.swing, (Object)$get$$class$groovy$ui$ConsoleView());
      var2[55].callCurrent(this);
      var2[56].call(this.swing, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"source", var2[57].callGetProperty(var2[58].callGroovyObjectGetProperty(this.swing)), "sourceProperty", "enabled", "target", var2[59].callGroovyObjectGetProperty(this.swing), "targetProperty", "enabled"}));
      var2[60].call(this.swing, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"source", var2[61].callGetProperty(var2[62].callGroovyObjectGetProperty(this.swing)), "sourceProperty", "enabled", "target", var2[63].callGroovyObjectGetProperty(this.swing), "targetProperty", "enabled"}));
      if (var2[64].callGroovyObjectGetProperty(this.swing) instanceof Window) {
         var2[65].call(var2[66].callGroovyObjectGetProperty(this.swing));
         var2[67].call(var2[68].callGroovyObjectGetProperty(this.swing));
      }

      var2[69].callCurrent(this);
      var2[70].call(this.swing, (Object)ScriptBytecodeAdapter.getMethodPointer(this.inputArea, "requestFocus"));
   }

   public void installInterceptor() {
      CallSite[] var1 = $getCallSiteArray();
      this.systemOutInterceptor = (SystemOutputInterceptor)ScriptBytecodeAdapter.castToType(var1[71].callConstructor($get$$class$groovy$ui$SystemOutputInterceptor(), ScriptBytecodeAdapter.getMethodPointer(this, "notifySystemOut"), Boolean.TRUE), $get$$class$groovy$ui$SystemOutputInterceptor());
      var1[72].call(this.systemOutInterceptor);
      this.systemErrorInterceptor = (SystemOutputInterceptor)ScriptBytecodeAdapter.castToType(var1[73].callConstructor($get$$class$groovy$ui$SystemOutputInterceptor(), ScriptBytecodeAdapter.getMethodPointer(this, "notifySystemErr"), Boolean.FALSE), $get$$class$groovy$ui$SystemOutputInterceptor());
      var1[74].call(this.systemErrorInterceptor);
   }

   public void addToHistory(Object record) {
      CallSite[] var2 = $getCallSiteArray();
      var2[75].call(this.history, (Object)record);
      if (ScriptBytecodeAdapter.compareGreaterThan(var2[76].call(this.history), DefaultTypeTransformation.box(this.maxHistory))) {
         var2[77].call(this.history, (Object)$const$2);
      }

      this.historyIndex = DefaultTypeTransformation.intUnbox(var2[78].call(this.history));
      var2[79].callCurrent(this);
   }

   private Object ensureNoDocLengthOverflow(Object doc) {
      CallSite[] var2 = $getCallSiteArray();
      Integer offset = (Integer)ScriptBytecodeAdapter.castToType(DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.box(this.stackOverFlowError)) ? DefaultTypeTransformation.box(this.maxOutputChars) : $const$2, $get$$class$java$lang$Integer());
      return ScriptBytecodeAdapter.compareGreaterThan(var2[80].callGetProperty(doc), DefaultTypeTransformation.box(this.maxOutputChars)) ? var2[81].call(doc, offset, var2[82].call(var2[83].callGetProperty(doc), DefaultTypeTransformation.box(this.maxOutputChars))) : null;
   }

   public void appendOutput(String text, AttributeSet style) {
      CallSite[] var3 = $getCallSiteArray();
      Object doc = var3[84].callGetProperty(this.outputArea);
      var3[85].call(doc, var3[86].callGetProperty(doc), text, style);
      var3[87].callCurrent(this, (Object)doc);
   }

   public void appendOutput(Window window, AttributeSet style) {
      CallSite[] var3 = $getCallSiteArray();
      var3[88].callCurrent(this, var3[89].call(window), style);
   }

   public void appendOutput(Object object, AttributeSet style) {
      CallSite[] var3 = $getCallSiteArray();
      var3[90].callCurrent(this, var3[91].call(object), style);
   }

   public void appendOutput(Component component, AttributeSet style) {
      CallSite[] var3 = $getCallSiteArray();
      SimpleAttributeSet sas = var3[92].callConstructor($get$$class$javax$swing$text$SimpleAttributeSet());
      var3[93].call(sas, var3[94].callGetProperty($get$$class$javax$swing$text$StyleConstants()), "component");
      var3[95].call($get$$class$javax$swing$text$StyleConstants(), sas, component);
      var3[96].callCurrent(this, var3[97].call(component), sas);
   }

   public void appendOutput(Icon icon, AttributeSet style) {
      CallSite[] var3 = $getCallSiteArray();
      SimpleAttributeSet sas = var3[98].callConstructor($get$$class$javax$swing$text$SimpleAttributeSet());
      var3[99].call(sas, var3[100].callGetProperty($get$$class$javax$swing$text$StyleConstants()), "icon");
      var3[101].call($get$$class$javax$swing$text$StyleConstants(), sas, icon);
      var3[102].callCurrent(this, var3[103].call(icon), sas);
   }

   public void appendStacktrace(Object text) {
      CallSite[] var2 = $getCallSiteArray();
      Object doc = new Reference(var2[104].callGetProperty(this.outputArea));
      Object lines = var2[105].call(text, (Object)"(\\n|\\r|\\r\\n|\u0085|\u2028|\u2029)");
      Object ji = "([\\p{Alnum}_\\$][\\p{Alnum}_\\$]*)";
      Object stacktracePattern = new Reference(new GStringImpl(new Object[]{ji, ji, ji}, new String[]{"\\tat ", "(\\.", ")+\\(((", "(\\.(java|groovy))?):(\\d+))\\)"}));
      var2[106].call(lines, (Object)(new GeneratedClosure(this, this, doc, stacktracePattern) {
         private Reference<T> doc;
         private Reference<T> stacktracePattern;
         // $FF: synthetic field
         private static final Integer $const$0 = (Integer)0;
         // $FF: synthetic field
         private static final Integer $const$1 = (Integer)-5;
         // $FF: synthetic field
         private static final Integer $const$2 = (Integer)-6;
         // $FF: synthetic field
         private static final Integer $const$3 = (Integer)-1;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$java$lang$Integer;
         // $FF: synthetic field
         private static Class $class$groovy$ui$Console$_appendStacktrace_closure5;
         // $FF: synthetic field
         private static Class $class$javax$swing$text$SimpleAttributeSet;
         // $FF: synthetic field
         private static Class $class$javax$swing$text$html$HTML$Tag;
         // $FF: synthetic field
         private static Class $class$javax$swing$text$html$HTML$Attribute;

         public {
            CallSite[] var5 = $getCallSiteArray();
            this.doc = (Reference)doc;
            this.stacktracePattern = (Reference)stacktracePattern;
         }

         public Object doCall(Object line) {
            Object linex = new Reference(line);
            CallSite[] var3 = $getCallSiteArray();
            Integer initialLength = new Reference((Integer)ScriptBytecodeAdapter.castToType(var3[0].callGetProperty(this.doc.get()), $get$$class$java$lang$Integer()));
            Object matcher = new Reference(ScriptBytecodeAdapter.findRegex(linex.get(), this.stacktracePattern.get()));
            Object fileName = DefaultTypeTransformation.booleanUnbox(var3[1].call(matcher.get())) ? var3[2].call(var3[3].call(matcher.get(), (Object)$const$0), (Object)$const$1) : "";
            if (DefaultTypeTransformation.booleanUnbox(!ScriptBytecodeAdapter.compareEqual(fileName, var3[4].callGetPropertySafe(var3[5].callGroovyObjectGetProperty(this))) && !DefaultTypeTransformation.booleanUnbox(var3[6].call(fileName, var3[7].callGroovyObjectGetProperty(this))) ? Boolean.FALSE : Boolean.TRUE)) {
               Object fileNameAndLineNumber = var3[8].call(var3[9].call(matcher.get(), (Object)$const$0), (Object)$const$2);
               Object length = var3[10].call(fileNameAndLineNumber);
               Object index = var3[11].call(linex.get(), fileNameAndLineNumber);
               Object style = var3[12].callGroovyObjectGetProperty(this);
               Object hrefAttr = var3[13].callConstructor($get$$class$javax$swing$text$SimpleAttributeSet());
               var3[14].call(hrefAttr, var3[15].callGetProperty($get$$class$javax$swing$text$html$HTML$Attribute()), var3[16].call("file://", (Object)fileNameAndLineNumber));
               var3[17].call(style, var3[18].callGetProperty($get$$class$javax$swing$text$html$HTML$Tag()), hrefAttr);
               var3[19].call(this.doc.get(), initialLength.get(), var3[20].call(linex.get(), (Object)ScriptBytecodeAdapter.createRange($const$0, index, false)), var3[21].callGroovyObjectGetProperty(this));
               var3[22].call(this.doc.get(), var3[23].call(initialLength.get(), index), var3[24].call(linex.get(), (Object)ScriptBytecodeAdapter.createRange(index, var3[25].call(index, length), false)), style);
               return var3[26].call(this.doc.get(), var3[27].call(var3[28].call(initialLength.get(), index), length), var3[29].call(var3[30].call(linex.get(), (Object)ScriptBytecodeAdapter.createRange(var3[31].call(index, length), $const$3, true)), (Object)"\n"), var3[32].callGroovyObjectGetProperty(this));
            } else {
               return var3[33].call(this.doc.get(), initialLength.get(), var3[34].call(linex.get(), (Object)"\n"), var3[35].callGroovyObjectGetProperty(this));
            }
         }

         public Object getDoc() {
            CallSite[] var1 = $getCallSiteArray();
            return this.doc.get();
         }

         public Object getStacktracePattern() {
            CallSite[] var1 = $getCallSiteArray();
            return this.stacktracePattern.get();
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$ui$Console$_appendStacktrace_closure5()) {
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
            var0[0] = "length";
            var0[1] = "matches";
            var0[2] = "getAt";
            var0[3] = "getAt";
            var0[4] = "name";
            var0[5] = "scriptFile";
            var0[6] = "startsWith";
            var0[7] = "DEFAULT_SCRIPT_NAME_START";
            var0[8] = "getAt";
            var0[9] = "getAt";
            var0[10] = "length";
            var0[11] = "indexOf";
            var0[12] = "hyperlinkStyle";
            var0[13] = "<$constructor$>";
            var0[14] = "addAttribute";
            var0[15] = "HREF";
            var0[16] = "plus";
            var0[17] = "addAttribute";
            var0[18] = "A";
            var0[19] = "insertString";
            var0[20] = "getAt";
            var0[21] = "stacktraceStyle";
            var0[22] = "insertString";
            var0[23] = "plus";
            var0[24] = "getAt";
            var0[25] = "plus";
            var0[26] = "insertString";
            var0[27] = "plus";
            var0[28] = "plus";
            var0[29] = "plus";
            var0[30] = "getAt";
            var0[31] = "plus";
            var0[32] = "stacktraceStyle";
            var0[33] = "insertString";
            var0[34] = "plus";
            var0[35] = "stacktraceStyle";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[36];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$ui$Console$_appendStacktrace_closure5(), var0);
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
         private static Class $get$$class$groovy$ui$Console$_appendStacktrace_closure5() {
            Class var10000 = $class$groovy$ui$Console$_appendStacktrace_closure5;
            if (var10000 == null) {
               var10000 = $class$groovy$ui$Console$_appendStacktrace_closure5 = class$("groovy.ui.Console$_appendStacktrace_closure5");
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
         private static Class $get$$class$javax$swing$text$html$HTML$Tag() {
            Class var10000 = $class$javax$swing$text$html$HTML$Tag;
            if (var10000 == null) {
               var10000 = $class$javax$swing$text$html$HTML$Tag = class$("javax.swing.text.html.HTML$Tag");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$javax$swing$text$html$HTML$Attribute() {
            Class var10000 = $class$javax$swing$text$html$HTML$Attribute;
            if (var10000 == null) {
               var10000 = $class$javax$swing$text$html$HTML$Attribute = class$("javax.swing.text.html.HTML$Attribute");
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
      var2[107].callCurrent(this, (Object)doc.get());
   }

   public void appendOutputNl(Object text, Object style) {
      CallSite[] var3 = $getCallSiteArray();
      Object doc = var3[108].callGetProperty(this.outputArea);
      Object len = var3[109].callGetProperty(doc);
      Object alreadyNewLine = !ScriptBytecodeAdapter.compareEqual(len, $const$2) && !ScriptBytecodeAdapter.compareEqual(var3[110].call(doc, var3[111].call(len, (Object)$const$1), $const$1), "\n") ? Boolean.FALSE : Boolean.TRUE;
      var3[112].call(doc, var3[113].callGetProperty(doc), " \n", style);
      if (DefaultTypeTransformation.booleanUnbox(alreadyNewLine)) {
         var3[114].call(doc, len, $const$3);
      }

      var3[115].callCurrent(this, text, style);
   }

   public void appendOutputLines(Object text, Object style) {
      CallSite[] var3 = $getCallSiteArray();
      var3[116].callCurrent(this, text, style);
      Object doc = var3[117].callGetProperty(this.outputArea);
      Object len = var3[118].callGetProperty(doc);
      var3[119].call(doc, len, " \n", style);
      var3[120].call(doc, len, $const$3);
   }

   public boolean askToSaveFile() {
      CallSite[] var1 = $getCallSiteArray();
      if (DefaultTypeTransformation.booleanUnbox(!ScriptBytecodeAdapter.compareEqual(this.scriptFile, (Object)null) && DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.box(this.dirty)) ? Boolean.FALSE : Boolean.TRUE)) {
         return DefaultTypeTransformation.booleanUnbox((Boolean)ScriptBytecodeAdapter.castToType(Boolean.TRUE, $get$$class$java$lang$Boolean()));
      } else {
         Object var2 = var1[121].call($get$$class$javax$swing$JOptionPane(), this.frame, var1[122].call(var1[123].call("Save changes to ", (Object)var1[124].callGetProperty(this.scriptFile)), (Object)"?"), "GroovyConsole", var1[125].callGetProperty($get$$class$javax$swing$JOptionPane()));
         if (ScriptBytecodeAdapter.isCase(var2, var1[126].callGetProperty($get$$class$javax$swing$JOptionPane()))) {
            return DefaultTypeTransformation.booleanUnbox((Boolean)ScriptBytecodeAdapter.castToType(var1[127].callCurrent(this), $get$$class$java$lang$Boolean()));
         } else {
            return ScriptBytecodeAdapter.isCase(var2, var1[128].callGetProperty($get$$class$javax$swing$JOptionPane())) ? DefaultTypeTransformation.booleanUnbox((Boolean)ScriptBytecodeAdapter.castToType(Boolean.TRUE, $get$$class$java$lang$Boolean())) : DefaultTypeTransformation.booleanUnbox((Boolean)ScriptBytecodeAdapter.castToType(Boolean.FALSE, $get$$class$java$lang$Boolean()));
         }
      }
   }

   public void beep() {
      CallSite[] var1 = $getCallSiteArray();
      var1[129].call(var1[130].callGetProperty($get$$class$java$awt$Toolkit()));
   }

   public void bindResults() {
      CallSite[] var1 = $getCallSiteArray();
      var1[131].call(this.shell, "_", var1[132].callCurrent(this));
      var1[133].call(this.shell, "__", var1[134].call(this.history, (Object)(new GeneratedClosure(this, this) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$groovy$ui$Console$_bindResults_closure6;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(Object it) {
            Object itx = new Reference(it);
            CallSite[] var3 = $getCallSiteArray();
            return var3[0].callGetProperty(itx.get());
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[1].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$ui$Console$_bindResults_closure6()) {
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
            var0[0] = "result";
            var0[1] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[2];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$ui$Console$_bindResults_closure6(), var0);
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
         private static Class $get$$class$groovy$ui$Console$_bindResults_closure6() {
            Class var10000 = $class$groovy$ui$Console$_bindResults_closure6;
            if (var10000 == null) {
               var10000 = $class$groovy$ui$Console$_bindResults_closure6 = class$("groovy.ui.Console$_bindResults_closure6");
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
         static Class class$(String var0) {
            try {
               return Class.forName(var0);
            } catch (ClassNotFoundException var2) {
               throw new NoClassDefFoundError(var2.getMessage());
            }
         }
      })));
   }

   public static void captureStdOut(EventObject evt) {
      CallSite[] var1 = $getCallSiteArray();
      captureStdOut = DefaultTypeTransformation.booleanUnbox(var1[135].callGetProperty(var1[136].callGetProperty(evt)));
      var1[137].call(prefs, "captureStdOut", DefaultTypeTransformation.box(captureStdOut));
   }

   public static void captureStdErr(EventObject evt) {
      CallSite[] var1 = $getCallSiteArray();
      captureStdErr = DefaultTypeTransformation.booleanUnbox(var1[138].callGetProperty(var1[139].callGetProperty(evt)));
      var1[140].call(prefs, "captureStdErr", DefaultTypeTransformation.box(captureStdErr));
   }

   public void fullStackTraces(EventObject evt) {
      CallSite[] var2 = $getCallSiteArray();
      this.fullStackTraces = DefaultTypeTransformation.booleanUnbox(var2[141].callGetProperty(var2[142].callGetProperty(evt)));
      var2[143].call($get$$class$java$lang$System(), "groovy.full.stacktrace", var2[144].call($get$$class$java$lang$Boolean(), (Object)DefaultTypeTransformation.box(this.fullStackTraces)));
      var2[145].call(prefs, "fullStackTraces", DefaultTypeTransformation.box(this.fullStackTraces));
   }

   public void showScriptInOutput(EventObject evt) {
      CallSite[] var2 = $getCallSiteArray();
      this.showScriptInOutput = DefaultTypeTransformation.booleanUnbox(var2[146].callGetProperty(var2[147].callGetProperty(evt)));
      var2[148].call(prefs, "showScriptInOutput", DefaultTypeTransformation.box(this.showScriptInOutput));
   }

   public void visualizeScriptResults(EventObject evt) {
      CallSite[] var2 = $getCallSiteArray();
      this.visualizeScriptResults = DefaultTypeTransformation.booleanUnbox(var2[149].callGetProperty(var2[150].callGetProperty(evt)));
      var2[151].call(prefs, "visualizeScriptResults", DefaultTypeTransformation.box(this.visualizeScriptResults));
   }

   public void showToolbar(EventObject evt) {
      CallSite[] var2 = $getCallSiteArray();
      this.showToolbar = DefaultTypeTransformation.booleanUnbox(var2[152].callGetProperty(var2[153].callGetProperty(evt)));
      var2[154].call(prefs, "showToolbar", DefaultTypeTransformation.box(this.showToolbar));
      ScriptBytecodeAdapter.setProperty(DefaultTypeTransformation.box(this.showToolbar), $get$$class$groovy$ui$Console(), this.toolbar, "visible");
   }

   public void detachedOutput(EventObject evt) {
      CallSite[] var2 = $getCallSiteArray();
      Object oldDetachedOutput = DefaultTypeTransformation.box(this.detachedOutput);
      this.detachedOutput = DefaultTypeTransformation.booleanUnbox(var2[155].callGetProperty(var2[156].callGetProperty(evt)));
      var2[157].call(prefs, "detachedOutput", DefaultTypeTransformation.box(this.detachedOutput));
      if (ScriptBytecodeAdapter.compareNotEqual(oldDetachedOutput, DefaultTypeTransformation.box(this.detachedOutput))) {
         if (DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.box(this.detachedOutput))) {
            var2[158].call(this.splitPane, this.blank, var2[159].callGetProperty($get$$class$javax$swing$JSplitPane()));
            this.origDividerSize = DefaultTypeTransformation.intUnbox(var2[160].callGetProperty(this.splitPane));
            ScriptBytecodeAdapter.setProperty($const$2, $get$$class$groovy$ui$Console(), this.splitPane, "dividerSize");
            ScriptBytecodeAdapter.setProperty($const$4, $get$$class$groovy$ui$Console(), this.splitPane, "resizeWeight");
            var2[161].call(this.outputWindow, this.scrollArea, var2[162].callGetProperty($get$$class$java$awt$BorderLayout()));
            var2[163].callCurrent(this);
         } else {
            var2[164].call(this.splitPane, this.scrollArea, var2[165].callGetProperty($get$$class$javax$swing$JSplitPane()));
            ScriptBytecodeAdapter.setProperty(DefaultTypeTransformation.box(this.origDividerSize), $get$$class$groovy$ui$Console(), this.splitPane, "dividerSize");
            var2[166].call(this.outputWindow, this.blank, var2[167].callGetProperty($get$$class$java$awt$BorderLayout()));
            ScriptBytecodeAdapter.setProperty(Boolean.FALSE, $get$$class$groovy$ui$Console(), this.outputWindow, "visible");
            ScriptBytecodeAdapter.setProperty($const$5, $get$$class$groovy$ui$Console(), this.splitPane, "resizeWeight");
         }
      }

   }

   public void autoClearOutput(EventObject evt) {
      CallSite[] var2 = $getCallSiteArray();
      this.autoClearOutput = DefaultTypeTransformation.booleanUnbox(var2[168].callGetProperty(var2[169].callGetProperty(evt)));
      var2[170].call(prefs, "autoClearOutput", DefaultTypeTransformation.box(this.autoClearOutput));
   }

   public void caretUpdate(CaretEvent e) {
      CallSite[] var2 = $getCallSiteArray();
      this.textSelectionStart = DefaultTypeTransformation.intUnbox(var2[171].call($get$$class$java$lang$Math(), var2[172].callGetProperty(e), var2[173].callGetProperty(e)));
      this.textSelectionEnd = DefaultTypeTransformation.intUnbox(var2[174].call($get$$class$java$lang$Math(), var2[175].callGetProperty(e), var2[176].callGetProperty(e)));
      var2[177].callCurrent(this);
   }

   public void clearOutput(EventObject evt) {
      CallSite[] var2 = $getCallSiteArray();
      ScriptBytecodeAdapter.setProperty("", $get$$class$groovy$ui$Console(), this.outputArea, "text");
   }

   public Object askToInterruptScript() {
      CallSite[] var1 = $getCallSiteArray();
      if (!DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.box(this.scriptRunning))) {
         return Boolean.TRUE;
      } else {
         Object rc = var1[178].call($get$$class$javax$swing$JOptionPane(), this.frame, "Script executing. Press 'OK' to attempt to interrupt it before exiting.", "GroovyConsole", var1[179].callGetProperty($get$$class$javax$swing$JOptionPane()));
         if (ScriptBytecodeAdapter.compareEqual(rc, var1[180].callGetProperty($get$$class$javax$swing$JOptionPane()))) {
            var1[181].callCurrent(this);
            return Boolean.TRUE;
         } else {
            return Boolean.FALSE;
         }
      }
   }

   public void doInterrupt(EventObject evt) {
      CallSite[] var2 = $getCallSiteArray();
      var2[182].callSafe(this.runThread);
   }

   public void exit(EventObject evt) {
      CallSite[] var2 = $getCallSiteArray();
      if (DefaultTypeTransformation.booleanUnbox(var2[183].callCurrent(this)) && DefaultTypeTransformation.booleanUnbox(var2[184].callCurrent(this))) {
         if (this.frame instanceof Window) {
            var2[185].call(this.frame);
            var2[186].call(this.frame);
            var2[187].callSafe(this.outputWindow);
         }

         var2[188].call($get$$class$groovy$ui$text$FindReplaceUtility());
         var2[189].call(consoleControllers, (Object)this);
         if (!DefaultTypeTransformation.booleanUnbox(consoleControllers)) {
            var2[190].call(this.systemOutInterceptor);
            var2[191].call(this.systemErrorInterceptor);
         }
      }

   }

   public void fileNewFile(EventObject evt) {
      CallSite[] var2 = $getCallSiteArray();
      if (DefaultTypeTransformation.booleanUnbox(var2[192].callCurrent(this))) {
         this.scriptFile = null;
         var2[193].callCurrent(this, (Object)Boolean.FALSE);
         ScriptBytecodeAdapter.setProperty("", $get$$class$groovy$ui$Console(), this.inputArea, "text");
      }

   }

   public void fileNewWindow(EventObject evt) {
      CallSite[] var2 = $getCallSiteArray();
      Console consoleController = var2[194].callConstructor($get$$class$groovy$ui$Console(), (Object)var2[195].callConstructor($get$$class$groovy$lang$Binding(), (Object)var2[196].callConstructor($get$$class$java$util$HashMap(), (Object)var2[197].callGetProperty(var2[198].callGroovyObjectGetProperty(this.shell)))));
      ScriptBytecodeAdapter.setGroovyObjectProperty(this.systemOutInterceptor, $get$$class$groovy$ui$Console(), (GroovyObject)consoleController, "systemOutInterceptor");
      ScriptBytecodeAdapter.setGroovyObjectProperty(this.systemErrorInterceptor, $get$$class$groovy$ui$Console(), (GroovyObject)consoleController, "systemErrorInterceptor");
      SwingBuilder swing = new Reference(var2[199].callConstructor($get$$class$groovy$swing$SwingBuilder()));
      ScriptBytecodeAdapter.setGroovyObjectProperty(swing.get(), $get$$class$groovy$ui$Console(), (GroovyObject)consoleController, "swing");
      var2[200].call(frameConsoleDelegates, (Object)(new GeneratedClosure(this, this, swing) {
         private Reference<T> swing;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$groovy$ui$Console$_fileNewWindow_closure7;
         // $FF: synthetic field
         private static Class $class$groovy$swing$SwingBuilder;

         public {
            CallSite[] var4 = $getCallSiteArray();
            this.swing = (Reference)swing;
         }

         public Object doCall(Object k, Object v) {
            Object kx = new Reference(k);
            Object vx = new Reference(v);
            CallSite[] var5 = $getCallSiteArray();
            CallSite var10000 = var5[0];
            Object var10001 = this.swing.get();
            Object var10002 = kx.get();
            Object var6 = vx.get();
            var10000.call(var10001, var10002, var6);
            return var6;
         }

         public Object call(Object k, Object v) {
            Object kx = new Reference(k);
            Object vx = new Reference(v);
            CallSite[] var5 = $getCallSiteArray();
            return var5[1].callCurrent(this, kx.get(), vx.get());
         }

         public SwingBuilder getSwing() {
            CallSite[] var1 = $getCallSiteArray();
            return (SwingBuilder)ScriptBytecodeAdapter.castToType(this.swing.get(), $get$$class$groovy$swing$SwingBuilder());
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$ui$Console$_fileNewWindow_closure7()) {
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
            var0[0] = "putAt";
            var0[1] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[2];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$ui$Console$_fileNewWindow_closure7(), var0);
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
         private static Class $get$$class$groovy$ui$Console$_fileNewWindow_closure7() {
            Class var10000 = $class$groovy$ui$Console$_fileNewWindow_closure7;
            if (var10000 == null) {
               var10000 = $class$groovy$ui$Console$_fileNewWindow_closure7 = class$("groovy.ui.Console$_fileNewWindow_closure7");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$groovy$swing$SwingBuilder() {
            Class var10000 = $class$groovy$swing$SwingBuilder;
            if (var10000 == null) {
               var10000 = $class$groovy$swing$SwingBuilder = class$("groovy.swing.SwingBuilder");
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
      ScriptBytecodeAdapter.setGroovyObjectProperty(consoleController, $get$$class$groovy$ui$Console(), (GroovyObject)swing.get(), "controller");
      var2[201].call(swing.get(), (Object)$get$$class$groovy$ui$ConsoleActions());
      var2[202].call(swing.get(), (Object)$get$$class$groovy$ui$ConsoleView());
      var2[203].callCurrent(this);
      var2[204].call(var2[205].callGroovyObjectGetProperty(swing.get()));
      var2[206].call(var2[207].callGroovyObjectGetProperty(swing.get()));
      var2[208].call(swing.get(), (Object)ScriptBytecodeAdapter.getMethodPointer(var2[209].callGroovyObjectGetProperty(swing.get()), "requestFocus"));
   }

   public void fileOpen(EventObject evt) {
      CallSite[] var2 = $getCallSiteArray();
      Object scriptName = var2[210].callCurrent(this);
      if (ScriptBytecodeAdapter.compareNotEqual(scriptName, (Object)null)) {
         var2[211].callCurrent(this, (Object)scriptName);
      }

   }

   public void loadScriptFile(File file) {
      File file = new Reference(file);
      CallSite[] var3 = $getCallSiteArray();
      var3[212].call(this.swing, (Object)(new GeneratedClosure(this, this) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$groovy$ui$Console$_loadScriptFile_closure8;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(Object it) {
            CallSite[] var2 = $getCallSiteArray();
            Boolean var10000 = Boolean.FALSE;
            ScriptBytecodeAdapter.setProperty(var10000, $get$$class$groovy$ui$Console$_loadScriptFile_closure8(), var2[0].callGroovyObjectGetProperty(this), "editable");
            return var10000;
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[1].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$ui$Console$_loadScriptFile_closure8()) {
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
            var0[0] = "inputArea";
            var0[1] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[2];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$ui$Console$_loadScriptFile_closure8(), var0);
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
         private static Class $get$$class$groovy$ui$Console$_loadScriptFile_closure8() {
            Class var10000 = $class$groovy$ui$Console$_loadScriptFile_closure8;
            if (var10000 == null) {
               var10000 = $class$groovy$ui$Console$_loadScriptFile_closure8 = class$("groovy.ui.Console$_loadScriptFile_closure8");
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
         static Class class$(String var0) {
            try {
               return Class.forName(var0);
            } catch (ClassNotFoundException var2) {
               throw new NoClassDefFoundError(var2.getMessage());
            }
         }
      }));
      var3[213].call(this.swing, (Object)(new GeneratedClosure(this, this, file) {
         private Reference<T> file;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$java$io$File;
         // $FF: synthetic field
         private static Class $class$groovy$ui$Console$_loadScriptFile_closure9;

         public {
            CallSite[] var4 = $getCallSiteArray();
            this.file = (Reference)file;
         }

         public Object doCall(Object it) {
            CallSite[] var2 = $getCallSiteArray();

            Throwable var10000;
            label69: {
               boolean var10001;
               Object var3;
               try {
                  ScriptBytecodeAdapter.setGroovyObjectProperty(var2[0].call(var2[1].call(this.file.get()), (Object)"\n"), $get$$class$groovy$ui$Console$_loadScriptFile_closure9(), this, "consoleText");
                  ScriptBytecodeAdapter.setGroovyObjectProperty(this.file.get(), $get$$class$groovy$ui$Console$_loadScriptFile_closure9(), this, "scriptFile");
                  var3 = var2[2].call(var2[3].callGroovyObjectGetProperty(this), (Object)(new GeneratedClosure(this, this.getThisObject()) {
                     // $FF: synthetic field
                     private static final Integer $const$0 = (Integer)0;
                     // $FF: synthetic field
                     private static ClassInfo $staticClassInfo;
                     // $FF: synthetic field
                     private static SoftReference $callSiteArray;
                     // $FF: synthetic field
                     private static Class $class$java$lang$Object;
                     // $FF: synthetic field
                     private static Class $class$groovy$ui$Console$_loadScriptFile_closure9_closure27;

                     public {
                        CallSite[] var3 = $getCallSiteArray();
                     }

                     public Object doCall(Object it) {
                        CallSite[] var2 = $getCallSiteArray();
                        var2[0].callCurrent(this);
                        var2[1].call(var2[2].callGetProperty(var2[3].callGroovyObjectGetProperty(this)), $const$0, var2[4].callGetProperty(var2[5].callGetProperty(var2[6].callGroovyObjectGetProperty(this))));
                        var2[7].call(var2[8].callGetProperty(var2[9].callGroovyObjectGetProperty(this)), $const$0, var2[10].callGroovyObjectGetProperty(this), (Object)null);
                        var2[11].callCurrent(this, (Object)Boolean.FALSE);
                        Integer var10000 = $const$0;
                        ScriptBytecodeAdapter.setProperty(var10000, $get$$class$groovy$ui$Console$_loadScriptFile_closure9_closure27(), var2[12].callGroovyObjectGetProperty(this), "caretPosition");
                        return var10000;
                     }

                     public Object doCall() {
                        CallSite[] var1 = $getCallSiteArray();
                        return var1[13].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
                     }

                     // $FF: synthetic method
                     protected MetaClass $getStaticMetaClass() {
                        if (this.getClass() == $get$$class$groovy$ui$Console$_loadScriptFile_closure9_closure27()) {
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
                        var0[0] = "updateTitle";
                        var0[1] = "remove";
                        var0[2] = "document";
                        var0[3] = "inputArea";
                        var0[4] = "length";
                        var0[5] = "document";
                        var0[6] = "inputArea";
                        var0[7] = "insertString";
                        var0[8] = "document";
                        var0[9] = "inputArea";
                        var0[10] = "consoleText";
                        var0[11] = "setDirty";
                        var0[12] = "inputArea";
                        var0[13] = "doCall";
                     }

                     // $FF: synthetic method
                     private static CallSiteArray $createCallSiteArray() {
                        String[] var0 = new String[14];
                        $createCallSiteArray_1(var0);
                        return new CallSiteArray($get$$class$groovy$ui$Console$_loadScriptFile_closure9_closure27(), var0);
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
                     private static Class $get$$class$groovy$ui$Console$_loadScriptFile_closure9_closure27() {
                        Class var10000 = $class$groovy$ui$Console$_loadScriptFile_closure9_closure27;
                        if (var10000 == null) {
                           var10000 = $class$groovy$ui$Console$_loadScriptFile_closure9_closure27 = class$("groovy.ui.Console$_loadScriptFile_closure9_closure27");
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
               } catch (Throwable var9) {
                  var10000 = var9;
                  var10001 = false;
                  break label69;
               }

               var2[4].call(var2[5].callGroovyObjectGetProperty(this), (Object)(new GeneratedClosure(this, this.getThisObject()) {
                  // $FF: synthetic field
                  private static ClassInfo $staticClassInfo;
                  // $FF: synthetic field
                  private static SoftReference $callSiteArray;
                  // $FF: synthetic field
                  private static Class $class$java$lang$Object;
                  // $FF: synthetic field
                  private static Class $class$groovy$ui$Console$_loadScriptFile_closure9_closure28;

                  public {
                     CallSite[] var3 = $getCallSiteArray();
                  }

                  public Object doCall(Object it) {
                     CallSite[] var2 = $getCallSiteArray();
                     Boolean var10000 = Boolean.TRUE;
                     ScriptBytecodeAdapter.setProperty(var10000, $get$$class$groovy$ui$Console$_loadScriptFile_closure9_closure28(), var2[0].callGroovyObjectGetProperty(this), "editable");
                     return var10000;
                  }

                  public Object doCall() {
                     CallSite[] var1 = $getCallSiteArray();
                     return var1[1].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
                  }

                  // $FF: synthetic method
                  protected MetaClass $getStaticMetaClass() {
                     if (this.getClass() == $get$$class$groovy$ui$Console$_loadScriptFile_closure9_closure28()) {
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
                     var0[0] = "inputArea";
                     var0[1] = "doCall";
                  }

                  // $FF: synthetic method
                  private static CallSiteArray $createCallSiteArray() {
                     String[] var0 = new String[2];
                     $createCallSiteArray_1(var0);
                     return new CallSiteArray($get$$class$groovy$ui$Console$_loadScriptFile_closure9_closure28(), var0);
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
                  private static Class $get$$class$groovy$ui$Console$_loadScriptFile_closure9_closure28() {
                     Class var10000 = $class$groovy$ui$Console$_loadScriptFile_closure9_closure28;
                     if (var10000 == null) {
                        var10000 = $class$groovy$ui$Console$_loadScriptFile_closure9_closure28 = class$("groovy.ui.Console$_loadScriptFile_closure9_closure28");
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
               var2[6].call(var2[7].callGroovyObjectGetProperty(this), (Object)ScriptBytecodeAdapter.getMethodPointer(var2[8].callGroovyObjectGetProperty(this), "requestFocusInWindow"));
               var2[9].call(var2[10].callGroovyObjectGetProperty(this), (Object)ScriptBytecodeAdapter.getMethodPointer(var2[11].callGroovyObjectGetProperty(this), "requestFocusInWindow"));

               label61:
               try {
                  return var3;
               } catch (Throwable var8) {
                  var10000 = var8;
                  var10001 = false;
                  break label61;
               }
            }

            Throwable var10 = var10000;
            var2[20].call(var2[21].callGroovyObjectGetProperty(this), (Object)(new GeneratedClosure(this, this.getThisObject()) {
               // $FF: synthetic field
               private static ClassInfo $staticClassInfo;
               // $FF: synthetic field
               private static SoftReference $callSiteArray;
               // $FF: synthetic field
               private static Class $class$java$lang$Object;
               // $FF: synthetic field
               private static Class $class$groovy$ui$Console$_loadScriptFile_closure9_closure28;

               public {
                  CallSite[] var3 = $getCallSiteArray();
               }

               public Object doCall(Object it) {
                  CallSite[] var2 = $getCallSiteArray();
                  Boolean var10000 = Boolean.TRUE;
                  ScriptBytecodeAdapter.setProperty(var10000, $get$$class$groovy$ui$Console$_loadScriptFile_closure9_closure28(), var2[0].callGroovyObjectGetProperty(this), "editable");
                  return var10000;
               }

               public Object doCall() {
                  CallSite[] var1 = $getCallSiteArray();
                  return var1[1].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
               }

               // $FF: synthetic method
               protected MetaClass $getStaticMetaClass() {
                  if (this.getClass() == $get$$class$groovy$ui$Console$_loadScriptFile_closure9_closure28()) {
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
                  var0[0] = "inputArea";
                  var0[1] = "doCall";
               }

               // $FF: synthetic method
               private static CallSiteArray $createCallSiteArray() {
                  String[] var0 = new String[2];
                  $createCallSiteArray_1(var0);
                  return new CallSiteArray($get$$class$groovy$ui$Console$_loadScriptFile_closure9_closure28(), var0);
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
               private static Class $get$$class$groovy$ui$Console$_loadScriptFile_closure9_closure28() {
                  Class var10000 = $class$groovy$ui$Console$_loadScriptFile_closure9_closure28;
                  if (var10000 == null) {
                     var10000 = $class$groovy$ui$Console$_loadScriptFile_closure9_closure28 = class$("groovy.ui.Console$_loadScriptFile_closure9_closure28");
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
            var2[22].call(var2[23].callGroovyObjectGetProperty(this), (Object)ScriptBytecodeAdapter.getMethodPointer(var2[24].callGroovyObjectGetProperty(this), "requestFocusInWindow"));
            var2[25].call(var2[26].callGroovyObjectGetProperty(this), (Object)ScriptBytecodeAdapter.getMethodPointer(var2[27].callGroovyObjectGetProperty(this), "requestFocusInWindow"));
            throw var10;
         }

         public File getFile() {
            CallSite[] var1 = $getCallSiteArray();
            return (File)ScriptBytecodeAdapter.castToType(this.file.get(), $get$$class$java$io$File());
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[28].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$ui$Console$_loadScriptFile_closure9()) {
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
            var0[0] = "join";
            var0[1] = "readLines";
            var0[2] = "edt";
            var0[3] = "swing";
            var0[4] = "edt";
            var0[5] = "swing";
            var0[6] = "doLater";
            var0[7] = "swing";
            var0[8] = "outputArea";
            var0[9] = "doLater";
            var0[10] = "swing";
            var0[11] = "inputArea";
            var0[12] = "edt";
            var0[13] = "swing";
            var0[14] = "doLater";
            var0[15] = "swing";
            var0[16] = "outputArea";
            var0[17] = "doLater";
            var0[18] = "swing";
            var0[19] = "inputArea";
            var0[20] = "edt";
            var0[21] = "swing";
            var0[22] = "doLater";
            var0[23] = "swing";
            var0[24] = "outputArea";
            var0[25] = "doLater";
            var0[26] = "swing";
            var0[27] = "inputArea";
            var0[28] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[29];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$ui$Console$_loadScriptFile_closure9(), var0);
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
         private static Class $get$$class$java$io$File() {
            Class var10000 = $class$java$io$File;
            if (var10000 == null) {
               var10000 = $class$java$io$File = class$("java.io.File");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$groovy$ui$Console$_loadScriptFile_closure9() {
            Class var10000 = $class$groovy$ui$Console$_loadScriptFile_closure9;
            if (var10000 == null) {
               var10000 = $class$groovy$ui$Console$_loadScriptFile_closure9 = class$("groovy.ui.Console$_loadScriptFile_closure9");
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
   }

   public boolean fileSave(EventObject evt) {
      CallSite[] var2 = $getCallSiteArray();
      if (ScriptBytecodeAdapter.compareEqual(this.scriptFile, (Object)null)) {
         return DefaultTypeTransformation.booleanUnbox((Boolean)ScriptBytecodeAdapter.castToType(var2[214].callCurrent(this, (Object)evt), $get$$class$java$lang$Boolean()));
      } else {
         var2[215].call(this.scriptFile, var2[216].callGetProperty(this.inputArea));
         var2[217].callCurrent(this, (Object)Boolean.FALSE);
         return DefaultTypeTransformation.booleanUnbox((Boolean)ScriptBytecodeAdapter.castToType(Boolean.TRUE, $get$$class$java$lang$Boolean()));
      }
   }

   public boolean fileSaveAs(EventObject evt) {
      CallSite[] var2 = $getCallSiteArray();
      this.scriptFile = var2[218].callCurrent(this, (Object)"Save");
      if (ScriptBytecodeAdapter.compareNotEqual(this.scriptFile, (Object)null)) {
         var2[219].call(this.scriptFile, var2[220].callGetProperty(this.inputArea));
         var2[221].callCurrent(this, (Object)Boolean.FALSE);
         return DefaultTypeTransformation.booleanUnbox((Boolean)ScriptBytecodeAdapter.castToType(Boolean.TRUE, $get$$class$java$lang$Boolean()));
      } else {
         return DefaultTypeTransformation.booleanUnbox((Boolean)ScriptBytecodeAdapter.castToType(Boolean.FALSE, $get$$class$java$lang$Boolean()));
      }
   }

   public Object finishException(Throwable t, boolean executing) {
      CallSite[] var3 = $getCallSiteArray();
      if (DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.box(executing))) {
         ScriptBytecodeAdapter.setProperty("Execution terminated with exception.", $get$$class$groovy$ui$Console(), this.statusLabel, "text");
         ScriptBytecodeAdapter.setProperty(t, $get$$class$groovy$ui$Console(), var3[222].call(this.history, (Object)$const$6), "exception");
      } else {
         ScriptBytecodeAdapter.setProperty("Compilation failed.", $get$$class$groovy$ui$Console(), this.statusLabel, "text");
      }

      if (t instanceof MultipleCompilationErrorsException) {
         MultipleCompilationErrorsException mcee = (MultipleCompilationErrorsException)ScriptBytecodeAdapter.castToType(t, $get$$class$org$codehaus$groovy$control$MultipleCompilationErrorsException());
         ErrorCollector collector = (ErrorCollector)ScriptBytecodeAdapter.castToType(var3[223].callGetProperty(mcee), $get$$class$org$codehaus$groovy$control$ErrorCollector());
         Integer count = (Integer)ScriptBytecodeAdapter.castToType(var3[224].callGetProperty(collector), $get$$class$java$lang$Integer());
         var3[225].callCurrent(this, new GStringImpl(new Object[]{count, ScriptBytecodeAdapter.compareGreaterThan(count, $const$1) ? "s" : ""}, new String[]{"", " compilation error", ":\n\n"}), this.commandStyle);
         var3[226].call(var3[227].callGetProperty(collector), (Object)(new GeneratedClosure(this, this) {
            // $FF: synthetic field
            private static ClassInfo $staticClassInfo;
            // $FF: synthetic field
            private static SoftReference $callSiteArray;
            // $FF: synthetic field
            private static Class $class$java$lang$Integer;
            // $FF: synthetic field
            private static Class $class$javax$swing$text$SimpleAttributeSet;
            // $FF: synthetic field
            private static Class $class$groovy$ui$Console$_finishException_closure10;
            // $FF: synthetic field
            private static Class $class$org$codehaus$groovy$syntax$SyntaxException;
            // $FF: synthetic field
            private static Class $class$javax$swing$text$html$HTML$Tag;
            // $FF: synthetic field
            private static Class $class$java$lang$String;
            // $FF: synthetic field
            private static Class $class$javax$swing$text$html$HTML$Attribute;

            public {
               CallSite[] var3 = $getCallSiteArray();
            }

            public Object doCall(Object error) {
               Object errorx = new Reference(error);
               CallSite[] var3 = $getCallSiteArray();
               if (errorx.get() instanceof SyntaxErrorMessage) {
                  SyntaxException se = (SyntaxException)ScriptBytecodeAdapter.castToType(var3[0].callGetProperty(errorx.get()), $get$$class$org$codehaus$groovy$syntax$SyntaxException());
                  Integer errorLine = (Integer)ScriptBytecodeAdapter.castToType(var3[1].callGetProperty(se), $get$$class$java$lang$Integer());
                  String message = (String)ScriptBytecodeAdapter.castToType(var3[2].callGetProperty(se), $get$$class$java$lang$String());
                  Object var10000 = var3[3].callGetPropertySafe(var3[4].callGroovyObjectGetProperty(this));
                  if (!DefaultTypeTransformation.booleanUnbox(var10000)) {
                     var10000 = var3[5].callGroovyObjectGetProperty(this);
                  }

                  String scriptFileName = (String)ScriptBytecodeAdapter.castToType(var10000, $get$$class$java$lang$String());
                  Object doc = var3[6].callGetProperty(var3[7].callGroovyObjectGetProperty(this));
                  Object style = var3[8].callGroovyObjectGetProperty(this);
                  Object hrefAttr = var3[9].callConstructor($get$$class$javax$swing$text$SimpleAttributeSet());
                  var3[10].call(hrefAttr, var3[11].callGetProperty($get$$class$javax$swing$text$html$HTML$Attribute()), var3[12].call(var3[13].call(var3[14].call("file://", (Object)scriptFileName), (Object)":"), (Object)errorLine));
                  var3[15].call(style, var3[16].callGetProperty($get$$class$javax$swing$text$html$HTML$Tag()), hrefAttr);
                  var3[17].call(doc, var3[18].callGetProperty(doc), var3[19].call(message, (Object)" at "), var3[20].callGroovyObjectGetProperty(this));
                  return var3[21].call(doc, var3[22].callGetProperty(doc), new GStringImpl(new Object[]{var3[23].callGetProperty(se), var3[24].callGetProperty(se)}, new String[]{"line: ", ", column: ", "\n\n"}), style);
               } else if (errorx.get() instanceof Throwable) {
                  return var3[25].callCurrent(this, (Object)errorx.get());
               } else {
                  return errorx.get() instanceof ExceptionMessage ? var3[26].callCurrent(this, (Object)var3[27].callGetProperty(errorx.get())) : null;
               }
            }

            // $FF: synthetic method
            protected MetaClass $getStaticMetaClass() {
               if (this.getClass() == $get$$class$groovy$ui$Console$_finishException_closure10()) {
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
               var0[0] = "cause";
               var0[1] = "line";
               var0[2] = "originalMessage";
               var0[3] = "name";
               var0[4] = "scriptFile";
               var0[5] = "DEFAULT_SCRIPT_NAME_START";
               var0[6] = "styledDocument";
               var0[7] = "outputArea";
               var0[8] = "hyperlinkStyle";
               var0[9] = "<$constructor$>";
               var0[10] = "addAttribute";
               var0[11] = "HREF";
               var0[12] = "plus";
               var0[13] = "plus";
               var0[14] = "plus";
               var0[15] = "addAttribute";
               var0[16] = "A";
               var0[17] = "insertString";
               var0[18] = "length";
               var0[19] = "plus";
               var0[20] = "stacktraceStyle";
               var0[21] = "insertString";
               var0[22] = "length";
               var0[23] = "line";
               var0[24] = "startColumn";
               var0[25] = "reportException";
               var0[26] = "reportException";
               var0[27] = "cause";
            }

            // $FF: synthetic method
            private static CallSiteArray $createCallSiteArray() {
               String[] var0 = new String[28];
               $createCallSiteArray_1(var0);
               return new CallSiteArray($get$$class$groovy$ui$Console$_finishException_closure10(), var0);
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
            private static Class $get$$class$javax$swing$text$SimpleAttributeSet() {
               Class var10000 = $class$javax$swing$text$SimpleAttributeSet;
               if (var10000 == null) {
                  var10000 = $class$javax$swing$text$SimpleAttributeSet = class$("javax.swing.text.SimpleAttributeSet");
               }

               return var10000;
            }

            // $FF: synthetic method
            private static Class $get$$class$groovy$ui$Console$_finishException_closure10() {
               Class var10000 = $class$groovy$ui$Console$_finishException_closure10;
               if (var10000 == null) {
                  var10000 = $class$groovy$ui$Console$_finishException_closure10 = class$("groovy.ui.Console$_finishException_closure10");
               }

               return var10000;
            }

            // $FF: synthetic method
            private static Class $get$$class$org$codehaus$groovy$syntax$SyntaxException() {
               Class var10000 = $class$org$codehaus$groovy$syntax$SyntaxException;
               if (var10000 == null) {
                  var10000 = $class$org$codehaus$groovy$syntax$SyntaxException = class$("org.codehaus.groovy.syntax.SyntaxException");
               }

               return var10000;
            }

            // $FF: synthetic method
            private static Class $get$$class$javax$swing$text$html$HTML$Tag() {
               Class var10000 = $class$javax$swing$text$html$HTML$Tag;
               if (var10000 == null) {
                  var10000 = $class$javax$swing$text$html$HTML$Tag = class$("javax.swing.text.html.HTML$Tag");
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
            private static Class $get$$class$javax$swing$text$html$HTML$Attribute() {
               Class var10000 = $class$javax$swing$text$html$HTML$Attribute;
               if (var10000 == null) {
                  var10000 = $class$javax$swing$text$html$HTML$Attribute = class$("javax.swing.text.html.HTML$Attribute");
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
      } else {
         var3[228].callCurrent(this, (Object)t);
      }

      if (!DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.box(executing))) {
         var3[229].callCurrent(this);
      }

      ScriptBytecodeAdapter.setProperty($const$2, $get$$class$groovy$ui$Console(), this.outputArea, "caretPosition");
      if (DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.box(this.detachedOutput))) {
         var3[230].callCurrent(this);
         return var3[231].callCurrent(this);
      } else {
         return null;
      }
   }

   private Object calcPreferredSize(Object a, Object b, Object c) {
      CallSite[] var4 = $getCallSiteArray();
      return var4[232].call(ScriptBytecodeAdapter.createList(new Object[]{c, var4[233].call(ScriptBytecodeAdapter.createList(new Object[]{a, b}))}));
   }

   private Object reportException(Throwable t) {
      Throwable t = new Reference(t);
      CallSite[] var3 = $getCallSiteArray();
      var3[234].callCurrent(this, "Exception thrown\n", this.commandStyle);
      StringWriter sw = var3[235].callConstructor($get$$class$java$io$StringWriter());
      var3[236].call(var3[237].callConstructor($get$$class$java$io$PrintWriter(), (Object)sw), (Object)(new GeneratedClosure(this, this, t) {
         private Reference<T> t;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$groovy$ui$Console$_reportException_closure11;
         // $FF: synthetic field
         private static Class $class$java$lang$Throwable;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$runtime$StackTraceUtils;

         public {
            CallSite[] var4 = $getCallSiteArray();
            this.t = (Reference)t;
         }

         public Object doCall(Object pw) {
            Object pwx = new Reference(pw);
            CallSite[] var3 = $getCallSiteArray();
            return var3[0].call(var3[1].call($get$$class$org$codehaus$groovy$runtime$StackTraceUtils(), (Object)this.t.get()), pwx.get());
         }

         public Throwable getT() {
            CallSite[] var1 = $getCallSiteArray();
            return (Throwable)ScriptBytecodeAdapter.castToType(this.t.get(), $get$$class$java$lang$Throwable());
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$ui$Console$_reportException_closure11()) {
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
            var0[0] = "printStackTrace";
            var0[1] = "deepSanitize";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[2];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$ui$Console$_reportException_closure11(), var0);
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
         private static Class $get$$class$groovy$ui$Console$_reportException_closure11() {
            Class var10000 = $class$groovy$ui$Console$_reportException_closure11;
            if (var10000 == null) {
               var10000 = $class$groovy$ui$Console$_reportException_closure11 = class$("groovy.ui.Console$_reportException_closure11");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$java$lang$Throwable() {
            Class var10000 = $class$java$lang$Throwable;
            if (var10000 == null) {
               var10000 = $class$java$lang$Throwable = class$("java.lang.Throwable");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$org$codehaus$groovy$runtime$StackTraceUtils() {
            Class var10000 = $class$org$codehaus$groovy$runtime$StackTraceUtils;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$runtime$StackTraceUtils = class$("org.codehaus.groovy.runtime.StackTraceUtils");
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
      return var3[238].callCurrent(this, (Object)(new GStringImpl(new Object[]{var3[239].callGetProperty(sw)}, new String[]{"\n", "\n"})));
   }

   public Object finishNormal(Object result) {
      CallSite[] var2 = $getCallSiteArray();
      ScriptBytecodeAdapter.setProperty(result, $get$$class$groovy$ui$Console(), var2[240].call(this.history, (Object)$const$6), "result");
      if (ScriptBytecodeAdapter.compareNotEqual(result, (Object)null)) {
         ScriptBytecodeAdapter.setProperty("Execution complete.", $get$$class$groovy$ui$Console(), this.statusLabel, "text");
         var2[241].callCurrent(this, "Result: ", this.promptStyle);
         Object obj = DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.box(this.visualizeScriptResults)) ? var2[242].call($get$$class$groovy$ui$OutputTransforms(), result, var2[243].callGetProperty(var2[244].callGroovyObjectGetProperty(this.shell))) : var2[245].call(result);
         var2[246].callCurrent(this, obj, this.resultStyle);
      } else {
         ScriptBytecodeAdapter.setProperty("Execution complete. Result was null.", $get$$class$groovy$ui$Console(), this.statusLabel, "text");
      }

      var2[247].callCurrent(this);
      if (DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.box(this.detachedOutput))) {
         var2[248].callCurrent(this);
         return var2[249].callCurrent(this);
      } else {
         return null;
      }
   }

   public Object compileFinishNormal() {
      CallSite[] var1 = $getCallSiteArray();
      ScriptBytecodeAdapter.setProperty("Compilation complete.", $get$$class$groovy$ui$Console(), this.statusLabel, "text");
      return "Compilation complete.";
   }

   private Object prepareOutputWindow() {
      CallSite[] var1 = $getCallSiteArray();
      var1[250].call(this.outputArea, (Object)null);
      var1[251].call(this.outputWindow);
      var1[252].call(this.outputArea, (Object)ScriptBytecodeAdapter.createPojoWrapper((Dimension)ScriptBytecodeAdapter.asType(ScriptBytecodeAdapter.createList(new Object[]{var1[253].callCurrent(this, var1[254].call(this.outputWindow), var1[255].call(this.inputEditor), $const$7), var1[256].callCurrent(this, var1[257].call(this.outputWindow), var1[258].call(this.inputEditor), $const$8)}), $get$$class$java$awt$Dimension()), $get$$class$java$awt$Dimension()));
      return var1[259].call(this.outputWindow);
   }

   public Object getLastResult() {
      CallSite[] var1 = $getCallSiteArray();
      if (!DefaultTypeTransformation.booleanUnbox(this.history)) {
         return null;
      } else {
         Object i = null;
         Object var3 = var1[260].call(ScriptBytecodeAdapter.createRange(var1[261].call(var1[262].call(this.history), (Object)$const$1), $const$2, true));

         do {
            if (!((Iterator)var3).hasNext()) {
               return null;
            }

            i = ((Iterator)var3).next();
         } while(!ScriptBytecodeAdapter.compareNotEqual(var1[263].callGetProperty(var1[264].call(this.history, (Object)i)), (Object)null));

         return var1[265].callGetProperty(var1[266].call(this.history, (Object)i));
      }
   }

   public void historyNext(EventObject evt) {
      CallSite[] var2 = $getCallSiteArray();
      if (ScriptBytecodeAdapter.compareLessThan(DefaultTypeTransformation.box(this.historyIndex), var2[267].call(this.history))) {
         var2[268].callCurrent(this, (Object)var2[269].call(DefaultTypeTransformation.box(this.historyIndex), (Object)$const$1));
      } else {
         ScriptBytecodeAdapter.setProperty("Can't go past end of history (time travel not allowed)", $get$$class$groovy$ui$Console(), this.statusLabel, "text");
         var2[270].callCurrent(this);
      }

   }

   public void historyPrev(EventObject evt) {
      CallSite[] var2 = $getCallSiteArray();
      if (ScriptBytecodeAdapter.compareGreaterThan(DefaultTypeTransformation.box(this.historyIndex), $const$2)) {
         var2[271].callCurrent(this, (Object)var2[272].call(DefaultTypeTransformation.box(this.historyIndex), (Object)$const$1));
      } else {
         ScriptBytecodeAdapter.setProperty("Can't go past start of history", $get$$class$groovy$ui$Console(), this.statusLabel, "text");
         var2[273].callCurrent(this);
      }

   }

   public void inspectLast(EventObject evt) {
      CallSite[] var2 = $getCallSiteArray();
      if (ScriptBytecodeAdapter.compareEqual((Object)null, var2[274].callGroovyObjectGetProperty(this))) {
         var2[275].call($get$$class$javax$swing$JOptionPane(), this.frame, "The last result is null.", "Cannot Inspect", var2[276].callGetProperty($get$$class$javax$swing$JOptionPane()));
      } else {
         var2[277].call($get$$class$groovy$inspect$swingui$ObjectBrowser(), (Object)var2[278].callGroovyObjectGetProperty(this));
      }
   }

   public void inspectVariables(EventObject evt) {
      CallSite[] var2 = $getCallSiteArray();
      var2[279].call($get$$class$groovy$inspect$swingui$ObjectBrowser(), (Object)var2[280].callGetProperty(var2[281].callGroovyObjectGetProperty(this.shell)));
   }

   public void inspectAst(EventObject evt) {
      CallSite[] var2 = $getCallSiteArray();
      var2[282].call(var2[283].callConstructor($get$$class$groovy$inspect$swingui$AstBrowser(), this.inputArea, this.rootElement, var2[284].call(this.shell)), (Object)(new GeneratedClosure(this, this) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$groovy$ui$Console$_inspectAst_closure12;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(Object it) {
            CallSite[] var2 = $getCallSiteArray();
            return var2[0].call(var2[1].callGroovyObjectGetProperty(this));
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[2].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$ui$Console$_inspectAst_closure12()) {
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
            var0[0] = "getText";
            var0[1] = "inputArea";
            var0[2] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[3];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$ui$Console$_inspectAst_closure12(), var0);
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
         private static Class $get$$class$groovy$ui$Console$_inspectAst_closure12() {
            Class var10000 = $class$groovy$ui$Console$_inspectAst_closure12;
            if (var10000 == null) {
               var10000 = $class$groovy$ui$Console$_inspectAst_closure12 = class$("groovy.ui.Console$_inspectAst_closure12");
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
         static Class class$(String var0) {
            try {
               return Class.forName(var0);
            } catch (ClassNotFoundException var2) {
               throw new NoClassDefFoundError(var2.getMessage());
            }
         }
      }));
   }

   public void largerFont(EventObject evt) {
      CallSite[] var2 = $getCallSiteArray();
      var2[285].callCurrent(this, (Object)var2[286].call(var2[287].callGetProperty(var2[288].callGetProperty(this.inputArea)), (Object)$const$3));
   }

   public static boolean notifySystemOut(String str) {
      String str = new Reference(str);
      CallSite[] var2 = $getCallSiteArray();
      if (!DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.box(captureStdOut))) {
         return DefaultTypeTransformation.booleanUnbox((Boolean)ScriptBytecodeAdapter.castToType(Boolean.TRUE, $get$$class$java$lang$Boolean()));
      } else {
         if (DefaultTypeTransformation.booleanUnbox(var2[289].call($get$$class$java$awt$EventQueue()))) {
            var2[290].call(consoleControllers, (Object)(new GeneratedClosure($get$$class$groovy$ui$Console(), $get$$class$groovy$ui$Console(), str) {
               private Reference<T> str;
               // $FF: synthetic field
               private static ClassInfo $staticClassInfo;
               // $FF: synthetic field
               private static SoftReference $callSiteArray;
               // $FF: synthetic field
               private static Class $class$java$lang$Object;
               // $FF: synthetic field
               private static Class $class$java$lang$String;
               // $FF: synthetic field
               private static Class $class$groovy$ui$Console$_notifySystemOut_closure13;

               public {
                  CallSite[] var4 = $getCallSiteArray();
                  this.str = (Reference)str;
               }

               public Object doCall(Object it) {
                  Object itx = new Reference(it);
                  CallSite[] var3 = $getCallSiteArray();
                  return var3[0].call(itx.get(), this.str.get(), var3[1].callGetProperty(itx.get()));
               }

               public String getStr() {
                  CallSite[] var1 = $getCallSiteArray();
                  return (String)ScriptBytecodeAdapter.castToType(this.str.get(), $get$$class$java$lang$String());
               }

               public Object doCall() {
                  CallSite[] var1 = $getCallSiteArray();
                  return var1[2].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
               }

               // $FF: synthetic method
               protected MetaClass $getStaticMetaClass() {
                  if (this.getClass() == $get$$class$groovy$ui$Console$_notifySystemOut_closure13()) {
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
                  var0[0] = "appendOutputLines";
                  var0[1] = "outputStyle";
                  var0[2] = "doCall";
               }

               // $FF: synthetic method
               private static CallSiteArray $createCallSiteArray() {
                  String[] var0 = new String[3];
                  $createCallSiteArray_1(var0);
                  return new CallSiteArray($get$$class$groovy$ui$Console$_notifySystemOut_closure13(), var0);
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
               private static Class $get$$class$java$lang$String() {
                  Class var10000 = $class$java$lang$String;
                  if (var10000 == null) {
                     var10000 = $class$java$lang$String = class$("java.lang.String");
                  }

                  return var10000;
               }

               // $FF: synthetic method
               private static Class $get$$class$groovy$ui$Console$_notifySystemOut_closure13() {
                  Class var10000 = $class$groovy$ui$Console$_notifySystemOut_closure13;
                  if (var10000 == null) {
                     var10000 = $class$groovy$ui$Console$_notifySystemOut_closure13 = class$("groovy.ui.Console$_notifySystemOut_closure13");
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
         } else {
            var2[291].call($get$$class$javax$swing$SwingUtilities(), (Object)(new GeneratedClosure($get$$class$groovy$ui$Console(), $get$$class$groovy$ui$Console(), str) {
               private Reference<T> str;
               // $FF: synthetic field
               private static ClassInfo $staticClassInfo;
               // $FF: synthetic field
               private static SoftReference $callSiteArray;
               // $FF: synthetic field
               private static Class $class$java$lang$Object;
               // $FF: synthetic field
               private static Class $class$java$lang$String;
               // $FF: synthetic field
               private static Class $class$groovy$ui$Console$_notifySystemOut_closure14;

               public {
                  CallSite[] var4 = $getCallSiteArray();
                  this.str = (Reference)str;
               }

               public Object doCall(Object it) {
                  CallSite[] var2 = $getCallSiteArray();
                  CallSite var10000 = var2[0];
                  Object var10001 = var2[1].callGroovyObjectGetProperty(this);
                  Object var10005 = this.getThisObject();
                  Reference str = this.str;
                  return var10000.call(var10001, (Object)(new GeneratedClosure(this, var10005, str) {
                     private Reference<T> str;
                     // $FF: synthetic field
                     private static ClassInfo $staticClassInfo;
                     // $FF: synthetic field
                     private static SoftReference $callSiteArray;
                     // $FF: synthetic field
                     private static Class $class$java$lang$Object;
                     // $FF: synthetic field
                     private static Class $class$java$lang$String;
                     // $FF: synthetic field
                     private static Class $class$groovy$ui$Console$_notifySystemOut_closure14_closure29;

                     public {
                        Reference strx = new Reference(str);
                        CallSite[] var5 = $getCallSiteArray();
                        this.str = (Reference)((Reference)strx.get());
                     }

                     public Object doCall(Object it) {
                        Object itx = new Reference(it);
                        CallSite[] var3 = $getCallSiteArray();
                        return var3[0].call(itx.get(), this.str.get(), var3[1].callGetProperty(itx.get()));
                     }

                     public String getStr() {
                        CallSite[] var1 = $getCallSiteArray();
                        return (String)ScriptBytecodeAdapter.castToType(this.str.get(), $get$$class$java$lang$String());
                     }

                     public Object doCall() {
                        CallSite[] var1 = $getCallSiteArray();
                        return var1[2].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
                     }

                     // $FF: synthetic method
                     protected MetaClass $getStaticMetaClass() {
                        if (this.getClass() == $get$$class$groovy$ui$Console$_notifySystemOut_closure14_closure29()) {
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
                        var0[0] = "appendOutputLines";
                        var0[1] = "outputStyle";
                        var0[2] = "doCall";
                     }

                     // $FF: synthetic method
                     private static CallSiteArray $createCallSiteArray() {
                        String[] var0 = new String[3];
                        $createCallSiteArray_1(var0);
                        return new CallSiteArray($get$$class$groovy$ui$Console$_notifySystemOut_closure14_closure29(), var0);
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
                     private static Class $get$$class$java$lang$String() {
                        Class var10000 = $class$java$lang$String;
                        if (var10000 == null) {
                           var10000 = $class$java$lang$String = class$("java.lang.String");
                        }

                        return var10000;
                     }

                     // $FF: synthetic method
                     private static Class $get$$class$groovy$ui$Console$_notifySystemOut_closure14_closure29() {
                        Class var10000 = $class$groovy$ui$Console$_notifySystemOut_closure14_closure29;
                        if (var10000 == null) {
                           var10000 = $class$groovy$ui$Console$_notifySystemOut_closure14_closure29 = class$("groovy.ui.Console$_notifySystemOut_closure14_closure29");
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
               }

               public String getStr() {
                  CallSite[] var1 = $getCallSiteArray();
                  return (String)ScriptBytecodeAdapter.castToType(this.str.get(), $get$$class$java$lang$String());
               }

               public Object doCall() {
                  CallSite[] var1 = $getCallSiteArray();
                  return var1[2].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
               }

               // $FF: synthetic method
               protected MetaClass $getStaticMetaClass() {
                  if (this.getClass() == $get$$class$groovy$ui$Console$_notifySystemOut_closure14()) {
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
                  var0[0] = "each";
                  var0[1] = "consoleControllers";
                  var0[2] = "doCall";
               }

               // $FF: synthetic method
               private static CallSiteArray $createCallSiteArray() {
                  String[] var0 = new String[3];
                  $createCallSiteArray_1(var0);
                  return new CallSiteArray($get$$class$groovy$ui$Console$_notifySystemOut_closure14(), var0);
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
               private static Class $get$$class$java$lang$String() {
                  Class var10000 = $class$java$lang$String;
                  if (var10000 == null) {
                     var10000 = $class$java$lang$String = class$("java.lang.String");
                  }

                  return var10000;
               }

               // $FF: synthetic method
               private static Class $get$$class$groovy$ui$Console$_notifySystemOut_closure14() {
                  Class var10000 = $class$groovy$ui$Console$_notifySystemOut_closure14;
                  if (var10000 == null) {
                     var10000 = $class$groovy$ui$Console$_notifySystemOut_closure14 = class$("groovy.ui.Console$_notifySystemOut_closure14");
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
         }

         return DefaultTypeTransformation.booleanUnbox((Boolean)ScriptBytecodeAdapter.castToType(Boolean.FALSE, $get$$class$java$lang$Boolean()));
      }
   }

   public static boolean notifySystemErr(String str) {
      String str = new Reference(str);
      CallSite[] var2 = $getCallSiteArray();
      if (!DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.box(captureStdErr))) {
         return DefaultTypeTransformation.booleanUnbox((Boolean)ScriptBytecodeAdapter.castToType(Boolean.TRUE, $get$$class$java$lang$Boolean()));
      } else {
         if (DefaultTypeTransformation.booleanUnbox(var2[292].call($get$$class$java$awt$EventQueue()))) {
            var2[293].call(consoleControllers, (Object)(new GeneratedClosure($get$$class$groovy$ui$Console(), $get$$class$groovy$ui$Console(), str) {
               private Reference<T> str;
               // $FF: synthetic field
               private static ClassInfo $staticClassInfo;
               // $FF: synthetic field
               private static SoftReference $callSiteArray;
               // $FF: synthetic field
               private static Class $class$java$lang$Object;
               // $FF: synthetic field
               private static Class $class$java$lang$String;
               // $FF: synthetic field
               private static Class $class$groovy$ui$Console$_notifySystemErr_closure15;

               public {
                  CallSite[] var4 = $getCallSiteArray();
                  this.str = (Reference)str;
               }

               public Object doCall(Object it) {
                  Object itx = new Reference(it);
                  CallSite[] var3 = $getCallSiteArray();
                  return var3[0].call(itx.get(), this.str.get());
               }

               public String getStr() {
                  CallSite[] var1 = $getCallSiteArray();
                  return (String)ScriptBytecodeAdapter.castToType(this.str.get(), $get$$class$java$lang$String());
               }

               public Object doCall() {
                  CallSite[] var1 = $getCallSiteArray();
                  return var1[1].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
               }

               // $FF: synthetic method
               protected MetaClass $getStaticMetaClass() {
                  if (this.getClass() == $get$$class$groovy$ui$Console$_notifySystemErr_closure15()) {
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
                  var0[0] = "appendStacktrace";
                  var0[1] = "doCall";
               }

               // $FF: synthetic method
               private static CallSiteArray $createCallSiteArray() {
                  String[] var0 = new String[2];
                  $createCallSiteArray_1(var0);
                  return new CallSiteArray($get$$class$groovy$ui$Console$_notifySystemErr_closure15(), var0);
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
               private static Class $get$$class$java$lang$String() {
                  Class var10000 = $class$java$lang$String;
                  if (var10000 == null) {
                     var10000 = $class$java$lang$String = class$("java.lang.String");
                  }

                  return var10000;
               }

               // $FF: synthetic method
               private static Class $get$$class$groovy$ui$Console$_notifySystemErr_closure15() {
                  Class var10000 = $class$groovy$ui$Console$_notifySystemErr_closure15;
                  if (var10000 == null) {
                     var10000 = $class$groovy$ui$Console$_notifySystemErr_closure15 = class$("groovy.ui.Console$_notifySystemErr_closure15");
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
         } else {
            var2[294].call($get$$class$javax$swing$SwingUtilities(), (Object)(new GeneratedClosure($get$$class$groovy$ui$Console(), $get$$class$groovy$ui$Console(), str) {
               private Reference<T> str;
               // $FF: synthetic field
               private static ClassInfo $staticClassInfo;
               // $FF: synthetic field
               private static SoftReference $callSiteArray;
               // $FF: synthetic field
               private static Class $class$java$lang$Object;
               // $FF: synthetic field
               private static Class $class$java$lang$String;
               // $FF: synthetic field
               private static Class $class$groovy$ui$Console$_notifySystemErr_closure16;

               public {
                  CallSite[] var4 = $getCallSiteArray();
                  this.str = (Reference)str;
               }

               public Object doCall(Object it) {
                  CallSite[] var2 = $getCallSiteArray();
                  CallSite var10000 = var2[0];
                  Object var10001 = var2[1].callGroovyObjectGetProperty(this);
                  Object var10005 = this.getThisObject();
                  Reference str = this.str;
                  return var10000.call(var10001, (Object)(new GeneratedClosure(this, var10005, str) {
                     private Reference<T> str;
                     // $FF: synthetic field
                     private static ClassInfo $staticClassInfo;
                     // $FF: synthetic field
                     private static SoftReference $callSiteArray;
                     // $FF: synthetic field
                     private static Class $class$groovy$ui$Console$_notifySystemErr_closure16_closure30;
                     // $FF: synthetic field
                     private static Class $class$java$lang$Object;
                     // $FF: synthetic field
                     private static Class $class$java$lang$String;

                     public {
                        Reference strx = new Reference(str);
                        CallSite[] var5 = $getCallSiteArray();
                        this.str = (Reference)((Reference)strx.get());
                     }

                     public Object doCall(Object it) {
                        Object itx = new Reference(it);
                        CallSite[] var3 = $getCallSiteArray();
                        return var3[0].call(itx.get(), this.str.get());
                     }

                     public String getStr() {
                        CallSite[] var1 = $getCallSiteArray();
                        return (String)ScriptBytecodeAdapter.castToType(this.str.get(), $get$$class$java$lang$String());
                     }

                     public Object doCall() {
                        CallSite[] var1 = $getCallSiteArray();
                        return var1[1].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
                     }

                     // $FF: synthetic method
                     protected MetaClass $getStaticMetaClass() {
                        if (this.getClass() == $get$$class$groovy$ui$Console$_notifySystemErr_closure16_closure30()) {
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
                        var0[0] = "appendStacktrace";
                        var0[1] = "doCall";
                     }

                     // $FF: synthetic method
                     private static CallSiteArray $createCallSiteArray() {
                        String[] var0 = new String[2];
                        $createCallSiteArray_1(var0);
                        return new CallSiteArray($get$$class$groovy$ui$Console$_notifySystemErr_closure16_closure30(), var0);
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
                     private static Class $get$$class$groovy$ui$Console$_notifySystemErr_closure16_closure30() {
                        Class var10000 = $class$groovy$ui$Console$_notifySystemErr_closure16_closure30;
                        if (var10000 == null) {
                           var10000 = $class$groovy$ui$Console$_notifySystemErr_closure16_closure30 = class$("groovy.ui.Console$_notifySystemErr_closure16_closure30");
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
               }

               public String getStr() {
                  CallSite[] var1 = $getCallSiteArray();
                  return (String)ScriptBytecodeAdapter.castToType(this.str.get(), $get$$class$java$lang$String());
               }

               public Object doCall() {
                  CallSite[] var1 = $getCallSiteArray();
                  return var1[2].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
               }

               // $FF: synthetic method
               protected MetaClass $getStaticMetaClass() {
                  if (this.getClass() == $get$$class$groovy$ui$Console$_notifySystemErr_closure16()) {
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
                  var0[0] = "each";
                  var0[1] = "consoleControllers";
                  var0[2] = "doCall";
               }

               // $FF: synthetic method
               private static CallSiteArray $createCallSiteArray() {
                  String[] var0 = new String[3];
                  $createCallSiteArray_1(var0);
                  return new CallSiteArray($get$$class$groovy$ui$Console$_notifySystemErr_closure16(), var0);
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
               private static Class $get$$class$java$lang$String() {
                  Class var10000 = $class$java$lang$String;
                  if (var10000 == null) {
                     var10000 = $class$java$lang$String = class$("java.lang.String");
                  }

                  return var10000;
               }

               // $FF: synthetic method
               private static Class $get$$class$groovy$ui$Console$_notifySystemErr_closure16() {
                  Class var10000 = $class$groovy$ui$Console$_notifySystemErr_closure16;
                  if (var10000 == null) {
                     var10000 = $class$groovy$ui$Console$_notifySystemErr_closure16 = class$("groovy.ui.Console$_notifySystemErr_closure16");
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
         }

         return DefaultTypeTransformation.booleanUnbox((Boolean)ScriptBytecodeAdapter.castToType(Boolean.FALSE, $get$$class$java$lang$Boolean()));
      }
   }

   public void runScript(EventObject evt) {
      CallSite[] var2 = $getCallSiteArray();
      var2[295].callCurrent(this, (Object)Boolean.FALSE);
   }

   public void runSelectedScript(EventObject evt) {
      CallSite[] var2 = $getCallSiteArray();
      var2[296].callCurrent(this, (Object)Boolean.TRUE);
   }

   public void addClasspathJar(EventObject evt) {
      CallSite[] var2 = $getCallSiteArray();
      Object fc = var2[297].callConstructor($get$$class$javax$swing$JFileChooser(), (Object)this.currentClasspathJarDir);
      ScriptBytecodeAdapter.setProperty(var2[298].callGetProperty($get$$class$javax$swing$JFileChooser()), $get$$class$groovy$ui$Console(), fc, "fileSelectionMode");
      ScriptBytecodeAdapter.setProperty(Boolean.TRUE, $get$$class$groovy$ui$Console(), fc, "acceptAllFileFilterUsed");
      if (ScriptBytecodeAdapter.compareEqual(var2[299].call(fc, this.frame, "Add"), var2[300].callGetProperty($get$$class$javax$swing$JFileChooser()))) {
         this.currentClasspathJarDir = (File)ScriptBytecodeAdapter.castToType((File)ScriptBytecodeAdapter.castToType(var2[301].callGetProperty(fc), $get$$class$java$io$File()), $get$$class$java$io$File());
         var2[302].call(var2[303].call($get$$class$java$util$prefs$Preferences(), (Object)$get$$class$groovy$ui$Console()), "currentClasspathJarDir", var2[304].callGetProperty(this.currentClasspathJarDir));
         var2[305].call(var2[306].call(this.shell), var2[307].call(var2[308].callGetProperty(fc)));
      }

   }

   public void addClasspathDir(EventObject evt) {
      CallSite[] var2 = $getCallSiteArray();
      Object fc = var2[309].callConstructor($get$$class$javax$swing$JFileChooser(), (Object)this.currentClasspathDir);
      ScriptBytecodeAdapter.setProperty(var2[310].callGetProperty($get$$class$javax$swing$JFileChooser()), $get$$class$groovy$ui$Console(), fc, "fileSelectionMode");
      ScriptBytecodeAdapter.setProperty(Boolean.TRUE, $get$$class$groovy$ui$Console(), fc, "acceptAllFileFilterUsed");
      if (ScriptBytecodeAdapter.compareEqual(var2[311].call(fc, this.frame, "Add"), var2[312].callGetProperty($get$$class$javax$swing$JFileChooser()))) {
         this.currentClasspathDir = (File)ScriptBytecodeAdapter.castToType((File)ScriptBytecodeAdapter.castToType(var2[313].callGetProperty(fc), $get$$class$java$io$File()), $get$$class$java$io$File());
         var2[314].call(var2[315].call($get$$class$java$util$prefs$Preferences(), (Object)$get$$class$groovy$ui$Console()), "currentClasspathDir", var2[316].callGetProperty(this.currentClasspathDir));
         var2[317].call(var2[318].call(this.shell), var2[319].call(var2[320].callGetProperty(fc)));
      }

   }

   public void clearContext(EventObject evt) {
      CallSite[] var2 = $getCallSiteArray();
      Object binding = var2[321].callConstructor($get$$class$groovy$lang$Binding());
      var2[322].callCurrent(this, (Object)null, binding);
      ScriptBytecodeAdapter.setProperty(var2[323].call($get$$class$groovy$ui$OutputTransforms()), $get$$class$groovy$ui$Console(), var2[324].callGetProperty(binding), "_outputTransforms");
   }

   private void runScriptImpl(boolean selected) {
      Boolean selected = new Reference(DefaultTypeTransformation.box(selected));
      CallSite[] var3 = $getCallSiteArray();
      if (DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.box(this.scriptRunning))) {
         ScriptBytecodeAdapter.setProperty("Cannot run script now as a script is already running. Please wait or use \"Interrupt Script\" option.", $get$$class$groovy$ui$Console(), this.statusLabel, "text");
      } else {
         this.scriptRunning = DefaultTypeTransformation.booleanUnbox(Boolean.TRUE);
         ScriptBytecodeAdapter.setProperty(Boolean.TRUE, $get$$class$groovy$ui$Console(), this.interruptAction, "enabled");
         this.stackOverFlowError = DefaultTypeTransformation.booleanUnbox(Boolean.FALSE);
         Object endLine = var3[325].call($get$$class$java$lang$System(), (Object)"line.separator");
         Object record = new Reference(var3[326].callConstructor($get$$class$groovy$ui$HistoryRecord(), (Object)ScriptBytecodeAdapter.createMap(new Object[]{"allText", var3[327].call(var3[328].call(this.inputArea), endLine, "\n"), "selectionStart", DefaultTypeTransformation.box(this.textSelectionStart), "selectionEnd", DefaultTypeTransformation.box(this.textSelectionEnd)})));
         var3[329].callCurrent(this, (Object)record.get());
         this.pendingRecord = (HistoryRecord)ScriptBytecodeAdapter.castToType(var3[330].callConstructor($get$$class$groovy$ui$HistoryRecord(), (Object)ScriptBytecodeAdapter.createMap(new Object[]{"allText", "", "selectionStart", $const$2, "selectionEnd", $const$2})), $get$$class$groovy$ui$HistoryRecord());
         if (DefaultTypeTransformation.booleanUnbox(var3[331].call(prefs, "autoClearOutput", Boolean.FALSE))) {
            var3[332].callCurrent(this);
         }

         if (DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.box(this.showScriptInOutput))) {
            Object line = null;
            Object var7 = var3[333].call(var3[334].call(var3[335].call(record.get(), selected.get()), (Object)"\n"));

            while(((Iterator)var7).hasNext()) {
               line = ((Iterator)var7).next();
               var3[336].callCurrent(this, "groovy> ", this.promptStyle);
               var3[337].callCurrent(this, line, this.commandStyle);
            }

            var3[338].callCurrent(this, " \n", this.promptStyle);
         }

         this.runThread = (Thread)ScriptBytecodeAdapter.castToType((Thread)ScriptBytecodeAdapter.castToType(var3[339].call($get$$class$java$lang$Thread(), (Object)(new GeneratedClosure(this, this, record, selected) {
            private Reference<T> record;
            private Reference<T> selected;
            // $FF: synthetic field
            private static ClassInfo $staticClassInfo;
            // $FF: synthetic field
            private static SoftReference $callSiteArray;
            // $FF: synthetic field
            private static Class $class$java$lang$Object;
            // $FF: synthetic field
            private static Class $class$java$lang$Boolean;
            // $FF: synthetic field
            private static Class $class$javax$swing$SwingUtilities;
            // $FF: synthetic field
            private static Class $class$java$lang$Thread;
            // $FF: synthetic field
            private static Class $class$java$lang$String;
            // $FF: synthetic field
            private static Class $class$groovy$ui$Console$_runScriptImpl_closure17;

            public {
               CallSite[] var5 = $getCallSiteArray();
               this.record = (Reference)record;
               this.selected = (Reference)selected;
            }

            public Object doCall(Object param1) {
               // $FF: Couldn't be decompiled
            }

            public Object getRecord() {
               CallSite[] var1 = $getCallSiteArray();
               return this.record.get();
            }

            public boolean getSelected() {
               CallSite[] var1 = $getCallSiteArray();
               return DefaultTypeTransformation.booleanUnbox((Boolean)ScriptBytecodeAdapter.castToType(this.selected.get(), $get$$class$java$lang$Boolean()));
            }

            public Object doCall() {
               CallSite[] var1 = $getCallSiteArray();
               return var1[22].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
            }

            // $FF: synthetic method
            protected MetaClass $getStaticMetaClass() {
               if (this.getClass() == $get$$class$groovy$ui$Console$_runScriptImpl_closure17()) {
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
               var0[0] = "invokeLater";
               var0[1] = "name";
               var0[2] = "scriptFile";
               var0[3] = "plus";
               var0[4] = "DEFAULT_SCRIPT_NAME_START";
               var0[5] = "scriptNameCounter";
               var0[6] = "next";
               var0[7] = "scriptNameCounter";
               var0[8] = "beforeExecution";
               var0[9] = "beforeExecution";
               var0[10] = "run";
               var0[11] = "shell";
               var0[12] = "getTextToRun";
               var0[13] = "afterExecution";
               var0[14] = "afterExecution";
               var0[15] = "invokeLater";
               var0[16] = "interruptAction";
               var0[17] = "clearOutput";
               var0[18] = "invokeLater";
               var0[19] = "interruptAction";
               var0[20] = "interruptAction";
               var0[21] = "interruptAction";
               var0[22] = "doCall";
            }

            // $FF: synthetic method
            private static CallSiteArray $createCallSiteArray() {
               String[] var0 = new String[23];
               $createCallSiteArray_1(var0);
               return new CallSiteArray($get$$class$groovy$ui$Console$_runScriptImpl_closure17(), var0);
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
            private static Class $get$$class$java$lang$Boolean() {
               Class var10000 = $class$java$lang$Boolean;
               if (var10000 == null) {
                  var10000 = $class$java$lang$Boolean = class$("java.lang.Boolean");
               }

               return var10000;
            }

            // $FF: synthetic method
            private static Class $get$$class$javax$swing$SwingUtilities() {
               Class var10000 = $class$javax$swing$SwingUtilities;
               if (var10000 == null) {
                  var10000 = $class$javax$swing$SwingUtilities = class$("javax.swing.SwingUtilities");
               }

               return var10000;
            }

            // $FF: synthetic method
            private static Class $get$$class$java$lang$Thread() {
               Class var10000 = $class$java$lang$Thread;
               if (var10000 == null) {
                  var10000 = $class$java$lang$Thread = class$("java.lang.Thread");
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
            private static Class $get$$class$groovy$ui$Console$_runScriptImpl_closure17() {
               Class var10000 = $class$groovy$ui$Console$_runScriptImpl_closure17;
               if (var10000 == null) {
                  var10000 = $class$groovy$ui$Console$_runScriptImpl_closure17 = class$("groovy.ui.Console$_runScriptImpl_closure17");
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
         })), $get$$class$java$lang$Thread()), $get$$class$java$lang$Thread());
      }
   }

   public void compileScript(EventObject evt) {
      CallSite[] var2 = $getCallSiteArray();
      if (DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.box(this.scriptRunning))) {
         ScriptBytecodeAdapter.setProperty("Cannot compile script now as a script is already running. Please wait or use \"Interrupt Script\" option.", $get$$class$groovy$ui$Console(), this.statusLabel, "text");
      } else {
         this.stackOverFlowError = DefaultTypeTransformation.booleanUnbox(Boolean.FALSE);
         Object endLine = var2[340].call($get$$class$java$lang$System(), (Object)"line.separator");
         Object record = new Reference(var2[341].callConstructor($get$$class$groovy$ui$HistoryRecord(), (Object)ScriptBytecodeAdapter.createMap(new Object[]{"allText", var2[342].call(var2[343].call(this.inputArea), endLine, "\n"), "selectionStart", DefaultTypeTransformation.box(this.textSelectionStart), "selectionEnd", DefaultTypeTransformation.box(this.textSelectionEnd)})));
         if (DefaultTypeTransformation.booleanUnbox(var2[344].call(prefs, "autoClearOutput", Boolean.FALSE))) {
            var2[345].callCurrent(this);
         }

         if (DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.box(this.showScriptInOutput))) {
            Object line = null;
            Object var6 = var2[346].call(var2[347].call(var2[348].callGetProperty(record.get()), (Object)"\n"));

            while(((Iterator)var6).hasNext()) {
               line = ((Iterator)var6).next();
               var2[349].callCurrent(this, "groovy> ", this.promptStyle);
               var2[350].callCurrent(this, line, this.commandStyle);
            }

            var2[351].callCurrent(this, " \n", this.promptStyle);
         }

         this.runThread = (Thread)ScriptBytecodeAdapter.castToType((Thread)ScriptBytecodeAdapter.castToType(var2[352].call($get$$class$java$lang$Thread(), (Object)(new GeneratedClosure(this, this, record) {
            private Reference<T> record;
            // $FF: synthetic field
            private static ClassInfo $staticClassInfo;
            // $FF: synthetic field
            private static SoftReference $callSiteArray;
            // $FF: synthetic field
            private static Class $class$java$lang$Object;
            // $FF: synthetic field
            private static Class $class$javax$swing$SwingUtilities;
            // $FF: synthetic field
            private static Class $class$java$lang$Thread;
            // $FF: synthetic field
            private static Class $class$groovy$ui$Console$_compileScript_closure18;

            public {
               CallSite[] var4 = $getCallSiteArray();
               this.record = (Reference)record;
            }

            public Object doCall(Object param1) {
               // $FF: Couldn't be decompiled
            }

            public Object getRecord() {
               CallSite[] var1 = $getCallSiteArray();
               return this.record.get();
            }

            public Object doCall() {
               CallSite[] var1 = $getCallSiteArray();
               return var1[6].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
            }

            // $FF: synthetic method
            protected MetaClass $getStaticMetaClass() {
               if (this.getClass() == $get$$class$groovy$ui$Console$_compileScript_closure18()) {
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
               var0[0] = "invokeLater";
               var0[1] = "parse";
               var0[2] = "shell";
               var0[3] = "allText";
               var0[4] = "invokeLater";
               var0[5] = "invokeLater";
               var0[6] = "doCall";
            }

            // $FF: synthetic method
            private static CallSiteArray $createCallSiteArray() {
               String[] var0 = new String[7];
               $createCallSiteArray_1(var0);
               return new CallSiteArray($get$$class$groovy$ui$Console$_compileScript_closure18(), var0);
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
            private static Class $get$$class$javax$swing$SwingUtilities() {
               Class var10000 = $class$javax$swing$SwingUtilities;
               if (var10000 == null) {
                  var10000 = $class$javax$swing$SwingUtilities = class$("javax.swing.SwingUtilities");
               }

               return var10000;
            }

            // $FF: synthetic method
            private static Class $get$$class$java$lang$Thread() {
               Class var10000 = $class$java$lang$Thread;
               if (var10000 == null) {
                  var10000 = $class$java$lang$Thread = class$("java.lang.Thread");
               }

               return var10000;
            }

            // $FF: synthetic method
            private static Class $get$$class$groovy$ui$Console$_compileScript_closure18() {
               Class var10000 = $class$groovy$ui$Console$_compileScript_closure18;
               if (var10000 == null) {
                  var10000 = $class$groovy$ui$Console$_compileScript_closure18 = class$("groovy.ui.Console$_compileScript_closure18");
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
         })), $get$$class$java$lang$Thread()), $get$$class$java$lang$Thread());
      }
   }

   public Object selectFilename(Object name) {
      CallSite[] var2 = $getCallSiteArray();
      Object fc = var2[353].callConstructor($get$$class$javax$swing$JFileChooser(), (Object)this.currentFileChooserDir);
      ScriptBytecodeAdapter.setProperty(var2[354].callGetProperty($get$$class$javax$swing$JFileChooser()), $get$$class$groovy$ui$Console(), fc, "fileSelectionMode");
      ScriptBytecodeAdapter.setProperty(Boolean.TRUE, $get$$class$groovy$ui$Console(), fc, "acceptAllFileFilterUsed");
      ScriptBytecodeAdapter.setProperty(groovyFileFilter, $get$$class$groovy$ui$Console(), fc, "fileFilter");
      if (ScriptBytecodeAdapter.compareEqual(name, "Save")) {
         ScriptBytecodeAdapter.setProperty(var2[355].callConstructor($get$$class$java$io$File(), (Object)"*.groovy"), $get$$class$groovy$ui$Console(), fc, "selectedFile");
      }

      if (ScriptBytecodeAdapter.compareEqual(var2[356].call(fc, this.frame, name), var2[357].callGetProperty($get$$class$javax$swing$JFileChooser()))) {
         this.currentFileChooserDir = (File)ScriptBytecodeAdapter.castToType((File)ScriptBytecodeAdapter.castToType(var2[358].callGetProperty(fc), $get$$class$java$io$File()), $get$$class$java$io$File());
         var2[359].call(var2[360].call($get$$class$java$util$prefs$Preferences(), (Object)$get$$class$groovy$ui$Console()), "currentFileChooserDir", var2[361].callGetProperty(this.currentFileChooserDir));
         return var2[362].callGetProperty(fc);
      } else {
         return null;
      }
   }

   public void setDirty(boolean newDirty) {
      CallSite[] var2 = $getCallSiteArray();
      this.dirty = DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.box(newDirty));
      ScriptBytecodeAdapter.setProperty(DefaultTypeTransformation.box(newDirty), $get$$class$groovy$ui$Console(), this.saveAction, "enabled");
      var2[363].callCurrent(this);
   }

   private void setInputTextFromHistory(Object newIndex) {
      CallSite[] var2 = $getCallSiteArray();
      Object endLine = var2[364].call($get$$class$java$lang$System(), (Object)"line.separator");
      if (ScriptBytecodeAdapter.compareGreaterThanEqual(DefaultTypeTransformation.box(this.historyIndex), var2[365].call(this.history))) {
         this.pendingRecord = (HistoryRecord)ScriptBytecodeAdapter.castToType(var2[366].callConstructor($get$$class$groovy$ui$HistoryRecord(), (Object)ScriptBytecodeAdapter.createMap(new Object[]{"allText", var2[367].call(var2[368].call(this.inputArea), endLine, "\n"), "selectionStart", DefaultTypeTransformation.box(this.textSelectionStart), "selectionEnd", DefaultTypeTransformation.box(this.textSelectionEnd)})), $get$$class$groovy$ui$HistoryRecord());
      }

      this.historyIndex = DefaultTypeTransformation.intUnbox(newIndex);
      Object record = null;
      if (ScriptBytecodeAdapter.compareLessThan(DefaultTypeTransformation.box(this.historyIndex), var2[369].call(this.history))) {
         record = var2[370].call(this.history, (Object)DefaultTypeTransformation.box(this.historyIndex));
         ScriptBytecodeAdapter.setProperty(new GStringImpl(new Object[]{var2[371].call(var2[372].call(this.history), DefaultTypeTransformation.box(this.historyIndex))}, new String[]{"command history ", ""}), $get$$class$groovy$ui$Console(), this.statusLabel, "text");
      } else {
         record = this.pendingRecord;
         ScriptBytecodeAdapter.setProperty("at end of history", $get$$class$groovy$ui$Console(), this.statusLabel, "text");
      }

      ScriptBytecodeAdapter.setProperty(var2[373].callGetProperty(record), $get$$class$groovy$ui$Console(), this.inputArea, "text");
      ScriptBytecodeAdapter.setProperty(var2[374].callGetProperty(record), $get$$class$groovy$ui$Console(), this.inputArea, "selectionStart");
      ScriptBytecodeAdapter.setProperty(var2[375].callGetProperty(record), $get$$class$groovy$ui$Console(), this.inputArea, "selectionEnd");
      var2[376].callCurrent(this, (Object)Boolean.TRUE);
      var2[377].callCurrent(this);
   }

   private void updateHistoryActions() {
      CallSite[] var1 = $getCallSiteArray();
      ScriptBytecodeAdapter.setProperty(ScriptBytecodeAdapter.compareLessThan(DefaultTypeTransformation.box(this.historyIndex), var1[378].call(this.history)) ? Boolean.TRUE : Boolean.FALSE, $get$$class$groovy$ui$Console(), this.nextHistoryAction, "enabled");
      ScriptBytecodeAdapter.setProperty(ScriptBytecodeAdapter.compareGreaterThan(DefaultTypeTransformation.box(this.historyIndex), $const$2) ? Boolean.TRUE : Boolean.FALSE, $get$$class$groovy$ui$Console(), this.prevHistoryAction, "enabled");
   }

   public void setVariable(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      var3[379].call(var3[380].callGroovyObjectGetProperty(this.shell), name, value);
   }

   public void showAbout(EventObject evt) {
      CallSite[] var2 = $getCallSiteArray();
      Object version = var2[381].call($get$$class$org$codehaus$groovy$runtime$InvokerHelper());
      Object pane = var2[382].call(this.swing);
      var2[383].call(pane, var2[384].call("Welcome to the Groovy Console for evaluating Groovy scripts\nVersion ", (Object)version));
      Object dialog = var2[385].call(pane, this.frame, "About GroovyConsole");
      var2[386].call(dialog);
   }

   public void find(EventObject evt) {
      CallSite[] var2 = $getCallSiteArray();
      var2[387].call($get$$class$groovy$ui$text$FindReplaceUtility());
   }

   public void findNext(EventObject evt) {
      CallSite[] var2 = $getCallSiteArray();
      var2[388].call(var2[389].callGetProperty($get$$class$groovy$ui$text$FindReplaceUtility()), (Object)evt);
   }

   public void findPrevious(EventObject evt) {
      CallSite[] var2 = $getCallSiteArray();
      Object reverseEvt = var2[390].callConstructor($get$$class$java$awt$event$ActionEvent(), (Object[])ArrayUtil.createArray(var2[391].call(evt), var2[392].call(evt), var2[393].call(evt), var2[394].call(evt), var2[395].callGetProperty($get$$class$java$awt$event$ActionEvent())));
      var2[396].call(var2[397].callGetProperty($get$$class$groovy$ui$text$FindReplaceUtility()), reverseEvt);
   }

   public void replace(EventObject evt) {
      CallSite[] var2 = $getCallSiteArray();
      var2[398].call($get$$class$groovy$ui$text$FindReplaceUtility(), (Object)Boolean.TRUE);
   }

   public void showExecutingMessage() {
      CallSite[] var1 = $getCallSiteArray();
      ScriptBytecodeAdapter.setProperty("Script executing now. Please wait or use \"Interrupt Script\" option.", $get$$class$groovy$ui$Console(), this.statusLabel, "text");
   }

   public void showCompilingMessage() {
      CallSite[] var1 = $getCallSiteArray();
      ScriptBytecodeAdapter.setProperty("Script compiling now. Please wait.", $get$$class$groovy$ui$Console(), this.statusLabel, "text");
   }

   public void showOutputWindow(EventObject evt) {
      CallSite[] var2 = $getCallSiteArray();
      if (DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.box(this.detachedOutput))) {
         var2[399].call(this.outputWindow, (Object)this.frame);
         var2[400].call(this.outputWindow);
      }

   }

   public void hideOutputWindow(EventObject evt) {
      CallSite[] var2 = $getCallSiteArray();
      if (DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.box(this.detachedOutput))) {
         ScriptBytecodeAdapter.setProperty(Boolean.FALSE, $get$$class$groovy$ui$Console(), this.outputWindow, "visible");
      }

   }

   public void hideAndClearOutputWindow(EventObject evt) {
      CallSite[] var2 = $getCallSiteArray();
      var2[401].callCurrent(this);
      var2[402].callCurrent(this);
   }

   public void smallerFont(EventObject evt) {
      CallSite[] var2 = $getCallSiteArray();
      var2[403].callCurrent(this, (Object)var2[404].call(var2[405].callGetProperty(var2[406].callGetProperty(this.inputArea)), (Object)$const$3));
   }

   public void updateTitle() {
      CallSite[] var1 = $getCallSiteArray();
      if (DefaultTypeTransformation.booleanUnbox(var1[407].call(var1[408].callGetProperty(this.frame), (Object)"title"))) {
         if (ScriptBytecodeAdapter.compareNotEqual(this.scriptFile, (Object)null)) {
            ScriptBytecodeAdapter.setProperty(var1[409].call(var1[410].call(var1[411].callGetProperty(this.scriptFile), (Object)(DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.box(this.dirty)) ? " * " : "")), (Object)" - GroovyConsole"), $get$$class$groovy$ui$Console(), this.frame, "title");
         } else {
            ScriptBytecodeAdapter.setProperty("GroovyConsole", $get$$class$groovy$ui$Console(), this.frame, "title");
         }
      }

   }

   private Object updateFontSize(Object newFontSize) {
      CallSite[] var2 = $getCallSiteArray();
      if (ScriptBytecodeAdapter.compareGreaterThan(newFontSize, $const$9)) {
         newFontSize = $const$9;
      } else if (ScriptBytecodeAdapter.compareLessThan(newFontSize, $const$10)) {
         newFontSize = $const$10;
      }

      var2[412].call(prefs, "fontSize", newFontSize);
      Object newFont = var2[413].callConstructor($get$$class$java$awt$Font(), var2[414].callGetProperty(this.inputEditor), var2[415].callGetProperty($get$$class$java$awt$Font()), newFontSize);
      ScriptBytecodeAdapter.setProperty(newFont, $get$$class$groovy$ui$Console(), this.inputArea, "font");
      ScriptBytecodeAdapter.setProperty(newFont, $get$$class$groovy$ui$Console(), this.outputArea, "font");
      return newFont;
   }

   public void invokeTextAction(Object evt, Object closure, Object area) {
      CallSite[] var4 = $getCallSiteArray();
      Object source = var4[416].call(evt);
      if (ScriptBytecodeAdapter.compareNotEqual(source, (Object)null)) {
         var4[417].call(closure, area);
      }

   }

   public void cut(EventObject evt) {
      CallSite[] var2 = $getCallSiteArray();
      var2[418].callCurrent(this, evt, new GeneratedClosure(this, this) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$groovy$ui$Console$_cut_closure19;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(Object source) {
            Object sourcex = new Reference(source);
            CallSite[] var3 = $getCallSiteArray();
            return var3[0].call(sourcex.get());
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$ui$Console$_cut_closure19()) {
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
            var0[0] = "cut";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[1];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$ui$Console$_cut_closure19(), var0);
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
         private static Class $get$$class$groovy$ui$Console$_cut_closure19() {
            Class var10000 = $class$groovy$ui$Console$_cut_closure19;
            if (var10000 == null) {
               var10000 = $class$groovy$ui$Console$_cut_closure19 = class$("groovy.ui.Console$_cut_closure19");
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
   }

   public void copy(EventObject evt) {
      CallSite[] var2 = $getCallSiteArray();
      CallSite var10000 = var2[419];
      GeneratedClosure var10003 = new GeneratedClosure(this, this) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$groovy$ui$Console$_copy_closure20;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(Object source) {
            Object sourcex = new Reference(source);
            CallSite[] var3 = $getCallSiteArray();
            return var3[0].call(sourcex.get());
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$ui$Console$_copy_closure20()) {
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
            var0[0] = "copy";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[1];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$ui$Console$_copy_closure20(), var0);
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
         private static Class $get$$class$groovy$ui$Console$_copy_closure20() {
            Class var10000 = $class$groovy$ui$Console$_copy_closure20;
            if (var10000 == null) {
               var10000 = $class$groovy$ui$Console$_copy_closure20 = class$("groovy.ui.Console$_copy_closure20");
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
      };
      Object var10004 = this.copyFromComponent;
      if (!DefaultTypeTransformation.booleanUnbox(var10004)) {
         var10004 = this.inputArea;
      }

      var10000.callCurrent(this, evt, var10003, var10004);
   }

   public void paste(EventObject evt) {
      CallSite[] var2 = $getCallSiteArray();
      var2[420].callCurrent(this, evt, new GeneratedClosure(this, this) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$groovy$ui$Console$_paste_closure21;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(Object source) {
            Object sourcex = new Reference(source);
            CallSite[] var3 = $getCallSiteArray();
            return var3[0].call(sourcex.get());
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$ui$Console$_paste_closure21()) {
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
            var0[0] = "paste";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[1];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$ui$Console$_paste_closure21(), var0);
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
         private static Class $get$$class$groovy$ui$Console$_paste_closure21() {
            Class var10000 = $class$groovy$ui$Console$_paste_closure21;
            if (var10000 == null) {
               var10000 = $class$groovy$ui$Console$_paste_closure21 = class$("groovy.ui.Console$_paste_closure21");
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
   }

   public void selectAll(EventObject evt) {
      CallSite[] var2 = $getCallSiteArray();
      var2[421].callCurrent(this, evt, new GeneratedClosure(this, this) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$groovy$ui$Console$_selectAll_closure22;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(Object source) {
            Object sourcex = new Reference(source);
            CallSite[] var3 = $getCallSiteArray();
            return var3[0].call(sourcex.get());
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$ui$Console$_selectAll_closure22()) {
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
            var0[0] = "selectAll";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[1];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$ui$Console$_selectAll_closure22(), var0);
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
         private static Class $get$$class$groovy$ui$Console$_selectAll_closure22() {
            Class var10000 = $class$groovy$ui$Console$_selectAll_closure22;
            if (var10000 == null) {
               var10000 = $class$groovy$ui$Console$_selectAll_closure22 = class$("groovy.ui.Console$_selectAll_closure22");
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
   }

   public void setRowNumAndColNum() {
      CallSite[] var1 = $getCallSiteArray();
      this.cursorPos = DefaultTypeTransformation.intUnbox(var1[422].call(this.inputArea));
      this.rowNum = DefaultTypeTransformation.intUnbox(var1[423].call(var1[424].call(this.rootElement, (Object)DefaultTypeTransformation.box(this.cursorPos)), (Object)$const$1));
      Object rowElement = var1[425].call(this.rootElement, (Object)var1[426].call(DefaultTypeTransformation.box(this.rowNum), (Object)$const$1));
      this.colNum = DefaultTypeTransformation.intUnbox(var1[427].call(var1[428].call(DefaultTypeTransformation.box(this.cursorPos), var1[429].call(rowElement)), (Object)$const$1));
      var1[430].call(this.rowNumAndColNum, (Object)(new GStringImpl(new Object[]{DefaultTypeTransformation.box(this.rowNum), DefaultTypeTransformation.box(this.colNum)}, new String[]{"", ":", ""})));
   }

   public void print(EventObject evt) {
      CallSite[] var2 = $getCallSiteArray();
      var2[431].call(var2[432].callGetProperty(this.inputEditor), (Object)evt);
   }

   public void undo(EventObject evt) {
      CallSite[] var2 = $getCallSiteArray();
      var2[433].call(var2[434].callGetProperty(this.inputEditor), (Object)evt);
   }

   public void redo(EventObject evt) {
      CallSite[] var2 = $getCallSiteArray();
      var2[435].call(var2[436].callGetProperty(this.inputEditor), (Object)evt);
   }

   public void hyperlinkUpdate(HyperlinkEvent e) {
      CallSite[] var2 = $getCallSiteArray();
      if (ScriptBytecodeAdapter.compareEqual(var2[437].callGetProperty(e), var2[438].callGetProperty($get$$class$javax$swing$event$HyperlinkEvent$EventType()))) {
         String url = (String)ScriptBytecodeAdapter.castToType(var2[439].call(e), $get$$class$java$lang$String());
         Integer lineNumber = (Integer)ScriptBytecodeAdapter.castToType(var2[440].call(var2[441].call(url, (Object)ScriptBytecodeAdapter.createRange(var2[442].call(var2[443].call(url, (Object)":"), (Object)$const$1), $const$6, true))), $get$$class$java$lang$Integer());
         Object editor = var2[444].callGetProperty(this.inputEditor);
         Object text = var2[445].callGetProperty(editor);
         Integer newlineBefore = $const$2;
         Integer newlineAfter = $const$2;
         Integer currentLineNumber = $const$1;
         Integer i = $const$2;
         Object ch = null;

         for(Object var12 = var2[446].call(text); ((Iterator)var12).hasNext(); i = var2[451].call(i)) {
            ch = ((Iterator)var12).next();
            if (ScriptBytecodeAdapter.compareEqual(ch, "\n")) {
               currentLineNumber = var2[447].call(currentLineNumber);
            }

            if (ScriptBytecodeAdapter.compareEqual(currentLineNumber, lineNumber)) {
               newlineBefore = i;
               Object nextNewline = var2[448].call(text, "\n", var2[449].call(i, (Object)$const$1));
               newlineAfter = (Integer)ScriptBytecodeAdapter.castToType(ScriptBytecodeAdapter.compareGreaterThan(nextNewline, $const$6) ? nextNewline : var2[450].call(text), $get$$class$java$lang$Integer());
               break;
            }
         }

         var2[452].call(editor, newlineBefore);
         var2[453].call(editor, (Object)newlineAfter);
      }

   }

   public void componentHidden(ComponentEvent e) {
      CallSite[] var2 = $getCallSiteArray();
   }

   public void componentMoved(ComponentEvent e) {
      CallSite[] var2 = $getCallSiteArray();
   }

   public void componentResized(ComponentEvent e) {
      CallSite[] var2 = $getCallSiteArray();
      Object component = var2[454].call(e);
      var2[455].call(prefs, new GStringImpl(new Object[]{var2[456].callGetProperty(component)}, new String[]{"", "Width"}), var2[457].callGetProperty(component));
      var2[458].call(prefs, new GStringImpl(new Object[]{var2[459].callGetProperty(component)}, new String[]{"", "Height"}), var2[460].callGetProperty(component));
   }

   public void componentShown(ComponentEvent e) {
      CallSite[] var2 = $getCallSiteArray();
   }

   public void focusGained(FocusEvent e) {
      CallSite[] var2 = $getCallSiteArray();
      if (DefaultTypeTransformation.booleanUnbox(!ScriptBytecodeAdapter.compareEqual(var2[461].callGetProperty(e), this.outputArea) && !ScriptBytecodeAdapter.compareEqual(var2[462].callGetProperty(e), this.inputArea) ? Boolean.FALSE : Boolean.TRUE)) {
         this.copyFromComponent = (Component)ScriptBytecodeAdapter.castToType((Component)ScriptBytecodeAdapter.castToType(var2[463].callGetProperty(e), $get$$class$java$awt$Component()), $get$$class$java$awt$Component());
      }

   }

   public void focusLost(FocusEvent e) {
      CallSite[] var2 = $getCallSiteArray();
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$groovy$ui$Console()) {
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
   public Object this$dist$invoke$2(String name, Object args) {
      CallSite[] var3 = $getCallSiteArray();
      Class var10000 = $get$$class$groovy$ui$Console();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$2(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$groovy$ui$Console(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$2(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$groovy$ui$Console(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   public void clearOutput() {
      CallSite[] var1 = $getCallSiteArray();
      var1[464].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((EventObject)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$java$util$EventObject()), $get$$class$java$util$EventObject()));
   }

   public void doInterrupt() {
      CallSite[] var1 = $getCallSiteArray();
      var1[465].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((EventObject)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$java$util$EventObject()), $get$$class$java$util$EventObject()));
   }

   public void exit() {
      CallSite[] var1 = $getCallSiteArray();
      var1[466].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((EventObject)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$java$util$EventObject()), $get$$class$java$util$EventObject()));
   }

   public void fileNewFile() {
      CallSite[] var1 = $getCallSiteArray();
      var1[467].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((EventObject)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$java$util$EventObject()), $get$$class$java$util$EventObject()));
   }

   public void fileNewWindow() {
      CallSite[] var1 = $getCallSiteArray();
      var1[468].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((EventObject)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$java$util$EventObject()), $get$$class$java$util$EventObject()));
   }

   public void fileOpen() {
      CallSite[] var1 = $getCallSiteArray();
      var1[469].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((EventObject)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$java$util$EventObject()), $get$$class$java$util$EventObject()));
   }

   public boolean fileSave() {
      CallSite[] var1 = $getCallSiteArray();
      return DefaultTypeTransformation.booleanUnbox((Boolean)ScriptBytecodeAdapter.castToType(var1[470].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((EventObject)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$java$util$EventObject()), $get$$class$java$util$EventObject())), $get$$class$java$lang$Boolean()));
   }

   public boolean fileSaveAs() {
      CallSite[] var1 = $getCallSiteArray();
      return DefaultTypeTransformation.booleanUnbox((Boolean)ScriptBytecodeAdapter.castToType(var1[471].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((EventObject)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$java$util$EventObject()), $get$$class$java$util$EventObject())), $get$$class$java$lang$Boolean()));
   }

   public void historyNext() {
      CallSite[] var1 = $getCallSiteArray();
      var1[472].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((EventObject)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$java$util$EventObject()), $get$$class$java$util$EventObject()));
   }

   public void historyPrev() {
      CallSite[] var1 = $getCallSiteArray();
      var1[473].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((EventObject)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$java$util$EventObject()), $get$$class$java$util$EventObject()));
   }

   public void inspectLast() {
      CallSite[] var1 = $getCallSiteArray();
      var1[474].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((EventObject)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$java$util$EventObject()), $get$$class$java$util$EventObject()));
   }

   public void inspectVariables() {
      CallSite[] var1 = $getCallSiteArray();
      var1[475].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((EventObject)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$java$util$EventObject()), $get$$class$java$util$EventObject()));
   }

   public void inspectAst() {
      CallSite[] var1 = $getCallSiteArray();
      var1[476].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((EventObject)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$java$util$EventObject()), $get$$class$java$util$EventObject()));
   }

   public void largerFont() {
      CallSite[] var1 = $getCallSiteArray();
      var1[477].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((EventObject)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$java$util$EventObject()), $get$$class$java$util$EventObject()));
   }

   public void runScript() {
      CallSite[] var1 = $getCallSiteArray();
      var1[478].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((EventObject)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$java$util$EventObject()), $get$$class$java$util$EventObject()));
   }

   public void runSelectedScript() {
      CallSite[] var1 = $getCallSiteArray();
      var1[479].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((EventObject)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$java$util$EventObject()), $get$$class$java$util$EventObject()));
   }

   public void addClasspathJar() {
      CallSite[] var1 = $getCallSiteArray();
      var1[480].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((EventObject)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$java$util$EventObject()), $get$$class$java$util$EventObject()));
   }

   public void addClasspathDir() {
      CallSite[] var1 = $getCallSiteArray();
      var1[481].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((EventObject)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$java$util$EventObject()), $get$$class$java$util$EventObject()));
   }

   public void clearContext() {
      CallSite[] var1 = $getCallSiteArray();
      var1[482].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((EventObject)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$java$util$EventObject()), $get$$class$java$util$EventObject()));
   }

   public void compileScript() {
      CallSite[] var1 = $getCallSiteArray();
      var1[483].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((EventObject)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$java$util$EventObject()), $get$$class$java$util$EventObject()));
   }

   public Object selectFilename() {
      CallSite[] var1 = $getCallSiteArray();
      return var1[484].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper("Open", $get$$class$java$lang$Object()));
   }

   public void showAbout() {
      CallSite[] var1 = $getCallSiteArray();
      var1[485].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((EventObject)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$java$util$EventObject()), $get$$class$java$util$EventObject()));
   }

   public void find() {
      CallSite[] var1 = $getCallSiteArray();
      var1[486].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((EventObject)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$java$util$EventObject()), $get$$class$java$util$EventObject()));
   }

   public void findNext() {
      CallSite[] var1 = $getCallSiteArray();
      var1[487].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((EventObject)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$java$util$EventObject()), $get$$class$java$util$EventObject()));
   }

   public void findPrevious() {
      CallSite[] var1 = $getCallSiteArray();
      var1[488].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((EventObject)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$java$util$EventObject()), $get$$class$java$util$EventObject()));
   }

   public void replace() {
      CallSite[] var1 = $getCallSiteArray();
      var1[489].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((EventObject)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$java$util$EventObject()), $get$$class$java$util$EventObject()));
   }

   public void showOutputWindow() {
      CallSite[] var1 = $getCallSiteArray();
      var1[490].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((EventObject)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$java$util$EventObject()), $get$$class$java$util$EventObject()));
   }

   public void hideOutputWindow() {
      CallSite[] var1 = $getCallSiteArray();
      var1[491].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((EventObject)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$java$util$EventObject()), $get$$class$java$util$EventObject()));
   }

   public void hideAndClearOutputWindow() {
      CallSite[] var1 = $getCallSiteArray();
      var1[492].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((EventObject)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$java$util$EventObject()), $get$$class$java$util$EventObject()));
   }

   public void smallerFont() {
      CallSite[] var1 = $getCallSiteArray();
      var1[493].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((EventObject)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$java$util$EventObject()), $get$$class$java$util$EventObject()));
   }

   public void invokeTextAction(Object evt, Object closure) {
      CallSite[] var3 = $getCallSiteArray();
      var3[494].callCurrent(this, ScriptBytecodeAdapter.createPojoWrapper(evt, $get$$class$java$lang$Object()), ScriptBytecodeAdapter.createPojoWrapper(closure, $get$$class$java$lang$Object()), ScriptBytecodeAdapter.createPojoWrapper(this.inputArea, $get$$class$java$lang$Object()));
   }

   public void cut() {
      CallSite[] var1 = $getCallSiteArray();
      var1[495].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((EventObject)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$java$util$EventObject()), $get$$class$java$util$EventObject()));
   }

   public void copy() {
      CallSite[] var1 = $getCallSiteArray();
      var1[496].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((EventObject)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$java$util$EventObject()), $get$$class$java$util$EventObject()));
   }

   public void paste() {
      CallSite[] var1 = $getCallSiteArray();
      var1[497].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((EventObject)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$java$util$EventObject()), $get$$class$java$util$EventObject()));
   }

   public void selectAll() {
      CallSite[] var1 = $getCallSiteArray();
      var1[498].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((EventObject)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$java$util$EventObject()), $get$$class$java$util$EventObject()));
   }

   public void print() {
      CallSite[] var1 = $getCallSiteArray();
      var1[499].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((EventObject)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$java$util$EventObject()), $get$$class$java$util$EventObject()));
   }

   public void undo() {
      CallSite[] var1 = $getCallSiteArray();
      var1[500].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((EventObject)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$java$util$EventObject()), $get$$class$java$util$EventObject()));
   }

   public void redo() {
      CallSite[] var1 = $getCallSiteArray();
      var1[501].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((EventObject)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$java$util$EventObject()), $get$$class$java$util$EventObject()));
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

   static {
      captureStdOut = DefaultTypeTransformation.booleanUnbox($getCallSiteArray()[503].call(prefs, "captureStdOut", Boolean.TRUE));
      captureStdErr = DefaultTypeTransformation.booleanUnbox($getCallSiteArray()[504].call(prefs, "captureStdErr", Boolean.TRUE));
      consoleControllers = ScriptBytecodeAdapter.createList(new Object[0]);
      groovyFileFilter = $getCallSiteArray()[505].callConstructor($get$$class$groovy$ui$GroovyFileFilter());
      frameConsoleDelegates = ScriptBytecodeAdapter.createMap(new Object[]{"rootContainerDelegate", new GeneratedClosure($get$$class$groovy$ui$Console(), $get$$class$groovy$ui$Console()) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$groovy$ui$Console$__clinit__closure23;
         // $FF: synthetic field
         private static Class $class$javax$swing$JFrame;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(Object it) {
            CallSite[] var2 = $getCallSiteArray();
            return var2[0].callCurrent(this, ScriptBytecodeAdapter.createMap(new Object[]{"title", "GroovyConsole", "iconImage", var2[1].callGetProperty(var2[2].callCurrent(this, (Object)"/groovy/ui/ConsoleIcon.png")), "defaultCloseOperation", var2[3].callGetProperty($get$$class$javax$swing$JFrame())}), new GeneratedClosure(this, this.getThisObject()) {
               // $FF: synthetic field
               private static final Integer $const$0 = (Integer)100;
               // $FF: synthetic field
               private static ClassInfo $staticClassInfo;
               // $FF: synthetic field
               private static SoftReference $callSiteArray;
               // $FF: synthetic field
               private static Class $class$java$lang$Object;
               // $FF: synthetic field
               private static Class $class$groovy$ui$Console$__clinit__closure23_closure37;

               public {
                  CallSite[] var3 = $getCallSiteArray();
               }

               public Object doCall(Object it) {
                  CallSite[] var2 = $getCallSiteArray();

                  try {
                     ScriptBytecodeAdapter.setProperty(Boolean.TRUE, $get$$class$groovy$ui$Console$__clinit__closure23_closure37(), var2[0].callGroovyObjectGetProperty(this), "locationByPlatform");
                  } catch (Exception var6) {
                     ScriptBytecodeAdapter.setProperty(ScriptBytecodeAdapter.createList(new Object[]{$const$0, $const$0}), $get$$class$groovy$ui$Console$__clinit__closure23_closure37(), var2[1].callGroovyObjectGetProperty(this), "location");
                  } finally {
                     ;
                  }

                  Object var10000 = var2[2].call(var2[3].callGroovyObjectGetProperty(this), var2[4].callGroovyObjectGetProperty(this));
                  ScriptBytecodeAdapter.setGroovyObjectProperty(var10000, $get$$class$groovy$ui$Console$__clinit__closure23_closure37(), this, "containingWindows");
                  return var10000;
               }

               public Object doCall() {
                  CallSite[] var1 = $getCallSiteArray();
                  return var1[5].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
               }

               // $FF: synthetic method
               protected MetaClass $getStaticMetaClass() {
                  if (this.getClass() == $get$$class$groovy$ui$Console$__clinit__closure23_closure37()) {
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
                  var0[0] = "current";
                  var0[1] = "current";
                  var0[2] = "plus";
                  var0[3] = "containingWindows";
                  var0[4] = "current";
                  var0[5] = "doCall";
               }

               // $FF: synthetic method
               private static CallSiteArray $createCallSiteArray() {
                  String[] var0 = new String[6];
                  $createCallSiteArray_1(var0);
                  return new CallSiteArray($get$$class$groovy$ui$Console$__clinit__closure23_closure37(), var0);
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
               private static Class $get$$class$groovy$ui$Console$__clinit__closure23_closure37() {
                  Class var10000 = $class$groovy$ui$Console$__clinit__closure23_closure37;
                  if (var10000 == null) {
                     var10000 = $class$groovy$ui$Console$__clinit__closure23_closure37 = class$("groovy.ui.Console$__clinit__closure23_closure37");
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
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[4].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$ui$Console$__clinit__closure23()) {
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
            var0[0] = "frame";
            var0[1] = "image";
            var0[2] = "imageIcon";
            var0[3] = "DO_NOTHING_ON_CLOSE";
            var0[4] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[5];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$ui$Console$__clinit__closure23(), var0);
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
         private static Class $get$$class$groovy$ui$Console$__clinit__closure23() {
            Class var10000 = $class$groovy$ui$Console$__clinit__closure23;
            if (var10000 == null) {
               var10000 = $class$groovy$ui$Console$__clinit__closure23 = class$("groovy.ui.Console$__clinit__closure23");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$javax$swing$JFrame() {
            Class var10000 = $class$javax$swing$JFrame;
            if (var10000 == null) {
               var10000 = $class$javax$swing$JFrame = class$("javax.swing.JFrame");
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
      }, "menuBarDelegate", new GeneratedClosure($get$$class$groovy$ui$Console(), $get$$class$groovy$ui$Console()) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$groovy$ui$Console$__clinit__closure24;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(Object arg) {
            Object argx = new Reference(arg);
            CallSite[] var3 = $getCallSiteArray();
            Object var10000 = var3[0].callCurrent(this, (Object)argx.get());
            ScriptBytecodeAdapter.setProperty(var10000, $get$$class$groovy$ui$Console$__clinit__closure24(), var3[1].callGroovyObjectGetProperty(this), "JMenuBar");
            return var10000;
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$ui$Console$__clinit__closure24()) {
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
            var0[0] = "build";
            var0[1] = "current";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[2];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$ui$Console$__clinit__closure24(), var0);
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
         private static Class $get$$class$groovy$ui$Console$__clinit__closure24() {
            Class var10000 = $class$groovy$ui$Console$__clinit__closure24;
            if (var10000 == null) {
               var10000 = $class$groovy$ui$Console$__clinit__closure24 = class$("groovy.ui.Console$__clinit__closure24");
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
      }});
   }

   public static final String getDEFAULT_SCRIPT_NAME_START() {
      return DEFAULT_SCRIPT_NAME_START;
   }

   public static boolean getCaptureStdOut() {
      return captureStdOut;
   }

   public static boolean isCaptureStdOut() {
      return captureStdOut;
   }

   public static void setCaptureStdOut(boolean var0) {
      captureStdOut = var0;
   }

   public static boolean getCaptureStdErr() {
      return captureStdErr;
   }

   public static boolean isCaptureStdErr() {
      return captureStdErr;
   }

   public static void setCaptureStdErr(boolean var0) {
      captureStdErr = var0;
   }

   public static Object getConsoleControllers() {
      return consoleControllers;
   }

   public static void setConsoleControllers(Object var0) {
      consoleControllers = var0;
   }

   public boolean getFullStackTraces() {
      return this.fullStackTraces;
   }

   public boolean isFullStackTraces() {
      return this.fullStackTraces;
   }

   public void setFullStackTraces(boolean var1) {
      this.fullStackTraces = var1;
   }

   public Action getFullStackTracesAction() {
      return this.fullStackTracesAction;
   }

   public void setFullStackTracesAction(Action var1) {
      this.fullStackTracesAction = var1;
   }

   public boolean getShowScriptInOutput() {
      return this.showScriptInOutput;
   }

   public boolean isShowScriptInOutput() {
      return this.showScriptInOutput;
   }

   public void setShowScriptInOutput(boolean var1) {
      this.showScriptInOutput = var1;
   }

   public Action getShowScriptInOutputAction() {
      return this.showScriptInOutputAction;
   }

   public void setShowScriptInOutputAction(Action var1) {
      this.showScriptInOutputAction = var1;
   }

   public boolean getVisualizeScriptResults() {
      return this.visualizeScriptResults;
   }

   public boolean isVisualizeScriptResults() {
      return this.visualizeScriptResults;
   }

   public void setVisualizeScriptResults(boolean var1) {
      this.visualizeScriptResults = var1;
   }

   public Action getVisualizeScriptResultsAction() {
      return this.visualizeScriptResultsAction;
   }

   public void setVisualizeScriptResultsAction(Action var1) {
      this.visualizeScriptResultsAction = var1;
   }

   public boolean getShowToolbar() {
      return this.showToolbar;
   }

   public boolean isShowToolbar() {
      return this.showToolbar;
   }

   public void setShowToolbar(boolean var1) {
      this.showToolbar = var1;
   }

   public Component getToolbar() {
      return this.toolbar;
   }

   public void setToolbar(Component var1) {
      this.toolbar = var1;
   }

   public Action getShowToolbarAction() {
      return this.showToolbarAction;
   }

   public void setShowToolbarAction(Action var1) {
      this.showToolbarAction = var1;
   }

   public boolean getDetachedOutput() {
      return this.detachedOutput;
   }

   public boolean isDetachedOutput() {
      return this.detachedOutput;
   }

   public void setDetachedOutput(boolean var1) {
      this.detachedOutput = var1;
   }

   public Action getDetachedOutputAction() {
      return this.detachedOutputAction;
   }

   public void setDetachedOutputAction(Action var1) {
      this.detachedOutputAction = var1;
   }

   public Action getShowOutputWindowAction() {
      return this.showOutputWindowAction;
   }

   public void setShowOutputWindowAction(Action var1) {
      this.showOutputWindowAction = var1;
   }

   public Action getHideOutputWindowAction1() {
      return this.hideOutputWindowAction1;
   }

   public void setHideOutputWindowAction1(Action var1) {
      this.hideOutputWindowAction1 = var1;
   }

   public Action getHideOutputWindowAction2() {
      return this.hideOutputWindowAction2;
   }

   public void setHideOutputWindowAction2(Action var1) {
      this.hideOutputWindowAction2 = var1;
   }

   public Action getHideOutputWindowAction3() {
      return this.hideOutputWindowAction3;
   }

   public void setHideOutputWindowAction3(Action var1) {
      this.hideOutputWindowAction3 = var1;
   }

   public Action getHideOutputWindowAction4() {
      return this.hideOutputWindowAction4;
   }

   public void setHideOutputWindowAction4(Action var1) {
      this.hideOutputWindowAction4 = var1;
   }

   public int getOrigDividerSize() {
      return this.origDividerSize;
   }

   public void setOrigDividerSize(int var1) {
      this.origDividerSize = var1;
   }

   public Component getOutputWindow() {
      return this.outputWindow;
   }

   public void setOutputWindow(Component var1) {
      this.outputWindow = var1;
   }

   public Component getCopyFromComponent() {
      return this.copyFromComponent;
   }

   public void setCopyFromComponent(Component var1) {
      this.copyFromComponent = var1;
   }

   public Component getBlank() {
      return this.blank;
   }

   public void setBlank(Component var1) {
      this.blank = var1;
   }

   public Component getScrollArea() {
      return this.scrollArea;
   }

   public void setScrollArea(Component var1) {
      this.scrollArea = var1;
   }

   public boolean getAutoClearOutput() {
      return this.autoClearOutput;
   }

   public boolean isAutoClearOutput() {
      return this.autoClearOutput;
   }

   public void setAutoClearOutput(boolean var1) {
      this.autoClearOutput = var1;
   }

   public Action getAutoClearOutputAction() {
      return this.autoClearOutputAction;
   }

   public void setAutoClearOutputAction(Action var1) {
      this.autoClearOutputAction = var1;
   }

   public int getMaxHistory() {
      return this.maxHistory;
   }

   public void setMaxHistory(int var1) {
      this.maxHistory = var1;
   }

   public int getMaxOutputChars() {
      return this.maxOutputChars;
   }

   public void setMaxOutputChars(int var1) {
      this.maxOutputChars = var1;
   }

   public SwingBuilder getSwing() {
      return this.swing;
   }

   public void setSwing(SwingBuilder var1) {
      this.swing = var1;
   }

   public RootPaneContainer getFrame() {
      return this.frame;
   }

   public void setFrame(RootPaneContainer var1) {
      this.frame = var1;
   }

   public ConsoleTextEditor getInputEditor() {
      return this.inputEditor;
   }

   public void setInputEditor(ConsoleTextEditor var1) {
      this.inputEditor = var1;
   }

   public JSplitPane getSplitPane() {
      return this.splitPane;
   }

   public void setSplitPane(JSplitPane var1) {
      this.splitPane = var1;
   }

   public JTextPane getInputArea() {
      return this.inputArea;
   }

   public void setInputArea(JTextPane var1) {
      this.inputArea = var1;
   }

   public JTextPane getOutputArea() {
      return this.outputArea;
   }

   public void setOutputArea(JTextPane var1) {
      this.outputArea = var1;
   }

   public JLabel getStatusLabel() {
      return this.statusLabel;
   }

   public void setStatusLabel(JLabel var1) {
      this.statusLabel = var1;
   }

   public JLabel getRowNumAndColNum() {
      return this.rowNumAndColNum;
   }

   public void setRowNumAndColNum(JLabel var1) {
      this.rowNumAndColNum = var1;
   }

   public Element getRootElement() {
      return this.rootElement;
   }

   public void setRootElement(Element var1) {
      this.rootElement = var1;
   }

   public int getCursorPos() {
      return this.cursorPos;
   }

   public void setCursorPos(int var1) {
      this.cursorPos = var1;
   }

   public int getRowNum() {
      return this.rowNum;
   }

   public void setRowNum(int var1) {
      this.rowNum = var1;
   }

   public int getColNum() {
      return this.colNum;
   }

   public void setColNum(int var1) {
      this.colNum = var1;
   }

   public Style getPromptStyle() {
      return this.promptStyle;
   }

   public void setPromptStyle(Style var1) {
      this.promptStyle = var1;
   }

   public Style getCommandStyle() {
      return this.commandStyle;
   }

   public void setCommandStyle(Style var1) {
      this.commandStyle = var1;
   }

   public Style getOutputStyle() {
      return this.outputStyle;
   }

   public void setOutputStyle(Style var1) {
      this.outputStyle = var1;
   }

   public Style getStacktraceStyle() {
      return this.stacktraceStyle;
   }

   public void setStacktraceStyle(Style var1) {
      this.stacktraceStyle = var1;
   }

   public Style getHyperlinkStyle() {
      return this.hyperlinkStyle;
   }

   public void setHyperlinkStyle(Style var1) {
      this.hyperlinkStyle = var1;
   }

   public Style getResultStyle() {
      return this.resultStyle;
   }

   public void setResultStyle(Style var1) {
      this.resultStyle = var1;
   }

   public List getHistory() {
      return this.history;
   }

   public void setHistory(List var1) {
      this.history = var1;
   }

   public int getHistoryIndex() {
      return this.historyIndex;
   }

   public void setHistoryIndex(int var1) {
      this.historyIndex = var1;
   }

   public HistoryRecord getPendingRecord() {
      return this.pendingRecord;
   }

   public void setPendingRecord(HistoryRecord var1) {
      this.pendingRecord = var1;
   }

   public Action getPrevHistoryAction() {
      return this.prevHistoryAction;
   }

   public void setPrevHistoryAction(Action var1) {
      this.prevHistoryAction = var1;
   }

   public Action getNextHistoryAction() {
      return this.nextHistoryAction;
   }

   public void setNextHistoryAction(Action var1) {
      this.nextHistoryAction = var1;
   }

   public boolean getDirty() {
      return this.dirty;
   }

   public boolean isDirty() {
      return this.dirty;
   }

   public Action getSaveAction() {
      return this.saveAction;
   }

   public void setSaveAction(Action var1) {
      this.saveAction = var1;
   }

   public int getTextSelectionStart() {
      return this.textSelectionStart;
   }

   public void setTextSelectionStart(int var1) {
      this.textSelectionStart = var1;
   }

   public int getTextSelectionEnd() {
      return this.textSelectionEnd;
   }

   public void setTextSelectionEnd(int var1) {
      this.textSelectionEnd = var1;
   }

   public Object getScriptFile() {
      return this.scriptFile;
   }

   public void setScriptFile(Object var1) {
      this.scriptFile = var1;
   }

   public File getCurrentFileChooserDir() {
      return this.currentFileChooserDir;
   }

   public void setCurrentFileChooserDir(File var1) {
      this.currentFileChooserDir = var1;
   }

   public File getCurrentClasspathJarDir() {
      return this.currentClasspathJarDir;
   }

   public void setCurrentClasspathJarDir(File var1) {
      this.currentClasspathJarDir = var1;
   }

   public File getCurrentClasspathDir() {
      return this.currentClasspathDir;
   }

   public void setCurrentClasspathDir(File var1) {
      this.currentClasspathDir = var1;
   }

   public GroovyShell getShell() {
      return this.shell;
   }

   public void setShell(GroovyShell var1) {
      this.shell = var1;
   }

   public int getScriptNameCounter() {
      return this.scriptNameCounter;
   }

   public void setScriptNameCounter(int var1) {
      this.scriptNameCounter = var1;
   }

   public SystemOutputInterceptor getSystemOutInterceptor() {
      return this.systemOutInterceptor;
   }

   public void setSystemOutInterceptor(SystemOutputInterceptor var1) {
      this.systemOutInterceptor = var1;
   }

   public SystemOutputInterceptor getSystemErrorInterceptor() {
      return this.systemErrorInterceptor;
   }

   public void setSystemErrorInterceptor(SystemOutputInterceptor var1) {
      this.systemErrorInterceptor = var1;
   }

   public Thread getRunThread() {
      return this.runThread;
   }

   public void setRunThread(Thread var1) {
      this.runThread = var1;
   }

   public Closure getBeforeExecution() {
      return this.beforeExecution;
   }

   public void setBeforeExecution(Closure var1) {
      this.beforeExecution = var1;
   }

   public Closure getAfterExecution() {
      return this.afterExecution;
   }

   public void setAfterExecution(Closure var1) {
      this.afterExecution = var1;
   }

   public Action getInterruptAction() {
      return this.interruptAction;
   }

   public void setInterruptAction(Action var1) {
      this.interruptAction = var1;
   }

   public static Object getFrameConsoleDelegates() {
      return frameConsoleDelegates;
   }

   public static void setFrameConsoleDelegates(Object var0) {
      frameConsoleDelegates = var0;
   }

   // $FF: synthetic method
   public Object this$2$ensureNoDocLengthOverflow(Object var1) {
      return this.ensureNoDocLengthOverflow(var1);
   }

   // $FF: synthetic method
   public Object this$2$calcPreferredSize(Object var1, Object var2, Object var3) {
      return this.calcPreferredSize(var1, var2, var3);
   }

   // $FF: synthetic method
   public Object this$2$reportException(Throwable var1) {
      return this.reportException(var1);
   }

   // $FF: synthetic method
   public Object this$2$prepareOutputWindow() {
      return this.prepareOutputWindow();
   }

   // $FF: synthetic method
   public void this$2$runScriptImpl(boolean var1) {
      this.runScriptImpl(var1);
   }

   // $FF: synthetic method
   public void this$2$setInputTextFromHistory(Object var1) {
      this.setInputTextFromHistory(var1);
   }

   // $FF: synthetic method
   public void this$2$updateHistoryActions() {
      this.updateHistoryActions();
   }

   // $FF: synthetic method
   public Object this$2$updateFontSize(Object var1) {
      return this.updateFontSize(var1);
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
   public void super$1$wait(long var1) {
      super.wait(var1);
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
   public void super$1$notifyAll() {
      super.notifyAll();
   }

   // $FF: synthetic method
   public Class super$1$getClass() {
      return super.getClass();
   }

   // $FF: synthetic method
   public Object super$1$clone() {
      return super.clone();
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
   public void super$1$finalize() {
      super.finalize();
   }

   // $FF: synthetic method
   private static void $createCallSiteArray_1(String[] var0) {
      var0[0] = "<$constructor$>";
      var0[1] = "<$constructor$>";
      var0[2] = "getBoolean";
      var0[3] = "valueOf";
      var0[4] = "getProperty";
      var0[5] = "getBoolean";
      var0[6] = "getBoolean";
      var0[7] = "getBoolean";
      var0[8] = "getBoolean";
      var0[9] = "getBoolean";
      var0[10] = "getProperty";
      var0[11] = "<$constructor$>";
      var0[12] = "<$constructor$>";
      var0[13] = "get";
      var0[14] = "userNodeForPackage";
      var0[15] = "<$constructor$>";
      var0[16] = "get";
      var0[17] = "userNodeForPackage";
      var0[18] = "<$constructor$>";
      var0[19] = "get";
      var0[20] = "userNodeForPackage";
      var0[21] = "newScript";
      var0[22] = "setProperty";
      var0[23] = "getProperty";
      var0[24] = "toString";
      var0[25] = "getBoolean";
      var0[26] = "plus";
      var0[27] = "addIvyListener";
      var0[28] = "eventManager";
      var0[29] = "ivyInstance";
      var0[30] = "instance";
      var0[31] = "loadOutputTransforms";
      var0[32] = "variables";
      var0[33] = "length";
      var0[34] = "getAt";
      var0[35] = "println";
      var0[36] = "getLogger";
      var0[37] = "STACK_LOG_NAME";
      var0[38] = "setLookAndFeel";
      var0[39] = "getSystemLookAndFeelClassName";
      var0[40] = "<$constructor$>";
      var0[41] = "getRootLoader";
      var0[42] = "classLoader";
      var0[43] = "run";
      var0[44] = "length";
      var0[45] = "loadScriptFile";
      var0[46] = "getAt";
      var0[47] = "<$constructor$>";
      var0[48] = "run";
      var0[49] = "run";
      var0[50] = "<$constructor$>";
      var0[51] = "each";
      var0[52] = "setProperty";
      var0[53] = "build";
      var0[54] = "build";
      var0[55] = "bindResults";
      var0[56] = "bind";
      var0[57] = "undoAction";
      var0[58] = "inputEditor";
      var0[59] = "undoAction";
      var0[60] = "bind";
      var0[61] = "redoAction";
      var0[62] = "inputEditor";
      var0[63] = "redoAction";
      var0[64] = "consoleFrame";
      var0[65] = "pack";
      var0[66] = "consoleFrame";
      var0[67] = "show";
      var0[68] = "consoleFrame";
      var0[69] = "installInterceptor";
      var0[70] = "doLater";
      var0[71] = "<$constructor$>";
      var0[72] = "start";
      var0[73] = "<$constructor$>";
      var0[74] = "start";
      var0[75] = "add";
      var0[76] = "size";
      var0[77] = "remove";
      var0[78] = "size";
      var0[79] = "updateHistoryActions";
      var0[80] = "length";
      var0[81] = "remove";
      var0[82] = "minus";
      var0[83] = "length";
      var0[84] = "styledDocument";
      var0[85] = "insertString";
      var0[86] = "length";
      var0[87] = "ensureNoDocLengthOverflow";
      var0[88] = "appendOutput";
      var0[89] = "toString";
      var0[90] = "appendOutput";
      var0[91] = "toString";
      var0[92] = "<$constructor$>";
      var0[93] = "addAttribute";
      var0[94] = "NameAttribute";
      var0[95] = "setComponent";
      var0[96] = "appendOutput";
      var0[97] = "toString";
      var0[98] = "<$constructor$>";
      var0[99] = "addAttribute";
      var0[100] = "NameAttribute";
      var0[101] = "setIcon";
      var0[102] = "appendOutput";
      var0[103] = "toString";
      var0[104] = "styledDocument";
      var0[105] = "split";
      var0[106] = "each";
      var0[107] = "ensureNoDocLengthOverflow";
      var0[108] = "styledDocument";
      var0[109] = "length";
      var0[110] = "getText";
      var0[111] = "minus";
      var0[112] = "insertString";
      var0[113] = "length";
      var0[114] = "remove";
      var0[115] = "appendOutput";
      var0[116] = "appendOutput";
      var0[117] = "styledDocument";
      var0[118] = "length";
      var0[119] = "insertString";
      var0[120] = "remove";
      var0[121] = "showConfirmDialog";
      var0[122] = "plus";
      var0[123] = "plus";
      var0[124] = "name";
      var0[125] = "YES_NO_CANCEL_OPTION";
      var0[126] = "YES_OPTION";
      var0[127] = "fileSave";
      var0[128] = "NO_OPTION";
      var0[129] = "beep";
      var0[130] = "defaultToolkit";
      var0[131] = "setVariable";
      var0[132] = "getLastResult";
      var0[133] = "setVariable";
      var0[134] = "collect";
      var0[135] = "selected";
      var0[136] = "source";
      var0[137] = "putBoolean";
      var0[138] = "selected";
      var0[139] = "source";
      var0[140] = "putBoolean";
      var0[141] = "selected";
      var0[142] = "source";
      var0[143] = "setProperty";
      var0[144] = "toString";
      var0[145] = "putBoolean";
      var0[146] = "selected";
      var0[147] = "source";
      var0[148] = "putBoolean";
      var0[149] = "selected";
      var0[150] = "source";
      var0[151] = "putBoolean";
      var0[152] = "selected";
      var0[153] = "source";
      var0[154] = "putBoolean";
      var0[155] = "selected";
      var0[156] = "source";
      var0[157] = "putBoolean";
      var0[158] = "add";
      var0[159] = "BOTTOM";
      var0[160] = "dividerSize";
      var0[161] = "add";
      var0[162] = "CENTER";
      var0[163] = "prepareOutputWindow";
      var0[164] = "add";
      var0[165] = "BOTTOM";
      var0[166] = "add";
      var0[167] = "CENTER";
      var0[168] = "selected";
      var0[169] = "source";
      var0[170] = "putBoolean";
      var0[171] = "min";
      var0[172] = "dot";
      var0[173] = "mark";
      var0[174] = "max";
      var0[175] = "dot";
      var0[176] = "mark";
      var0[177] = "setRowNumAndColNum";
      var0[178] = "showConfirmDialog";
      var0[179] = "OK_CANCEL_OPTION";
      var0[180] = "OK_OPTION";
      var0[181] = "doInterrupt";
      var0[182] = "interrupt";
      var0[183] = "askToInterruptScript";
      var0[184] = "askToSaveFile";
      var0[185] = "hide";
      var0[186] = "dispose";
      var0[187] = "dispose";
      var0[188] = "dispose";
      var0[189] = "remove";
      var0[190] = "stop";
      var0[191] = "stop";
      var0[192] = "askToSaveFile";
      var0[193] = "setDirty";
      var0[194] = "<$constructor$>";
      var0[195] = "<$constructor$>";
      var0[196] = "<$constructor$>";
      var0[197] = "variables";
      var0[198] = "context";
      var0[199] = "<$constructor$>";
      var0[200] = "each";
      var0[201] = "build";
      var0[202] = "build";
      var0[203] = "installInterceptor";
      var0[204] = "pack";
      var0[205] = "consoleFrame";
      var0[206] = "show";
      var0[207] = "consoleFrame";
      var0[208] = "doLater";
      var0[209] = "inputArea";
      var0[210] = "selectFilename";
      var0[211] = "loadScriptFile";
      var0[212] = "edt";
      var0[213] = "doOutside";
      var0[214] = "fileSaveAs";
      var0[215] = "write";
      var0[216] = "text";
      var0[217] = "setDirty";
      var0[218] = "selectFilename";
      var0[219] = "write";
      var0[220] = "text";
      var0[221] = "setDirty";
      var0[222] = "getAt";
      var0[223] = "errorCollector";
      var0[224] = "errorCount";
      var0[225] = "appendOutputNl";
      var0[226] = "each";
      var0[227] = "errors";
      var0[228] = "reportException";
      var0[229] = "bindResults";
      var0[230] = "prepareOutputWindow";
      var0[231] = "showOutputWindow";
      var0[232] = "max";
      var0[233] = "min";
      var0[234] = "appendOutputNl";
      var0[235] = "<$constructor$>";
      var0[236] = "withWriter";
      var0[237] = "<$constructor$>";
      var0[238] = "appendStacktrace";
      var0[239] = "buffer";
      var0[240] = "getAt";
      var0[241] = "appendOutputNl";
      var0[242] = "transformResult";
      var0[243] = "_outputTransforms";
      var0[244] = "context";
      var0[245] = "toString";
      var0[246] = "appendOutput";
      var0[247] = "bindResults";
      var0[248] = "prepareOutputWindow";
      var0[249] = "showOutputWindow";
      var0[250] = "setPreferredSize";
      var0[251] = "pack";
      var0[252] = "setPreferredSize";
      var0[253] = "calcPreferredSize";
      var0[254] = "getWidth";
      var0[255] = "getWidth";
      var0[256] = "calcPreferredSize";
      var0[257] = "getHeight";
      var0[258] = "getHeight";
      var0[259] = "pack";
      var0[260] = "iterator";
      var0[261] = "minus";
      var0[262] = "size";
      var0[263] = "result";
      var0[264] = "getAt";
      var0[265] = "result";
      var0[266] = "getAt";
      var0[267] = "size";
      var0[268] = "setInputTextFromHistory";
      var0[269] = "plus";
      var0[270] = "beep";
      var0[271] = "setInputTextFromHistory";
      var0[272] = "minus";
      var0[273] = "beep";
      var0[274] = "lastResult";
      var0[275] = "showMessageDialog";
      var0[276] = "INFORMATION_MESSAGE";
      var0[277] = "inspect";
      var0[278] = "lastResult";
      var0[279] = "inspect";
      var0[280] = "variables";
      var0[281] = "context";
      var0[282] = "run";
      var0[283] = "<$constructor$>";
      var0[284] = "getClassLoader";
      var0[285] = "updateFontSize";
      var0[286] = "plus";
      var0[287] = "size";
      var0[288] = "font";
      var0[289] = "isDispatchThread";
      var0[290] = "each";
      var0[291] = "invokeLater";
      var0[292] = "isDispatchThread";
      var0[293] = "each";
      var0[294] = "invokeLater";
      var0[295] = "runScriptImpl";
      var0[296] = "runScriptImpl";
      var0[297] = "<$constructor$>";
      var0[298] = "FILES_ONLY";
      var0[299] = "showDialog";
      var0[300] = "APPROVE_OPTION";
      var0[301] = "currentDirectory";
      var0[302] = "put";
      var0[303] = "userNodeForPackage";
      var0[304] = "path";
      var0[305] = "addURL";
      var0[306] = "getClassLoader";
      var0[307] = "toURL";
      var0[308] = "selectedFile";
      var0[309] = "<$constructor$>";
      var0[310] = "DIRECTORIES_ONLY";
      var0[311] = "showDialog";
      var0[312] = "APPROVE_OPTION";
      var0[313] = "currentDirectory";
      var0[314] = "put";
      var0[315] = "userNodeForPackage";
      var0[316] = "path";
      var0[317] = "addURL";
      var0[318] = "getClassLoader";
      var0[319] = "toURL";
      var0[320] = "selectedFile";
      var0[321] = "<$constructor$>";
      var0[322] = "newScript";
      var0[323] = "loadOutputTransforms";
      var0[324] = "variables";
      var0[325] = "getProperty";
      var0[326] = "<$constructor$>";
      var0[327] = "replaceAll";
      var0[328] = "getText";
      var0[329] = "addToHistory";
      var0[330] = "<$constructor$>";
      var0[331] = "getBoolean";
      var0[332] = "clearOutput";
      var0[333] = "iterator";
      var0[334] = "tokenize";
      var0[335] = "getTextToRun";
      var0[336] = "appendOutputNl";
      var0[337] = "appendOutput";
      var0[338] = "appendOutputNl";
      var0[339] = "start";
      var0[340] = "getProperty";
      var0[341] = "<$constructor$>";
      var0[342] = "replaceAll";
      var0[343] = "getText";
      var0[344] = "getBoolean";
      var0[345] = "clearOutput";
      var0[346] = "iterator";
      var0[347] = "tokenize";
      var0[348] = "allText";
      var0[349] = "appendOutputNl";
      var0[350] = "appendOutput";
      var0[351] = "appendOutputNl";
      var0[352] = "start";
      var0[353] = "<$constructor$>";
      var0[354] = "FILES_ONLY";
      var0[355] = "<$constructor$>";
      var0[356] = "showDialog";
      var0[357] = "APPROVE_OPTION";
      var0[358] = "currentDirectory";
      var0[359] = "put";
      var0[360] = "userNodeForPackage";
      var0[361] = "path";
      var0[362] = "selectedFile";
      var0[363] = "updateTitle";
      var0[364] = "getProperty";
      var0[365] = "size";
      var0[366] = "<$constructor$>";
      var0[367] = "replaceAll";
      var0[368] = "getText";
      var0[369] = "size";
      var0[370] = "getAt";
      var0[371] = "minus";
      var0[372] = "size";
      var0[373] = "allText";
      var0[374] = "selectionStart";
      var0[375] = "selectionEnd";
      var0[376] = "setDirty";
      var0[377] = "updateHistoryActions";
      var0[378] = "size";
      var0[379] = "setVariable";
      var0[380] = "context";
      var0[381] = "getVersion";
      var0[382] = "optionPane";
      var0[383] = "setMessage";
      var0[384] = "plus";
      var0[385] = "createDialog";
      var0[386] = "show";
      var0[387] = "showDialog";
      var0[388] = "actionPerformed";
      var0[389] = "FIND_ACTION";
      var0[390] = "<$constructor$>";
      var0[391] = "getSource";
      var0[392] = "getID";
      var0[393] = "getActionCommand";
      var0[394] = "getWhen";
      var0[395] = "SHIFT_MASK";
      var0[396] = "actionPerformed";
      var0[397] = "FIND_ACTION";
      var0[398] = "showDialog";
      var0[399] = "setLocationRelativeTo";
      var0[400] = "show";
      var0[401] = "clearOutput";
      var0[402] = "hideOutputWindow";
      var0[403] = "updateFontSize";
      var0[404] = "minus";
      var0[405] = "size";
      var0[406] = "font";
      var0[407] = "containsKey";
      var0[408] = "properties";
      var0[409] = "plus";
      var0[410] = "plus";
      var0[411] = "name";
      var0[412] = "putInt";
      var0[413] = "<$constructor$>";
      var0[414] = "defaultFamily";
      var0[415] = "PLAIN";
      var0[416] = "getSource";
      var0[417] = "call";
      var0[418] = "invokeTextAction";
      var0[419] = "invokeTextAction";
      var0[420] = "invokeTextAction";
      var0[421] = "invokeTextAction";
      var0[422] = "getCaretPosition";
      var0[423] = "plus";
      var0[424] = "getElementIndex";
      var0[425] = "getElement";
      var0[426] = "minus";
      var0[427] = "plus";
      var0[428] = "minus";
      var0[429] = "getStartOffset";
      var0[430] = "setText";
      var0[431] = "actionPerformed";
      var0[432] = "printAction";
      var0[433] = "actionPerformed";
      var0[434] = "undoAction";
      var0[435] = "actionPerformed";
      var0[436] = "redoAction";
      var0[437] = "eventType";
      var0[438] = "ACTIVATED";
      var0[439] = "getURL";
      var0[440] = "toInteger";
      var0[441] = "getAt";
      var0[442] = "plus";
      var0[443] = "lastIndexOf";
      var0[444] = "textEditor";
      var0[445] = "text";
      var0[446] = "iterator";
      var0[447] = "next";
      var0[448] = "indexOf";
      var0[449] = "plus";
      var0[450] = "length";
      var0[451] = "next";
      var0[452] = "setCaretPosition";
      var0[453] = "moveCaretPosition";
      var0[454] = "getComponent";
      var0[455] = "putInt";
      var0[456] = "name";
      var0[457] = "width";
      var0[458] = "putInt";
      var0[459] = "name";
      var0[460] = "height";
      var0[461] = "component";
      var0[462] = "component";
      var0[463] = "component";
      var0[464] = "clearOutput";
      var0[465] = "doInterrupt";
      var0[466] = "exit";
      var0[467] = "fileNewFile";
      var0[468] = "fileNewWindow";
      var0[469] = "fileOpen";
      var0[470] = "fileSave";
      var0[471] = "fileSaveAs";
      var0[472] = "historyNext";
      var0[473] = "historyPrev";
      var0[474] = "inspectLast";
      var0[475] = "inspectVariables";
      var0[476] = "inspectAst";
      var0[477] = "largerFont";
      var0[478] = "runScript";
      var0[479] = "runSelectedScript";
      var0[480] = "addClasspathJar";
      var0[481] = "addClasspathDir";
      var0[482] = "clearContext";
      var0[483] = "compileScript";
      var0[484] = "selectFilename";
      var0[485] = "showAbout";
      var0[486] = "find";
      var0[487] = "findNext";
      var0[488] = "findPrevious";
      var0[489] = "replace";
      var0[490] = "showOutputWindow";
      var0[491] = "hideOutputWindow";
      var0[492] = "hideAndClearOutputWindow";
      var0[493] = "smallerFont";
      var0[494] = "invokeTextAction";
      var0[495] = "cut";
      var0[496] = "copy";
      var0[497] = "paste";
      var0[498] = "selectAll";
      var0[499] = "print";
      var0[500] = "undo";
      var0[501] = "redo";
      var0[502] = "userNodeForPackage";
      var0[503] = "getBoolean";
      var0[504] = "getBoolean";
      var0[505] = "<$constructor$>";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[506];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$groovy$ui$Console(), var0);
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
   private static Class $get$$class$org$apache$ivy$core$event$IvyListener() {
      Class var10000 = $class$org$apache$ivy$core$event$IvyListener;
      if (var10000 == null) {
         var10000 = $class$org$apache$ivy$core$event$IvyListener = class$("org.apache.ivy.core.event.IvyListener");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$awt$Dimension() {
      Class var10000 = $class$java$awt$Dimension;
      if (var10000 == null) {
         var10000 = $class$java$awt$Dimension = class$("java.awt.Dimension");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$awt$Font() {
      Class var10000 = $class$java$awt$Font;
      if (var10000 == null) {
         var10000 = $class$java$awt$Font = class$("java.awt.Font");
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
   private static Class $get$$class$javax$swing$JSplitPane() {
      Class var10000 = $class$javax$swing$JSplitPane;
      if (var10000 == null) {
         var10000 = $class$javax$swing$JSplitPane = class$("javax.swing.JSplitPane");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$inspect$swingui$ObjectBrowser() {
      Class var10000 = $class$groovy$inspect$swingui$ObjectBrowser;
      if (var10000 == null) {
         var10000 = $class$groovy$inspect$swingui$ObjectBrowser = class$("groovy.inspect.swingui.ObjectBrowser");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$lang$Math() {
      Class var10000 = $class$java$lang$Math;
      if (var10000 == null) {
         var10000 = $class$java$lang$Math = class$("java.lang.Math");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$ui$Console() {
      Class var10000 = $class$groovy$ui$Console;
      if (var10000 == null) {
         var10000 = $class$groovy$ui$Console = class$("groovy.ui.Console");
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
   private static Class $get$$class$java$util$Set() {
      Class var10000 = $class$java$util$Set;
      if (var10000 == null) {
         var10000 = $class$java$util$Set = class$("java.util.Set");
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
   private static Class $get$$class$org$codehaus$groovy$control$MultipleCompilationErrorsException() {
      Class var10000 = $class$org$codehaus$groovy$control$MultipleCompilationErrorsException;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$control$MultipleCompilationErrorsException = class$("org.codehaus.groovy.control.MultipleCompilationErrorsException");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$lang$Thread() {
      Class var10000 = $class$java$lang$Thread;
      if (var10000 == null) {
         var10000 = $class$java$lang$Thread = class$("java.lang.Thread");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$awt$event$ActionEvent() {
      Class var10000 = $class$java$awt$event$ActionEvent;
      if (var10000 == null) {
         var10000 = $class$java$awt$event$ActionEvent = class$("java.awt.event.ActionEvent");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$lang$Binding() {
      Class var10000 = $class$groovy$lang$Binding;
      if (var10000 == null) {
         var10000 = $class$groovy$lang$Binding = class$("groovy.lang.Binding");
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
   private static Class $get$$class$java$util$prefs$Preferences() {
      Class var10000 = $class$java$util$prefs$Preferences;
      if (var10000 == null) {
         var10000 = $class$java$util$prefs$Preferences = class$("java.util.prefs.Preferences");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$javax$swing$SwingUtilities() {
      Class var10000 = $class$javax$swing$SwingUtilities;
      if (var10000 == null) {
         var10000 = $class$javax$swing$SwingUtilities = class$("javax.swing.SwingUtilities");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$ui$SystemOutputInterceptor() {
      Class var10000 = $class$groovy$ui$SystemOutputInterceptor;
      if (var10000 == null) {
         var10000 = $class$groovy$ui$SystemOutputInterceptor = class$("groovy.ui.SystemOutputInterceptor");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$awt$Toolkit() {
      Class var10000 = $class$java$awt$Toolkit;
      if (var10000 == null) {
         var10000 = $class$java$awt$Toolkit = class$("java.awt.Toolkit");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$runtime$StackTraceUtils() {
      Class var10000 = $class$org$codehaus$groovy$runtime$StackTraceUtils;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$runtime$StackTraceUtils = class$("org.codehaus.groovy.runtime.StackTraceUtils");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$awt$Component() {
      Class var10000 = $class$java$awt$Component;
      if (var10000 == null) {
         var10000 = $class$java$awt$Component = class$("java.awt.Component");
      }

      return var10000;
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
   private static Class $get$$class$groovy$ui$text$FindReplaceUtility() {
      Class var10000 = $class$groovy$ui$text$FindReplaceUtility;
      if (var10000 == null) {
         var10000 = $class$groovy$ui$text$FindReplaceUtility = class$("groovy.ui.text.FindReplaceUtility");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$util$HashMap() {
      Class var10000 = $class$java$util$HashMap;
      if (var10000 == null) {
         var10000 = $class$java$util$HashMap = class$("java.util.HashMap");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$awt$EventQueue() {
      Class var10000 = $class$java$awt$EventQueue;
      if (var10000 == null) {
         var10000 = $class$java$awt$EventQueue = class$("java.awt.EventQueue");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$inspect$swingui$AstBrowser() {
      Class var10000 = $class$groovy$inspect$swingui$AstBrowser;
      if (var10000 == null) {
         var10000 = $class$groovy$inspect$swingui$AstBrowser = class$("groovy.inspect.swingui.AstBrowser");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$util$EventObject() {
      Class var10000 = $class$java$util$EventObject;
      if (var10000 == null) {
         var10000 = $class$java$util$EventObject = class$("java.util.EventObject");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$io$StringWriter() {
      Class var10000 = $class$java$io$StringWriter;
      if (var10000 == null) {
         var10000 = $class$java$io$StringWriter = class$("java.io.StringWriter");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$ui$OutputTransforms() {
      Class var10000 = $class$groovy$ui$OutputTransforms;
      if (var10000 == null) {
         var10000 = $class$groovy$ui$OutputTransforms = class$("groovy.ui.OutputTransforms");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$ui$ConsoleActions() {
      Class var10000 = $class$groovy$ui$ConsoleActions;
      if (var10000 == null) {
         var10000 = $class$groovy$ui$ConsoleActions = class$("groovy.ui.ConsoleActions");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$util$List() {
      Class var10000 = $class$java$util$List;
      if (var10000 == null) {
         var10000 = $class$java$util$List = class$("java.util.List");
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
   private static Class $get$$class$groovy$ui$GroovyFileFilter() {
      Class var10000 = $class$groovy$ui$GroovyFileFilter;
      if (var10000 == null) {
         var10000 = $class$groovy$ui$GroovyFileFilter = class$("groovy.ui.GroovyFileFilter");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$javax$swing$text$StyleConstants() {
      Class var10000 = $class$javax$swing$text$StyleConstants;
      if (var10000 == null) {
         var10000 = $class$javax$swing$text$StyleConstants = class$("javax.swing.text.StyleConstants");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$grape$GrapeIvy() {
      Class var10000 = $class$groovy$grape$GrapeIvy;
      if (var10000 == null) {
         var10000 = $class$groovy$grape$GrapeIvy = class$("groovy.grape.GrapeIvy");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$javax$swing$JFileChooser() {
      Class var10000 = $class$javax$swing$JFileChooser;
      if (var10000 == null) {
         var10000 = $class$javax$swing$JFileChooser = class$("javax.swing.JFileChooser");
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
   private static Class $get$$class$groovy$ui$HistoryRecord() {
      Class var10000 = $class$groovy$ui$HistoryRecord;
      if (var10000 == null) {
         var10000 = $class$groovy$ui$HistoryRecord = class$("groovy.ui.HistoryRecord");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$awt$BorderLayout() {
      Class var10000 = $class$java$awt$BorderLayout;
      if (var10000 == null) {
         var10000 = $class$java$awt$BorderLayout = class$("java.awt.BorderLayout");
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
   private static Class $get$$class$org$codehaus$groovy$control$ErrorCollector() {
      Class var10000 = $class$org$codehaus$groovy$control$ErrorCollector;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$control$ErrorCollector = class$("org.codehaus.groovy.control.ErrorCollector");
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
   private static Class $get$$class$groovy$grape$Grape() {
      Class var10000 = $class$groovy$grape$Grape;
      if (var10000 == null) {
         var10000 = $class$groovy$grape$Grape = class$("groovy.grape.Grape");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$javax$swing$event$HyperlinkEvent$EventType() {
      Class var10000 = $class$javax$swing$event$HyperlinkEvent$EventType;
      if (var10000 == null) {
         var10000 = $class$javax$swing$event$HyperlinkEvent$EventType = class$("javax.swing.event.HyperlinkEvent$EventType");
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
   private static Class $get$$class$java$lang$Boolean() {
      Class var10000 = $class$java$lang$Boolean;
      if (var10000 == null) {
         var10000 = $class$java$lang$Boolean = class$("java.lang.Boolean");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$lang$System() {
      Class var10000 = $class$java$lang$System;
      if (var10000 == null) {
         var10000 = $class$java$lang$System = class$("java.lang.System");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$javax$swing$JOptionPane() {
      Class var10000 = $class$javax$swing$JOptionPane;
      if (var10000 == null) {
         var10000 = $class$javax$swing$JOptionPane = class$("javax.swing.JOptionPane");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$swing$SwingBuilder() {
      Class var10000 = $class$groovy$swing$SwingBuilder;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$SwingBuilder = class$("groovy.swing.SwingBuilder");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$io$PrintWriter() {
      Class var10000 = $class$java$io$PrintWriter;
      if (var10000 == null) {
         var10000 = $class$java$io$PrintWriter = class$("java.io.PrintWriter");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$lang$GroovyShell() {
      Class var10000 = $class$groovy$lang$GroovyShell;
      if (var10000 == null) {
         var10000 = $class$groovy$lang$GroovyShell = class$("groovy.lang.GroovyShell");
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

   class _closure1 extends Closure implements GeneratedClosure {
      private Reference<T> resolvedDependencies;
      private Reference<T> downloadedArtifacts;
      // $FF: synthetic field
      private static ClassInfo $staticClassInfo;
      // $FF: synthetic field
      private static SoftReference $callSiteArray;
      // $FF: synthetic field
      private static Class $class$org$apache$ivy$core$event$resolve$StartResolveEvent;
      // $FF: synthetic field
      private static Class $class$java$util$Set;
      // $FF: synthetic field
      private static Class $class$groovy$ui$Console$_closure1;
      // $FF: synthetic field
      private static Class $class$org$apache$ivy$core$event$download$PrepareDownloadEvent;

      public _closure1(Object _outerInstance, Object _thisObject, Reference<T> resolvedDependencies, Reference<T> downloadedArtifacts) {
         CallSite[] var5 = $getCallSiteArray();
         super(_outerInstance, _thisObject);
         this.resolvedDependencies = (Reference)resolvedDependencies;
         this.downloadedArtifacts = (Reference)downloadedArtifacts;
      }

      public Object doCall(Object ivyEvent) {
         Object ivyEventx = new Reference(ivyEvent);
         CallSite[] var3 = $getCallSiteArray();
         Object var4 = ivyEventx.get();
         CallSite var10000;
         Object var10001;
         Object var10005;
         Reference downloadedArtifacts;
         if (ScriptBytecodeAdapter.isCase(var4, $get$$class$org$apache$ivy$core$event$resolve$StartResolveEvent())) {
            var10000 = var3[0];
            var10001 = var3[1].callGetProperty(var3[2].callGetProperty(ivyEventx.get()));
            var10005 = this.getThisObject();
            downloadedArtifacts = this.resolvedDependencies;
            return var10000.call(var10001, (Object)(new GeneratedClosure(this, var10005, downloadedArtifacts) {
               private Reference<T> resolvedDependencies;
               // $FF: synthetic field
               private static ClassInfo $staticClassInfo;
               // $FF: synthetic field
               private static SoftReference $callSiteArray;
               // $FF: synthetic field
               private static Class $class$java$util$Set;
               // $FF: synthetic field
               private static Class $class$groovy$ui$Console$_closure1_closure25;

               public {
                  Reference resolvedDependenciesx = new Reference(resolvedDependencies);
                  CallSite[] var5 = $getCallSiteArray();
                  this.resolvedDependencies = (Reference)((Reference)resolvedDependenciesx.get());
               }

               public Object doCall(Object it) {
                  Object itx = new Reference(it);
                  CallSite[] var3 = $getCallSiteArray();
                  Object name = new Reference(var3[0].call(itx.get()));
                  if (!DefaultTypeTransformation.booleanUnbox(var3[1].call(this.resolvedDependencies.get(), name.get()))) {
                     var3[2].call(this.resolvedDependencies.get(), name.get());
                     GStringImpl var10000 = new GStringImpl(new Object[]{name.get()}, new String[]{"Resolving ", " ..."});
                     ScriptBytecodeAdapter.setProperty(var10000, $get$$class$groovy$ui$Console$_closure1_closure25(), var3[3].callGroovyObjectGetProperty(this), "text");
                     return var10000;
                  } else {
                     return null;
                  }
               }

               public Set<String> getResolvedDependencies() {
                  CallSite[] var1 = $getCallSiteArray();
                  return (Set)ScriptBytecodeAdapter.castToType(this.resolvedDependencies.get(), $get$$class$java$util$Set());
               }

               // $FF: synthetic method
               protected MetaClass $getStaticMetaClass() {
                  if (this.getClass() == $get$$class$groovy$ui$Console$_closure1_closure25()) {
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
                  var0[0] = "toString";
                  var0[1] = "contains";
                  var0[2] = "leftShift";
                  var0[3] = "statusLabel";
               }

               // $FF: synthetic method
               private static CallSiteArray $createCallSiteArray() {
                  String[] var0 = new String[4];
                  $createCallSiteArray_1(var0);
                  return new CallSiteArray($get$$class$groovy$ui$Console$_closure1_closure25(), var0);
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
               private static Class $get$$class$java$util$Set() {
                  Class var10000 = $class$java$util$Set;
                  if (var10000 == null) {
                     var10000 = $class$java$util$Set = class$("java.util.Set");
                  }

                  return var10000;
               }

               // $FF: synthetic method
               private static Class $get$$class$groovy$ui$Console$_closure1_closure25() {
                  Class var10000 = $class$groovy$ui$Console$_closure1_closure25;
                  if (var10000 == null) {
                     var10000 = $class$groovy$ui$Console$_closure1_closure25 = class$("groovy.ui.Console$_closure1_closure25");
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
         } else if (ScriptBytecodeAdapter.isCase(var4, $get$$class$org$apache$ivy$core$event$download$PrepareDownloadEvent())) {
            var10000 = var3[3];
            var10001 = var3[4].callGetProperty(ivyEventx.get());
            var10005 = this.getThisObject();
            downloadedArtifacts = this.downloadedArtifacts;
            return var10000.call(var10001, (Object)(new GeneratedClosure(this, var10005, downloadedArtifacts) {
               private Reference<T> downloadedArtifacts;
               // $FF: synthetic field
               private static ClassInfo $staticClassInfo;
               // $FF: synthetic field
               private static SoftReference $callSiteArray;
               // $FF: synthetic field
               private static Class $class$java$util$Set;
               // $FF: synthetic field
               private static Class $class$groovy$ui$Console$_closure1_closure26;

               public {
                  Reference downloadedArtifactsx = new Reference(downloadedArtifacts);
                  CallSite[] var5 = $getCallSiteArray();
                  this.downloadedArtifacts = (Reference)((Reference)downloadedArtifactsx.get());
               }

               public Object doCall(Object it) {
                  Object itx = new Reference(it);
                  CallSite[] var3 = $getCallSiteArray();
                  Object name = new Reference(var3[0].call(itx.get()));
                  if (!DefaultTypeTransformation.booleanUnbox(var3[1].call(this.downloadedArtifacts.get(), name.get()))) {
                     var3[2].call(this.downloadedArtifacts.get(), name.get());
                     GStringImpl var10000 = new GStringImpl(new Object[]{name.get()}, new String[]{"Downloading artifact ", " ..."});
                     ScriptBytecodeAdapter.setProperty(var10000, $get$$class$groovy$ui$Console$_closure1_closure26(), var3[3].callGroovyObjectGetProperty(this), "text");
                     return var10000;
                  } else {
                     return null;
                  }
               }

               public Set<String> getDownloadedArtifacts() {
                  CallSite[] var1 = $getCallSiteArray();
                  return (Set)ScriptBytecodeAdapter.castToType(this.downloadedArtifacts.get(), $get$$class$java$util$Set());
               }

               // $FF: synthetic method
               protected MetaClass $getStaticMetaClass() {
                  if (this.getClass() == $get$$class$groovy$ui$Console$_closure1_closure26()) {
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
                  var0[0] = "toString";
                  var0[1] = "contains";
                  var0[2] = "leftShift";
                  var0[3] = "statusLabel";
               }

               // $FF: synthetic method
               private static CallSiteArray $createCallSiteArray() {
                  String[] var0 = new String[4];
                  $createCallSiteArray_1(var0);
                  return new CallSiteArray($get$$class$groovy$ui$Console$_closure1_closure26(), var0);
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
               private static Class $get$$class$java$util$Set() {
                  Class var10000 = $class$java$util$Set;
                  if (var10000 == null) {
                     var10000 = $class$java$util$Set = class$("java.util.Set");
                  }

                  return var10000;
               }

               // $FF: synthetic method
               private static Class $get$$class$groovy$ui$Console$_closure1_closure26() {
                  Class var10000 = $class$groovy$ui$Console$_closure1_closure26;
                  if (var10000 == null) {
                     var10000 = $class$groovy$ui$Console$_closure1_closure26 = class$("groovy.ui.Console$_closure1_closure26");
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
         } else {
            return null;
         }
      }

      public Set<String> getResolvedDependencies() {
         CallSite[] var1 = $getCallSiteArray();
         return (Set)ScriptBytecodeAdapter.castToType(this.resolvedDependencies.get(), $get$$class$java$util$Set());
      }

      public Set<String> getDownloadedArtifacts() {
         CallSite[] var1 = $getCallSiteArray();
         return (Set)ScriptBytecodeAdapter.castToType(this.downloadedArtifacts.get(), $get$$class$java$util$Set());
      }

      // $FF: synthetic method
      protected MetaClass $getStaticMetaClass() {
         if (this.getClass() == $get$$class$groovy$ui$Console$_closure1()) {
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
         var0[0] = "each";
         var0[1] = "dependencies";
         var0[2] = "moduleDescriptor";
         var0[3] = "each";
         var0[4] = "artifacts";
      }

      // $FF: synthetic method
      private static CallSiteArray $createCallSiteArray() {
         String[] var0 = new String[5];
         $createCallSiteArray_1(var0);
         return new CallSiteArray($get$$class$groovy$ui$Console$_closure1(), var0);
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
      private static Class $get$$class$org$apache$ivy$core$event$resolve$StartResolveEvent() {
         Class var10000 = $class$org$apache$ivy$core$event$resolve$StartResolveEvent;
         if (var10000 == null) {
            var10000 = $class$org$apache$ivy$core$event$resolve$StartResolveEvent = class$("org.apache.ivy.core.event.resolve.StartResolveEvent");
         }

         return var10000;
      }

      // $FF: synthetic method
      private static Class $get$$class$java$util$Set() {
         Class var10000 = $class$java$util$Set;
         if (var10000 == null) {
            var10000 = $class$java$util$Set = class$("java.util.Set");
         }

         return var10000;
      }

      // $FF: synthetic method
      private static Class $get$$class$groovy$ui$Console$_closure1() {
         Class var10000 = $class$groovy$ui$Console$_closure1;
         if (var10000 == null) {
            var10000 = $class$groovy$ui$Console$_closure1 = class$("groovy.ui.Console$_closure1");
         }

         return var10000;
      }

      // $FF: synthetic method
      private static Class $get$$class$org$apache$ivy$core$event$download$PrepareDownloadEvent() {
         Class var10000 = $class$org$apache$ivy$core$event$download$PrepareDownloadEvent;
         if (var10000 == null) {
            var10000 = $class$org$apache$ivy$core$event$download$PrepareDownloadEvent = class$("org.apache.ivy.core.event.download.PrepareDownloadEvent");
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
}
