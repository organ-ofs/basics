SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS `job_task`;
CREATE TABLE `job_task`
(
  `id`          int(11)      NOT NULL AUTO_INCREMENT,
  `name`        varchar(255) DEFAULT NULL,
  `job_group`   varchar(255) DEFAULT NULL,
  `cron`        varchar(255) DEFAULT NULL,
  `parameter`   varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `vm_param`    varchar(255) DEFAULT NULL,
  `jar_path`    varchar(255) DEFAULT NULL,
  `status`      varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 7
  DEFAULT CHARSET = utf8;
INSERT INTO `job_task`
VALUES ('1', 'first', 'helloworld', '0/10 * * * * ? ', '1', '1', '', null, 'OPEN');
INSERT INTO `job_task`
VALUES ('2', 'second', 'helloworld', '0/5 * * * * ? ', '2', '2', null, null, 'CLOSE');
INSERT INTO `job_task`
VALUES ('4', 'third', 'helloworld', '0/15 * * * * ? ', '3', '3', null, null, 'CLOSE');
INSERT INTO `job_task`
VALUES ('5', 'four', 'helloworld', '0 0/1 * * * ? *', '4', '4', null, null, 'CLOSE');
INSERT INTO `job_task`
VALUES ('6', 'OLAY Job', 'Nomal', '0 0/2 * * * ?', '', '5', null, 'C:\\EalenXie\\Download\\JDE-Order-1.0-SNAPSHOT.jar',
        'CLOSE');
