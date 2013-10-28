import java.util.Random;import java.util.Random;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.*;
/**
 * CodeVS 2012 Java
 */
public class codeVS {
	//static Scanner scan = new Scanner(System.in);
	static int wid = 10;
	static int hei = 16;
	static int size = 4;
	static int sum = 10;
	static int step = 1000;
 
	static int lad = 0;	//���ݐςݏd�˂�ׂ��i��
	static int stop = 0;	//��~
	//static int max = 0;
	static int now_max;
	static int now_min;
	static int sum_max = 0; //sum_tresure�̍ő�l
	static int min_rot,min_xpos;
	static int max_rot,max_xpos;
	static int max_point;
	static int now_step = 0;	//���݌v�Z���̃X�e�b�v
	static int delete_count;	//�܂������ɂ���A�����ǂ���
	static int chain_count;		//���̃^�[���ɂ��A����
	static int[] ch_count = new int[hei];	//ch_state�̐�
	static int[][] ch_now_step = new int[hei][3000];	//ch_state�̂Ƃ���now_step
	static int[][][] state = new int[step+2][hei+size+2][wid];	//�}�X�ڂ̏�� �c�A��
	static int[][][][] ch_state = new int[hei][3000][hei+size+2][wid];	//�`�F�b�N�ς݂�state
	static int[][] peak = new int[step+2][wid];	//�e��̒��_�n
	static int[][][] pack = new int[step][size][size];
	static int[][] rot_pack = new int[size][size];	//��]���ꂽ�p�b�N�̏��
	static int[][] tmp = new int[500][2];	//�������u���b�N�̃}�X�� 2�Ԗڂ�[]��0��y�A�P��x��\��
	static int block;	//�������u���b�N�̐�
	static int[][] del_p = new int[500][2];	//������\��̃u���b�N�̃}�X�� 2�Ԗڂ�[]��0��y�A�P��x��\��
	static int[][] ok = new int[step+2][(wid+2)*4];	//���̃X�e�b�v�ŗ����ł��邩�ǂ���	2�Ԗڂ̍��́A(xpos+2)*4+rot�Ŏ������
	static int[][] tresure = new int[step+2][wid];	//�elad�ɂ����āA�󂵂Ă͂����Ȃ��u���b�N���ۂ�	0�͉󂵂Ă悵�A1�̓_��
	static int[] lad_step = new int[hei];	//�i�i������������now_step��
	static int[] lad_xpos = new int[hei];	//�i�i����������������xpos
	static int[] lad_rot = new int[hei];	//
	static int[][] fix = new int[step+2][wid];	//fix�}�X
	static int[] sum_tresure = new int[step+2];
	static int repeat_count = 0;
	static int[] max_i = new int[step+2];	//����step��max_i
	static File outputFile = new File("text.txt");
 
	public static void main(String[] arg){	
		try{
			File file = new File("input.txt");
			Scanner scan = new Scanner(file);
			wid = scan.nextInt();
			hei = scan.nextInt();
			size = scan.nextInt();
			sum = scan.nextInt();
			step = scan.nextInt();
			String endstr = null;
			for(int i=0;i<step;i++){
				for(int j=0;j<size;j++){
					for(int k=0;k<size;k++){
						pack[i][j][k] = scan.nextInt();		//j�͏c�Ai�͉�
					}
				}
				endstr = scan.next();
			}
		}catch(Exception e){
		}	
		//������
		for(int i=0;i<wid;i++){
			peak[0][i] = 0;
			tresure[0][i] = 0;
		}
		
		for(int i=0;i<hei;i++){
			ch_count[i] = 0;
			lad_step[i] = 0;
		}
 
		for(int i=0;i<wid;i++){
			for(int j=0;j<hei+2;j++){
				state[0][j][i] = 0;
			}
		}
 
		
 
		Random random = new Random(1);
 
		step:for(int i=0;i<step;i++){
			init(0);
			while(lad < 12){
				repeat_main();
			}
		}
	}
	
	static void init(int _step){
		if(lad > 0){	//tresure,fix������
			for(int i=0;i<wid;i++){
				tresure[now_step-1][i] = tresure[now_step][i];
				fix[now_step-1][i] = fix[now_step][i];
			}
		}
		for(int xpos = -2; xpos < wid; xpos++){
			for(int rot = 0; rot<4; rot++){
				//chain_count = 0;
				delete_count = 0;
				if(rot != 0) rotate(rot,_step);
				int judge = set(rot,xpos,_step);	//judge�ɂ�point������
				ok[now_step][(xpos+2)*4 + rot] = judge;					
				state_set();
			}
		}	
	}
	
	static void repeat_main(){
	repeat_count++;
	int check = 0;
	int max = -1;
	/*
try{
	String result;
	FileOutputStream fos = new FileOutputStream(outputFile,true);
	OutputStreamWriter osw = new OutputStreamWriter(fos);
	PrintWriter pw = new PrintWriter(osw);
	
	//pw.println("-------------------------------------------------------------------------");
	pw.close();
}catch(Exception e){
}
*/
 
 
		for(int i=0;i<(wid+2)*4;i++){
			if(ok[now_step][i] > max){
				max = ok[now_step][i];
				max_i[now_step] = i;
			}
		}
		if(max > 0){
				check = 1;
				int _rot = max_i[now_step]%4;
				int _xpos = max_i[now_step]/4 - 2;
				rotate(_rot,now_step);	//j%4 = rot
				set(_rot,_xpos,now_step);
				//if(lad == 3) 0,0,0);
				if( ( (lad%4 == 0|| lad%4 == 1) && sum_tresure[now_step] == wid + 10) || (lad%4 == 3 && sum_tresure[now_step] == wid + 18) || (lad%4 == 2 && sum_tresure[now_step] == wid + 20)){	//���Ƃ�
					//�������Ɠ����łȂ����m�F
					
					for(int count=ch_count[lad+1]-1;count >= 0;count--){
						if(ch_now_step[lad+1][count] == now_step+1){
							int _check = 0;
							for(int i=0;i<lad+2;i++){
								for(int j=0;j<wid;j++){
									if(ch_state[lad+1][count][i][j] != state[now_step][i][j]){
										_check = 1;
									}								
								}
							}
							if(_check == 0){
							//write(_xpos,_rot,888);
								state_set();
								ok[now_step][max_i[now_step]] = -1;
								return;
							}else{
								//write(_xpos,_rot,299);
							}
						}
					}
					
					
					
					//�������Ɠ����łȂ����
					if(grow(_xpos,_rot) == 1){
						//if(lad == 2 ) write(_xpos,_rot,185);
						return;
					}
					else if(lad>0){
						//���Ȃ��\��
						write(_xpos,_rot,99999);
						now_step = lad_step[lad-1] + 1;
						state_set();
						ok[now_step][max_i[now_step]] = -1;
						return;
					}						
				}
				now_step++;
				state_set();
				//write(0,0,0);
				
				
				if(lad > sum_max){
					sum_max = lad;
					write(0,0,lad);
				}
				
			
				
				/*
				if(lad == 6){
					//write(0,0,999);
					
					
					if(sum_tresure[now_step-1] > sum_max){
						sum_max = sum_tresure[now_step-1];
						write(0,0,999);
					}
					else{
						//write(0,0,666);
					}
					
					
					
				}
				*/
			
			
				
				
				
				/*
				if(lad == 6){
				write(_xpos,_rot,555);			
				}
				*/
				
				
				for(int xpos = -2; xpos < wid; xpos++){
					for(int rot = 0; rot<4; rot++){
						//chain_count = 0;
						//delete_count = 0;
						if(rot != 0) rotate(rot,now_step);
						int judge = set(rot,xpos,now_step);	//judge�ɂ�point������
						ok[now_step][(xpos+2)*4 + rot] = judge;
						/*
						if(judge > 0){
							if(grow(xpos,rot) == 1){
								return;
							}
						}*/
						state_set();
					}
				}
				return;
			}
			//write(now_step,0,256);
		if(check == 0){
/*
			if(stop == 1){
								_step:for(int _i=0;_i<now_step;_i++){
									for(int j=0;j<(wid+2)*4;j++){
										if(ok[_i][j] == 1){
											System.out.printf("%d %d\n",j/4-2,j%4);
											System.out.flush();
											continue _step;
										}
									}
								}
			}
			*/
			
			now_step--;
			state_set();
			if(lad == 0){
						ok[now_step][max_i[now_step]] = -1;
						//now_step--;
						return;
				//break�ł��Ȃ�������A����now_step�ł͂����o����R�}���Ȃ��@�̂�
				
			}
			else{
				if(now_step > lad_step[lad-1]){
						ok[now_step][max_i[now_step]] = -1;
						//now_step--;
						return;
					//break�ł��Ȃ�������A����now_step�ł͂����o����R�}���Ȃ��@�̂�
					
				}else if(now_step == lad_step[lad-1]){
				
/*
	try{
		String result;
		FileOutputStream fos = new FileOutputStream(outputFile,true);
		OutputStreamWriter osw = new OutputStreamWriter(fos);
		PrintWriter pw = new PrintWriter(osw);
		pw.println(repeat_count);
		pw.println("wakita");
		pw.close();
	}catch(Exception e){
	}
	*/
	
 
 
 
					//now_step��lad_step[lad-1]�܂ŉ������Ă�����
					//ok[now_step][max_i[now_step]] = -1;
					//now_step--;
					ok[now_step][max_i[now_step]] = -1;
					lad_step[lad-1] = 0;
					lad--;
					
					return;
				}else{
					ok[now_step][max_i[now_step]] = -1;
					return;
				}
 
			}
		}
	}
 
