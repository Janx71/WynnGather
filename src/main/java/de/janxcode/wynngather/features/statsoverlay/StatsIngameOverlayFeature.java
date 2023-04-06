package de.janxcode.wynngather.features.statsoverlay;

import de.janxcode.wynngather.core.interfaces.IFeature;

public class StatsIngameOverlayFeature implements IFeature {
    DrawInfoPanel statsOverlay = null;

    @Override
    public void activate() {
        statsOverlay = new DrawInfoPanel();
        GatheringSession session = new GatheringSession();  // todo: allow creation of session to be done more dynamically
        session.start();
        session.register();
        statsOverlay.setDisplayedSession(session);
        statsOverlay.register();
    }

    @Override
    public void deactivate() {
        statsOverlay.unregister();
        statsOverlay = null;
    }
}
