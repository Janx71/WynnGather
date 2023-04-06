package de.janxcode.wynngather.features.statsoverlay;

import de.janxcode.wynngather.core.interfaces.IFeature;

public class StatsIngameOverlayFeature implements IFeature {
    DrawInfoPanel statsOverlay = null;

    @Override
    public void activate() {
        statsOverlay = new DrawInfoPanel();
        statsOverlay.register();
    }

    @Override
    public void deactivate() {
        statsOverlay.unregister();
        statsOverlay = null;
    }
}
