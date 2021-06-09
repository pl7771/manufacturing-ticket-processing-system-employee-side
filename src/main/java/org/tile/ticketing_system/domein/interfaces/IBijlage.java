package org.tile.ticketing_system.domein.interfaces;

public interface IBijlage {
    @Override
    String toString();

    String getBijlageId();

    String getFileType();

    String getName();

    byte[] getDataFiles();
}
