package com.mks.api;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Command {
   public static final String AA = "aa";
   public static final String IM = "im";
   public static final String INTEGRITY = "integrity";
   public static final String SI = "si";
   public static final String SD = "sd";
   public static final String TM = "tm";
   public static final String RQ = "rq";
   protected OptionList optionList;
   protected SelectionList selectionList;
   protected String command;
   protected String app;
   protected boolean generateSubRtns;

   public Command() {
      this("", "");
   }

   public Command(String app) {
      this(app, "");
   }

   public Command(String app, String commandName) {
      this.app = app;
      this.command = commandName;
      this.optionList = new OptionList();
      this.selectionList = new SelectionList();
   }

   public void addOption(Option option) {
      this.optionList.add(option);
   }

   public void clearOptionList() {
      this.optionList.clear();
   }

   public OptionList getOptionList() {
      return this.optionList;
   }

   public void setOptionList(OptionList optionList) {
      this.optionList = optionList;
   }

   public void clearSelectionList() {
      this.selectionList.clear();
   }

   public SelectionList getSelectionList() {
      return this.selectionList;
   }

   public void setSelectionList(SelectionList selectionList) {
      this.selectionList = selectionList;
   }

   public void addSelection(String selection) {
      this.selectionList.add(selection);
   }

   public String getApp() {
      return this.app;
   }

   public void setApp(String app) {
      this.app = app;
   }

   public String getCommandName() {
      return this.command;
   }

   public void setCommandName(String commandName) {
      this.command = commandName;
   }

   public boolean getGenerateSubRoutines() {
      return this.generateSubRtns;
   }

   public void setGenerateSubRoutines(boolean generateSubRtns) {
      this.generateSubRtns = generateSubRtns;
   }

   public String[] toStringArray() {
      List list = new ArrayList();
      list.add(this.getApp());
      list.add(this.getCommandName());
      Iterator it = this.getOptionList().getOptions();

      while(true) {
         while(it.hasNext()) {
            String s = it.next().toString();
            if (s.length() > 2 && s.charAt(2) == ' ') {
               list.add(s.substring(0, 2));
               list.add(s.substring(3));
            } else {
               list.add(s);
            }
         }

         list.add("--");
         it = this.getSelectionList().getSelections();

         while(it.hasNext()) {
            list.add(it.next().toString());
         }

         return (String[])list.toArray(new String[0]);
      }
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() != o.getClass()) {
         Command other = (Command)o;
         return (this.app == other.app || this.app != null && this.app.equals(other.app)) && (this.command == other.command || this.command != null && this.command.equals(other.command)) && (this.optionList == other.optionList || this.optionList != null && this.optionList.equals(other.optionList)) && (this.selectionList == other.selectionList || this.selectionList != null && this.selectionList.equals(other.selectionList));
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.app.hashCode() ^ this.command.hashCode() ^ this.optionList.hashCode() ^ this.selectionList.hashCode();
   }
}
