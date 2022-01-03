package cn.lanink.keephunger;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerDeathEvent;
import cn.nukkit.event.player.PlayerRespawnEvent;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;

/**
 * @author lt_name
 */
public class KeepHunger extends PluginBase implements Listener {

    private Config config;

    @Override
    public void onEnable() {
        this.config = new Config(this.getDataFolder() + "/config.yml", Config.YAML);

        this.getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public Config getConfig() {
        return this.config;
    }

    /**
     * 玩家死亡时记录饥饿值
     *
     * @param event 玩家死亡事件
     */
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        this.config.set(player.getName(), player.getFoodData().getLevel());
        this.config.save();
    }

    /**
     * 玩家复活后设置饥饿值
     *
     * @param event 玩家重生事件
     */
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        this.getServer().getScheduler().scheduleDelayedTask(this, () -> {
            int level = Math.max(this.config.getInt(player.getName(), player.getFoodData().getMaxLevel()), 1);
            player.getFoodData().setLevel(level);
        }, 1);
    }

}
