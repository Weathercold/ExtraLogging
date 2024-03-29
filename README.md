# Extra Logging

[![Commit Testing](https://github.com/Weathercold/ExtraLogging/actions/workflows/commitTest.yml/badge.svg?branch=master)](https://github.com/Weathercold/ExtraLogging/actions/workflows/commitTest.yml) [![Total Downloads](https://img.shields.io/github/downloads/Weathercold/ExtraLogging/total?color=success&labelColor=gray&label=Downloads&logo=docusign&logoColor=white)](https://github.com/Weathercold/ExtraLogging/releases)\
Mindustry mod that adds more logging features and improvements.\
I initially made this mod to better understand the order in which the events are triggered, now I decided to add more
features and turn this into a mod. Also this is my first *published* mod.

## Features

- Enables the in-game console.
- Adds customisable log level<sup>[1]</sup>.
- **Fully customizable log format /w timestamp support**<sup>[1]</sup>.
- **Optionally reenables/disables colored terminal**<sup>[2]</sup>.
- Fixes log display order (vanilla issue).
- **Translates chat messages to your locale**.
- Optionally prints important event triggers to the console.
- Various other quality of life improvements.

## Details on event trigger order

### On client start

1. ClientCreateEvent
2. Mod::new
3. FileTreeInitEvent
4. Mod::loadContent
5. ContentInitEvent
6. WorldLoadEvent
7. Mod::registerClientCommands # On servers it's Mod::registerServerCommands
8. Mod:init
9. ClientLoadEvent

### On game start

1. WorldLoadEvent
2. ClientPreConnectEvent # Only if multiplayer
3. StateChangeEvent

---

[1]: Only after the mod is created though\
[2]: For Windows and Android, you need a modern terminal that supports ANSI color codes. ~~This proves Linux is
superior~~