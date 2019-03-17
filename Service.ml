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
	|e::l -> let nom = e # get_nom in (
			print_endline nom;
			 print_endline (nom^":"^e#get_coord^"|")); 
			 get_list_coords l


let session l = 
	print_endline ("nombre de usr dans la session est "^(string_of_int (List.length l)));
	print_endline "La session commence !";
	get_list_coords l;
	




