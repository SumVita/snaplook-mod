package com.snaplook;

/**
 * Simple in-memory config for Snaplook.
 * Can be extended with a proper JSON/TOML config file if desired.
 */
public class SnaplookConfig {

    /**
     * If true, pressing the key toggles the perspective instead of holding.
     * Default: false (hold mode, like Lunar Client default)
     */
    public boolean toggleMode = false;

    /**
     * If true, snaplook is enabled at all.
     */
    public boolean enabled = true;
}
