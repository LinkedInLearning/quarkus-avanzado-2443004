package com.kineteco;

import com.kineteco.commands.ModifyCustomerEmailCommand;
import com.kineteco.commands.ViewCustomerCommand;
import io.quarkus.picocli.runtime.annotations.TopCommand;
import picocli.CommandLine;

import javax.enterprise.context.ApplicationScoped;

@TopCommand
@CommandLine.Command(name = "kineteco",
      mixinStandardHelpOptions = true,
      subcommands = {
            ViewCustomerCommand.class,
            ModifyCustomerEmailCommand.class
      })
@ApplicationScoped
public class SalesCli{
}
