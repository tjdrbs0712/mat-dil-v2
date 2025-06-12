-- 메뉴 테이블에 menu_name 컬럼 추가
ALTER TABLE menus
    ADD COLUMN menu_name VARCHAR(255) NOT NULL DEFAULT '';