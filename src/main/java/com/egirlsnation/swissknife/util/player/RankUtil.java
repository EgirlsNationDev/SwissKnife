package com.egirlsnation.swissknife.util.player;

import com.egirlsnation.swissknife.hooks.votingPlugin.UserUtils;
import org.bukkit.*;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static com.egirlsnation.swissknife.SwissKnife.Config.*;

public class RankUtil {

    private final UserUtils userUtils = new UserUtils();

    public void promoteIfEligible(@NotNull Player player){

        if(!player.hasPlayedBefore()) return;

        //Name "PLAY_ONE_MINUTE" is missleading. It's actually in ticks.
        int pt = player.getStatistic(Statistic.PLAY_ONE_MINUTE);
        final ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();

        if(pt >= getTicksFromHours(midfagHours) && !player.hasPermission("egirls.rank.vet")){ //Hours to ticks
            String command = "lp user " + player.getName() + " parent add veteran";
            Bukkit.dispatchCommand(console, command);
            Bukkit.getServer().broadcastMessage(player.getDisplayName() + ChatColor.GREEN + " reached " + ChatColor.YELLOW + "MidFag" + ChatColor.GREEN + "!");
        }

        if(pt >= getTicksFromHours(oldfagHours) && !player.hasPermission("egirls.rank.oldfag")){ //Hours to ticks
            String command = "lp user " + player.getName() + " parent add oldfag";
            Bukkit.dispatchCommand(console, command);
            Bukkit.getServer().broadcastMessage(player.getDisplayName() + ChatColor.GREEN + " reached " + ChatColor.RED + "OldFag" + ChatColor.GREEN + "!");
            player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, SoundCategory.PLAYERS, 100, 0);
        }

        if(userUtils.getUserManager() == null) return;
        if(pt >= getTicksFromHours(elderfagHours) && userUtils.getVotes(player) >= 300 && !player.hasPermission("egirls.rank.legend")){ //Hours to ticks
            String command = "lp user " + player.getName() + " parent add legend";
            Bukkit.dispatchCommand(console, command);
            Bukkit.getServer().broadcastMessage(player.getDisplayName() + ChatColor.GREEN + " reached " + ChatColor.AQUA + "ElderFag" + ChatColor.GREEN + "!");
            for(Player p : Bukkit.getServer().getOnlinePlayers()){
                p.playSound(p.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, SoundCategory.PLAYERS, 100, 0);
            }
        }
    }

    public int getTicksFromHours(int hours){
        int ticks = hours * 20;
        ticks = ticks * 60;
        ticks = ticks * 60;
        return ticks;
    }
}
