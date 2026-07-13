-- カテゴリ
INSERT IGNORE INTO categories (id, name) VALUES (1, '和食');
INSERT IGNORE INTO categories (id, name) VALUES (2, '洋食');
INSERT IGNORE INTO categories (id, name) VALUES (3, 'イタリアン');
INSERT IGNORE INTO categories (id, name) VALUES (4, 'フレンチ');
INSERT IGNORE INTO categories (id, name) VALUES (5, '中華料理');
INSERT IGNORE INTO categories (id, name) VALUES (6, '韓国料理');
INSERT IGNORE INTO categories (id, name) VALUES (7, 'ステーキ');
INSERT IGNORE INTO categories (id, name) VALUES (8, 'ハンバーガー');
INSERT IGNORE INTO categories (id, name) VALUES (9, 'そば');
INSERT IGNORE INTO categories (id, name) VALUES (10, 'うどん');
INSERT IGNORE INTO categories (id, name) VALUES (11, 'お好み焼き');
INSERT IGNORE INTO categories (id, name) VALUES (12, '鍋');
INSERT IGNORE INTO categories (id, name) VALUES (13, 'スイーツ');


-- 店舗情報
-- shops
INSERT IGNORE INTO shops
(id, name, description, phone_number, email, price_range, opening_time, closing_time, holiday, image_name, postal_code, address)
VALUES

-- 名古屋駅エリア
(1,'侍食堂','名古屋名物を楽しめる人気和食店。','080-1111-1111','shop01@sample.com',3000,'11:00:00','23:00:00','なし','shop_01.jpg','450-0002','愛知県名古屋市中村区名駅1-1-1'),

(2,'旬彩堂','四季折々の会席料理が自慢。','080-1111-1112','shop02@sample.com',8000,'17:00:00','22:00:00','月曜日','shop_02.jpg','450-0002','愛知県名古屋市中村区名駅2-2-2'),

(3,'洋食キッチン侍','昔ながらの洋食を味わえるレストラン。','080-1111-1113','shop03@sample.com',2000,'11:00:00','21:00:00','火曜日','shop_03.jpg','450-0003','愛知県名古屋市中村区名駅南1-3-3'),

-- 栄・伏見・錦エリア
(4,'トラットリア・ロッソ','本格窯焼きピザが人気。','080-1111-1114','shop04@sample.com',4000,'11:30:00','22:00:00','水曜日','shop_04.jpg','460-0008','愛知県名古屋市中区栄3-4-4'),

(5,'パスタ工房ベラ','自家製生パスタが自慢。','080-1111-1115','shop05@sample.com',2500,'11:00:00','21:30:00','なし','shop_05.jpg','460-0003','愛知県名古屋市中区錦2-5-5'),

(6,'ビストロ青空','気軽に楽しめる本格フレンチ。','080-1111-1116','shop06@sample.com',6000,'18:00:00','23:00:00','日曜日','shop_06.jpg','460-0008','愛知県名古屋市中区栄2-6-6'),

-- 大須・金山エリア
(7,'龍門飯店','四川料理が人気の中華店。','080-1111-1117','shop07@sample.com',1500,'11:00:00','22:00:00','なし','shop_07.jpg','460-0011','愛知県名古屋市中区大須2-7-7'),

(8,'北京楼','本格点心を楽しめる中華料理店。','080-1111-1118','shop08@sample.com',3500,'11:30:00','21:00:00','木曜日','shop_08.jpg','460-0022','愛知県名古屋市中区金山3-8-8'),

(9,'ソウルキッチン','サムギョプサルと韓国鍋が人気。','080-1111-1119','shop09@sample.com',4000,'17:00:00','23:30:00','なし','shop_09.jpg','460-0011','愛知県名古屋市中区大須1-9-9'),

-- 北部エリア
(10,'ステーキ侍','厳選牛を使用したステーキ専門店。','080-1111-1120','shop10@sample.com',9000,'11:00:00','22:00:00','なし','shop_10.jpg','462-0841','愛知県名古屋市北区黒川1-10-10'),

