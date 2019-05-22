package com.github.javaparser.printer.concretesyntaxmodel;

import com.github.javaparser.printer.Printable;

class PrintingHelper {
   static String printToString(Object value) {
      if (value instanceof Printable) {
         return ((Printable)value).asString();
      } else if (value instanceof Enum) {
         return ((Enum)value).name().toLowerCase();
      } else {
         return value != null ? value.toString() : "";
      }
   }
}
