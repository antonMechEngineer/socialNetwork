INSERT INTO person_settings (post_comment_notification,comment_comment_notification,friend_request_notification,message_notification,friend_birthday_notification,like_notification,post_notification)
VALUES
    ('true','true','true','true','true','true','true'),
    ('true','true','true','true','true','true','true'),
    ('true','true','true','true','true','true','true'),
    ('true','true','true','true','true','true','true'),
    ('true','true','true','true','true','true','true'),
    ('true','true','true','true','true','true','true'),
    ('true','true','true','true','true','true','true'),
    ('true','true','true','true','true','true','true');

INSERT INTO persons (id, first_name, last_name, reg_date, birth_date, email, phone, password, about, person_settings_id)
VALUES (1, 'Gretchen', 'Contreras', '2022-09-29 21:49:07', '2000-06-08 10:54:06', 'rhoncus.nullam@yahoo.edu',
        '+7 (978) 311-43-59', 'ZvJ57ekY5Tc', 's',1),
       (2, 'Jescie', 'Logan', '2022-04-18 23:14:12', '1979-01-03 15:07:47', 'molestie@yahoo.edu', '+7 (965) 698-46-45',
        'UbQ85uuS8Lu', 'm',2),
       (3, 'Gretchn', 'Contrras', '2022-09-29 21:49:07', '2000-06-08 10:54:06', 'rhoncu.nullam@yahoo.edu',
        '+7 (978) 311-43-59', 'ZvJ57ekY5Tc', 's',3),
       (4, 'Gretc', 'Contr', '2022-09-29 21:49:07', '2000-06-08 10:54:06', 'rhon.nullam@yahoo.edu', '+7 (978) 311-43-59',
        'ZvJ57ekY5Tc', 's',4),
       (5, 'Gre', 'Con', '2022-09-29 21:49:07', '2000-06-08 10:54:06', 'rh.nullam@yahoo.edu', '+7 (978) 311-43-59',
        'ZvJ57ekY5Tc', 's',5),
       (6, 'Gr', 'Co', '2022-09-29 21:49:07', '2000-06-08 10:54:06', 'r.nullam@yahoo.edu', '+7 (978) 311-43-59',
        'ZvJ57ekY5Tc', 's',6),
       (7, 'G', 'C', '2022-09-29 21:49:07', '2000-06-08 10:54:06', 're.nullam@yahoo.edu', '+7 (978) 311-43-59',
        'ZvJ57ekY5Tc', 's',7),
       (8, 'Stas', 'M', '2022-09-29 21:49:07', '2000-06-08 10:54:06', 'req.nullam@yahoo.edu', '+7 (978) 311-43-59',
        'ZvJ57ekY5Tc', 'F',8);

INSERT INTO dialogs (first_person_id, second_person_id, last_active_time)
VALUES (1, 2, '2022-12-20 21:36:23.833872'),
       (3, 4, '2022-12-20 21:36:23.833872'),
       (5, 6, '2022-12-20 21:36:23.833872'),
       (7, 8, '2022-12-20 21:36:23.833872');