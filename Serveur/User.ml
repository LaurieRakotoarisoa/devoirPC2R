let get_Coords user =
	(user#get_nom)^":"^(user#get_coord)

let get_Scores user =
	(user#get_nom)^":"^(string_of_int user#get_score)


let rec find_usr nom users = 
match users with
[]->failwith "user no found"
|a::l-> if (String.equal nom a#get_nom) then a
		 else find_usr nom l 


let rec s_fin list_score win_cap = 
match list_score with
[]->false
|a::l-> if a >= win_cap then true else s_fin l win_cap

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
		val mutable coordX = Random.float 900.
		val mutable coordY = Random.float 400.0
		val mutable vitesse = (0,0)
		val mutable vainqueur = false
		method get_coord =  "X"^(string_of_float coordX)^"Y"^(string_of_float coordY)
		method get_score = score
		method add_score = score <- score + 1  
		method get_nom = nom
		method get_nom_and_coord = nom^":"^"X"^(string_of_float coordX)^"Y"^(string_of_float coordY)
		method gagne =  vainqueur <- true
		method set_pos x y = coordX <-  x ;coordY <- y
	end


class session (list_usrs:user list)= 
	object(self)
		val obj_radius = 30.
		val mutable users = list_usrs
		val mutable list_usr_sock = []
		val mutable objectifX = Random.float 900.0
		val mutable objectifY = Random.float 400.0
		val mutable win_cap = 5
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

		method get_list_scores = let l = List.map get_Scores users in
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
		method genere_new_obj = objectifX <- Random.float 900.0; objectifY <- Random.float 400.0
		method send_new_obj = List.iter (fun (usr,sock)->  let outchan = Unix.out_channel_of_descr sock in 
			output_string outchan ("NEWOBJ/"^self#get_coord_objectif^"/"^self#get_list_scores^"\n");flush outchan) 
			list_usr_sock ;
					
		method get_usr_par_nom nom = find_usr nom users;
		method touche_obj posX posY =  let distance = sqrt(((posX -. objectifX)*.(posX -. objectifX))+.((posY-. objectifY)*.(posY-.objectifY)))
					  in if distance <= obj_radius then true else false ;
	
		method est_fin_session = let l_scores = List.map (fun u -> u#get_score) users in s_fin l_scores win_cap 
		method send_scores_finaux =  List.iter (fun (usr,sock)->  let outchan = Unix.out_channel_of_descr sock in 
			output_string outchan ("WINNER/"^self#get_list_scores^"\n");flush outchan) 
			list_usr_sock ;	
	
		method send_coords = List.iter (fun (usr,sock)->  let outchan = Unix.out_channel_of_descr sock in 
			output_string outchan ("TICK/"^self#get_list_coords^"\n");flush outchan) 
			list_usr_sock
				
	end		 
