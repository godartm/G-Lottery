package fr.godartm.main;

import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;



public class EventsListener implements Listener {


    public EventsListener() {

    }

    @EventHandler
    public void inventoryClickEvent(InventoryClickEvent e) {

        if (e.getClickedInventory() != null) {
            if (e.getClickedInventory().getName() != null) {

                if (e.getInventory().getTitle().equalsIgnoreCase("Loterie")) {

                    Player p = (Player) e.getWhoClicked();
                    ItemStack clicked = e.getCurrentItem();

                    if (clicked.getType() == Material.ARROW) {
                        p.closeInventory();
                        e.setCancelled(true);
                    }

                    /**
                     * Si la map ne contient pas le joueur alors on crée l'array list et on l'ajoute.
                     */
                    if (!LoteryInv.lottery_map.containsKey(p)) {
                        ArrayList selectNumber = new ArrayList();
                        LoteryInv.lottery_map.put(p, selectNumber);
                    }

                    ArrayList selectNumberlist = LoteryInv.lottery_map.get(p);

                    /**
                     * Permet de refresh les tickets selectionné en cas de reprise du choix des numéros.
                     */
                    if(selectNumberlist.size() != 0){
                        selectNumberlist.forEach(
                                (index) -> LoteryInv.updateInventoryColor(p, e.getClickedInventory(), (Integer) index));
                    }

                    if (selectNumberlist.size() < 5) {

                        if (clicked.getType() == Material.STAINED_CLAY) {
                            int amount = clicked.getAmount();
                            if (!selectNumberlist.contains(amount)) {

                                /**
                                 * L'on ajoute le nombre choisi.
                                 */
                                selectNumberlist.add(amount);

                                selectNumberlist.forEach((index) -> LoteryInv.updateInventoryColor(p, e.getClickedInventory(), (Integer) index));

                                e.setCancelled(true);
                            } else {
                                //Le joueur a cliquer sur un nombre deja choisi l'on ne fait rien.
                                e.setCancelled(true);
                            }
                        } else {
                            e.setCancelled(true);
                        }
                    }

                    if (selectNumberlist.size() == 5) {

                        selectNumberlist.forEach(
                                (index) -> LoteryInv.updateInventoryColor(p, e.getClickedInventory(), (Integer) index));

                        ItemStack netherStars = new ItemStack(Material.NETHER_STAR, 1);

                        ItemMeta netherStarsM = netherStars.getItemMeta();
                        netherStarsM.setDisplayName("Valider et payer " + Main.main.getInt(Conf.PRICE) + " $");
                        netherStars.setItemMeta(netherStarsM);

                        e.getClickedInventory().clear(50);
                        e.getClickedInventory().setItem(50, netherStars);
                        p.updateInventory();

                        if(clicked.getType() == Material.NETHER_STAR){

                            if(!rewardManager.lock_lottery) {
                                if(SqlManager.checkIfAlreadyAPlayer(p.getUniqueId())){


                                    EconomyResponse r = Main.econ.withdrawPlayer(p, Main.main.getInt(Conf.PRICE));

                                    if(r.transactionSuccess()) {
                                        p.sendMessage(String.format("§8[§c§lLoterie§8] §aVotre pari a bien été sauvegardé : §e%s§7, §e%s§7, §e%s§7, §e%s§7, §e%s§a pour un prix de %s",
                                                LoteryInv.lottery_map.get(p).get(0),
                                                LoteryInv.lottery_map.get(p).get(1),
                                                LoteryInv.lottery_map.get(p).get(2),
                                                LoteryInv.lottery_map.get(p).get(3),
                                                LoteryInv.lottery_map.get(p).get(4),
                                                Main.econ.format(r.amount)));

                                        SqlManager.addPlayer(p);
                                        p.closeInventory();

                                    } else {
                                        p.sendMessage("§8[§c§lLoterie§8] §cErreur : Vous n'avez pas assez d'argent pour jouer.");
                                        p.closeInventory();
                                    }
                                }else{
                                    p.sendMessage("§8[§c§lLoterie§8] §cErreur : Vous participez déjà à cette lotterie.");
                                    p.closeInventory();

                                }
                                e.setCancelled(true);
                            }else {
                                p.sendMessage("§8[§c§lLoterie§8] §cErreur : La lotterie n'est pas encore ouverte ou est déjà fermée.");
                                p.closeInventory();
                            }
                            e.setCancelled(true);
                        }
                        e.setCancelled(true);
                    }
                }
            }
        }
    }


    public void onQuitEvent(PlayerQuitEvent e){

        LoteryInv.lottery_map.remove(e.getPlayer());
    }
}
