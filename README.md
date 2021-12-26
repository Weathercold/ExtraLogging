# Extra Logging
Mindustry mod that adds more logging features.\
I initially made this mod to better understand the order in which the events are triggered, now I decided to add more features and turn this into a mod. Also this is my first *published* mod.

## Features
- Listens to the following events and prints them to the console:
    - FileTreeInitEvent
    - ContentInitEvent
    - WorldLoadEvent
    - ClientLoadEvent
    - ClientPreConnectEvent
    - StateChangeEvent
- Timestamp for console messages
    - Only after the mod is created though

## Details on event trigger order
### On client start
1. ClientCreateEvent
2. Mod::new
3. FileTreeInitEvent
4. WorldLoadEvent
5. Mod::registerClientCommands # On servers it's Mod::registerServerCommands
6. Mod:init
7. ClientLoadEvent

### On game start
1. WorldLoadEvent
2. ClientPreConnectEvent       # Only if multiplayer
3. StateChangeEvent

## Note
The order in which the logs are displayed in the in-game console is weird, might have something to do with the log buffer idk\

For some reason the outputs to the Java console are formatted twice which results in duplicated timestamps. If you know how to fix this, please leave a pull request.