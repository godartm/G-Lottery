package fr.godartm.main;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class loteryCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender instanceof Player){
            if(command.getName().equalsIgnoreCase("loterie")){

                Player p = ((Player) commandSender).getPlayer();
                LoteryInv loteryInv = new LoteryInv(p);
                p.openInventory(loteryInv.getLotery());

            }
        }
        return false;

    }
}
