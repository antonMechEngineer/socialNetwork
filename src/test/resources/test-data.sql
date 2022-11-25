INSERT INTO countries (title)
VALUES
    ('China'),
    ('Nigeria'),
    ('Russian Federation'),
    ('Pakistan'),
    ('Turkey'),
    ('Canada'),
    ('Belgium'),
    ('New Zealand'),
    ('South Korea'),
    ('Norway');


INSERT INTO cities (title,country_id)
VALUES
    ('Puqueldón',4),
    ('Warszawa',3),
    ('Choa Chu Kang',8),
    ('Sarreguemines',6),
    ('Queenstown',6),
    ('Vienna',2),
    ('Fairbanks',2),
    ('Ivano-Frankivsk',5),
    ('Belfast',2),
    ('Anamur',7),
    ('Chandigarh',6),
    ('Drohobych',4),
    ('Phan Rang–Tháp Chàm',9),
    ('Nässjö',2),
    ('Kaneohe',9),
    ('Châtellerault',2),
    ('Leticia',9),
    ('Hubei',7),
    ('Sunset Point',5),
    ('Kozan',8),
    ('Sachs Harbour',2),
    ('Warrington',4),
    ('Heilongjiang',5),
    ('Mildura',5),
    ('Pescantina',9),
    ('Chambave',6),
    ('Jecheon',3),
    ('Chandigarh',8),
    ('Chilpancingo',5),
    ('Labico',4),
    ('Woodstock',1),
    ('Montenegro',9),
    ('Puerto Princesa',5),
    ('Meugliano',6),
    ('Pontarlier',3),
    ('Guápiles',4),
    ('Algeciras',7),
    ('Jiangxi',9),
    ('Huacho',7),
    ('Bergen',3);


INSERT INTO persons (first_name,last_name,reg_date,birth_date,email,phone,password,city_id,about)
VALUES
    ('Gretchen','Contreras','2022-09-29 21:49:07','2000-06-08 10:54:06','rhoncus.nullam@yahoo.edu','+7 (978) 311-43-59','ZvJ57ekY5Tc',32,'senectus et netus et malesuada fames ac turpis egestas. Fusce aliquet magna a neque. Nullam ut'),
    ('Jescie','Logan','2022-04-18 23:14:12','1979-01-03 15:07:47','molestie@yahoo.edu','+7 (965) 698-46-45','UbQ85uuS8Lu',8,'molestie arcu. Sed eu nibh vulputate mauris sagittis placerat. Cras dictum ultricies ligula. Nullam enim. Sed nulla ante,');


INSERT INTO posts (time,author_id,title,post_text)
VALUES
    ('2022-07-02 11:04:07',1,'adipiscing fringilla,','augue eu tellus. Phasellus elit pede, malesuada vel, venenatis vel, faucibus id, libero. Donec consectetuer mauris id sapien. Cras dolor dolor, tempus non, lacinia at, iaculis quis, pede. Praesent eu dui. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Aenean eget magna. Suspendisse tristique neque venenatis lacus. Etiam bibendum fermentum metus. Aenean sed pede nec ante blandit viverra. Donec tempus, lorem fringilla ornare placerat, orci lacus vestibulum lorem,'),
    ('2022-04-21 20:08:24',1,'sed dui. Fusce','Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Fusce aliquet magna a neque. Nullam ut nisi a odio semper cursus. Integer mollis. Integer tincidunt aliquam arcu. Aliquam ultrices iaculis odio. Nam interdum enim non nisi. Aenean eget metus. In nec orci. Donec nibh. Quisque nonummy ipsum non arcu. Vivamus sit amet'),
    ('2022-03-19 11:53:27',1,'arcu imperdiet ullamcorper.','eget, dictum placerat, augue. Sed molestie. Sed id risus quis diam luctus lobortis. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos hymenaeos. Mauris ut quam vel sapien imperdiet ornare. In faucibus. Morbi vehicula. Pellentesque tincidunt tempus risus. Donec egestas. Duis ac arcu. Nunc mauris. Morbi non sapien molestie orci tincidunt adipiscing. Mauris molestie pharetra nibh. Aliquam ornare, libero at auctor ullamcorper, nisl arcu iaculis enim, sit amet ornare lectus justo eu arcu. Morbi sit amet massa. Quisque porttitor eros nec tellus. Nunc lectus pede, ultrices a, auctor non,'),
    ('2022-04-06 12:37:49',2,'eu,','non enim commodo hendrerit. Donec porttitor tellus non magna. Nam ligula elit, pretium et, rutrum non, hendrerit id, ante. Nunc mauris sapien, cursus in, hendrerit consectetuer, cursus et, magna. Praesent interdum ligula eu enim. Etiam imperdiet dictum magna. Ut tincidunt orci'),
    ('2022-10-23 10:20:54',1,'morbi','purus sapien, gravida non, sollicitudin a, malesuada id, erat. Etiam vestibulum massa rutrum magna. Cras convallis convallis dolor. Quisque tincidunt pede ac urna. Ut tincidunt vehicula risus. Nulla eget metus eu erat semper rutrum. Fusce dolor quam, elementum at, egestas a, scelerisque sed, sapien. Nunc pulvinar arcu et pede. Nunc sed orci lobortis augue scelerisque mollis. Phasellus libero mauris, aliquam eu, accumsan sed, facilisis vitae, orci. Phasellus dapibus quam quis diam. Pellentesque habitant morbi tristique senectus');


