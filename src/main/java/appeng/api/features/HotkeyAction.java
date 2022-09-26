package appeng.api.features;

import net.minecraft.world.entity.player.Player;

import appeng.hotkeys.HotkeyActions;

/**
 * locates and opens Inventories from items
 */
public interface HotkeyAction {

    /**
     * locates and opens an Inventory and returns if it has found anything
     */
    boolean run(Player player);

    String WIRELESS_TERMINAL = "wireless_terminal";
    /**
     * register a new {@link HotkeyAction} under an id
     * <p/>
     * {@link HotkeyAction}s which are added later will be called first
     * <p/>
     * a Keybinding will be created automatically for every id
     */
    static void register(HotkeyAction hotkeyAction, String id) {
        HotkeyActions.register(hotkeyAction, id);
    }
}
