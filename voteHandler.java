import java.util.Random;
import java.util.Scanner;

public class voteHandler<T> implements Comparable<Player> {
	T user;
	Scanner s;
	static Random rand = new Random();

	voteHandler(T ss,Scanner s) {
		this.user = ss;
		this.s=s;
	}

	public int voteSet(Player[] playing_player,int target) {
		while(!(target>0&&target<playing_player.length)||playing_player[target]==null || this.compareTo(playing_player[target])==0) {
			System.out.print("You cannot choose this Player"+target+".\nChoose again ");
			target=s.nextInt();
		}
		return target;
	}

	public int randomVoteSet(Player[] playing_player) {
		int target=rand.nextInt(playing_player.length-1)+1;
		while(playing_player[target]==null||this.compareTo(playing_player[target])==0) {
			target=rand.nextInt(playing_player.length-1);
		}
		return target;
	}
	
	public int compareTo(Player arg0) {
		if (user.getClass() == arg0.getClass())
			return 0;
		return -1;
	}
}

