package org.codehaus.plexus.interpolation;

import java.util.List;

public class PrefixedObjectValueSource extends AbstractDelegatingValueSource implements QueryEnabledValueSource {
   public PrefixedObjectValueSource(String prefix, Object root) {
      super(new PrefixedValueSourceWrapper(new ObjectBasedValueSource(root), prefix));
   }

   public PrefixedObjectValueSource(List possiblePrefixes, Object root, boolean allowUnprefixedExpressions) {
      super(new PrefixedValueSourceWrapper(new ObjectBasedValueSource(root), possiblePrefixes, allowUnprefixedExpressions));
   }

   public String getLastExpression() {
      return ((QueryEnabledValueSource)this.getDelegate()).getLastExpression();
   }
}
