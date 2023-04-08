package de.janxcode.wynngather.features.statsoverlay;

import de.janxcode.wynngather.core.GatheringSession;
import de.janxcode.wynngather.core.interfaces.IFeature;

public class StatsOverlayFeature implements IFeature {
    StatsOverlayIngameGui statsOverlay = null;
    GatheringSession session = null; // todo: allow creation of session to be done more dynamically

    @Override
    public void activate() {
        statsOverlay = new StatsOverlayIngameGui();
        session = new GatheringSession();
        session.start();
        session.register();
        statsOverlay.setDisplayedSession(session);
        statsOverlay.register();
    }

    @Override
    public void deactivate() {
        session.unregister();
        statsOverlay.unregister();
        statsOverlay = null;
        session = null;
    }
}