(11,'バーガーファクトリー','肉厚パティのハンバーガー専門店。','080-1111-1121','shop11@sample.com',1800,'10:00:00','21:00:00','なし','shop_11.jpg','462-0810','愛知県名古屋市北区山田1-11-11'),

-- 南部・名古屋港エリア
(12,'手打ちそば匠','毎朝打つ十割そばが人気。','080-1111-1122','shop12@sample.com',1200,'11:00:00','20:00:00','水曜日','shop_12.jpg','455-0033','愛知県名古屋市港区港町1-12-12'),

(13,'讃岐うどん一番','本場仕込みの讃岐うどん。','080-1111-1123','shop13@sample.com',1000,'10:30:00','20:30:00','なし','shop_13.jpg','457-0841','愛知県名古屋市南区豊田2-13-13'),

-- 東部エリア
(14,'浪花焼き本舗','ふわふわ食感のお好み焼き。','080-1111-1124','shop14@sample.com',2000,'11:00:00','22:00:00','火曜日','shop_14.jpg','465-0025','愛知県名古屋市名東区上社1-14-14'),

(15,'パティスリー桜','季節のケーキと焼き菓子のお店。','080-1111-1125','shop15@sample.com',1500,'10:00:00','19:00:00','月曜日','shop_15.jpg','466-0825','愛知県名古屋市昭和区八事本町1-15-15');

-- shop_categories
-- 侍食堂 → 和食・鍋
INSERT IGNORE INTO shop_categories VALUES (1, 1);
INSERT IGNORE INTO shop_categories VALUES (1, 12);

-- 旬彩堂 → 和食
INSERT IGNORE INTO shop_categories VALUES (2, 1);

-- 洋食キッチン侍 → 洋食
INSERT IGNORE INTO shop_categories VALUES (3, 2);

-- トラットリア・ロッソ → イタリアン
INSERT IGNORE INTO shop_categories VALUES (4, 3);

-- パスタ工房ベラ → イタリアン
INSERT IGNORE INTO shop_categories VALUES (5, 3);

-- ビストロ青空 → フレンチ
INSERT IGNORE INTO shop_categories VALUES (6, 4);

-- 龍門飯店 → 中華
INSERT IGNORE INTO shop_categories VALUES (7, 5);

-- 北京楼 → 中華
INSERT IGNORE INTO shop_categories VALUES (8, 5);

-- ソウルキッチン → 韓国料理・鍋
INSERT IGNORE INTO shop_categories VALUES (9, 6);
INSERT IGNORE INTO shop_categories VALUES (9, 12);

-- ステーキ侍 → ステーキ
INSERT IGNORE INTO shop_categories VALUES (10, 7);

-- バーガーファクトリー → ハンバーガー
INSERT IGNORE INTO shop_categories VALUES (11, 8);

-- 手打ちそば匠 → そば
INSERT IGNORE INTO shop_categories VALUES (12, 9);

-- 讃岐うどん一番 → うどん
INSERT IGNORE INTO shop_categories VALUES (13, 10);

-- 浪花焼き本舗 → お好み焼き
INSERT IGNORE INTO shop_categories VALUES (14, 11);

-- パティスリー桜 → スイーツ
INSERT IGNORE INTO shop_categories VALUES (15, 13);


-- Role
INSERT IGNORE INTO roles (id, name) VALUES
(1, 'ROLE_ADMIN'),
(2, 'ROLE_GENERAL');

