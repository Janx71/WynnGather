package de.janxcode.wynngather.core.interfaces;


import net.minecraftforge.common.MinecraftForge;

public interface IRegisterable {  // todo: rename
    default void register() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    default void unregister() {
        MinecraftForge.EVENT_BUS.unregister(this);
    }
}
