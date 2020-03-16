package fr.godartm.main;

import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.UUID;


public class SqlManager {

    /**
     * @param p player voulant participer a la loterie.
     */
    public static void addPlayer(Player p){

        try {
            PreparedStatement ps = SqlConnection.getConnection().prepareStatement("INSERT INTO `LotteryPlayer` (`uuid`, `number_one` , `number_two`, `number_three`, `number_four`, `number_five`, `amount`) VALUES (?,?,?,?,?,?,?)");
            ps.setString(1,p.getUniqueId().toString());
            ps.setInt(2, (Integer) LoteryInv.lottery_map.get(p).get(0));
            ps.setInt(3, (Integer) LoteryInv.lottery_map.get(p).get(1));
            ps.setInt(4, (Integer) LoteryInv.lottery_map.get(p).get(2));
            ps.setInt(5, (Integer) LoteryInv.lottery_map.get(p).get(3));
            ps.setInt(6, (Integer) LoteryInv.lottery_map.get(p).get(4));
            ps.setInt(7, Main.main.getInt(Conf.PRICE));
            ps.execute();
            ps.close();
        } catch (SQLException e3) {
            e3.printStackTrace();
        }

    }

    /**
     * @param amountLeftToWin montant restant a gagner ou 0
     *
     */
    public static void updateLotteryHistory(double amountLeftToWin){

        try {
            PreparedStatement ps = SqlConnection.getConnection().prepareStatement("UPDATE  `LotteryHistory` SET amount_left_to_win = 0");
            ps.execute();
            ps.close();
        } catch (SQLException e3) {
            e3.printStackTrace();
        }

        try {
            PreparedStatement ps1 = SqlConnection.getConnection().prepareStatement("INSERT INTO `LotteryHistory` ( `amount_left_to_win`) VALUES (?)");
            ps1.setInt(1, (int) amountLeftToWin);

            ps1.execute();
            ps1.close();
        } catch (SQLException e3) {
            e3.printStackTrace();
        }
    }

    /**
     * @return int Total restant a gagner des parties precedentes.
     */
    public static int getAmountFromHistory(){
        try {
            PreparedStatement pS = SqlConnection.getConnection().prepareStatement("SELECT sum(amount_left_to_win) FROM LotteryHistory");
            pS.execute();
            ResultSet datalogin =  pS.getResultSet();
            if (datalogin.next()){
                int amount = datalogin.getInt(1);
                return amount;
            }else{

                pS.close();
                return 0;
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
            return 0;
        }
    }

    /**
     * @return int Total de la recompenses pour les gagnants de la loterie.
     */
    public static double getReward(){

        double amount = 0;

        amount = (amount + getAmountFromHistory());

        try {
            PreparedStatement pS = SqlConnection.getConnection().prepareStatement("SELECT sum(amount) FROM LotteryPlayer");
            pS.execute();
            ResultSet datalogin =  pS.getResultSet();
            if (datalogin.next()){
                amount = (amount + datalogin.getInt(1));

                return amount;
            }else{

                pS.close();
                return amount;
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
            return amount;
        }
    }

    public static boolean checkIfAlreadyAPlayer(UUID uuid){

        try {
            PreparedStatement pS = SqlConnection.getConnection().prepareStatement("SELECT COUNT(uuid) FROM LotteryPlayer WHERE uuid = ?");
            pS.setString(1, uuid.toString());
            pS.execute();
            ResultSet datalogin =  pS.getResultSet();
            if (datalogin.next()){
                int FirstConnection = datalogin.getInt(1);

                if(FirstConnection == 0){

                    pS.close();
                    return true;

                }else{

                    pS.close();
                    return false;
                }
            }else{

                pS.close();
                return false;
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
            return false;
        }


    }

    /**
     * CLEAR la table lotteryPlayer pour la prochaine "partie"
     */
    public static void clearLotteryPlayer() {

        try {
            PreparedStatement pS = SqlConnection.getConnection().prepareStatement("TRUNCATE TABLE LotteryPlayer");
            pS.execute();
            pS.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
