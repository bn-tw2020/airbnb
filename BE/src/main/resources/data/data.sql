insert into region(distance, region_img, region_name) values ('차로 30분거리', '서울이미지', '서울');
insert into region(distance, region_img, region_name) values ('차로 4시간거리', '광주이미지', '광주');
insert into region(distance, region_img, region_name) values ('차로 30분거리', '의정부이미지', '의정부');
insert into region(distance, region_img, region_name) values ('차로 45분거리', '수원이미지', '수원');
insert into region(distance, region_img, region_name) values ('차로 3.5시간거리', '이미지', '대구');
insert into region(distance, region_img, region_name) values ('차로 4.5시간시간거리', '대구이미지', '울산');
insert into region(distance, region_img, region_name) values ('차로 2시간거리', '대전이미지', '대전');
insert into region(distance, region_img, region_name) values ('차로 30분거리', '부천이미지', '부천');

insert into user(username, password, address, birthday, phone_number) values ('geombong', '1234', 'abnc@naver.com', '2000-01-01', '010-1234-5678');
insert into user(username, password, address, birthday, phone_number) values ('tany', '8888', 'juni8453@naver.com', '1996-12-24', '010-5592-9710');

## 숙소 데이터
insert into accommodation(dong, eup, gu, gun, si, accommodation_type, max_guest, bath_count, bed_count, description, review_count, name, start_point, main_img_url, region_id)
                                                                                    VALUES ('양재동', null, '서초구', null, '서울특별시', 'ONE_ROOM', 3, 1, 1, '설명', '130', '신라호텔', '4.80', 'mainUrl', 1);

insert into accommodation(dong, eup, gu, gun, si, accommodation_type, max_guest, bath_count, bed_count, description, review_count, name, start_point, main_img_url, region_id)
                                                                                    VALUES ('남현동', null, '관악구', null, '서울특별시', 'TWO_ROOM', 3, 1, 1, '남현동 숙소', '50', '남현호텔', 3.20, 'mainURL', 1)


## 예약 데이터 (숙소에 예약 2개)
insert into reservation(check_in_time, checkout_time, max_price, min_price, adult_count, child_count, infant_count, cleaning_fee, discount_policy, room_charge, service_fee, total_amount_of_day, total_amount_of_reservation, accommodation_id, user_id)
                                                                                    VALUES ('2022-04-01', '2022-04-03', 1000000, 50000, 2, 1, 0, 25996, 'WEEK', 82953, 8188, 1322396, 1488195, 1,1);

insert into reservation(check_in_time, checkout_time, max_price, min_price, adult_count, child_count, infant_count, cleaning_fee, discount_policy, room_charge, service_fee, total_amount_of_day, total_amount_of_reservation, accommodation_id, user_id)
                                                                                    VALUES ('2022-04-05', '2022-04-15', 1000000, 50000, 2, 1, 0, 25996, 'WEEK', 82953, 8188, 1322396, 1488195, 1,1);

insert into reservation(check_in_time, checkout_time, max_price, min_price, adult_count, child_count, infant_count, cleaning_fee, discount_policy, room_charge, service_fee, total_amount_of_day, total_amount_of_reservation, accommodation_id, user_id)
                                                                                    VALUES ('2022-05-01', '2022-05-04', 1000000, 50000, 2, 1, 0, 25996, 'WEEK', 82953, 8188, 1322396, 1488195, 2,1);

insert into reservation(check_in_time, checkout_time, max_price, min_price, adult_count, child_count, infant_count, cleaning_fee, discount_policy, room_charge, service_fee, total_amount_of_day, total_amount_of_reservation, accommodation_id, user_id)
                                                                                    VALUES ('2022-05-05', '2022-05-10', 1000000, 50000, 2, 1, 0, 25996, 'WEEK', 82953, 8188, 1322396, 1488195, 2,1);