	static int set(int rot,int _xpos,int _step){
		int point;	//pos��neg�̌v�Z�l
		block = 0;
		if(rot == 0){	//��]0��������
			for(int i=0;i<size;i++){
				for(int j=size-1;j>=0;j--){
					if(pack[_step][j][i] != 0){
						if(i+_xpos < 0 || i+_xpos > wid-1) return -1;	//�g�O����
						state[now_step][peak[now_step][i+_xpos]][i+_xpos] = pack[_step][j][i];
						tmp[block][0] = peak[now_step][i+_xpos];
						tmp[block][1] = i + _xpos;	//�������u���b�N�̈ʒu��ۑ�
						
						block++;
						peak[now_step][i+_xpos]++;
					}
				}
			}
		}else{
			for(int i=0;i<size;i++){
				for(int j=size-1;j>=0;j--){
					if(rot_pack[j][i] != 0){
						if(i+_xpos < 0 || i+_xpos > wid-1) return -1;
						
						state[now_step][peak[now_step][i+_xpos]][i+_xpos] = rot_pack[j][i];
						tmp[block][0] = peak[now_step][i+_xpos];
						tmp[block][1] = i + _xpos;	//�������u���b�N�̈ʒu��ۑ�
						
						block++;
						peak[now_step][i+_xpos]++;
					}
				}
			}
		}
		point = check();	//�m�X�e�ƃ_���X�e�̔���
		return point;
	}
	
	static int grow(int xpos,int rot){	//lad�����N�A�b�v�]��
		if(lad %4 == 0){
			def_fix();
			if(fix[now_step][0] + fix[now_step][1] == sum){
				return -1;
			}
			for(int i=1;i<wid;i++){
				tmp[i-1][0] = lad + 1;
				tmp[i-1][1] = i;
			}
			block = wid-1;
			delete(1);
			if(delete_count != 0){
				return -1;
			}
		}
		else if(lad%4 == 1){
			def_fix();
			//��i�`�F�b�N
			for(int i=0;i<wid;i++){
				tmp[i][0] = lad + 1;
				tmp[i][1] = i;
			}
			block = wid;
			delete(1);
			if(delete_count != 0){
				return -1;
			}
			//���i�`�F�b�N
			for(int i=1;i<wid;i++){
				tmp[i][0] = lad;
				tmp[i][1] = i;
			}
			block = wid - 1;
			delete(1);
			if(delete_count != 0){
				return -1;
			}
		}
		else{
		}
				
 
		
				
		lad_step[lad] = now_step;
		lad_xpos[lad] = xpos;
		lad_rot[lad] = rot;
		
		ok[now_step][(xpos+2)*4 + rot] = -1;
		now_step++;
		state_set();
		
		//def_fix();	//fix���v�Z
		lad++;
		if(ch_count[lad] < 3000){
			ch_now_step[lad][ch_count[lad]] = now_step;
			for(int i=0;i<lad+2;i++){
				for(int j=0;j<wid;j++){
					ch_state[lad][ch_count[lad]][i][j] = state[now_step][i][j];
				}
			}
			ch_count[lad]++;
		}
		//def_tresure();	//���̗�ŉ󂵂Ă͂����Ȃ��u���b�N�����߂�
		for(int k=0;k<wid;k++){
			tresure[now_step][k] = 0;
		}
		
		init(now_step);
		
		if(lad == 12){
			write(0,0,0);
			int _lad = 0;
			int f_xpos,f_rot;
			for(int _i=0;_i<now_step;_i++){
			
				f_xpos = max_i[_i]/4-2;
				f_rot = max_i[_i]%4;
				System.out.printf("%d %d\n",f_xpos,f_rot);
				System.out.flush();	
				
			}
			System.out.println("finish\n");
			System.out.flush();	
		}
		
		
		return 1;
	}
 
