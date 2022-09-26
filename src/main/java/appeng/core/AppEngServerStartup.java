package appeng.core;

import net.fabricmc.api.DedicatedServerModInitializer;

@SuppressWarnings("unused")
public class AppEngServerStartup implements DedicatedServerModInitializer {
    public static final boolean DISABLE_AE2_NETWORKS = System.getenv().containsKey("DISABLE_AE2_NETWORKS");
    @Override
    public void onInitializeServer() {
        new AppEngServer();
    }
}
