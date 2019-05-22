package groovy.ui;

import groovy.lang.Binding;
import groovy.lang.MetaClass;
import groovy.lang.Script;
import java.io.File;
import java.lang.ref.SoftReference;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class ConsoleActions extends Script {
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private static final Integer $const$0 = (Integer)0;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524205056L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524205056 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$java$awt$event$InputEvent;
   // $FF: synthetic field
   private static Class $class$javax$swing$KeyStroke;
   // $FF: synthetic field
   private static Class $class$groovy$lang$Script;
   // $FF: synthetic field
   private static Class $class$java$awt$event$KeyEvent;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$runtime$InvokerHelper;
   // $FF: synthetic field
   private static Class $class$groovy$ui$ConsoleActions;
   // $FF: synthetic field
   private static Class $class$java$lang$String;

   public ConsoleActions() {
      CallSite[] var1 = $getCallSiteArray();
   }

   public ConsoleActions(Binding context) {
      CallSite[] var2 = $getCallSiteArray();
      ScriptBytecodeAdapter.invokeMethodOnSuperN($get$$class$groovy$lang$Script(), this, "setBinding", new Object[]{context});
   }

   public static void main(String... args) {
      CallSite[] var1 = $getCallSiteArray();
      var1[0].call($get$$class$org$codehaus$groovy$runtime$InvokerHelper(), $get$$class$groovy$ui$ConsoleActions(), args);
   }

   public Object run() {
      CallSite[] var1 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectProperty(var1[1].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"name", "New File", "closure", ScriptBytecodeAdapter.getMethodPointer(var1[2].callGroovyObjectGetProperty(this), "fileNewFile"), "mnemonic", "N", "accelerator", var1[3].callCurrent(this, (Object)"N"), "smallIcon", var1[4].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"resource", "icons/page.png", "class", this})), "shortDescription", "New Groovy Script"})), $get$$class$groovy$ui$ConsoleActions(), this, "newFileAction");
      ScriptBytecodeAdapter.setGroovyObjectProperty(var1[5].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"name", "New Window", "closure", ScriptBytecodeAdapter.getMethodPointer(var1[6].callGroovyObjectGetProperty(this), "fileNewWindow"), "mnemonic", "W", "accelerator", var1[7].callCurrent(this, (Object)"shift N")})), $get$$class$groovy$ui$ConsoleActions(), this, "newWindowAction");
      ScriptBytecodeAdapter.setGroovyObjectProperty(var1[8].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"name", "Open", "closure", ScriptBytecodeAdapter.getMethodPointer(var1[9].callGroovyObjectGetProperty(this), "fileOpen"), "mnemonic", "O", "accelerator", var1[10].callCurrent(this, (Object)"O"), "smallIcon", var1[11].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"resource", "icons/folder_page.png", "class", this})), "shortDescription", "Open Groovy Script"})), $get$$class$groovy$ui$ConsoleActions(), this, "openAction");
      ScriptBytecodeAdapter.setGroovyObjectProperty(var1[12].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"name", "Save", "closure", ScriptBytecodeAdapter.getMethodPointer(var1[13].callGroovyObjectGetProperty(this), "fileSave"), "mnemonic", "S", "accelerator", var1[14].callCurrent(this, (Object)"S"), "smallIcon", var1[15].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"resource", "icons/disk.png", "class", this})), "shortDescription", "Save Groovy Script", "enabled", Boolean.FALSE})), $get$$class$groovy$ui$ConsoleActions(), this, "saveAction");
      ScriptBytecodeAdapter.setGroovyObjectProperty(var1[16].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"name", "Save As...", "closure", ScriptBytecodeAdapter.getMethodPointer(var1[17].callGroovyObjectGetProperty(this), "fileSaveAs"), "mnemonic", "A"})), $get$$class$groovy$ui$ConsoleActions(), this, "saveAsAction");
      ScriptBytecodeAdapter.setGroovyObjectProperty(var1[18].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"name", "Print...", "closure", ScriptBytecodeAdapter.getMethodPointer(var1[19].callGroovyObjectGetProperty(this), "print"), "mnemonic", "P", "accelerator", var1[20].callCurrent(this, (Object)"P")})), $get$$class$groovy$ui$ConsoleActions(), this, "printAction");
      ScriptBytecodeAdapter.setGroovyObjectProperty(var1[21].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"name", "Exit", "closure", ScriptBytecodeAdapter.getMethodPointer(var1[22].callGroovyObjectGetProperty(this), "exit"), "mnemonic", "X"})), $get$$class$groovy$ui$ConsoleActions(), this, "exitAction");
      ScriptBytecodeAdapter.setGroovyObjectProperty(var1[23].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"name", "Undo", "closure", ScriptBytecodeAdapter.getMethodPointer(var1[24].callGroovyObjectGetProperty(this), "undo"), "mnemonic", "U", "accelerator", var1[25].callCurrent(this, (Object)"Z"), "smallIcon", var1[26].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"resource", "icons/arrow_undo.png", "class", this})), "shortDescription", "Undo"})), $get$$class$groovy$ui$ConsoleActions(), this, "undoAction");
      ScriptBytecodeAdapter.setGroovyObjectProperty(var1[27].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"name", "Redo", "closure", ScriptBytecodeAdapter.getMethodPointer(var1[28].callGroovyObjectGetProperty(this), "redo"), "mnemonic", "R", "accelerator", var1[29].callCurrent(this, (Object)"shift Z"), "smallIcon", var1[30].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"resource", "icons/arrow_redo.png", "class", this})), "shortDescription", "Redo"})), $get$$class$groovy$ui$ConsoleActions(), this, "redoAction");
      ScriptBytecodeAdapter.setGroovyObjectProperty(var1[31].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"name", "Find...", "closure", ScriptBytecodeAdapter.getMethodPointer(var1[32].callGroovyObjectGetProperty(this), "find"), "mnemonic", "F", "accelerator", var1[33].callCurrent(this, (Object)"F"), "smallIcon", var1[34].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"resource", "icons/find.png", "class", this})), "shortDescription", "Find"})), $get$$class$groovy$ui$ConsoleActions(), this, "findAction");
      ScriptBytecodeAdapter.setGroovyObjectProperty(var1[35].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"name", "Find Next", "closure", ScriptBytecodeAdapter.getMethodPointer(var1[36].callGroovyObjectGetProperty(this), "findNext"), "mnemonic", "N", "accelerator", var1[37].call($get$$class$javax$swing$KeyStroke(), var1[38].callGetProperty($get$$class$java$awt$event$KeyEvent()), $const$0)})), $get$$class$groovy$ui$ConsoleActions(), this, "findNextAction");
      ScriptBytecodeAdapter.setGroovyObjectProperty(var1[39].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"name", "Find Previous", "closure", ScriptBytecodeAdapter.getMethodPointer(var1[40].callGroovyObjectGetProperty(this), "findPrevious"), "mnemonic", "V", "accelerator", var1[41].call($get$$class$javax$swing$KeyStroke(), var1[42].callGetProperty($get$$class$java$awt$event$KeyEvent()), var1[43].callGetProperty($get$$class$java$awt$event$InputEvent()))})), $get$$class$groovy$ui$ConsoleActions(), this, "findPreviousAction");
      ScriptBytecodeAdapter.setGroovyObjectProperty(var1[44].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"name", "Replace...", "closure", ScriptBytecodeAdapter.getMethodPointer(var1[45].callGroovyObjectGetProperty(this), "replace"), "mnemonic", "E", "accelerator", var1[46].callCurrent(this, (Object)"H"), "smallIcon", var1[47].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"resource", "icons/text_replace.png", "class", this})), "shortDescription", "Replace"})), $get$$class$groovy$ui$ConsoleActions(), this, "replaceAction");
      ScriptBytecodeAdapter.setGroovyObjectProperty(var1[48].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"name", "Cut", "closure", ScriptBytecodeAdapter.getMethodPointer(var1[49].callGroovyObjectGetProperty(this), "cut"), "mnemonic", "T", "accelerator", var1[50].callCurrent(this, (Object)"X"), "smallIcon", var1[51].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"resource", "icons/cut.png", "class", this})), "shortDescription", "Cut"})), $get$$class$groovy$ui$ConsoleActions(), this, "cutAction");
      ScriptBytecodeAdapter.setGroovyObjectProperty(var1[52].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"name", "Copy", "closure", ScriptBytecodeAdapter.getMethodPointer(var1[53].callGroovyObjectGetProperty(this), "copy"), "mnemonic", "C", "accelerator", var1[54].callCurrent(this, (Object)"C"), "smallIcon", var1[55].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"resource", "icons/page_copy.png", "class", this})), "shortDescription", "Copy"})), $get$$class$groovy$ui$ConsoleActions(), this, "copyAction");
      ScriptBytecodeAdapter.setGroovyObjectProperty(var1[56].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"name", "Paste", "closure", ScriptBytecodeAdapter.getMethodPointer(var1[57].callGroovyObjectGetProperty(this), "paste"), "mnemonic", "P", "accelerator", var1[58].callCurrent(this, (Object)"V"), "smallIcon", var1[59].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"resource", "icons/page_paste.png", "class", this})), "shortDescription", "Paste"})), $get$$class$groovy$ui$ConsoleActions(), this, "pasteAction");
      ScriptBytecodeAdapter.setGroovyObjectProperty(var1[60].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"name", "Select All", "closure", ScriptBytecodeAdapter.getMethodPointer(var1[61].callGroovyObjectGetProperty(this), "selectAll"), "mnemonic", "A", "accelerator", var1[62].callCurrent(this, (Object)"A")})), $get$$class$groovy$ui$ConsoleActions(), this, "selectAllAction");
      ScriptBytecodeAdapter.setGroovyObjectProperty(var1[63].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"name", "Previous", "closure", ScriptBytecodeAdapter.getMethodPointer(var1[64].callGroovyObjectGetProperty(this), "historyPrev"), "mnemonic", "P", "accelerator", var1[65].callCurrent(this, (Object)var1[66].callGetProperty($get$$class$java$awt$event$KeyEvent())), "smallIcon", var1[67].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"resource", "icons/book_previous.png", "class", this})), "shortDescription", "Previous Groovy Script", "enabled", Boolean.FALSE})), $get$$class$groovy$ui$ConsoleActions(), this, "historyPrevAction");
      ScriptBytecodeAdapter.setGroovyObjectProperty(var1[68].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"name", "Next", "closure", ScriptBytecodeAdapter.getMethodPointer(var1[69].callGroovyObjectGetProperty(this), "historyNext"), "mnemonic", "N", "accelerator", var1[70].callCurrent(this, (Object)var1[71].callGetProperty($get$$class$java$awt$event$KeyEvent())), "smallIcon", var1[72].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"resource", "icons/book_next.png", "class", this})), "shortDescription", "Next Groovy Script", "enabled", Boolean.FALSE})), $get$$class$groovy$ui$ConsoleActions(), this, "historyNextAction");
      ScriptBytecodeAdapter.setGroovyObjectProperty(var1[73].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"name", "Clear Output", "closure", ScriptBytecodeAdapter.getMethodPointer(var1[74].callGroovyObjectGetProperty(this), "clearOutput"), "mnemonic", "C", "accelerator", var1[75].callCurrent(this, (Object)"W")})), $get$$class$groovy$ui$ConsoleActions(), this, "clearOutputAction");
      ScriptBytecodeAdapter.setGroovyObjectProperty(var1[76].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"name", "Run", "closure", ScriptBytecodeAdapter.getMethodPointer(var1[77].callGroovyObjectGetProperty(this), "runScript"), "mnemonic", "R", "keyStroke", var1[78].callCurrent(this, (Object)"ENTER"), "accelerator", var1[79].callCurrent(this, (Object)"R"), "smallIcon", var1[80].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"resource", "icons/script_go.png", "class", this})), "shortDescription", "Execute Groovy Script"})), $get$$class$groovy$ui$ConsoleActions(), this, "runAction");
      ScriptBytecodeAdapter.setGroovyObjectProperty(var1[81].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"name", "Run Selection", "closure", ScriptBytecodeAdapter.getMethodPointer(var1[82].callGroovyObjectGetProperty(this), "runSelectedScript"), "mnemonic", "E", "keyStroke", var1[83].callCurrent(this, (Object)"shift ENTER"), "accelerator", var1[84].callCurrent(this, (Object)"shift R")})), $get$$class$groovy$ui$ConsoleActions(), this, "runSelectionAction");
      ScriptBytecodeAdapter.setGroovyObjectProperty(var1[85].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"name", "Add Jar to ClassPath", "closure", ScriptBytecodeAdapter.getMethodPointer(var1[86].callGroovyObjectGetProperty(this), "addClasspathJar"), "mnemonic", "J"})), $get$$class$groovy$ui$ConsoleActions(), this, "addClasspathJar");
      ScriptBytecodeAdapter.setGroovyObjectProperty(var1[87].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"name", "Add Directory to ClassPath", "closure", ScriptBytecodeAdapter.getMethodPointer(var1[88].callGroovyObjectGetProperty(this), "addClasspathDir"), "mnemonic", "D"})), $get$$class$groovy$ui$ConsoleActions(), this, "addClasspathDir");
      ScriptBytecodeAdapter.setGroovyObjectProperty(var1[89].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"name", "Clear Script Context", "closure", ScriptBytecodeAdapter.getMethodPointer(var1[90].callGroovyObjectGetProperty(this), "clearContext"), "mnemonic", "C"})), $get$$class$groovy$ui$ConsoleActions(), this, "clearClassloader");
      ScriptBytecodeAdapter.setGroovyObjectProperty(var1[91].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"name", "Inspect Last", "closure", ScriptBytecodeAdapter.getMethodPointer(var1[92].callGroovyObjectGetProperty(this), "inspectLast"), "mnemonic", "I", "accelerator", var1[93].callCurrent(this, (Object)"I")})), $get$$class$groovy$ui$ConsoleActions(), this, "inspectLastAction");
      ScriptBytecodeAdapter.setGroovyObjectProperty(var1[94].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"name", "Inspect Variables", "closure", ScriptBytecodeAdapter.getMethodPointer(var1[95].callGroovyObjectGetProperty(this), "inspectVariables"), "mnemonic", "V", "accelerator", var1[96].callCurrent(this, (Object)"J")})), $get$$class$groovy$ui$ConsoleActions(), this, "inspectVariablesAction");
      ScriptBytecodeAdapter.setGroovyObjectProperty(var1[97].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"name", "Inspect Ast", "closure", ScriptBytecodeAdapter.getMethodPointer(var1[98].callGroovyObjectGetProperty(this), "inspectAst"), "mnemonic", "A", "accelerator", var1[99].callCurrent(this, (Object)"T")})), $get$$class$groovy$ui$ConsoleActions(), this, "inspectAstAction");
      ScriptBytecodeAdapter.setGroovyObjectProperty(var1[100].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"name", "Capture Standard Output", "closure", ScriptBytecodeAdapter.getMethodPointer(var1[101].callGroovyObjectGetProperty(this), "captureStdOut"), "mnemonic", "O"})), $get$$class$groovy$ui$ConsoleActions(), this, "captureStdOutAction");
      ScriptBytecodeAdapter.setGroovyObjectProperty(var1[102].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"name", "Capture Standard Error Output", "closure", ScriptBytecodeAdapter.getMethodPointer(var1[103].callGroovyObjectGetProperty(this), "captureStdErr"), "mnemonic", "E"})), $get$$class$groovy$ui$ConsoleActions(), this, "captureStdErrAction");
      ScriptBytecodeAdapter.setGroovyObjectProperty(var1[104].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"name", "Show Full Stack Traces", "closure", ScriptBytecodeAdapter.getMethodPointer(var1[105].callGroovyObjectGetProperty(this), "fullStackTraces"), "mnemonic", "F"})), $get$$class$groovy$ui$ConsoleActions(), this, "fullStackTracesAction");
      ScriptBytecodeAdapter.setGroovyObjectProperty(var1[106].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"name", "Show Script in Output", "closure", ScriptBytecodeAdapter.getMethodPointer(var1[107].callGroovyObjectGetProperty(this), "showScriptInOutput"), "mnemonic", "R"})), $get$$class$groovy$ui$ConsoleActions(), this, "showScriptInOutputAction");
      ScriptBytecodeAdapter.setGroovyObjectProperty(var1[108].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"name", "Visualize Script Results", "closure", ScriptBytecodeAdapter.getMethodPointer(var1[109].callGroovyObjectGetProperty(this), "visualizeScriptResults"), "mnemonic", "V"})), $get$$class$groovy$ui$ConsoleActions(), this, "visualizeScriptResultsAction");
      ScriptBytecodeAdapter.setGroovyObjectProperty(var1[110].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"name", "Show Toolbar", "closure", ScriptBytecodeAdapter.getMethodPointer(var1[111].callGroovyObjectGetProperty(this), "showToolbar"), "mnemonic", "T"})), $get$$class$groovy$ui$ConsoleActions(), this, "showToolbarAction");
      ScriptBytecodeAdapter.setGroovyObjectProperty(var1[112].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"name", "Detached Output", "closure", ScriptBytecodeAdapter.getMethodPointer(var1[113].callGroovyObjectGetProperty(this), "detachedOutput"), "mnemonic", "D"})), $get$$class$groovy$ui$ConsoleActions(), this, "detachedOutputAction");
      ScriptBytecodeAdapter.setGroovyObjectProperty(var1[114].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"closure", ScriptBytecodeAdapter.getMethodPointer(var1[115].callGroovyObjectGetProperty(this), "showOutputWindow"), "keyStroke", var1[116].callCurrent(this, (Object)"shift O")})), $get$$class$groovy$ui$ConsoleActions(), this, "showOutputWindowAction");
      ScriptBytecodeAdapter.setGroovyObjectProperty(var1[117].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"closure", ScriptBytecodeAdapter.getMethodPointer(var1[118].callGroovyObjectGetProperty(this), "hideOutputWindow"), "keyStroke", "SPACE"})), $get$$class$groovy$ui$ConsoleActions(), this, "hideOutputWindowAction1");
      ScriptBytecodeAdapter.setGroovyObjectProperty(var1[119].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"closure", ScriptBytecodeAdapter.getMethodPointer(var1[120].callGroovyObjectGetProperty(this), "hideOutputWindow"), "keyStroke", "ENTER"})), $get$$class$groovy$ui$ConsoleActions(), this, "hideOutputWindowAction2");
      ScriptBytecodeAdapter.setGroovyObjectProperty(var1[121].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"closure", ScriptBytecodeAdapter.getMethodPointer(var1[122].callGroovyObjectGetProperty(this), "hideOutputWindow"), "keyStroke", "ESCAPE"})), $get$$class$groovy$ui$ConsoleActions(), this, "hideOutputWindowAction3");
      ScriptBytecodeAdapter.setGroovyObjectProperty(var1[123].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"closure", ScriptBytecodeAdapter.getMethodPointer(var1[124].callGroovyObjectGetProperty(this), "hideAndClearOutputWindow"), "keyStroke", var1[125].callCurrent(this, (Object)"W")})), $get$$class$groovy$ui$ConsoleActions(), this, "hideOutputWindowAction4");
      ScriptBytecodeAdapter.setGroovyObjectProperty(var1[126].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"name", "Auto Clear Output On Run", "closure", ScriptBytecodeAdapter.getMethodPointer(var1[127].callGroovyObjectGetProperty(this), "autoClearOutput"), "mnemonic", "A"})), $get$$class$groovy$ui$ConsoleActions(), this, "autoClearOutputAction");
      ScriptBytecodeAdapter.setGroovyObjectProperty(var1[128].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"name", "Larger Font", "closure", ScriptBytecodeAdapter.getMethodPointer(var1[129].callGroovyObjectGetProperty(this), "largerFont"), "mnemonic", "L", "accelerator", var1[130].callCurrent(this, (Object)"shift L")})), $get$$class$groovy$ui$ConsoleActions(), this, "largerFontAction");
      ScriptBytecodeAdapter.setGroovyObjectProperty(var1[131].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"name", "Smaller Font", "closure", ScriptBytecodeAdapter.getMethodPointer(var1[132].callGroovyObjectGetProperty(this), "smallerFont"), "mnemonic", "S", "accelerator", var1[133].callCurrent(this, (Object)"shift S")})), $get$$class$groovy$ui$ConsoleActions(), this, "smallerFontAction");
      ScriptBytecodeAdapter.setGroovyObjectProperty(var1[134].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"name", "About", "closure", ScriptBytecodeAdapter.getMethodPointer(var1[135].callGroovyObjectGetProperty(this), "showAbout"), "mnemonic", "A"})), $get$$class$groovy$ui$ConsoleActions(), this, "aboutAction");
      ScriptBytecodeAdapter.setGroovyObjectProperty(var1[136].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"name", "Interrupt", "closure", ScriptBytecodeAdapter.getMethodPointer(var1[137].callGroovyObjectGetProperty(this), "doInterrupt"), "mnemonic", "T", "smallIcon", var1[138].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"resource", "icons/cross.png", "class", this})), "shortDescription", "Interrupt Running Script", "enabled", Boolean.FALSE})), $get$$class$groovy$ui$ConsoleActions(), this, "interruptAction");
      Object var10000 = var1[139].callCurrent(this, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"name", "Compile", "closure", ScriptBytecodeAdapter.getMethodPointer(var1[140].callGroovyObjectGetProperty(this), "compileScript"), "mnemonic", "L", "accelerator", var1[141].callCurrent(this, (Object)"L"), "shortDescription", "Compile Groovy Script"}));
      ScriptBytecodeAdapter.setGroovyObjectProperty(var10000, $get$$class$groovy$ui$ConsoleActions(), this, "compileAction");
      return var10000;
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$groovy$ui$ConsoleActions()) {
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
      Class var10000 = $get$$class$groovy$ui$ConsoleActions();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$4(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$groovy$ui$ConsoleActions(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$4(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$groovy$ui$ConsoleActions(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
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
      var0[1] = "action";
      var0[2] = "controller";
      var0[3] = "shortcut";
      var0[4] = "imageIcon";
      var0[5] = "action";
      var0[6] = "controller";
      var0[7] = "shortcut";
      var0[8] = "action";
      var0[9] = "controller";
      var0[10] = "shortcut";
      var0[11] = "imageIcon";
      var0[12] = "action";
      var0[13] = "controller";
      var0[14] = "shortcut";
      var0[15] = "imageIcon";
      var0[16] = "action";
      var0[17] = "controller";
      var0[18] = "action";
      var0[19] = "controller";
      var0[20] = "shortcut";
      var0[21] = "action";
      var0[22] = "controller";
      var0[23] = "action";
      var0[24] = "controller";
      var0[25] = "shortcut";
      var0[26] = "imageIcon";
      var0[27] = "action";
      var0[28] = "controller";
      var0[29] = "shortcut";
      var0[30] = "imageIcon";
      var0[31] = "action";
      var0[32] = "controller";
      var0[33] = "shortcut";
      var0[34] = "imageIcon";
      var0[35] = "action";
      var0[36] = "controller";
      var0[37] = "getKeyStroke";
      var0[38] = "VK_F3";
      var0[39] = "action";
      var0[40] = "controller";
      var0[41] = "getKeyStroke";
      var0[42] = "VK_F3";
      var0[43] = "SHIFT_DOWN_MASK";
      var0[44] = "action";
      var0[45] = "controller";
      var0[46] = "shortcut";
      var0[47] = "imageIcon";
      var0[48] = "action";
      var0[49] = "controller";
      var0[50] = "shortcut";
      var0[51] = "imageIcon";
      var0[52] = "action";
      var0[53] = "controller";
      var0[54] = "shortcut";
      var0[55] = "imageIcon";
      var0[56] = "action";
      var0[57] = "controller";
      var0[58] = "shortcut";
      var0[59] = "imageIcon";
      var0[60] = "action";
      var0[61] = "controller";
      var0[62] = "shortcut";
      var0[63] = "action";
      var0[64] = "controller";
      var0[65] = "shortcut";
      var0[66] = "VK_COMMA";
      var0[67] = "imageIcon";
      var0[68] = "action";
      var0[69] = "controller";
      var0[70] = "shortcut";
      var0[71] = "VK_PERIOD";
      var0[72] = "imageIcon";
      var0[73] = "action";
      var0[74] = "controller";
      var0[75] = "shortcut";
      var0[76] = "action";
      var0[77] = "controller";
      var0[78] = "shortcut";
      var0[79] = "shortcut";
      var0[80] = "imageIcon";
      var0[81] = "action";
      var0[82] = "controller";
      var0[83] = "shortcut";
      var0[84] = "shortcut";
      var0[85] = "action";
      var0[86] = "controller";
      var0[87] = "action";
      var0[88] = "controller";
      var0[89] = "action";
      var0[90] = "controller";
      var0[91] = "action";
      var0[92] = "controller";
      var0[93] = "shortcut";
      var0[94] = "action";
      var0[95] = "controller";
      var0[96] = "shortcut";
      var0[97] = "action";
      var0[98] = "controller";
      var0[99] = "shortcut";
      var0[100] = "action";
      var0[101] = "controller";
      var0[102] = "action";
      var0[103] = "controller";
      var0[104] = "action";
      var0[105] = "controller";
      var0[106] = "action";
      var0[107] = "controller";
      var0[108] = "action";
      var0[109] = "controller";
      var0[110] = "action";
      var0[111] = "controller";
      var0[112] = "action";
      var0[113] = "controller";
      var0[114] = "action";
      var0[115] = "controller";
      var0[116] = "shortcut";
      var0[117] = "action";
      var0[118] = "controller";
      var0[119] = "action";
      var0[120] = "controller";
      var0[121] = "action";
      var0[122] = "controller";
      var0[123] = "action";
      var0[124] = "controller";
      var0[125] = "shortcut";
      var0[126] = "action";
      var0[127] = "controller";
      var0[128] = "action";
      var0[129] = "controller";
      var0[130] = "shortcut";
      var0[131] = "action";
      var0[132] = "controller";
      var0[133] = "shortcut";
      var0[134] = "action";
      var0[135] = "controller";
      var0[136] = "action";
      var0[137] = "controller";
      var0[138] = "imageIcon";
      var0[139] = "action";
      var0[140] = "controller";
      var0[141] = "shortcut";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[142];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$groovy$ui$ConsoleActions(), var0);
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
   private static Class $get$$class$java$awt$event$InputEvent() {
      Class var10000 = $class$java$awt$event$InputEvent;
      if (var10000 == null) {
         var10000 = $class$java$awt$event$InputEvent = class$("java.awt.event.InputEvent");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$javax$swing$KeyStroke() {
      Class var10000 = $class$javax$swing$KeyStroke;
      if (var10000 == null) {
         var10000 = $class$javax$swing$KeyStroke = class$("javax.swing.KeyStroke");
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
   private static Class $get$$class$java$awt$event$KeyEvent() {
      Class var10000 = $class$java$awt$event$KeyEvent;
      if (var10000 == null) {
         var10000 = $class$java$awt$event$KeyEvent = class$("java.awt.event.KeyEvent");
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
   private static Class $get$$class$groovy$ui$ConsoleActions() {
      Class var10000 = $class$groovy$ui$ConsoleActions;
      if (var10000 == null) {
         var10000 = $class$groovy$ui$ConsoleActions = class$("groovy.ui.ConsoleActions");
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
