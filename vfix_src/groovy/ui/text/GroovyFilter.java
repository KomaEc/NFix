package groovy.ui.text;

import java.awt.Color;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import javax.swing.text.Segment;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

public class GroovyFilter extends StructuredSyntaxDocumentFilter {
   private static final Action AUTO_TAB_ACTION = new GroovyFilter.AutoTabAction();
   public static final String COMMENT = "comment";
   public static final String SLASH_STAR_COMMENT = "/\\*(?s:.)*?(?:\\*/|\\z)";
   public static final String SLASH_SLASH_COMMENT = "//.*";
   public static final String QUOTES = "(?ms:\"{3}(?!\\\"{1,3}).*?(?:\"{3}|\\z))|(?:\"{1}(?!\\\").*?(?:\"|\\Z))";
   public static final String SINGLE_QUOTES = "(?ms:'{3}(?!'{1,3}).*?(?:'{3}|\\z))|(?:'[^'].*?(?:'|\\z))";
   public static final String SLASHY_QUOTES = "/[^/*].*?/";
   public static final String DIGIT = "\\d+?[efld]?";
   public static final String IDENT = "[\\w\\$&&[\\D]][\\w\\$]*";
   public static final String OPERATION = "[\\w\\$&&[\\D]][\\w\\$]* *\\(";
   public static final String LEFT_PARENS = "\\(";
   private static final Color COMMENT_COLOR;
   public static final String RESERVED_WORD = "reserved";
   public static final String[] RESERVED_WORDS;

   public GroovyFilter(DefaultStyledDocument doc) {
      super(doc);
      this.init();
   }

   private void init() {
      StyleContext styleContext = StyleContext.getDefaultStyleContext();
      Style defaultStyle = styleContext.getStyle("default");
      Style comment = styleContext.addStyle("comment", defaultStyle);
      StyleConstants.setForeground(comment, COMMENT_COLOR);
      StyleConstants.setItalic(comment, true);
      Style quotes = styleContext.addStyle("(?ms:\"{3}(?!\\\"{1,3}).*?(?:\"{3}|\\z))|(?:\"{1}(?!\\\").*?(?:\"|\\Z))", defaultStyle);
      StyleConstants.setForeground(quotes, Color.MAGENTA.darker().darker());
      Style charQuotes = styleContext.addStyle("(?ms:'{3}(?!'{1,3}).*?(?:'{3}|\\z))|(?:'[^'].*?(?:'|\\z))", defaultStyle);
      StyleConstants.setForeground(charQuotes, Color.GREEN.darker().darker());
      Style slashyQuotes = styleContext.addStyle("/[^/*].*?/", defaultStyle);
      StyleConstants.setForeground(slashyQuotes, Color.ORANGE.darker());
      Style digit = styleContext.addStyle("\\d+?[efld]?", defaultStyle);
      StyleConstants.setForeground(digit, Color.RED.darker());
      Style operation = styleContext.addStyle("[\\w\\$&&[\\D]][\\w\\$]* *\\(", defaultStyle);
      StyleConstants.setBold(operation, true);
      Style ident = styleContext.addStyle("[\\w\\$&&[\\D]][\\w\\$]*", defaultStyle);
      Style reservedWords = styleContext.addStyle("reserved", defaultStyle);
      StyleConstants.setBold(reservedWords, true);
      StyleConstants.setForeground(reservedWords, Color.BLUE.darker().darker());
      Style leftParens = styleContext.addStyle("[\\w\\$&&[\\D]][\\w\\$]*", defaultStyle);
      this.getRootNode().putStyle("/\\*(?s:.)*?(?:\\*/|\\z)", comment);
      this.getRootNode().putStyle("//.*", comment);
      this.getRootNode().putStyle("(?ms:\"{3}(?!\\\"{1,3}).*?(?:\"{3}|\\z))|(?:\"{1}(?!\\\").*?(?:\"|\\Z))", quotes);
      this.getRootNode().putStyle("(?ms:'{3}(?!'{1,3}).*?(?:'{3}|\\z))|(?:'[^'].*?(?:'|\\z))", charQuotes);
      this.getRootNode().putStyle("/[^/*].*?/", slashyQuotes);
      this.getRootNode().putStyle("\\d+?[efld]?", digit);
      this.getRootNode().putStyle("[\\w\\$&&[\\D]][\\w\\$]* *\\(", operation);
      StructuredSyntaxDocumentFilter.LexerNode node = this.createLexerNode();
      node.putStyle(RESERVED_WORDS, reservedWords);
      node.putStyle("\\(", leftParens);
      this.getRootNode().putChild("[\\w\\$&&[\\D]][\\w\\$]* *\\(", node);
      this.getRootNode().putStyle("[\\w\\$&&[\\D]][\\w\\$]*", ident);
      node = this.createLexerNode();
      node.putStyle(RESERVED_WORDS, reservedWords);
      this.getRootNode().putChild("[\\w\\$&&[\\D]][\\w\\$]*", node);
   }

