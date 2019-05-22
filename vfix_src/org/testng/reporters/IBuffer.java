package org.testng.reporters;

import java.io.Writer;

public interface IBuffer {
   IBuffer append(CharSequence var1);

   void toWriter(Writer var1);
}
