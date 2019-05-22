package org.codehaus.plexus.personality.plexus.lifecycle.phase;

import org.codehaus.plexus.context.Context;
import org.codehaus.plexus.context.ContextException;

public interface Contextualizable {
   void contextualize(Context var1) throws ContextException;
}