   public static void installAutoTabAction(JTextComponent tComp) {
      tComp.getActionMap().put("GroovyFilter-autoTab", AUTO_TAB_ACTION);
      KeyStroke keyStroke = KeyStroke.getKeyStroke(10, 0, false);
      tComp.getInputMap().put(keyStroke, "GroovyFilter-autoTab");
   }

   static {
      COMMENT_COLOR = Color.LIGHT_GRAY.darker().darker();
      RESERVED_WORDS = new String[]{"\\babstract\\b", "\\bassert\\b", "\\bdefault\\b", "\\bif\\b", "\\bprivate\\b", "\\bthis\\b", "\\bboolean\\b", "\\bdo\\b", "\\bimplements\\b", "\\bprotected\\b", "\\bthrow\\b", "\\bbreak\\b", "\\bdouble\\b", "\\bimport\\b", "\\bpublic\\b", "\\bthrows\\b", "\\bbyte\\b", "\\belse\\b", "\\binstanceof\\b", "\\breturn\\b", "\\btransient\\b", "\\bcase\\b", "\\bextends\\b", "\\bint\\b", "\\bshort\\b", "\\btry\\b", "\\bcatch\\b", "\\bfinal\\b", "\\binterface\\b", "\\benum\\b", "\\bstatic\\b", "\\bvoid\\b", "\\bchar\\b", "\\bfinally\\b", "\\blong\\b", "\\bstrictfp\\b", "\\bvolatile\\b", "\\bclass\\b", "\\bfloat\\b", "\\bnative\\b", "\\bsuper\\b", "\\bwhile\\b", "\\bconst\\b", "\\bfor\\b", "\\bnew\\b", "\\bswitch\\b", "\\bcontinue\\b", "\\bgoto\\b", "\\bpackage\\b", "\\bdef\\b", "\\bas\\b", "\\bin\\b", "\\bsynchronized\\b", "\\bnull\\b"};
   }

   private static class AutoTabAction extends AbstractAction {
      private StyledDocument doc;
      private final Segment segment;
      private final StringBuffer buffer;

      private AutoTabAction() {
         this.segment = new Segment();
         this.buffer = new StringBuffer();
      }

      public void actionPerformed(ActionEvent ae) {
         JTextComponent tComp = (JTextComponent)ae.getSource();
         if (tComp.getDocument() instanceof StyledDocument) {
            this.doc = (StyledDocument)tComp.getDocument();

            try {
               this.doc.getText(0, this.doc.getLength(), this.segment);
            } catch (Exception var7) {
               var7.printStackTrace();
            }

            int offset = tComp.getCaretPosition();
            int index = this.findTabLocation(offset);
            this.buffer.delete(0, this.buffer.length());
            this.buffer.append('\n');
            if (index > -1) {
               for(int i = 0; i < index + 4; ++i) {
                  this.buffer.append(' ');
               }
            }

            try {
               this.doc.insertString(offset, this.buffer.toString(), this.doc.getDefaultRootElement().getAttributes());
            } catch (BadLocationException var6) {
               var6.printStackTrace();
            }
         }

      }

      public int findTabLocation(int offset) {
         for(boolean cont = true; offset > -1 && cont; offset -= cont ? 1 : 0) {
            Element el = this.doc.getCharacterElement(offset);
            Object color = el.getAttributes().getAttribute(StyleConstants.Foreground);
            if (!GroovyFilter.COMMENT_COLOR.equals(color)) {
               cont = this.segment.array[offset] != '{' && this.segment.array[offset] != '}';
            }
         }

         if (offset > -1 && this.segment.array[offset] == '{') {
            while(offset > -1 && !Character.isWhitespace(this.segment.array[offset--])) {
            }
         }

         int index = offset >= 0 && this.segment.array[offset] != '}' ? 0 : -4;
         if (offset > -1) {
            Element top = this.doc.getDefaultRootElement();

            for(offset = top.getElement(top.getElementIndex(offset)).getStartOffset(); Character.isWhitespace(this.segment.array[offset++]); ++index) {
            }
         }

         return index;
      }

      // $FF: synthetic method
      AutoTabAction(Object x0) {
         this();
      }
   }
}