INSERT INTO post_comments (time,author_id,post_id,comment_text)
VALUES
    ('2022-09-28 19:46:50',2,2,'metus urna convallis erat, eget tincidunt dui augue eu'),
    ('2022-03-04 22:14:58',1,1,'lobortis tellus justo sit amet nulla. Donec non justo. Proin non massa non ante bibendum ullamcorper. Duis cursus, diam at pretium aliquet, metus urna convallis erat, eget tincidunt dui augue eu tellus. Phasellus elit pede, malesuada vel, venenatis vel,'),
    ('2022-10-20 16:41:33',1,3,'Suspendisse tristique neque venenatis lacus. Etiam bibendum fermentum metus. Aenean sed pede nec ante blandit viverra. Donec tempus, lorem fringilla ornare placerat, orci lacus vestibulum lorem, sit amet ultricies sem magna nec quam. Curabitur vel'),
    ('2022-04-17 12:39:04',1,2,'Vestibulum accumsan neque et nunc. Quisque ornare tortor at risus. Nunc ac sem ut dolor dapibus gravida. Aliquam tincidunt, nunc ac mattis ornare, lectus ante dictum'),
    ('2022-11-20 16:08:31',2,4,'ullamcorper. Duis cursus, diam at pretium aliquet, metus urna convallis erat, eget tincidunt dui augue eu tellus. Phasellus elit pede, malesuada vel, venenatis vel, faucibus id, libero. Donec consectetuer mauris'),
    ('2022-11-27 07:44:53',2,4,'tincidunt nibh. Phasellus nulla. Integer vulputate, risus a ultricies adipiscing, enim mi tempor lorem, eget mollis lectus pede et risus.'),
    ('2022-03-09 08:50:17',1,4,'congue. In scelerisque scelerisque dui. Suspendisse ac metus vitae velit egestas lacinia. Sed congue, elit sed consequat auctor, nunc nulla vulputate dui, nec tempus mauris erat eget ipsum. Suspendisse sagittis. Nullam vitae diam. Proin dolor. Nulla semper tellus id'),
    ('2022-03-17 14:55:48',2,3,'magnis dis parturient montes, nascetur ridiculus mus. Aenean eget magna. Suspendisse tristique neque venenatis lacus. Etiam bibendum fermentum metus. Aenean sed pede nec ante blandit viverra. Donec tempus, lorem fringilla ornare placerat, orci lacus vestibulum lorem,'),
    ('2022-07-03 16:47:51',1,5,'nulla vulputate dui, nec tempus mauris erat eget ipsum. Suspendisse sagittis. Nullam vitae diam.'),
    ('2022-03-21 08:00:59',2,5,'id nunc interdum feugiat. Sed nec metus facilisis lorem tristique aliquet. Phasellus fermentum convallis');


INSERT INTO tags (tag) VALUES  ('impedit');
INSERT INTO tags (tag) VALUES  ('voluptatum');
INSERT INTO tags (tag) VALUES  ('pariatur');
INSERT INTO tags (tag) VALUES  ('illo');
INSERT INTO tags (tag) VALUES  ('quidem');
INSERT INTO tags (tag) VALUES  ('recusandae');
INSERT INTO tags (tag) VALUES  ('quod');


INSERT INTO post2tag (post_id,tag_id)
VALUES
    (1,1),
    (1,4),
    (1,7),
    (2,3),
    (2,5),
    (3,3),
    (4,4),
    (4,6),
    (5,3),
    (5,5);
