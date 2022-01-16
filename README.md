Game Metascore
==============

This is an app that uses Android Architecture to keep a collection of videogames, with data provided through [IGDB API](https://api-docs.igdb.com/).

Introduction
------------

It displays critics rating, users rating, and total rating.
It also saves those scores in a local database to be displayed as updated data is fetched from IGDB API.
Videogames can be added, removed, and marked as played.

This is only for personal use.

### IGDB API Authentication

To access IGDB API, you need to follow [these instructions](https://api-docs.igdb.com/#account-creation).
Once you have a Client ID / Client Secret, add them to the `gradle.properties` file.

```
twitch_client_id=<your client id>
twitch_client_secret=<your client secret>
```

Screenshots
-----------
![screenshot](https://github.com/hgabriel84/videogames-scores/blob/main/screenshots/screenshot.png)
