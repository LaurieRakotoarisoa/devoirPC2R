open User

let rec print_list = function 
[] -> ()
| e::l -> print_string e ;print_string " " ; print_list l

let append l a = 
	l:=!l @ [a]

let compte_a_rebours () = 
	for i = 5 downto 1 do
		Thread.delay 1.0;
		print_endline ("Il reste "^(string_of_int i)^" secondes  avant de lancer la session ");

	done

(* la frequence de rafraichissement reseau *)
let server_tickrate = 10.0
let refresh_tickrate = 100.0

let rec get_list_coords  l_usr= 
	match l_usr with
	[]-> ()
	|e::l -> print_endline (e # get_nom_and_coord);
			get_list_coords l 
		
let tick s = 
	while (String.equal s#get_phase "jeu" ) do

		
			Thread.delay (1.0/.server_tickrate);
			(* s#send_coords; *)
			s#send_vcoords;

			(* print_endline s#get_list_vcoords; *)
			(* print_endline s#get_list_coords; *)
	done

let deplacement_vehicules s =
	while  (String.equal s#get_phase "jeu" ) do
		Thread.delay (1.0/.refresh_tickrate);
		s#deplacement_vehicules;		
	done

let send_bombe s = 
	while (String.equal s#get_phase "jeu" ) do

			Thread.delay (1.0/.server_tickrate);
			if not ((List.length s#get_list_bombes)==0 ) then
					s#send_bombes
					(* print_endline s#get_list_bcoords *)
			
	done	

let detect_collision s = 
	while (String.equal s#get_phase "jeu" ) do

			Thread.delay (1.0/.refresh_tickrate);
			s#detect_collision
			
	done	

let lanche_session s = 
	
	s#session_lauched;
	
	let _ = Thread.create tick s 
	and _ = Thread.create deplacement_vehicules s 
	and _ = Thread.create send_bombe s
	and _ = Thread.create detect_collision s in ()

	