	static void delete(int def){	//def��def_tresure�ݒ莞���ۂ�
		int x,y,num,num_2,tmp_hei;	//num �v�Z�����}�X�� num_w �������Ɍv�Z�����}�X�� tmp_hei �u���b�N�������ɂ����܂ŗ�����
		int col;	//�v�Z���̍��v�l
		int delete_n = 0;	//������u���b�N��
		int tmp_other = 0;	//���̃u���b�N���󂵂��� lad = 0�p
		int[] tmp_fix = new int[wid];
		int[] tmp_over = new int[wid];
		int[][] tmp_delete = new int[500][2];
		
		if(def == 1){
			for(int i=0;i<wid;i++){
				tmp_fix[i] = state[now_step][tmp[0][0]][i];
				tmp_over[i] = state[now_step][tmp[0][0]+1][i];
				state[now_step][tmp[0][0]][i] = fix[now_step][i];
				state[now_step][tmp[0][0]+1][i] = sum + 1;
			}
		}
 
		for(int i=0;i<block;i++){
			y = tmp[i][0];
			x = tmp[i][1];
			if(state[now_step][y][x] == 0 || state[now_step][y][x] < 0) continue;
			//�� �E��
			num = 0;
			col = 0;
			while(col < sum){
				num++;
				if(x < 0 || x > wid - 1 || state[now_step][y][x] == 0) break;
				col += state[now_step][y][x];
				if(col == sum){
					for(int j=0;j<num;j++){
						del_p[delete_n][0] = y;
						del_p[delete_n][1] = x;
						x--;
						delete_n++;
					}
					break;
				}
				x++;
			}
 
			//����			
			for(int k=0;k<num-1;k++){
				num_2 = 0;
				col = 0;
				x = tmp[i][1] + num - 2 - k;	//x�̃X�^�[�g�l��ݒ�
				while(col < sum){
					if(x < 0 || x > wid - 1 || state[now_step][y][x] == 0) break;
					num_2++;
					col += state[now_step][y][x];
					if(col == sum){
						for(int j=0;j<num_2;j++){
							del_p[delete_n][0] = y;
							del_p[delete_n][1] = x;
							x++;
							delete_n++;
						}
						break;
					}
					x--;
				}
			}
 
			//�c ���
			x = tmp[i][1];
			col = 0;
			num = 0;
			while(col < sum){
				num++;
				if(y < 0 || y > hei+size || state[now_step][y][x] == 0) break;
				
				col += state[now_step][y][x];
				if(col == sum){
					for(int j=0;j<num;j++){
						del_p[delete_n][0] = y;
						del_p[delete_n][1] = x;
						y--;
						delete_n++;
					}
					break;
				}
				y++;
			}
 
			//����
			
			
			for(int k=0;k<num-1;k++){
				col = 0;
				num_2 = 0;
				y = tmp[i][0] + num - 2 - k;	//y�̃X�^�[�g�l��ݒ�
				while(col < sum){
					if(y < 0 || y > hei+size || state[now_step][y][x] == 0) break;
					num_2++;
					col += state[now_step][y][x];
					if(col == sum){
						for(int j=0;j<num_2;j++){
							del_p[delete_n][0] = y;
							y++;
							del_p[delete_n][1] = x;
							delete_n++;
						}
						break;
					}
					y--;
				}
			}
 
			//�ȂȂ� �ȂȂ߉E���
			y = tmp[i][0];
			col = 0;
			num = 0;
			while(col < sum){
				num++;
				if(y < 0 || y > hei+size || x < 0 || x > wid-1 || state[now_step][y][x] == 0) break;
 
				col += state[now_step][y][x];
				if(col == sum){
					for(int j=0;j<num;j++){
						del_p[delete_n][0] = y;
						del_p[delete_n][1] = x;
						x--;
						y--;
						delete_n++;
					}
					break;
				}
				x++;
				y++;
			}
 
			//�ȂȂߍ�����
			
			
			for(int k=0;k<num-1;k++){
				col = 0;
				num_2 = 0;
				x = tmp[i][1] + num - 2 - k;
				y = tmp[i][0] + num - 2 - k;	//�X�^�[�g�l��ݒ�
				while(col < sum){
					if(y < 0 || y > hei+size || x < 0 || x > wid-1 || state[now_step][y][x] == 0) break;
					num_2++;
					col += state[now_step][y][x];
					if(col == sum){
						for(int j=0;j<num_2;j++){
							del_p[delete_n][0] = y;
							del_p[delete_n][1] = x;
							x++;
							y++;
							delete_n++;
						}
						break;
					}
					x--;
					y--;
				}
			}
 
			//�ȂȂ� �ȂȂߍ����
			x = tmp[i][1];
			y = tmp[i][0];
			col = 0;
			num = 0;
			while(col < sum){
				num++;
				if(y < 0 || y > hei+size || x < 0 || x > wid-1 || state[now_step][y][x] == 0) break;
				
				col += state[now_step][y][x];
				if(col == sum){
					for(int j=0;j<num;j++){
						del_p[delete_n][0] = y;
						del_p[delete_n][1] = x;
						x++;
						y--;
						delete_n++;
					}
					break;
				}
				x--;
				y++;
			}
 
			//�ȂȂ߉E����
			
			
			for(int k=0;k<num-1;k++){
				col = 0;
				num_2 = 0;
				x = tmp[i][1] - num + 2 + k;
				y = tmp[i][0] + num - 2 - k;	//�X�^�[�g�l��ݒ�
				while(col < sum){
					if(y < 0 || y > hei+size || x < 0 || x > wid-1 || state[now_step][y][x] == 0) break;
					num_2++;
					col += state[now_step][y][x];
					if(col == sum){
						for(int j=0;j<num_2;j++){
							del_p[delete_n][0] = y;
							del_p[delete_n][1] = x;
							x--;
							y++;
							delete_n++;
						}
						break;
					}
					x++;
					y--;
				}
			}
				
		}
 
		if(def == 1){	//def�ݒ莞�p
			for(int i=0;i<wid;i++){
				state[now_step][tmp[0][0]][i] = tmp_fix[i];
				state[now_step][tmp[0][0]+1][i] = tmp_over[i];
			}
			for(int i=0;i<delete_n;i++){
				if(del_p[i][0] <= tmp[0][0]){	//fix�i���󂵂Ă��Ȃ����A�������� tresure�����Ώۂ̃u���b�N�����Ă��Ȃ����ǂ����𒲂ׂĂ���
					delete_count = 1;
		/*			
try{
	String result;
	FileOutputStream fos = new FileOutputStream(outputFile,true);
	OutputStreamWriter osw = new OutputStreamWriter(fos);
	PrintWriter pw = new PrintWriter(osw);
	pw.println(del_p[i][0]);
	pw.println(del_p[i][1]);
	pw.println("delete");
	pw.close();
}catch(Exception e){
}
*/
 
					return;
				}
			}
			delete_count = 0;
			return;
		}
		
		if(lad > 0){
			for(int i=0;i<delete_n;i++){
				if(lad%4 == 1){
					if(del_p[i][0] < lad && del_p[i][1] < wid-2 ){
						delete_count = 1;
						break;
					}
					else if(del_p[i][0] < lad-1 && del_p[i][1] >= wid-2) {	//�E�Q�}�X�ƍ��P�}�X�͏����Ă悵
						delete_count = 1;
						break;
					}
				}
				else{
					if(del_p[i][0] < lad){	//���̒i�̃u���b�N���󂵂Ă����� ��������tresure���󂵂Ă�����
						delete_count = 1;
						break;
					}
				}
			}
			//delete_count = 0;
		}
 
 
		//��������
		block = 0;
		if(delete_n != 0){
			chain_count++;
			//�܂��S�Ẵu���b�N������
			for(int i=0;i<delete_n;i++){
				state[now_step][del_p[i][0]][del_p[i][1]] = 0; 
			}
 
			for(int i=0;i<delete_n;i++){
				if(state[now_step][del_p[i][0]][del_p[i][1]] != 0){
					continue;	//���łɗ����ς̗�
				}
				tmp_hei = 0;	//��u���b�N������
				while(state[now_step][tmp_hei][del_p[i][1]] != 0){
					tmp_hei++;
				}
				for(int j=tmp_hei+1;j<peak[now_step][del_p[i][1]];j++){
					if(state[now_step][j][del_p[i][1]] != 0){
						state[now_step][tmp_hei][del_p[i][1]] = state[now_step][j][del_p[i][1]];
						state[now_step][j][del_p[i][1]] = 0;
						tmp[block][0] = tmp_hei;
						tmp[block][1] = del_p[i][1];	//�������u���b�N�̈ʒu��ۑ�
						block++;
						tmp_hei++;
					}
				}
				peak[now_step][del_p[i][1]] = tmp_hei;
			}
			delete(0);
		}
	}
 
