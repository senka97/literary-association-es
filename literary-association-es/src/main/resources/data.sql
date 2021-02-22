insert into role (name) values ('ROLE_READER');
insert into role (name) values ('ROLE_WRITER');
insert into role (name) value ('ROLE_BOARD_MEMBER');
insert into role (name) value ('ROLE_ADMIN');
insert into role (name) value ('ROLE_EDITOR');
insert into role (name) value ('ROLE_LECTURER');

insert into permission (name) values ('create_order');

insert into role_permissions (role_id, permission_id) values (1,1);

insert into merchant (merchant_name, merchant_email, activated, error_url, failed_url, success_url)
values ('Vulkan knjizare', 'sb-nsr1z4072854@business.example.com', true, 'https://localhost:3000/error', 'https://localhost:3000/failed', 'https://localhost:3000/success');

insert into merchant (merchant_name, merchant_email, activated, error_url, failed_url, success_url)
values ('Laguna', 'laguna@gmail.com', true, 'https://localhost:3000/error', 'https://localhost:3000/failed', 'https://localhost:3000/success');

-- sifra: reader123, username: reader123
insert into user (type, email, password, username, first_name, last_name, city, country, verified, enabled, beta_reader, penalty_points, lat, lon)
value ('Reader', 'reader123@gmail.com', '$2a$10$MgS2lefNxeyaDHxP/inYO.D0G5bkS8OX5RbAj7MJgghT16n6dQwIe', 'reader123', 'Senka', 'Reader', 'Novi Sad', 'Srbija', true, true, true, 3, 45.267136, 19.833549); --novi sad

-- sifra boardmember
insert into user (type, email, password, username, first_name, last_name, city, country, verified, enabled, lat, lon)
value ('BoardMember', 'boardmember1@gmail.com', '$2a$10$U0MOxpLw1mEOI/sJbPQfxOmCrnSQlhSHhT5oWW.EVFvL5ahoEoXFu', 'boardMember1', 'Pera', 'BoardMember', 'NS', 'Srbija', true, true, 45.267136, 19.833549);

insert into user (type, email, password, username, first_name, last_name, city, country, verified, enabled, lat, lon)
value ('BoardMember', 'boardmember2@gmail.com', '$2a$10$U0MOxpLw1mEOI/sJbPQfxOmCrnSQlhSHhT5oWW.EVFvL5ahoEoXFu', 'boardMember2', 'Mika', 'BoardMember', 'NS', 'Srbija', true, true, 45.267136, 19.833549);

-- sifra: reader123, username: admin123
insert into user (type, email, password, username, first_name, last_name, city, country, verified, enabled, lat, lon)
value ('Admin', 'admin123@gmail.com', '$2a$10$MgS2lefNxeyaDHxP/inYO.D0G5bkS8OX5RbAj7MJgghT16n6dQwIe', 'admin123', 'Senka', 'Admin', 'NS', 'Srbija', true, true, 45.267136, 19.833549);

-- sifra: reader123, username: editor123
insert into user (type, email, password, username, first_name, last_name, city, country, verified, enabled, lat, lon)
value ('Editor', 'editor123@maildrop.cc', '$2a$10$MgS2lefNxeyaDHxP/inYO.D0G5bkS8OX5RbAj7MJgghT16n6dQwIe', 'editor123', 'Senka', 'Editor', 'NS', 'Srbija', true, true, 45.267136, 19.833549);

-- sifra: reader123, username: writer123
insert into user (type, email, password, username, first_name, last_name, city, country, verified, enabled, lat, lon)
    value ('Writer', 'writer123@maildrop.cc', '$2a$10$MgS2lefNxeyaDHxP/inYO.D0G5bkS8OX5RbAj7MJgghT16n6dQwIe', 'writer123', 'Senka', 'Writer', 'NS', 'Srbija', true, true, 45.267136, 19.833549);

insert into user_roles (user_id, role_id) values (1,1); -- reader
insert into user_roles (user_id, role_id) values (2,3); -- board member
insert into user_roles (user_id, role_id) values (3,3); -- board member
insert into user_roles (user_id, role_id) values (4,4); -- admin
insert into user_roles (user_id, role_id) values (5,5); -- editor
insert into user_roles (user_id, role_id) values (6,2); -- writer

