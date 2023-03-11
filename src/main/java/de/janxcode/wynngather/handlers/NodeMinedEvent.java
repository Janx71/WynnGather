package de.janxcode.wynngather.handlers;

import net.minecraftforge.fml.common.eventhandler.Event;

public class NodeMinedEvent extends Event {
    public final int xp;
    public final int next;
    public NodeMinedEvent(int xp, int next){
        this.xp = xp;
        this.next = next;
    }
}