-- メンバー
INSERT IGNORE INTO users (
    name,
    furigana,
    email,
    password,
    phone_number,
    membership_type,
    date_of_birth,
    occupation,
    role_id,
    enabled,
    stripe_customer_id,   -- 👈 スキマに合わせて追加
    stripe_subscription_id -- 👈 スキマに合わせて追加
) VALUES
(
    '管理者',
    'カンリシャ',
    'admin@example.com',
    '$2a$10$Du3wDkv2NTv3ADdKG6vHjuLHI9HDo6urThmNR9DAnU6WiRophefSe',
    '09000000000',
    1,
    '1985-01-01',
    'システム管理者',
    1,
    true,
    NULL,
    NULL
),
(
    '山田太郎',
    'ヤマダタロウ',
    'yamada@example.com',
    '$2a$10$Du3wDkv2NTv3ADdKG6vHjuLHI9HDo6urThmNR9DAnU6WiRophefSe',
    '09011111111',
    1,
    '1990-04-10',
    '会社員',
    2,
    true,
    NULL,
    NULL
),
(
    '佐藤花子',
    'サトウハナコ',
    'sato@example.com',
    '$2a$10$Du3wDkv2NTv3ADdKG6vHjuLHI9HDo6urThmNR9DAnU6WiRophefSe',
    '09022222222',
    1,
    '1992-06-15',
    '看護師',
    2,
    true,
    NULL,
    NULL
),
(
    '鈴木一郎',
    'スズキイチロウ',
    'suzuki@example.com',
    '$2a$10$Du3wDkv2NTv3ADdKG6vHjuLHI9HDo6urThmNR9DAnU6WiRophefSe',
    '09033333333',
    1,
    '1988-03-20',
    '公務員',
    2,
    true,
    NULL,
    NULL
),
(
    '高橋美咲',
    'タカハシミサキ',
    'takahashi@example.com',
    '$2a$10$Du3wDkv2NTv3ADdKG6vHjuLHI9HDo6urThmNR9DAnU6WiRophefSe',
    '09044444444',
    0,
    '1995-09-12',
    '保育士',
    2,
    true,
    NULL,
    NULL
),
(
    '田中健',
    'タナカケン',
    'tanaka@example.com',
    '$2a$10$Du3wDkv2NTv3ADdKG6vHjuLHI9HDo6urThmNR9DAnU6WiRophefSe',
    '09055555555',
    0,
    '1987-11-08',
    '営業職',
    2,
    true,
    NULL,
    NULL
),
(
    '伊藤陽子',
    'イトウヨウコ',
    'ito@example.com',
    '$2a$10$Du3wDkv2NTv3ADdKG6vHjuLHI9HDo6urThmNR9DAnU6WiRophefSe',
    '09066666666',
    0,
    '1993-08-25',
    '事務職',
    2,
    true,
    NULL,
    NULL
),
(
    '渡辺翔',
    'ワタナベショウ',
    'watanabe@example.com',
    '$2a$10$Du3wDkv2NTv3ADdKG6vHjuLHI9HDo6urThmNR9DAnU6WiRophefSe',
    '09077777777',
    0,
    '1991-02-14',
    'エンジニア',
    2,
    true,
    NULL,
    NULL
),
(
    '中村彩',
    'ナカムラアヤ',
    'nakamura@example.com',
    '$2a$10$Du3wDkv2NTv3ADdKG6vHjuLHI9HDo6urThmNR9DAnU6WiRophefSe',
    '09088888888',
    0,
    '1994-07-03',
    'デザイナー',
    2,
    true,
    NULL,
    NULL
),
(
    '小林拓也',
    'コバヤシタクヤ',
    'kobayashi@example.com',
    '$2a$10$Du3wDkv2NTv3ADdKG6vHjuLHI9HDo6urThmNR9DAnU6WiRophefSe',
    '09099999999',
    0,
    '1989-12-21',
    '自営業',
    2,
    true,
    NULL,
    NULL
),
(
    '加藤真由',
    'カトウマユ',
    'kato@example.com',
    '$2a$10$Du3wDkv2NTv3ADdKG6vHjuLHI9HDo6urThmNR9DAnU6WiRophefSe',
    '08011112222',
    0,
    '1996-05-30',
    '教師',
    2,
    true,
    NULL,
    NULL
),
(
    '管理者2',
    'カンリシャ2',
    'administrator@example.com',
    '$2a$10$Du3wDkv2NTv3ADdKG6vHjuLHI9HDo6urThmNR9DAnU6WiRophefSe',
    '09012345678',
    1,
    '1996-05-17',
    'システム管理者',
    1,
    true,
    NULL,
    NULL
),
(
    '有料太郎',
    'ユウリョウタロウ',
    'yuryo@example.com',
    '$2a$10$Du3wDkv2NTv3ADdKG6vHjuLHI9HDo6urThmNR9DAnU6WiRophefSe',
    '08033334444',
    1,
    '1997-10-18',
    '学生',
    2,
    true,
    'cus_UrpE9s5fJX2qRm',
    'sub_1Ts5a5CPuMjWnUiT0v7gmCfb'
), -- 👈 セミコロンをカンマに修正
(
    '無料次郎',
    'ムリョウジロウ',
    'muryo@example.com',
    '$2a$10$Du3wDkv2NTv3ADdKG6vHjuLHI9HDo6urThmNR9DAnU6WiRophefSe',
    '08033334444',
    0,
    '1997-10-18',
    '学生',
    2,
    true,
    NULL,
    NULL
);

