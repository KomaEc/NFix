package groovy.swing.binding;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JComponent;
import org.codehaus.groovy.binding.PropertyBinding;
import org.codehaus.groovy.binding.TargetBinding;

abstract class AbstractJComponentBinding extends AbstractSyntheticBinding implements PropertyChangeListener, ComponentListener {
   JComponent boundComponent;
   String propertyName;

   public AbstractJComponentBinding(PropertyBinding source, TargetBinding target, String propertyName) {
      super(source, target, JComponent.class, propertyName);
      source.setNonChangeCheck(true);
   }

   public synchronized void syntheticBind() {
      this.boundComponent = (JComponent)((PropertyBinding)this.sourceBinding).getBean();
      this.boundComponent.addPropertyChangeListener(this.propertyName, this);
      this.boundComponent.addComponentListener(this);
   }

   public synchronized void syntheticUnbind() {
      this.boundComponent.removePropertyChangeListener(this.propertyName, this);
      this.boundComponent.removeComponentListener(this);
      this.boundComponent = null;
   }

   public void propertyChange(PropertyChangeEvent event) {
      this.update();
      ((JComponent)event.getOldValue()).removeComponentListener(this);
      ((JComponent)event.getNewValue()).addComponentListener(this);
   }

   public void componentHidden(ComponentEvent event) {
   }

   public void componentShown(ComponentEvent event) {
   }

   public void componentMoved(ComponentEvent event) {
   }

   public void componentResized(ComponentEvent event) {
   }
}
