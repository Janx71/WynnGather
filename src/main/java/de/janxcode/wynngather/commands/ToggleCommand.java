package de.janxcode.wynngather.commands;

import de.janxcode.wynngather.WynnGather;
import de.janxcode.wynngather.handlers.ArmourStandHandler;
import de.janxcode.wynngather.inforenderer.DrawBoundingBox;
import de.janxcode.wynngather.inforenderer.DrawInfoPanel;
import de.janxcode.wynngather.inforenderer.Node;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;

import java.util.List;

public class ToggleCommand extends CommandBase {
    private final DrawBoundingBox box = new DrawBoundingBox();
    private final ArmourStandHandler handler = new ArmourStandHandler();
    private final DrawInfoPanel info = new DrawInfoPanel();
    private final List<Node> nodes = WynnGather.NODES;
    private boolean toggled = false;

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
            if (toggled){
                unregister();
            } else {
                register();
            }
            toggled = !toggled;
            return;
        }

        switch (args[0]){
            case "start":
                register();
                break;

            case "stop":
                unregister();
                break;
        }
    }

    private void unregister(){
        MinecraftForge.EVENT_BUS.unregister(box);
        MinecraftForge.EVENT_BUS.unregister(handler);
        MinecraftForge.EVENT_BUS.unregister(info);
        nodes.clear();
    }

    private void register(){
        MinecraftForge.EVENT_BUS.register(box);
        MinecraftForge.EVENT_BUS.register(handler);
        MinecraftForge.EVENT_BUS.register(info);
        info.init();
        nodes.clear();
    }
}
