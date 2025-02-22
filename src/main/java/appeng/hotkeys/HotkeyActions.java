package appeng.hotkeys;

import static appeng.api.features.HotkeyAction.WIRELESS_TERMINAL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import appeng.api.features.HotkeyAction;
import appeng.core.AppEng;
import appeng.core.definitions.AEItems;

/**
 * registers {@link HotkeyAction}
 */
public class HotkeyActions {
    public static final HashMap<String, List<HotkeyAction>> REGISTRY = new HashMap<>();

    static {
        register(
                new InventoryHotkeyAction(AEItems.WIRELESS_TERMINAL.asItem(),
                        (player, i) -> AEItems.WIRELESS_TERMINAL.asItem().openFromInventory(player, i)),
                WIRELESS_TERMINAL);
        register(
                new InventoryHotkeyAction(AEItems.WIRELESS_CRAFTING_TERMINAL.asItem(),
                        (player, i) -> AEItems.WIRELESS_CRAFTING_TERMINAL.asItem().openFromInventory(player, i)),
                WIRELESS_TERMINAL);
    }

    public static void init() {
        // init static
    }

    /**
     * see {@link HotkeyAction#register(HotkeyAction, String)}
     */
    public static synchronized void register(HotkeyAction hotkeyAction, String id) {
        if (REGISTRY.containsKey(id)) {
            REGISTRY.get(id).add(0, hotkeyAction);
        } else {
            REGISTRY.put(id, new ArrayList<>(List.of(hotkeyAction)));
            AppEng.instance().registerHotkey(id);
        }
    }
}
