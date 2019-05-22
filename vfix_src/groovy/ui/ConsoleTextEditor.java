package groovy.ui;

import groovy.ui.text.GroovyFilter;
import groovy.ui.text.StructuredSyntaxResources;
import groovy.ui.text.TextEditor;
import groovy.ui.text.TextUndoManager;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.print.PrinterJob;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.prefs.Preferences;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import org.codehaus.groovy.runtime.DefaultGroovyMethods;

public class ConsoleTextEditor extends JScrollPane {
   private String defaultFamily = "Monospaced";
   private static final PrinterJob PRINTER_JOB = PrinterJob.getPrinterJob();
   private ConsoleTextEditor.LineNumbersPanel numbersPanel = new ConsoleTextEditor.LineNumbersPanel();
   private boolean documentChangedSinceLastRepaint = false;
   private TextEditor textEditor = new TextEditor(true, true, true) {
      public void paintComponent(Graphics g) {
         super.paintComponent(g);
         if (ConsoleTextEditor.this.documentChangedSinceLastRepaint) {
            ConsoleTextEditor.this.numbersPanel.repaint();
            ConsoleTextEditor.this.documentChangedSinceLastRepaint = false;
         }

      }
   };
   private ConsoleTextEditor.UndoAction undoAction = new ConsoleTextEditor.UndoAction();
   private ConsoleTextEditor.RedoAction redoAction = new ConsoleTextEditor.RedoAction();
   private ConsoleTextEditor.PrintAction printAction = new ConsoleTextEditor.PrintAction();
   private boolean editable = true;
   private TextUndoManager undoManager;

   public String getDefaultFamily() {
      return this.defaultFamily;
   }

   public void setDefaultFamily(String defaultFamily) {
      this.defaultFamily = defaultFamily;
   }

   public ConsoleTextEditor() {
      this.textEditor.setFont(new Font(this.defaultFamily, 0, Preferences.userNodeForPackage(Console.class).getInt("fontSize", 12)));
      this.setViewportView(new JPanel(new BorderLayout()) {
         {
            this.add(ConsoleTextEditor.this.numbersPanel, "West");
            this.add(ConsoleTextEditor.this.textEditor, "Center");
         }
      });
      this.textEditor.setDragEnabled(this.editable);
      this.getVerticalScrollBar().setUnitIncrement(10);
      this.initActions();
      DefaultStyledDocument doc = new DefaultStyledDocument();
      doc.setDocumentFilter(new GroovyFilter(doc));
      this.textEditor.setDocument(doc);
      doc.addDocumentListener(new DocumentListener() {
         public void insertUpdate(DocumentEvent documentEvent) {
            ConsoleTextEditor.this.documentChangedSinceLastRepaint = true;
         }

         public void removeUpdate(DocumentEvent documentEvent) {
            ConsoleTextEditor.this.documentChangedSinceLastRepaint = true;
         }

         public void changedUpdate(DocumentEvent documentEvent) {
            ConsoleTextEditor.this.documentChangedSinceLastRepaint = true;
            int width = 3 * Preferences.userNodeForPackage(Console.class).getInt("fontSize", 12);
            ConsoleTextEditor.this.numbersPanel.setPreferredSize(new Dimension(width, width));
         }
      });
      this.undoManager = new TextUndoManager();
      doc.addUndoableEditListener(this.undoManager);
      this.undoManager.addPropertyChangeListener(this.undoAction);
      this.undoManager.addPropertyChangeListener(this.redoAction);
      doc.addDocumentListener(this.undoAction);
      doc.addDocumentListener(this.redoAction);
      InputMap im = this.textEditor.getInputMap(2);
      KeyStroke ks = KeyStroke.getKeyStroke(90, 2, false);
      im.put(ks, "Undo");
      ActionMap am = this.textEditor.getActionMap();
      am.put("Undo", this.undoAction);
      ks = KeyStroke.getKeyStroke(89, 2, false);
      im.put(ks, "Redo");
      am.put("Redo", this.redoAction);
      ks = KeyStroke.getKeyStroke(80, 2, false);
      im.put(ks, "Print");
      am.put("Print", this.printAction);
   }

   public boolean clipBoardAvailable() {
      Transferable t = StructuredSyntaxResources.SYSTEM_CLIPBOARD.getContents(this);
      return t.isDataFlavorSupported(DataFlavor.stringFlavor);
   }

   public TextEditor getTextEditor() {
      return this.textEditor;
   }

   protected void initActions() {
      ActionMap map = this.getActionMap();
      ConsoleTextEditor.PrintAction printAction = new ConsoleTextEditor.PrintAction();
      map.put("Print", printAction);
   }

