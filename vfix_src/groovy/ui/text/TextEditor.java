package groovy.ui.text;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.print.PageFormat;
import java.awt.print.Pageable;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.Calendar;
import java.util.regex.Pattern;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.plaf.ComponentUI;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.DefaultCaret;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import javax.swing.text.StyledDocument;
import javax.swing.text.Utilities;

public class TextEditor extends JTextPane implements Pageable, Printable {
   public static final String FIND = "Find...";
   public static final String FIND_NEXT = "Find Next";
   public static final String FIND_PREVIOUS = "Find Previous";
   public static final String REPLACE = "Replace...";
   public static final String AUTO_INDENT = "AutoIndent";
   private static final String TABBED_SPACES = "    ";
   private static final Pattern TAB_BACK_PATTERN = Pattern.compile("^(([\t])|(    )|(   )|(  )|( ))", 8);
   private static final Pattern LINE_START = Pattern.compile("^", 8);
   private static final JTextPane PRINT_PANE = new JTextPane();
   private static final Dimension PRINT_SIZE = new Dimension();
   private static Toolkit toolkit = Toolkit.getDefaultToolkit();
   private static boolean isOvertypeMode;
   private Caret defaultCaret;
   private Caret overtypeCaret;
   private static final PageFormat PAGE_FORMAT;
   private int numPages;
   private int lastUpdate;
   private MouseAdapter mouseAdapter;
   private boolean unwrapped;
   private boolean tabsAsSpaces;
   private boolean multiLineTab;
   private boolean searchable;

   public TextEditor() {
      this(false);
   }

   public TextEditor(boolean tabsAsSpaces) {
      this(tabsAsSpaces, false);
   }

   public TextEditor(boolean tabsAsSpaces, boolean multiLineTab) {
      this(multiLineTab, tabsAsSpaces, false);
   }

   public TextEditor(boolean tabsAsSpaces, boolean multiLineTab, boolean unwrapped) {
      this.mouseAdapter = new MouseAdapter() {
         Cursor cursor;

         public void mouseEntered(MouseEvent me) {
            if (TextEditor.this.contains(me.getPoint())) {
               this.cursor = TextEditor.this.getCursor();
               Cursor curs = Cursor.getPredefinedCursor(2);
               TextEditor.this.getRootPane().getLayeredPane().setCursor(curs);
            } else {
               TextEditor.this.getRootPane().getLayeredPane().setCursor(this.cursor);
            }

         }

         public void mouseExited(MouseEvent me) {
            TextEditor.this.getRootPane().getLayeredPane().setCursor((Cursor)null);
         }
      };
      this.searchable = true;
      this.tabsAsSpaces = tabsAsSpaces;
      this.multiLineTab = multiLineTab;
      this.unwrapped = unwrapped;
      ActionMap aMap = this.getActionMap();
      Action action = null;

      do {
         action = action == null ? aMap.get("delete-previous") : null;
         aMap.remove("delete-previous");
         aMap = aMap.getParent();
      } while(aMap != null);

      aMap = this.getActionMap();
      InputMap iMap = this.getInputMap();
      KeyStroke keyStroke = KeyStroke.getKeyStroke(8, 0, false);
      iMap.put(keyStroke, "delete");
      keyStroke = KeyStroke.getKeyStroke(8, 1, false);
      iMap.put(keyStroke, "delete");
      aMap.put("delete", action);
      Action action = new TextEditor.FindAction();
      aMap.put("Find...", action);
      keyStroke = KeyStroke.getKeyStroke(70, 2, false);
      iMap.put(keyStroke, "Find...");
      aMap.put("Find Next", FindReplaceUtility.FIND_ACTION);
      keyStroke = KeyStroke.getKeyStroke(114, 0, false);
      iMap.put(keyStroke, "Find Next");
      aMap.put("Find Previous", FindReplaceUtility.FIND_ACTION);
      keyStroke = KeyStroke.getKeyStroke(114, 1, false);
      iMap.put(keyStroke, "Find Previous");
      Action action = new TextEditor.TabAction();
      aMap.put("TextEditor-tabAction", action);
      keyStroke = KeyStroke.getKeyStroke(9, 0, false);
      iMap.put(keyStroke, "TextEditor-tabAction");
      Action action = new TextEditor.ShiftTabAction();
      aMap.put("TextEditor-shiftTabAction", action);
      keyStroke = KeyStroke.getKeyStroke(9, 1, false);
      iMap.put(keyStroke, "TextEditor-shiftTabAction");
      Action action = new TextEditor.ReplaceAction();
      this.getActionMap().put("Replace...", action);
      keyStroke = KeyStroke.getKeyStroke(72, 2, false);

      do {
         iMap.remove(keyStroke);
         iMap = iMap.getParent();
      } while(iMap != null);

      this.getInputMap().put(keyStroke, "Replace...");
      Action action = new AutoIndentAction();
      this.getActionMap().put("AutoIndent", action);
      keyStroke = KeyStroke.getKeyStroke(10, 0, false);
      this.getInputMap().put(keyStroke, "AutoIndent");
      this.setAutoscrolls(true);
      this.defaultCaret = this.getCaret();
      this.overtypeCaret = new TextEditor.OvertypeCaret();
      this.overtypeCaret.setBlinkRate(this.defaultCaret.getBlinkRate());
   }

