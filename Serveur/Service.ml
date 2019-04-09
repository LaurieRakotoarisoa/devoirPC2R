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

let rec get_list_coords  l_usr= 
	match l_usr with
	[]-> ()
	|e::l -> print_endline (e # get_nom_and_coord);
			get_list_coords l 
		
let synchronisation s = 
	
	while true do
		Thread.delay 1.0;
		List.iter (fun (usr,sock)->  let outchan = Unix.out_channel_of_descr sock in 
			output_string outchan ("TICK/"^s#get_list_coords^"\n");flush outchan) 
			s#get_usrs_socks;
	done
	


let lanche_session s = 
	print_endline "La session commence !";
	s#session_lauched;

	List.iter (fun (usr,sock)->  let outchan = Unix.out_channel_of_descr sock in 
			output_string outchan ("SESSION/"^s#get_list_coords^"/"^s#get_coord_objectif^"\n");flush outchan) 
			s#get_usrs_socks ;

	
		
	(* let t = Thread.create synchronisation s in () *)
	

	
	(* get_list_coords (s#get_usrs);
	print_endline ("nombre de usr dans la session est "^(string_of_int (s # size_users)));
 *)
	




