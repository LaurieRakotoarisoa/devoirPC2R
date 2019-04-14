OCAMLC = ocamlc	
all: server clean
server:bombe usr service server.ml
	$(OCAMLC) -thread unix.cma threads.cma Bombe.cmo User.cmo Service.cmo  server.ml -o server
usr: bombe User.ml
	$(OCAMLC) -c -thread unix.cma threads.cma  Bombe.cmo User.ml
service : Service.ml
	$(OCAMLC) -c -thread unix.cma threads.cma Service.ml
bombe : Bombe.ml
	$(OCAMLC) -c  Bombe.ml
clean: 
	rm -f *.cmo
	rm -f *.cmi
