package com.publicuhc.autoubl.entries;

import java.util.Date;
import java.util.UUID;

public interface UBLEntry {

    /**
     * @return The UUID of the banned account
     */
    UUID getUUID();

    /**
     * @param uuid the UUID to ban
     * @return this
     */
    UBLEntry setUUID(UUID uuid);

    /**
     * @return The reason for the ban
     */
    String getReason();

    /**
     * @param reason the reason for the ban
     * @return this
     */
    UBLEntry setReason(String reason);

    /**
     * @return the date banned
     */
    Date getDateBanned();

    /**
     * @param banned the date banned
     * @return this
     */
    UBLEntry setDateBanned(Date banned);

    /**
     * @return the length of the ban to show
     */
    String lengthOfBan();

    /**
     * @param lengthOfBan the length of ban to show
     * @return this
     */
    UBLEntry setLengthOfBan(String lengthOfBan);

    /**
     * @return the date for unban
     */
    Date getExpiryDate();

    /**
     * @param expiryDate the date for unban
     * @return this
     */
    UBLEntry setExpiryDate(Date expiryDate);

    /**
     * @return the URL to the courtroom post
     */
    String getCourtroomPost();

    /**
     * @param courtroomPost the URL to the post
     * @return this
     */
    UBLEntry setCourtroomPost(String courtroomPost);
}
