package soot.tagkit;

import java.util.Hashtable;
import java.util.Map;
import soot.Unit;

public abstract class JasminAttribute implements Attribute {
   public abstract byte[] decode(String var1, Hashtable<String, Integer> var2);

   public abstract String getJasminValue(Map<Unit, String> var1);
}
