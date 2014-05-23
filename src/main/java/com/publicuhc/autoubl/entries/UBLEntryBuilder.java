package com.publicuhc.autoubl.entries;

import com.publicuhc.autoubl.fetcher.UBLParseException;

public interface UBLEntryBuilder {

    UBLEntry build() throws UBLParseException;
}