	static void state_set(){
		if(now_step == 0){
			for(int i=0;i<wid;i++){
				peak[now_step][i] = 0;
				fix[now_step][i] = 0;
				tresure[now_step][i] = 0;
			}
 
			for(int i=0;i<wid;i++){
				for(int j=0;j<hei+size;j++){
					state[now_step][j][i] = 0;
				}
			}	
		}
		else{
			for(int i=0;i<wid;i++){
				peak[now_step][i] = peak[now_step-1][i];
				tresure[now_step][i] = tresure[now_step-1][i];
				fix[now_step][i] = fix[now_step-1][i];
			}
 
			for(int i=0;i<wid;i++){
				for(int j=0;j<hei+size;j++){
					state[now_step][j][i] = state[now_step-1][j][i];
				}
			}
		}
	}
	static void rotate(int rot,int _step){
		if(rot == 1){
			for(int j=0; j<size;j++){
				for(int i=0; i<size;i++){
					rot_pack[j][i] = pack[_step][size-i-1][j];
				}
			}
		}
		if(rot == 2){
			for(int j=0; j<size;j++){
				for(int i=0; i<size;i++){
					rot_pack[j][i] = pack[_step][size-j-1][size-i-1];
				}
			}
		}
		if(rot == 3){
			for(int j=0; j<size;j++){
				for(int i=0; i<size;i++){
					rot_pack[j][i] = pack[_step][i][size-j-1];
				}
			}
		}
	}
	
