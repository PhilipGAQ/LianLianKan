package LLK;

public class CreateMap {

	static int[][] map;
	
	public CreateMap(){
		
	}
	
	public static int[][] getMap(int n){
		map = new int[n][n];//生成n*n地图
		
		for(int i=0;i<n;i++){
			for(int j=0;j<n;j++){
				map[i][j] = -1;
			}
		}
		
		return map;
	}
	
}
