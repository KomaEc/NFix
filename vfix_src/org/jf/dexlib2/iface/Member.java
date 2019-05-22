package org.jf.dexlib2.iface;

import javax.annotation.Nonnull;

public interface Member extends Annotatable {
   @Nonnull
   String getDefiningClass();

   @Nonnull
   String getName();

   int getAccessFlags();
}
