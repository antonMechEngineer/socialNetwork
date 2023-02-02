INSERT INTO person_settings (post_comment_notification,comment_comment_notification,friend_request_notification,message_notification,friend_birthday_notification,like_notification,post_notification)
VALUES
    ('true','true','true','true','true','true','true'),
    ('true','true','true','true','true','true','true');

INSERT INTO persons (id, first_name, last_name, reg_date, birth_date, email, phone, password, about, person_settings_id)
VALUES (1, 'Gretchen', 'Contreras', '2022-09-29 21:49:07', '2000-06-08 10:54:06', 'rhoncus.nullam@yahoo.edu',
        '+7 (978) 311-43-59', 'ZvJ57ekY5Tc',
        'senectus et netus et malesuada fames ac turpis egestas. Fusce aliquet magna a neque. Nullam ut',1),
       (2, 'Jescie', 'Logan', '2022-04-18 23:14:12', '1979-01-03 15:07:47', 'molestie@yahoo.edu', '+7 (965) 698-46-45',
        'UbQ85uuS8Lu',
        'molestie arcu. Sed eu nibh vulputate mauris sagittis placerat. Cras dictum ultricies ligula. Nullam enim. Sed nulla ante,',2);

INSERT INTO posts (id, time, author_id, title, post_text)
VALUES (1, '2022-07-02 11:04:07', 1, 'adipiscing fringilla,',
        'e'),
       (2, '2022-04-21 20:08:24', 1, 'sed dui. Fusce',
        'e'),
       (3, '2022-03-19 11:53:27', 1, 'arcu imperdiet ullamcorper.',
        'e'),
       (4, '2022-04-06 12:37:49', 1, 'eu,',
        'e'),
       (5, '2022-10-23 10:20:54', 2, 'morbi',
        'a');

INSERT INTO tags (id, tag)
VALUES (1, 'funny'),
       (2, 'summer');

INSERT INTO post2tag (id, post_id, tag_id)
VALUES (1, 1, 1),
       (2, 1, 2),
       (3, 2, 1),
       (4, 2, 2),
       (5, 3, 1),
       (6, 3, 2),
       (7, 4, 1),
       (8, 4, 2);