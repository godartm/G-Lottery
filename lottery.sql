CREATE DATABASE lottery;


CREATE TABLE `LotteryHistory` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `amount_left_to_win` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `LotteryPlayer` (
  `uuid` varchar(36) NOT NULL,
  `number_one` int(11) NOT NULL,
  `number_two` int(11) NOT NULL,
  `number_three` int(11) NOT NULL,
  `number_four` int(11) NOT NULL,
  `number_five` int(11) NOT NULL,
  `amount` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
