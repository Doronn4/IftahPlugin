package me.doron.doronmc;

import me.doron.doronmc.handlers.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class DoronMC extends JavaPlugin {

    @Override
    public void onEnable() {
        PluginManager manager = Bukkit.getServer().getPluginManager();

        //manager.registerEvents(new MamanHandler(), this);
        manager.registerEvents(new MamanHandlerNew(), this);
        manager.registerEvents(new LiamHandler(), this);
        manager.registerEvents(new GabzoHandler(), this);
        manager.registerEvents(new ShakedHandler(), this);
        manager.registerEvents(new DoronHandler(), this);
        manager.registerEvents(new EtamarHandler(), this);

        this.getCommand("kit").setExecutor(new CommandKit());

        Bukkit.getLogger().info("DoronMC Plugin has started!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
