# G-Lottery
This project is a simple lottery system for Minecraft.
This plugin run a lottery every week.

If you don't have winner the amount of money is added for the next week.



## Prerequisite

- Vault
- Joda-Time
- Mysql Database
- economy

## Configuration

```
mysqlStorage:
  urlBase: "jdbc:mysql://"
  host: "localhost"
  port: "3306"
  database: "lottery"
  username: "youruserhere"
  password: "yourpasswordhere"
```

```
lotery:
  price: 1000 # The price of a ticket
  color_select : 13 
  color_default : 9
  
  reward:
  	#The repartition of the money for the winner (must be equal to 100)
    fiveNumber : 70
    fourNumber : 20
    threeNumber : 9
    twoNumber: 1
    
    #The day when the lottery should be run (exemple 1 is monday)
    rewardDay: 3 
    rewardHour : 7
    rewardMinute : 50
```