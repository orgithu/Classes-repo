USE university_lab3;
--prequisite uzsen eseh
SELECT 
    st.name,
    now.hicheel_ner AS songoson,
    pre.hicheel_ner AS required,
    IF(uzsenu.id IS NOT NULL, 'yes', 'no') AS uzsenuu,
    main.songoson_ognoo
FROM songolt main 
LEFT JOIN student st ON st.id = main.oyutan_id
LEFT JOIN subject now ON main.hicheel_id = now.id
LEFT JOIN subject pre ON now.umnuh_holboo = pre.hicheel_kod
LEFT JOIN songolt uzsenu 
    ON main.oyutan_id = uzsenu.oyutan_id 
    AND uzsenu.hicheel_id = pre.id;

--bagsh heden oyutand hicheel zaaj baiga
SELECT 
    b.name AS Bagsh, 
    COUNT(hs.oyutan_id) AS Oyutan_Too
FROM teacher AS b
JOIN songolt AS hs ON b.id = hs.bagsh_id
GROUP BY b.id;

--oyutnii songoson niit credit
SELECT 
    o.name AS Oyutan, 
    SUM(h.kredit) AS Niit_Kredit
FROM student AS o
JOIN songolt AS hs ON o.id = hs.oyutan_id
JOIN subject AS h ON hs.hicheel_id = h.id
GROUP BY o.id, o.name;
