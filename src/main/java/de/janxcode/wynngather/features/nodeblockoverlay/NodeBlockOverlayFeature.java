package de.janxcode.wynngather.features.nodeblockoverlay;

import de.janxcode.wynngather.features.IFeature;

public class NodeBlockOverlayFeature implements IFeature {
    DrawNodeInfo drawNodeBox = null;
    @Override
    public void activate() {
        drawNodeBox = new DrawNodeInfo();
        drawNodeBox.register();
    }

    @Override
    public void deactivate() {
        drawNodeBox.unregister();
        drawNodeBox = null;
    }
}
