-- Insert data into the BRAND table
INSERT INTO public."BRAND"
("STATUS_ENTITY", "USR_LOG_I", "DTE_LOG_I", "DTE_LOG_U", "USR_LOG_U", "VERSION", "BRAND_NAME", "DESCRIPTION", "IMAGE")
VALUES
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Samsung', 'Leading electronics brand known for phones, TVs, and appliances.', 'abc'),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'LG', 'Electronics and home appliances brand.', 'abc'),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Nike', 'Global sportswear and footwear brand.', 'abc'),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Adidas', 'Innovative sportswear brand.', 'abc'),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Zara', 'Fashion retailer offering trendy clothing.', 'abc'),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'IKEA', 'Famous for affordable and stylish furniture.', 'abc'),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Puma', 'Sportswear and footwear brand.', 'abc'),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'H&M', 'Affordable clothing and accessories brand.', 'abc'),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Whirlpool', 'Brand known for reliable home appliances.', 'abc'),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Sony', 'Electronics and entertainment products leader.', 'abc');

-- Insert data into the CATEGORY table
INSERT INTO public."CATEGORY"
("STATUS_ENTITY", "USR_LOG_I", "DTE_LOG_I", "DTE_LOG_U", "USR_LOG_U", "VERSION", "CATEGORY_NAME", "DESCRIPTION")
VALUES
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Electronics', 'Devices like phones, TVs, and appliances.'),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Clothing', 'Apparel for men, women, and children.'),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Shoes', 'Footwear for sports, casual, and formal occasions.'),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Furniture', 'Household items like tables, chairs, and cabinets.'),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Kitchenware', 'Items like cookware, utensils, and appliances for cooking.'),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Books', 'Second-hand books across various genres.'),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Toys', 'Items for children, including games and playsets.'),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Sports Equipment', 'Gear for fitness, sports, and outdoor activities.'),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Accessories', 'Items like bags, belts, and jewelry.'),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Home Decor', 'Decorative items for living spaces.');

-- Insert data into the ROLE table
INSERT INTO public."ROLE"
("STATUS_ENTITY", "USR_LOG_I", "DTE_LOG_I", "DTE_LOG_U", "USR_LOG_U", "VERSION", "NAME")
VALUES
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'ROLE_ADMIN'),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'ROLE_STAFF'),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'ROLE_USER');

-- Insert data into the USER table
INSERT INTO public."USER"
("STATUS_ENTITY", "USR_LOG_I", "DTE_LOG_I", "DTE_LOG_U", "USR_LOG_U", "VERSION", "EMAIL", "FULL_NAME", "GENDER", "IMAGE", "IS_FIRST_LOGIN", "PASSWORD", "PHONE", "USER_NAME", "ROLE_ID")
VALUES
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'ngoccuong@reas.vn', 'Ngoc Cuong', 'MALE', 'abc', false, 'password123', '1234567890', 'ngoccuong', 1),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'ducson@reas.vn', 'Duc Son', 'MALE', 'abc', false, 'password123', '0987654321', 'ducson', 2),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'tiendung@reas.vn', 'Tien Dung', 'MALE', 'abc', false, 'password123', '0912345678', 'tiendung', 3),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'ducanh@reas.vn', 'Duc Anh', 'MALE', 'abc', false, 'password123', '0922334455', 'ducanh', 3),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'trangthao@reas.vn', 'Trang Thao', 'FEMALE', 'abc', false, 'password123', '0933445566', 'trangthao', 3),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'minhquan@reas.vn', 'Minh Quan', 'FEMALE', 'abc', false, 'password123', '0944556677', 'minhquan', 3),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'hiennhu@reas.vn', 'Hien Nhu', 'MALE', 'abc', false, 'password123', '0955667788', 'hiennhu', 3);

-- Insert data into the LOCATION table
INSERT INTO public."LOCATION"
("STATUS_ENTITY", "USR_LOG_I", "DTE_LOG_I", "DTE_LOG_U", "USR_LOG_U", "VERSION", "AREA", "CITY", "CLUSTER", "DISTRICT", "PROVINCE", "WARD")
VALUES
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Vinhomes Grand Park', 'Ho Chi Minh City', 'Vinhomes', 'District 9', 'Ho Chi Minh', 'Long Thanh My'),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Masteri Thao Dien', 'Ho Chi Minh City', 'Masteri', 'District 2', 'Ho Chi Minh', 'Thao Dien'),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Cityland Park Hills', 'Ho Chi Minh City', 'Cityland', 'Go Vap', 'Ho Chi Minh', 'Phu Nhuan'),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'The Ascent', 'Ho Chi Minh City', 'The Ascent', 'District 2', 'Ho Chi Minh', 'Binh An'),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Saigon Pearl', 'Ho Chi Minh City', 'Saigon Pearl', 'Binh Thanh', 'Ho Chi Minh', '22 Ward');

