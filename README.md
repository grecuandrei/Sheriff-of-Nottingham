				                           Copyright @ 2019 Grecu Andrei-George, All rights reserved
                                    ██████████                                    ██████████                                    
                                  ██████████    ░░                                  ██████████                                  
                  ░░              ██████████  ████████                    ████████  ██████████                                  
    ░░      ░░    ░░              ████████  ██        ████            ████        ▓▓  ████████          ░░              ░░░░░░  
            ░░░░                ██████████░░▒▒            ████    ████            ▒▒░░██████████                ░░░░            
                                ██████████    ██████████      ████      ██████████    ██████████                ░░              
                                ██████████  ██████▒▒▒▒████            ████▒▒▒▒██████  ██████████                                
                              ████████████  ████▒▒████▒▒██            ██▒▒████▒▒████  ████████████                              
                              ██████████▒▒▒▒  ████▒▒▒▒████  ██    ██  ████▒▒▒▒████  ▒▒▒▒██████████                              
                              ██████████    ▒▒  ████████    ██    ██    ████████  ▒▒    ██████████                              
                              ████████▒▒▒▒    ▒▒            ██    ██            ▒▒    ▒▒▒▒████████                              
                              ████████    ▒▒  ▒▒            ██    ██            ▒▒  ▒▒    ████████                              
                                ██████▒▒▒▒  ▒▒  ▒▒          ██    ██          ▒▒  ▒▒  ▒▒▒▒████████                              
                              ████████▒▒▒▒  ▒▒  ▒▒          ██    ██          ▒▒  ▒▒  ▒▒▒▒████████                              
                                ████████  ▒▒    ▒▒        ██        ██        ▒▒    ▒▒  ████████                                
                                ████████████  ▒▒          ██        ██          ▒▒  ████████████                                
                                ██████████████    ▒▒▒▒  ██            ██  ▒▒▒▒    ██████████████                                
                                ██████████████      ▒▒▒▒▒▒████████████▒▒▒▒▒▒      ██████████████                                
                                  ██████████████      ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒      ██████████████                                  
                                  ██████████████      ██                ██      ██████████████                                  
                                    ██  ████  ██      ██                ██      ██  ████  ██                                    
                                                ██    ██                ██    ██                                                
    ░░      ░░    ░░    ░░░░        ░░  ░░░░░░  ██    ██                ██    ██  ░░  ░░        ░░  ░░░░░░  ░░    ░░  ░░    ░░  
                                              ░░  ██  ██                ██  ██                                  ░░              
                                                    ████                ████                                                    
                                                        ██            ██                                                        
                                                          ████████████                                                          

		Sheriff of Nottingham - A variation of the base game implemented in java

		Text : http://elf.cs.pub.ro/poo/teme/tema

	Structure:
		common package:
		Constants - all constants used
		ChainedComparator - for Players
		ChainedComparator2 - for Goods
		CoinsComparator - for Players coins
		ProfitComparator - for Goods profit
		IdComparator - for Goods id
		OrdComparator - for Players Order
		

		main package:
		Main -> Game -> SubRound -> Player -> Basic
						   \-> Greedy
						    \-> Bribe

	Explanation:
		Game: Takes the input and processes it. Creates the players according to the input.
			
			Starts the game: For every round, do a new subround for every player to be sheriff.
			
			After the rounds finnished, we transform the illegals in their bonus, add profits
			and compute the king and queen for every good.
			
			Sort the players and display the leaderboard.

		Subround: At the start of the subround, every player needs to take his bag and declare
			the bag to sheriff according to his strategy, or set the sheriff.
			
			Inspection is used a single tie for bribed sheriff (as he checks up to two players
			takes bribe from others), or for every player besides the sheriff.
			 
			Clear the hands and the bag for the sheriff for every player as the subround has finished.

		Player: The class implements the bag method that adds cards from the pile in hand.
			
			The declaration method is used to build the bag (BagToSheriff) that gets to be checked by the sheriff and
			the declaration (BagDeclaration), for the basic startegy.

			The inspection method is for every basic sheriff and implements the method trough which he inspects the
			other players.

			ProfitAdd method is used to add the profit for the goods that every player has on the stall, called by Game.

		Basic: Extends the Player class and has all the implementation already set,
			but in the constructor, the name is set accordingly.

		Greedy: Extends the Player class and has the name set accordingly in the constructor.
			
			The declaration method from Player is Overriden. It calls the declaration of the Basic and
			then it implements the Greedy startegy mention in the text.

			The inspection method from Player is Overriden. Checks if a player has bribe, if yes, then
			it takes the bribe, else inspects the player with the basic strategy.
		
		Bribe: Extends the Player class and has the name set accordingly in the constructor.
			
			The declaration methos is Overriden from Player and it is implementing the strategy described in the text.

			The inspection method from Player is Overriden. It computes the left and right neighbours for the sheriff
			and then applies the inspection method based on the basic strategy. If there are more than two players,
			from the rest of them it takes the bribe, if they have.
                                                                                                                   
