package com.kineteco;

import io.quarkus.picocli.runtime.annotations.TopCommand;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import picocli.CommandLine;

import javax.inject.Inject;

@TopCommand
@CommandLine.Command(name = "kineteco", mixinStandardHelpOptions = true, subcommands = {
      ViewCustomerCommand.class,
      ModifyCustomerEmailCommand.class
})
public class SalesCli implements Runnable, QuarkusApplication {

   @Inject
   CommandLine.IFactory factory;

   @Override
   public int run(String... args) throws Exception {
      return new CommandLine(this, factory).execute(args);
   }

   @Override
   public void run() {
      //
   }
}
