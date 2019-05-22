package groovy.ui;

import groovy.lang.GroovyObject;
import groovy.lang.MetaClass;
import java.applet.AppletContext;
import java.applet.AppletStub;
import java.applet.AudioClip;
import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.FocusTraversalPolicy;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.ImageCapabilities;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.MenuComponent;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Component.BaselineResizeBehavior;
import java.awt.dnd.DropTarget;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.HierarchyBoundsListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.InputMethodEvent;
import java.awt.event.InputMethodListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.im.InputContext;
import java.awt.im.InputMethodRequests;
import java.awt.image.ColorModel;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.awt.image.VolatileImage;
import java.awt.peer.ComponentPeer;
import java.beans.PropertyChangeListener;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.Locale;
import java.util.Set;
import javax.accessibility.AccessibleContext;
import javax.swing.JApplet;
import javax.swing.JLayeredPane;
import javax.swing.JMenuBar;
import javax.swing.JRootPane;
import javax.swing.TransferHandler;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class ConsoleApplet extends JApplet implements GroovyObject {
   private Console console;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private transient MetaClass metaClass;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524205731L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524205731 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClass;
   // $FF: synthetic field
   private static Class $class$groovy$ui$ConsoleApplet;
   // $FF: synthetic field
   private static Class $class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$groovy$ui$Console;

   public ConsoleApplet() {
      CallSite[] var1 = $getCallSiteArray();
      this.metaClass = (MetaClass)ScriptBytecodeAdapter.castToType(this.$getStaticMetaClass(), $get$$class$groovy$lang$MetaClass());
   }

   public void start() {
      CallSite[] var1 = $getCallSiteArray();
      this.console = (Console)ScriptBytecodeAdapter.castToType(var1[0].callConstructor($get$$class$groovy$ui$Console()), $get$$class$groovy$ui$Console());
      var1[1].call(this.console, (Object)this);
   }

   public void stop() {
      CallSite[] var1 = $getCallSiteArray();
      var1[2].call(this.console);
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$groovy$ui$ConsoleApplet()) {
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
   public Object this$dist$invoke$7(String name, Object args) {
      CallSite[] var3 = $getCallSiteArray();
      Class var10000 = $get$$class$groovy$ui$ConsoleApplet();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$7(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$groovy$ui$ConsoleApplet(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$7(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$groovy$ui$ConsoleApplet(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
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

   public Console getConsole() {
      return this.console;
   }

   public void setConsole(Console var1) {
      this.console = var1;
   }

   // $FF: synthetic method
   public AudioClip super$5$getAudioClip(URL var1, String var2) {
      return super.getAudioClip(var1, var2);
   }

   // $FF: synthetic method
   public void super$2$addHierarchyListener(HierarchyListener var1) {
      super.addHierarchyListener(var1);
   }

   // $FF: synthetic method
   public void super$2$setComponentOrientation(ComponentOrientation var1) {
      super.setComponentOrientation(var1);
   }

   // $FF: synthetic method
   public void super$5$resize(Dimension var1) {
      super.resize(var1);
   }

   // $FF: synthetic method
   public MouseWheelListener[] super$2$getMouseWheelListeners() {
      return super.getMouseWheelListeners();
   }

   // $FF: synthetic method
   public void super$2$addFocusListener(FocusListener var1) {
      super.addFocusListener(var1);
   }

   // $FF: synthetic method
   public AudioClip super$5$getAudioClip(URL var1) {
      return super.getAudioClip(var1);
   }

   // $FF: synthetic method
   public boolean super$6$isRootPaneCheckingEnabled() {
      return super.isRootPaneCheckingEnabled();
   }

   // $FF: synthetic method
   public void super$5$init() {
      super.init();
   }

   // $FF: synthetic method
   public Point super$2$location() {
      return super.location();
   }

   // $FF: synthetic method
   public void super$4$addNotify() {
      super.addNotify();
   }

   // $FF: synthetic method
   public void super$3$paint(Graphics var1) {
      super.paint(var1);
   }

   // $FF: synthetic method
   public boolean super$2$isDisplayable() {
      return super.isDisplayable();
   }

   // $FF: synthetic method
   public void super$6$remove(Component var1) {
      super.remove(var1);
   }

   // $FF: synthetic method
   public void super$2$setCursor(Cursor var1) {
      super.setCursor(var1);
   }

   // $FF: synthetic method
   public Class super$1$getClass() {
      return super.getClass();
   }

   // $FF: synthetic method
   public void super$3$setComponentZOrder(Component var1, int var2) {
      super.setComponentZOrder(var1, var2);
   }

   // $FF: synthetic method
   public Rectangle super$2$getBounds() {
      return super.getBounds();
   }

   // $FF: synthetic method
   public void super$3$print(Graphics var1) {
      super.print(var1);
   }

   // $FF: synthetic method
   public void super$2$firePropertyChange(String var1, float var2, float var3) {
      super.firePropertyChange(var1, var2, var3);
   }

   // $FF: synthetic method
   public int super$1$hashCode() {
      return super.hashCode();
   }

   // $FF: synthetic method
   public void super$6$setLayout(LayoutManager var1) {
      super.setLayout(var1);
   }

   // $FF: synthetic method
   public LayoutManager super$3$getLayout() {
      return super.getLayout();
   }

   // $FF: synthetic method
   public Point super$2$getLocation() {
      return super.getLocation();
   }

   // $FF: synthetic method
   public DropTarget super$2$getDropTarget() {
      return super.getDropTarget();
   }

   // $FF: synthetic method
   public boolean super$2$mouseEnter(Event var1, int var2, int var3) {
      return super.mouseEnter(var1, var2, var3);
   }

   // $FF: synthetic method
   public Dimension super$2$getSize() {
      return super.getSize();
   }

   // $FF: synthetic method
   public void super$5$showStatus(String var1) {
      super.showStatus(var1);
   }

   // $FF: synthetic method
   public Container super$6$getContentPane() {
      return super.getContentPane();
   }

   // $FF: synthetic method
   public Image super$2$createImage(int var1, int var2) {
      return super.createImage(var1, var2);
   }

   // $FF: synthetic method
   public boolean super$3$isFocusTraversalPolicyProvider() {
      return super.isFocusTraversalPolicyProvider();
   }

   // $FF: synthetic method
   public void super$6$setGlassPane(Component var1) {
      super.setGlassPane(var1);
   }

   // $FF: synthetic method
   public void super$2$setIgnoreRepaint(boolean var1) {
      super.setIgnoreRepaint(var1);
   }

   // $FF: synthetic method
   public void super$6$repaint(long var1, int var3, int var4, int var5, int var6) {
      super.repaint(var1, var3, var4, var5, var6);
   }

   // $FF: synthetic method
   public Component super$6$getGlassPane() {
      return super.getGlassPane();
   }

   // $FF: synthetic method
   public Dimension super$2$getSize(Dimension var1) {
      return super.getSize(var1);
   }

   // $FF: synthetic method
   public PropertyChangeListener[] super$2$getPropertyChangeListeners() {
      return super.getPropertyChangeListeners();
   }

   // $FF: synthetic method
   public int super$2$getX() {
      return super.getX();
   }

   // $FF: synthetic method
   public Locale super$5$getLocale() {
      return super.getLocale();
   }

   // $FF: synthetic method
   public boolean super$2$imageUpdate(Image var1, int var2, int var3, int var4, int var5, int var6) {
      return super.imageUpdate(var1, var2, var3, var4, var5, var6);
   }

   // $FF: synthetic method
   public JRootPane super$6$createRootPane() {
      return super.createRootPane();
   }

   // $FF: synthetic method
   public Dimension super$3$getPreferredSize() {
      return super.getPreferredSize();
   }

   // $FF: synthetic method
   public void super$3$printComponents(Graphics var1) {
      super.printComponents(var1);
   }

   // $FF: synthetic method
   public void super$5$destroy() {
      super.destroy();
   }

   // $FF: synthetic method
   public Component super$3$getComponentAt(Point var1) {
      return super.getComponentAt(var1);
   }

   // $FF: synthetic method
   public boolean super$2$isOpaque() {
      return super.isOpaque();
   }

   // $FF: synthetic method
   public void super$2$transferFocusBackward() {
      super.transferFocusBackward();
   }

   // $FF: synthetic method
   public boolean super$2$action(Event var1, Object var2) {
      return super.action(var1, var2);
   }

   // $FF: synthetic method
   public boolean super$2$isCursorSet() {
      return super.isCursorSet();
   }

   // $FF: synthetic method
   public void super$3$setFocusCycleRoot(boolean var1) {
      super.setFocusCycleRoot(var1);
   }

   // $FF: synthetic method
   public void super$2$repaint(int var1, int var2, int var3, int var4) {
      super.repaint(var1, var2, var3, var4);
   }

   // $FF: synthetic method
   public Point super$2$getLocationOnScreen() {
      return super.getLocationOnScreen();
   }

   // $FF: synthetic method
   public void super$3$add(Component var1, Object var2) {
      super.add(var1, var2);
   }

   // $FF: synthetic method
   public boolean super$2$getIgnoreRepaint() {
      return super.getIgnoreRepaint();
   }

   // $FF: synthetic method
   public boolean super$2$isForegroundSet() {
      return super.isForegroundSet();
   }

   // $FF: synthetic method
   public boolean super$3$isFocusTraversalPolicySet() {
      return super.isFocusTraversalPolicySet();
   }

   // $FF: synthetic method
   public void super$2$setFocusable(boolean var1) {
      super.setFocusable(var1);
   }

   // $FF: synthetic method
   public Component super$3$locate(int var1, int var2) {
      return super.locate(var1, var2);
   }

   // $FF: synthetic method
   public void super$3$invalidate() {
      super.invalidate();
   }

   // $FF: synthetic method
   public boolean super$2$isFontSet() {
      return super.isFontSet();
   }

   // $FF: synthetic method
   public Cursor super$2$getCursor() {
      return super.getCursor();
   }

   // $FF: synthetic method
   public void super$3$processContainerEvent(ContainerEvent var1) {
      super.processContainerEvent(var1);
   }

   // $FF: synthetic method
   public void super$3$removeContainerListener(ContainerListener var1) {
      super.removeContainerListener(var1);
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
   public ColorModel super$2$getColorModel() {
      return super.getColorModel();
   }

   // $FF: synthetic method
   public void super$2$removeComponentListener(ComponentListener var1) {
      super.removeComponentListener(var1);
   }

   // $FF: synthetic method
   public void super$6$setLayeredPane(JLayeredPane var1) {
      super.setLayeredPane(var1);
   }

   // $FF: synthetic method
   public void super$2$processComponentEvent(ComponentEvent var1) {
      super.processComponentEvent(var1);
   }

   // $FF: synthetic method
   public void super$2$enable() {
      super.enable();
   }

   // $FF: synthetic method
   public boolean super$5$isValidateRoot() {
      return super.isValidateRoot();
   }

   // $FF: synthetic method
   public Object super$1$clone() {
      return super.clone();
   }

   // $FF: synthetic method
   public AWTEvent super$2$coalesceEvents(AWTEvent var1, AWTEvent var2) {
      return super.coalesceEvents(var1, var2);
   }

   // $FF: synthetic method
   public Component super$3$getComponentAt(int var1, int var2) {
      return super.getComponentAt(var1, var2);
   }

   // $FF: synthetic method
   public void super$2$hide() {
      super.hide();
   }

   // $FF: synthetic method
   public boolean super$2$isVisible() {
      return super.isVisible();
   }

   // $FF: synthetic method
   public Point super$3$getMousePosition(boolean var1) {
      return super.getMousePosition(var1);
   }

   // $FF: synthetic method
   public boolean super$2$gotFocus(Event var1, Object var2) {
      return super.gotFocus(var1, var2);
   }

   // $FF: synthetic method
   public void super$2$move(int var1, int var2) {
      super.move(var1, var2);
   }

   // $FF: synthetic method
   public void super$2$removeMouseMotionListener(MouseMotionListener var1) {
      super.removeMouseMotionListener(var1);
   }

   // $FF: synthetic method
   public MouseListener[] super$2$getMouseListeners() {
      return super.getMouseListeners();
   }

   // $FF: synthetic method
   public void super$1$wait(long var1) {
      super.wait(var1);
   }

   // $FF: synthetic method
   public void super$2$removeMouseWheelListener(MouseWheelListener var1) {
      super.removeMouseWheelListener(var1);
   }

   // $FF: synthetic method
   public void super$5$setStub(AppletStub var1) {
      super.setStub(var1);
   }

   // $FF: synthetic method
   public Dimension super$3$preferredSize() {
      return super.preferredSize();
   }

   // $FF: synthetic method
   public void super$5$start() {
      super.start();
   }

   // $FF: synthetic method
   public FocusTraversalPolicy super$3$getFocusTraversalPolicy() {
      return super.getFocusTraversalPolicy();
   }

   // $FF: synthetic method
   public Color super$2$getForeground() {
      return super.getForeground();
   }

   // $FF: synthetic method
   public void super$2$removePropertyChangeListener(String var1, PropertyChangeListener var2) {
      super.removePropertyChangeListener(var1, var2);
   }

   // $FF: synthetic method
   public void super$2$enableInputMethods(boolean var1) {
      super.enableInputMethods(var1);
   }

   // $FF: synthetic method
   public boolean super$2$lostFocus(Event var1, Object var2) {
      return super.lostFocus(var1, var2);
   }

   // $FF: synthetic method
   public HierarchyBoundsListener[] super$2$getHierarchyBoundsListeners() {
      return super.getHierarchyBoundsListeners();
   }

   // $FF: synthetic method
   public Component super$3$findComponentAt(int var1, int var2) {
      return super.findComponentAt(var1, var2);
   }

   // $FF: synthetic method
   public Set super$3$getFocusTraversalKeys(int var1) {
      return super.getFocusTraversalKeys(var1);
   }

   // $FF: synthetic method
   public void super$2$add(PopupMenu var1) {
      super.add(var1);
   }

   // $FF: synthetic method
   public boolean super$2$contains(int var1, int var2) {
      return super.contains(var1, var2);
   }

   // $FF: synthetic method
   public Dimension super$3$getMaximumSize() {
      return super.getMaximumSize();
   }

   // $FF: synthetic method
   public boolean super$3$isFocusCycleRoot(Container var1) {
      return super.isFocusCycleRoot(var1);
   }

   // $FF: synthetic method
   public int super$2$getBaseline(int var1, int var2) {
      return super.getBaseline(var1, var2);
   }

   // $FF: synthetic method
   public void super$2$processMouseWheelEvent(MouseWheelEvent var1) {
      super.processMouseWheelEvent(var1);
   }

   // $FF: synthetic method
   public boolean super$2$requestFocus(boolean var1) {
      return super.requestFocus(var1);
   }

   // $FF: synthetic method
   public void super$3$validate() {
      super.validate();
   }

   // $FF: synthetic method
   public void super$2$removeInputMethodListener(InputMethodListener var1) {
      super.removeInputMethodListener(var1);
   }

   // $FF: synthetic method
   public boolean super$2$postEvent(Event var1) {
      return super.postEvent(var1);
   }

   // $FF: synthetic method
   public void super$2$requestFocus() {
      super.requestFocus();
   }

   // $FF: synthetic method
   public int super$2$getY() {
      return super.getY();
   }

   // $FF: synthetic method
   public InputMethodListener[] super$2$getInputMethodListeners() {
      return super.getInputMethodListeners();
   }

   // $FF: synthetic method
   public Rectangle super$2$bounds() {
      return super.bounds();
   }

   // $FF: synthetic method
   public void super$2$printAll(Graphics var1) {
      super.printAll(var1);
   }

   // $FF: synthetic method
   public Insets super$3$getInsets() {
      return super.getInsets();
   }

   // $FF: synthetic method
   public int super$3$countComponents() {
      return super.countComponents();
   }

   // $FF: synthetic method
   public ComponentOrientation super$2$getComponentOrientation() {
      return super.getComponentOrientation();
   }

   // $FF: synthetic method
   public void super$3$remove(int var1) {
      super.remove(var1);
   }

   // $FF: synthetic method
   public FocusListener[] super$2$getFocusListeners() {
      return super.getFocusListeners();
   }

   // $FF: synthetic method
   public void super$3$validateTree() {
      super.validateTree();
   }

   // $FF: synthetic method
   public Object[] super$3$getListeners(Class var1) {
      return super.getListeners(var1);
   }

   // $FF: synthetic method
   public void super$2$firePropertyChange(String var1, double var2, double var4) {
      super.firePropertyChange(var1, var2, var4);
   }

   // $FF: synthetic method
   public void super$3$list(PrintWriter var1, int var2) {
      super.list(var1, var2);
   }

   // $FF: synthetic method
   public String super$2$toString() {
      return super.toString();
   }

   // $FF: synthetic method
   public TransferHandler super$6$getTransferHandler() {
      return super.getTransferHandler();
   }

   // $FF: synthetic method
   public boolean super$2$isEnabled() {
      return super.isEnabled();
   }

   // $FF: synthetic method
   public boolean super$2$isShowing() {
      return super.isShowing();
   }

   // $FF: synthetic method
   public void super$2$setSize(Dimension var1) {
      super.setSize(var1);
   }

   // $FF: synthetic method
   public Component super$3$getComponent(int var1) {
      return super.getComponent(var1);
   }

   // $FF: synthetic method
   public void super$3$addPropertyChangeListener(String var1, PropertyChangeListener var2) {
      super.addPropertyChangeListener(var1, var2);
   }

   // $FF: synthetic method
   public void super$6$addImpl(Component var1, Object var2, int var3) {
      super.addImpl(var1, var2, var3);
   }

   // $FF: synthetic method
   public void super$2$addMouseListener(MouseListener var1) {
      super.addMouseListener(var1);
   }

   // $FF: synthetic method
   public void super$2$setSize(int var1, int var2) {
      super.setSize(var1, var2);
   }

   // $FF: synthetic method
   public void super$2$show() {
      super.show();
   }

   // $FF: synthetic method
   public Toolkit super$2$getToolkit() {
      return super.getToolkit();
   }

   // $FF: synthetic method
   public void super$3$setFocusTraversalPolicy(FocusTraversalPolicy var1) {
      super.setFocusTraversalPolicy(var1);
   }

   // $FF: synthetic method
   public void super$2$setEnabled(boolean var1) {
      super.setEnabled(var1);
   }

   // $FF: synthetic method
   public void super$2$removeHierarchyListener(HierarchyListener var1) {
      super.removeHierarchyListener(var1);
   }

   // $FF: synthetic method
   public InputMethodRequests super$2$getInputMethodRequests() {
      return super.getInputMethodRequests();
   }

   // $FF: synthetic method
   public String[][] super$5$getParameterInfo() {
      return super.getParameterInfo();
   }

   // $FF: synthetic method
   public void super$2$list(PrintStream var1) {
      super.list(var1);
   }

   // $FF: synthetic method
   public void super$5$play(URL var1) {
      super.play(var1);
   }

   // $FF: synthetic method
   public void super$2$firePropertyChange(String var1, int var2, int var3) {
      super.firePropertyChange(var1, var2, var3);
   }

   // $FF: synthetic method
   public ComponentListener[] super$2$getComponentListeners() {
      return super.getComponentListeners();
   }

   // $FF: synthetic method
   public boolean super$3$isAncestorOf(Component var1) {
      return super.isAncestorOf(var1);
   }

   // $FF: synthetic method
   public boolean super$2$isDoubleBuffered() {
      return super.isDoubleBuffered();
   }

   // $FF: synthetic method
   public GraphicsConfiguration super$2$getGraphicsConfiguration() {
      return super.getGraphicsConfiguration();
   }

   // $FF: synthetic method
   public boolean super$2$requestFocusInWindow(boolean var1) {
      return super.requestFocusInWindow(var1);
   }

   // $FF: synthetic method
   public void super$2$processFocusEvent(FocusEvent var1) {
      super.processFocusEvent(var1);
   }

   // $FF: synthetic method
   public void super$3$addPropertyChangeListener(PropertyChangeListener var1) {
      super.addPropertyChangeListener(var1);
   }

   // $FF: synthetic method
   public void super$1$wait(long var1, int var3) {
      super.wait(var1, var3);
   }

   // $FF: synthetic method
   public void super$3$layout() {
      super.layout();
   }

   // $FF: synthetic method
   public Component super$3$findComponentAt(Point var1) {
      return super.findComponentAt(var1);
   }

   // $FF: synthetic method
   public boolean super$3$isFocusCycleRoot() {
      return super.isFocusCycleRoot();
   }

   // $FF: synthetic method
   public void super$3$setFont(Font var1) {
      super.setFont(var1);
   }

   // $FF: synthetic method
   public boolean super$2$mouseDrag(Event var1, int var2, int var3) {
      return super.mouseDrag(var1, var2, var3);
   }

   // $FF: synthetic method
   public boolean super$2$inside(int var1, int var2) {
      return super.inside(var1, var2);
   }

   // $FF: synthetic method
   public Image super$2$createImage(ImageProducer var1) {
      return super.createImage(var1);
   }

   // $FF: synthetic method
   public boolean super$2$keyUp(Event var1, int var2) {
      return super.keyUp(var1, var2);
   }

   // $FF: synthetic method
   public boolean super$5$isActive() {
      return super.isActive();
   }

   // $FF: synthetic method
   public MouseMotionListener[] super$2$getMouseMotionListeners() {
      return super.getMouseMotionListeners();
   }

   // $FF: synthetic method
   public void super$2$firePropertyChange(String var1, long var2, long var4) {
      super.firePropertyChange(var1, var2, var4);
   }

   // $FF: synthetic method
   public void super$5$resize(int var1, int var2) {
      super.resize(var1, var2);
   }

   // $FF: synthetic method
   public void super$6$setRootPaneCheckingEnabled(boolean var1) {
      super.setRootPaneCheckingEnabled(var1);
   }

   // $FF: synthetic method
   public void super$2$setBounds(Rectangle var1) {
      super.setBounds(var1);
   }

   // $FF: synthetic method
   public JMenuBar super$6$getJMenuBar() {
      return super.getJMenuBar();
   }

   // $FF: synthetic method
   public void super$3$addContainerListener(ContainerListener var1) {
      super.addContainerListener(var1);
   }

   // $FF: synthetic method
   public boolean super$2$getFocusTraversalKeysEnabled() {
      return super.getFocusTraversalKeysEnabled();
   }

   // $FF: synthetic method
   public String super$5$getParameter(String var1) {
      return super.getParameter(var1);
   }

   // $FF: synthetic method
   public void super$3$list(PrintStream var1, int var2) {
      super.list(var1, var2);
   }

   // $FF: synthetic method
   public String super$6$paramString() {
      return super.paramString();
   }

   // $FF: synthetic method
   public boolean super$2$isValid() {
      return super.isValid();
   }

   // $FF: synthetic method
   public void super$2$setLocation(Point var1) {
      super.setLocation(var1);
   }

   // $FF: synthetic method
   public Image super$5$getImage(URL var1, String var2) {
      return super.getImage(var1, var2);
   }

   // $FF: synthetic method
   public void super$2$firePropertyChange(String var1, char var2, char var3) {
      super.firePropertyChange(var1, var2, var3);
   }

   // $FF: synthetic method
   public void super$3$removeNotify() {
      super.removeNotify();
   }

   // $FF: synthetic method
   public String super$2$getName() {
      return super.getName();
   }

   // $FF: synthetic method
   public void super$2$firePropertyChange(String var1, boolean var2, boolean var3) {
      super.firePropertyChange(var1, var2, var3);
   }

   // $FF: synthetic method
   public void super$2$addHierarchyBoundsListener(HierarchyBoundsListener var1) {
      super.addHierarchyBoundsListener(var1);
   }

   // $FF: synthetic method
   public void super$2$processHierarchyEvent(HierarchyEvent var1) {
      super.processHierarchyEvent(var1);
   }

   // $FF: synthetic method
   public boolean super$2$contains(Point var1) {
      return super.contains(var1);
   }

   // $FF: synthetic method
   public void super$2$removeHierarchyBoundsListener(HierarchyBoundsListener var1) {
      super.removeHierarchyBoundsListener(var1);
   }

   // $FF: synthetic method
   public void super$5$play(URL var1, String var2) {
      super.play(var1, var2);
   }

   // $FF: synthetic method
   public KeyListener[] super$2$getKeyListeners() {
      return super.getKeyListeners();
   }

   // $FF: synthetic method
   public Component[] super$3$getComponents() {
      return super.getComponents();
   }

   // $FF: synthetic method
   public boolean super$2$requestFocusInWindow() {
      return super.requestFocusInWindow();
   }

   // $FF: synthetic method
   public void super$2$addInputMethodListener(InputMethodListener var1) {
      super.addInputMethodListener(var1);
   }

   // $FF: synthetic method
   public InputContext super$2$getInputContext() {
      return super.getInputContext();
   }

   // $FF: synthetic method
   public PropertyChangeListener[] super$2$getPropertyChangeListeners(String var1) {
      return super.getPropertyChangeListeners(var1);
   }

   // $FF: synthetic method
   public boolean super$2$prepareImage(Image var1, int var2, int var3, ImageObserver var4) {
      return super.prepareImage(var1, var2, var3, var4);
   }

   // $FF: synthetic method
   public int super$3$getComponentCount() {
      return super.getComponentCount();
   }

   // $FF: synthetic method
   public void super$2$setDropTarget(DropTarget var1) {
      super.setDropTarget(var1);
   }

   // $FF: synthetic method
   public boolean super$2$prepareImage(Image var1, ImageObserver var2) {
      return super.prepareImage(var1, var2);
   }

   // $FF: synthetic method
   public void super$2$repaint(long var1) {
      super.repaint(var1);
   }

   // $FF: synthetic method
   public Insets super$3$insets() {
      return super.insets();
   }

   // $FF: synthetic method
   public void super$3$doLayout() {
      super.doLayout();
   }

   // $FF: synthetic method
   public boolean super$2$mouseExit(Event var1, int var2, int var3) {
      return super.mouseExit(var1, var2, var3);
   }

   // $FF: synthetic method
   public void super$3$deliverEvent(Event var1) {
      super.deliverEvent(var1);
   }

   // $FF: synthetic method
   public void super$2$processMouseMotionEvent(MouseEvent var1) {
      super.processMouseMotionEvent(var1);
   }

   // $FF: synthetic method
   public boolean super$2$isLightweight() {
      return super.isLightweight();
   }

   // $FF: synthetic method
   public void super$2$firePropertyChange(String var1, Object var2, Object var3) {
      super.firePropertyChange(var1, var2, var3);
   }

   // $FF: synthetic method
   public Container super$2$getFocusCycleRootAncestor() {
      return super.getFocusCycleRootAncestor();
   }

   // $FF: synthetic method
   public void super$2$enableEvents(long var1) {
      super.enableEvents(var1);
   }

   // $FF: synthetic method
   public void super$2$setMinimumSize(Dimension var1) {
      super.setMinimumSize(var1);
   }

   // $FF: synthetic method
   public Container super$2$getParent() {
      return super.getParent();
   }

   // $FF: synthetic method
   public BaselineResizeBehavior super$2$getBaselineResizeBehavior() {
      return super.getBaselineResizeBehavior();
   }

   // $FF: synthetic method
   public URL super$5$getDocumentBase() {
      return super.getDocumentBase();
   }

   // $FF: synthetic method
   public Graphics super$6$getGraphics() {
      return super.getGraphics();
   }

   // $FF: synthetic method
   public void super$2$addMouseMotionListener(MouseMotionListener var1) {
      super.addMouseMotionListener(var1);
   }

   // $FF: synthetic method
   public void super$2$addMouseWheelListener(MouseWheelListener var1) {
      super.addMouseWheelListener(var1);
   }

   // $FF: synthetic method
   public void super$2$setBounds(int var1, int var2, int var3, int var4) {
      super.setBounds(var1, var2, var3, var4);
   }

   // $FF: synthetic method
   public boolean super$2$handleEvent(Event var1) {
      return super.handleEvent(var1);
   }

   // $FF: synthetic method
   public HierarchyListener[] super$2$getHierarchyListeners() {
      return super.getHierarchyListeners();
   }

   // $FF: synthetic method
   public void super$3$processEvent(AWTEvent var1) {
      super.processEvent(var1);
   }

   // $FF: synthetic method
   public void super$3$transferFocusDownCycle() {
      super.transferFocusDownCycle();
   }

   // $FF: synthetic method
   public boolean super$2$isFocusTraversable() {
      return super.isFocusTraversable();
   }

   // $FF: synthetic method
   public Dimension super$3$minimumSize() {
      return super.minimumSize();
   }

   // $FF: synthetic method
   public boolean super$2$mouseMove(Event var1, int var2, int var3) {
      return super.mouseMove(var1, var2, var3);
   }

   // $FF: synthetic method
   public int super$3$getComponentZOrder(Component var1) {
      return super.getComponentZOrder(var1);
   }

   // $FF: synthetic method
   public FontMetrics super$2$getFontMetrics(Font var1) {
      return super.getFontMetrics(var1);
   }

   // $FF: synthetic method
   public void super$3$removeAll() {
      super.removeAll();
   }

   // $FF: synthetic method
   public Point super$2$getMousePosition() {
      return super.getMousePosition();
   }

   // $FF: synthetic method
   public Image super$5$getImage(URL var1) {
      return super.getImage(var1);
   }

   // $FF: synthetic method
   public void super$2$show(boolean var1) {
      super.show(var1);
   }

   // $FF: synthetic method
   public void super$5$stop() {
      super.stop();
   }

   // $FF: synthetic method
   public Dimension super$3$getMinimumSize() {
      return super.getMinimumSize();
   }

   // $FF: synthetic method
   public void super$2$transferFocus() {
      super.transferFocus();
   }

   // $FF: synthetic method
   public void super$2$enable(boolean var1) {
      super.enable(var1);
   }

   // $FF: synthetic method
   public void super$2$dispatchEvent(AWTEvent var1) {
      super.dispatchEvent(var1);
   }

   // $FF: synthetic method
   public void super$2$firePropertyChange(String var1, short var2, short var3) {
      super.firePropertyChange(var1, var2, var3);
   }

   // $FF: synthetic method
   public void super$2$removeKeyListener(KeyListener var1) {
      super.removeKeyListener(var1);
   }

   // $FF: synthetic method
   public void super$2$disableEvents(long var1) {
      super.disableEvents(var1);
   }

   // $FF: synthetic method
   public boolean super$3$areFocusTraversalKeysSet(int var1) {
      return super.areFocusTraversalKeysSet(var1);
   }

   // $FF: synthetic method
   public void super$2$reshape(int var1, int var2, int var3, int var4) {
      super.reshape(var1, var2, var3, var4);
   }

   // $FF: synthetic method
   public void super$6$setContentPane(Container var1) {
      super.setContentPane(var1);
   }

   // $FF: synthetic method
   public void super$2$setForeground(Color var1) {
      super.setForeground(var1);
   }

   // $FF: synthetic method
   public void super$6$setTransferHandler(TransferHandler var1) {
      super.setTransferHandler(var1);
   }

   // $FF: synthetic method
   public boolean super$2$isMinimumSizeSet() {
      return super.isMinimumSizeSet();
   }

   // $FF: synthetic method
   public void super$3$add(Component var1, Object var2, int var3) {
      super.add(var1, var2, var3);
   }

   // $FF: synthetic method
   public boolean super$2$isBackgroundSet() {
      return super.isBackgroundSet();
   }

   // $FF: synthetic method
   public String super$5$getAppletInfo() {
      return super.getAppletInfo();
   }

   // $FF: synthetic method
   public JRootPane super$6$getRootPane() {
      return super.getRootPane();
   }

   // $FF: synthetic method
   public int super$2$checkImage(Image var1, int var2, int var3, ImageObserver var4) {
      return super.checkImage(var1, var2, var3, var4);
   }

   // $FF: synthetic method
   public void super$2$transferFocusUpCycle() {
      super.transferFocusUpCycle();
   }

   // $FF: synthetic method
   public void super$2$list(PrintWriter var1) {
      super.list(var1);
   }

   // $FF: synthetic method
   public boolean super$2$isMaximumSizeSet() {
      return super.isMaximumSizeSet();
   }

   // $FF: synthetic method
   public void super$3$setFocusTraversalKeys(int var1, Set var2) {
      super.setFocusTraversalKeys(var1, var2);
   }

   // $FF: synthetic method
   public Point super$2$getLocation(Point var1) {
      return super.getLocation(var1);
   }

   // $FF: synthetic method
   public ContainerListener[] super$3$getContainerListeners() {
      return super.getContainerListeners();
   }

   // $FF: synthetic method
   public void super$2$setLocation(int var1, int var2) {
      super.setLocation(var1, var2);
   }

   // $FF: synthetic method
   public void super$2$setName(String var1) {
      super.setName(var1);
   }

   // $FF: synthetic method
   public void super$2$removeMouseListener(MouseListener var1) {
      super.removeMouseListener(var1);
   }

   // $FF: synthetic method
   public void super$6$update(Graphics var1) {
      super.update(var1);
   }

   // $FF: synthetic method
   public void super$2$addComponentListener(ComponentListener var1) {
      super.addComponentListener(var1);
   }

   // $FF: synthetic method
   public Object super$2$getTreeLock() {
      return super.getTreeLock();
   }

   // $FF: synthetic method
   public Rectangle super$2$getBounds(Rectangle var1) {
      return super.getBounds(var1);
   }

   // $FF: synthetic method
   public boolean super$2$isFocusOwner() {
      return super.isFocusOwner();
   }

   // $FF: synthetic method
   public void super$3$paintComponents(Graphics var1) {
      super.paintComponents(var1);
   }

   // $FF: synthetic method
   public void super$2$nextFocus() {
      super.nextFocus();
   }

   // $FF: synthetic method
   public void super$2$setLocale(Locale var1) {
      super.setLocale(var1);
   }

   // $FF: synthetic method
   public void super$2$setVisible(boolean var1) {
      super.setVisible(var1);
   }

   // $FF: synthetic method
   public Color super$2$getBackground() {
      return super.getBackground();
   }

   // $FF: synthetic method
   public void super$2$setPreferredSize(Dimension var1) {
      super.setPreferredSize(var1);
   }

   // $FF: synthetic method
   public boolean super$2$isPreferredSizeSet() {
      return super.isPreferredSizeSet();
   }

   // $FF: synthetic method
   public boolean super$2$hasFocus() {
      return super.hasFocus();
   }

   // $FF: synthetic method
   public void super$2$setFocusTraversalKeysEnabled(boolean var1) {
      super.setFocusTraversalKeysEnabled(var1);
   }

   // $FF: synthetic method
   public void super$2$processInputMethodEvent(InputMethodEvent var1) {
      super.processInputMethodEvent(var1);
   }

   // $FF: synthetic method
   public int super$2$checkImage(Image var1, ImageObserver var2) {
      return super.checkImage(var1, var2);
   }

   // $FF: synthetic method
   public void super$2$firePropertyChange(String var1, byte var2, byte var3) {
      super.firePropertyChange(var1, var2, var3);
   }

   // $FF: synthetic method
   public float super$3$getAlignmentX() {
      return super.getAlignmentX();
   }

   // $FF: synthetic method
   public void super$2$processMouseEvent(MouseEvent var1) {
      super.processMouseEvent(var1);
   }

   // $FF: synthetic method
   public AppletContext super$5$getAppletContext() {
      return super.getAppletContext();
   }

   // $FF: synthetic method
   public Component super$3$add(Component var1) {
      return super.add(var1);
   }

   // $FF: synthetic method
   public void super$2$processHierarchyBoundsEvent(HierarchyEvent var1) {
      super.processHierarchyBoundsEvent(var1);
   }

   // $FF: synthetic method
   public boolean super$2$keyDown(Event var1, int var2) {
      return super.keyDown(var1, var2);
   }

   // $FF: synthetic method
   public URL super$5$getCodeBase() {
      return super.getCodeBase();
   }

   // $FF: synthetic method
   public void super$6$setRootPane(JRootPane var1) {
      super.setRootPane(var1);
   }

   // $FF: synthetic method
   public void super$2$setBackground(Color var1) {
      super.setBackground(var1);
   }

   // $FF: synthetic method
   public void super$6$setJMenuBar(JMenuBar var1) {
      super.setJMenuBar(var1);
   }

   // $FF: synthetic method
   public VolatileImage super$2$createVolatileImage(int var1, int var2, ImageCapabilities var3) {
      return super.createVolatileImage(var1, var2, var3);
   }

   // $FF: synthetic method
   public Component super$3$add(String var1, Component var2) {
      return super.add(var1, var2);
   }

   // $FF: synthetic method
   public void super$2$setMaximumSize(Dimension var1) {
      super.setMaximumSize(var1);
   }

   // $FF: synthetic method
   public int super$2$getWidth() {
      return super.getWidth();
   }

   // $FF: synthetic method
   public JLayeredPane super$6$getLayeredPane() {
      return super.getLayeredPane();
   }

   // $FF: synthetic method
   public void super$1$wait() {
      super.wait();
   }

   // $FF: synthetic method
   public void super$3$setFocusTraversalPolicyProvider(boolean var1) {
      super.setFocusTraversalPolicyProvider(var1);
   }

   // $FF: synthetic method
   public AccessibleContext super$6$getAccessibleContext() {
      return super.getAccessibleContext();
   }

   // $FF: synthetic method
   public boolean super$2$mouseDown(Event var1, int var2, int var3) {
      return super.mouseDown(var1, var2, var3);
   }

   // $FF: synthetic method
   public void super$2$removeFocusListener(FocusListener var1) {
      super.removeFocusListener(var1);
   }

   // $FF: synthetic method
   public void super$2$addKeyListener(KeyListener var1) {
      super.addKeyListener(var1);
   }

   // $FF: synthetic method
   public void super$2$removePropertyChangeListener(PropertyChangeListener var1) {
      super.removePropertyChangeListener(var1);
   }

   // $FF: synthetic method
   public void super$2$remove(MenuComponent var1) {
      super.remove(var1);
   }

   // $FF: synthetic method
   public Component super$3$add(Component var1, int var2) {
      return super.add(var1, var2);
   }

   // $FF: synthetic method
   public void super$2$processKeyEvent(KeyEvent var1) {
      super.processKeyEvent(var1);
   }

   // $FF: synthetic method
   public void super$2$list() {
      super.list();
   }

   // $FF: synthetic method
   public void super$2$paintAll(Graphics var1) {
      super.paintAll(var1);
   }

   // $FF: synthetic method
   public Font super$2$getFont() {
      return super.getFont();
   }

   // $FF: synthetic method
   public Dimension super$2$size() {
      return super.size();
   }

   // $FF: synthetic method
   public boolean super$2$mouseUp(Event var1, int var2, int var3) {
      return super.mouseUp(var1, var2, var3);
   }

   // $FF: synthetic method
   public boolean super$2$isFocusable() {
      return super.isFocusable();
   }

   // $FF: synthetic method
   public VolatileImage super$2$createVolatileImage(int var1, int var2) {
      return super.createVolatileImage(var1, var2);
   }

   // $FF: synthetic method
   public void super$3$applyComponentOrientation(ComponentOrientation var1) {
      super.applyComponentOrientation(var1);
   }

   // $FF: synthetic method
   public void super$2$repaint() {
      super.repaint();
   }

   // $FF: synthetic method
   public int super$2$getHeight() {
      return super.getHeight();
   }

   // $FF: synthetic method
   public void super$1$finalize() {
      super.finalize();
   }

   // $FF: synthetic method
   public float super$3$getAlignmentY() {
      return super.getAlignmentY();
   }

   // $FF: synthetic method
   public void super$2$disable() {
      super.disable();
   }

   // $FF: synthetic method
   public ComponentPeer super$2$getPeer() {
      return super.getPeer();
   }

   // $FF: synthetic method
   public boolean super$1$equals(Object var1) {
      return super.equals(var1);
   }

   // $FF: synthetic method
   private static void $createCallSiteArray_1(String[] var0) {
      var0[0] = "<$constructor$>";
      var0[1] = "run";
      var0[2] = "exit";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[3];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$groovy$ui$ConsoleApplet(), var0);
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
   private static Class $get$$class$groovy$ui$ConsoleApplet() {
      Class var10000 = $class$groovy$ui$ConsoleApplet;
      if (var10000 == null) {
         var10000 = $class$groovy$ui$ConsoleApplet = class$("groovy.ui.ConsoleApplet");
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
   private static Class $get$$class$groovy$ui$Console() {
      Class var10000 = $class$groovy$ui$Console;
      if (var10000 == null) {
         var10000 = $class$groovy$ui$Console = class$("groovy.ui.Console");
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
