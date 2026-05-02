# Snaplook — Fabric Mod

A Fabric mod for Minecraft 1.21.4 that replicates **Lunar Client's Snaplook** feature.

## What it does

Snaplook lets you **instantly snap to a specific camera perspective** by holding (or toggling) a keybind, then automatically returns to your previous perspective when you let go.

This is different from vanilla's F5 which cycles through all three perspectives — Snaplook jumps directly to a target view.

## Features

| Feature | Details |
|---|---|
| **Hold mode** (default) | Hold the key → snap to perspective. Release → return to previous. |
| **Toggle mode** | Press once to enter perspective, press again to return. |
| **3 keybinds** | Third Person (Back), Front View, Back View — all independently bindable |
| **Smart restore** | Always remembers exactly which perspective you were in before |

## Keybinds (configurable in Controls menu)

| Action | Default Key |
|---|---|
| Snaplook (Third Person) | `F4` |
| Snaplook (Front View) | *Unbound* |
| Snaplook (Back View) | *Unbound* |

All keybinds appear under the **"Snaplook"** category in Options → Controls → Keybinds.

## Toggle vs Hold mode

By default, the mod uses **hold mode** (same as Lunar Client default).

To switch to toggle mode, edit `SnaplookConfig.java` and set `toggleMode = true`.
> A proper config screen / file can be added with Cloth Config if desired.

## How to build

**Requirements:** Java 21, Gradle

```bash
# Clone / download the project, then:
./gradlew build
```

The compiled `.jar` will appear in `build/libs/snaplook-1.0.0.jar`.

Copy it to your `.minecraft/mods/` folder alongside **Fabric Loader** and **Fabric API**.

## Dependencies

- Minecraft 1.21.4
- Fabric Loader ≥ 0.16.0
- Fabric API

## Compatibility

Snaplook only changes your **local camera perspective** — it does not send any extra packets to the server, making it safe to use on any server.

## License

MIT
