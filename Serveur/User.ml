
let refresh_tickrate = 100.0

let get_Coords user =
	(user#get_nom)^":"^(user#get_coord)

let get_Scores user =
	(user#get_nom)^":"^(string_of_int user#get_score)

let get_Vcoords user =
	(user#get_nom)^":"^(user#get_vcoords)

let get_Occord obstacle =
	obstacle#get_coord

let get_bcoord bombe = 
	if bombe#est_visible then
		bombe#get_coord else ""
	
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



class balle x y angle = 
	object(self)
		val mutable coordX = x
		val mutable coordY = y
		val vitesseX = 10. *. (cos angle)
		val vitesseY = 10. *. (sin angle)
		val demi_largeur = 450.0
		val demi_hauteur = 200.0
		val mutable visible = true
		method  get_coord = "X"^(string_of_float coordX)^"Y"^(string_of_float coordY)
		method get_x = coordX
		method get_y = coordY
		method bombe_move = if visible then (
							  coordX <- (let x = coordX +. vitesseX in if (compare  x demi_largeur) >= 0  then (visible<-false ; 0. )
								else if (compare  x (-.demi_largeur)) <= 0 then (visible<-false ; 0. ) else x);
						 coordY <- (let y = coordY +. vitesseY in if (compare  y demi_hauteur) >=0 then (visible<-false ; 0. )
						 		else if (compare  y  (-. demi_hauteur))<=0 then (visible<-false ; 0. ) else y )
						)

		method est_visible = visible
		method touche = visible <- false
	end  

class user  (n:string)=
	object(self)
		val mutable nom = n 
		val mutable score = 0
		val mutable coordX = Random.float 450.
		val mutable coordY = Random.float 200.0
		val mutable vitesse = (0.,0.)
		val mutable vainqueur = false
		val mutable angle = 0.0
		val mutable turnit = 0.1
		val mutable thrustit = 0.1
		val demi_largeur = 450.0
		val demi_hauteur = 200.0
		val ob_radius = 30.
		method get_x = coordX
		method get_y = coordY  
		method get_coord =  "X"^(string_of_float coordX)^"Y"^(string_of_float coordY)
		method get_score = score
		method add_score = score <- score + 1  
		method get_nom = nom
		method get_nom_and_coord = nom^":"^"X"^(string_of_float coordX)^"Y"^(string_of_float coordY)
		method gagne =  vainqueur <- true
		method set_pos x y = coordX <-  x ;coordY <- y
		method reset_pos =  coordX <-  Random.float 450. ;coordY <- Random.float 200.0
		method reset_score = score <- 0
		method inverse_vitesse = let vx, vy = vitesse in vitesse <- ( (-.vx), (-.vy) )
		method rotation a = angle <- angle +. a;
		method thrust = vitesse<- let vx,vy = vitesse in 
					(vx +. thrustit *. (cos angle),vy +. thrustit*.(sin angle)) 
		method deplacer = let vx,vy = vitesse in 
						 coordX <- (let x = coordX +. vx in if (compare  x demi_largeur) >= 0  then (-.demi_largeur) 
								else if (compare  x (-.demi_largeur)) <= 0 then demi_largeur else x);
						 coordY <- (let y = coordY +. vy in if (compare  y demi_hauteur) >=0 then (-.demi_hauteur)
						 		else if (compare  y  (-. demi_hauteur))<=0 then demi_hauteur else y )
		method gerer_cmd a (t:int) = self#rotation a ;
									for i = t downto 1 do
										self#thrust
									done
		method  get_vcoords = let vx,vy = vitesse in 
			self#get_coord ^ "VX"^(string_of_float vx)^"VY"^(string_of_float vy)^"T"^(string_of_float angle)

		method generer_balle = let x = coordX +. (ob_radius*.(cos angle)) and  y = coordY +. (ob_radius*.(sin angle)) in 
					let b = new balle x y angle in 
					b 
		method freeze =  vitesse <- (0.,0.)
	end

class obstacle (coord) =
	object(self)
		val mutable coordX = let x,y = coord in x 
		val mutable coordY = let x,y = coord in y
		method  get_coord = "X"^(string_of_float coordX)^"Y"^(string_of_float coordY)
		method get_x = coordX
		method get_y = coordY

	end  



class session (list_usrs:user list)= 
	object(self)
		val obj_radius = 20.
		val ob_radius = 15.
		val ve_radius = 15.
		val mutable users = list_usrs
		val mutable list_usr_sock = []
		val mutable objectifX = (Random.float 900.0)-.450.
		val mutable objectifY = (Random.float 400.0)-.200.
		val mutable win_cap = 2
		val mutable phase = "attente"
		val mutable list_obstacle = []
		val mutable list_balles = []
		method ajout_balle (b:balle) = list_balles <- b::list_balles 
		method init_obstacles = list_obstacle <- [];

				let coord1 = (  ((Random.float 900.0)-.450.),((Random.float 400.0)-.200.)) 
					and coord2 = ( ((Random.float 900.0)-.450.),((Random.float 400.0)-.200.)) in 
				let o1 = new obstacle coord1 and o2 = new obstacle coord2 in 
								list_obstacle<-o1::o2::list_obstacle
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

		method get_list_bcoords = let  l = List.map get_bcoord list_balles in 
								let s = ref (List.hd l) and l2 = List.tl l in
						let rec liste_to_str liste =
						match liste with
						u::tl -> if (not (String.equal "" u)) then 
						s := !s^"|"^u; liste_to_str tl;
						| [] -> ()
					in liste_to_str l2; !s
		method get_list_occords = let  l = List.map get_Occord list_obstacle in 
								let s = ref (List.hd l) and l2 = List.tl l in
						let rec liste_to_str liste =
						match liste with
						u::tl -> s := !s^"|"^u; liste_to_str tl;
						| [] -> ()
					in liste_to_str l2; !s
		method get_list_vcoords = let l =  List.map get_Vcoords users in
					let s = ref (List.hd l) and l2 = List.tl l in
						let rec liste_to_str liste =
						match liste with
						u::tl -> s := !s^"|"^u; liste_to_str tl;
						| [] -> ()
					in liste_to_str l2; !s
		method get_list_scores = let l = List.map get_Scores users in
					let s = ref (List.hd l) and l2 = List.tl l in
						let rec liste_to_str liste =
						match liste with
						u::tl -> s := !s^"|"^u; liste_to_str tl;
						| [] -> ()
					in liste_to_str l2; !s
		method session_lauched = print_endline "La session commence !";phase <- "jeu" ; 
								list_balles<-[];
								 self#send_session
		method get_phase = phase 
		method genere_new_obj = objectifX <- (Random.float 900.0)-.450.; objectifY <- (Random.float 400.0)-.200.
		method send_new_obj = List.iter (fun (usr,sock)->  let outchan = Unix.out_channel_of_descr sock in 
			output_string outchan ("NEWOBJ/"^self#get_coord_objectif^"/"^self#get_list_scores^"\n");flush outchan) 
			list_usr_sock ;
					
		method get_usr_par_nom nom = find_usr nom users;
		method touche_obj usr  =  let distance = sqrt(((usr#get_x -. objectifX)*.(usr#get_x -. objectifX))+.((usr#get_y-. objectifY)*.(usr#get_y-.objectifY)))
					  in if distance <= obj_radius+.ve_radius then (usr#add_score; self#genere_new_obj; self#send_new_obj)  ;
	
		method detect_touche_obstacles  (usr:user) = List.iter (fun ob -> 
					let distance = sqrt(((usr#get_x -. ob#get_x)*.(usr#get_x -. ob#get_x))+.((usr#get_y-. ob#get_y)*.(usr#get_y-.ob#get_y)))
					  in if distance <= ob_radius+.ve_radius then usr#inverse_vitesse 
											) list_obstacle

		method detect_touche_bombes  (usr:user) = List.iter (fun bombe -> 
					let distance = sqrt(((usr#get_x -. bombe#get_x)*.(usr#get_x -. bombe#get_x))+.((usr#get_y-. bombe#get_y)*.(usr#get_y-.bombe#get_y)))
					in if distance <=  ve_radius then bombe#touche ; (* usr#freeze *) 
					) list_balles

		method detect_object = List.iter self#touche_obj users
		method detect_collision = List.iter self#detect_touche_bombes users;
							List.iter self#detect_touche_obstacles users
		method est_fin_session = let l_scores = List.map (fun u -> u#get_score) users in s_fin l_scores win_cap 
		method fin_de_session = print_endline "La session est fini !" ;
							List.iter (fun (usr,sock)->  let outchan = Unix.out_channel_of_descr sock in 
							output_string outchan ("WINNER/"^self#get_list_scores^"\n");flush outchan) 
							list_usr_sock ;	
							phase <-"attente" ;
							self#reset
	
		method send_coords = List.iter (fun (usr,sock)->  let outchan = Unix.out_channel_of_descr sock in 
			output_string outchan ("TICK/"^self#get_list_coords^"\n");flush outchan) 
			list_usr_sock
		
		method send_vcoords = List.iter (fun (usr,sock)->  let outchan = Unix.out_channel_of_descr sock in 
			output_string outchan ("TICK/"^self#get_list_vcoords^"\n");flush outchan) 
			list_usr_sock

		method send_session = List.iter (fun (usr,sock)->  let outchan = Unix.out_channel_of_descr sock in 
			output_string outchan ("SESSION/"^self#get_list_coords^"/"^self#get_coord_objectif^"/"^self#get_list_occords^"\n");flush outchan) 
			list_usr_sock

		method  send_welcome =  List.iter (fun (usr,sock)->  let outchan = Unix.out_channel_of_descr sock in 
			output_string outchan ("WELCOME/"^self#get_phase ^"/"^self#get_list_scores^"/"^self#get_coord_objectif^"/"^self#get_list_occords^"\n");flush outchan) 
			list_usr_sock

		method reset = List.iter (fun u -> u#reset_score ; u#reset_pos) users ;
						list_balles <- [];
						self#init_obstacles


		method  deplacement_vehicules =  List.iter (fun u-> u#deplacer ) users
		method deplacement_balles = List.iter (fun b -> b#bombe_move) list_balles
		method send_balles =  List.iter (fun (usr,sock)->  let outchan = Unix.out_channel_of_descr sock in 
			output_string outchan ("BALLES/"^self#get_list_bcoords^"\n");flush outchan) 
			list_usr_sock

		method get_list_bombes =  list_balles 
		method envoi_pmessage (name:string) msg =  
			List.iter (fun (usr,sock)->  if (String.equal usr name) then
				let outchan = Unix.out_channel_of_descr sock in 
			output_string outchan ("PRECEPTION/"^msg^"/"^name^"/\n");flush outchan) 
			list_usr_sock
		method envoi_msg (nom:string) msg = 
		List.iter (fun (usr,sock)->  if not (String.equal usr nom) then
			let outchan = Unix.out_channel_of_descr sock in 
			output_string outchan ("RECEPTION/"^msg^"\n");flush outchan) 
			list_usr_sock

		method have_balle = let have_balle = ref false in 
						List.iter (fun b -> if b#est_visible then have_balle:=true ) list_balles;
						!have_balle 


							
	end		 
