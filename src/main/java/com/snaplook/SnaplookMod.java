package com.snaplook;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Snaplook Mod for Minecraft Fabric
 *
 * Replicates Lunar Client's Snaplook mod:
 *  - Hold (default): Hold a key to snap to a perspective, release to return.
 *  - Toggle mode: Press a key to toggle into a perspective, press again to return.
 *
 * Three separate keybinds:
 *  - Third Person (Back)  -- default: F4
 *  - Front View           -- default: unbound
 *  - Back View            -- default: unbound
 */
public class SnaplookMod implements ClientModInitializer {

    public static final String MOD_ID = "snaplook";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final SnaplookConfig CONFIG = new SnaplookConfig();

    private static KeyBinding thirdPersonKey;
    private static KeyBinding frontViewKey;
    private static KeyBinding backViewKey;

    private boolean thirdPersonHeld = false;
    private boolean frontViewHeld   = false;
    private boolean backViewHeld    = false;

    private Perspective savedPerspective = null;
    private KeyBinding activeHoldKey = null;

    @Override
    public void onInitializeClient() {
        LOGGER.info("[Snaplook] Mod loaded.");

        thirdPersonKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.snaplook.third_person",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_F4,
                "category.snaplook"
        ));

        frontViewKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.snaplook.front",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_UNKNOWN,
                "category.snaplook"
        ));

        backViewKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.snaplook.back",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_UNKNOWN,
                "category.snaplook"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(this::onClientTick);
    }

    private void onClientTick(MinecraftClient client) {
        if (client.player == null || client.world == null) return;
        if (!CONFIG.enabled) return;

        if (CONFIG.toggleMode) {
            tickToggleMode(client);
        } else {
            tickHoldMode(client);
        }
    }

    // HOLD MODE
    private void tickHoldMode(MinecraftClient client) {
        boolean tpNow    = thirdPersonKey.isPressed();
        boolean frontNow = frontViewKey.isPressed();
        boolean backNow  = backViewKey.isPressed();

        if (tpNow && !thirdPersonHeld) activateHold(client, thirdPersonKey, Perspective.THIRD_PERSON_BACK);
        if (frontNow && !frontViewHeld) activateHold(client, frontViewKey, Perspective.THIRD_PERSON_FRONT);
        if (backNow && !backViewHeld) activateHold(client, backViewKey, Perspective.THIRD_PERSON_BACK);

        if (!tpNow && thirdPersonHeld && activeHoldKey == thirdPersonKey) deactivateHold(client);
        if (!frontNow && frontViewHeld && activeHoldKey == frontViewKey) deactivateHold(client);
        if (!backNow && backViewHeld && activeHoldKey == backViewKey) deactivateHold(client);

        thirdPersonHeld = tpNow;
        frontViewHeld   = frontNow;
        backViewHeld    = backNow;
    }

    private void activateHold(MinecraftClient client, KeyBinding key, Perspective target) {
        if (activeHoldKey != null) return;
        savedPerspective = client.options.getPerspective();
        client.options.setPerspective(target);
        activeHoldKey = key;
    }

    private void deactivateHold(MinecraftClient client) {
        if (savedPerspective != null) {
            client.options.setPerspective(savedPerspective);
            savedPerspective = null;
        }
        activeHoldKey = null;
    }

    // TOGGLE MODE
    private void tickToggleMode(MinecraftClient client) {
        if (thirdPersonKey.wasPressed()) toggle(client, Perspective.THIRD_PERSON_BACK);
        if (frontViewKey.wasPressed()) toggle(client, Perspective.THIRD_PERSON_FRONT);
        if (backViewKey.wasPressed()) toggle(client, Perspective.THIRD_PERSON_BACK);
    }

    private void toggle(MinecraftClient client, Perspective target) {
        Perspective current = client.options.getPerspective();
        if (current == target && savedPerspective != null) {
            client.options.setPerspective(savedPerspective);
            savedPerspective = null;
        } else {
            savedPerspective = current;
            client.options.setPerspective(target);
        }
    }

    public static KeyBinding getThirdPersonKey() { return thirdPersonKey; }
    public static KeyBinding getFrontViewKey()   { return frontViewKey; }
    public static KeyBinding getBackViewKey()    { return backViewKey; }
}
