
INSERT INTO card (id, name, creator, publisher, category, price, quantity, version, url_immagine)
VALUES
    (1, '4060', 'Nvidia', 'ASUS', 'DUAL', 318.99 , 8, 1, 'images/4060asus.jpg'),
    (2, '4060', 'Nvidia', 'MSI', 'DUAL', 318.93 , 7, 1, 'images/4060msi.jpg'),
    (3, '4060 Ti', 'Nvidia', 'ASUS', 'DUAL', 433.72 , 5, 1, 'images/4060tiasus.jpg'),
    (4, '4060 Ti', 'Nvidia', 'MSI', 'TRIPLE', 479.01 , 6, 1, 'images/4060timsi.jpg'),
    (5, '4070', 'Nvidia', 'ASUS', 'DUAL', 589.05 , 7, 1, 'images/4070asus.jpg'),
    (6, '4070', 'Nvidia', 'MSI', 'DUAL', 597.96 , 4, 1, 'images/4070msi.jpg'),
    (7, '4070 Super', 'Nvidia', 'ASUS', 'DUAL', 653.99 , 9, 1, 'images/4070superasus.jpg'),
    (8, '4070 Super', 'Nvidia', 'MSI', 'TRIPLE', 729.01 , 4, 1, 'images/4070supermsi.jpg'),
    (9, '4070 Ti', 'Nvidia', 'ASUS', 'DUAL', 849.98 , 7, 1, 'images/4070tiasus.jpg'),
    (10, '4070 Ti', 'Nvidia', 'MSI', 'DUAL', 788.01 , 5, 1, 'images/4070timsi.jpg'),
    (11, '4070 Ti Super', 'Nvidia', 'ASUS', 'TRIPLE', 961.02 , 6, 1, 'images/4070tisuperasus.jpg'),
    (12, '4070 Ti Super', 'Nvidia', 'MSI', 'TRIPLE', 957.27 , 8, 1, 'images/4070tisupermsi.jpg'),
    (13, '4080', 'Nvidia', 'ASUS', 'TRIPLE', 1499.45 , 4, 1, 'images/4080asus.jpg'),
    (14, '4080', 'Nvidia', 'MSI', 'TRIPLE', 1524.55 , 6, 1, 'images/4080msi.jpg'),
    (15, '4080 Super', 'Nvidia', 'ASUS', 'TRIPLE', 1259.04 , 5, 1, 'images/4080superasus.jpg'),
    (16, '4080 Super', 'Nvidia', 'MSI', 'TRIPLE', 1278.26 , 4, 1, 'images/4080supermsi.jpg'),
    (17, '4090', 'Nvidia', 'MSI', 'TRIPLE', 2243.77 , 2, 1, 'images/4090asus.jpg'),
    (18, '4090', 'Nvidia', 'MSI', 'TRIPLE', 2029.98 , 1, 1, 'images/4090msi.jpg'),
    (19, '7600', 'AMD', 'XFX', 'DUAL', 269.08 , 1, 1, 'images/7600xfx.jpg'),
    (20, '7600', 'AMD', 'SAPPHIRE', 'DUAL', 288.44 , 2, 1, 'images/7600sapphire.jpg'),
    (21, '7600 XT', 'AMD', 'XFX', 'DUAL', 369.04 , 2, 1, 'images/7600xtxfx.jpg'),
    (22, '7600 XT', 'AMD', 'SAPPHIRE', 'DUAL', 362.66 , 2, 1, 'images/7600xtsapphire.jpg'),
    (23, '7700 XT', 'AMD', 'XFX', 'TRIPLE', 439.02 , 2, 1, 'images/7700xtxfx.jpg'),
    (24, '7700 XT', 'AMD', 'SAPPHIRE', 'DUAL', 448.41 , 2, 1, 'images/7700xtsapphire.jpg'),
    (25, '7800 XT', 'AMD', 'XFX', 'TRIPLE', 539.05 , 2, 1, 'images/7800xtxfx.jpg'),
    (26, '7800 XT', 'AMD', 'SAPPHIRE', 'DUAL', 563.32 , 2, 1, 'images/7800xtsapphire.jpg'),
    (27, '7900 XT', 'AMD', 'XFX', 'TRIPLE', 769.07 , 2, 1, 'images/7900xtxfx.jpg'),
    (28, '7900 XT', 'AMD', 'SAPPHIRE', 'TRIPLE', 881.91 , 2, 1, 'images/7900xtsapphire.jpg'),
    (29, '7900 GRE', 'AMD', 'XFX', 'TRIPLE', 589.03 , 2, 1, 'images/7900grexfx.jpg'),
    (30, '7900 GRE', 'AMD', 'SAPPHIRE', 'TRIPLE', 639.98 , 2, 1, 'images/7900gresapphire.jpg'),
    (31, '7900 XTX', 'AMD', 'XFX', 'TRIPLE', 979.03 , 2, 1, 'images/7900xtxxfx.jpg'),
    (32, '7900 XTX', 'AMD', 'SAPPHIRE', 'TRIPLE', 1131.17 , 2, 1, 'images/7900xtxsapphire.jpg'),
    (33, 'A380', 'INTEL ARC', 'ASROCK', 'DUAL', 127.77, 2, 1, 'images/a380.jpg'),
    (34, 'A580', 'INTEL ARC', 'ASROCK', 'DUAL', 199.58 , 2, 1, 'images/a580.jpg'),
    (35, 'A770', 'INTEL ARC', 'ASROCK', 'DUAL', 328.28 , 2, 1, 'images/a770.jpg');


INSERT INTO carts(id,user_id)
values
    (9000,9000);
INSERT INTO users (id, first_name, last_name, email, address, phone)
VALUES
    (9000, 'Demetrio', 'Romeo', 'romeodemetrio01@gmail.com', 'via adasda', '321321233');

SELECT * FROM information_schema.sequences WHERE sequence_name = 'users_id_seq';

