class bombe x y vX vY = 
	object(self)
		val mutable coordX = x
		val mutable coordY = y
		val vitesseX = 2. *. vX
		val vitesseY = 2. *. vY
		val demi_largeur = 450.0
		val demi_hauteur = 200.0
		val visible = true
		method  get_coord = "X"^(string_of_float coordX)^"Y"^(string_of_float coordY)
		method get_x = coordX
		method get_y = coordY
		method bombe_move = 
						 coordX <- (let x = coordX +. vitesseX in if (compare (floor x) demi_largeur) >= 0  then (-.demi_largeur) 
								else if (compare (floor x) (-.demi_largeur)) <= 0 then demi_largeur else x);
						 coordY <- (let y = coordY +. vitesseY in if (compare (floor y) demi_hauteur) >=0 then (-.demi_hauteur)
						 		else if (compare (floor y)  (-. demi_hauteur))<=0 then demi_hauteur else y )

		method est_visible = visible
	end  