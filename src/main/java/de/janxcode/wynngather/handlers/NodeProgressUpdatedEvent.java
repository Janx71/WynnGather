package de.janxcode.wynngather.handlers;

import net.minecraftforge.fml.common.eventhandler.Event;

public class NodeProgressUpdatedEvent extends Event {
    public static class MinedEvent extends NodeProgressUpdatedEvent {
        public final int xp;
        public final int next;

        public MinedEvent(int xp, int nexLevel, GatherNode node) {
            super(node);
            this.xp = xp;
            this.next = nexLevel;
        }
    }

    public final GatherNode node;

    public NodeProgressUpdatedEvent(GatherNode node) {
        this.node = node;
    }
}
