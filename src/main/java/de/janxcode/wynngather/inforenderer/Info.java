package de.janxcode.wynngather.inforenderer;

import de.janxcode.wynngather.handlers.NodeMinedEvent;
import de.janxcode.wynngather.utils.Utils;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Info {
    private static Info instance = null;
    private int xp;
    private long startTime;
    private int nodesMined;
    private int nextLevel;
    private String type;
    private String progress;

    private Info(){}

    public static Info getInstance(){
        if(instance == null){
            instance = new Info();
        }

        return instance;
    }

    public void register(){
        startTime = System.currentTimeMillis();
        xp = 0;
        nodesMined = 0;
        nextLevel = 0;
        type = "" + TextFormatting.RED + TextFormatting.BOLD + "Not Set";
        progress = "";

        MinecraftForge.EVENT_BUS.register(instance);
    }

    public void unregister(){
        MinecraftForge.EVENT_BUS.unregister(instance);
    }

    @SubscribeEvent
    public void onNode(NodeMinedEvent e){
        nodesMined++;
        nextLevel = e.next;
        xp += e.xp;
        type = e.node.getType();
    }

    public int getNodesMined(){
        return nodesMined;
    }

    public void setProgress(String progress){
        this.progress = progress;
    }

    public String getType(){
        return type;
    }

    public int getNextLevel(){
        return nextLevel;
    }

    public String getProgress(){
        return progress;
    }

    public String getTime(){
        long seconds = (System.currentTimeMillis() - startTime) / 1000;

        return String.format("%02d:%02d:%02d", seconds / 3600, (seconds % 3600) / 60, seconds % 60);
    }

    public int getNodesPerMinute(){
        long seconds = (System.currentTimeMillis() - startTime) / 1000;

        if(seconds / 60d > 0){
            return (int) (nodesMined / (seconds / 60d));
        }

        return 0;
    }

    public String getXpPerHour(){
        long seconds = (System.currentTimeMillis() - startTime) / 1000;

        if(seconds / 3600d > 0){
            return Utils.formatValue((long) (xp / (seconds / 3600d)));
        }

        return "0";
    }
}
