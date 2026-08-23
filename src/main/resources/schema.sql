/* ユーザーマスタ */
CREATE TABLE IF NOT EXISTS m_user (
    user_id VARCHAR(50) PRIMARY KEY
  , password VARCHAR(100)
  , user_name VARCHAR(50)
  , role VARCHAR(50)
);

/* 犬種マスタ */
CREATE TABLE IF NOT EXISTS breed (
    id VARCHAR(10) PRIMARY KEY,
    name VARCHAR(100) ,
    stamina INT,
    max_event INT,
    max_point_event INT
);

/* イベントマスタ */
CREATE TABLE IF NOT EXISTS event (
    id VARCHAR(10) PRIMARY KEY,
    incident VARCHAR(100)
);

/* 選択肢マスタ */
CREATE TABLE IF NOT EXISTS choice (
	id VARCHAR(10) PRIMARY KEY,
    incident VARCHAR(100),
    choice VARCHAR(100),
    result_stamina INT ,
    result_point_rate DECIMAL(2,1),
    UNIQUE (incident,choice)
);


/* ステータスマスタ */
CREATE TABLE IF NOT EXISTS status (
    id VARCHAR(50) PRIMARY KEY,
    breed VARCHAR(100) ,
    stamina INT ,
    point INT,
    game_start_time TIMESTAMP
);

/* 変更履歴マスタ */
CREATE TABLE IF NOT EXISTS log (
    id VARCHAR(10) PRIMARY KEY,
    user_id VARCHAR(50),
  	user_name VARCHAR(50),
  	breed VARCHAR(100) ,
    diff_stamina INT, 
    diff_point INT,
    started_time TIMESTAMP,
    update_time TIMESTAMP
);

/* リザルトマスタ */
CREATE TABLE IF NOT EXISTS result (
    id VARCHAR(67) PRIMARY KEY,
    user_id VARCHAR(50),
  	user_name VARCHAR(50),
  	breed VARCHAR(100),
  	stamina INT,
  	point INT,
    started_time TIMESTAMP,
    update_time TIMESTAMP
);