	static int check(){
		int point = 0;
		int judge;
		int ex_block;
		sum_tresure[now_step] = 0;
		if(lad %4 ==  0){
			delete_count = 0;
			delete(0);	//�������u���b�N�̏�������	
			if(lad > 0 && delete_count != 0){
				return -1;
			}
			
			for(int i=0; i<wid; i++){	//�����R�������� or �ז��u���b�N�r��
				if(state[now_step][lad][i] > sum && i>0 && i < wid -2) return -1;
				else if(state[now_step][lad+1][i] > sum && i < wid -1) return -1;
				else if(lad == 0 && peak[now_step][i] > lad+2) return -1;
				else if(lad != 0 && peak[now_step][i] > hei) return -1;
			}	
		
			
			for(int i=0;i<wid;i++){
				if(i == 0 || i == 1 || i == wid-2 || i == wid-1){
				
					if(state[now_step][lad][i] != 0){
						sum_tresure[now_step]++;
					}
					
				}
				else if(i == 2 || i == 4 || i == 6){
					if(state[now_step][lad][i] != 0 && state[now_step][lad][i+1] != 0){
						if(state[now_step][lad][i] + state[now_step][lad][i+1] > sum ) {
							sum_tresure[now_step]--; 
						}
						else{
							sum_tresure[now_step]+=2;
						}
					}
					else if(state[now_step][lad][i] == sum-1 || state[now_step][lad][i+1] == sum-1 ){
						sum_tresure[now_step]--;
					}
					else if(state[now_step][lad][i] != 0){
						sum_tresure[now_step]++;
					}
					else if(state[now_step][lad][i+1] != 0){
						sum_tresure[now_step]++;
					}
				}
			}
			
			def_fix();
			
			for(int i=1;i<wid;i++){
				tmp[i-1][0] = lad + 1;
				tmp[i-1][1] = i;
			}
			block = wid - 1;
			delete(1);
			if(delete_count != 0){
				return -1;
			}
			
			sum_tresure[now_step] += 10;	
			
			if(lad == 0 && now_step != 0 && sum_tresure[now_step] <= sum_tresure[now_step-1]){
				return -1;
			}
			else if(lad != 0 && now_step != lad_step[lad-1] + 1 && sum_tresure[now_step] <= sum_tresure[now_step-1]){
				return -1;
			}
			else{
				ex_block = 0;
				//��������Ă�u���b�N�̐�
				for(int i=0;i<wid;i++){
					for(int j=lad;j<hei;j++){
						if(state[now_step][j][i] != 0){
							ex_block += 1;
						}
					}
				}
				if(ex_block > 20){
					return -1;
				}
				else{
					return sum_tresure[now_step] * 1000 - ex_block;
				}
			}
		}
		else if(lad%4 == 1){
			int tmp1,tmp2,tmp3,tmp4,_i,_check;
			chain_count = 0;
			delete_count = 0;
			sum_tresure[now_step] = 0;
			delete(0);	//�������u���b�N�̏�������
			if(delete_count != 0) return -1;	//���̒i�̃u���b�N���󂵂Ă�����
			
			
			
			for(int i=0; i<wid; i++){	//������Q�𒴂�����
				if(state[now_step][lad][i] > sum && i < wid -1) return -1;
				else if(lad == 1 && peak[now_step][i] > lad+2) return -1;
				else if(lad != 1 && peak[now_step][i] > hei) return -1;
			}
			
			for(int i=0;i<wid;i++){
				for(int j=lad+1;j <= peak[now_step][i];j++){
					if(state[now_step][j][i] > sum){
						return -1;
					}
				}
			}
			
			for(int i=0;i<wid;i++){
				if(i == wid-1 || i == wid -2){
					sum_tresure[now_step]++;
				/*
					if(state[now_step][lad][i] != 0){
						if(lad == 1){
							sum_tresure[now_step]++;
						}
						else{
							sum_tresure[now_step] += 2;
						}
					}
					*/
				}
				else if(i == 0 || i == 2 || i == 4 || i == 6){
					if(state[now_step][lad][i] != 0 && state[now_step][lad][i] == fix[now_step][i+3]){//fix���Ԃ�`�F�b�N
						sum_tresure[now_step]--;
						if(state[now_step][lad][i+1] == sum - 1){
							sum_tresure[now_step]--;
						}else if(state[now_step][lad][i+1] != 0){
							sum_tresure[now_step]++;
						}
					}
					else if(state[now_step][lad][i] != 0 && state[now_step][lad][i+1] != 0){
						if(state[now_step][lad][i] + state[now_step][lad][i+1] > sum || state[now_step][lad][i] + state[now_step][lad][i+1] < 6){
							sum_tresure[now_step] -= 2;
						}
						else{
							sum_tresure[now_step] += 2;
						}
					}
					else if(state[now_step][lad][i] == sum-1 || state[now_step][lad][i+1] == sum-1  ){
						sum_tresure[now_step]--;
					}
					else if(state[now_step][lad][i] != 0){
						sum_tresure[now_step]++;
					}
					else if(state[now_step][lad][i+1] != 0){
						sum_tresure[now_step]++;
					}
				}
				/*
				else if(i == 6){
					if(state[now_step][lad][i] != 0 && state[now_step][lad][i+1] != 0){
						if(state[now_step][lad][i] + state[now_step][lad][i+1] > sum){
							sum_tresure[now_step] -= 2;
						}
						else{
							if(lad == 1){
								sum_tresure[now_step] += 2;
							}
							else{
								sum_tresure[now_step] += 4;
							}
						}
					}
					else if(state[now_step][lad][i] == sum-1 || state[now_step][lad][i+1] == sum-1){
						sum_tresure[now_step]--;
					}
					else if(state[now_step][lad][i] != 0){
						if(lad == 1){
							sum_tresure[now_step]++;
						}
						else{
							sum_tresure[now_step] += 2;
						}
					}
					else if(state[now_step][lad][i+1] != 0){
						if(lad == 1){
							sum_tresure[now_step]++;
						}
						else{
							sum_tresure[now_step] += 2;
						}
					}
				}
				*/
				/*
				else if(i == 8){
					if(state[now_step][lad][i] != 0){
						if(state[now_step][lad][i] > 4){
							sum_tresure[now_step]--;
						}
						else{
							if(lad == 1){
								sum_tresure[now_step]++;
							}
							else{
								sum_tresure[now_step] += 2;
							}
						}
					}
				}
				*/
			}
			def_fix();
			//��i�`�F�b�N
			for(int i=0;i<wid;i++){
				tmp[i][0] = lad + 1;
				tmp[i][1] = i;
			}
			block = wid;
			delete(1);
			if(delete_count != 0){
				return -1;
			}
			//���i�`�F�b�N
			for(int i=1;i<wid;i++){
				tmp[i-1][0] = lad;
				tmp[i-1][1] = i;
			}
			block = wid - 1;
			delete(1);
			if(delete_count != 0){
				return -1;
			}
			
			//�E��`�F�b�N
			
			if(state[now_step][lad][wid-2] != 0){
				tmp1 = state[now_step][lad+2][wid-1];
				tmp2 = state[now_step][lad+1][wid-1];
				tmp3 = state[now_step][lad+2][wid-2];
				tmp4 = state[now_step][lad+1][wid-2];
				state[now_step][lad+2][wid-1] = sum - state[now_step][lad][wid-2];
				state[now_step][lad+1][wid-1] = sum + 1;
				state[now_step][lad+2][wid-2] = sum + 1;
				state[now_step][lad+1][wid-2] = sum - state[now_step][lad][wid-3] - state[now_step][lad][wid-4];
				tmp[0][0] = lad + 2;
				tmp[0][1] = wid - 1;
				block = 1;
				delete(0);
				state[now_step][lad+2][wid-1] = tmp1;
				state[now_step][lad+1][wid-1] = tmp2;
				state[now_step][lad+2][wid-2] = tmp3;
				if(state[now_step][lad+1][wid-2] == 0){
					state[now_step][lad+1][wid-2] = tmp4;
					return -1;
				}
				else{
					state[now_step][lad+1][wid-2] = tmp4;
				}
			}
				
 
			sum_tresure[now_step] += 10;
			
			_check = 0;
			for(int i=0;i<size;i++){
				for(int j=0;j<size;j++){
					if(pack[now_step][j][i] > sum) _check = 1;
				}
			}
			
			if(_check == 0 && lad == 1 && now_step > lad_step[lad-1] + 1 && sum_tresure[now_step] <= sum_tresure[now_step-1]){
				return -1;
			}
			else if(_check == 0 && lad != 1 && now_step > lad_step[lad-1] + 1 && sum_tresure[now_step] <= sum_tresure[now_step-1]){
				return -1;
			}			
			else{
				ex_block = 0;
				//��������Ă�u���b�N�̐�
				for(int i=0;i<wid;i++){
					for(int j=lad;j<hei;j++){
						if(state[now_step][j][i] != 0){
							ex_block += 1;
						}
					}
				}
				if(ex_block > 20){
					return -1;
				}
				else{
					return sum_tresure[now_step] * 1000 - ex_block;
				}
			}	
		}
		else if(lad%4 == 2){
			int want = 0;
			int want_2 = 0;
			int want_3 = 0;
			int peak_max = 0;
			int[] ok_blc = new int [wid];
			delete_count = 0;
			sum_tresure[now_step] = 0;
			delete(0);	//�������u���b�N�̏�������
			if(delete_count != 0) return -1;	//���̒i�̃u���b�N���󂵂Ă�����
			
			//��������
			
			for(int i=0; i<wid; i++){	
				ok_blc[i] = 0;
				if(state[now_step][lad][i] > sum ) return -1;
				if(peak[now_step][i] > peak_max){
					peak_max = peak[now_step][i];
				}
				if(peak_max > hei){
					return -1;
				}
			}
			
		
				
			if(lad == 2){
				fix[now_step][7] = state[now_step][lad][7];
			}
		
			fix[now_step][9] = state[now_step][lad][9];
			
			for(int i = 0;i<wid;i++){
				if(fix[now_step][i] == 0){	//�V���X
					if(i == 7 && state[now_step][lad][i] != 0){
						for(int _i=0;_i<wid;_i++){
							tmp[_i][0] = lad;
							tmp[_i][1] = _i;
						}
						block = wid;
						delete(1);
						if(delete_count != 0){
							sum_tresure[now_step]--;
						}
						else{
							ok_blc[i] = 1;
							sum_tresure[now_step] += 2;
						}
					}
					else if(i == 9 && state[now_step][lad][i] != 0){
						for(int _i=0;_i<wid;_i++){
							tmp[_i][0] = lad;
							tmp[_i][1] = _i;
						}
						block = wid;
						delete(1);
						if(delete_count != 0 || state[now_step][lad][i] > 6){
							sum_tresure[now_step]--;
						}
						else{
							ok_blc[i] = 1;
							sum_tresure[now_step] += 2;
						}
					}
				}
				else if(state[now_step][lad][i] != 0){
					if(state[now_step][lad][i] == fix[now_step][i]){
						ok_blc[i] = 1;
						sum_tresure[now_step] += 2;
					}
					else if(state[now_step][lad][i] == sum-1){
						sum_tresure[now_step] -= 4;
					}
					else{
						sum_tresure[now_step]--;
					}
				}
			}
			
			//�u���b�N��ok_blc�̏�ɏ���Ă��Ȃ�������return 
			for(int i=0;i<wid;i++){
				if(i < wid -2 && state[now_step][lad+1][i] > sum && ok_blc[i] != 1){
					return -1;
				}
				else if(i >= wid-2 && state[now_step][lad+1][i] > sum){
					return -1;
				}
			}
			
			for(int i=0;i<wid;i++){
				for(int j=lad+2;j <= peak[now_step][i];j++){
					if(state[now_step][j][i] > sum){
						return -1;
					}
				}
			}
			
			for(int i=0;i<wid;i++){
				if(peak[now_step][i] > lad+1 && ok_blc[i] != 1){
					sum_tresure[now_step]--;
				}
			}
			
			sum_tresure[now_step] += 10;
			
			if(now_step == lad_step[lad-1] + 1){
				return sum_tresure[now_step];
			}
			else if(sum_tresure[now_step] == wid + 20){	//���X�g�Ȃ�
				return sum_tresure[now_step] * 1000;
			}
			else if(sum_tresure[now_step] >= 28){
				//write(0,0,0);
				for(int i=0;i<wid;i++){
					if(state[now_step][lad][i] != fix[now_step][i]){
						want = fix[now_step][i];
					}
				}
				
				
				for(int i=0;i<size;i++){
					for(int j=0;j<size;j++){
						if(pack[now_step][i][j] == want && sum_tresure[now_step] <= sum_tresure[now_step-1]){
							return -1;
						}
					}
				}
				
				ex_block = 0;
				//��������Ă�u���b�N�̐�
				for(int i=0;i<wid;i++){
					for(int j=lad+1;j<hei;j++){
						if(state[now_step][j][i] != 0){
							ex_block += 1;
						}
					}
				}
				if(ex_block > 10){
					return -1;
				}
				else{
					return sum_tresure[now_step] * 1000 - ex_block;
				}
			}
			else if(sum_tresure[now_step] >= 26){
				//write(0,0,0);
				for(int i=0;i<wid;i++){
					if(state[now_step][lad][i] != fix[now_step][i]){
						if(want == 0){
							want = fix[now_step][i];
						}
						else{
							want_2 = fix[now_step][i];
						}
					}
				}
				
				
				for(int i=0;i<size;i++){
					for(int j=0;j<size;j++){
						if((pack[now_step][i][j] == want || pack[now_step][i][j] == want_2) && sum_tresure[now_step] <= sum_tresure[now_step-1]){
							return -1;
						}
					}
				}
				
				ex_block = 0;
				//��������Ă�u���b�N�̐�
				for(int i=0;i<wid;i++){
					for(int j=lad+1;j<hei;j++){
						if(state[now_step][j][i] != 0){
							ex_block += 1;
						}
					}
				}
				if(ex_block > 10){
					return -1;
				}
				else{
					return sum_tresure[now_step] * 1000 - ex_block;
				}
			}
			else if(sum_tresure[now_step] >= 24){
				//write(0,0,0);
				for(int i=0;i<wid;i++){
					if(state[now_step][lad][i] != fix[now_step][i]){
						if(want == 0){
							want = fix[now_step][i];
						}
						else if(want_2 == 0){
							want_2 = fix[now_step][i];
						}
						else{
							want_3 = fix[now_step][i];
						}
					}
				}
				
				
				for(int i=0;i<size;i++){
					for(int j=0;j<size;j++){
						if((pack[now_step][i][j] == want || pack[now_step][i][j] == want_2 || pack[now_step][i][j] == want_3 ) && sum_tresure[now_step] <= sum_tresure[now_step-1]){
							return -1;
						}
					}
				}
				
				ex_block = 0;
				//��������Ă�u���b�N�̐�
				for(int i=0;i<wid;i++){
					for(int j=lad+1;j<hei;j++){
						if(state[now_step][j][i] != 0){
							ex_block += 1;
						}
					}
				}
				if(ex_block > 10){
					return -1;
				}
				else{
					return sum_tresure[now_step] * 1000 - ex_block;
				}
			}
			/*
			else if(sum_tresure[now_step] >= 28){
				int[] tmp_fix = new int[wid];
				
				if(peak_max > lad + 3) return -1;
				for(int i=0;i<wid;i++){
					if(i == 2 || i == 4 || i == 6){
						if(state[now_step][lad+1][i] != 0 && state[now_step][lad+1][i+1] != 0){
							if(state[now_step][lad+1][i] + state[now_step][lad+1][i+1] > sum){
								return -1;
							}
						}
 
					}
				}
				for(int i=0;i<wid;i++){
					tmp_fix[i] = state[now_step][lad][i];
					state[now_step][lad][i] = fix[now_step][i];
				}
				//�ꎞ�I��lad++
	
				lad++;
				def_fix();
				for(int i=1;i<wid;i++){
					tmp[i-1][0] = lad + 1;
					tmp[i-1][1] = i;
				}
				block = wid - 1;
				delete(1);
				if(delete_count != 0){
					return -1;
				}
				lad--;
				lad -= 2;
				def_fix();
				lad++;
				def_fix();
				lad++;
				
				for(int i=0;i<wid;i++){
					state[now_step][lad][i] = tmp_fix[i];
				}
				if(sum_tresure[now_step] >= sum_tresure[now_step-1]){
					write(0,0,999);
					return sum_tresure[now_step];
				}
				else{
					return -1;
				}
			}
			*/
			
			else if(sum_tresure[now_step] >= sum_tresure[now_step-1]){
				ex_block = 0;
				//��������Ă�u���b�N�̐�
				for(int i=0;i<wid;i++){
					for(int j=lad+1;j<hei;j++){
						if(state[now_step][j][i] != 0){
							ex_block += 1;
						}
					}
				}
				if(ex_block > 10) return -1;
				
				for(int i=0;i<wid;i++){
					if( peak[now_step][i] > lad + 3) return -1;
					if( peak[now_step][i] > lad+2 && ok_blc[i] != 1 ){
						return -1;
					}
				}
				return sum_tresure[now_step];
			}
			else{
				return -1;
			}
		}
		else if(lad%4 == 3){
			int peak_max = 0;
			int ok_blc = 0;
			int ok_blc2 = 0;
			int want = 0;
			delete_count = 0;
			sum_tresure[now_step] = 0;
			delete(0);	//�������u���b�N�̏�������
			if(delete_count != 0){
				return -1;	//���̒i�̃u���b�N���󂵂Ă�����
			}
			for(int i=0;i<wid;i++){
				if(peak[now_step][i] > hei) return -1;
			}
			
			if(state[now_step][lad][wid-2] != 0){
				if(state[now_step][lad][wid-2] + state[now_step][lad-1][wid-1] < sum){
					ok_blc = 1;
					sum_tresure[now_step]+=4;
				}
				else{
					sum_tresure[now_step]--;
				}
			}
			if(state[now_step][lad][wid-1] != 0){
				if(state[now_step][lad][wid-1] == sum - state[now_step][lad-2][wid-2]){
					ok_blc2 = 1;
					sum_tresure[now_step]+=4;
				}
				else{
					for(int i=0;i<size;i++){
						for(int j=0;j<size;j++){
							if(pack[now_step][j][i] == sum - state[now_step][lad-2][wid-2]){
								return -1;
							}
						}
					}					
					sum_tresure[now_step]--;
				}
			}
			
			
			
			//��������:
			
			
			for(int i=0; i<wid; i++){	
				if(peak[now_step][i] > lad + 3) return -1;
				else if(state[now_step][lad+2][i] > sum) return -1;
				else if(state[now_step][lad][i] > sum && state[now_step][lad+1][i] > sum) return -1;
				else if( (i == wid-1 || i == wid -2) && state[now_step][lad][i] > sum ) return -1;	
				else if( i == wid - 2 && state[now_step][lad+1][i] > sum && ok_blc != 1) return -1;
				else if( i == wid - 1 && state[now_step][lad+1][i] > sum && ok_blc2 != 1) return -1;
			}
			
			
			//def_fix�͂���
			/*
			for(int i=0;i<wid;i++){
				if(i < wid-2){
					if(state[now_step][lad][i] > sum){
						sum_tresure[now_step]++;
					}
				}
				else if(i == wid-2){
					sum_tresure[now_step]++;
				}
				else if(i == wid -1 && state[now_step][lad][i] != 0){
					if(state[now_step][lad][i] == sum - state[now_step][lad-2][i-1]){
						ok_blc = 1;
						sum_tresure[now_step]++;
					}
					else{
						sum_tresure[now_step]--;
					}
				}
			}
			*/
			
			/*
			for(int i=0;i<wid;i++){
				for(int j=lad;j<lad+2;j++){
					if(state[now_step][j][i] > sum && ( (i > wid-3 && j == lad) || (i != wid -1 && j == lad +1) || (i == wid-1 && j == lad+1 && ok_blc != 1) )){
						write(0,0,55);
						return -1;
					}						
				}
			}
			*/
			for(int i=0;i<wid-2;i++){
				if(state[now_step][lad][i] > sum && i < wid-4) sum_tresure[now_step]++;
				else if(state[now_step][lad][i] > sum && i >= wid-4) sum_tresure[now_step]+=2;
			}
			sum_tresure[now_step] += 10;
			ex_block = 0;
			//��������Ă�u���b�N�̐�
			for(int i=0;i<wid;i++){
				for(int j=lad;j<hei;j++){	//lad���琔����
					if(state[now_step][j][i] != 0){
						ex_block += 1;
					}
				}
			}
			if(ex_block > 20){
				return -1;
			}
			else{
				//write(0,0,333);
				return sum_tresure[now_step] * 1000 - ex_block;
			}
		}
			
			return 1;	//��
	}
	
