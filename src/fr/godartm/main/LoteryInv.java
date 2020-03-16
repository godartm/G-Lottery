package fr.godartm.main;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class LoteryInv  {

    private Inventory inv;

    /**
     * Association valeur -> index
     */
    public static final int[] ticketIndexList  = {0,2,3,4,5,6,11,12,13,14,15,20,21,22,23,24,29,30,31,32,33};

    public static Map<Player, ArrayList> lottery_map = new HashMap<>();

    LoteryInv(Player p){


        this.inv = Bukkit.createInventory(p, 54, "Loterie");

        /**
         * Création item stack
         */
        ItemStack greyGlass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7);
        ItemStack arrow = new ItemStack(Material.ARROW, 1);
        ItemStack fence = new ItemStack(Material.FENCE, 1);
        ItemStack hardenedClay = new ItemStack(Material.STAINED_CLAY,1 , (short) Main.main.getInt(Conf.COLOR_DEFAULT));

        /**
         * Création item meta
         */
        ItemMeta arrowM = arrow.getItemMeta();
        arrowM.setDisplayName("Annuler");
        arrow.setItemMeta(arrowM);

        ItemMeta fenceM = fence.getItemMeta();
        fenceM.setDisplayName("Merci de choisir 5 numéros");
        fence.setItemMeta(fenceM);

        ItemMeta greyGlassM = greyGlass.getItemMeta();
        greyGlassM.setDisplayName(" ");
        greyGlass.setItemMeta(greyGlassM);

        ItemMeta hardenedM = hardenedClay.getItemMeta();
        hardenedM.setDisplayName(" ");
        hardenedClay.setItemMeta(hardenedM);


        /**
         * SET DE L'INVENTAIRE
         */
        inv.setItem(48,arrow);
        inv.setItem(50,fence);

        inv.setItem(2,hardenedClay); hardenedClay.setAmount(2);
        inv.setItem(3,hardenedClay); hardenedClay.setAmount(3);
        inv.setItem(4,hardenedClay); hardenedClay.setAmount(4);
        inv.setItem(5,hardenedClay); hardenedClay.setAmount(5);
        inv.setItem(6,hardenedClay); hardenedClay.setAmount(6);
        inv.setItem(11,hardenedClay); hardenedClay.setAmount(7);
        inv.setItem(12,hardenedClay); hardenedClay.setAmount(8);
        inv.setItem(13,hardenedClay); hardenedClay.setAmount(9);
        inv.setItem(14,hardenedClay); hardenedClay.setAmount(10);
        inv.setItem(15,hardenedClay); hardenedClay.setAmount(11);
        inv.setItem(20,hardenedClay); hardenedClay.setAmount(12);
        inv.setItem(21,hardenedClay); hardenedClay.setAmount(13);
        inv.setItem(22,hardenedClay); hardenedClay.setAmount(14);
        inv.setItem(23,hardenedClay); hardenedClay.setAmount(15);
        inv.setItem(24,hardenedClay); hardenedClay.setAmount(16);
        inv.setItem(29,hardenedClay); hardenedClay.setAmount(17);
        inv.setItem(30,hardenedClay); hardenedClay.setAmount(18);
        inv.setItem(31,hardenedClay); hardenedClay.setAmount(19);
        inv.setItem(32,hardenedClay); hardenedClay.setAmount(20);
        inv.setItem(33,hardenedClay); hardenedClay.setAmount(21);

        //L'on remplie l'espace restant de grey glass.
        for(int i=0; i<this.inv.getSize(); i++){
            if(this.inv.getItem(i) == null){
                this.inv.setItem(i,greyGlass);
            }
        }
    }

    /**
     *
     * @param p Player qui as fait la commande et dont la couleurs va etre update.
     * @param inventory Inventaire a update.
     * @param index Valeur selectionné le joueur.
     */
    public static void updateInventoryColor(Player p, Inventory inventory , int index) {

        ItemStack hardenedClay = new ItemStack(Material.STAINED_CLAY,index , (short) Main.main.getInt(Conf.COLOR_SELECT));

        ItemMeta hardenedM = hardenedClay.getItemMeta();
        hardenedM.setDisplayName("Vous avez choisi ce numéro !");
        hardenedClay.setItemMeta(hardenedM);

        int inventoryPosition = LoteryInv.ticketIndexList[index];

        inventory.clear(inventoryPosition);
        inventory.setItem(inventoryPosition , hardenedClay);
        p.updateInventory();
    }

    public Inventory getLotery(){

        return this.inv;
    }
}
