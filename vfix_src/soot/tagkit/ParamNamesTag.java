package soot.tagkit;

import java.util.Arrays;
import java.util.List;

public class ParamNamesTag implements Tag {
   String[] names;

   public ParamNamesTag(List<String> parameterNames) {
      this((String[])parameterNames.toArray(new String[parameterNames.size()]));
   }

   public ParamNamesTag(String[] parameterNames) {
      this.names = parameterNames;
   }

   public String toString() {
      return this.names.toString();
   }

   public List<String> getNames() {
      return Arrays.asList(this.names);
   }

   public String[] getNameArray() {
      return this.names;
   }

   public String getName() {
      return "ParamNamesTag";
   }

   public List<String> getInfo() {
      return this.getNames();
   }

   public byte[] getValue() {
      throw new RuntimeException("ParamNamesTag has no value for bytecode");
   }
}