	static void def_tresure(){
		int tmp1,tmp2,tmp3;
		
		for(int i=0;i<wid;i++){
			tresure[now_step][i] = 1;
		}
		if(lad == 1){
			//tresure[now_step][wid-1] = 0;
			for(int i=0; i<wid; i++){	//i+=2���Ăł���H ��ԉE�͂��̒i�K�ł͓��肵�Ȃ�
				if(i == 0){
					if(state[now_step][lad][i] == 0 || state[now_step][lad][i] == 9){
						tresure[now_step][i] = 0;
					}
					else{
						tmp1 = state[now_step][lad+1][i];
						tmp2 = state[now_step][lad+1][i+1];
						state[now_step][lad+1][i] = sum - state[now_step][lad-1][i+1];
						tmp[0][0] = lad+1;
						tmp[0][1] = i;
						state[now_step][lad+1][i+1] = sum - state[now_step][lad-1][i+2] - state[now_step][lad-1][i+3];
						tmp[1][0] = lad+1;
						tmp[1][1] = i+1;
						block = 2;
						delete(1);
						if(delete_count == 1){
							tresure[now_step][i] = 0;
						}
						state[now_step][lad+1][i] = tmp1;
						state[now_step][lad+1][i+1] = tmp2;
						continue;
					}
				}
				else if(i == 1){
					if(state[now_step][lad][i] == 0 || state[now_step][lad][i] == 9){
						tresure[now_step][i] = 0;
					}
					else{
						tmp1 = state[now_step][lad+1][i];
						tmp2 = state[now_step][lad+1][i+1];
						tmp3 = state[now_step][lad+1][i-1];
						state[now_step][lad+1][i] = sum - state[now_step][lad-1][i+1] - state[now_step][lad-1][i+2];
						tmp[0][0] = lad+1;
						tmp[0][1] = i;
						state[now_step][lad+1][i-1] = sum - state[now_step][lad-1][i];
						tmp[1][0] = lad+1;
						tmp[1][1] = i-1;
						if(tresure[now_step][i-1] == 1){
							state[now_step][lad+1][i+1] = sum - state[now_step][lad][i] - state[now_step][lad][i-1];
						}else{
							state[now_step][lad+1][i+1] = sum + 1;
						}
						tmp[2][0] = lad+1;
						tmp[2][1] = i+1;
						block = 3;
						delete(1);
						if(delete_count == 1){
							tresure[now_step][i] = 0;
						}
						state[now_step][lad+1][i] = tmp1;
						state[now_step][lad+1][i+1] = tmp2;
						state[now_step][lad+1][i-1] = tmp3;
						continue;
					}
				}
				else if(i == 2 || i == 4){
					if(state[now_step][lad][i] == 0 || state[now_step][lad][i] == 9){
						tresure[now_step][i] = 0;
					}
					else{
						tmp1 = state[now_step][lad+1][i];
						tmp2 = state[now_step][lad+1][i+1];
						tmp3 = state[now_step][lad+1][i-1];
						if(tresure[now_step][i-1] == 1 && tresure[now_step][i-2] == 1 && state[now_step][lad][i-1] + state[now_step][lad][i-2] < sum){
							state[now_step][lad+1][i] = sum - state[now_step][lad][i-1] - state[now_step][lad][i-2];
						}else{
							state[now_step][lad+1][i] = sum + 1;
						}
						tmp[0][0] = lad+1;
						tmp[0][1] = i;
						state[now_step][lad+1][i+1] = sum - state[now_step][lad-1][i+2] - state[now_step][lad-1][i+3];
						tmp[1][0] = lad+1;
						tmp[1][1] = i+1;
						state[now_step][lad+1][i-1] = sum - state[now_step][lad-1][i] - state[now_step][lad-1][i+1];
						tmp[2][0] = lad+1;
						tmp[2][1] = i-1;
						block = 3;
						delete(1);
						if(delete_count == 1){
							tresure[now_step][i] = 0;
						}
						state[now_step][lad+1][i] = tmp1;
						state[now_step][lad+1][i+1] = tmp2;
						state[now_step][lad+1][i-1] = tmp3;
						continue;
					}
				}
				else if(i == 3 || i == 5){
					if(state[now_step][lad][i] == 0 || state[now_step][lad][i] == 9){
						tresure[now_step][i] = 0;
					}
					else{
						tmp1 = state[now_step][lad+1][i];
						tmp2 = state[now_step][lad+1][i+1];
						tmp3 = state[now_step][lad+1][i-1];
						state[now_step][lad+1][i] = sum - state[now_step][lad-1][i+1] - state[now_step][lad-1][i+2];
						tmp[0][0] = lad+1;
						tmp[0][1] = i;
						if(tresure[now_step][i-1] == 1 && state[now_step][lad][i] + state[now_step][lad][i-1] < sum){
							state[now_step][lad+1][i+1] = sum - state[now_step][lad][i] - state[now_step][lad][i-1];
						}else{
							state[now_step][lad+1][i+1] = sum + 1;
						}
						tmp[1][0] = lad+1;
						tmp[1][1] = i+1;
						if(tresure[now_step][i-2] == 1 && tresure[now_step][i-3] == 1 && state[now_step][lad][i-2] + state[now_step][lad][i-3] < sum){
							state[now_step][lad+1][i-1] = sum - state[now_step][lad][i-2] - state[now_step][lad][i-3];
						}else{
							state[now_step][lad+1][i-1] = sum + 1;
						}
						tmp[2][0] = lad+1;
						tmp[2][1] = i-1;
						block = 3;
						delete(1);
						if(delete_count == 1){
							tresure[now_step][i] = 0;
						}
						state[now_step][lad+1][i] = tmp1;
						state[now_step][lad+1][i+1] = tmp2;
						state[now_step][lad+1][i-1] = tmp3;
						continue;
					}
				}
				else if(i == 6){
					if(state[now_step][lad][i] == 0 || state[now_step][lad][i] > sum-4){
						tresure[now_step][i] = 0;
					}
					else{
						tmp1 = state[now_step][lad+1][i];
						tmp2 = state[now_step][lad+1][i-1];
						//tmp3�Ȃ�
						if(tresure[now_step][i-1] == 1 && tresure[now_step][i-2] == 1 && state[now_step][lad][i-1] + state[now_step][lad][i-2] < sum){
							state[now_step][lad+1][i] = sum - state[now_step][lad][i-1] - state[now_step][lad][i-2];
						}else{
							state[now_step][lad+1][i] = sum + 1;
						}
						tmp[0][0] = lad+1;
						tmp[0][1] = i;
						state[now_step][lad+1][i-1] = sum - state[now_step][lad-1][i] - state[now_step][lad-1][i+1];
						tmp[1][0] = lad+1;
						tmp[1][1] = i-1;
						block = 2;
						delete(1);
						if(delete_count == 1){
							tresure[now_step][i] = 0;
						}
						state[now_step][lad+1][i] = tmp1;
						state[now_step][lad+1][i-1] = tmp2;
						continue;
					}
				}
				else if(i == 7){
					if(state[now_step][lad][i] == 0 || state[now_step][lad][i] > sum-4){
						tresure[now_step][i] = 0;
					}
					else{
						tmp1 = state[now_step][lad+1][i];	//�_�~�[
						tmp2 = state[now_step][lad+1][i-1];
						state[now_step][lad+1][i] = sum + 1;
						tmp[0][0] = lad+1;
						tmp[0][1] = i;
						if(tresure[now_step][i-2] == 1 && tresure[now_step][i-3] == 1 && state[now_step][lad][i-2] + state[now_step][lad][i-3] < sum){
							state[now_step][lad+1][i-1] = sum - state[now_step][lad][i-2] - state[now_step][lad][i-3];
						}else{
							state[now_step][lad+1][i-1] = sum + 1;
						}
						tmp[1][0] = lad+1;
						tmp[1][1] = i-1;
						block = 2;
						delete(1);
						if(delete_count == 1){
							tresure[now_step][i] = 0;
						}
						state[now_step][lad+1][i] = tmp1;
						state[now_step][lad+1][i-1] = tmp2;
						continue;
					}
				}
				else if(i == 8){
					if(state[now_step][lad][i] == 0 || state[now_step][lad][i] > sum-4){
						tresure[now_step][i] = 0;
					}
				}
				else if(i == 9){
					if(state[now_step][lad][i] > sum - 2){
						tresure[now_step][i] = 0;
					}
				}
			}
			//sum����check
			for(int i=0;i<wid-2;i+=2){
				if(state[now_step][lad][i] + state[now_step][lad][i+1] > sum){
					if(state[now_step][lad][i] > state[now_step][lad][i+1]){	//�傫���ق����tresure��
						tresure[now_step][i] = 0;
					}else{
						tresure[now_step][i+1] = 0;
					}
				}
			}
			
			//wid-1�u���b�N
			if(tresure[now_step][wid-3] == 1 && tresure[now_step][wid-4] == 1){
				if(state[now_step][lad][wid-2] + state[now_step][lad][wid-3] + state[now_step][lad][wid-4] > sum){
					tresure[now_step][wid-1] = 0;
				}
			}else if(tresure[now_step][wid-3] == 1){
				if(state[now_step][lad][wid-2] + state[now_step][lad][wid-3] > sum){
					tresure[now_step][wid-2] = 0;
				}
			}else if(tresure[now_step][wid-4] == 1){
				if(state[now_step][lad][wid-2] + state[now_step][lad][wid-4] > sum){
					tresure[now_step][wid-2] = 0;
				}
			}
					
		}			
	}
	
