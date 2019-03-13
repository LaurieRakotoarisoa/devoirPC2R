class user n =
	object 
		val mutable nom = n 
		val mutable score = 0
		val mutable coordX = 0.0
		val mutable coordY = 0.0
		method get_coord =  "X"^(string_of_float coordX)^"Y"^(string_of_float coordY)
		method get_score_str = string_of_int score
		method add_score = score <- score + 1  
	end
	 