insert into genre (name, description) value ('Thriller', 'Thrillers are characterized by fast pacing, frequent action, and resourceful heroes who must thwart the plans of more-powerful and better-equipped villains. Literary devices such as suspense, red herrings and cliffhangers are used extensively.');
insert into genre (name, description) value ('Romance', 'Romance fiction is smart, fresh and diverse. Two basic elements comprise every romance novel: a central love story and an emotionally satisfying and optimistic ending.');
insert into genre (name, description) value ('Drama', 'Drama is a mode of fictional representation through dialogue and performance. It is one of the literary genres, which is an imitation of some action. It contains conflict of characters, particularly the ones who perform in front of audience on the stage.');
insert into genre (name, description) value ('Crime', 'The crime genre includes the broad selection of books on criminals and the court system, but the most common focus is investigations and sleuthing. Mystery novels are usually placed into this category, although there is a separate division for "crime".');
insert into genre (name, description) value ('Biography', 'A biography, or simply bio, is a detailed description of a persons life. It involves more than just the basic facts like education, work, relationships, and death; it portrays a persons experience of these life events.');
insert into genre (name, description) value ('Classic', 'A classic is a novel makes a contribution to literature. Classics come from all cultures and all years, and can reflect a time period, a societal standard or may offer commentary on a subject.');
insert into genre (name, description) value ('Psychology', 'Popular psychology (sometimes shortened as pop psychology or pop psych) is the concepts and theories about human mental life and behavior that are purportedly based on psychology and that find credence among and pass muster with the populace.');

insert into reader_genre (reader_id,genre_id) value (1, 1);
insert into reader_genre (reader_id,genre_id) value (1, 2);

insert into beta_reader_genre (beta_reader_id,genre_id) value (1, 1);
insert into beta_reader_genre (beta_reader_id,genre_id) value (1, 2);

-- sifra: reader2, username: reader1234
insert into user (type, email, password, username, first_name, last_name, city, country, verified, enabled, beta_reader, penalty_points, lat, lon)
value ('Reader', 'reader2@gmail.com', '$2a$10$MgS2lefNxeyaDHxP/inYO.D0G5bkS8OX5RbAj7MJgghT16n6dQwIe', 'reader2', 'Bojana', 'Kliska', 'Berlin', 'Nemacka', true, true, true, 4, 52.521992, 13.413244);

-- sifra: reader3, username: reader12345
insert into user (type, email, password, username, first_name, last_name, city, country, verified, enabled, beta_reader, penalty_points, lat, lon)
value ('Reader', 'reader3@gmail.com', '$2a$10$MgS2lefNxeyaDHxP/inYO.D0G5bkS8OX5RbAj7MJgghT16n6dQwIe', 'reader3', 'Marina', 'Bartulov', 'Berlin', 'Nemacka', true, true, true, 0, 52.521992, 13.413244);

insert into beta_reader_genre (beta_reader_id,genre_id) value (7, 3);
insert into beta_reader_genre (beta_reader_id,genre_id) value (8, 3);

insert into user_roles (user_id, role_id) values (7,1);
insert into user_roles (user_id, role_id) values (8,1);

insert into user (type, email, password, username, first_name, last_name, city, country, verified, enabled, lat, lon)
value ('Lecturer', 'lecturer1@gmail.com', '$2a$10$MgS2lefNxeyaDHxP/inYO.D0G5bkS8OX5RbAj7MJgghT16n6dQwIe', 'lecturer1', 'Nikola', 'Lecturer', 'NS', 'Srbija', true, true, 45.267136, 19.833549);

insert into user (type, email, password, username, first_name, last_name, city, country, verified, enabled, lat, lon)
value ('Lecturer', 'lecturer2@gmail.com', '$2a$10$MgS2lefNxeyaDHxP/inYO.D0G5bkS8OX5RbAj7MJgghT16n6dQwIe', 'lecturer2', 'Milan', 'Lecturer', 'NS', 'Srbija', true, true, 45.267136, 19.833549);

insert into user (type, email, password, username, first_name, last_name, city, country, verified, enabled, lat, lon)
value ('Lecturer', 'lecturer3@gmail.com', '$2a$10$MgS2lefNxeyaDHxP/inYO.D0G5bkS8OX5RbAj7MJgghT16n6dQwIe', 'lecturer3', 'Jovan', 'Lecturer', 'NS', 'Srbija', true, true, 45.267136, 19.833549);

insert into user_roles (user_id, role_id) values (9,6);
insert into user_roles (user_id, role_id) values (10,6);
insert into user_roles (user_id, role_id) values (11,6);

insert into book (file_name, plagiarism, isbn, num_of_pages, open_access, pdf, price, publishers_address, synopsis, title, year, editor_id, genre_id, lecturer_id, publisher_id, writer_id)
values ('Bitcoin.pdf' ,false, '2222', 300, true, 'uploaded-files/Bitcoin.pdf', 13.0, 'Novi Sad, Jevrejska 10', 'Bitcoin', 'Bitcoin', '2020', 5, 1, 11, 1, 6);

