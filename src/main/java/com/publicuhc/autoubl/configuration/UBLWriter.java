package com.publicuhc.autoubl.configuration;

import com.publicuhc.autoubl.entries.UBLEntry;

import java.util.Collection;

public class UBLWriter implements Runnable {

    private final Collection<UBLEntry> m_entries;

    public UBLWriter(Collection<UBLEntry> entries) {
        m_entries = entries;
    }

    @Override
    public void run() {
        //TODO write the bans to the config file
    }
}