	static void def_fix(){
		if(lad%4 == 0){
			for(int i=0;i<wid;i++){
				fix[now_step][i] = 0;
			}
			if(state[now_step][lad][1] != 0){
				fix[now_step][0] = sum - state[now_step][lad][1];
			}
			//if(tresure[now_step][1] == 1) fix[now_step][0] = sum - state[now_step][lad][1];
			for(int i=1;i<6;i+=2){
				if(state[now_step][lad][i+1] != 0 && state[now_step][lad][i+2] != 0){
					fix[now_step][i] = sum - state[now_step][lad][i+1] - state[now_step][lad][i+2];
				}
			}
			if(lad != 0){
				fix[now_step][wid-3] = sum - state[now_step][lad-1][wid-2] - state[now_step][lad-2][wid-1];
			}
		}
		else if(lad%4 ==1){
			for(int i=2;i<9;i+=2){
				fix[now_step][i] = 0;
				if(state[now_step][lad][i-1] != 0 && state[now_step][lad][i-2] != 0){
					fix[now_step][i] = sum - state[now_step][lad][i-1] - state[now_step][lad][i-2];
				}
			}
			/*
			fix[now_step][wid-1] = 0;
			if(state[now_step][lad][wid-4] != 0 && state[now_step][lad][wid-2] != 0 && state[now_step][lad][wid-3] != 0){
				fix[now_step][wid-1] = sum - state[now_step][lad][wid-2] - state[now_step][lad][wid-3] - state[now_step][lad][wid-4];
			}
			*/
		}
	}
	
