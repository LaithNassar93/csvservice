insert into authentication (id, password, username)
values (1, '$2a$12$d2.YHk6CyyoogPUerIYIaeE2DoZ6eOBJcbG6B7FO4smGf7WUj/G0K','laith');
insert into student (id, date_of_birth, first_name, gpa, last_name, authentication_id)
values (1, STR_TO_DATE('10-17-2021', '%m-%d-%Y'),'Laith',3.8,'Nassar',1);