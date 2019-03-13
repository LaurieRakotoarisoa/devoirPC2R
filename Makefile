OCAMLC = ocamlc	
server:usr server.ml
	$(OCAMLC) -thread unix.cma threads.cma User.cmo server.ml -o server
usr:User.ml
	$(OCAMLC) -c User.ml

