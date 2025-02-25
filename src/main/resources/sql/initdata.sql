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
("STATUS_ENTITY", "USR_LOG_I", "DTE_LOG_I", "DTE_LOG_U", "USR_LOG_U", "VERSION", "CATEGORY_NAME", "DESCRIPTION", "TYPE_ITEM")
VALUES
    -- KITCHEN_APPLIANCES ("KITC")
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Refrigerators', 'Including compact and multi-door models.', 'KITC'),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Rice Cookers', 'Essential for daily cooking.', 'KITC'),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Induction Cookers', 'Popular for fast and efficient cooking.', 'KITC'),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Microwave Ovens', 'Widely used for reheating and quick meals.', 'KITC'),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Electric Kettles', 'For boiling water for tea and coffee.', 'KITC'),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Water Purifiers', 'Important for ensuring safe drinking water.', 'KITC'),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Blenders & Food Processors', 'For preparing smoothies, sauces, and traditional recipes.', 'KITC'),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Dishwashers', 'Increasingly popular in urban apartments.', 'KITC'),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Coffee Makers', 'Catering to both local and international coffee trends.', 'KITC'),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Air Fryers', 'Gaining traction as a healthier cooking option.', 'KITC'),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Others', 'Other appliances', 'KITC'),

    -- CLEANING_LAUNDRY_APPLIANCES ("CLLA")
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Washing Machines', 'Both front-load and top-load types.', 'CLLA'),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Washer-Dryer Combos', 'Useful in limited urban spaces.', 'CLLA'),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Clothes Dryers', 'For apartment dwellers without outdoor drying space.', 'CLLA'),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Steam Irons', 'For quick and effective wrinkle removal.', 'CLLA'),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Robotic Vacuum Cleaners', 'An emerging trend in high-end urban homes.', 'CLLA'),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Upright/Handheld Vacuum Cleaners', 'For everyday cleaning tasks.', 'CLLA'),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Steam Mops', 'A modern approach to floor cleaning.', 'CLLA'),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Portable Washing Machines', 'Ideal for smaller households or dorms.', 'CLLA'),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Carpet Cleaners', 'For homes with area rugs or carpets.', 'CLLA'),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Lint Removers/Fabric Shavers', 'To maintain clothing and upholstery quality.', 'CLLA'),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Others', 'Other appliances', 'CLLA'),

    -- COOLING_HEATING_APPLIANCES ("COHE")
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Split Air Conditioners', 'Standard for most urban homes.', 'COHE'),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Window Air Conditioners', 'A cost-effective cooling option.', 'COHE'),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Portable Air Conditioners', 'For added flexibility in cooling.', 'COHE'),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Ceiling Fans', 'Popular and energy-efficient.', 'COHE'),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Table & Floor Fans', 'Common in many households.', 'COHE'),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Air Coolers/Evaporative Coolers', 'Suited for certain regions.', 'COHE'),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Smart Air Conditioners', 'With remote and app control features.', 'COHE'),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Air Purifiers', 'Improving sleep quality by filtering air.', 'BEDR'),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Humidifiers', 'Depending on the local microclimate.', 'BEDR'),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Dehumidifiers', 'Depending on the local microclimate.', 'BEDR'),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Energy-Efficient Cooling Solutions', 'Models designed to lower electricity bills.', 'COHE'),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Others', 'Other appliances', 'COHE'),

    -- ELECTRONICS_ENTERTAINMENT_DEVICES ("ELEC")
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Smart TVs', 'A centerpiece for modern living rooms.', 'ELEC'),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Home Theater Systems', 'For immersive audio-visual experiences.', 'ELEC'),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Soundbars', 'To enhance TV audio in compact spaces.', 'ELEC'),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Streaming Devices', 'Devices like Roku, Apple TV, or local alternatives.', 'ELEC'),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Gaming Consoles', 'Popular among youth and tech-savvy consumers.', 'ELEC'),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Digital Cameras', 'Although smartphones are often preferred.', 'ELEC'),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Portable Bluetooth Speakers', 'For on-the-go or outdoor use.', 'ELEC'),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Set-Top Boxes', 'For cable and satellite TV integration.', 'ELEC'),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Blu-ray/DVD Players', 'Still in use for certain content.', 'ELEC'),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Virtual Reality Headsets', 'A niche but growing interest.', 'ELEC'),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Others', 'Other appliances', 'ELEC'),

    -- LIGHTING_SECURITY_DEVICES ("LISE")
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Smart LED Bulbs', 'Energy-efficient with app control.', 'LISE'),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'LED Light Strips & Panels', 'For decorative and ambient lighting.', 'LISE'),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Security Cameras', 'Essential for modern home security.', 'LISE'),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Video Doorbells', 'For added safety and convenience.', 'LISE'),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Motion Sensors', 'Integrated into smart home setups.', 'LISE'),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Alarm Systems', 'Ranging from basic to advanced.', 'LISE'),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Smart Locks', 'Enhancing door security.', 'LISE'),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Outdoor Floodlights', 'For well-lit surroundings.', 'LISE'),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Wireless Security Sensors', 'For windows and doors.', 'LISE'),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Home Automation Hubs', 'Centralizing control of multiple devices.', 'LISE'),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Others', 'Other appliances', 'LISE'),

    -- BEDROOM_APPLIANCES ("BEDR")
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Bedside Lamps', 'With adjustable brightness.', 'BEDR'),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Bed', 'Comfortable thing to sleep on.', 'BEDR'),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Matress', 'The part of a bed, made of a strong cloth cover filled with firm material, that makes the bed comfortable to lie on.', 'BEDR'),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Personal/Mini Fans', 'For personal cooling.', 'BEDR'),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Smart Alarm Clocks', 'With gentle wake-up features.', 'BEDR'),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Sleep Trackers', 'For monitoring sleep quality.', 'BEDR'),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'LED Reading Lights', 'For night-time reading.', 'BEDR'),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Aroma Diffusers', 'To create a relaxing environment.', 'BEDR'),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Electric Mosquito Repellers', 'Essential in tropical regions.', 'BEDR'),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'White Noise Machines', 'For blocking out disruptive sounds.', 'BEDR'),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Others', 'Other appliances', 'BEDR'),

    -- LIVING_ROOM_APPLIANCES ("LIVI")
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Sofa Set', 'A comfortable and stylish sofa set perfect for a cozy living room.', 'LIVI'),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Armchair', 'Elegant armchairs that add both comfort and charm to your living space.', 'LIVI'),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Coffee Table', 'A versatile coffee table ideal for holding drinks, magazines, and decor.', 'LIVI'),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Side Table', 'Convenient side tables to complement seating areas and provide extra surface space.', 'LIVI'),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Ottoman', 'A multifunctional ottoman that can serve as a footrest or additional seating.', 'LIVI'),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Others', 'Other appliances', 'LIVI'),

    -- BATHROOM_APPLIANCES ("BATH")
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Electric Toothbrushes', 'For improved oral hygiene.', 'BATH'),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Instant/Tankless Water Heaters', 'Providing hot water on demand.', 'BATH'),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Hair Dryers', 'A common necessity.', 'BATH'),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Electric Shavers/Hair Trimmers', 'For personal grooming.', 'BATH'),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Bathroom Ventilation Fans', 'To reduce humidity and mold.', 'BATH'),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Bidet Toilet Seats', 'Gaining popularity in modern bathrooms.', 'BATH'),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Heated Towel Racks', 'More common in upscale settings.', 'BATH'),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Electric Faucets', 'For water efficiency and hygiene.', 'BATH'),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Smart Mirrors', 'Offering integrated lighting and information.', 'BATH'),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Digital Bathroom Scales', 'For personal health tracking.', 'BATH'),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'Others', 'Other appliances', 'BATH');


-- Insert data into the ROLE table
INSERT INTO public."ROLE"
("STATUS_ENTITY", "USR_LOG_I", "DTE_LOG_I", "DTE_LOG_U", "USR_LOG_U", "VERSION", "NAME")
VALUES
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'ROLE_ADMIN'),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'ROLE_STAFF'),
    ('ACTIVE', 'admin', NOW(), NULL, NULL, 0, 'ROLE_RESIDENT');

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


