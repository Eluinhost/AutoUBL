AutoUBL
=======

A plugin that bans players on the UBL. 
Auto updates on a schedule and doesn't affect the regular whitelist.

## DEVELOPMENT STAGE: IN PROGRESS

Reads from a json source with the following style:

    [
        {
            "uuid": "048fa310-30de-44fe-9f5e-c7443e91ad46",
            "reason": "Test Reason",
            "expiryDate": "Mon, 3 Dec 2007 12:00:22 +0100",
            "courtroom": "http://google.com"
        },
        {
            "uuid": "6ac803fd-132f-4540-a741-cb18ffeed8ce",
            "reason": "Test Reason 2",
            "expiryDate": "Mon, 10 Dec 2007 12:00:22 +0100",
            "courtroom": "http://publicuhc.com"
        }
    ]

The source is not yet available but will be from a website API