package de.janxcode.wynngather.utils;

public class HorizontalPos {
    private final int x;
    private final int z;

    public HorizontalPos(int x, int z){
        this.x = x;
        this.z = z;
    }

    public int getX(){
        return x;
    }

    public int getZ(){
        return z;
    }

    @Override
    public boolean equals(Object obj){
        if(!(obj instanceof HorizontalPos)) return false;
        HorizontalPos pos = (HorizontalPos) obj;

        return this.getX() == pos.getX() && this.getZ() == pos.getZ();
    }
}
