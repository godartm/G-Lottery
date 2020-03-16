package fr.godartm.main;

import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class rewardManager {

    public static ArrayList<Integer> winningNumber = new ArrayList<>();
    public static boolean lock_lottery = false;


    private ArrayList<String> _threeNumberWinner = new ArrayList<>();
    private ArrayList<String> _fourNumberWinner = new ArrayList<>();
    private ArrayList<String> _fiveNumberWinner = new ArrayList<>();
    private ArrayList<String>  _twoNumberWinner = new ArrayList<>();

    private double _fiveNumberAmount;
    private double _fourNumberAmount;
    private double _threeNumberAmount;
    private double _twoNumberAmount;

    private int _totalOfWinners;
    private double _amountLeftToWin = 0;
    private ArrayList<String> _winnerUuid = new ArrayList<>();


    public rewardManager(int numberOne,int numberTwo,int numberThree,int numberFour,int numberFive ){


        double rewardAmount = SqlManager.getReward();

        this._fiveNumberAmount = rewardAmount * (Main.main.getDouble(Conf.FIVE_NUMBER_REWARD)/100) ;
        this._fourNumberAmount = rewardAmount * (Main.main.getDouble(Conf.FOUR_NUMBER_REWARD)/100) ;
        this._threeNumberAmount = rewardAmount * (Main.main.getDouble(Conf.THREE_NUMBER_REWARD)/100) ;
        this._twoNumberAmount = rewardAmount * (Main.main.getDouble(Conf.TWO_NUMBER_REWARD)/100) ;

        try {
            PreparedStatement pS = SqlConnection.getConnection().prepareStatement("SELECT * FROM LotteryPlayer");

            int totalNumberFound = 0;
            pS.execute();
            ResultSet datalogin =  pS.getResultSet();
            while (datalogin.next()){
                String uuidWinner = datalogin.getString(1);

                int dataFirstNumber = datalogin.getInt(2);
                int dataSecondNumber = datalogin.getInt(3);
                int dataThirdNumber = datalogin.getInt(4);
                int dataFourNumber = datalogin.getInt(5);
                int dataFiveNumber = datalogin.getInt(6);

                if(dataFirstNumber == numberOne || dataFirstNumber == numberTwo || dataFirstNumber == numberThree || dataFirstNumber == numberFour || dataFirstNumber == numberFive ){
                    totalNumberFound = totalNumberFound + 1;
                }

                if(dataSecondNumber == numberOne || dataSecondNumber == numberTwo || dataSecondNumber == numberThree || dataSecondNumber == numberFour || dataSecondNumber == numberFive ){
                    totalNumberFound = totalNumberFound + 1;
                }

                if(dataThirdNumber == numberOne || dataThirdNumber == numberTwo || dataThirdNumber == numberThree || dataThirdNumber == numberFour || dataThirdNumber == numberFive ){
                    totalNumberFound = totalNumberFound + 1;
                }

                if(dataFourNumber == numberOne || dataFourNumber == numberTwo || dataFourNumber == numberThree || dataFourNumber == numberFour || dataFourNumber == numberFive ){
                    totalNumberFound = totalNumberFound + 1;
                }

                if(dataFiveNumber == numberOne || dataFiveNumber == numberTwo || dataFiveNumber == numberThree || dataFiveNumber == numberFour || dataFiveNumber == numberFive ){
                    totalNumberFound = totalNumberFound + 1;
                }

                if(uuidWinner != null){
                    if(totalNumberFound == 2){
                        //trouver 2 num
                        //todo stats
                        this._twoNumberWinner.add(uuidWinner);
                        this._totalOfWinners = this._totalOfWinners + 1;
                        System.out.println(" 2 nombre");

                    }else if(totalNumberFound == 3){
                        //trouver 3
                        this. _threeNumberWinner.add(uuidWinner);
                        this._totalOfWinners = this._totalOfWinners + 1;
                        System.out.println(" 3 nombre");

                    }else if(totalNumberFound == 4){
                        //trouver 4
                        this. _fourNumberWinner.add(uuidWinner);
                        this._totalOfWinners = this._totalOfWinners + 1;
                        System.out.println(" 4 nombre");

                    }else if(totalNumberFound == 5){
                        //trouver 5
                        this. _fiveNumberWinner.add(uuidWinner);
                        this._totalOfWinners = this._totalOfWinners + 1;
                        System.out.println(" 5 nombre");
                    }
                }else{
                    System.out.println("[ALERT] un UUID d'un joueur de la lotterie est potentiel vide en bdd !");
                }


                totalNumberFound = 0;

            }
            pS.close();
        } catch (SQLException e1) {
            e1.printStackTrace();
        }




        if(this.get_totalOfWinners() != 0){
            if(this._twoNumberWinner.size() != 0){
                this._winnerUuid.addAll(this._twoNumberWinner);
                this._twoNumberWinner.forEach(s -> this.giveReward(s ,this._twoNumberAmount , _twoNumberWinner.size() , 2 ));
            }else{
                this._amountLeftToWin = this._amountLeftToWin + _twoNumberAmount;
            }
            if(this._threeNumberWinner.size() != 0){
                this._winnerUuid.addAll(this._threeNumberWinner);
                this._threeNumberWinner.forEach(s -> this.giveReward(s ,this._threeNumberAmount , _threeNumberWinner.size() , 3 ));
            }else{
                this._amountLeftToWin = this._amountLeftToWin + _threeNumberAmount;
            }
            if(this._fourNumberWinner.size() != 0){
                this._winnerUuid.addAll(this._fourNumberWinner);
                this._fourNumberWinner.forEach(s -> this.giveReward(s ,this._fourNumberAmount, _fourNumberWinner.size() , 4));
            }else{
                this._amountLeftToWin = this._amountLeftToWin + _fourNumberAmount;

            }
            if(this._fiveNumberWinner.size() != 0){
                this._winnerUuid.addAll(this._fiveNumberWinner);
                this._fiveNumberWinner.forEach(s -> this.giveReward(s ,this._fiveNumberAmount , _fiveNumberWinner.size() , 5));
            }else{
                this._amountLeftToWin = this._amountLeftToWin + _fiveNumberAmount;
            }
        }else{
            this._amountLeftToWin = rewardAmount;
        }


        SqlManager.updateLotteryHistory(this._amountLeftToWin);
        SqlManager.clearLotteryPlayer();


    }

    private void giveReward(String s, double numberAmount ,double  winnerAmount , int i) {

        System.out.println("NUMBER AMOUT " + numberAmount);
        System.out.println("winnerAmount" + winnerAmount);
        double finalAmountPerPlayer = (numberAmount / winnerAmount);
        System.out.println("finalAmountPerPlayer" + finalAmountPerPlayer);
        OfflinePlayer Winner =  Bukkit.getServer().getOfflinePlayer(UUID.fromString(s));

        Main.econ.depositPlayer(Winner, finalAmountPerPlayer);

        if(Winner.isOnline()){
            ((Player) Winner).sendMessage(String.format("§8[§c§lLoterie§8] §aVous avez trouvé §e%s §abons numéros lors de cette loterie. Vous gagnez §c%s $",i,finalAmountPerPlayer));
        }


    }


    public ArrayList<String> get_threeNumberWinner() {
        return _threeNumberWinner;
    }

    public ArrayList<String> get_fourNumberWinner() {
        return _fourNumberWinner;
    }

    public ArrayList<String> get_fiveNumberWinner() {
        return _fiveNumberWinner;
    }

    public ArrayList<String> get_winnerUuid() {
        return _winnerUuid;
    }

    public double get_fiveNumberAmount() {
        return _fiveNumberAmount;
    }

    public double get_fourNumberAmount() {
        return _fourNumberAmount;
    }

    public double get_threeNumberAmount() {
        return _threeNumberAmount;
    }
    public double get_twoNumberAmount() {
        return _twoNumberAmount;
    }

    public int get_totalOfWinners() {
        return _totalOfWinners;
    }
}
