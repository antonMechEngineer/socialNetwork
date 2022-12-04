INSERT INTO persons (id, first_name,last_name,reg_date,birth_date,email,phone,password,about)
VALUES
    (1, 'Gretchen','Contreras','2022-09-29 21:49:07','2000-06-08 10:54:06','rhoncus.nullam@yahoo.edu','+7 (978) 311-43-59','ZvJ57ekY5Tc','senectus et netus et malesuada fames ac turpis egestas. Fusce aliquet magna a neque. Nullam ut'),
    (2, 'Jescie','Logan','2022-04-18 23:14:12','1979-01-03 15:07:47','molestie@yahoo.edu','+7 (965) 698-46-45','UbQ85uuS8Lu','molestie arcu. Sed eu nibh vulputate mauris sagittis placerat. Cras dictum ultricies ligula. Nullam enim. Sed nulla ante,'),
    (3, 'Gret','Cont','2022-09-29 21:49:07','2000-06-08 10:54:06','rh.nullam@yahoo.edu','+7 (978) 311-43-59','ZvJ57ekY5Tc','senectus et netus et malesuada fames ac turpis egestas. Fusce aliquet magna a neque. Nullam ut'),
    (4, 'Jes','Log','2022-04-18 23:14:12','1979-01-03 15:07:47','mol@yahoo.edu','+7 (965) 698-46-45','UbQ85uuS8Lu','molestie arcu. Sed eu nibh vulputate mauris sagittis placerat. Cras dictum ultricies ligula. Nullam enim. Sed nulla ante,'),
    (5, 'J','L','2022-04-18 23:14:12','1979-01-03 15:07:47','m@yahoo.edu','+7 (965) 698-46-45','UbQ85uuS8Lu','molestie arcu. Sed eu nibh vulputate mauris sagittis placerat. Cras dictum ultricies ligula. Nullam enim. Sed nulla ante,');

INSERT INTO friendship_statuses(time, name, code)
VALUES
    ('2022-09-29 21:49:07', 'FRIEND', 'FRIEND'),
    ('2022-09-29 21:49:07', 'FRIEND', 'FRIEND'),

    ('2022-09-29 21:49:07', 'RECEIVED_REQUEST', 'RECEIVED_REQUEST'),
    ('2022-09-29 21:49:07', 'REQUEST', 'REQUEST'),

    ('2022-09-29 21:49:07', 'REQUEST', 'REQUEST'),
    ('2022-09-29 21:49:07', 'RECEIVED_REQUEST', 'RECEIVED_REQUEST');

INSERT INTO friendships (sent_time, src_person_id, dst_person_id, status_id)
VALUES
    ('2022-09-29 21:49:07', 1, 2, 1),
    ('2022-09-29 21:49:07', 2, 1, 2),

    ('2022-09-29 21:49:07', 1, 3, 3),
    ('2022-09-29 21:49:07', 3, 1, 4),

    ('2022-09-29 21:49:07', 1, 4, 5),
    ('2022-09-29 21:49:07', 4, 1, 6);


