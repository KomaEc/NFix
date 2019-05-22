package groovy.util;

import groovy.lang.Closure;
import java.util.Map;

public abstract class AbstractFactory implements Factory {
   public boolean isLeaf() {
      return false;
   }

   public boolean isHandlesNodeChildren() {
      return false;
   }

   public void onFactoryRegistration(FactoryBuilderSupport builder, String registerdName, String group) {
   }

   public boolean onHandleNodeAttributes(FactoryBuilderSupport builder, Object node, Map attributes) {
      return true;
   }

   public boolean onNodeChildren(FactoryBuilderSupport builder, Object node, Closure childContent) {
      return true;
   }

   public void onNodeCompleted(FactoryBuilderSupport builder, Object parent, Object node) {
   }

   public void setParent(FactoryBuilderSupport builder, Object parent, Object child) {
   }

   public void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
   }
}
