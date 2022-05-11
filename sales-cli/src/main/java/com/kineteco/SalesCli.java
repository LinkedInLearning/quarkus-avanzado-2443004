package com.kineteco;

import io.quarkus.runtime.QuarkusApplication;
import picocli.CommandLine;

import javax.inject.Inject;

@CommandLine.Command(name = "kineteco", mixinStandardHelpOptions = true)
public class SalesCli implements Runnable, QuarkusApplication {

   @Inject
   CommandLine.IFactory factory;

   @Override
   public int run(String... args) throws Exception {
      return new CommandLine(this, factory).execute(args);
   }

   @Override
   public void run() {

   }
}
