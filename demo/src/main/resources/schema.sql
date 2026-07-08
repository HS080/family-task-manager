-- 家庭任务管理与共享平台（实用优化版）
-- 开发期：为保证结构一致性，启动时重建表

SET NAMES utf8mb4;

DROP TABLE IF EXISTS t_task_activity;
DROP TABLE IF EXISTS t_task_comment;
DROP TABLE IF EXISTS t_reminder;
DROP TABLE IF EXISTS t_task;
DROP TABLE IF EXISTS t_permission;
DROP TABLE IF EXISTS t_user;
DROP TABLE IF EXISTS t_family_group;

CREATE TABLE t_family_group (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    family_name VARCHAR(64) NOT NULL,
    invite_code VARCHAR(8) NOT NULL UNIQUE COMMENT '6-8位家庭邀请码',
    owner_user_id BIGINT DEFAULT NULL,
    is_deleted TINYINT NOT NULL DEFAULT 0,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_owner_user_id(owner_user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE t_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_uid VARCHAR(9) NOT NULL UNIQUE COMMENT '9位小写字母+数字用户标识',
    username VARCHAR(32) NOT NULL UNIQUE,
    password VARCHAR(128) NOT NULL,
    nickname VARCHAR(32) NOT NULL,
    role VARCHAR(16) NOT NULL DEFAULT 'USER' COMMENT '用户角色：USER/ADMIN',
    family_identity VARCHAR(16) DEFAULT NULL COMMENT '家庭身份：FATHER/MOTHER/CHILD/OTHER',
    avatar_url VARCHAR(255) DEFAULT NULL,
    family_id BIGINT DEFAULT NULL COMMENT '一人一户，当前归属家庭',
    status TINYINT NOT NULL DEFAULT 1,
    is_deleted TINYINT NOT NULL DEFAULT 0,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_family_id(family_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE t_permission (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    family_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    role_code VARCHAR(16) NOT NULL COMMENT 'OWNER/MEMBER',
    is_deleted TINYINT NOT NULL DEFAULT 0,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_family_user(family_id, user_id),
    INDEX idx_user_id(user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE t_task (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    family_id BIGINT NOT NULL,
    title VARCHAR(120) NOT NULL,
    description VARCHAR(500) DEFAULT NULL,
    assignee_user_id BIGINT NOT NULL,
    creator_user_id BIGINT NOT NULL,
    status TINYINT NOT NULL DEFAULT 0 COMMENT '0待完成 1进行中 2已完成',
    priority TINYINT NOT NULL DEFAULT 1 COMMENT '0低 1中 2高',
    deadline DATETIME NOT NULL,
    completed_at DATETIME DEFAULT NULL,
    is_deleted TINYINT NOT NULL DEFAULT 0,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_family_status(family_id, status),
    INDEX idx_assignee(assignee_user_id),
    INDEX idx_deadline(deadline)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE t_task_comment (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    task_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    content VARCHAR(500) NOT NULL,
    is_deleted TINYINT NOT NULL DEFAULT 0,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_task_id(task_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE t_task_activity (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    task_id BIGINT NOT NULL,
    operator_user_id BIGINT NOT NULL,
    action_type VARCHAR(32) NOT NULL,
    action_desc VARCHAR(255) NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_task_time(task_id, created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE t_reminder (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    family_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    task_id BIGINT DEFAULT NULL,
    remind_type VARCHAR(32) NOT NULL COMMENT 'DUE_SOON/OVERDUE/COMMENT/ASSIGNED/MANUAL',
    content VARCHAR(255) NOT NULL,
    remind_at DATETIME NOT NULL,
    is_read TINYINT NOT NULL DEFAULT 0,
    is_deleted TINYINT NOT NULL DEFAULT 0,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_user_read(user_id, is_read),
    INDEX idx_task(task_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 初始化测试数据：1个OWNER + 2个MEMBER + 1个家庭 + 测试任务
INSERT INTO t_family_group (id, family_name, invite_code, owner_user_id, is_deleted)
VALUES (1, '幸福三口之家', 'HM2026A1', 1, 0);

INSERT INTO t_user (id, user_uid, username, password, nickname, role, family_id, status, is_deleted)
VALUES (1, 'm8a2x7kq1', 'owner001', '123456', '妈妈', 'USER', 1, 1, 0),
       (2, 'p4h9t2nq6', 'member001', '123456', '爸爸', 'USER', 1, 1, 0),
       (3, 'c7m3v8rj2', 'member002', '123456', '孩子', 'USER', 1, 1, 0);

INSERT INTO t_permission (family_id, user_id, role_code, is_deleted)
VALUES (1, 1, 'OWNER', 0),
       (1, 2, 'MEMBER', 0),
       (1, 3, 'MEMBER', 0);

INSERT INTO t_task (family_id, title, description, assignee_user_id, creator_user_id, status, priority, deadline, is_deleted)
VALUES (1, '今晚倒垃圾', '晚饭后20:30前完成', 2, 1, 0, 1, DATE_ADD(NOW(), INTERVAL 6 HOUR), 0),
       (1, '周一前整理书包', '检查作业和文具', 3, 1, 1, 2, DATE_ADD(NOW(), INTERVAL 2 DAY), 0);