   public Action getUndoAction() {
      return this.undoAction;
   }

   public Action getRedoAction() {
      return this.redoAction;
   }

   public Action getPrintAction() {
      return this.printAction;
   }

   private class UndoAction extends ConsoleTextEditor.UpdateCaretListener implements PropertyChangeListener {
      public UndoAction() {
         super(null);
         this.setEnabled(false);
      }

      public void actionPerformed(ActionEvent ae) {
         ConsoleTextEditor.this.undoManager.undo();
         this.setEnabled(ConsoleTextEditor.this.undoManager.canUndo());
         ConsoleTextEditor.this.redoAction.setEnabled(ConsoleTextEditor.this.undoManager.canRedo());
         super.actionPerformed(ae);
      }

      public void propertyChange(PropertyChangeEvent pce) {
         this.setEnabled(ConsoleTextEditor.this.undoManager.canUndo());
      }
   }

   private abstract class UpdateCaretListener extends AbstractAction implements DocumentListener {
      protected int lastUpdate;

      private UpdateCaretListener() {
      }

      public void changedUpdate(DocumentEvent de) {
      }

      public void insertUpdate(DocumentEvent de) {
         this.lastUpdate = de.getOffset() + de.getLength();
      }

      public void removeUpdate(DocumentEvent de) {
         this.lastUpdate = de.getOffset();
      }

      public void actionPerformed(ActionEvent ae) {
         ConsoleTextEditor.this.textEditor.setCaretPosition(this.lastUpdate);
      }

      // $FF: synthetic method
      UpdateCaretListener(Object x1) {
         this();
      }
   }

   private class RedoAction extends ConsoleTextEditor.UpdateCaretListener implements PropertyChangeListener {
      public RedoAction() {
         super(null);
         this.setEnabled(false);
      }

      public void actionPerformed(ActionEvent ae) {
         ConsoleTextEditor.this.undoManager.redo();
         this.setEnabled(ConsoleTextEditor.this.undoManager.canRedo());
         ConsoleTextEditor.this.undoAction.setEnabled(ConsoleTextEditor.this.undoManager.canUndo());
         super.actionPerformed(ae);
      }

      public void propertyChange(PropertyChangeEvent pce) {
         this.setEnabled(ConsoleTextEditor.this.undoManager.canRedo());
      }
   }

   private class PrintAction extends AbstractAction {
      public PrintAction() {
         this.setEnabled(true);
      }

      public void actionPerformed(ActionEvent ae) {
         ConsoleTextEditor.PRINTER_JOB.setPageable(ConsoleTextEditor.this.textEditor);

         try {
            if (ConsoleTextEditor.PRINTER_JOB.printDialog()) {
               ConsoleTextEditor.PRINTER_JOB.print();
            }
         } catch (Exception var3) {
            var3.printStackTrace();
         }

      }
   }

   private class LineNumbersPanel extends JPanel {
      public LineNumbersPanel() {
         int initialSize = 3 * Preferences.userNodeForPackage(Console.class).getInt("fontSize", 12);
         this.setMinimumSize(new Dimension(initialSize, initialSize));
         this.setPreferredSize(new Dimension(initialSize, initialSize));
      }

      public void paintComponent(Graphics g) {
         super.paintComponent(g);
         int start = ConsoleTextEditor.this.textEditor.viewToModel(ConsoleTextEditor.this.getViewport().getViewPosition());
         int end = ConsoleTextEditor.this.textEditor.viewToModel(new Point(10, ConsoleTextEditor.this.getViewport().getViewPosition().y + (int)ConsoleTextEditor.this.textEditor.getVisibleRect().getHeight()));
         Document doc = ConsoleTextEditor.this.textEditor.getDocument();
         int startline = doc.getDefaultRootElement().getElementIndex(start) + 1;
         int endline = doc.getDefaultRootElement().getElementIndex(end) + 1;
         Font f = ConsoleTextEditor.this.textEditor.getFont();
         int fontHeight = g.getFontMetrics(f).getHeight();
         int fontDesc = g.getFontMetrics(f).getDescent();
         int startingY = -1;

         try {
            startingY = ConsoleTextEditor.this.textEditor.modelToView(start).y + fontHeight - fontDesc;
         } catch (BadLocationException var14) {
            System.err.println(var14.getMessage());
         }

         g.setFont(f);
         int line = startline;

         for(int y = startingY; line <= endline; ++line) {
            String lineNumber = DefaultGroovyMethods.padLeft(Integer.toString(line), 4, " ");
            g.drawString(lineNumber, 0, y);
            y += fontHeight;
         }

      }
   }
}
