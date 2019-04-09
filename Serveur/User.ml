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
		val mutable vitesse = (0,0)
		method get_coord =  "X"^(string_of_float coordX)^"Y"^(string_of_float coordY)
		method get_score_str = string_of_int score
		method add_score = score <- score + 1  
		method get_nom = nom
		method get_nom_and_coord = nom^":"^"X"^(string_of_float coordX)^"Y"^(string_of_float coordY)
	end


class session (list_usrs:user list)= 
	object(self)
		val obj_radius = 5
		val mutable users = list_usrs
		val mutable list_usr_sock = []
		val mutable objectifX = Random.float 100.0
		val mutable objectifY = Random.float 100.0
		val mutable win_cap = 2
		val mutable phase = "attente"
		method connect x nom_socket = users <- (x:user)::users ;
								list_usr_sock <- (nom_socket :string *Unix.file_descr)::list_usr_sock
		method deconnect nom_usr = users <- List.filter (fun u -> not (String.equal u#get_nom nom_usr)) users;
									list_usr_sock <- List.filter (fun nom_socket ->  let (nom,sock) = nom_socket in 
																	not (String.equal nom nom_usr) ) list_usr_sock
		method size_users = List.length users
		method get_usrs = users 
		method get_usrs_socks = list_usr_sock
		method get_coord_objectif = "X"^(string_of_float objectifX)^"Y"^(string_of_float objectifY)
		method get_list_coords = let l = List.map get_Coords users in
					let s = ref (List.hd l) and l2 = List.tl l in
						let rec liste_to_str liste =
						match liste with
						u::tl -> s := !s^"|"^u; liste_to_str tl;
						| [] -> ()
					in liste_to_str l2; 
					!s;
		method session_lauched = phase <- "jeu" 
		method session_arrete = phase <- "attente"
		method get_phase = phase 
		method genere_nouvel_objectif = objectifX <- Random.float 100.0;
										objectifY <- Random.float 100.0
					
									
			 (* (List.fold_right (fun u acc-> (u#get_nom^acc) ) users "/" ) *)
				
	end		 