   public void addNotify() {
      super.addNotify();
      this.addMouseListener(this.mouseAdapter);
      FindReplaceUtility.registerTextComponent(this);
   }

   public int getNumberOfPages() {
      StyledDocument doc = (StyledDocument)this.getDocument();
      Paper paper = PAGE_FORMAT.getPaper();
      this.numPages = (int)Math.ceil(this.getSize().getHeight() / paper.getImageableHeight());
      return this.numPages;
   }

   public PageFormat getPageFormat(int pageIndex) throws IndexOutOfBoundsException {
      return PAGE_FORMAT;
   }

   public Printable getPrintable(int param) throws IndexOutOfBoundsException {
      return this;
   }

   public int print(Graphics graphics, PageFormat pageFormat, int page) throws PrinterException {
      if (page < this.numPages) {
         StyledDocument doc = (StyledDocument)this.getDocument();
         Paper paper = pageFormat.getPaper();
         PRINT_PANE.setDocument(this.getDocument());
         PRINT_PANE.setFont(this.getFont());
         PRINT_SIZE.setSize(paper.getImageableWidth(), this.getSize().getHeight());
         PRINT_PANE.setSize(PRINT_SIZE);
         double y = -((double)page * paper.getImageableHeight()) + paper.getImageableY();
         ((Graphics2D)graphics).translate(paper.getImageableX(), y);
         PRINT_PANE.print(graphics);
         ((Graphics2D)graphics).translate(0.0D, -y);
         Rectangle rect = graphics.getClipBounds();
         graphics.setClip(rect.x, 0, rect.width, (int)paper.getHeight() + 100);
         Calendar cal = Calendar.getInstance();
         String header = cal.getTime().toString().trim();
         String name = this.getName() == null ? System.getProperty("user.name").trim() : this.getName().trim();
         String pageStr = String.valueOf(page + 1);
         Font font = Font.decode("Monospaced 8");
         graphics.setFont(font);
         FontMetrics fm = graphics.getFontMetrics(font);
         int width = SwingUtilities.computeStringWidth(fm, header);
         ((Graphics2D)graphics).drawString(header, (float)(paper.getImageableWidth() / 2.0D - (double)(width / 2)), (float)paper.getImageableY() / 2.0F + (float)fm.getHeight());
         ((Graphics2D)graphics).translate(0.0D, paper.getImageableY() - (double)fm.getHeight());
         double height = paper.getImageableHeight() + paper.getImageableY() / 2.0D;
         width = SwingUtilities.computeStringWidth(fm, name);
         ((Graphics2D)graphics).drawString(name, (float)(paper.getImageableWidth() / 2.0D - (double)(width / 2)), (float)height - (float)(fm.getHeight() / 2));
         ((Graphics2D)graphics).translate(0, fm.getHeight());
         width = SwingUtilities.computeStringWidth(fm, pageStr);
         ((Graphics2D)graphics).drawString(pageStr, (float)(paper.getImageableWidth() / 2.0D - (double)(width / 2)), (float)height - (float)(fm.getHeight() / 2));
         return 0;
      } else {
         return 1;
      }
   }

   public boolean getScrollableTracksViewportWidth() {
      boolean bool = super.getScrollableTracksViewportWidth();
      if (this.unwrapped) {
         Component parent = this.getParent();
         ComponentUI ui = this.getUI();
         int uiWidth = ui.getPreferredSize(this).width;
         int parentWidth = parent.getSize().width;
         bool = parent != null ? ui.getPreferredSize(this).width < parent.getSize().width : true;
      }

      return bool;
   }

   public boolean isMultiLineTabbed() {
      return this.multiLineTab;
   }

   public static boolean isOvertypeMode() {
      return isOvertypeMode;
   }

   public boolean isTabsAsSpaces() {
      return this.tabsAsSpaces;
   }

   public boolean isUnwrapped() {
      return this.unwrapped;
   }

   protected void processKeyEvent(KeyEvent e) {
      super.processKeyEvent(e);
      if (e.getID() == 402 && e.getKeyCode() == 155) {
         this.setOvertypeMode(!isOvertypeMode());
      }

   }

   public void removeNotify() {
      super.removeNotify();
      this.removeMouseListener(this.mouseAdapter);
      FindReplaceUtility.unregisterTextComponent(this);
   }

   public void replaceSelection(String text) {
      if (isOvertypeMode()) {
         int pos = this.getCaretPosition();
         if (this.getSelectedText() == null && pos < this.getDocument().getLength()) {
            this.moveCaretPosition(pos + 1);
         }
      }

      super.replaceSelection(text);
   }

   public void setBounds(int x, int y, int width, int height) {
      if (this.unwrapped) {
         Dimension size = this.getPreferredSize();
         super.setBounds(x, y, Math.max(size.width, width), Math.max(size.height, height));
      } else {
         super.setBounds(x, y, width, height);
      }

   }

