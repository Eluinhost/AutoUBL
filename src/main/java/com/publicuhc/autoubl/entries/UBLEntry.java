package com.publicuhc.autoubl.entries;

import java.util.Date;
import java.util.UUID;

public interface UBLEntry {

    /**
     * @return The UUID of the banned account
     */
    UUID getUUID();

    /**
     * @return The reason for the ban
     */
    String getReason();

    /**
     * @return the date banned
     */
    Date getDateBanned();

    /**
     * @return the length of the ban to show
     */
    String lengthOfBan();

    /**
     * @return the date for unban
     */
    Date getExpiryDate();

    /**
     * @return the URL to the courtroom post
     */
    String getCourtroomPost();

}
