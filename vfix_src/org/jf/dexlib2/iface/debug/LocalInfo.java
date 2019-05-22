package org.jf.dexlib2.iface.debug;

import javax.annotation.Nullable;

public interface LocalInfo {
   @Nullable
   String getName();

   @Nullable
   String getType();

   @Nullable
   String getSignature();
}
