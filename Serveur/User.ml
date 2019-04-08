let get_Coords user =
	(user#get_nom)^":"^(user#get_coord)
;;

let print_coords l_users = 
	let rec print s =
		match l_users with
		u::l -> print (s^"|"^u);
		| [] -> s
	in print ""

class user  (n:string)=
	object 
		val mutable nom = n 
		val mutable score = 0
		val mutable coordX = Random.float 100.0
		val mutable coordY = Random.float 100.0
		val mutable phase = "attente"
		method get_coord =  "X"^(string_of_float coordX)^"Y"^(string_of_float coordY)
		method get_score_str = string_of_int score
		method add_score = score <- score + 1  
		method get_phase = phase 
		method get_nom = nom
		method get_nom_and_coord = nom^":"^"X"^(string_of_float coordX)^"Y"^(string_of_float coordY)
		method change_phase p = phase = p
	end


class session (list_usrs:user list)= 
	object(self)
		val mutable users = list_usrs
		val mutable objectifX = Random.float 100.0
		val mutable objectifY = Random.float 100.0
		val mutable win_cap = 2

		method connect x = users <- (x:user)::users
		method size_users = List.length users
		method get_usrs = users 
		method get_coord_objectif = "X"^(string_of_float objectifX)^"Y"^(string_of_float objectifY)
		method get_list_coords = let l = List.map get_Coords users in
					let s = ref (List.hd l) and l2 = List.tl l in
						let rec liste_to_str liste =
						match liste with
						u::tl -> s := !s^"|"^u; liste_to_str tl;
						| [] -> ()
					in liste_to_str l2; 
					!s;
					
									
			 (* (List.fold_right (fun u acc-> (u#get_nom^acc) ) users "/" ) *)
				
	end		 
