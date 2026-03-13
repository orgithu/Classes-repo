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
