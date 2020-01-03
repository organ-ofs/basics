INSERT INTO sys_resource (`ID`, `code`, `name`, `permission`, `url`, `parent_id`, `type`, `description`, `create_date`,
                          `update_date`, `create_user`, `update_user`, `sort`)
VALUES ('1', 'sys', '系统管理', 'sys', '/sys', '0', '1', NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO sys_resource (`ID`, `code`, `name`, `permission`, `url`, `parent_id`, `type`, `description`, `create_date`,
                          `update_date`, `create_user`, `update_user`, `sort`)
VALUES ('2', 'munus', '菜单管理', 'menus', '/sys/menus', '1', '1', NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO sys_resource (`ID`, `code`, `name`, `permission`, `url`, `parent_id`, `type`, `description`, `create_date`,
                          `update_date`, `create_user`, `update_user`, `sort`)
VALUES ('3', 'user', '用户管理', 'users', '/sys/user', '1', '1', NULL, NULL, NULL, NULL, NULL, 2);
INSERT INTO sys_resource (`ID`, `code`, `name`, `permission`, `url`, `parent_id`, `type`, `description`, `create_date`,
                          `update_date`, `create_user`, `update_user`, `sort`)
VALUES ('4', 'root', '角色管理', 'role', '/sys/role', '1', '1', NULL, NULL, NULL, NULL, NULL, 3);
INSERT INTO sys_resource (`ID`, `code`, `name`, `permission`, `url`, `parent_id`, `type`, `description`, `create_date`,
                          `update_date`, `create_user`, `update_user`, `sort`)
VALUES ('5', 'log', '日志管理', 'log', '/log', '0', '1', NULL, NULL, NULL, NULL, NULL, 2);
INSERT INTO sys_resource (`ID`, `code`, `name`, `permission`, `url`, `parent_id`, `type`, `description`, `create_date`,
                          `update_date`, `create_user`, `update_user`, `sort`)
VALUES ('6', 'sysLog', '操作日志', 'syslogs', '/sys/logs', '5', '1', NULL, NULL, NULL, NULL, NULL, 1);