-- Insert data into the USER_LOCATION table
INSERT INTO public."USER_LOCATION"
("STATUS_ENTITY", "USR_LOG_I", "DTE_LOG_I", "DTE_LOG_U", "USR_LOG_U", "VERSION", "IS_PRIMARY", "SPECIFIC_ADDRESS", "LOCATION_ID", "USER_ID")
VALUES
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, true, 'Room S2052503, Building 1', 1, 3),  -- Ti Dung - Vinhomes Grand Park
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, false, 'Room 101, Building 2', 2, 3),  -- Ti Dung - Masteri Thao Dien
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, true, 'Room 1205, Building A', 3, 3),  -- Ti Dung - Cityland Park Hills
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, false, 'Room 2005, Building B', 4, 3),  -- Ti Dung - The Ascent
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, true, 'Room 303, Building 3', 1, 4),  -- Duc Anh - Vinhomes Grand Park
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, false, 'Room 905, Building D', 5, 4),  -- Duc Anh - Saigon Pearl
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, true, 'Room 1106, Building 5', 2, 5),  -- Trang Thao - Masteri Thao Dien
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, false, 'Room 303, Building 1', 1, 5),  -- Trang Thao - Vinhomes Grand Park
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, true, 'Room 1603, Building A', 3, 6),  -- Minh Quan - Cityland Park Hills
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, false, 'Room 1502, Building C', 4, 6),  -- Minh Quan - The Ascent
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, true, 'Room 302, Building B', 4, 7),  -- Hien Nhu - The Ascent
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, false, 'Room 1206, Building D', 5, 7);  -- Hien Nhu - Saigon Pearl

-- Insert data into the ITEM table
-- Insert data into the ITEM table with corrected BRAND_ID and CATEGORY_ID
INSERT INTO public."ITEM"
("STATUS_ENTITY", "USR_LOG_I", "DTE_LOG_I", "DTE_LOG_U", "USR_LOG_U", "VERSION", "DESCRIPTION", "IMAGE_URL", "ITEM_NAME", "PRICE", "STATUS_ITEM", "BRAND_ID", "CATEGORY_ID", "OWNER_ID", "USER_LOCATION_ID")
VALUES
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Second-hand Nike shoes', 'abc', 'Nike Air Max', 1500000, 'AVAI', 3, 3, 3, 1),  -- Ti Dung - Location 1
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Used Samsung Galaxy phone', 'abc', 'Samsung Galaxy S21', 12000000, 'AVAI', 2, 1, 3, 2),  -- Ti Dung - Location 2
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Pre-owned LG refrigerator', 'abc', 'LG Double Door Refrigerator', 8000000, 'AVAI', 3, 5, 4, 3),  -- Duc Anh - Location 3
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Gently used Nike T-shirt', 'abc', 'Nike Sportswear T-shirt', 400000, 'AVAI', 3, 2, 5, 4),  -- Trang Thao - Location 4
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Slightly used Sony headphones', 'abc', 'Sony WH-1000XM4', 6000000, 'AVAI', 10, 1, 6, 5),  -- Minh Quan - Location 5
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Used Samsung refrigerator', 'abc', 'Samsung Refrigerator', 7000000, 'AVAI', 2, 5, 3, 6),  -- Ti Dung - Location 6
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Used Adidas sneakers', 'abc', 'Adidas Ultraboost', 2000000, 'AVAI', 4, 3, 4, 7),  -- Duc Anh - Location 7
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Used iPhone 12', 'abc', 'iPhone 12', 13000000, 'AVAI', 6, 1, 5, 8),  -- Trang Thao - Location 8
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Pre-owned LG TV', 'abc', 'LG 55" LED TV', 10000000, 'AVAI', 3, 1, 6, 9),  -- Minh Quan - Location 9
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Used Sony PlayStation 5', 'abc', 'PS5', 15000000, 'AVAI', 10, 1, 7, 10),  -- Hien Nhu - Location 10
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Used Samsung washing machine', 'abc', 'Samsung 8kg Washer', 5000000, 'AVAI', 2, 5, 1, 11),  -- Ti Dung - Location 11
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Pre-owned Bose speakers', 'abc', 'Bose SoundLink', 3500000, 'AVAI', 7, 1, 7, 12);  -- Hien Nhu - Location 12