-- sifra: reader123, username: writer2 id: 12
insert into user (type, email, password, username, first_name, last_name, city, country, verified, enabled, lat, lon)
values ('Writer', 'writer2@maildrop.cc', '$2a$10$MgS2lefNxeyaDHxP/inYO.D0G5bkS8OX5RbAj7MJgghT16n6dQwIe', 'writer2', 'Filip', 'Writer', 'Berlin', 'Nemacka', true, true, 52.521992, 13.413244); -- Berlin

insert into book (file_name, plagiarism, isbn, num_of_pages, open_access, pdf, price, publishers_address, synopsis, title, year, editor_id, genre_id, lecturer_id, publisher_id, writer_id)
values ('Bitcoin prešao 44.pdf', false, '2223', 300, true, 'uploaded-files/Bitcoin prešao 44.pdf', 12.0, 'Novi Sad, Jevrejska 10', 'Bitcoin', 'Bitcoin prešao 44.000$ nakon što je Tesla kupila kriptovalutu za 1,5 milijardi $', '2020', 5, 2, 10, 1, 12);

insert into book (file_name, plagiarism, isbn, num_of_pages, open_access, pdf, price, publishers_address, synopsis, title, year, editor_id, genre_id, lecturer_id, publisher_id, writer_id)
values ('Frilenseri i mediji.pdf', false, '2224', 300, true, 'uploaded-files/Frilenseri i mediji.pdf', 15.0, 'Novi Sad, Jevrejska 10', 'Frilenseri i mediji', 'Frilenseri i mediji — ponekad površno i ostrašćeno, uglavnom neutralno', '2020', 5, 1, 9, 1, 6);

-- sifra: reader123, username: editor1
insert into user (type, email, password, username, first_name, last_name, city, country, verified, enabled, lat, lon)
value ('Editor', 'prvieditor@maildrop.cc', '$2a$10$MgS2lefNxeyaDHxP/inYO.D0G5bkS8OX5RbAj7MJgghT16n6dQwIe', 'editor1', 'Prvi', 'Prvic', 'NS', 'Srbija', true, true, 45.267136, 19.833549);

-- sifra: reader123, username: editor2
insert into user (type, email, password, username, first_name, last_name, city, country, verified, enabled, lat, lon)
value ('Editor', 'drugieditor@maildrop.cc', '$2a$10$MgS2lefNxeyaDHxP/inYO.D0G5bkS8OX5RbAj7MJgghT16n6dQwIe', 'editor2', 'Drugi', 'Drugic', 'NS', 'Srbija', true, true, 45.267136, 19.833549);

insert into user_roles (user_id, role_id) values (12,2);
insert into user_roles (user_id, role_id) values (13,5);
insert into user_roles (user_id, role_id) values (14,5);

-- sifra: reader123, username: editor3
insert into user (type, email, password, username, first_name, last_name, city, country, verified, enabled, lat, lon)
value ('Editor', 'trecieditor@maildrop.cc', '$2a$10$MgS2lefNxeyaDHxP/inYO.D0G5bkS8OX5RbAj7MJgghT16n6dQwIe', 'editor3', 'Treci', 'Trecic', 'NS', 'Srbija', true, true, 45.267136, 19.833549);

-- sifra: reader123, username: editor4
insert into user (type, email, password, username, first_name, last_name, city, country, verified, enabled, lat, lon)
value ('Editor', 'cetvrtieditor@maildrop.cc', '$2a$10$MgS2lefNxeyaDHxP/inYO.D0G5bkS8OX5RbAj7MJgghT16n6dQwIe', 'editor4', 'Cetvrti', 'Cetvrtic', 'NS', 'Srbija', true, true, 45.267136, 19.833549);

insert into user_roles (user_id, role_id) values (15,5);
insert into user_roles (user_id, role_id) values (16,5);

insert into book (file_name, plagiarism, isbn, num_of_pages, open_access, pdf, price, publishers_address, synopsis, title, year, editor_id, genre_id, lecturer_id, publisher_id, writer_id)
value ('korona virus.pdf', false, '1234567891234', 100, true, 'uploaded-files/korona virus.pdf',10.59, 'Bulevar oslobodjenja 34', 'Korona virus','Корона вирус','2015',5,7,9,1,6);

insert into book (file_name, plagiarism, isbn, num_of_pages, open_access, pdf, price, publishers_address, synopsis, title, year, editor_id, genre_id, lecturer_id, publisher_id, writer_id)
value ('Loš dizajn korisničkog interfejsa koštao banku 500 miliona.pdf', false, '1234567333333', 150, false, 'uploaded-files/Loš dizajn korisničkog interfejsa koštao banku 500 miliona.pdf',17.50, 'Bulevar Mihajla Pupina 23', 'Korisnički interfejs ','Loš dizajn korisničkog interfejsa koštao banku 500 miliona $','2015',5,1,9,2,6);
