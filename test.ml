let rec get_texte = function 
[] -> ""
| e::l -> (e ^ " | " ^ get_texte l);;


let a = ["hello"; " world"; " ning"];;
print_endline (get_texte a);;
