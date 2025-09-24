🌀 Labyrinth Game

A 2D maze adventure built in Java with database integration. Your mission is simple but dangerous:
Start at the portal in the top-left corner.
Collect all scoring gems on the path.
Reach the portal in the bottom-right corner.
Only when all gems are collected can you activate the exit portal and win the game.

🎮 Gameplay Features

Random Labyrinths – Each game generates a new random maze layout.
New Challenge on Death – If you die by stepping into lava, a brand-new labyrinth is generated.
Deadly Lava – One wrong step on lava tiles, and you’re done.
Gem Collecting – You must collect every gem scattered on the pathway before escaping.
Pathway Navigation – Movement is restricted to safe pathways only.

💾 Account System & Saving

The game requires you to sign up and then log in before playing.
Pause & Save – You can pause the game and save progress to your account.
Resume – Next login, you can resume where you left off.
Saved Game Loading – Option to load your last saved game session at any time.
Database-Backed – Player state, maps, scores, and progress are stored securely in a SQLite database.

🏆 Scoring

Gems increase your score.
Keeps track of how many times you have beaten the game.

⚙️ Tech Stack

Java (Swing GUI, JDBC for database connectivity)
MySQL / SQLite (depending on setup) for storing:
User accounts
Game progress
Maps and tiles
Scores

🚀 How to Play

Sign Up with a new account.
Log In to start your adventure.
Explore the labyrinth, collect all gems, avoid lava, and reach the exit portal.
Pause & Save your progress if you want to continue later.
Resume next time by loading in again or starting a new game.