-- reservations (予約データ)

INSERT INTO reservations (shop_id, user_id, reservation_datetime, number_of_people, status) VALUES 
(1, 2, '2026-08-10 18:00:00', 2, 'CONFIRMED'),
(2, 2, '2026-08-11 19:00:00', 4, 'CONFIRMED'),
(3, 2, '2026-08-12 12:00:00', 2, 'CANCELLED'),
(4, 2, '2026-08-13 18:30:00', 3, 'CONFIRMED'),
(1, 3, '2026-08-14 19:30:00', 2, 'CONFIRMED'),
(2, 3, '2026-08-15 11:30:00', 1, 'CONFIRMED'),
(3, 3, '2026-08-16 20:00:00', 5, 'CONFIRMED'),
(8, 3, '2026-08-17 18:00:00', 2, 'CONFIRMED'),
(9, 4, '2026-08-18 19:00:00', 2, 'CANCELLED'),
(10, 4, '2026-08-19 12:30:00', 4, 'CONFIRMED'),
(11, 4, '2026-08-20 18:00:00', 2, 'CONFIRMED'),
(12, 4, '2026-08-21 19:00:00', 6, 'CONFIRMED'),
(12, 2, '2026-08-22 18:30:00', 2, 'CONFIRMED'),
(12, 3, '2026-08-23 20:30:00', 3, 'CONFIRMED'),
(15, 3, '2026-08-24 17:30:00', 2, 'CONFIRMED');

-- favorites (お気に入りデータ)
INSERT INTO favorites (user_id, shop_id) VALUES 
(2, 2),
(2, 5),
(2, 3),
(2, 10),
(2, 15),
(3, 1),
(3, 7),
(3, 8),
(3, 9),
(3, 11),
(3, 12),
(4, 4),
(4, 13),
(4, 14),
(12, 6);


-- reviews (レビューデータ)
INSERT INTO reviews (user_id, shop_id, rating, comment) VALUES 
(2, 1, 4, '料理の味は抜群ですが、少し混んでいました。'),
(2, 2, 5, '最高のディナーになりました！また絶対来ます。'),
(2, 3, 3,'普通に美味しいです。ランチはお得かも。'),
(2, 4, 5, '店内の雰囲気がとてもおしゃれでデートにぴったりです。'),
(2, 5, 2, '接客が少し残念でした。味は良いのでもったいない。'),
(2, 6, 4, 'ボリューム満点で大満足！学生さんにもおすすめです。'),
(2, 7, 5, '個室があって落ち着いて食事ができました。接待にも使えそう。'),
(2, 8, 4, '季節のメニューが豊富で、何度来ても飽きません。'),
(2, 9, 3, '可もなく不可もなく、安定した美味しさです。'),
(3, 2, 5, 'ワインの品揃えが豊富で、料理とのペアリングが最高でした。'),
(3, 3, 4, '駅から近くて便利！雨の日でも濡れずに行けます。'),
(3, 13, 1, '注文してから料理が出てくるまでかなり待たされました。'),
(4, 13, 5, 'デザートまで全部美味しかったです。女性に人気なのも納得。'),
(4, 14, 4, '夜景が綺麗でロマンチックな時間を過ごせました。'),
(4, 15, 5, '地元の食材を活かした料理が絶品！シェフのこだわりを感じます。');