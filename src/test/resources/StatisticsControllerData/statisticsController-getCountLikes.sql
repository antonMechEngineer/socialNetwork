INSERT INTO persons (id, first_name, last_name, reg_date, birth_date, email, phone, password, about)
VALUES (1, 'Gretchen', 'Contreras', '2022-09-29 21:49:07', '2000-06-08 10:54:06', 'rhoncus.nullam@yahoo.edu',
        '+7 (978) 311-43-59', 'ZvJ57ekY5Tc',
        'senectus et netus et malesuada fames ac turpis egestas. Fusce aliquet magna a neque. Nullam ut'),
       (2, 'Jescie', 'Logan', '2022-04-18 23:14:12', '1979-01-03 15:07:47', 'molestie@yahoo.edu', '+7 (965) 698-46-45',
        'UbQ85uuS8Lu',
        'molestie arcu. Sed eu nibh vulputate mauris sagittis placerat. Cras dictum ultricies ligula. Nullam enim. Sed nulla ante,'),
       (3, 'Gretchn', 'Contrras', '2022-09-29 21:49:07', '2000-06-08 10:54:06', 'rhoncu.nullam@yahoo.edu',
        '+7 (978) 311-43-59', 'ZvJ57ekY5Tc',
        'senectus et netus et malesuada fames ac turpis egestas. Fusce aliquet magna a neque. Nullam ut'),
       (4, 'Gretc', 'Contr', '2022-09-29 21:49:07', '2000-06-08 10:54:06', 'rhon.nullam@yahoo.edu', '+7 (978) 311-43-59',
        'ZvJ57ekY5Tc',
        'senectus et netus et malesuada fames ac turpis egestas. Fusce aliquet magna a neque. Nullam ut'),
       (5, 'Gre', 'Con', '2022-09-29 21:49:07', '2000-06-08 10:54:06', 'rh.nullam@yahoo.edu', '+7 (978) 311-43-59',
        'ZvJ57ekY5Tc',
        'senectus et netus et malesuada fames ac turpis egestas. Fusce aliquet magna a neque. Nullam ut'),
       (6, 'Gr', 'Co', '2022-09-29 21:49:07', '2000-06-08 10:54:06', 'r.nullam@yahoo.edu', '+7 (978) 311-43-59',
        'ZvJ57ekY5Tc',
        'senectus et netus et malesuada fames ac turpis egestas. Fusce aliquet magna a neque. Nullam ut');

INSERT INTO likes(type, entity_id, time, person_id)
VALUES ('COMMENT', 1, '2022-12-27 22:30:19.000000', 1),
       ('COMMENT', 1, '2022-12-27 22:30:19.000000', 2),
       ('COMMENT', 1, '2022-12-27 22:30:19.000000', 3),
       ('COMMENT', 1, '2022-12-27 22:30:19.000000', 4),
       ('COMMENT', 1, '2022-12-27 22:30:19.000000', 5),
       ('COMMENT', 1, '2022-12-27 22:30:19.000000', 6);