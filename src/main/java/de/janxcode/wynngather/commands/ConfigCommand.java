package de.janxcode.wynngather.commands;

import de.janxcode.wynngather.WynnGather;
import de.janxcode.wynngather.utils.ModConfig;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;

public class ConfigCommand extends CommandBase {
    @Override
    public String getName() {
        return "config";
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
        if(args.length != 2) return;

        int i = convertToInt(args[1]);
        if(i <= 0) return;

        //Some lazy shit but its 8am and im tired
        switch (args[0]){
            case "v+":
                ModConfig.guiPosY += i;
                break;

            case "v-":
                ModConfig.guiPosY -= i;
                break;

            case "h+":
                ModConfig.guiPosX += i;
                break;

            case "h-":
                ModConfig.guiPosX -= i;
                break;
        }

        ConfigManager.sync(WynnGather.MODID, Config.Type.INSTANCE);
    }

    private int convertToInt(String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
