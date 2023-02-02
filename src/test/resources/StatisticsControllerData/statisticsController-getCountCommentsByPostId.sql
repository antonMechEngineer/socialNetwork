INSERT INTO person_settings (post_comment_notification,comment_comment_notification,friend_request_notification,message_notification,friend_birthday_notification,like_notification,post_notification)
VALUES
    ('true','true','true','true','true','true','true');

INSERT INTO persons (id, first_name, last_name, reg_date, birth_date, email, phone, password, about, person_settings_id)
VALUES (1, 'Gretchen', 'Contreras', '2022-09-29 21:49:07', '2000-06-08 10:54:06', 'rhoncus.nullam@yahoo.edu',
        '+7 (978) 311-43-59', 'ZvJ57ekY5Tc',
        'senectus et netus et malesuada fames ac turpis egestas. Fusce aliquet magna a neque. Nullam ut',1);

INSERT INTO posts (id,is_blocked, is_deleted, post_text, time, title, author_id)
VALUES (1, false, false, 'e', '2022-07-19 08:47:54.000000', 'title', 1);

INSERT INTO post_comments (comment_text, is_blocked, is_deleted, time, parent_id, author_id, post_id)
VALUES ('e', false, false, '2022-07-19 08:47:54.000000', 1, 1, 1);
