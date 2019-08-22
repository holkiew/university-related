INSERT INTO roles(name) VALUES('ROLE_USER');
INSERT INTO roles(name) VALUES('ROLE_ADMIN');
INSERT INTO roles(name) VALUES('ROLE_PARTNER_USER');
INSERT INTO roles(name) VALUES('ROLE_PARTNER_ADMIN');

INSERT INTO auth_user(created_at, updated_at, email, username, password) VALUES('2018-10-23 01:33:42.368718', '2018-10-23 01:33:42.368718', 'test@test.test', 'sa', '$2a$10$/OTaxGsSRX7PMrH1eRByNuUsTfYXftoifjqpQkPYIerz/5U/23Ip6');
INSERT INTO auth_user(created_at, updated_at, email, username, password) VALUES('2018-10-23 01:33:42.368718', '2018-10-23 01:33:42.368718', 'test0@test.test', 'ecmaAdmin', '$2a$10$/OTaxGsSRX7PMrH1eRByNuUsTfYXftoifjqpQkPYIerz/5U/23Ip6');
INSERT INTO auth_user(created_at, updated_at, email, username, password) VALUES('2018-10-23 01:33:42.368718', '2018-10-23 01:33:42.368718', 'test1@test.test', 'ecmaUser', '$2a$10$/OTaxGsSRX7PMrH1eRByNuUsTfYXftoifjqpQkPYIerz/5U/23Ip6');
INSERT INTO auth_user(created_at, updated_at, email, username, password) VALUES('2018-10-23 01:33:42.368718', '2018-10-23 01:33:42.368718', 'test2@test.test', 'partnerUser', '$2a$10$/OTaxGsSRX7PMrH1eRByNuUsTfYXftoifjqpQkPYIerz/5U/23Ip6');
INSERT INTO auth_user(created_at, updated_at, email, username, password) VALUES('2018-10-23 01:33:42.368718', '2018-10-23 01:33:42.368718', 'test3@test.test', 'partnerAdmin', '$2a$10$/OTaxGsSRX7PMrH1eRByNuUsTfYXftoifjqpQkPYIerz/5U/23Ip6');

INSERT INTO user_roles(user_id, role_id) VALUES (1, 1);
INSERT INTO user_roles(user_id, role_id) VALUES (2, 1);
INSERT INTO user_roles(user_id, role_id) VALUES (2, 2);
INSERT INTO user_roles(user_id, role_id) VALUES (3, 1);
INSERT INTO user_roles(user_id, role_id) VALUES (4, 3);
INSERT INTO user_roles(user_id, role_id) VALUES (5, 3);
INSERT INTO user_roles(user_id, role_id) VALUES (5, 4);

INSERT INTO companies(name, address) VALUES ('Company 1', 'Prague, Václavské náměstí 1');
INSERT INTO companies(name, address) VALUES ('Company 2', 'Prague, Václavské náměstí 1');

INSERT INTO user(firstname, surname, phone, IS_APPROVAL, AUTH_USER_ID, COMPANY_ID) VALUES ('sa', 'sa', '123456789', true, 1, null);
INSERT INTO user(firstname, surname, phone, IS_APPROVAL, AUTH_USER_ID, COMPANY_ID) VALUES ('ecma', 'admin', '123456789', true, 2, null);
INSERT INTO user(firstname, surname, phone, IS_APPROVAL, AUTH_USER_ID, COMPANY_ID) VALUES ('ecma', 'user', '123456789', true, 3, null);
INSERT INTO user(firstname, surname, phone, IS_APPROVAL, AUTH_USER_ID, COMPANY_ID) VALUES ('partner', 'user', '123456789', false, 4, 1);
INSERT INTO user(firstname, surname, phone, IS_APPROVAL, AUTH_USER_ID, COMPANY_ID) VALUES ('partner', 'admin', '123456789', false, 5, 1);

INSERT INTO event_proposals(approved, budget, description, goals, needed_materials, title, work_plan, author_id, involved_company_id, assigned_reviewer_id) VALUES (false, 50000, 'Event proposal 1', 'introducing company 1', 'nothing', 'Company 1 event', 'dinner', 3, 1, 2);
INSERT INTO event_proposals(approved, budget, description, goals, needed_materials, title, work_plan, author_id, involved_company_id, assigned_reviewer_id) VALUES (false, 1000000, 'Event proposal 1', 'next company 1 event', 'nothing', 'Company 1 event', 'lunch', 4, 2, 3);
INSERT INTO event_proposals(approved, budget, description, goals, needed_materials, title, work_plan, author_id, involved_company_id, assigned_reviewer_id) VALUES (false, 1000000, 'Event proposal 1', 'next company 1 event', 'nothing', 'Company 1 event', 'lunch', 4, 2, 3);
INSERT INTO event_proposals(approved, budget, description, goals, needed_materials, title, work_plan, author_id, involved_company_id, assigned_reviewer_id) VALUES (false, 1000000, 'Event proposal 1', 'next company 1 event', 'nothing', 'Company 1 event', 'lunch', 4, 2, 3);
INSERT INTO comments(text, timestamp, url, author_id, event_proposal_id) VALUES ('first comment', '2018-10-23 01:33:42.368718', 'http://url.com', 1, 1);
INSERT INTO comments(text, timestamp, url, author_id, event_proposal_id) VALUES ('second comment', '2018-10-23 01:33:42.368718', 'http://url.com', 3, 1);