package org.codehaus.groovy.ast;

import java.util.ArrayList;
import java.util.List;

public class InterfaceHelperClassNode extends InnerClassNode {
   private List callSites = new ArrayList();

   public InterfaceHelperClassNode(ClassNode outerClass, String name, int modifiers, ClassNode superClass, List callSites) {
      super(outerClass, name, modifiers, superClass, ClassHelper.EMPTY_TYPE_ARRAY, MixinNode.EMPTY_ARRAY);
      this.setCallSites(callSites);
   }

   public void setCallSites(List cs) {
      this.callSites = (List)(cs != null ? cs : new ArrayList());
   }

   public List getCallSites() {
      return this.callSites;
   }
}
