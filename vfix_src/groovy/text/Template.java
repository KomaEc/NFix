package groovy.text;

import groovy.lang.Writable;
import java.util.Map;

public interface Template {
   Writable make();

   Writable make(Map var1);
}
