package com.mks.api.commands;

import com.mks.api.CmdRunner;
import com.mks.api.CmdRunnerCreator;
import com.mks.api.Command;
import com.mks.api.IntegrationPointFactory;
import com.mks.api.Option;
import com.mks.api.OptionList;
import com.mks.api.SelectionList;
import com.mks.api.Session;
import com.mks.api.response.APIException;
import com.mks.api.response.Response;
import com.mks.api.response.WorkItem;
import com.mks.api.util.MKSLogger;

abstract class CommandBase implements ICommandBase {
   private static final String LOG_CATEGORY = "COMMANDS";
   private static final String NO = "no";
   protected boolean interactive = false;
   private OptionList options = null;
   private CmdRunnerCreator cmdRunnerCreator;

   protected CommandBase(CmdRunnerCreator session) throws APIException {
      validateConnection(session);
      this.cmdRunnerCreator = session;
      this.options = new OptionList();
   }

   static void validateConnection(CmdRunnerCreator cmdRunnerCreator) throws APIException {
      if (cmdRunnerCreator instanceof Session) {
         Command cmd = new Command("api", "ping");
         runAPICommand(cmdRunnerCreator, cmd, false);
      }

   }

   protected final CmdRunnerCreator getCmdRunnerCreator() {
      return this.cmdRunnerCreator;
   }

   protected final Response runAPICommand(Command command) throws APIException {
      return runAPICommand(this.getCmdRunnerCreator(), command, false);
   }

   static Response runAPICommand(CmdRunnerCreator cmdRunnerCreator, Command command, boolean useStreamedResponse) throws APIException {
      CmdRunner runner = null;
      Response response = null;

      try {
         runner = cmdRunnerCreator.createCmdRunner();
         String[] cmdArray = command.toStringArray();
         StringBuffer sb = new StringBuffer("Executing: ");

         for(int i = 0; i < cmdArray.length; ++i) {
            String arg = cmdArray[i];
            if (arg.toLowerCase().startsWith("--password")) {
               arg = "--password=XXXX";
            }

            if (i != 0) {
               sb.append(" ");
            }

            sb.append(arg);
         }

         IntegrationPointFactory.getLogger().message("COMMANDS", 10, sb.toString());
         response = useStreamedResponse ? runner.executeWithInterim(command, false) : runner.execute(command);
      } catch (APIException var14) {
         MKSLogger logger = IntegrationPointFactory.getLogger();
         response = var14.getResponse();
         if (response == null || response.getWorkItemListSize() < 1) {
            logger.exception("COMMANDS", var14);
            throw var14;
         }

         if (response.getWorkItemListSize() == 1) {
            APIException wix = null;

            try {
               WorkItem wi = response.getWorkItems().next();
               wix = wi.getAPIException();
            } catch (APIException var13) {
               wix = var13;
            }

            if (wix != null) {
               logger.exception("COMMANDS", wix);
               throw wix;
            }
         }
      } finally {
         if (!useStreamedResponse) {
            runner.release();
         }

      }

      return response;
   }

   protected abstract Response execute(SelectionList var1) throws APIException;

   public final Response execute(String[] selection, boolean isInteractive) throws APIException {
      this.interactive = isInteractive;
      SelectionList selectionList = new SelectionList();
      if (selection != null) {
         for(int i = 0; i < selection.length; ++i) {
            selectionList.add(selection[i]);
         }
      }

      return this.execute(selectionList);
   }

   public final Response execute(String selection, boolean isInteractive) throws APIException {
      return this.execute(new String[]{selection}, isInteractive);
   }

   public final Response execute(boolean isInteractive) throws APIException {
      return this.execute((String[])null, isInteractive);
   }

   protected Option createBinaryOption(String optionName, boolean value) {
      String opt = optionName;
      if (!value) {
         opt = "no" + optionName;
      }

      return new Option(opt);
   }

   public final void addOptionList(OptionList options) {
      if (options != null) {
         this.options.add(options);
      }

   }

   public final OptionList getBaseOptions() {
      OptionList optionClone = new OptionList();
      optionClone.add(this.options);
      return optionClone;
   }
}
