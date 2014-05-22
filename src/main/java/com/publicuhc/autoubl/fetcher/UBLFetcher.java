package com.publicuhc.autoubl.fetcher;

import com.publicuhc.autoubl.entries.UBLEntry;

import java.util.List;

public abstract interface UBLFetcher {

    /**
     * Attempt to parse the fetched UBL
     * @return The entries from the fetched UBL
     * @throws UBLParseException if there was an error parsing the data
     * @throws UBLFetchException if there was an error fetching the data
     */
    abstract List<UBLEntry> fetch() throws UBLParseException, UBLFetchException;
}
