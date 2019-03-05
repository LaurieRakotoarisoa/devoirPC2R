(* compilation: ocamlc -thread unix.cma threads.cma echoserverthread.ml -o echoserver *)
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
						let usr = (List.hd l) in 
							if List.mem  usr !list_usr then   ( output_string outchan ("DENIED\n");flush outchan ) 
								else (
								append list_usr usr;signal_tout usr;
								print_endline ("Nouvelle connexion d’un client nomme "^usr) )

								
				
		done;;
										
let main () =
  let port = int_of_string Sys.argv.(1)
  in
  let sock = creer_serveur port 4 ;
  in
    serveur_process sock service_projet;;

let _ = main ();;
