package com.publicuhc.autoubl.fetcher;

import java.net.URL;

public class JSONUBLFetcher implements UBLFetcher {

    private final URL m_ublLocation;

    public JSONUBLFetcher(URL ublLocation) {
        m_ublLocation = ublLocation;
    }


    @Override
    public void run() {
        //TODO fetch the ubl json and parse it, then save it to the config file
    }
}
