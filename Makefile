OCAMLC = ocamlc	
server:usr service server.ml
	$(OCAMLC) -thread unix.cma threads.cma User.cmo Service.cmo server.ml -o server
usr:User.ml
	$(OCAMLC) -c User.ml
service : Service.ml
	$(OCAMLC) -c -thread unix.cma threads.cma Service.ml