   public void isMultiLineTabbed(boolean multiLineTab) {
      this.multiLineTab = multiLineTab;
   }

   public void isTabsAsSpaces(boolean tabsAsSpaces) {
      this.tabsAsSpaces = tabsAsSpaces;
   }

   public void setOvertypeMode(boolean isOvertypeMode) {
      TextEditor.isOvertypeMode = isOvertypeMode;
      int pos = this.getCaretPosition();
      if (isOvertypeMode()) {
         this.setCaret(this.overtypeCaret);
      } else {
         this.setCaret(this.defaultCaret);
      }

      this.setCaretPosition(pos);
   }

   public void setUnwrapped(boolean unwrapped) {
      this.unwrapped = unwrapped;
   }

   static {
      PrinterJob job = PrinterJob.getPrinterJob();
      PAGE_FORMAT = job.defaultPage();
   }

   private class OvertypeCaret extends DefaultCaret {
      private OvertypeCaret() {
      }

      public void paint(Graphics g) {
         if (this.isVisible()) {
            try {
               JTextComponent component = this.getComponent();
               Rectangle r = component.getUI().modelToView(component, this.getDot());
               Color c = g.getColor();
               g.setColor(component.getBackground());
               g.setXORMode(component.getCaretColor());
               r.setBounds(r.x, r.y, g.getFontMetrics().charWidth('w'), g.getFontMetrics().getHeight());
               g.fillRect(r.x, r.y, r.width, r.height);
               g.setPaintMode();
               g.setColor(c);
            } catch (BadLocationException var5) {
               var5.printStackTrace();
            }
         }

      }

      protected synchronized void damage(Rectangle r) {
         if (r != null) {
            JTextComponent component = this.getComponent();
            this.x = r.x;
            this.y = r.y;
            Font font = component.getFont();
            this.width = component.getFontMetrics(font).charWidth('w');
            this.height = r.height;
            this.repaint();
         }

      }

      // $FF: synthetic method
      OvertypeCaret(Object x1) {
         this();
      }
   }

   private class TabAction extends AbstractAction {
      private TabAction() {
      }

      public void actionPerformed(ActionEvent ae) {
         try {
            Document doc = TextEditor.this.getDocument();
            String text = TextEditor.this.tabsAsSpaces ? "    " : "\t";
            int pos;
            if (TextEditor.this.multiLineTab && TextEditor.this.getSelectedText() != null) {
               pos = Utilities.getRowEnd(TextEditor.this, TextEditor.this.getSelectionEnd());
               TextEditor.this.setSelectionEnd(pos);
               Element el = Utilities.getParagraphElement(TextEditor.this, TextEditor.this.getSelectionStart());
               int start = el.getStartOffset();
               TextEditor.this.setSelectionStart(start);
               String toReplace = TextEditor.this.getSelectedText();
               toReplace = TextEditor.LINE_START.matcher(toReplace).replaceAll(text);
               TextEditor.this.replaceSelection(toReplace);
               TextEditor.this.select(start, start + toReplace.length());
            } else {
               pos = TextEditor.this.getCaretPosition();
               doc.insertString(pos, text, (AttributeSet)null);
            }
         } catch (Exception var8) {
            var8.printStackTrace();
         }

      }

      // $FF: synthetic method
      TabAction(Object x1) {
         this();
      }
   }

   private class ShiftTabAction extends AbstractAction {
      private ShiftTabAction() {
      }

      public void actionPerformed(ActionEvent ae) {
         try {
            if (TextEditor.this.multiLineTab && TextEditor.this.getSelectedText() != null) {
               Document doc = TextEditor.this.getDocument();
               int end = Utilities.getRowEnd(TextEditor.this, TextEditor.this.getSelectionEnd());
               TextEditor.this.setSelectionEnd(end);
               Element el = Utilities.getParagraphElement(TextEditor.this, TextEditor.this.getSelectionStart());
               int start = el.getStartOffset();
               TextEditor.this.setSelectionStart(start);
               String text = TextEditor.this.tabsAsSpaces ? TextEditor.TAB_BACK_PATTERN.matcher(TextEditor.this.getSelectedText()).replaceAll("") : TextEditor.this.getSelectedText().replaceAll("^\t", "");
               TextEditor.this.replaceSelection(text);
               TextEditor.this.select(start, start + text.length());
            }
         } catch (Exception var7) {
            var7.printStackTrace();
         }

      }

      // $FF: synthetic method
      ShiftTabAction(Object x1) {
         this();
      }
   }

   private class ReplaceAction extends AbstractAction {
      private ReplaceAction() {
      }

      public void actionPerformed(ActionEvent ae) {
         FindReplaceUtility.showDialog(true);
      }

      // $FF: synthetic method
      ReplaceAction(Object x1) {
         this();
      }
   }

   private class FindAction extends AbstractAction {
      private FindAction() {
      }

      public void actionPerformed(ActionEvent ae) {
         FindReplaceUtility.showDialog();
      }

      // $FF: synthetic method
      FindAction(Object x1) {
         this();
      }
   }
}
