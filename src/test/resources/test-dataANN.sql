INSERT INTO person_settings(id, comment_comment_notification, friend_birthday_notification, friend_request_notification, like_notification, message_notification, post_comment_notification, post_notification)
VALUES (1, true, true, true, true, true, true, true),
       (2, true, true, true, true, true, true, true),
       (3, true, true, true, true, true, true, true),
       (4, true, true, true, true, true, true, true),
       (5, true, true, true, true, true, true, true);

INSERT INTO persons (id,first_name,last_name,reg_date,birth_date,email,phone,password,about,city,country,person_settings_id)
VALUES
    (1, 'Gretchen','Contreras','2022-09-29 21:49:07','2000-06-08 10:54:06','rhoncus.nullam@yahoo.edu','+7 (978) 311-43-59','ZvJ57ekY5Tc','senectus et netus et malesuada fames ac turpis egestas. Fusce aliquet magna a neque. Nullam ut','Miami','USA',1),
    (2, 'Jescie','Logan','2022-04-18 23:14:12','1979-01-03 15:07:47','molestie@yahoo.edu','+7 (965) 698-46-45','UbQ85uuS8Lu','molestie arcu. Sed eu nibh vulputate mauris sagittis placerat. Cras dictum ultricies ligula. Nullam enim. Sed nulla ante,','Miami','USA',2),
    (3, 'Gret','Cont','2022-09-29 21:49:07','2000-06-08 10:54:06','rh.nullam@yahoo.edu','+7 (978) 311-43-59','ZvJ57ekY5Tc','senectus et netus et malesuada fames ac turpis egestas. Fusce aliquet magna a neque. Nullam ut','Miami','USA',3),
    (4, 'Jes','Log','2022-04-18 23:14:12','1979-01-03 15:07:47','mol@yahoo.edu','+7 (965) 698-46-45','UbQ85uuS8Lu','molestie arcu. Sed eu nibh vulputate mauris sagittis placerat. Cras dictum ultricies ligula. Nullam enim. Sed nulla ante,','Miami', 'USA',4),
    (5, 'J','L','2022-04-18 23:14:12','1979-01-03 15:07:47','m@yahoo.edu','+7 (965) 698-46-45','UbQ85uuS8Lu','molestie arcu. Sed eu nibh vulputate mauris sagittis placerat. Cras dictum ultricies ligula. Nullam enim. Sed nulla ante,','Miami','USA',5);


INSERT INTO friendships (sent_time, src_person_id, dst_person_id, status_name)
VALUES
    ('2022-09-29 21:49:07', 1, 2, 'FRIEND'),
    ('2022-09-29 21:49:07', 2, 1, 'FRIEND'),

    ('2022-09-29 21:49:07', 1, 3, 'RECEIVED_REQUEST'),
    ('2022-09-29 21:49:07', 3, 1, 'REQUEST'),

    ('2022-09-29 21:49:07', 1, 4, 'REQUEST'),
    ('2022-09-29 21:49:07', 4, 1, 'RECEIVED_REQUEST');


