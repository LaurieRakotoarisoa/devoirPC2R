open User

let rec print_list = function 
[] -> ()
| e::l -> print_string e ;print_string " " ; print_list l

let append l a = 
	l:=!l @ [a]

let compte_a_rebours () = 
	for i = 20 downto 1 do
		Thread.delay 1.0;
		print_endline ("Il reste "^(string_of_int i)^" secondes  avant de lancer la session ");

	done

let rec get_list_coords  l_usr= 
	match l_usr with
	[]-> ()
	|e::l -> print_endline (e # get_nom_and_coord);
			get_list_coords l 
		

let lanche_session s list_usr_sock= 
	print_endline "La session commence !";


	List.iter (fun (usr,sock)->  let outchan = Unix.out_channel_of_descr sock in 
			output_string outchan ("SESSION"^get_list_coords^"/"^s#get_coord_objectif);flush outchan) 
			!list_usr_sock;;
	
	(* get_list_coords (s#get_usrs);
	print_endline ("nombre de usr dans la session est "^(string_of_int (s # size_users)));
 *)
	




