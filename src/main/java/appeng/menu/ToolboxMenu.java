package appeng.menu;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
/**
 * Helper class for dealing with an equipped toolbox.
 */
public class ToolboxMenu {
    public ToolboxMenu(AEBaseMenu menu) {}

    public boolean isPresent() {
        return false;
    }

    public void tick() {}

    public Component getName() {
        return TextComponent.EMPTY;
    }
}
