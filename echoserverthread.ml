(* compilation: ocamlc -thread unix.cma threads.cma echoserverthread.ml -o echoserver *)

open User
let list_usr = ref [];;
let list_sock = ref [];;
let rec print_list = function 
[] -> ()
| e::l -> print_string e ;print_string " " ; print_list l;;

let append l a = 
	l:=!l @ [a];;

let creer_serveur port max_con =
  let sock = Unix.socket Unix.PF_INET Unix.SOCK_STREAM 0
  and addr = Unix.inet_addr_of_string "127.0.0.1"
  in
    Unix.bind sock (Unix.ADDR_INET(addr, port));
    Unix.listen sock max_con ;
    sock;;

let serveur_process sock service =
  while true do
    let (s, caller) = Unix.accept sock
    in
      ignore(Thread.create service (Unix.in_channel_of_descr s,Unix.out_channel_of_descr s));
	append list_sock s 
  done;; 

let signal_tout usr= 
	List.iter (fun s-> let outchan = Unix.out_channel_of_descr s in 
				output_string outchan ("NEWPLAYER/"^usr);flush outchan) !list_sock;;
	
let service_projet chans = 
	let inchan = fst chans
	and outchan = snd chans
	in 
		while true do
			let line = input_line inchan
			in (*CONNECT user*) 
				let s = (String.split_on_char '/' line) in
					 match s with
					|[]->()
					|h::l-> if (String.equal h "CONNECT") then 
						let nom = (List.hd l) in 
							if List.mem  nom !list_usr then   ( output_string outchan ("DENIED\n");flush outchan ) 
								else (

								let usr = new user nom in (
									append list_usr nom;signal_tout nom;
									print_endline ("Nouvelle connexion d’un client nomme "^nom) );
									output_string outchan ("WELCOME/attente/"^usr # get_score_str^"/"^usr#get_coord^"\n"); flush outchan

								)
							else if (String.equal h "EXIT") then 
								let nom = (List.hd l) in if List.mem  nom !list_usr then (
										list_usr := List.filter (fun x -> not (String.equal nom x) ) !list_usr;
										print_endline ("Deconnexion de "^nom);

									)
				
		done;;
										
let main () =
  let port = int_of_string Sys.argv.(1)
  in
  let sock = creer_serveur port 4 ;
  in
    serveur_process sock service_projet;;

let _ = main ();;
