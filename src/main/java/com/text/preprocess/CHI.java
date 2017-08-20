package com.text.preprocess;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class CHI {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int termNum = 818;  //词的数目，也就是没选择前特征数目
		int k=100;   //取权重最高的前K个feature
		String termWeighFile="CS5termWeigh.txt";  //用于保存全部《特征 ，权重》对
		String topFeatureFile="CS5top" + k + "Feature.txt";  //保存前k个《特征 ，权重》对
		String termFile="KeyWordsCS5.txt"; //要求先分好词，存放词的文件，每一行一个词
		int [] A ; //
		int [] B ; //
		int [] C ; //
		int [] D ; //
		double[] chi;
		String [] terms;  //用来记录每个词
		String dirName="D:\\\\workspace\\CHI\\C\\";  //存放类别Ci语料集的目录地址，如C：//
		String dirName1="D:\\\\workspace\\CHI\\NC\\";  //存放不同于类别Ci的其他语料集的目录地址，如C：//
		//初始化
		//w=new int[termNum];
		A=new int[termNum];
		B=new int[termNum];
		C=new int[termNum];
		D=new int[termNum];
		chi=new double[termNum];
		//把keywords导进来保存在terms数组里面
		terms = new String[termNum];
		//把语料集全部列出来
		File dir = new File(dirName);
		File dir1=new File(dirName1);
		File[] files = dir.listFiles();
		File[] files1=dir1.listFiles();

		try {
			FileInputStream fis2 = new FileInputStream(termFile);
			InputStreamReader isr2 = new InputStreamReader(fis2);
			BufferedReader br2 = new BufferedReader(isr2);

			for(int i=0;i<termNum;i++){
				terms[i] = br2.readLine();
			}
			br2.close();
			isr2.close();
			fis2.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		//使用DF方法进行特征选择
		chi_count(files,files1,terms,A,B,C,D,chi);

		//输出《特征，权重》对
		try {
			FileOutputStream fos = new FileOutputStream(termWeighFile);
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			BufferedWriter bw = new BufferedWriter(osw);
			for(int i=0;i<termNum;i++){
				bw.write(terms[i]+" , ");
				bw.write(String.valueOf(chi[i]));
				bw.newLine();
			}
			bw.close();
			osw.close();
			fos.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		//对《特征，权重》进行排序
		top_feature(terms,chi);

		try {
			FileOutputStream fos = new FileOutputStream(topFeatureFile);
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			BufferedWriter bw = new BufferedWriter(osw);
			for(int i=0;i<k;i++){
				bw.write(terms[i]+" , ");
				bw.write(String.valueOf(chi[i]));
				bw.newLine();
			}
			bw.close();
			osw.close();
			fos.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		System.out.println("end chi!!!");
	}
	public static void chi_count(File[] files,File[] files1,String[] terms, int[] A,int[] B,int[] C,int[] D,double[] chi){
		boolean isContain=false;
		int Contains = -1;
		String tempString;
		boolean isContain1=false;
		int Contains1 = -1;
		String tempString1;
		try {
			for(int i=0;i<terms.length;i++){
				for(int j=0;j<files.length;j++){
					char[] tempFile = new char[1024] ;
					FileInputStream fis = new FileInputStream(files[j].toString());
					InputStreamReader isr = new InputStreamReader(fis);
					BufferedReader br = new BufferedReader(isr);
					br.read(tempFile);

					br.close();
					isr.close();
					fis.close();

					tempString= new String(tempFile);
					isContain=tempString.contains(terms[i]);
					if(isContain){
						A[i]++;
					}
					if(!isContain){
						C[i]++;
					}
				}
				for(int j=0;j<files1.length;j++){
					char[] tempFile1 = new char[1024] ;
					FileInputStream fis1 = new FileInputStream(files1[j].toString());
					InputStreamReader isr1 = new InputStreamReader(fis1);
					BufferedReader br1 = new BufferedReader(isr1);
					br1.read(tempFile1);

					br1.close();
					isr1.close();
					fis1.close();

					tempString1= new String(tempFile1);
					isContain1=tempString1.contains(terms[i]);
					if(isContain1){
						B[i]++;
					}
					if(isContain1){
						D[i]++;
					}
				}
				System.out.println("A="+A[i]+"  B="+B[i]+"  C="+C[i]+" D="+D[i]);
				int temp=A[i]*D[i]-B[i]*C[i];
//			System.out.println(temp*temp);
				chi[i]=(float)(temp*temp)/((A[i]+B[i])*(C[i]+D[i])+1);
//			System.out.println(i);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ;
	}
	//对《特征，权重》进行排序
	public static void top_feature(String[] terms, double[] chi){
		for (int i = 0; i < chi.length - 1; i++) {
			int m = i;
			for (int j = i + 1; j < chi.length; j++) {
				if (chi[j] > chi[m]){
					m = j;
				}
			}
			if (i != m)
				swap(terms,chi, i, m);  // 交换两个数组中的索引为i、m的元素
		}
		return ;
	}

	// 交换两个数组中的索引为i、j的元素
	private static void swap(String[] terms, double[] chi, int i, int j) {
		double tempchi;
		String tempterm;
		tempchi = chi[i];
		tempterm = terms[i];
		chi[i] = chi[j];
		terms[i]=terms[j];
		chi[j] = tempchi;
		terms[j]=tempterm;
	}
}
