package org.jf.util.jcommander;

import com.beust.jcommander.converters.IParameterSplitter;
import java.util.Arrays;
import java.util.List;

public class ColonParameterSplitter implements IParameterSplitter {
   public List<String> split(String value) {
      return Arrays.asList(value.split(":"));
   }
}
