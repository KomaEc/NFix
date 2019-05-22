package com.mks.api.commands;

import com.mks.api.CmdRunnerCreator;
import com.mks.api.Command;
import com.mks.api.Option;
import com.mks.api.response.APIException;
import com.mks.api.response.Field;
import com.mks.api.response.InvalidCommandOptionException;
import com.mks.api.response.InvalidCommandSelectionException;
import com.mks.api.response.Item;
import com.mks.api.response.Response;
import com.mks.api.response.Result;
import com.mks.api.response.WorkItem;
import com.mks.api.response.WorkItemIterator;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class IMCommands extends MKSCommands {
   public IMCommands(CmdRunnerCreator session) throws APIException {
      super(session);
   }

   public void imConnect() throws APIException {
      Command cmd = new Command("im", "connect");
      cmd.addOption(new Option("g"));
      this.runAPICommand(cmd);
   }

   public void imEditIssue(int issueId) throws APIException {
      Command cmd = new Command("im", "editissue");
      cmd.addOption(new Option("g"));
      String issue = String.valueOf(issueId);
      if (issue != null && issue.length() > 0) {
         cmd.addSelection(issue);
         this.runAPICommand(cmd);
      } else {
         throw new InvalidCommandSelectionException("IMCommands.imEditIssue: parameter 'issueId' is invalid.");
      }
   }

   public void imViewIssue(int issueId) throws APIException {
      Command cmd = new Command("im", "viewissue");
      cmd.addOption(new Option("g"));
      String issue = String.valueOf(issueId);
      if (issue != null && issue.length() > 0) {
         cmd.addSelection(issue);
         this.runAPICommand(cmd);
      } else {
         throw new InvalidCommandSelectionException("IMCommands.imViewIssue: parameter 'issueId' is invalid.");
      }
   }

   public String imCreateIssue() throws APIException {
      String newIssueid = "";
      Command cmd = new Command("im", "createissue");
      cmd.addOption(new Option("g"));
      Response response = this.runAPICommand(cmd);
      Result result = null;
      Item item = null;
      if (response != null) {
         result = response.getResult();
      }

      if (result != null) {
         item = result.getPrimaryValue();
      }

      if (item != null) {
         newIssueid = item.getId();
      }

      return newIssueid;
   }

   public void imCopyIssue(int issueId) throws APIException {
      Command cmd = new Command("im", "copyissue");
      cmd.addOption(new Option("g"));
      cmd.addOption(new Option("link"));
      cmd.addOption(new Option("copyfields"));
      String issue = String.valueOf(issueId);
      if (issue != null && issue.length() > 0) {
         cmd.addSelection(issue);
         this.runAPICommand(cmd);
      } else {
         throw new InvalidCommandSelectionException("IMCommands.imCopyIssue: parameter 'issueId' is invalid.");
      }
   }

   public void imViewCP(String cpId) throws APIException {
      if (cpId != null && cpId.length() != 0) {
         Command cmd = new Command("im", "viewcp");
         cmd.addOption(new Option("g"));
         cmd.addSelection(cpId);
         this.runAPICommand(cmd);
      } else {
         throw new InvalidCommandSelectionException("IMCommands.imViewCP: parameter 'cpId' cannot be null or empty.");
      }
   }

   public String[] imGetQueries() throws APIException {
      String[] result = new String[0];
      Command cmd = new Command("im", "queries");
      cmd.addOption(new Option("showVisibleOnly"));
      cmd.addOption(new Option("fields", "name"));
      cmd.addOption(new Option("settingsUI", "gui"));
      Response response = this.runAPICommand(cmd);
      result = new String[response.getWorkItemListSize()];
      int index = 0;

      for(WorkItemIterator it = response.getWorkItems(); it.hasNext(); ++index) {
         result[index] = it.next().getField("name").getValueAsString();
      }

      return result;
   }

   public String[] imGetQueryFields(String query) throws APIException {
      String[] result = new String[0];
      Command cmd = new Command("im", "queries");
      cmd.addOption(new Option("showVisibleOnly"));
      cmd.addOption(new Option("fields", "fields"));
      cmd.addOption(new Option("settingsUI", "gui"));
      if (query != null & query.length() > 0) {
         cmd.addSelection(query);
      }

      Response response = null;

      try {
         response = this.runAPICommand(cmd);
         WorkItem wi = response.getWorkItem(query);
         Field columns = wi.getField("fields");
         List list = columns.getList();
         result = new String[list.size()];
         Iterator it = list.iterator();

         for(int index = 0; it.hasNext(); ++index) {
            result[index] = ((Item)it.next()).getId();
         }
      } catch (NoSuchElementException var10) {
      }

      return result;
   }

   public String[] imGetColumnSets() throws APIException {
      String[] result = new String[0];
      Command cmd = new Command("im", "columnsets");
      cmd.addOption(new Option("fields", "name"));
      cmd.addOption(new Option("settingsUI", "gui"));
      Response response = this.runAPICommand(cmd);
      WorkItemIterator it = response.getWorkItems();
      result = new String[response.getWorkItemListSize()];

      for(int i = 0; it.hasNext(); ++i) {
         result[i] = it.next().getField("name").getValueAsString();
      }

      return result;
   }

   public String[] imGetColumnsetFields(String columnset) throws APIException {
      String[] result = new String[0];
      Command cmd = new Command("im", "viewcolumnset");
      cmd.addOption(new Option("settingsUI", "gui"));
      if (columnset != null & columnset.length() > 0) {
         cmd.addSelection(columnset);
      }

      Response response = null;

      try {
         response = this.runAPICommand(cmd);
         WorkItem wi = response.getWorkItem(columnset);
         Field columns = wi.getField("fields");
         List list = columns.getList();
         result = new String[list.size()];
         Iterator it = list.iterator();

         for(int index = 0; it.hasNext(); ++index) {
            result[index] = ((Item)it.next()).getId();
         }
      } catch (NoSuchElementException var10) {
         if (!columnset.equalsIgnoreCase("default")) {
            result = this.imGetColumnsetFields("default");
         }
      }

      return result;
   }

   public void launchMKSGUI() throws APIException {
      Command cmd = new Command("im", "gui");
      this.runAPICommand(cmd);
   }

   public WorkItem[] imGetIssuesViewContents(String query, String[] fields) throws APIException {
      if (query != null && query.length() != 0) {
         if (fields != null && fields.length != 0) {
            WorkItem[] issues = new WorkItem[0];
            Command cmd = new Command("im", "issues");
            cmd.addOption(new Option("settingsUI", "gui"));
            cmd.addOption(new Option("query", query));
            StringBuffer fieldsValue = new StringBuffer();

            for(int i = 0; i < fields.length; ++i) {
               String field = fields[i];
               if (fieldsValue.length() > 0) {
                  fieldsValue.append(",");
               }

               fieldsValue.append(field);
            }

            cmd.addOption(new Option("fields", fieldsValue.toString()));
            Response response = this.runAPICommand(cmd);
            issues = new WorkItem[response.getWorkItemListSize()];
            int index = 0;

            for(WorkItemIterator items = response.getWorkItems(); items.hasNext(); ++index) {
               issues[index] = items.next();
            }

            return issues;
         } else {
            throw new InvalidCommandOptionException("IMCommands.imGetIssuesViewContents: parameter 'fields' cannot be null or empty.");
         }
      } else {
         throw new InvalidCommandOptionException("IMCommands.imGetIssuesViewContents: parameter 'query' cannot be null or empty.");
      }
   }

   public WorkItem[] imGetImplementerCPViewContents(int issueId) throws APIException {
      WorkItem[] cps = new WorkItem[0];
      Command cmd = new Command("im", "viewcp");
      cmd.addOption(new Option("filter", "type:implementer"));
      String issue = String.valueOf(issueId);
      if (issue != null && issue.length() > 0) {
         cmd.addSelection(issue);
         Response response = this.runAPICommand(cmd);
         cps = new WorkItem[response.getWorkItemListSize()];
         int index = 0;

         for(WorkItemIterator items = response.getWorkItems(); items.hasNext(); ++index) {
            cps[index] = items.next();
         }

         return cps;
      } else {
         throw new InvalidCommandSelectionException("IMCommands.imGetImplementerCPViewContents: parameter 'issueId' is invalid.");
      }
   }
}
