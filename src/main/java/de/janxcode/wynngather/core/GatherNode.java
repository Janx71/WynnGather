package de.janxcode.wynngather.core;

import de.janxcode.wynngather.core.interfaces.IGatherNode;
import de.janxcode.wynngather.utils.HorizontalPos;

public class GatherNode implements IGatherNode {
    private final HorizontalPos pos;
    private final int yPos;
    private final String type;
    private boolean mined;
    private long minedTimeStamp;
    private String miningProgress;
    private String nodeProf;

    public GatherNode(HorizontalPos pos, int yPos, String type) {
        this.pos = pos;
        this.yPos = yPos;
        this.type = type;
        mined = false;
        minedTimeStamp = 0;
        miningProgress = null;
        nodeProf = "";
    }

    public void setMined() {
        mined = true;
        minedTimeStamp = System.currentTimeMillis();
    }

    public int getYPos() {
        return yPos;
    }

    public void setMiningProgress(String progress) {
        miningProgress = progress;
    }

    public String getMiningProgress() {
        return miningProgress;
    }

    public boolean isMined() {
        if (!mined) return false;

        if (System.currentTimeMillis() - minedTimeStamp > 60000) {
            mined = false;
            minedTimeStamp = 0;
        }

        return mined;
    }

    public long getMinedTime() {
        return System.currentTimeMillis() - minedTimeStamp;
    }

    public HorizontalPos getPos() {
        return pos;
    }

    public String getProf() {
        return nodeProf;
    }

    public void setProf(String prof) {
        nodeProf = prof;
    }

    public String getType() {
        return type;
    }
}
