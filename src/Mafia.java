import java.util.*;

public class Mafia {
	static int num_player;
	static Random rand = new Random();

	static class soft {
		int num_mafia = num_player / 5;
		int num_detective = num_player / 5;
		int num_healer = Math.max(1, num_player / 10);
		int num_commoner = num_player - (num_mafia + num_detective + num_healer);
		int round = 1;
		Scanner s;

		soft(Scanner s) {
			this.s = s;
		}

		void setPlayer(Player[] playing_player, int choice) {
			int count_mafia = num_mafia;
			int count_detective = num_detective;
			int count_healer = num_healer;
			int count_commoner = num_commoner;
			if (choice == 1)
				count_mafia -= 1;
			else if (choice == 2)
				count_detective -= 1;
			else if (choice == 3)
				count_healer -= 1;
			else
				count_commoner -= 1;
			while (count_mafia > 0) {
				int place = rand.nextInt(num_player) + 1;
				if (playing_player[place] == null) {
					playing_player[place] = new mafiaPlayer(place);
					count_mafia -= 1;
					if (choice == 1)
						System.out.print("[Player" + place + "] ");
				}
			}
			while (count_detective > 0) {
				int place = rand.nextInt(num_player) + 1;
				if (playing_player[place] == null) {
					playing_player[place] = new Detective(place);
					count_detective -= 1;
					if (choice == 2)
						System.out.print("[Player" + place + "] ");
				}
			}
			while (count_healer > 0) {
				int place = rand.nextInt(num_player) + 1;
				if (playing_player[place] == null) {
					playing_player[place] = new Healer(place);
					count_healer -= 1;
				}
				if (choice == 3)
					System.out.print("[Player" + place + "] ");
			}
			while (count_commoner > 0) {
				int place = rand.nextInt(num_player) + 1;
				if (playing_player[place] == null) {
					playing_player[place] = new Commoner(place);
					count_commoner -= 1;
					if (choice == 4)
						System.out.print("[Player" + place + "] ");
				}
			}
		}

		void showAlive(Player[] playing_player) {
			System.out.println("Round " + round + ":");
			System.out.print((num_mafia + num_detective + num_healer + num_commoner) + " players are remaining: ");
			for (int i = 1; i < num_player + 1; i++) {
				if (playing_player[i] != null)
					System.out.print("Player" + i + ", ");
			}
			System.out.println("are alive.");
		}

		void dispEnd(Player[] cp, int user_id) {
			for (int i = 1; i < num_player + 1; i++) {
				if (cp[i].getType() == "mafiaPlayer") {
					if (user_id == i)
						System.out.print("Player" + i + "[User] ");
					else
						System.out.print("Player" + i + " ");
				}
			}
			System.out.println("were mafias.");
			for (int i = 1; i < num_player + 1; i++) {
				if (cp[i].getType() == "Detective") {
					if (user_id == i)
						System.out.print("Player" + i + "[User] ");
					else
						System.out.print("Player" + i + " ");
				}
			}
			System.out.println("were Detectives.");
			for (int i = 1; i < num_player + 1; i++) {
				if (cp[i].getType() == "Healer") {
					if (user_id == i)
						System.out.print("Player" + i + "[User] ");
					else
						System.out.print("Player" + i + " ");
				}
			}
			System.out.println("were Healer.");
			for (int i = 1; i < num_player + 1; i++) {
				if (cp[i].getType() == "Commoner") {
					if (user_id == i)
						System.out.print("Player" + i + "[User] ");
					else
						System.out.print("Player" + i + " ");
				}
			}
			System.out.println("were Commoners.");
		}

