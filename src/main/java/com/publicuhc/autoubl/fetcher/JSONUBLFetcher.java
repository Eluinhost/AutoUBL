package com.publicuhc.autoubl.fetcher;

import com.publicuhc.autoubl.entries.UBLEntry;

import java.net.URL;
import java.util.List;

public class JSONUBLFetcher implements UBLFetcher {

    private final URL m_ublLocation;

    public JSONUBLFetcher(URL ublLocation) {
        m_ublLocation = ublLocation;
    }

    @Override
    public void fetch() throws UBLFetchException {
        //TODO
    }

    @Override
    public List<UBLEntry> parse() throws UBLParseException {
        //TODO
        return null;
    }
}
