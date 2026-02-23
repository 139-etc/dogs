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
    stamina INT(10),
    max_event INT(10),
    max_point_event INT(10)
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
    result_stamina INT(10) ,
    result_point_rate DECIMAL(2,1),
    UNIQUE KEY (incident,choice),
    FOREIGN KEY (incident) REFERENCES event(incident)
);


/* ステータスマスタ */
CREATE TABLE IF NOT EXISTS status (
    id VARCHAR(50) PRIMARY KEY,
    breed VARCHAR(100) ,
    stamina INT(10) ,
    point INT(10),
    times INT(10)
);

/* 変更履歴マスタ */
CREATE TABLE IF NOT EXISTS log (
    id VARCHAR(10) PRIMARY KEY,
    user_id VARCHAR(50),
  	user_name VARCHAR(50),
  	breed VARCHAR(100) ,
    diff_stamina INT(10), 
    diff_point INT(10),
     times INT(10),
    update_time TIMESTAMP
);

/* リザルトマスタ */
CREATE TABLE IF NOT EXISTS result (
    id VARCHAR(10) PRIMARY KEY,
    user_id VARCHAR(50),
  	user_name VARCHAR(50),
  	breed VARCHAR(100),
  	stamina INT(10),
  	point INT(10),
    times INT(10),
    result_time TIMESTAMP
);
