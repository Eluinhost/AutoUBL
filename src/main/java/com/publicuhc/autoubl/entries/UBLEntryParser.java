package com.publicuhc.autoubl.entries;

public interface UBLEntryParser {

    /**
     * Attempt to parse the entry
     * @return the UBLEntry
     * @throws UBLEntryParseException if could not parse the entry
     */
    UBLEntry parse() throws UBLEntryParseException;
}
