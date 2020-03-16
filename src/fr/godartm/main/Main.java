package fr.godartm.main;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;
import java.util.logging.Logger;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import static fr.godartm.main.LoteryInv.lottery_map;


public class Main  extends JavaPlugin {


    public static Main main;
    public static SqlConnection sql;
    public static final Logger log = Logger.getLogger("Minecraft");
    public static Economy econ = null;

    public void onEnable() {

        main = this;

        rewardManager.winningNumber.add(2);
        rewardManager.winningNumber.add(5);
        rewardManager.winningNumber.add(10);
        rewardManager.winningNumber.add(15);
        rewardManager.winningNumber.add(20);

        if (!setupEconomy() ) {
            log.severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }


        saveDefaultConfig();

        sql = new SqlConnection(get(Conf.URL_BASE), get(Conf.HOST), get(Conf.PORT), get(Conf.DB),
                get(Conf.USER), get(Conf.PASS));

        SqlConnection.openConnection();

        getServer().getPluginManager().registerEvents(new EventsListener(), this);
        getCommand("loterie").setExecutor(new loteryCommand());


        System.out.println("Le plugin loterie est start !");


        LocalDate newDate = new LocalDate();
        if(newDate.dayOfWeek().get() == Main.main.getInt(Conf.REWARD_DAY)) {

            BukkitTask checkHoursTask = new BukkitRunnable() {
                public void run() {

                    LocalDateTime now = LocalDateTime.now();
                    int hour = now.getHourOfDay();
                    int minute = now.getMinuteOfHour();

                    if(hour == Main.main.getInt(Conf.REWARD_HOUR) && minute == Main.main.getInt(Conf.REWARD_MINUTE) ){
                        this.cancel();
                        rewardManager.lock_lottery = true;
                        Bukkit.broadcastMessage("§8§m--------------------------------------------------\n" +
                                "\n" +
                                "§8[§6§lLoterie§8] §e§lAnnonce ! §aTirage de la loterie en cours !\n" +
                                "§aLes 5 numéros vont être tirés et les gagnants annoncés !\n" +
                                "\n" +
                                "§8§m--------------------------------------------------");
/*
                        while (rewardManager.winningNumber.size() < 5){
                            int nombreAleatoire = (int)(Math.random() * 20);

                            if(!rewardManager.winningNumber.contains(nombreAleatoire) && nombreAleatoire != 0){
                               rewardManager.winningNumber.add(nombreAleatoire);
                            }
                       }*/

                        BukkitTask rewardTask = new BukkitRunnable() {
                            @Override
                            public void run() {
                                LocalDateTime now = LocalDateTime.now();
                                int second = now.getSecondOfMinute();

                                switch (second){
                                    case 10:
                                        //Premier nombre a 10s
                                        Bukkit.broadcastMessage(String.format("§8[§6§lLoterie§8] §eLe premier numéro est le §c§l%s !", rewardManager.winningNumber.get(0)));
                                        break;
                                    case 20:
                                        //Second nombre a 20s
                                        Bukkit.broadcastMessage(String.format("§8[§6§lLoterie§8] §eLe second numéro est le §c§l%s !", rewardManager.winningNumber.get(1)));

                                        break;
                                    case 30:
                                        //Troisieme nombre a 30s
                                        Bukkit.broadcastMessage(String.format("§8[§6§lLoterie§8] §eLe troisième numéro est le §c§l%s !", rewardManager.winningNumber.get(2)));

                                        break;
                                    case 40:
                                        //quatrieme nombre a 40s
                                        Bukkit.broadcastMessage(String.format("§8[§6§lLoterie§8] §eLe quatrième numéro est le §c§l%s !", rewardManager.winningNumber.get(3)));

                                        break;
                                    case 50:
                                        //Cinquieme nombre a 50s
                                        Bukkit.broadcastMessage(String.format("§8[§6§lLoterie§8] §eLe cinquième numéro est le §c§l%s !", rewardManager.winningNumber.get(4)));
                                        break;
                                    case 59:
                                        //recompense
                                        this.cancel();

                                        rewardManager reward = new rewardManager(rewardManager.winningNumber.get(0),
                                                rewardManager.winningNumber.get(1),
                                                rewardManager.winningNumber.get(2),
                                                rewardManager.winningNumber.get(3),
                                                rewardManager.winningNumber.get(4));

                                        if(reward.get_totalOfWinners() == 0){
                                            Bukkit.broadcastMessage("§8[§6§lLoterie§8] §eNous n'avons malheureusement aucun grand gagnant.");
                                        }else if(reward.get_fiveNumberWinner().size() != 0){
                                            Bukkit.broadcastMessage(String.format("§8[§6§lLoterie§8] §eNous avons §c§l%s grand(s) gagnant(s) §e!", reward.get_fiveNumberWinner().size()));
                                            reward.get_fiveNumberWinner().forEach(s -> Bukkit.broadcastMessage(String.format("§e Félicitations a %s !" , Bukkit.getOfflinePlayer(UUID.fromString(s)).getName())));
                                        }else{
                                            Bukkit.broadcastMessage(String.format("§8[§6§lLoterie§8] §eNous avons §c§l%s  gagnant(s) §e!", reward.get_totalOfWinners()));
                                        }

                                        lottery_map.clear();
                                        rewardManager.lock_lottery = false;
                                        break;
                                }


                            }
                        }.runTaskTimer(Main.main,0,20);
                    }

                }
            }.runTaskTimer(this,0,20);


        }



    }

    public String get(Conf m) {
        return getConfig().getString(m.get()).replace("&", "§");
    }
    public int getInt(Conf m) {
        return getConfig().getInt(m.get());
    }
    public double getDouble(Conf m) {
        return getConfig().getDouble(m.get());
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public static Economy getEconomy() {
        return econ;
    }
}
