package com.kineteco;

import io.quarkus.picocli.runtime.annotations.TopCommand;
import picocli.CommandLine;

@TopCommand
@CommandLine.Command(
      name = "kineteco",
      mixinStandardHelpOptions = true,
      subcommands = {
            ViewCustomerCommand.class,
            ModifyCustomerEmailCommand.class
      }
)
public class SalesCliEntryCommand {
}
