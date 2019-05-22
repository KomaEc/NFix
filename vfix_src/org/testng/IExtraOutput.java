package org.testng;

import java.io.Serializable;
import java.util.List;

public interface IExtraOutput extends Serializable {
   List<String> getParameterOutput();
}
