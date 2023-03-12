package de.janxcode.wynngather.handlers;

import de.janxcode.wynngather.inforenderer.Node;
import net.minecraftforge.fml.common.eventhandler.Event;

public class NodeMinedEvent extends Event {
    public final int xp;
    public final int next;
    public final Node node;
    public NodeMinedEvent(int xp, int next, Node node){
        this.xp = xp;
        this.next = next;
        this.node = node;
    }
}
