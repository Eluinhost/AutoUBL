AutoUBL
=======

A plugin that bans players on the [UBL](https://hosts.uhc.gg/ubl) and updates on a regular schedule.

Stores a backup in `/AutoUBL/backup.json` in case of API going down + server restart.

This plugin should work in any version of Bukkit/Spigot from 1.2.5-R5.0 onwards

## Minecraft 1.7.6+

This plugin will stop players from joining the server with a UUID that is on the ban list

## Minecraft < 1.7.6

No UUIDs are available on login in < 1.7.6. When a player joins they are allowed into the server. 
Every 5 seconds the server will lookup UUIDs for each unknown player that has joined if needed. 
If any online player UUID is then detected as banned they will be kicked. Any subsequent joins will
be denied login as the UUID will already be known and is checked against the ban list immediately

## NOTES

This plugin is a work in progress to use the new hosts.uhc.gg API. 
Until the API is in public use and this plugin is completed this plugin *should not be used*

## DEVELOPMENT STAGE: TESTING

## Config

```yaml
minutes per refresh: 10
ubl endpoint: https://hosts.uhc.gg/api/ubl/current
mojangUuidEndpoint: https://api.mojang.com/profiles/minecraft
ban template: "&c{ign} ({uuid}) is banned - {reason} ({link}), expires: {expires}"
uninitialized message: Please wait, ban list is loading
failed to lookup uuid message: Failed to lookup UUID for your username
run update checker: true
```

### `minutes per refresh`

How many minutes between automatically pulling new ban list from the server

### `ubl endpoint`

Where to query the current ban list from. Shouldn't need to be changed

### `mojangUuidEndpoint`

In < 1.7.6 this endpoint will be used for bulk UUID lookups for logged in users. Shouldn't need to be changed

### `ban template`

Allows use of the following:

`{ign}`, `{uuid}`, `{reason}`, `{link}`, `{created}`, `{expires}` 

### `uninitialized message`

Message to show if a user joins before the initial load, this should only really end up happening if there 
was no backup to load from and the server is slow

### `failed to lookup uuid message`

In < 1.7.6 when UUIDs are looked up, if there is an error looking up the UUID of users we 'safety' kick
them. This is the message to show the kicked players

### `run update checker`

Whether to check for updates or not. Checks this github page for any plugin updates and notifies ops/console
