(* compilation: ocamlc -thread unix.cma threads.cma echoserverthread.ml -o echoserver *)
open User
open Service

let list_usr_noms = ref [];;
let list_usr_sock = ref [];;
let list_usr = ref [];;
let session_courant = new session [];; 

let rebour = ref true;;

let creer_serveur port max_con =
  let sock = Unix.socket Unix.PF_INET Unix.SOCK_STREAM 0
  and addr = Unix.inet_addr_of_string "127.0.0.1"
  in
    Unix.setsockopt sock Unix.SO_REUSEADDR true;
    Unix.bind sock (Unix.ADDR_INET(addr, port));
    Unix.listen sock max_con ;
    sock;;

let serveur_process sock service =
  while true do
    let (s, caller) = Unix.accept sock
    in
      ignore(Thread.create service (s));
	
  done;; 

let signal_tout_sauf nomUsr message= 
	List.iter (fun (usr,sock)-> if not (String.equal usr nomUsr) then let outchan = Unix.out_channel_of_descr sock in 
			output_string outchan (message^"/"^nomUsr^"\n");flush outchan) !list_usr_sock;;
	
let service_projet socket = 
	let client_connecte = ref "no connect" in 
	let inchan = Unix.in_channel_of_descr socket
	and outchan = Unix.out_channel_of_descr socket
	in 
		while true do
			let line = input_line inchan
			in (*CONNECT user*) 
				let s = (String.split_on_char '/' line) in
					 match s with
					|[]->()
					|h::l-> if (String.equal h "CONNECT") then 
						let nom = (List.hd l) in 
							if List.mem  nom !list_usr_noms then   ( output_string outchan ("DENIED\n");flush outchan ) 
								else (

								let usr = new user nom in (
									client_connecte:=nom;
									append list_usr_noms nom;
									append list_usr_sock (nom,socket);
									append list_usr usr;
									session_courant#connect usr (nom,socket);
									signal_tout_sauf nom "NEWPLAYER";
									print_endline ("Nouvelle connexion d’un client nomme "^usr#get_nom) );
									session_courant#init_obstacles;
									session_courant#send_welcome;
									if !rebour then ( rebour := false ;
										let t = Thread.create compte_a_rebours () in Thread.join t;
										lanche_session session_courant ;
										)
								)
							else if (String.equal h "EXIT") then (
								let nom = (List.hd l) in if List.mem  nom !list_usr_noms then (
										list_usr_noms := List.filter (fun x -> not (String.equal nom x) ) !list_usr_noms;
										signal_tout_sauf nom "PLAYERLEFT";
										list_usr_sock :=  List.filter (fun (x,sock) -> not (String.equal nom x) ) !list_usr_sock;
										session_courant#deconnect nom;
										print_endline ("Deconnexion de "^nom);

									)
								)
							else if (String.equal h "NEWPOS") then (
								let coord = (List.hd l) in 
								let len = String.length coord and index_Y = String.index coord 'Y' in 
								let coordX = String.sub coord 1 (index_Y-1) 
								and coordY = String.sub coord (index_Y+1) (len-index_Y-1) in 
								let nom = !client_connecte and posX = float_of_string coordX and posY = float_of_string coordY in 
									let usr = session_courant#get_usr_par_nom nom in
								(
									usr#set_pos posX posY; (*mise à jour la position du véhicule*)

									(*détecter si il a touché objectif*)
									session_courant#touche_obj usr ;
									
									session_courant#detect_touche_obstacles usr
			
								);

							)else if (String.equal h "TIR") then(

									let nom = !client_connecte in 
									let usr = session_courant#get_usr_par_nom nom in 
									let balle = usr# generer_balle in 
									session_courant#ajout_balle balle ;

									
								

							)


							else if(String.equal h "NEWCOM") then (
								let cmd = (List.hd l) in let len = String.length cmd and 
								index_T = String.index cmd 'T' in 
								let cmd_a = String.sub cmd 1 (index_T-1) 
								and cmd_t = String.sub cmd (index_T+1) (len-index_T-1) in 
								let nom = !client_connecte and a = float_of_string cmd_a and t = int_of_string cmd_t in
									let usr = session_courant#get_usr_par_nom nom in
									( usr#gerer_cmd a t )


							)else if (String.equal h "ENVOI") then (
								let msg = (List.hd l) in 
								let nom = !client_connecte  in
								session_courant#envoi_msg nom msg

							)else if (String.equal h "PENVOI") then (
								let username = (List.hd l) in 
								let msg = (List.hd (List.tl l)) in 
								session_courant#envoi_pmessage username msg
								
							)

							;
		if (session_courant#est_fin_session) then ( session_courant#fin_de_session;
												let t = Thread.create compte_a_rebours () in Thread.join t;
												lanche_session session_courant 
											)
		done;;
										
let main () =
  let port = int_of_string Sys.argv.(1)
  in
  let sock = creer_serveur port 4 ;
  in
    serveur_process sock service_projet;;

let _ = main ();;