		int votingResult(HashMap<Integer, Integer> voteData) {
			ArrayList<Map.Entry<Integer, Integer>> list = new ArrayList<>(voteData.entrySet());

			Collections.sort(list, new Comparator<Map.Entry<Integer, Integer>>() {
				@Override
				public int compare(Map.Entry<Integer, Integer> o1, Map.Entry<Integer, Integer> o2) {
					return o2.getValue().compareTo(o1.getValue());
				}
			});
			int counter = 0;
			int voteout_key = -1;
			int voteout_value = -1;
			HashMap<Integer, Integer> temp = new HashMap<>();
			for (Map.Entry<Integer, Integer> aa : list) {
				temp.put(aa.getKey(), aa.getValue());
//	            System.out.println("ID: "+aa.getKey()+" Vote: "+aa.getValue());
				if (counter == 0) {
					voteout_key = aa.getKey();
					voteout_value = aa.getValue();
					counter++;
					continue;
				}
				if (counter > 0 && voteout_value == aa.getValue()) {
					voteout_key = -1;
					voteout_value = -1;
				}
			}
			return voteout_key;
		}

		void upgradVal(Player pp) {
			if (pp.getType() == "mafiaPlayer")
				num_mafia -= 1;
			else if (pp.getType() == "Detective")
				num_detective -= 1;
			else if (pp.getType() == "Healer")
				num_healer -= 1;
			else
				num_commoner -= 1;
		}

