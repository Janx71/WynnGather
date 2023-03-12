package de.janxcode.wynngather.utils;

import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class DelayedTaskFrames {
    private int counter;
    private final Runnable runnable;

    public DelayedTaskFrames(Runnable run, int frames){
        counter = frames;
        this.runnable = run;
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onTick(RenderGameOverlayEvent.Text event){
        if(counter <= 0){
            MinecraftForge.EVENT_BUS.unregister(this);
            runnable.run();
        }
        counter--;
    }
}
