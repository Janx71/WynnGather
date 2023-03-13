package de.janxcode.wynngather.commands;

import de.janxcode.wynngather.gui.InfoLineGui;
import de.janxcode.wynngather.gui.ModMenuGui;
import de.janxcode.wynngather.utils.DelayedTaskFrames;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class ToggleCommand extends CommandBase {

    private final ToggleMod mod = new ToggleMod();
    @Override
    public String getName() {
        return "gather";
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
            return;
        }

        switch (args[0]){
            case "start":
                mod.register();
                break;

            case "stop":
                mod.unregister();
                break;

            case "pos":
                new DelayedTaskFrames((() -> Minecraft.getMinecraft().displayGuiScreen(new ModMenuGui())), 1);
                break;

            case "menu":
                new DelayedTaskFrames((() -> Minecraft.getMinecraft().displayGuiScreen(new InfoLineGui())), 1);
                break;
        }
    }


}
