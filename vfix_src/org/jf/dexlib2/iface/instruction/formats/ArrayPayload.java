package org.jf.dexlib2.iface.instruction.formats;

import java.util.List;
import javax.annotation.Nonnull;
import org.jf.dexlib2.iface.instruction.PayloadInstruction;

public interface ArrayPayload extends PayloadInstruction {
   int getElementWidth();

   @Nonnull
   List<Number> getArrayElements();
}
