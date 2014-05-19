package com.lqyandpy.DBN;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.lqyandpy.RBM.*;
import com.lqyandpy.crf.*;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		double[][] tempI=new double[][]{
				  {0,0,0,0,0,1,1},
				  {1,0,0,1,0,0,1},
				  {1,0,1,1,0,1,1},
				  {0,1,1,1,0,1,0},
				  {1,1,0,1,0,1,1},
				  {1,1,0,1,1,1,1},
				  {1,0,1,0,0,1,0},
				  {1,1,1,1,1,1,1},
				  {1,1,1,1,0,1,1}
				  };
//		double[][] tempI=new double[][]{
//				  {0,2,4,3,1,3,13},
//				  {7,3,2,6,6,3,14},
//				  {2,7,3,3,2,3,11},
//				  {0,2,1,8,0,3,13},
//				  {1,3,0,1,0,7,19},
//				  {1,1,0,1,1,1,11},
//				  {1,0,1,0,0,1,10},
//				  {2,1,2,4,2,2,11},
//				  {1,1,1,1,0,1,11}
//				  };
		//Tool.PrintW(tempI);
		
		
//		List<ArrayList<Double>> tempI_ex = read_datafile("D:/eclipse/workspace32/CRF/bin/com/lqyandpy/DBN/test.txt"); 
//		if(tempI_ex == null)
//		{
//			System.out.print("not find data!\n");
//			return;
//		}
//		double[][] tempI =  new double[tempI_ex.size()][tempI_ex.get(0).size()-1];
//		for(int i = 0;i < tempI_ex.size();++i)
//		{
//			for(int j = 0;j < tempI_ex.get(i).size()-1;++j)
//				tempI[i][j] = tempI_ex.get(i).get(j);
//		}
//		System.out.print("data ready.\n");
//		tempI_ex = null;
			
		
		Data tempD=new Data(tempI,false);//这个归一化是针对某一属性进行的归一化，对图像而言不适用
		
		//Tool.PrintW(Data.Normalization(tempI));
	    
		//PermanentRBM tempP=new PermanentRBM();
		//tempP.ReadFromFile("D:\\旧盘备份\\RBMS\\rbm5.txt");
		//RBM tempR=tempP.ReBuildRBM();
		SimpleDBN tempS=new SimpleDBN();
		tempS.Layers=2;
		
		//RBM tempR = new RBM(7,20,false);
		
		//tempS.RebuildDBN("D:\\旧盘备份\\dbn.dat");
		
		
		
		//tempS.InsertRBM(tempR);
		//tempS.InsertRBM(tempR.CopyTiedRBM());
		//Tool.PrintW(tempS.getRBM(1).W);
		
		DBNTrain tempT=new DBNTrain(tempD,tempS);

		tempT.greedyLayerwiseTraining(0.1,0.001,500,new L1(),true,50,10);
		
		//tempS.PermanentDBN("D:\\旧盘备份\\dbn.dat");
		/*
		Data tempDX=tempT.getDataForNextLayer();
		
		int i=0;
		for(int[] v:tempDX.databag){
			tempR.PrintV(v);
	//		System.out.println();
			tempR.PrintV(tempI[i++]);
			System.out.println();
		}*/
		
		System.out.print("prepare the ann weight\n");
		double[][] w_for_ann = tempS.get_ann_wight(1); 
		ANN ann = new ANN();
		ann.InitAnn(w_for_ann,tempS.ann_bias,new TanhFunction(),new TanhFunction());
		
		Trainer tempTR=new Trainer();
		tempTR.setLearningRate(0.2);
		double[][] tempI_label = new double[tempI.length][tempI[0].length+1];
		for(int i = 0;i < tempI_label.length;++i)
		{
			for(int j = 0;j < tempI_label[0].length;++j)
			{
				if(j == tempI_label[0].length-1)
					tempI_label[i][j] = tempI_label[i][j-1];
				else
					tempI_label[i][j] = tempI[i][j];
			}
		}
		tempTR.Train(ann,tempI_label, 0.01);

	}
	
	public static List<ArrayList<Double> > read_datafile(String src)
	{
		try {
		File file = new File(src);
		if(!file.exists() || file.isDirectory())
			return null;
		List<ArrayList<Double> > ret = new ArrayList<ArrayList<Double>>();
		
		FileInputStream input = new FileInputStream(file);
		InputStreamReader inReader = new InputStreamReader(input);
		BufferedReader reader = new BufferedReader(inReader);
		String line;
		while((line = reader.readLine()) != null)
		{
			String[] line_data = line.split("\t");
			ArrayList<Double> cont = new ArrayList<Double>();
			for(String s:line_data)
				cont.add(Double.parseDouble(s));
			ret.add(cont);
			//System.out.print(ret.size()+"\n");
		}
		return ret;
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