		void manageTask(int choice) {
			int user_id;
			Player[] playing_player = new Player[num_player + 1];

			if (choice == 1) {
				user_id = rand.nextInt(num_player) + 1;
				playing_player[user_id] = new mafiaPlayer(user_id);
				System.out.println("You are Player" + user_id + ".");
				System.out.print("You are a Mafia. Other mafias are:");
				setPlayer(playing_player, 1);
				Player[] copy_playing_player = playing_player.clone();
				System.out.println();
				int user_target = -1;
				while (!(num_mafia == 0 || (num_mafia == (num_detective + num_healer + num_commoner)))) {
					showAlive(playing_player);
					if (playing_player[user_id] != null) {
						System.out.print("Choose a target: ");
						user_target = s.nextInt();
					}
					int detective_target = -1;
					int healer_target = -1;
					if (playing_player[user_id] != null) {
						voteHandler<mafiaPlayer> nik = new voteHandler<mafiaPlayer>(new mafiaPlayer(6), s);
						user_target = nik.voteSet(playing_player, user_target);
					} else {
						voteHandler<mafiaPlayer> nik = new voteHandler<mafiaPlayer>(new mafiaPlayer(6), s);
						user_target = nik.randomVoteSet(playing_player);
						System.out.println("Other mafias had choosen target");
					}
					if (num_detective != 0) {
						voteHandler<Detective> nik2 = new voteHandler<Detective>(new Detective(6), s);
						detective_target = nik2.randomVoteSet(playing_player);
						//System.out.println("target choosen by detective" + detective_target);
					}
					System.out.println("Detectives have chosen a player to test.");
					int target_hp = playing_player[user_target].getHP();
					//System.out.println("target hp before divide is " + target_hp);
					int tot_mafia_hp = 0;
					int num_unused_mafia = num_mafia;
					PriorityQueue<Player> pq = new PriorityQueue<Player>(num_mafia, new mafiaPlayerComparator());

					for (int i = 1; i < num_player + 1; i++) {
						if (playing_player[i] != null && playing_player[i].getType().equals("mafiaPlayer")) {
							tot_mafia_hp += playing_player[i].getHP();
							pq.add(playing_player[i]);
						}
					}
					int dif_single = 0;
					if (tot_mafia_hp >= target_hp) {
						//System.out.println("IN");
						dif_single = tot_mafia_hp - playing_player[user_target].getHP();
						playing_player[user_target].setHP(0);
					} else
						playing_player[user_target].setHP(target_hp - tot_mafia_hp);

					int red_hp = target_hp / num_unused_mafia;
					//System.out.println("initial reducng hp" + red_hp);
					int carry = 0;
					if (pq.size() == 1) {
						//System.out.println("In for 1");
						Player work = pq.poll();
						if (playing_player[user_target].getHP() == 0)
							work.setHP(work.getHP() - dif_single);
						else
							work.setHP(0);
					} else {
						while (!pq.isEmpty()) {
							Player work = pq.poll();
							//System.out.println("HP of a mafia " + work.getHP());
							if (work.getHP() >= (red_hp + carry)) {
								work.setHP(work.getHP() - (red_hp + carry));
							} else {
								work.setHP(0);
								carry = red_hp - work.getHP();
								carry /= pq.size();
							}
						}
					}
					if (num_healer != 0) {
						healer_target = rand.nextInt(num_player) + 1;
						//System.out.println("target choosen by healer" + healer_target);
						while (playing_player[healer_target] == null)
							healer_target = rand.nextInt(num_player) + 1;
						playing_player[healer_target].upgradHP();
					}
					System.out.println("Healers have chosen someone to heal.\n" + "--End of actions--");
					if (playing_player[user_target].getHP() == 0) {
						System.out.println("Player" + user_target + " has died");
						upgradVal(playing_player[user_target]);
						playing_player[user_target] = null;
					} else
						System.out.println("No one died");
					if (detective_target != -1 && playing_player[detective_target] != null
							&& playing_player[detective_target].getType().equals("mafiaPlayer")) {
						upgradVal(playing_player[detective_target]);
						playing_player[detective_target] = null;
						System.out.println("Player" + detective_target + " has been voted out");
					} else {
						HashMap<Integer, Integer> hm = new HashMap<>();
						int res=0;
						do {
						if (!(playing_player[user_id] == null)) {

							System.out.print("Select a person to vote out: ");
							int vote = s.nextInt();
							while ((!(vote>0&&vote<playing_player.length))||playing_player[vote] == null) {
								System.out.print("You cannot vote this player\n"
										+ "Choose another payer: ");
								vote = s.nextInt();
							}
							hm.put(vote, 1);
						}
							for (int ll = 1; ll < num_player + 1; ll++) {
								if (playing_player[ll] != null && ll != user_id) {
									int pv = rand.nextInt(num_player) + 1;
									while (playing_player[pv] == null)
										pv = rand.nextInt(num_player) + 1;
									if (hm.containsKey(pv)) {
										int vl = hm.get(pv);
										hm.put(pv, vl + 1);
									} else
										hm.put(pv, 1);
								}
							}
							res=votingResult(hm);
						}while(res==-1);
						System.out.println("Player" + res + " has been voted out.");
						upgradVal(playing_player[res]);
						playing_player[res] = null;

					}
					System.out.println("--End of Round" + round + "--");
					round++;
				}
				System.out.println("Game Over");
				if (num_mafia == 0)
					System.out.println("The Mafias have lost.");
				else
					System.out.println("The Mafias have won.");
				dispEnd(copy_playing_player, user_id);
			} else if (choice == 2) {
				user_id = rand.nextInt(num_player) + 1;
				playing_player[user_id] = new Detective(user_id);
				System.out.println("You are Player" + user_id + ".");
				System.out.print("You are a Detective. Other detectives are:");
				setPlayer(playing_player, 2);
				Player[] copy_playing_player = playing_player.clone();
				System.out.println();
				int user_target = -1;
				while (!(num_mafia == 0 || (num_mafia == (num_detective + num_healer + num_commoner)))) {
					showAlive(playing_player);
					int mafia_target = -1;
					int healer_target = -1;
					voteHandler<mafiaPlayer> nik = new voteHandler<mafiaPlayer>(new mafiaPlayer(6), s);
					mafia_target = nik.randomVoteSet(playing_player);
					//System.out.println("target choosen by mafia" + mafia_target);
					System.out.println("Mafias have chosen a player to test.");
					if (playing_player[user_id] != null) {
						System.out.print("Choose a target: ");
						user_target = s.nextInt();
					}
					if (playing_player[user_id] != null) {
						voteHandler<Detective> nik2 = new voteHandler<Detective>(new Detective(6), s);
						user_target = nik2.voteSet(playing_player, user_target);
					} else {
						voteHandler<Detective> nik2 = new voteHandler<Detective>(new Detective(6), s);
						user_target = nik2.randomVoteSet(playing_player);
						System.out.println("Other detectives had choosen target");
						//System.out.println("target choosen by detective" + user_target);
					}
					if (!(playing_player[user_target].getType().equals("mafiaPlayer")))
						System.out.println("Player" + user_target + " is not a mafia");
					else
						System.out.println("Player" + user_target + " is a mafia");
					int target_hp = playing_player[mafia_target].getHP();
					//System.out.println("target hp before divide is " + target_hp);
					int tot_mafia_hp = 0;
					int num_unused_mafia = num_mafia;
					PriorityQueue<Player> pq = new PriorityQueue<Player>(num_mafia, new mafiaPlayerComparator());

					for (int i = 1; i < num_player + 1; i++) {
						if (playing_player[i] != null && playing_player[i].getType().equals("mafiaPlayer")) {
							tot_mafia_hp += playing_player[i].getHP();
							pq.add(playing_player[i]);
						}
					}
					int dif_single = 0;
					if (tot_mafia_hp >= target_hp) {
						//System.out.println("IN");
						dif_single = tot_mafia_hp - playing_player[mafia_target].getHP();
						playing_player[mafia_target].setHP(0);
					} else
						playing_player[mafia_target].setHP(target_hp - tot_mafia_hp);

					int red_hp = target_hp / num_unused_mafia;
					//System.out.println("initial reducng hp" + red_hp);
					int carry = 0;
					if (pq.size() == 1) {
						//System.out.println("In for 1");
						Player work = pq.poll();
						if (playing_player[mafia_target].getHP() == 0)
							work.setHP(work.getHP() - dif_single);
						else
							work.setHP(0);
					} else {
						while (!pq.isEmpty()) {
							Player work = pq.poll();
							//System.out.println("HP of a mafia " + work.getHP());
							if (work.getHP() >= (red_hp + carry)) {
								work.setHP(work.getHP() - (red_hp + carry));
							} else {
								work.setHP(0);
								carry = red_hp - work.getHP();
								carry /= pq.size();
							}
						}
					}
					if (num_healer != 0) {
						healer_target = rand.nextInt(num_player) + 1;
						//System.out.println("target choosen by healer" + healer_target);
						while (playing_player[healer_target] == null)
							healer_target = rand.nextInt(num_player) + 1;
						playing_player[healer_target].upgradHP();
					}
					System.out.println("Healers have chosen someone to heal.\n" + "--End of actions--");
					if (playing_player[mafia_target].getHP() == 0) {
						System.out.println("Player" + mafia_target + " has died");
						upgradVal(playing_player[mafia_target]);
						playing_player[mafia_target] = null;
					} else
						System.out.println("No one died");
					if (user_target != -1 && playing_player[user_target] != null
							&& playing_player[user_target].getType().equals("mafiaPlayer")) {
						upgradVal(playing_player[user_target]);
						playing_player[user_target] = null;
						System.out.println("Player" + user_target + " has been voted out");
					} else {
						HashMap<Integer, Integer> hm = new HashMap<>();
						int res=0;
						do {
						if (!(playing_player[user_id] == null)) {
							System.out.print("Select a person to vote out: ");
							int vote = s.nextInt();
							while ((!(vote>0&&vote<playing_player.length))||playing_player[vote] == null) {
								System.out.print("You cannot vote this player.\n"
										+ "Choose another payer: ");
								vote = s.nextInt();
							}
							hm.put(vote, 1);
						}
							for (int ll = 1; ll < num_player + 1; ll++) {
								if (playing_player[ll] != null && ll != user_id) {
									int pv = rand.nextInt(num_player) + 1;
									while (playing_player[pv] == null)
										pv = rand.nextInt(num_player) + 1;
									if (hm.containsKey(pv)) {
										int vl = hm.get(pv);
										hm.put(pv, vl + 1);
									} else
										hm.put(pv, 1);
								}
							}
							res=votingResult(hm);
						}while(res==-1);
						System.out.println("Player" + res + " has been voted out.");
						upgradVal(playing_player[res]);
						playing_player[res] = null;

					
					}
					System.out.println("--End of Round" + round + "--");
					round++;
				}
				System.out.println("Game Over");
				if (num_mafia == 0)
					System.out.println("The Mafias have lost.");
				else
					System.out.println("The Mafias have won.");
				dispEnd(copy_playing_player, user_id);
			} else if (choice == 3) {
				user_id = rand.nextInt(num_player) + 1;
				playing_player[user_id] = new Healer(user_id);
				System.out.println("You are Player" + user_id + ".");
				System.out.print("You are a Healer. Other healers are:");
				setPlayer(playing_player, 3);
				Player[] copy_playing_player = playing_player.clone();
				System.out.println();
				int user_target = -1;
				while (!(num_mafia == 0 || (num_mafia == (num_detective + num_healer + num_commoner)))) {
					showAlive(playing_player);
					int mafia_target = -1;
					int detective_target=-1;
					voteHandler<mafiaPlayer> nik = new voteHandler<mafiaPlayer>(new mafiaPlayer(6), s);
					mafia_target = nik.randomVoteSet(playing_player);
					//System.out.println("target choosen by mafia" + mafia_target);
					System.out.println("Mafias have chosen a player to test.");
					if (num_detective != 0) {
						voteHandler<Detective> nik2 = new voteHandler<Detective>(new Detective(6), s);
						detective_target = nik2.randomVoteSet(playing_player);
						//System.out.println("target choosen by detective" + detective_target);
					}
					System.out.println("Detectives have chosen a player to test.");

					int target_hp = playing_player[mafia_target].getHP();
					//System.out.println("target hp before divide is " + target_hp);
					int tot_mafia_hp = 0;
					int num_unused_mafia = num_mafia;
					PriorityQueue<Player> pq = new PriorityQueue<Player>(num_mafia, new mafiaPlayerComparator());

					for (int i = 1; i < num_player + 1; i++) {
						if (playing_player[i] != null && playing_player[i].getType().equals("mafiaPlayer")) {
							tot_mafia_hp += playing_player[i].getHP();
							pq.add(playing_player[i]);
						}
					}
					int dif_single = 0;
					if (tot_mafia_hp >= target_hp) {
						//System.out.println("IN");
						dif_single = tot_mafia_hp - playing_player[mafia_target].getHP();
						playing_player[mafia_target].setHP(0);
					} else
						playing_player[mafia_target].setHP(target_hp - tot_mafia_hp);

					int red_hp = target_hp / num_unused_mafia;
					//System.out.println("initial reducng hp" + red_hp);
					int carry = 0;
					if (pq.size() == 1) {
						//System.out.println("In for 1");
						Player work = pq.poll();
						if (playing_player[mafia_target].getHP() == 0)
							work.setHP(work.getHP() - dif_single);
						else
							work.setHP(0);
					} else {
						while (!pq.isEmpty()) {
							Player work = pq.poll();
							//System.out.println("HP of a mafia " + work.getHP());
							if (work.getHP() >= (red_hp + carry)) {
								work.setHP(work.getHP() - (red_hp + carry));
							} else {
								work.setHP(0);
								carry = red_hp - work.getHP();
								carry /= pq.size();
							}
						}
					}
					if (playing_player[user_id] != null) {
						System.out.print("Choose a player to heal: ");
						user_target = s.nextInt();
						while(playing_player[user_target]==null)
						{
							System.out.print("You cannot choose this player\n"+"Choose again: ");
							user_target = s.nextInt();
						}
						playing_player[user_target].upgradHP();
					}
					if (num_healer != 0 && playing_player[user_id] == null) {
						user_target = rand.nextInt(num_player) + 1;
						//System.out.println("target choosen by healer" + user_target);
						while (playing_player[user_target] == null)
							user_target = rand.nextInt(num_player) + 1;
						playing_player[user_target].upgradHP();
						System.out.println("Other Healers have chosen someone to heal.");
					}
					System.out.println("--End of actions--");
					if (playing_player[mafia_target].getHP() == 0) {
						System.out.println("Player" + mafia_target + " has died");
						upgradVal(playing_player[mafia_target]);
						playing_player[mafia_target] = null;
					} else
						System.out.println("No one died");
					if (detective_target != -1 && playing_player[detective_target] != null
							&& playing_player[detective_target].getType().equals("mafiaPlayer")) {
						upgradVal(playing_player[detective_target]);
						playing_player[detective_target] = null;
						System.out.println("Player" + detective_target + " has been voted out");
					} else {
						HashMap<Integer, Integer> hm = new HashMap<>();
						int res=0;
						do {
						if (!(playing_player[user_id] == null)) {

							System.out.print("Select a person to vote out: ");
							int vote = s.nextInt();
							while ((!(vote>0&&vote<playing_player.length))||playing_player[vote] == null) {
								System.out.print("You cannot vote this player.\n"
										+ "Choose another payer: ");
								vote = s.nextInt();
							}
							hm.put(vote, 1);
						}
							for (int ll = 1; ll < num_player + 1; ll++) {
								if (playing_player[ll] != null && ll != user_id) {
									int pv = rand.nextInt(num_player) + 1;
									while (playing_player[pv] == null)
										pv = rand.nextInt(num_player) + 1;
									if (hm.containsKey(pv)) {
										int vl = hm.get(pv);
										hm.put(pv, vl + 1);
									} else
										hm.put(pv, 1);
								}
							}
							res=votingResult(hm);
						}while(res==-1);
						System.out.println("Player" + res + " has been voted out.");
						upgradVal(playing_player[res]);
						playing_player[res] = null;

					
					}
					System.out.println("--End of Round" + round + "--");
					round++;
				}
				System.out.println("Game Over");
				if (num_mafia == 0)
					System.out.println("The Mafias have lost.");
				else
					System.out.println("The Mafias have won.");
				dispEnd(copy_playing_player, user_id);
			} else {
				user_id = rand.nextInt(num_player) + 1;
				playing_player[user_id] = new Commoner(user_id);
				System.out.println("You are Player" + user_id + ".");
				System.out.print("You are a Commoner. Other commoners are:");
				setPlayer(playing_player, 4);
				Player[] copy_playing_player = playing_player.clone();
				System.out.println();
				while (!(num_mafia == 0 || (num_mafia == (num_detective + num_healer + num_commoner)))) {
					showAlive(playing_player);
					int mafia_target = -1;
					int detective_target=-1;
					int healer_target=-1;
					voteHandler<mafiaPlayer> nik = new voteHandler<mafiaPlayer>(new mafiaPlayer(6), s);
					mafia_target = nik.randomVoteSet(playing_player);
					//System.out.println("target choosen by mafia" + mafia_target);
					System.out.println("Mafias have chosen a player to test.");
					if (num_detective != 0) {
						voteHandler<Detective> nik2 = new voteHandler<Detective>(new Detective(6), s);
						detective_target = nik2.randomVoteSet(playing_player);
						//System.out.println("target choosen by detective" + detective_target);
					}
					System.out.println("Detectives have chosen a player to test.");

					int target_hp = playing_player[mafia_target].getHP();
					//System.out.println("target hp before divide is " + target_hp);
					int tot_mafia_hp = 0;
					int num_unused_mafia = num_mafia;
					PriorityQueue<Player> pq = new PriorityQueue<Player>(num_mafia, new mafiaPlayerComparator());

					for (int i = 1; i < num_player + 1; i++) {
						if (playing_player[i] != null && playing_player[i].getType().equals("mafiaPlayer")) {
							tot_mafia_hp += playing_player[i].getHP();
							pq.add(playing_player[i]);
						}
					}
					int dif_single = 0;
					if (tot_mafia_hp >= target_hp) {
						//System.out.println("IN");
						dif_single = tot_mafia_hp - playing_player[mafia_target].getHP();
						playing_player[mafia_target].setHP(0);
					} else
						playing_player[mafia_target].setHP(target_hp - tot_mafia_hp);

					int red_hp = target_hp / num_unused_mafia;
					//System.out.println("initial reducng hp" + red_hp);
					int carry = 0;
					if (pq.size() == 1) {
						//System.out.println("In for 1");
						Player work = pq.poll();
						if (playing_player[mafia_target].getHP() == 0)
							work.setHP(work.getHP() - dif_single);
						else
							work.setHP(0);
					} else {
						while (!pq.isEmpty()) {
							Player work = pq.poll();
							//System.out.println("HP of a mafia " + work.getHP());
							if (work.getHP() >= (red_hp + carry)) {
								work.setHP(work.getHP() - (red_hp + carry));
							} else {
								work.setHP(0);
								carry = red_hp - work.getHP();
								carry /= pq.size();
							}
						}
					}
					if (num_healer != 0) {
						healer_target = rand.nextInt(num_player) + 1;
						//System.out.println("target choosen by healer" + healer_target);
						while (playing_player[healer_target] == null)
							healer_target = rand.nextInt(num_player) + 1;
						playing_player[healer_target].upgradHP();
					}
					System.out.println("Healers have chosen someone to heal.\n"+"--End of actions--");
					if (playing_player[mafia_target].getHP() == 0) {
						System.out.println("Player" + mafia_target + " has died");
						upgradVal(playing_player[mafia_target]);
						playing_player[mafia_target] = null;
					} else
						System.out.println("No one died");
					if (detective_target != -1 && playing_player[detective_target] != null 
							&& playing_player[detective_target].getType().equals("mafiaPlayer")) {
						upgradVal(playing_player[detective_target]);
						playing_player[detective_target] = null;
						System.out.println("Player" + detective_target + " has been voted out");
					} else {
						HashMap<Integer, Integer> hm = new HashMap<>();
						int res=0;
						do {
						if (!(playing_player[user_id] == null)) {

							System.out.print("Select a person to vote out: ");
							int vote = s.nextInt();
							while ((!(vote>0&&vote<playing_player.length))||playing_player[vote] == null) {
								System.out.print("You cannot vote this player.\n"
										+ "Choose another payer: ");
								vote = s.nextInt();
							}
							hm.put(vote, 1);
						}
							for (int ll = 1; ll < num_player + 1; ll++) {
								if (playing_player[ll] != null && ll != user_id) {
									int pv = rand.nextInt(num_player) + 1;
									while (playing_player[pv] == null)
										pv = rand.nextInt(num_player) + 1;
									if (hm.containsKey(pv)) {
										int vl = hm.get(pv);
										hm.put(pv, vl + 1);
									} else
										hm.put(pv, 1);
								}
							}
							res=votingResult(hm);
						}while(res==-1);
						System.out.println("Player" + res + " has been voted out.");
						upgradVal(playing_player[res]);
						playing_player[res] = null;

					
					}
					System.out.println("--End of Round" + round + "--");
					round++;
				}
				System.out.println("Game Over");
				if (num_mafia == 0)
					System.out.println("The Mafias have lost.");
				else
					System.out.println("The Mafias have won.");
				dispEnd(copy_playing_player, user_id);
			
			}
		}

	}

	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		System.out.println("Welcome to Mafia");
		while (true) {
            System.out.print("Enter Numbers of Players: ");
            try {
                num_player = s.nextInt();
                s.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("incorrect input, try again");
                s.nextLine();
                continue;
            }
            if (num_player < 6) {
                System.out.println("Enter players no greater than or equal to 6");
            } else {
                break;
            }
        }
		System.out.println("Choose a Character\n" + "1) Mafia\n" + "2) Detective\n" + "3) Healer\n" + "4) Commoner\n"
				+ "5) Assign Randomly");
		int choice = s.nextInt();
		while (!(choice >= 1 && choice <= 5)) {
			System.out.println("Wrong Choice.\n" + "Choose a Character");
			choice = s.nextInt();
		}
		if (choice == 5)
			choice = rand.nextInt(5) + 1;
		soft user = new soft(s);
		user.manageTask(choice);
		s.close();
	}
}

class mafiaPlayerComparator implements Comparator<Player> {
	@Override
	public int compare(Player arg0, Player arg1) {
		if (arg0.getHP() < arg1.getHP())
			return -1;
		else if (arg0.getHP() > arg1.getHP())
			return 1;
		return 0;
	}
}