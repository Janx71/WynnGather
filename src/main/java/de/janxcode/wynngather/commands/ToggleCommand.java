package de.janxcode.wynngather.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class ToggleCommand extends CommandBase {

    private final ToggleMod mod = new ToggleMod();
    @Override
    public String getName() {
        return "toggle";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return null;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if(args.length == 0) {
            mod.toggle();
        }

        switch (args[0]){
            case "start":
                mod.register();
                break;

            case "stop":
                mod.unregister();
                break;
        }
    }


}
