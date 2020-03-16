package fr.godartm.main;


public enum Conf {
    URL_BASE("storage.mysqlStorage.urlBase"),
    HOST("storage.mysqlStorage.host"),
    PORT("storage.mysqlStorage.port"),
    DB("storage.mysqlStorage.database"),
    USER("storage.mysqlStorage.username"),
    PASS("storage.mysqlStorage.password"),
    PRICE("lotery.price"),
    COLOR_SELECT("lotery.color_select"),
    COLOR_DEFAULT("lotery.color_default"),
    FIVE_NUMBER_REWARD("lotery.reward.fiveNumber"),
    FOUR_NUMBER_REWARD("lotery.reward.fourNumber"),
    THREE_NUMBER_REWARD("lotery.reward.threeNumber"),
    TWO_NUMBER_REWARD("lotery.reward.twoNumber"),
    REWARD_DAY("lotery.reward.rewardDay"),
    REWARD_HOUR("lotery.reward.rewardHour"),
    REWARD_MINUTE("lotery.reward.rewardMinute");


    private String string;

    private Conf(String string) {
        this.string = string;
    }

    public String get() {
        return this.string;
    }
}