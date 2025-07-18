INSERT INTO students (name, kana_name, nickname, email, area, age, gender, remark, is_deleted)
 VALUES
 ('田中 太郎', 'タナカ タロウ', 'たろちゃん', 'tanaka@example.com', '東京都', 28, '男性', '初学者', 0),
 ('鈴木 花子', 'スズキ ハナコ', 'はなちゃん', 'suzuki@example.com', '神奈川県', 32, '女性', '', 0),
 ('佐藤 健', 'サトウ ケン', 'けんけん', 'sato@example.com', '大阪府', 25, '男性', '途中で転職予定', 0),
 ('山本 美咲', 'ヤマモト ミサキ', 'みーちゃん', 'yamamoto@example.com', '愛知県', 30, '女性', '', 0),
 ('伊藤 海斗', 'イトウ カイト', 'カイくん', 'ito@example.com', '北海道', 22, '男性', '大学生', 0),
 ('渡辺 優子', 'ワタナベ ユウコ', 'ゆうちゃん', 'watanabe@example.com', '福岡県', 35, '女性', '復職希望', 0),
 ('中村 悠真', 'ナカムラ ユウマ', 'ゆうま', 'nakamura@example.com', '京都府', 27, '男性', '', 0),
 ('小林 美優', 'コバヤシ ミユ', 'みゆみゆ', 'kobayashi@example.com', '千葉県', 29, '女性', '', 0),
 ('加藤 亮', 'カトウ リョウ', 'りょうくん', 'kato@example.com', '埼玉県', 24, '男性', '', 0),
 ('高橋 真理子', 'タカハシ マリコ', 'まりまり', 'takahashi@example.com', '兵庫県', 31, '女性', 'Web業界経験者', 0);

INSERT INTO student_courses (student_id, course_name, course_start_at, course_end_at)
 VALUES
(1, 'Javaコース', '2025-04-01 10:00:00', '2026-04-01 10:00:00'),
(1, 'AWSコース', '2025-05-20 10:00:00', '2026-05-20 10:00:00'),
(2, 'WPコース', '2025-03-15 09:00:00', '2026-03-15 09:00:00'),
(3, '映像制作コース', '2025-05-01 10:00:00', '2026-05-01 10:00:00'),
(4, 'デザインコース', '2025-01-10 14:00:00', '2026-01-10 14:00:00'),
(5, 'フロントエンドコース', '2025-05-05 13:00:00', '2026-05-05 13:00:00'),
(6, 'Webマーケティングコース', '2025-04-10 09:30:00', '2026-04-10 09:30:00'),
(7, 'Javaコース', '2025-03-01 10:00:00', '2026-03-01 10:00:00'),
(7, 'フロントエンドコース', '2025-05-10 10:00:00', '2026-05-10 10:00:00'),
(8, 'AWSコース', '2025-04-15 09:00:00', '2026-04-15 09:00:00'),
(9, 'WPコース', '2025-03-20 11:00:00', '2026-03-20 11:00:00'),
(10, 'Webマーケティングコース', '2025-05-01 10:00:00', '2026-05-01 10:00:00'),
(10, 'デザインコース', '2025-04-01 09:00:00', '2026-04-01 09:00:00');

INSERT INTO application_statuses (student_course_id, status)
 VALUES
 (1, '仮申込'),
 (2, '本申込'),
 (3, '受講中'),
 (4, '受講終了'),
 (5, '仮申込'),
 (6, '受講中'),
 (7, '本申込'),
 (8, '受講終了'),
 (9, '本申込'),
 (10, '仮申込'),
 (11, '受講中'),
 (12, '本申込'),
 (13, '受講終了');