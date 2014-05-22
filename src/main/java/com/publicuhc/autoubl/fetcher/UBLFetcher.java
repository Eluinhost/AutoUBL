package com.publicuhc.autoubl.fetcher;

import com.publicuhc.autoubl.entries.UBLEntry;

import java.util.List;

public abstract interface UBLFetcher {

    /**
     * Attempt to get the UBL
     * @throws UBLFetchException if there was an error during the fetch
     */
    abstract void fetch() throws UBLFetchException;

    /**
     * Attempt to parse the fetched UBL
     * @return The entries from the fetched UBL
     * @throws UBLParseException if there was an error parsing the data
     * @throws IllegalStateException if the fetch method hasn't been called yet
     */
    abstract List<UBLEntry> parse() throws UBLParseException;
}
