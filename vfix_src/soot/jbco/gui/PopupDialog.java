package soot.jbco.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class PopupDialog extends JDialog implements ActionListener {
   private JPanel myPanel = null;
   private JButton okButton = null;

   public PopupDialog(JFrame frame, boolean modal, String myMessage) {
      super(frame, modal);
      this.myPanel = new JPanel();
      this.getContentPane().add(this.myPanel);
      JTextArea jta = new JTextArea(myMessage);
      this.myPanel.add(jta);
      this.okButton = new JButton("Ok");
      this.okButton.addActionListener(this);
      this.myPanel.add(this.okButton);
      this.pack();
      this.setLocationRelativeTo(frame);
      this.setVisible(true);
   }

   public void actionPerformed(ActionEvent e) {
      if (this.okButton == e.getSource()) {
         this.setVisible(false);
      }

   }
}
