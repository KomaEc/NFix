package org.jf.dexlib2.iface.instruction;

import java.util.List;
import javax.annotation.Nonnull;

public interface SwitchPayload extends PayloadInstruction {
   @Nonnull
   List<? extends SwitchElement> getSwitchElements();
}
