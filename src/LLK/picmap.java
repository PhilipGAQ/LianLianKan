package LLK;

import java.util.ArrayList;

public class picmap {
	
	private int[][] map;
	private int count;
	private int n;
	
	
	public picmap(int count,int n){	
		map = CreateMap.getMap(n);
		this.count = count;
		this.n = n;
	}
	
	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getN() {
		return n;
	}

	public void setN(int n) {
		this.n = n;
	}

	public int[][] getMap(){
		
		ArrayList<Integer> list = new ArrayList<Integer>();
		int k=0;
		for (int i=0;i<n*n/2;i++){
			list.add(k%10);
			list.add(k%10);
			k=k+1;
		}
		for(int i=0;i<n;i++){
			for(int j=0;j<n;j++){
				int	index = (int) (Math.random()*list.size());
				map[i][j] = list.get(index);
				list.remove(index);	
			}
		}
	return map;

	}

	
	public int[][] getResetMap(){
		
		ArrayList<Integer> list = new ArrayList<Integer>();
		
		for(int i=0;i<n;i++){
			for(int j=0;j<n;j++){
				if(map[i][j]!=-1)
					list.add(map[i][j]);		
				map[i][j]=-1;
			}
		}
		
		while(!list.isEmpty()){
			
			int	index = (int) (Math.random()*list.size());
			boolean flag = false;
			
			while(!flag){
				int i = (int) (Math.random()*n);
				int j = (int) (Math.random()*n);
				if(map[i][j]==-1){
					map[i][j] = list.get(index);
					list.remove(index);
					flag = true;
				}	
			}
			
		}
		
		return map;
		
	}
	
	
}