	static void write(int xpos,int rot,int max){
		try{
			String result;
			FileOutputStream fos = new FileOutputStream(outputFile,true);
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			PrintWriter pw = new PrintWriter(osw);
			pw.println(sum_tresure[now_step-1]);
			pw.println("sum");
			pw.println(xpos);
			pw.println("xpos");
			pw.println(rot);
			pw.println("rot");
			pw.println(repeat_count);
			pw.println("repeat");
			pw.println(max);
			pw.println("max");
			pw.println(lad);
			pw.println("lad");
			pw.println(now_step);
			pw.println("now_step");
			if(lad != 0){
				pw.println(lad_step[lad-1]);
				pw.println("lad_step");
			}
			else{
				pw.println(lad_step[0]);
				pw.println("lad_step");
			}
				
			pw.println("\n");
			for(int s=hei;s>=0;s--){
				String tmp = "";
				for(int t=0;t<wid;t++){
					result = String.format("%2s", String.valueOf(state[now_step][s][t]));
					tmp = tmp + result;
				}
				pw.println(tmp);
			}
			for(int _j=0;_j<wid;_j++){
				pw.println(fix[now_step][_j]);
			}
			for(int _j=0;_j<wid;_j++){
				pw.println(peak[now_step][_j]);
			}
			pw.println("fix");
			pw.println("-------------------------------------------------------------------------");
			pw.close();
		}catch(Exception e){
		}
	}
}