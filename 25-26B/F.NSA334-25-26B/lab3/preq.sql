use university_lab3;
select 
	st.name,
	now.hicheel_ner songoson,
	pre.hicheel_ner required,
	if(uzsenu.id is not null,'yes','no') as uzsenuu,
	main.songoson_ognoo
from songolt main 
left join student st on st.id = main.oyutan_id
left join subject now on main.hicheel_id = now.id
left join subject pre on now.umnuh_holboo = pre.hicheel_kod;
left join songolt uzsenu 
	on main.oyutan_id = uzsenu.oyutan_id 
	and uzsenu.hicheel_id = pre.id;
