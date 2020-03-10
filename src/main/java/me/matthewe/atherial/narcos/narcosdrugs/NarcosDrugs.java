package me.matthewe.atherial.narcos.narcosdrugs;

import me.matthewe.atherial.narcos.narcosdrugs.config.MenuConfig;
import me.matthewe.atherial.narcos.narcosdrugs.config.Messages;
import me.matthewe.atherial.narcos.narcosdrugs.config.NarcosDrugsCommandMessages;
import me.matthewe.atherial.narcos.narcosdrugs.drug.DrugHandler;
import me.matthewe.atherial.narcos.narcosdrugs.drugpacks.DrugPackHandler;
import me.matthewe.atherial.narcos.narcosdrugs.gson.GsonHandler;
import me.matthewe.atherial.narcos.narcosdrugs.level.LevelHandler;
import me.matthewe.atherial.narcos.narcosdrugs.normal.NormalHandler;
import me.matthewe.atherial.narcos.narcosdrugs.npc.NPCHandler;
import me.matthewe.atherial.narcos.narcosdrugs.player.PlayerHandler;
import me.matthewe.atherial.narcos.narcosdrugs.quality.QualityHandler;
import me.matthewe.atherial.narcos.narcosdrugs.wand.WandHandler;
import net.atherial.api.event.AtherialEventListener;
import net.atherial.api.menu.MenuDependency;
import net.atherial.api.plugin.AtherialPlugin;
import net.atherial.api.plugin.AtherialTasks;
import net.atherial.api.plugin.hologram.HolographicDisplaysHologramManager;
import net.atherial.api.plugin.item.AtherialItemAPI;
import net.atherial.api.vault.VaultDependency;

import java.util.ArrayList;
import java.util.List;

public class NarcosDrugs extends AtherialPlugin {
    private static NarcosDrugs instance;
    private AtherialEventListener eventListener;
    private List<NarcosHandler> handlers;
    private MenuConfig menuConfig;
    private Messages messages;
    private HolographicDisplaysHologramManager<NarcosDrugs> hologramManager;


    public NarcosDrugs() {
        this.handlers = new ArrayList<>();
    }

    @Override
    public void initDependencies() {
        addDependency(new MenuDependency(this));
        addDependency(new VaultDependency(this));
    }

    @Override
    public void initConfigs() {

    }

    @Override
    public void onAtherialEnable() {
        instance = this;
        this.enableAtherial();

        AtherialTasks.setPlugin(this);
        AtherialItemAPI.setAtherialPlugin(this);

        getDependency(MenuDependency.class).init();
        getDependency(VaultDependency.class).init("NarcosDrugs");
        commandMessages = new NarcosDrugsCommandMessages(this);
        this.eventListener = AtherialEventListener.create(this);
        this.menuConfig = new MenuConfig(this).load();

        this.hologramManager = new HolographicDisplaysHologramManager<>(this);
        this.messages = new Messages(this);
        this.handlers.add(new DrugHandler(this));
        this.handlers.add(new DrugPackHandler(this));
        this.handlers.add(new GsonHandler(this));
        this.handlers.add(new QualityHandler(this));
        this.handlers.add(new LevelHandler(this));
        this.handlers.add(new PlayerHandler(this));
        this.handlers.add(new NPCHandler(this, this.hologramManager));
        this.handlers.add(new WandHandler(this));
        this.handlers.add(new NormalHandler(this));

        this.handlers.forEach(NarcosHandler::enable);

        this.handlers.forEach(NarcosHandler::registerListeners);
    }

    public <T extends NarcosHandler> T getHandler(Class<T> clazz) {
        return (T) handlers.stream().filter(narcosHandler -> narcosHandler.getClass().equals(clazz)).findFirst().orElse(null);
    }

    public HolographicDisplaysHologramManager<NarcosDrugs> getHologramManager() {
        return hologramManager;
    }

    public void reloadHandlers() {
        NarcosHandler.setReloading(true);
        this.menuConfig = new MenuConfig(this).load();
        this.messages = new Messages(this);
        this.handlers.forEach(NarcosHandler::reloadHandler);
        NarcosHandler.setReloading(false);
    }

    public List<NarcosHandler> getHandlers() {
        return handlers;
    }

    public MenuConfig getMenuConfig() {
        return menuConfig;
    }

    public Messages getMessages() {
        return messages;
    }

    @Override
    public void onDisable() {
        this.handlers.forEach(NarcosHandler::disable);
    }

    public static NarcosDrugs getInstance() {
        return instance;
    }

    public AtherialEventListener getEventListener() {
        return eventListener;
    }
}
